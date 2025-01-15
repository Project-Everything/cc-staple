package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.util.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")

public final class SpeedCommand {

    public SpeedCommand(Commands commands) {
        commands.register(Commands.literal("speed")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_SPEED))
                        .executes(this::execute0)
                        .then(Commands.argument("value", IntegerArgumentType.integer(0, 10))
                                .executes(this::execute1))
                        .then(Commands.literal("reset")
                                .executes(this::execute2))
                        .build(),
                "Set your speed");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        context.getSource().getSender().sendMessage(Component.text("/" + context.getInput() + " <value>").color(NamedTextColor.RED));
        return Command.SINGLE_SUCCESS;
    }

    private int execute1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            int value = context.getArgument("value", Integer.class);
            String state = "walkspeed";
            if (player.isFlying()) {
                player.setFlySpeed((float) value / 10);
                state = "flyspeed";
            } else {
                player.setWalkSpeed((float) value / 10);
            }

            player.sendMessage(Component.text("Set your " + state + " to " + value + ".").color(NamedTextColor.GOLD));
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int execute2(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            String state = "walkspeed";
            if (player.isFlying()) {
                player.setFlySpeed(0.2f);
                state = "flyspeed";
            } else {
                player.setWalkSpeed(0.2f);
            }

            player.sendMessage(Component.text("Reset your " + state + ".").color(NamedTextColor.GOLD));
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }
}
