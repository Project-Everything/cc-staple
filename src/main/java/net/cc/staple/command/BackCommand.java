package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StaplePlugin;
import net.cc.staple.util.StapleUtil;
import net.cc.staple.player.StaplePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")

public final class BackCommand {

    private final StaplePlugin plugin;

    public BackCommand(final StaplePlugin plugin, Commands commands) {
        this.plugin = plugin;
        commands.register(Commands.literal("back")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_TELEPORT))
                        .executes(this::execute0)
                        .build(),
                "Teleport to your previous location");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            StaplePlayer staplePlayer = plugin.getPlayerManager().getPlayer(player);
            if (staplePlayer != null) {
                Location oldLocation = staplePlayer.getOldLocation();
                oldLocation.setPitch(player.getPitch());
                oldLocation.setYaw(player.getYaw());
                staplePlayer.setOldLocation(player.getLocation());

                player.teleport(oldLocation);
                player.sendMessage(Component.text("You've arrived at your previous location!").color(NamedTextColor.GOLD));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
