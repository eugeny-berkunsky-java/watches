package model;

import main.Settings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class CountryDAO implements DAO<Country> {
    private final String url;
    private Properties properties;

    public CountryDAO() {
        url = Settings.getConnectionURL();
        properties = Settings.getConnectionProperties();
    }

    @Override
    public Country create(Country country) {

        if (country == null
                || country.getName() == null
                || country.getName().trim().length() == 0) {
            return null;
        }

        final String sql = "insert into public.\"Country\" (name) values (?) returning *";

        try (Connection connection = DriverManager.getConnection(url, properties)) {
            PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, country.getName());
            st.execute();
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                int savedId = rs.getInt("id");
                String savedName = rs.getString("name");

                return new Country(savedId, savedName);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return null;
    }

    public List<Country> getAll() {
        List<Country> result = new ArrayList<>();
        final String query = "select id, name from public.\"Country\"";

        try (Connection connection = DriverManager.getConnection(url, properties)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                result.add(new Country(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return new ArrayList<>(result);
    }

    public Country getById(int id) {
        final String query = "select id, name from public.\"Country\" where id = ?;";

        try (Connection connection = DriverManager.getConnection(url, properties)) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Country(resultSet.getInt("id"), resultSet.getString("name"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    @Override
    public boolean update(Country country) {

        if (country == null || country.getName().trim().length() == 0) {
            return false;
        }

        final String query = "update public.\"Country\" set name = ? where id = ?;";
        try (Connection connection = DriverManager.getConnection(url, properties)) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, country.getName());
            statement.setInt(2, country.getId());
            final int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return false;
    }


    public boolean delete(int id) {
        final String query = "delete from public.\"Country\" where id = ?;";
        try (final Connection connection = DriverManager.getConnection(url, properties)) {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            final int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return false;
    }
}
