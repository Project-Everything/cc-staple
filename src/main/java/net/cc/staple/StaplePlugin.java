package net.cc.staple;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import net.cc.staple.command.*;
import net.cc.staple.config.ConfigManager;
import net.cc.staple.listener.PlayerListener;
import net.cc.staple.player.PlayerManager;
import net.cc.staple.player.TpaManager;
import net.cc.staple.storage.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@SuppressWarnings("UnstableApiUsage")
public final class StaplePlugin extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private PlayerManager playerManager;
    private TpaManager tpaManager;

    @Override
    public void onLoad() {
        configManager = new ConfigManager(this);
        configManager.init();
    }

    @Override
    public void onEnable() {
        databaseManager = new DatabaseManager(this);
        playerManager = new PlayerManager(this);
        tpaManager = new TpaManager(this);

        new PlayerListener(this);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            new BackCommand(this, commands);
            new BroadcastCommand(commands);
            new DiscordCommand(commands);
            new EnderChestCommand(commands);
            new GamemodeCommand(commands);
            new HatCommand(commands);
            new HelpCommand(commands);
            new ItemCommand(commands);
            new PingCommand(commands);
            new PlayerTimeCommand(commands);
            new RespawnCommand(commands);
            new RulesCommand(commands);
            new SpawnCommand(this, commands);
            new SpeedCommand(commands);
            new TeleportCommand(this, commands);
            new TopCommand(commands);
            new TpaCommand(this, commands);
            new TpToggleCommand(this, commands);
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}
