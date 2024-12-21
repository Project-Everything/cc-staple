package net.cc.staple.command;

import net.cc.staple.StaplePlugin;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.cc.staple.util.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings({"UnstableApiUsage"})

public final class TpaCommand implements BasicCommand {

    private final StaplePlugin plugin;

    public TpaCommand() {
        this.plugin = StaplePlugin.getInstance();
    }

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
            player.sendMessage(Component.text("Usage: /tpa <player>").color(NamedTextColor.GRAY));
            return;
        }

        // Attempt to find target player
        Collection<Player> targets = Bukkit.matchPlayer(args[0]);
        if (targets.isEmpty()) {
            player.sendMessage(Component.text("Player not found").color(NamedTextColor.RED));
            return;
        }

        Player targetPlayer = targets.iterator().next();
        plugin.getTpaManager().sendRequest(player, targetPlayer, targetPlayer);
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
        return StapleUtil.PERMISSION_COMMAND_TPA;
    }
}
