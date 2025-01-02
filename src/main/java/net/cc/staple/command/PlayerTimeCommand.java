package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleConfig;
import net.cc.staple.StapleUtil;
import net.cc.staple.command.argument.StapleTime;
import net.cc.staple.command.argument.StapleTimeArgumentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

@SuppressWarnings("UnstableApiUsage")

public final class PlayerTimeCommand {

    public PlayerTimeCommand(Commands commands) {
        commands.register(Commands.literal("playertime")
                .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_PLAYERTIME))
                .executes(this::execute0)
                .then(Commands.argument("preset", new StapleTimeArgumentType())
                        .executes(this::execute1))
                .then(Commands.argument("value", LongArgumentType.longArg())
                        .executes(this::execute2))
                .build());
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            player.sendMessage(Component.text()
                    .content("/" + context.getInput() + " <day/night/morning/evening/off>")
                    .color(NamedTextColor.GOLD)
                    .build());
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }

    private int execute1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            StapleTime stapleTime = context.getArgument("preset", StapleTime.class);

            long longTime;
            switch (stapleTime) {
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

            TextReplacementConfig config = TextReplacementConfig.builder()
                    .matchLiteral("{time}")
                    .replacement(stapleTime.name().toUpperCase(Locale.ENGLISH))
                    .build();

            player.sendMessage(StapleConfig.getPlayerTimeMessage().replaceText(config));
            return Command.SINGLE_SUCCESS;
        }
        return 0;
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

            TextReplacementConfig config = TextReplacementConfig.builder()
                    .matchLiteral("{time}")
                    .replacement(String.valueOf(longTime))
                    .build();
            player.sendMessage(StapleConfig.getPlayerTimeMessage().replaceText(config));
        }
        return 0;
    }
}
