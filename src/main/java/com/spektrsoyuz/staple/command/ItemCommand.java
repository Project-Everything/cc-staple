package com.spektrsoyuz.staple.command;

import com.spektrsoyuz.staple.util.StapleColor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings({"UnstableApiUsage"})

public final class ItemCommand implements BasicCommand {

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
            player.sendMessage(Component.text("Usage: /item <name>").color(StapleColor.GRAY));
            return;
        }

        // Attempt to find specified material (item)
        Material material = Material.matchMaterial(args[0]);
        if (material == null) {
            player.sendMessage(Component.text("Invalid item name.").color((StapleColor.RED)));
            return;
        }

        player.getInventory().addItem(new ItemStack(material, 1));
        player.sendMessage(Component.text("Received 1x " + args[0].replace("_", " ")).color(StapleColor.GOLD));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        Collection<String> suggestions = new ArrayList<>();
        for (Material material : Material.values()) {
            suggestions.add(material.name().toLowerCase());
        }
        return suggestions;
    }

    @Override
    public @NotNull String permission() {
        return "staple.item";
    }
}
