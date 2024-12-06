package com.spektrsoyuz.staple.command;

import com.spektrsoyuz.staple.StaplePlugin;
import com.spektrsoyuz.staple.util.StapleColor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"UnstableApiUsage"})

public final class SpawnCommand implements BasicCommand {

    private final StaplePlugin plugin;

    public SpawnCommand() {
        this.plugin = StaplePlugin.getInstance();
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        // Check if sender is not player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("The console cannot use this command.").color(StapleColor.RED));
            return;
        }

        String worldName = player.getWorld().getName();
        Location location = new Location(Bukkit.getWorld(worldName),
                plugin.getConfig().getDouble("spawn-x"),
                plugin.getConfig().getDouble("spawn-y"),
                plugin.getConfig().getDouble("spawn-z"));
        // Teleport player to spawn
        player.teleport(location);
        player.sendMessage(Component.text("You've been teleported to spawn.").color(StapleColor.GOLD));
    }

    @Override
    public @NotNull String permission() {
        return "staple.spawn";
    }
}
