package utils;

import main.Main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Settings {
    private final static String url = "jdbc:postgresql://localhost:5432/watches";
    private final static Properties properties = new Properties();

    private static Connection connection;
    private static Logger logger;

    static {
        properties.setProperty("user", "watches");
        properties.setProperty("password", "watches");

        logger = Logger.getLogger(Settings.class.getName());

    }

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
            logger.log(Level.SEVERE, "error retrieving connection", e);
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        // init logger
        try (final InputStream inputStream = Files.newInputStream(Paths.get("logging.properties"))) {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            //todo: add default behaviour
        }
        logger.log(Level.INFO, "program started");
    }

    public static void shutDown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "error closing DB connection", e);
            }
        }

        Logger.getLogger(Main.class.getName()).log(Level.INFO, "program stopped");
    }
}
