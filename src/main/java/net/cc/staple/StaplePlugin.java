package net.cc.staple;

import net.cc.staple.command.*;
import net.cc.staple.listener.PlayerJoinListener;
import net.cc.staple.listener.PlayerQuitListener;
import net.cc.staple.player.PlayerManager;
import net.cc.staple.player.TpaManager;
import net.cc.staple.storage.impl.MySQLManager;
import net.cc.staple.storage.Storage;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class StaplePlugin extends JavaPlugin {

    private static StaplePlugin instance;
    private Storage storage;
    private PlayerManager playerManager;
    private TpaManager tpaManager;

    private boolean isHubServer;

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        // Load config
        try {
            this.isHubServer = getConfig().getBoolean("is-hub-server");
        } catch (NullPointerException e) {
            getLogger().severe("Configuration is invalid!");
            getLogger().severe("Exception: " + e.getMessage());
        }

        // Load database
        String mysqlUrl = getConfig().getString("mysql.url");
        String mysqlUsername = getConfig().getString("mysql.username");
        String mysqlPassword = getConfig().getString("mysql.password");

        // Connect to database using HikariCP
        try {
            this.storage = new MySQLManager(mysqlUrl, mysqlUsername, mysqlPassword);
            getLogger().info("MySQL Database loaded.");
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().severe("SQL Exception while initializing SQL Manager: " + e.getMessage());
            getLogger().severe("MySQL Database did not load, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Setup methods
        registerCommands();
        registerHooks();
        registerListeners();
        registerManagers();
    }

    @NotNull
    public static StaplePlugin getInstance() {
        return instance;
    }

    @NotNull
    public Storage getStorage() {
        return storage;
    }

    @NotNull
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @NotNull
    public TpaManager getTpaManager() {
        return tpaManager;
    }

    public boolean isHubServer() {
        return isHubServer;
    }

    private void registerCommands() {
        LifecycleEventManager<Plugin> lifecycleEventManager = getLifecycleManager();
        lifecycleEventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            commands.register("broadcast", "Broadcast a message", List.of("bc", "say"), new BroadcastCommand());
            commands.register("enderchest", "View your ender chest", List.of("chest", "ec"), new EnderChestCommand());
            commands.register("gamemode", "Switch your gamemode", List.of("gm"), new GamemodeCommand());
            commands.register("gma", "Set your gamemode to adventure", new GMACommand());
            commands.register("gmc", "Set your gamemode to creative", new GMCCommand());
            commands.register("gms", "Set your gamemode to survival", new GMSCommand());
            commands.register("gmsp", "Set your gamemode to spectator", new GMSPCommand());
            commands.register("help", "View the help message", new HelpCommand());
            commands.register("item", "Give yourself an item", List.of("i"), new ItemCommand());
            commands.register("jump", "Jump to a location", new JumpCommand());
            commands.register("ping", "Pong!", new PingCommand());
            commands.register("playertime", "Set the player time", List.of("ptime"), new PlayerTimeCommand());
            commands.register("respawn", "Set your health to 0", new RespawnCommand());
            commands.register("rules", "View the rules message", new RulesCommand());
            commands.register("setspawn", "Set the server spawn", new SetSpawnCommand());
            commands.register("speed", "Set your player speed", new SpeedCommand());
            commands.register("teleport", "Teleport to a player", List.of("tp"), new TeleportCommand());
            commands.register("teleporthere", "Teleport a player to your location", List.of("tphere"), new TeleportHereCommand());
            commands.register("top", "Teleport to the highest block above you", new TopCommand());
            commands.register("tpaccept", "Accept a teleport request", new TpAcceptCommand());
            commands.register("tpa", "Send a teleport request", new TpaCommand());
            commands.register("tpahere", "Send a teleport request to your location", new TpaHereCommand());
            commands.register("tpcancel", "Cancel a teleport request", new TpCancelCommand());
            commands.register("tpdeny", "Deny a teleport request", new TpDenyCommand());
            commands.register("tptoggle", "Toggle receiving tp requests", new TpToggleCommand());
            commands.register("vote", "View the vote message", new VoteCommand());

            boolean plotSquared = getServer().getPluginManager().isPluginEnabled("PlotSquared");

            if (plotSquared) {
                commands.register("spawn", "Teleport to spawn", new SpawnCommand());
            }
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
        tpaManager = new TpaManager();
    }

    @Override
    public void onDisable() {
        playerManager.saveAll();

        instance = null;
    }
}
