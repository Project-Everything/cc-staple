package com.spektrsoyuz.staple.command;

import com.spektrsoyuz.staple.StaplePlugin;
import com.spektrsoyuz.staple.player.StaplePlayer;
import com.spektrsoyuz.staple.util.StapleColor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")

public final class VanishCommand implements BasicCommand {

    private final StaplePlugin plugin;

    public VanishCommand() {
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

        UUID playerId = player.getUniqueId();

        // Get StaplePlayer instance from cache
        StaplePlayer staplePlayer = plugin.getPlayerManager().get(playerId);
        if (staplePlayer == null) {
            player.sendMessage(Component.text("Command execution failed. Please notify an admin of this error.").color(StapleColor.RED));
            return;
        }

        // Set vanished
        boolean isVanished = staplePlayer.isVanished();
        if (isVanished) {
            staplePlayer.setVanished(false);
            for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                onlinePlayer.showPlayer(plugin, player);
            }
            player.sendMessage(Component.text("You are no longer vanished.").color(StapleColor.GOLD));
        } else {
            staplePlayer.setVanished(true);
            for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                onlinePlayer.hidePlayer(plugin, player);
            }
            player.sendMessage(Component.text("You are now vanished.").color(StapleColor.GOLD));
        }
    }

    @Override
    public @NotNull String permission() {
        return "cc.command.vanish";
    }
}
