package com.spektrsoyuz.staple.command;

import com.spektrsoyuz.staple.util.StapleColor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")

public final class EnderChestCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        // Check if sender is not player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("The console cannot use this command.").color(StapleColor.RED));
            return;
        }

        // Open ender chest
        player.openInventory(player.getEnderChest());
        player.sendMessage(Component.text("Opened your ender chest.").color(StapleColor.GOLD));
    }

    @Override
    public @NotNull String permission() {
        return "cc.command.enderchest";
    }
}
