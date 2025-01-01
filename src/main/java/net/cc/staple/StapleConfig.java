package net.cc.staple;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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

    /* Command messages */
    private static String helpMessage;
    private static String rulesMessage;
    private static String spawnMessage;
    private static String setSpawnMessage;
    private static String voteMessage;
    private static String playerTimeMessage;
    private static String topMessage;
    private static String jumpMessage;
    private static String respawnMessage;
    private static String speedMessage;

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

            /* Command messages */
            helpMessage = staplePlugin.getConfig().getString("messages.help");
            rulesMessage = staplePlugin.getConfig().getString("messages.rules");
            spawnMessage = staplePlugin.getConfig().getString("messages.spawn");
            setSpawnMessage = staplePlugin.getConfig().getString("messages.setspawn");
            voteMessage = staplePlugin.getConfig().getString("messages.vote");
            playerTimeMessage = staplePlugin.getConfig().getString("messages.playertime");
            topMessage = staplePlugin.getConfig().getString("messages.top");
            jumpMessage = staplePlugin.getConfig().getString("messages.jump");
            respawnMessage = staplePlugin.getConfig().getString("messages.respawn");
            speedMessage = staplePlugin.getConfig().getString("messages.speed");
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
        staplePlugin.getConfig().set("messages.help", helpMessage);
        staplePlugin.getConfig().set("messages.rules", rulesMessage);
        staplePlugin.getConfig().set("messages.spawn", spawnMessage);
        staplePlugin.getConfig().set("messages.setspawn", setSpawnMessage);
        staplePlugin.getConfig().set("messages.vote", voteMessage);
        staplePlugin.getConfig().set("messages.playertime", playerTimeMessage);
        staplePlugin.getConfig().set("messages.top", topMessage);
        staplePlugin.getConfig().set("messages.jump", jumpMessage);
        staplePlugin.getConfig().set("messages.respawn", respawnMessage);
        staplePlugin.getConfig().set("messages.speed", speedMessage);
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

    public static Component getHelpMessage() {
        return MiniMessage.miniMessage().deserialize(helpMessage);
    }

    public static Component getRulesMessage() {
        return MiniMessage.miniMessage().deserialize(rulesMessage);
    }

    public static Component getSpawnMessage() {
        return MiniMessage.miniMessage().deserialize(spawnMessage);
    }

    public static Component getSetSpawnMessage() {
        return MiniMessage.miniMessage().deserialize(setSpawnMessage);
    }

    public static Component getVoteMessage() {
        return MiniMessage.miniMessage().deserialize(voteMessage);
    }

    public static Component getPlayerTimeMessage() {
        return MiniMessage.miniMessage().deserialize(playerTimeMessage);
    }

    public static Component getTopMessage() {
        return MiniMessage.miniMessage().deserialize(topMessage);
    }

    public static Component getJumpMessage() {
        return MiniMessage.miniMessage().deserialize(jumpMessage);
    }

    public static Component getRespawnMessage() {
        return MiniMessage.miniMessage().deserialize(respawnMessage);
    }

    public static Component getSpeedMessage() {
        return MiniMessage.miniMessage().deserialize(speedMessage);
    }
}
