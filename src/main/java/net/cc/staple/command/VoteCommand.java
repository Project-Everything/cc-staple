package net.cc.staple.command;

import net.cc.staple.StaplePlugin;
import net.cc.staple.util.StapleColor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")

public final class VoteCommand implements BasicCommand {

    private final StaplePlugin plugin;

    public VoteCommand() {
        this.plugin = StaplePlugin.getInstance();
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        final String text = plugin.getConfig().getString("vote-message");

        // Deserialize text into component
        if (text != null) {
            MiniMessage miniMessage = MiniMessage.miniMessage();
            Component component = miniMessage.deserialize(text);
            sender.sendMessage(component);
        } else {
            sender.sendMessage(Component.text("Command execution failed. Please notify an admin of this error.").color(StapleColor.RED));
        }
    }

    @Override
    public @NotNull String permission() {
        return "cc.command.vote";
    }
}
