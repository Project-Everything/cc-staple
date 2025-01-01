package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleConfig;
import net.cc.staple.StapleUtil;
import net.kyori.adventure.text.Component;

@SuppressWarnings("UnstableApiUsage")

public final class VoteCommand {

    public VoteCommand(Commands commands) {
        commands.register(Commands.literal("vote")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_VOTE))
                        .executes(this::execute0)
                        .build(),
                "View vote message");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        Component component = StapleConfig.getVoteMessage();
        context.getSource().getSender().sendMessage(component);
        return Command.SINGLE_SUCCESS;
    }
}
