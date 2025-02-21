package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

@SuppressWarnings("UnstableApiUsage")

public final class DiscordCommand {

    public DiscordCommand(Commands commands) {
        commands.register(Commands.literal("discord")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtils.PERMISSION_COMMAND_RULES))
                        .executes(this::execute0)
                        .build(),
                "View discord message");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        final TextComponent component = Component.text()
                .append(StapleUtils.COMMAND_HEADER,
                        Component.newline(),
                        Component.newline(),
                        Component.text("Discord").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.text(" > ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD),
                        Component.text("discord.gg/Hpg5K7wgUb")
                                .clickEvent(ClickEvent.openUrl("https://discord.gg/Hpg5K7wgUb")),
                        Component.newline(),
                        Component.newline(),
                        StapleUtils.COMMAND_HEADER
                ).build();

        context.getSource().getSender().sendMessage(component);
        return Command.SINGLE_SUCCESS;
    }
}
