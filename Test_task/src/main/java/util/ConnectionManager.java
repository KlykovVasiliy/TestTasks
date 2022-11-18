package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource source;

    static {
        config.setJdbcUrl(PropertiesUtil.get(URL_KEY));
        config.setUsername(PropertiesUtil.get(USERNAME_KEY));
        config.setPassword(PropertiesUtil.get(PASSWORD_KEY));
        config.setMaximumPoolSize(POOL_SIZE);
        source = new HikariDataSource(config);
    }

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}