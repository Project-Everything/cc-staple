package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")

public final class RespawnCommand {

    public RespawnCommand(Commands commands) {
        commands.register(Commands.literal("respawn")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtils.PERMISSION_COMMAND_RESPAWN))
                        .executes(this::execute0)
                        .build(),
                "Respawn");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            player.setHealth(0);
            player.sendMessage(Component.text("You've respawned!", NamedTextColor.GOLD));
        } else {
            sender.sendMessage(StapleUtils.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }
}
