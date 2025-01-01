package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleConfig;
import net.cc.staple.StapleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")

public final class RespawnCommand {

    public RespawnCommand(Commands commands) {
        commands.register(Commands.literal("respawn")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_RESPAWN))
                        .executes(this::execute0)
                        .build(),
                "Respawn");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            player.setHealth(0);
            player.sendMessage(StapleConfig.getRespawnMessage());
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }
}
