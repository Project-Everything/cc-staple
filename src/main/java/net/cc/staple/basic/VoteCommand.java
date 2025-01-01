package net.cc.staple.basic;

import net.cc.staple.StaplePlugin;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.cc.staple.util.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

        final String text = plugin.getConfig().getString("messages.vote");

        // Deserialize text into component
        if (text != null) {
            MiniMessage miniMessage = MiniMessage.miniMessage();
            Component component = miniMessage.deserialize(text);
            sender.sendMessage(component);
        } else {
            sender.sendMessage(Component.text("Command execution failed. Please notify an admin of this error.").color(NamedTextColor.RED));
        }
    }

    @Override
    public @NotNull String permission() {
        return StapleUtil.PERMISSION_COMMAND_VOTE;
    }
}
