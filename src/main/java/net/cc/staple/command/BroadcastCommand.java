package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class BroadcastCommand {

    public BroadcastCommand(Commands command) {
        command.register(Commands.literal("broadcast")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtils.PERMISSION_COMMAND_BROADCAST))
                        .executes(this::execute0)
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(this::execute1))
                        .build(),
                "Broadcast a message",
                List.of("bc"));
    }

    public int execute0(CommandContext<CommandSourceStack> context) {
        context.getSource().getSender().sendMessage(StapleUtils.getUsageComponent("/" + context.getInput() + " <message>"));
        return Command.SINGLE_SUCCESS;
    }

    public int execute1(CommandContext<CommandSourceStack> context) {
        String message = context.getArgument("message", String.class);
        final TextComponent component = Component.text()
                .content("[Broadcast]").color(NamedTextColor.DARK_PURPLE)
                .append(Component.space(),
                        Component.text(message).color(NamedTextColor.LIGHT_PURPLE))
                .build();
        Bukkit.broadcast(component);
        return Command.SINGLE_SUCCESS;
    }
}
