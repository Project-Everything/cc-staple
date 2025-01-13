package net.cc.staple;

import net.cc.staple.command.*;
import net.cc.staple.command.PingCommand;
import net.cc.staple.command.RespawnCommand;
import net.cc.staple.listener.PlayerListener;
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
    public static String serverName;

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
        serverName = getConfig().getString("server");
        StapleConfig.load(this);

        try {
            setupStorage();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        setupCommands();
        setupListeners();
        setupManagers();
    }

    @Override
    public void onDisable() {
        instance = null;
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

    private void setupStorage() throws SQLException, ClassNotFoundException {
        String url = StapleConfig.getMySqlUrl();
        String username = StapleConfig.getMySqlUser();
        String password = StapleConfig.getMySqlPassword();
        storage = new MySQLManager(url, username, password);
    }

    private void setupManagers() {
        playerManager = new PlayerManager();
        tpaManager = new TpaManager();
    }

    private void setupListeners() {
        new PlayerListener();
    }

    private void setupCommands() {
        LifecycleEventManager<Plugin> lifecycleEventManager = getLifecycleManager();
        lifecycleEventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            new BackCommand(commands);
            new BroadcastCommand(commands);
            new EnderChestCommand(commands);
            new GamemodeCommand(commands);
            new HatCommand(commands);
            new HelpCommand(commands);
            new ItemCommand(commands);
            new PingCommand(commands);
            new PlayerTimeCommand(commands);
            new RespawnCommand(commands);
            new RulesCommand(commands);
            new SpeedCommand(commands);
            new TeleportCommand(commands);
            new TopCommand(commands);
            new TpaCommand(commands);
            new TpToggleCommand(commands);
            new VoteCommand(commands);

            boolean plotSquared = getServer().getPluginManager().isPluginEnabled("PlotSquared");

            if (plotSquared) {
                new SpawnCommand(commands);
            }
        });
    }
}
