package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.cc.staple.StaplePlugin;
import net.cc.staple.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"UnstableApiUsage"})

public final class TpaCommand {

    private final StaplePlugin staplePlugin;

    public TpaCommand(Commands commands) {
        staplePlugin = StaplePlugin.getInstance();
        commands.register(Commands.literal("tpa")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_TPA))
                        .executes(this::tpa0)
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(this::tpa1))
                        .build(),
                "Send a teleport request");
        commands.register(Commands.literal("tpahere")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_TPA))
                        .executes(this::tpa0)
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(this::tpa1))
                        .build(),
                "Send a teleport here request");
        commands.register(Commands.literal("tpaccept")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_TPA))
                        .executes(this::tpa0)
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(this::tpa1))
                        .build(),
                "Accept a teleport request");
        commands.register(Commands.literal("tpdeny")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_TPA))
                        .executes(this::tpa0)
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(this::tpa1))
                        .build(),
                "Deny a teleport request");
        commands.register(Commands.literal("tpcancel")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_TPA))
                        .executes(this::tpa0)
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(this::tpa1))
                        .build(),
                "Cancel a teleport request");
    }

    private int tpa0(CommandContext<CommandSourceStack> context) {
        context.getSource().getSender().sendMessage(Component.text("/" + context.getInput() + " <player>").color(NamedTextColor.RED));
        return Command.SINGLE_SUCCESS;
    }

    private int tpa1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
            final String label = context.getInput().split(" ")[0];

            try {
                final Player target = targetResolver.resolve(context.getSource()).getFirst();
                switch (label) {
                    case "tpa" -> staplePlugin.getTpaManager().sendRequest(player, target, target);
                    case "tpahere" -> staplePlugin.getTpaManager().sendRequest(player, target, player);
                    case "tpaccept" -> staplePlugin.getTpaManager().acceptRequest(target, player);
                    case "tpdeny" -> staplePlugin.getTpaManager().denyRequest(target, player);
                    case "tpcancel" -> staplePlugin.getTpaManager().cancelRequest(player, target);
                }
            } catch (CommandSyntaxException e) {
                context.getSource().getSender().sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
            }
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }
}
