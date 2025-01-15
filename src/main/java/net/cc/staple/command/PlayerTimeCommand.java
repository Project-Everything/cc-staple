package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.util.StapleUtil;
import net.cc.staple.command.argument.CustomTime;
import net.cc.staple.command.argument.CustomTimeArgumentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class PlayerTimeCommand {

    public PlayerTimeCommand(Commands commands) {
        commands.register(Commands.literal("playertime")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_PLAYERTIME))
                        .executes(this::execute0)
                        .then(Commands.argument("preset", new CustomTimeArgumentType())
                                .executes(this::execute1))
                        .then(Commands.argument("value", LongArgumentType.longArg())
                                .executes(this::execute2))
                        .build(),
                "Set the player time",
                List.of("ptime"));
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            player.sendMessage(Component.text()
                    .content("/" + context.getInput() + " <day/night/morning/evening/off>")
                    .color(NamedTextColor.GOLD)
                    .build());
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int execute1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            CustomTime customTime = context.getArgument("preset", CustomTime.class);

            long longTime;
            switch (customTime) {
                case morning -> longTime = 0;
                case day -> longTime = 6000;
                case evening -> longTime = 12000;
                case night -> longTime = 18000;
                default -> longTime = -1;
            }

            if (longTime == -1) {
                player.resetPlayerTime();
            } else {
                player.setPlayerTime(longTime, false);
            }

            player.sendMessage(Component.text("Time set to " + StringUtils.capitalize(customTime.name()), NamedTextColor.GOLD));
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int execute2(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            long longTime = context.getArgument("value", Long.class);
            if (longTime == -1) {
                player.resetPlayerTime();
                longTime = 0;
            } else {
                player.setPlayerTime(longTime, false);
            }

            player.sendMessage(Component.text("Time set to " + longTime, NamedTextColor.GOLD));
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }
}
