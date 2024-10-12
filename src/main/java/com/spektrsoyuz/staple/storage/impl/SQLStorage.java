package com.spektrsoyuz.staple.storage.impl;

import com.spektrsoyuz.staple.StaplePlugin;
import com.spektrsoyuz.staple.player.StaplePlayer;
import com.spektrsoyuz.staple.storage.query.StaplePlayerQuery;
import com.spektrsoyuz.staple.storage.Storage;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class SQLStorage extends Storage {

    private final StaplePlugin plugin;

    public SQLStorage() {
        this.plugin = StaplePlugin.getInstance();
    }

    protected void createTables() {
        try (Connection connection = createConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.addBatch("CREATE TABLE IF NOT EXISTS " + Table.STAPLE_PLAYERS + " (id VARCHAR(36) PRIMARY KEY, tp_disabled BOOLEAN);");
                statement.executeBatch();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("SQL Exception while creating tables: " + e.getMessage());
        }
    }

    /**
     * addPlayer - adds a player to the database
     *
     * @param staplePlayer instance of StaplePlayer
     * @return CompletableFuture
     */
    @Override
    public CompletableFuture<Void> addPlayer(StaplePlayer staplePlayer) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = createConnection()) {
                String sql = "INSERT INTO " + Table.STAPLE_PLAYERS + " (id, tp_disabled) VALUES (?, ?)";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, staplePlayer.getPlayerId().toString());
                    statement.setBoolean(2, staplePlayer.isTpDisabled());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("SQL Exception while adding player: " + e.getMessage());
            }
        });
    }

    /**
     * addPlayer - saves a player to the database
     *
     * @param staplePlayer instance of StaplePlayer
     * @return CompletableFuture
     */
    @Override
    public CompletableFuture<Void> savePlayer(StaplePlayer staplePlayer) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = createConnection()) {
                String sql = "UPDATE " + Table.STAPLE_PLAYERS + " SET tp_disabled = ? WHERE id = ?";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setBoolean(1, staplePlayer.isTpDisabled());
                    statement.setString(2, staplePlayer.getPlayerId().toString());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("SQL Exception while saving player: " + e.getMessage());
            }
        });
    }

    /**
     * queryPlayer - queries a player from the database
     *
     * @param playerId player unique id
     * @return CompletableFuture
     */
    @Override
    public CompletableFuture<StaplePlayerQuery> queryPlayer(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            StaplePlayerQuery query = new StaplePlayerQuery();

            try (Connection connection = createConnection()) {
                String sql = "SELECT * FROM " + Table.STAPLE_PLAYERS + " WHERE id = ?";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, playerId.toString());
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            boolean isTpDisabled = resultSet.getBoolean("tp_disabled");

                            StaplePlayer staplePlayer = new StaplePlayer(playerId, isTpDisabled);
                            query.addResult(staplePlayer);
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("SQL Exception while querying player: " + e.getMessage());
            }
            return query;
        });
    }

    public abstract Connection createConnection() throws SQLException;
}
