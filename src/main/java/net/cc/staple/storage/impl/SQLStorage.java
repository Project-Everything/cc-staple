package net.cc.staple.storage.impl;

import net.cc.staple.StaplePlugin;
import net.cc.staple.player.StaplePlayer;
import net.cc.staple.storage.query.StaplePlayerQuery;
import net.cc.staple.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class SQLStorage extends Storage {

    private final StaplePlugin staplePlugin;

    public SQLStorage() {
        this.staplePlugin = StaplePlugin.getInstance();
    }

    protected void createTables() {
        try (Connection connection = createConnection()) {
            Statement statement = connection.createStatement();
            statement.addBatch("CREATE TABLE IF NOT EXISTS " + Table.STAPLE_PLAYERS + " (id VARCHAR(36) PRIMARY KEY, tp_disabled BOOLEAN, old_location_x DOUBLE, old_location_y DOUBLE, old_location_z DOUBLE);");
            statement.executeBatch();
        } catch (SQLException e) {
            staplePlugin.getLogger().severe("SQL Exception while creating tables: " + e.getMessage());
        }
    }

    /**
     * savePlayer - saves a player to the database
     *
     * @param staplePlayer instance of StaplePlayer
     * @return CompletableFuture
     */
    @Override
    public CompletableFuture<Void> savePlayer(StaplePlayer staplePlayer) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = createConnection()) {
                String sql = "INSERT INTO " + Table.STAPLE_PLAYERS + " (id, tp_disabled, old_location_x, old_location_y, old_location_z) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE tp_disabled = ?, old_location_x = ?, old_location_y = ?, old_location_z = ?;";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, staplePlayer.getMojangId().toString());
                statement.setBoolean(2, staplePlayer.isTpDisabled());
                statement.setDouble(3, staplePlayer.getOldLocation().getX());
                statement.setDouble(4, staplePlayer.getOldLocation().getY());
                statement.setDouble(5, staplePlayer.getOldLocation().getZ());
                statement.setBoolean(6, staplePlayer.isTpDisabled());
                statement.setDouble(7, staplePlayer.getOldLocation().getX());
                statement.setDouble(8, staplePlayer.getOldLocation().getY());
                statement.setDouble(9, staplePlayer.getOldLocation().getZ());
                statement.executeUpdate();
            } catch (SQLException e) {
                staplePlugin.getLogger().severe("SQL Exception while saving instance of StaplePlayer: " + e.getMessage());
            }
        });
    }

    /**
     * queryPlayer - queries a player from the database
     *
     * @param mojangId player unique id
     * @return CompletableFuture
     */
    @Override
    public CompletableFuture<StaplePlayerQuery> queryPlayer(UUID mojangId) {
        return CompletableFuture.supplyAsync(() -> {
            StaplePlayerQuery query = new StaplePlayerQuery();
            try (Connection connection = createConnection()) {
                String sql = "SELECT * FROM " + Table.STAPLE_PLAYERS + " WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, mojangId.toString());
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    boolean isTpDisabled = resultSet.getBoolean("tp_disabled");
                    double prevPositionX = resultSet.getDouble("old_location_x");
                    double prevPositionY = resultSet.getDouble("old_location_y");
                    double prevPositionZ = resultSet.getDouble("old_location_z");

                    Player player = Bukkit.getPlayer(mojangId);
                    if (player != null) {
                        Location location = new Location(player.getWorld(), prevPositionX, prevPositionY, prevPositionZ, player.getPitch(), player.getYaw());
                        StaplePlayer staplePlayer = new StaplePlayer(mojangId, isTpDisabled, location);
                        query.addResult(staplePlayer);
                    }
                }
            } catch (SQLException e) {
                staplePlugin.getLogger().severe("SQL Exception while querying instance of StaplePlayer: " + e.getMessage());
            }
            return query;
        });
    }

    /**
     * queryPlayers - queries a player from the database
     *
     * @return CompletableFuture
     */
    @Override
    public CompletableFuture<StaplePlayerQuery> queryPlayers() {
        return CompletableFuture.supplyAsync(() -> {
            StaplePlayerQuery query = new StaplePlayerQuery();
            try (Connection connection = createConnection()) {
                String sql = "SELECT * FROM " + Table.STAPLE_PLAYERS + ";";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    UUID mojangId = UUID.fromString(resultSet.getString("id"));
                    boolean isTpDisabled = resultSet.getBoolean("tp_disabled");
                    double prevPositionX = resultSet.getDouble("old_location_x");
                    double prevPositionY = resultSet.getDouble("old_location_y");
                    double prevPositionZ = resultSet.getDouble("old_location_z");

                    Player player = Bukkit.getPlayer(mojangId);
                    if (player != null) {
                        Location location = new Location(player.getWorld(), prevPositionX, prevPositionY, prevPositionZ, player.getPitch(), player.getYaw());
                        StaplePlayer staplePlayer = new StaplePlayer(mojangId, isTpDisabled, location);
                        query.addResult(staplePlayer);
                    }
                }
            } catch (SQLException e) {
                staplePlugin.getLogger().severe("SQL Exception while querying all instances of StaplePlayer: " + e.getMessage());
            }
            return query;
        });
    }

    public abstract Connection createConnection() throws SQLException;
}
