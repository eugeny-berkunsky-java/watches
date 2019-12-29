package model;

import manage.DBException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static main.Settings.getConnection;

class CountryDAO implements DAO<Country> {

    @Override
    public Country create(Country country) throws SQLException {

        final String sql = "insert into public.\"CountryModel\" (c_name) values (?) returning *;";

        try (final PreparedStatement st
                     = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, country.getName());
            st.execute();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            return createCountryFromResultSet(rs);
        }


    }

    public List<Country> getAll() throws SQLException {
        final String sql = "select c_id, c_name from public.\"CountryModel\";";

        try (PreparedStatement st = getConnection().prepareStatement(sql)) {
            return executeAndReturnCollection(st, this::createCountryFromResultSet);
        }
    }

    public Country getById(int id) throws SQLException {
        final String sql = "select c_id, c_name from public.\"CountryModel\" where c_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return executeAndReturnObject(st, this::createCountryFromResultSet);
        }
    }

    @Override
    public boolean update(Country country) throws SQLException {

        final String sql = "update public.\"CountryModel\" set c_name = ? where c_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setString(1, country.getName());
            st.setInt(2, country.getId());
            return st.executeUpdate() > 0;
        }
    }


    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"CountryModel\" where c_id = ?;";
        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }

    private Country createCountryFromResultSet(ResultSet rs) {
        try {
            return new Country(rs.getInt("c_id"), rs.getString("c_name"));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
