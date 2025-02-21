package net.cc.staple.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.cc.staple.StaplePlugin;
import net.cc.staple.config.ConfigManager;
import net.cc.staple.config.DatabaseSettings;
import net.cc.staple.player.StaplePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class DatabaseManager {

    public static final String STAPLE_PLAYERS = "staple_players";
    private final ConfigManager config;
    private final Logger logger;
    private HikariDataSource dataSource;

    public DatabaseManager(final StaplePlugin plugin) {
        this.config = plugin.getConfigManager();
        this.logger = plugin.getLogger();

        init();
        createTables();
    }

    // Method to create the HikariCP data source (connection to SQL database)
    public void init() {
        DatabaseSettings settings = config.getDatabaseSettings();
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://" + settings.getHost() + ":" + settings.getPort() + "/" + settings.getDatabase());
        hikariConfig.setUsername(settings.getUsername());
        hikariConfig.setPassword(settings.getPassword());
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        hikariConfig.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(hikariConfig);
    }

    protected void createTables() {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.addBatch("CREATE TABLE IF NOT EXISTS " + STAPLE_PLAYERS + " (id VARCHAR(36) PRIMARY KEY, tp_disabled BOOLEAN, old_location_x DOUBLE, old_location_y DOUBLE, old_location_z DOUBLE);");
            statement.executeBatch();
        } catch (SQLException e) {
            logger.severe("SQL Exception while creating tables: " + e.getMessage());
        }
    }

    /**
     * savePlayer - saves a player to the database
     *
     * @param staplePlayer instance of StaplePlayer
     */
    public void savePlayer(StaplePlayer staplePlayer) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection()) {
                String sql = "INSERT INTO " + STAPLE_PLAYERS + " (id, tp_disabled, old_location_x, old_location_y, old_location_z) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE tp_disabled = ?, old_location_x = ?, old_location_y = ?, old_location_z = ?;";
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
                logger.severe("SQL Exception while saving instance of StaplePlayer: " + e.getMessage());
            }
        });
    }

    /**
     * queryPlayer - queries a player from the database
     *
     * @param mojangId player unique id
     * @return CompletableFuture for StaplePlayer
     */
    public CompletableFuture<StaplePlayer> queryPlayer(UUID mojangId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                String sql = "SELECT * FROM " + STAPLE_PLAYERS + " WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, mojangId.toString());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return getStaplePlayerResult(resultSet);
                }
            } catch (SQLException e) {
                logger.severe("SQL Exception while querying instance of StaplePlayer: " + e.getMessage());
            }
            return null;
        });
    }

    /**
     * queryPlayers - queries a player from the database
     *
     * @return CompletableFuture for Set of StaplePlayer
     */
    public CompletableFuture<Set<StaplePlayer>> queryPlayers() {
        return CompletableFuture.supplyAsync(() -> {
            Set<StaplePlayer> staplePlayers = new HashSet<>();
            try (Connection connection = getConnection()) {
                String sql = "SELECT * FROM " + STAPLE_PLAYERS + ";";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    staplePlayers.add(getStaplePlayerResult(resultSet));
                }
                return staplePlayers;
            } catch (SQLException e) {
                logger.severe("SQL Exception while querying all instances of StaplePlayer: " + e.getMessage());
            }
            return Collections.emptySet();
        });
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private StaplePlayer getStaplePlayerResult(ResultSet resultSet) throws SQLException {
        UUID mojangId = UUID.fromString(resultSet.getString("id"));
        boolean isTpDisabled = resultSet.getBoolean("tp_disabled");
        double prevPositionX = resultSet.getDouble("old_location_x");
        double prevPositionY = resultSet.getDouble("old_location_y");
        double prevPositionZ = resultSet.getDouble("old_location_z");

        Player player = Bukkit.getPlayer(mojangId);
        if (player != null) {
            Location location = new Location(player.getWorld(), prevPositionX, prevPositionY, prevPositionZ, player.getPitch(), player.getYaw());
            return new StaplePlayer(mojangId, isTpDisabled, location);
        }
        return null;
    }
}
