package com.spektrsoyuz.staple;

import com.spektrsoyuz.staple.command.BroadcastCommand;
import com.spektrsoyuz.staple.command.EnderChestCommand;
import com.spektrsoyuz.staple.command.GamemodeCommand;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class StaplePlugin extends JavaPlugin {

    private static StaplePlugin instance;

    public StaplePlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        registerCommands();
        registerHooks();
        registerListeners();
        registerManagers();
    }

    private void registerCommands() {
        LifecycleEventManager<Plugin> lifecycleEventManager = getLifecycleManager();
        lifecycleEventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            commands.register("enderchest", "View your ender chest", List.of("chest", "ec"), new EnderChestCommand());
            commands.register("gamemode", "Switch your gamemode", List.of("gm"), new GamemodeCommand());
            commands.register("broadcast", "Broadcast a message", new BroadcastCommand());
        });
    }

    private void registerHooks() {

    }

    private void registerListeners() {

    }

    private void registerManagers() {

    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
