package net.cc.staple;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.cc.staple.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"UnstableApiUsage", "unused"})

public final class StaplePluginBootstrap implements PluginBootstrap {

    private StaplePlugin plugin;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            new BackCommand(plugin, commands);
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
            new TeleportCommand(plugin, commands);
            new TopCommand(commands);
            new TpaCommand(plugin, commands);
            new TpToggleCommand(plugin, commands);
            new VoteCommand(commands);

            if (StaplePlugin.serverName.equals("plots")) {
                new SpawnCommand(plugin, commands);
            }
        });
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        plugin = new StaplePlugin();
        return plugin;
    }
}
