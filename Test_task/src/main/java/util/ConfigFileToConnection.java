package util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileToConnection {
    private static final String NAME_FILE = "src/main/resources/application.properties";

    private Properties properties;
    private static ConfigFileToConnection configToConnection;

    private ConfigFileToConnection() {
    }

    public void setConfig(String nameUser, String passwordUser, String url, String poolSize) {
        properties = new Properties();
        properties.setProperty("db.username", nameUser);
        properties.setProperty("db.password", passwordUser);
        properties.setProperty("db.url", url);
        properties.setProperty("db.pool.connection.size", poolSize);
        storeToFile();
    }

    private void storeToFile() {
        try (FileWriter fileWriter = new FileWriter(NAME_FILE)) {
            properties.store(fileWriter, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigFileToConnection getConfigFileToConnection() {
        if (configToConnection == null) {
            configToConnection = new ConfigFileToConnection();
        }
        return configToConnection;
    }
}