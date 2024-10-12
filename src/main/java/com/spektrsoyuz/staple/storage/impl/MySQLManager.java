package com.spektrsoyuz.staple.storage.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySQLManager extends SQLStorage {

    private final HikariDataSource dataSource;

    public MySQLManager(String host, int port, String database, String username, String password) throws SQLException {
        HikariConfig config = new HikariConfig();
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setPoolName("amethyst-pool");

        dataSource = new HikariDataSource(config);
        createTables();
    }

    @Override
    public Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
