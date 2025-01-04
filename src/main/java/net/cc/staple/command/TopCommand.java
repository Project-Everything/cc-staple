package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")

public final class TopCommand {

    public TopCommand(Commands commands) {
        commands.register(Commands.literal("top")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_TOP))
                        .executes(this::execute0)
                        .build(),
                "Teleport to the highest block");
        commands.register(Commands.literal("jump")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_JUMP))
                        .executes(this::execute1)
                        .build(),
                "Teleport to a direct block");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            Location location = player.getWorld().getHighestBlockAt(player.getLocation()).getLocation().add(0, 1, 0);
            location.setDirection(player.getLocation().getDirection());
            location.setPitch(player.getLocation().getPitch());

            if (location.getY() > player.getLocation().getY()) {
                player.teleport(location);
            }
            player.sendMessage(Component.text("You've been teleported up!", NamedTextColor.GOLD));
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int execute1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            Block targetBlock = player.getTargetBlockExact(100);
            if (targetBlock == null) {
                player.sendMessage(Component.text("Could not find a block within 100m.", NamedTextColor.RED));
            } else {
                Location location = targetBlock.getLocation().add(0, 1, 0);
                location.setDirection(player.getLocation().getDirection());
                location.setPitch(player.getLocation().getPitch());

                player.teleport(location);
                player.sendMessage(Component.text("You've jumped to a location!", NamedTextColor.GOLD));
            }
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }
}
