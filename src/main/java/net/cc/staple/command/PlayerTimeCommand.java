package net.cc.staple.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings({"UnstableApiUsage"})

public final class PlayerTimeCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        // Check if sender is not player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("The console cannot use this command.").color(NamedTextColor.RED));
            return;
        }

        // Check if player entered no arguments
        if (args.length == 0) {
            player.sendMessage(Component.text("Usage: /playertime <day/night/morning/evening/off>").color(NamedTextColor.GRAY));
            return;
        }

        switch (args[0]) {
            case "morning":
                player.sendMessage(Component.text("Time set to morning.").color(NamedTextColor.GOLD));
                player.setPlayerTime(0, false);
                break;
            case "day":
                player.sendMessage(Component.text("Time set to day.").color(NamedTextColor.GOLD));
                player.setPlayerTime(6000, false);
                break;
            case "evening":
                player.sendMessage(Component.text("Time set to evening.").color(NamedTextColor.GOLD));
                player.setPlayerTime(12000, false);
                break;
            case "night":
                player.sendMessage(Component.text("Time set to night.").color(NamedTextColor.GOLD));
                player.setPlayerTime(18000, false);
                break;
            case "off":
                player.sendMessage(Component.text("Disabled player time.").color(NamedTextColor.GOLD));
                player.setPlayerTime(Objects.requireNonNull(Bukkit.getWorld("plotworld")).getTime(), true);
                break;
            default:
                try {
                    long time = Long.parseLong(args[0]);
                    player.sendMessage(Component.text("Time set to " + time).color(NamedTextColor.GOLD));
                    player.setPlayerTime(time, true);
                } catch (NumberFormatException e) {
                    player.sendMessage(Component.text("Invalid time format.").color(NamedTextColor.RED));
                }
                break;
        }
    }

    @Override
    public @NotNull String permission() {
        return "cc.command.playertime";
    }
}
