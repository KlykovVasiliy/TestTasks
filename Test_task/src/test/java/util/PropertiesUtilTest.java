package util;

import junit.framework.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class PropertiesUtilTest {
    private String userName = "user";
    private String passwordUser = "password";
    private String url = "jdbc:mysql://localhost:3306/testing_db";
    private String poolSize = String.valueOf(Runtime.getRuntime().availableProcessors());

    @BeforeEach
    public void createFile() {
        ConfigFileToConnection configToConnection =
                ConfigFileToConnection.getConfigFileToConnection();
        configToConnection.setConfig(userName, passwordUser, url, poolSize);
    }

    @Test
    public void checkConfigData() {
        String userKey = "db.username";
        String passwordKey = "db.password";
        String urlKey = "db.url";
        String poolSizeKey = "db.pool.connection.size";

        Assertions.assertEquals(userName, PropertiesUtil.get(userKey));
        Assertions.assertEquals(passwordUser, PropertiesUtil.get(passwordKey));
        Assertions.assertEquals(url, PropertiesUtil.get(urlKey));
        Assertions.assertEquals(poolSize, PropertiesUtil.get(poolSizeKey));
    }
}
