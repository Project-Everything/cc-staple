package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

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
        final TextComponent component = Component.text()
                .append(StapleUtil.COMMAND_HEADER,
                        Component.newline(),
                        Component.newline(),
                        Component.text("Help").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.text(" > ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD),
                        Component.text("creative-central.net/help'>")
                                .clickEvent(ClickEvent.openUrl("https://creative-central.net/help")),
                        Component.newline(),
                        Component.newline(),
                        StapleUtil.COMMAND_HEADER
                ).build();

        context.getSource().getSender().sendMessage(component);
        return Command.SINGLE_SUCCESS;
    }
}
