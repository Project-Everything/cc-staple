package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.cc.staple.util.StapleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class EnderChestCommand {

    public EnderChestCommand(Commands commands) {
        commands.register(Commands.literal("enderchest")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_ENDERCHEST))
                        .executes(this::execute0)
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_ENDERCHEST_OTHER))
                                .executes(this::execute1))
                        .build(),
                "View your ender chest",
                List.of("ec")
        );
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            player.openInventory(player.getEnderChest());
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }

    private int execute1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            Player targetPlayer = context.getArgument("player", Player.class);
            player.openInventory(targetPlayer.getEnderChest());
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }
}
