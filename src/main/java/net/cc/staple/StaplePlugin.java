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

@SuppressWarnings("UnstableApiUsage")

public final class StaplePlugin extends JavaPlugin {

    public static String serverName;

    private Storage storage;
    private PlayerManager playerManager;
    private TpaManager tpaManager;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        serverName = getConfig().getString("server");
        StapleConfig.load(this);

        setupStorage();
        if (this.storage == null) {
            getLogger().severe("Storage was not initialized correctly. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupCommands();
        setupListeners();
        setupManagers();
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

    private void setupStorage() {
        try {
            String url = getConfig().getString("mysql.url");
            String username = getConfig().getString("mysql.username");
            String password = getConfig().getString("mysql.password");
            this.storage = new MySQLManager(this, url, username, password);
        } catch (ClassNotFoundException e) {
            getLogger().severe("Error while setting up SQL storage: " + e.getMessage());
        }
    }

    private void setupManagers() {
        playerManager = new PlayerManager(this);
        tpaManager = new TpaManager(this);
    }

    private void setupListeners() {
        new PlayerListener(this);
    }

    private void setupCommands() {
        LifecycleEventManager<Plugin> lifecycleEventManager = getLifecycleManager();
        lifecycleEventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            new BackCommand(this, commands);
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
            new TeleportCommand(this, commands);
            new TopCommand(commands);
            new TpaCommand(this, commands);
            new TpToggleCommand(this, commands);
            new VoteCommand(commands);

            boolean plotSquared = getServer().getPluginManager().isPluginEnabled("PlotSquared");

            if (plotSquared) {
                new SpawnCommand(this, commands);
            }
        });
    }
}
