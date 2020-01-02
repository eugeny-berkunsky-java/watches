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
import java.util.logging.*;

public class Settings {
    private final static String url = "jdbc:postgresql://localhost:5432/watches";
    private final static Properties properties = new Properties();

    private static Connection connection;

    static {
        properties.setProperty("user", "watches");
        properties.setProperty("password", "watches");


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
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        // init logger
        try (final InputStream inputStream = Files.newInputStream(Paths.get("logging.properties"))) {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            try {
                final Logger rootLogger = Logger.getLogger("");

                // remove other console handlers
                for (Handler handler : rootLogger.getHandlers()) {
                    if (handler.getClass() == ConsoleHandler.class) {
                        rootLogger.removeHandler(handler);
                    }
                }

                rootLogger.setLevel(Level.ALL);

                // default console handler
                final ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.SEVERE);
                consoleHandler.setFormatter(new ConsoleFormatter());
                rootLogger.addHandler(consoleHandler);

                // default file handler
                final FileHandler fileHandler = new FileHandler("watches.log", 500_000, 1, true);
                fileHandler.setLevel(Level.INFO);
                fileHandler.setFormatter(new FileFormatter());
                rootLogger.addHandler(fileHandler);

                rootLogger.log(Level.WARNING, "cant find logger settings file", e);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
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

        Logger.getLogger(Main.class.getName()).log(Level.INFO, "program stopped");
    }
}
