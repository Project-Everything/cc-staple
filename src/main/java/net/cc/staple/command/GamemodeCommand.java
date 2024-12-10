package net.cc.staple.command;

import net.cc.staple.util.StapleColor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class GamemodeCommand implements BasicCommand {

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
            player.sendMessage(Component.text("Usage: /gamemode <gamemode>").color(StapleColor.GRAY));
            return;
        }

        String gameModeString = args[0];
        GameMode gameMode;

        // Set gameMode based on gameModeString
        switch (gameModeString) {
            case "creative":
                gameMode = GameMode.CREATIVE;
                player.sendMessage(Component.text("Gamemode set to Creative").color(StapleColor.GOLD));
                break;
            case "survival":
                gameMode = GameMode.SURVIVAL;
                player.sendMessage(Component.text("Gamemode set to Survival").color(StapleColor.GOLD));
                break;
            case "adventure":
                gameMode = GameMode.ADVENTURE;
                player.sendMessage(Component.text("Gamemode set to Adventure").color(StapleColor.GOLD));
                break;
            case "spectator":
                gameMode = GameMode.SPECTATOR;
                player.sendMessage(Component.text("Gamemode set to Spectator").color(StapleColor.GOLD));
                break;
            default:
                player.sendMessage(Component.text("Unknown gamemode \"" + gameModeString + "\"").color(StapleColor.RED));
                return;
        }

        player.setGameMode(gameMode);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        return List.of("creative", "survival", "adventure", "spectator");
    }

    @Override
    public @NotNull String permission() {
        return "cc.command.gamemode";
    }
}
