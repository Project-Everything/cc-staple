package net.cc.staple.storage.impl;

import net.cc.staple.StaplePlugin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySQLManager extends SQLStorage {

    private final HikariDataSource dataSource;

    public MySQLManager(String url, String username, String password) throws ClassNotFoundException {
        final StaplePlugin staplePlugin = StaplePlugin.getInstance();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setPoolName("cc-staple-pool");
        Class.forName("com.mysql.cj.jdbc.Driver");
        staplePlugin.getLogger().info("jdbc url: " + url);
        staplePlugin.getLogger().info("jdbc username: " + username);
        staplePlugin.getLogger().info("jdbc password: " + password);

        dataSource = new HikariDataSource(config);
        createTables();
    }

    @Override
    public Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
