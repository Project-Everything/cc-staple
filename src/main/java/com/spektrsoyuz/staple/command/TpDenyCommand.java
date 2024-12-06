package com.spektrsoyuz.staple.command;

import com.spektrsoyuz.staple.StaplePlugin;
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

@SuppressWarnings({"UnstableApiUsage"})

public final class TpDenyCommand implements BasicCommand {

    private final StaplePlugin plugin;

    public TpDenyCommand() {
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

        // Check if player entered no arguments
        if (args.length == 0) {
            player.sendMessage(Component.text("Usage: /tpdeny <player>").color(StapleColor.GRAY));
            return;
        }

        // Attempt to find target player
        Collection<Player> targets = Bukkit.matchPlayer(args[0]);
        if (targets.isEmpty()) {
            player.sendMessage(Component.text("Player not found").color(StapleColor.RED));
            return;
        }

        Player targetPlayer = targets.iterator().next();
        plugin.getTpaManager().denyRequest(targetPlayer, player);
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
        return "cc.command.tpa";
    }
}
