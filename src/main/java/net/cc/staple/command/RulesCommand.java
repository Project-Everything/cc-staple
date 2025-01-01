package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleConfig;
import net.cc.staple.StapleUtil;
import net.kyori.adventure.text.Component;

@SuppressWarnings("UnstableApiUsage")

public final class RulesCommand {

    public RulesCommand(Commands commands) {
        commands.register(Commands.literal("rules")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_RULES))
                        .executes(this::execute0)
                        .build(),
                "View rules message");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        Component component = StapleConfig.getRulesMessage();
        context.getSource().getSender().sendMessage(component);
        return Command.SINGLE_SUCCESS;
    }
}
