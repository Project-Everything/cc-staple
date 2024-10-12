package com.spektrsoyuz.staple;

import com.spektrsoyuz.staple.command.*;
import com.spektrsoyuz.staple.listener.PlayerJoinListener;
import com.spektrsoyuz.staple.listener.PlayerQuitListener;
import com.spektrsoyuz.staple.player.PlayerManager;
import com.spektrsoyuz.staple.storage.impl.MySQLManager;
import com.spektrsoyuz.staple.storage.Storage;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class StaplePlugin extends JavaPlugin {

    private static StaplePlugin instance;
    private Storage storage;
    private PlayerManager playerManager;

    public static StaplePlugin getInstance() {
        return instance;
    }

    public Storage getStorage() {
        return storage;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Read MySQL login credentials from config.yml
        String mysqlHost = getConfig().getString("mysql.host");
        int mysqlPort = getConfig().getInt("mysql.port");
        String mysqlDatabase = getConfig().getString("mysql.database");
        String mysqlUsername = getConfig().getString("mysql.username");
        String mysqlPassword = getConfig().getString("mysql.password");

        // Instantiate MySQLManager using HikariCP
        try {
            storage = new MySQLManager(mysqlHost, mysqlPort, mysqlDatabase, mysqlUsername, mysqlPassword);
            getLogger().info("MySQL Database loaded.");
        } catch (SQLException e) {
            getLogger().severe("SQL Exception while initializing SQL Manager: " + e.getMessage());
            getLogger().severe("MySQL Database did not load, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        registerHooks();
        registerListeners();
        registerManagers();
    }

    private void registerCommands() {
        LifecycleEventManager<Plugin> lifecycleEventManager = getLifecycleManager();
        lifecycleEventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            commands.register("broadcast", "Broadcast a message", new BroadcastCommand());
            commands.register("enderchest", "View your ender chest", List.of("chest", "ec"), new EnderChestCommand());
            commands.register("gamemode", "Switch your gamemode", List.of("gm"), new GamemodeCommand());
            commands.register("gma", "Set your gamemode to adventure", new GMACommand());
            commands.register("gmc", "Set your gamemode to creative", new GMCCommand());
            commands.register("gms", "Set your gamemode to survival", new GMSCommand());
            commands.register("gmsp", "Set your gamemode to spectator", new GMSPCommand());
            commands.register("help", "View the help message", new HelpCommand());
            commands.register("rules", "View the rules message", new RulesCommand());
            commands.register("teleport", "Teleport to a player", List.of("tp"), new TeleportCommand());
            commands.register("teleporthere", "Teleport a player to your location", List.of("tphere"), new TeleportHereCommand());
        });
    }

    private void registerHooks() {

    }

    private void registerListeners() {
        new PlayerJoinListener();
        new PlayerQuitListener();
    }

    private void registerManagers() {
        playerManager = new PlayerManager();
    }

    @Override
    public void onDisable() {
        playerManager.saveAll();

        instance = null;
    }
}
