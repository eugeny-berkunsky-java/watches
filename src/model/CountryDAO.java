package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static main.Settings.getConnection;

class CountryDAO implements DAO<Country> {

    @Override
    public Country create(Country country) throws SQLException {

        final String sql = "insert into public.\"Country\" (name) values (?) returning *";

        try (final PreparedStatement st
                     = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, country.getName());
            st.execute();
            ResultSet rs = st.getGeneratedKeys();

            return new Country(rs.getInt("id"), rs.getString("name"));
        }
    }

    public List<Country> getAll() throws SQLException {
        final String sql = "select id, name from public.\"Country\"";

        try (Statement st = getConnection().createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            List<Country> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new Country(rs.getInt("id"), rs.getString("name")));
            }
            return new ArrayList<>(result);
        }
    }

    public Country getById(int id) throws SQLException {
        final String sql = "select id, name from public.\"Country\" where id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            return new Country(rs.getInt("id"), rs.getString("name"));
        }
    }

    @Override
    public boolean update(Country country) throws SQLException {

        final String sql = "update public.\"Country\" set name = ? where id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setString(1, country.getName());
            st.setInt(2, country.getId());
            return st.executeUpdate() > 0;
        }
    }


    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"Country\" where id = ?;";
        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
