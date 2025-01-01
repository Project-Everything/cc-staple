package net.cc.staple.basic;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.cc.staple.util.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"UnstableApiUsage"})

public final class PingCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        sender.sendMessage(Component.text("Pong!").color(NamedTextColor.GREEN));
    }

    @Override
    public @NotNull String permission() {
        return StapleUtil.PERMISSION_COMMAND_PING;
    }
}
