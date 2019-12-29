package model;

import manage.DBException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static main.Settings.getConnection;

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
    public Vendor create(Vendor model) throws SQLException {

        final String sql = "insert into public.\"VendorModel\" (vendor_name, country_id) " +
                "VALUES (?, ?) returning *;";

        try (final PreparedStatement st
                     = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, model.getVendorName());
            st.setInt(2, model.getCountry().getId());
            st.execute();

            final ResultSet rs = st.getGeneratedKeys();
            rs.next();
            return createFromResultSet(rs);
        }
    }

    @Override
    public List<Vendor> getAll() throws SQLException {
        final String sql = "select * from public.\"VendorModel\";";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            return executeAndReturnCollection(st, VendorDAO::createFromResultSet);
        }
    }

    @Override
    public Vendor getById(int id) throws SQLException {
        final String sql = "select * from public.\"VendorModel\" where vendor_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return executeAndReturnObject(st, VendorDAO::createFromResultSet);
        }
    }

    @Override
    public boolean update(Vendor model) throws SQLException {
        final String sql = "update public.\"VendorModel\" set vendor_name=?, country_id = ? " +
                "where vendor_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setString(1, model.getVendorName());
            st.setInt(2, model.getCountry().getId());
            st.setInt(3, model.getId());

            return st.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"VendorModel\" where vendor_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
