package com.spektrsoyuz.staple.command;

import com.spektrsoyuz.staple.util.StapleColor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("UnstableApiUsage")

public final class TeleportCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        // Check if sender is not player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("The console cannot use this command.").color(StapleColor.RED));
            return;
        }

        // Check if player entered no arguments
        if (args.length == 0) {
            player.sendMessage(Component.text("Usage: /teleport <player>").color(StapleColor.GRAY));
            return;
        }

        // Attempt to find target player
        Collection<Player> targets = Bukkit.matchPlayer(args[0]);
        if (targets.isEmpty()) {
            player.sendMessage(Component.text("Player not found").color(StapleColor.RED));
            return;
        }

        Player targetPlayer = targets.iterator().next();

        // Check if player specified a second target
        if (args.length == 1) {
            if (player.getUniqueId().equals(targetPlayer.getUniqueId())) {
                player.sendMessage(Component.text("You cannot teleport to yourself!").color(StapleColor.RED));
            } else {
                player.teleport(targetPlayer);
                player.sendMessage(Component.text("Teleported to " + targetPlayer.getName()).color(StapleColor.GOLD));
            }
        } else {
            Collection<Player> targetLocations = Bukkit.matchPlayer(args[1]);

            // Attempt to find target location
            if (targetLocations.isEmpty()) {
                player.sendMessage(Component.text("Target Player not found").color(StapleColor.RED));
                return;
            }

            Player targetLocation = targetLocations.iterator().next();
            if (targetPlayer.getUniqueId().equals(targetLocation.getUniqueId())) {
                player.sendMessage(Component.text("You cannot teleport a player to themselves!").color(StapleColor.RED));
            } else {
                targetPlayer.teleport(targetLocation);
                player.sendMessage(Component.text("Teleported " + targetPlayer.getName() + " to " + targetLocation.getName()).color(StapleColor.GOLD));
            }
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        Collection<String> suggestions = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            suggestions.add(player.getName());
        }
        return suggestions;
    }

    @Override
    public @NotNull String permission() {
        return "staple.teleport";
    }
}
