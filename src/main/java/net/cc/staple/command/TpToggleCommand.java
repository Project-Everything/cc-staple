package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StaplePlugin;
import net.cc.staple.player.StaplePlayer;
import net.cc.staple.util.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"UnstableApiUsage"})

public final class TpToggleCommand {

    public TpToggleCommand(Commands commands) {
        commands.register(Commands.literal("tptoggle")
                .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_TPTOGGLE))
                .executes(this::execute0)
                .build());
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            StaplePlayer staplePlayer = StaplePlugin.getInstance().getPlayerManager().get(player.getUniqueId());
            if (staplePlayer != null) {
                boolean isTpDisabled = staplePlayer.isTpDisabled();
                staplePlayer.setTpDisabled(!isTpDisabled);

                if (isTpDisabled) {
                    player.sendMessage(Component.text("Enabled receiving tp requests").color(NamedTextColor.GOLD));
                } else {
                    player.sendMessage(Component.text("Disabled receiving tp requests").color(NamedTextColor.GOLD));
                }
                return Command.SINGLE_SUCCESS;
            }
        }
        return 0;
    }
}
