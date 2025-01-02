package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.cc.staple.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.codehaus.plexus.util.StringUtils;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class GamemodeCommand {

    public GamemodeCommand(Commands commands) {
        commands.register(Commands.literal("gamemode")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_GAMEMODE))
                        .executes(this::execute0)
                        .then(Commands.argument("gamemode", ArgumentTypes.gameMode())
                                .executes(this::execute1)
                                .then(Commands.argument("target", ArgumentTypes.player())
                                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_GAMEMODE_OTHER))
                                        .executes(this::execute2)))
                        .build(),
                "Set your gamemode",
                List.of("ec")
        );
        commands.register(Commands.literal("gms")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_GAMEMODE))
                        .executes(context -> execute3(context, GameMode.SURVIVAL))
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(context -> execute4(context, GameMode.SURVIVAL)))
                        .build(),
                "Set your gamemode to Survival");

        commands.register(Commands.literal("gmc")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_GAMEMODE))
                        .executes(context -> execute3(context, GameMode.CREATIVE))
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(context -> execute4(context, GameMode.CREATIVE)))
                        .build(),
                "Set your gamemode to Creative");

        commands.register(Commands.literal("gma")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_GAMEMODE))
                        .executes(context -> execute3(context, GameMode.ADVENTURE))
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(context -> execute4(context, GameMode.ADVENTURE)))
                        .build(),
                "Set your gamemode to Adventure");

        commands.register(Commands.literal("gmsp")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_GAMEMODE))
                        .executes(context -> execute3(context, GameMode.SPECTATOR))
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(context -> execute4(context, GameMode.SPECTATOR)))
                        .build(),
                "Set your gamemode to Spectator");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            player.sendMessage(Component.text()
                    .content("/" + context.getInput() + " <gamemode> <player>")
                    .color(NamedTextColor.RED)
                    .build());
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }

    private int execute1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            GameMode gameMode = context.getArgument("gamemode", GameMode.class);
            setGamemode(player, gameMode);
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }

    private int execute2(CommandContext<CommandSourceStack> context) {
        final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            GameMode gameMode = context.getArgument("gamemode", GameMode.class);
            try {
                Player target = targetResolver.resolve(context.getSource()).getFirst();
                setGamemode(player, target, gameMode);
                return Command.SINGLE_SUCCESS;
            } catch (CommandSyntaxException e) {
                context.getSource().getSender().sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
            }
        }
        return 0;
    }

    private int execute3(CommandContext<CommandSourceStack> context, GameMode gameMode) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            setGamemode(player, gameMode);
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }

    private int execute4(CommandContext<CommandSourceStack> context, GameMode gameMode) {
        final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            try {
                Player target = targetResolver.resolve(context.getSource()).getFirst();
                setGamemode(player, target, gameMode);
                return Command.SINGLE_SUCCESS;
            } catch (CommandSyntaxException e) {
                context.getSource().getSender().sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
            }
        }
        return 0;
    }

    private void setGamemode(Player source, GameMode gameMode) {
        source.setGameMode(gameMode);
        source.sendMessage(Component.text()
                .content("Gamemode set to " + StringUtils.capitalizeFirstLetter(gameMode.name()) + ".")
                .color(NamedTextColor.GOLD)
                .build());
    }

    private void setGamemode(Player source, Player target, GameMode gameMode) {
        source.sendMessage(Component.text()
                .content("Gamemode set to " + StringUtils.capitalizeFirstLetter(gameMode.name()) + " for " + target.getName() + ".")
                .color(NamedTextColor.GOLD)
                .build());
        target.sendMessage(Component.text()
                .content("Gamemode set to " + StringUtils.capitalizeFirstLetter(gameMode.name()) + ".")
                .color(NamedTextColor.GOLD)
                .build());
    }
}
