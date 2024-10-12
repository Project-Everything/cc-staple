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

@SuppressWarnings({"UnstableApiUsage"})

public final class TpToggleCommand implements BasicCommand {

    private final StaplePlugin plugin;

    public TpToggleCommand() {
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

        // Get StaplePlayer instance from cache
        StaplePlayer staplePlayer = plugin.getPlayerManager().get(player.getUniqueId());
        if (staplePlayer == null) {
            player.sendMessage(Component.text("Command execution failed. Please notify an admin of this error.").color(StapleColor.RED));
            return;
        }

        // Set tp disable
        boolean isTpDisabled = staplePlayer.isTpDisabled();
        if (isTpDisabled) {
            staplePlayer.setTpDisabled(false);
            player.sendMessage(Component.text("Enabled receiving tp requests").color(StapleColor.GOLD));
        } else {
            staplePlayer.setTpDisabled(true);
            player.sendMessage(Component.text("Disabled receiving tp requests").color(StapleColor.GOLD));
        }
    }

    @Override
    public @NotNull String permission() {
        return "staple.tptoggle";
    }
}
