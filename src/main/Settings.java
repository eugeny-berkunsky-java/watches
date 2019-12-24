package main;

import java.util.Properties;

public class Settings {

    private static Properties properties = new Properties();


    static {
        properties.setProperty("user", "watches");
        properties.setProperty("password", "watches");
    }



    public static String getConnectionURL() {
        return "jdbc:postgresql://localhost:5432/watches";
    }

    public static Properties getConnectionProperties() {
        return properties;
    }
}
