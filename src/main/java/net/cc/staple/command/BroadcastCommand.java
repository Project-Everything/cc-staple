package net.cc.staple.command;

import net.cc.staple.util.StapleColor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")

public final class BroadcastCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        // Check if player entered no arguments
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /broadcast <message>").color(StapleColor.GRAY));
            return;
        }

        // Broadcast message
        final TextComponent message = Component.text()
                .append(Component.text("[Broadcast] ").color(StapleColor.DARK_PURPLE))
                .append(Component.text(String.join(" ", args)).color(StapleColor.LIGHT_PURPLE))
                .build();

        Bukkit.broadcast(message);
    }

    @Override
    public @NotNull String permission() {
        return "cc.command.broadcast";
    }
}