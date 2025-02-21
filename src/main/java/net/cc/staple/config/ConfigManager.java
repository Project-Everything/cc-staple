package net.cc.staple.config;

import net.cc.staple.StaplePlugin;
import org.bukkit.Location;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class ConfigManager {

    private static final String CONFIG = "config.conf";
    private final StaplePlugin plugin;
    private final File dataFolder;
    private final Logger logger;
    private CommentedConfigurationNode root;

    // Constructor
    public ConfigManager(final StaplePlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        this.logger = plugin.getLogger();
    }

    // Method to create and load config file
    public void init() {
        File configFile = new File(dataFolder, CONFIG);
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(configFile.toPath())
                .build();

        if (!configFile.exists()) {
            plugin.saveResource(CONFIG, false); // save config file if missing
            logger.info(String.format("Created %s", CONFIG));
        }

        try {
            root = loader.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get the server name from the config
    public String getServerName() {
        final ConfigurationNode node = root.node("server");
        return node.getString("default");
    }

    // Method to get the Spawn Location from the config
    public Location getSpawnLocation() {
        final ConfigurationNode node = root.node("settings", "spawn-location");
        final String spawnString = node.getString();

        if (spawnString != null) {
            final String[] stringArgs = spawnString.split(",");

            final Map<String, Object> locationMap = new HashMap<>();
            locationMap.put("world", stringArgs[0]);
            locationMap.put("x", stringArgs[1]);
            locationMap.put("y", stringArgs[2]);
            locationMap.put("z", stringArgs[3]);
            locationMap.put("yaw", stringArgs[4]);
            locationMap.put("pitch", stringArgs[5]);

            return Location.deserialize(locationMap);
        }
        return null;
    }

    // Method to save the Spawn Location to the config
    public void setSpawnLocation(final Location location) {
        final File configFile = new File(dataFolder, CONFIG);
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().path(configFile.toPath()).build();
        final ConfigurationNode node = root.node("settings", "spawn-location");

        final Map<String, Object> locationMap = location.serialize();
        final String spawnString = locationMap.values().stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        try {
            node.set(spawnString);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }

        try {
            loader.save(root);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get the database settings from the config
    public DatabaseSettings getDatabaseSettings() {
        ConfigurationNode node = root.node("database");
        try {
            return node.get(DatabaseSettings.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }
}
