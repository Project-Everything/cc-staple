package net.cc.staple.util;

import net.cc.staple.StaplePlugin;
import org.bukkit.Location;
import org.bukkit.World;

public final class SpawnUtil {

    public static Location getSpawnLocation(final StaplePlugin plugin, World world) {
        double spawnX = plugin.getConfig().getDouble("spawn.x");
        double spawnY = plugin.getConfig().getDouble("spawn.y");
        double spawnZ = plugin.getConfig().getDouble("spawn.z");
        float spawnYaw = (float) plugin.getConfig().getDouble("spawn.yaw");
        float spawnPitch = (float) plugin.getConfig().getDouble("spawn.pitch");

        return new Location(world, spawnX, spawnY, spawnZ, spawnYaw, spawnPitch);
    }

    public static void setSpawnLocation(StaplePlugin plugin, Location location) {
        plugin.getConfig().set("spawn.x", location.getX());
        plugin.getConfig().set("spawn.y", location.getY());
        plugin.getConfig().set("spawn.z", location.getZ());
        plugin.getConfig().set("spawn.yaw", location.getYaw());
        plugin.getConfig().set("spawn.pitch", location.getPitch());
        plugin.saveConfig();
    }
}
