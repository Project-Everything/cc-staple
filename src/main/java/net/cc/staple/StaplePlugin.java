package net.cc.staple;

import net.cc.staple.command.*;
import net.cc.staple.command.PingCommand;
import net.cc.staple.command.RespawnCommand;
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

@SuppressWarnings("UnstableApiUsage")

public final class StaplePlugin extends JavaPlugin {

    private static StaplePlugin instance;
    private Storage storage;
    private PlayerManager playerManager;
    private TpaManager tpaManager;

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        StapleConfig.load(this);

        // Load database
        String mysqlUrl = StapleConfig.getMySqlUrl();
        String mysqlUsername = StapleConfig.getMySqlUser();
        String mysqlPassword = StapleConfig.getMySqlPassword();

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

    private void registerCommands() {
        LifecycleEventManager<Plugin> lifecycleEventManager = getLifecycleManager();
        lifecycleEventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            new BroadcastCommand(commands);
            new EnderChestCommand(commands);
            new GamemodeCommand(commands);
            new HelpCommand(commands);
            new ItemCommand(commands);
            new PingCommand(commands);
            new PlayerTimeCommand(commands);
            new RespawnCommand(commands);
            new RulesCommand(commands);
            new SpeedCommand(commands);
            new SpawnCommand(commands);
            new TeleportCommand(commands);
            new TopCommand(commands);
            new TpaCommand(commands);
            new TpToggleCommand(commands);
            new VoteCommand(commands);
        });
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
