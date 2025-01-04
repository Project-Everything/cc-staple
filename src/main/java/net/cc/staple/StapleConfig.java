package net.cc.staple;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class StapleConfig {

    /* SQL Storage options */
    private static String mySqlUrl;
    private static String mySqlUser;
    private static String mySqlPassword;

    /* Spawn options */
    private static double spawnX;
    private static double spawnY;
    private static double spawnZ;
    private static float spawnYaw;
    private static float spawnPitch;

    public static void load(StaplePlugin staplePlugin) {
        try {
            /* SQL Storage options */
            mySqlUrl = staplePlugin.getConfig().getString("mysql.url");
            mySqlUser = staplePlugin.getConfig().getString("mysql.username");
            mySqlPassword = staplePlugin.getConfig().getString("mysql.password");

            /* Spawn options */
            spawnX = staplePlugin.getConfig().getDouble("spawn.x");
            spawnY = staplePlugin.getConfig().getDouble("spawn.y");
            spawnZ = staplePlugin.getConfig().getDouble("spawn.z");
            spawnYaw = (float) staplePlugin.getConfig().getDouble("spawn.yaw");
            spawnPitch = (float) staplePlugin.getConfig().getDouble("spawn.pitch");
        } catch (NullPointerException e) {
            staplePlugin.getLogger().severe("Configuration did not load properly, disabling plugin!");
            Bukkit.getPluginManager().disablePlugin(staplePlugin);
        }
    }

    public static void save(StaplePlugin staplePlugin) {
        staplePlugin.getConfig().set("mysql.url", mySqlUrl);
        staplePlugin.getConfig().set("mysql.username", mySqlUser);
        staplePlugin.getConfig().set("mysql.password", mySqlPassword);
        staplePlugin.getConfig().set("spawn.x", spawnX);
        staplePlugin.getConfig().set("spawn.y", spawnY);
        staplePlugin.getConfig().set("spawn.z", spawnZ);
        staplePlugin.getConfig().set("spawn.yaw", spawnYaw);
        staplePlugin.getConfig().set("spawn.pitch", spawnPitch);
        staplePlugin.saveConfig();
    }

    public static String getMySqlUrl() {
        return mySqlUrl;
    }

    public static String getMySqlUser() {
        return mySqlUser;
    }

    public static String getMySqlPassword() {
        return mySqlPassword;
    }

    public static Location getSpawnLocation(World world) {
        return new Location(world, spawnX, spawnY, spawnZ, spawnYaw, spawnPitch);
    }

    public static void setSpawnLocation(Location location) {
        spawnX = location.getX();
        spawnY = location.getY();
        spawnZ = location.getZ();
        spawnYaw = location.getYaw();
        spawnPitch = location.getPitch();
    }
}
