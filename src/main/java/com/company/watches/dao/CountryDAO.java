package com.company.watches.dao;

import com.company.watches.model.Country;
import com.company.watches.utils.DBException;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static com.company.watches.utils.Settings.getConnection;

class CountryDAO implements DAO<Country> {

    static Country createFromResultSet(ResultSet rs) {
        try {
            return new Country(rs.getInt("country_id"), rs.getString("country_name"));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Country> create(Country country) throws SQLException {
        final String sql = "insert into public.\"CountryModel\" (country_name) values (?) returning *;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, country.getName());
            st.execute();
            ResultSet rs = st.getGeneratedKeys();

            return rs.next() ? Optional.of(createFromResultSet(rs)) : Optional.empty();
        }
    }

    public List<Country> getAll() throws SQLException {
        final String sql = "select * from public.\"CountryModel\";";

        try (final Connection conn = getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            return executeAndReturnCollection(st, CountryDAO::createFromResultSet);
        }
    }

    public Optional<Country> getById(int id) throws SQLException {
        final String sql = "select * from public.\"CountryModel\" where country_id = ?;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            return executeAndReturnObject(st, CountryDAO::createFromResultSet);
        }
    }

    @Override
    public boolean update(Country country) throws SQLException {
        final String sql = "update public.\"CountryModel\" set country_name = ? where country_id = ?;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, country.getName());
            st.setInt(2, country.getId());
            return st.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"CountryModel\" where country_id = ?;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
