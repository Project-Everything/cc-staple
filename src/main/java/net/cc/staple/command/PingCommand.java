package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings("UnstableApiUsage")

public final class PingCommand {

    public PingCommand(Commands commands) {
        commands.register(Commands.literal("ping")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_PING))
                        .executes(this::execute0)
                        .build(),
                "Pong!");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        context.getSource().getSender().sendMessage(Component.text("Pong!", NamedTextColor.GOLD));
        return Command.SINGLE_SUCCESS;
    }
}
