package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.FinePosition;
import net.cc.staple.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class TeleportCommand {

    public TeleportCommand(Commands commands) {
        commands.register(Commands.literal("teleport")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_TELEPORT))
                        .executes(this::teleport0)
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(this::teleport1)
                                .then(Commands.argument("target2", ArgumentTypes.player())
                                        .executes(this::teleport2))
                                .then(Commands.argument("position", ArgumentTypes.finePosition(true))
                                        .executes(this::teleport4)))
                        .then(Commands.argument("position", ArgumentTypes.finePosition(true))
                                .executes(this::teleport3))
                        .build(),
                "Teleport to a player",
                List.of("tp"));
        commands.register(Commands.literal("teleporthere")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_TELEPORT))
                        .executes(this::teleportHere0)
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(this::teleportHere1))
                        .build(),
                "Teleport a player to your location",
                List.of("tphere"));
    }

    private int teleport0(CommandContext<CommandSourceStack> context) {
        context.getSource().getSender().sendMessage(Component.text("/" + context.getInput() + " <target>").color(NamedTextColor.RED));
        context.getSource().getSender().sendMessage(Component.text("/" + context.getInput() + " <target> <target2>").color(NamedTextColor.RED));
        context.getSource().getSender().sendMessage(Component.text("/" + context.getInput() + " <x> <y> <z>").color(NamedTextColor.RED));
        return Command.SINGLE_SUCCESS;
    }

    private int teleport1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);

            try {
                final Player target = targetResolver.resolve(context.getSource()).getFirst();

                player.teleport(target);
                player.sendMessage(Component.text("Teleported to " + target.getName() + ".").color(NamedTextColor.GOLD));
            } catch (CommandSyntaxException e) {
                context.getSource().getSender().sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
            }
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int teleport2(CommandContext<CommandSourceStack> context) {
        final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
        final PlayerSelectorArgumentResolver targetResolver2 = context.getArgument("target2", PlayerSelectorArgumentResolver.class);

        try {
            final Player target = targetResolver.resolve(context.getSource()).getFirst();
            final Player target2 = targetResolver2.resolve(context.getSource()).getFirst();

            target.teleport(target2);
            context.getSource().getSender().sendMessage(Component.text("Teleported " + target.getName() + " to " + target2.getName() + ".").color(NamedTextColor.GOLD));
        } catch (CommandSyntaxException e) {
            context.getSource().getSender().sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
        }
        return Command.SINGLE_SUCCESS;
    }

    private int teleport3(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            FinePositionResolver finePositionResolver = context.getArgument("position", FinePositionResolver.class);

            try {
                final FinePosition finePosition = finePositionResolver.resolve(context.getSource());
                final Location location = finePosition.toLocation(player.getWorld());
                DecimalFormat df = new DecimalFormat("#.##");

                player.teleport(location);
                player.sendMessage(Component.text("Teleported to " + df.format(location.getX()) + ", " + df.format(location.getY()) + ", " + df.format(location.getZ())).color(NamedTextColor.GOLD));
            } catch (CommandSyntaxException e) {
                context.getSource().getSender().sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
            }
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int teleport4(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
            FinePositionResolver finePositionResolver = context.getArgument("position", FinePositionResolver.class);

            try {
                final Player target = targetResolver.resolve(context.getSource()).getFirst();
                final FinePosition finePosition = finePositionResolver.resolve(context.getSource());
                final Location location = finePosition.toLocation(player.getWorld());
                DecimalFormat df = new DecimalFormat("#.##");

                target.teleport(location);
                player.sendMessage(Component.text("Teleported " + target.getName() + " to " + df.format(location.getX()) + ", " + df.format(location.getY()) + ", " + df.format(location.getZ())).color(NamedTextColor.GOLD));
                target.sendMessage(Component.text("Teleported to " + df.format(location.getX()) + ", " + df.format(location.getY()) + ", " + df.format(location.getZ())).color(NamedTextColor.GOLD));
            } catch (CommandSyntaxException e) {
                context.getSource().getSender().sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
            }
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int teleportHere0(CommandContext<CommandSourceStack> context) {
        context.getSource().getSender().sendMessage(Component.text("/" + context.getInput() + " <target>").color(NamedTextColor.RED));
        return Command.SINGLE_SUCCESS;
    }

    private int teleportHere1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);

            try {
                final Player target = targetResolver.resolve(context.getSource()).getFirst();

                target.teleport(player);
                player.sendMessage(Component.text("Teleported " + target.getName() + " to your location.").color(NamedTextColor.GOLD));
            } catch (CommandSyntaxException e) {
                context.getSource().getSender().sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
            }
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }
}
