package model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CountryDAO {
    private static final String url = "jdbc:postgresql://localhost:5432/watches";
    private Properties properties;

    public CountryDAO() {
        properties = new Properties();
        properties.setProperty("user", "watches");
        properties.setProperty("password", "watches");
    }

    public Country create(String name) {
        final String sql = "insert into public.country (name) values (?) ";

        if (name == null || name.trim().length() == 0) {
            return null;
        }

        try (Connection connection = DriverManager.getConnection(url, properties)) {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.executeUpdate(sql);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt("id");
                return getById(id);
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public List<Country> getAll() {
        List<Country> result = new ArrayList<>();
        final String query = "select id, name from public.country";

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
        final String query = "select id, name from public.country where id = ?;";

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


    public void delete(Country country) {
        throw new NotImplementedException();
    }
}
