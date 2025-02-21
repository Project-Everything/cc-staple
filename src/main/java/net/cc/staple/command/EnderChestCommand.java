package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.cc.staple.util.StapleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class EnderChestCommand {

    // Constructor
    public EnderChestCommand(Commands commands) {
        commands.register(Commands.literal("enderchest")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtils.PERMISSION_COMMAND_ENDERCHEST))
                        .executes(this::openInventory)
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .requires(stack -> stack.getSender().hasPermission(StapleUtils.PERMISSION_COMMAND_ENDERCHEST_OTHER))
                                .executes(this::openInventoryOther))
                        .build(),
                "View your ender chest",
                List.of("echest", "ec")
        );
    }

    // Method to execute command
    private int openInventory(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            player.openInventory(player.getEnderChest());
            player.sendMessage(Component.text("Opened your ender chest.").color(NamedTextColor.GOLD));
        } else {
            sender.sendMessage(StapleUtils.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    // Method to execute command with additional Player argument
    private int openInventoryOther(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
            try {
                final Player targetPlayer = targetResolver.resolve(context.getSource()).getFirst();
                player.openInventory(targetPlayer.getEnderChest());
                player.sendMessage(Component.text("Opened " + targetPlayer.getName() + "'s ender chest.").color(NamedTextColor.GOLD));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            sender.sendMessage(StapleUtils.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }
}
