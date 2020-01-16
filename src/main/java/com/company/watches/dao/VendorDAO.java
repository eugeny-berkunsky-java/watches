package com.company.watches.dao;

import com.company.watches.model.Vendor;
import com.company.watches.utils.DBException;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static com.company.watches.utils.Settings.getConnection;

class VendorDAO implements DAO<Vendor> {

    static Vendor createFromResultSet(ResultSet rs) {
        try {
            final int vendorId = rs.getInt("vendor_id");
            final String vendorName = rs.getString("vendor_name");

            return new Vendor(vendorId, vendorName, CountryDAO.createFromResultSet(rs));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Vendor> create(Vendor model) throws SQLException {
        final String sql = "insert into public.\"VendorModel\" (vendor_name, country_id) " +
                "VALUES (?, ?) returning *;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st
                    = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, model.getName());
            st.setInt(2, model.getCountry().getId());
            st.execute();

            final ResultSet rs = st.getGeneratedKeys();

            return rs.next() ? Optional.of(createFromResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Vendor> getAll() throws SQLException {
        final String sql = "select * from public.\"VendorModel\";";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);

            return executeAndReturnCollection(st, VendorDAO::createFromResultSet);
        }
    }

    @Override
    public Optional<Vendor> getById(int id) throws SQLException {
        final String sql = "select * from public.\"VendorModel\" where vendor_id = ?;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            return executeAndReturnObject(st, VendorDAO::createFromResultSet);
        }
    }

    @Override
    public boolean update(Vendor model) throws SQLException {
        final String sql = "update public.\"VendorModel\" set vendor_name=?, country_id = ? " +
                "where vendor_id = ?;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, model.getName());
            st.setInt(2, model.getCountry().getId());
            st.setInt(3, model.getId());

            return st.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"VendorModel\" where vendor_id = ?;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
