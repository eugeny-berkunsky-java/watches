package com.company.watches.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.*;

public class Settings {
    private final static String LOGGING_SETTINGS_FILE = "logging.properties";
    private final static String WATCHES_SETTINGS_FILE = "watches.properties";

    private final static Properties properties = new Properties();

    private static InitialContext context;

    public static void init() {
        // init logger
        try (final InputStream inputStream = Files.newInputStream(Paths.get(LOGGING_SETTINGS_FILE))) {
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

                rootLogger.log(Level.WARNING, "can't find logger settings file", e);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
        Thread.setDefaultUncaughtExceptionHandler(
                (t, e) -> Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "error", e)
        );

        // load properties
        try (final BufferedReader reader = Files.newBufferedReader(Paths.get(WATCHES_SETTINGS_FILE))) {
            properties.load(reader);
        } catch (IOException e) {
            Logger.getLogger("").log(Level.WARNING, "can't find program settings file", e);
        }
    }

    public static Connection getConnection() {
        try {
            if (context == null) {
                context = new InitialContext();
            }

            return ((DataSource) context.lookup("java:/comp/env/jdbc/watches")).getConnection();
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
