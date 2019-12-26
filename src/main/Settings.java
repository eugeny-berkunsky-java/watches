package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Settings {
    private final static String url = "jdbc:postgresql://localhost:5432/watches";
    private final static Properties properties = new Properties();

    private static Connection connection;

    static {
        properties.setProperty("user", "watches");
        properties.setProperty("password", "watches");

    }


   /* public static String getConnectionURL() {
        return "jdbc:postgresql://localhost:5432/watches";
    }

    public static Properties getConnectionProperties() {
        return properties;
    }*/

    public static Connection getConnection() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(url, properties);
            } else if (!connection.isValid(60)) {
                connection.close();
                connection = DriverManager.getConnection(url, properties);
            }

            return connection;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

    public static void shutDown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        }
    }

}
