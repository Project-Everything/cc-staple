package net.cc.staple;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class StapleConfig {

    /* Spawn options */
    private static double spawnX;
    private static double spawnY;
    private static double spawnZ;
    private static float spawnYaw;
    private static float spawnPitch;

    public static void load(StaplePlugin plugin) {
        try {

            /* Spawn options */
            spawnX = plugin.getConfig().getDouble("spawn.x");
            spawnY = plugin.getConfig().getDouble("spawn.y");
            spawnZ = plugin.getConfig().getDouble("spawn.z");
            spawnYaw = (float) plugin.getConfig().getDouble("spawn.yaw");
            spawnPitch = (float) plugin.getConfig().getDouble("spawn.pitch");
        } catch (NullPointerException e) {
            plugin.getLogger().severe("Configuration did not load properly, disabling plugin!");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    public static void save(StaplePlugin plugin) {
        plugin.getConfig().set("spawn.x", spawnX);
        plugin.getConfig().set("spawn.y", spawnY);
        plugin.getConfig().set("spawn.z", spawnZ);
        plugin.getConfig().set("spawn.yaw", spawnYaw);
        plugin.getConfig().set("spawn.pitch", spawnPitch);
        plugin.saveConfig();
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
