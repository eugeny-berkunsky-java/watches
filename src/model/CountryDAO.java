package model;

import utils.DBException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static utils.Settings.getConnection;

class CountryDAO implements DAO<Country> {

    static Country createFromResultSet(ResultSet rs) {
        try {
            return new Country(rs.getInt("country_id"), rs.getString("country_name"));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Country create(Country country) throws SQLException {
        final String sql = "insert into public.\"CountryModel\" (country_name) values (?) returning *;";

        try (final PreparedStatement st
                     = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, country.getName());
            st.execute();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            return createFromResultSet(rs);
        }


    }

    public List<Country> getAll() throws SQLException {
        final String sql = "select * from public.\"CountryModel\";";

        try (PreparedStatement st = getConnection().prepareStatement(sql)) {
            return executeAndReturnCollection(st, CountryDAO::createFromResultSet);
        }
    }

    public Country getById(int id) throws SQLException {
        final String sql = "select * from public.\"CountryModel\" where country_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return executeAndReturnObject(st, CountryDAO::createFromResultSet);
        }
    }

    @Override
    public boolean update(Country country) throws SQLException {
        final String sql = "update public.\"CountryModel\" set country_name = ? where country_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setString(1, country.getName());
            st.setInt(2, country.getId());
            return st.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"CountryModel\" where country_id = ?;";
        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
