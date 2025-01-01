package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StaplePlugin;
import net.cc.staple.util.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@SuppressWarnings("UnstableApiUsage")

public final class HelpCommand {

    public HelpCommand(Commands commands) {
        commands.register(Commands.literal("help")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_HELP))
                        .executes(this::execute0)
                        .build(),
                "View help message");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        final StaplePlugin staplePlugin = StaplePlugin.getInstance();
        final String configText = staplePlugin.getConfig().getString(StapleUtil.CONFIG_MESSAGES_HELP);

        if (configText != null) {
            MiniMessage miniMessage = MiniMessage.miniMessage();
            Component component = miniMessage.deserialize(configText);
            context.getSource().getSender().sendMessage(component);
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }
}
