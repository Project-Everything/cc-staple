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

@SuppressWarnings({"UnstableApiUsage"})

public final class SpawnCommand {

    private final StaplePlugin plugin;

    public SpawnCommand(final StaplePlugin plugin, Commands commands) {
        this.plugin = plugin;
        commands.register(Commands.literal("spawn")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_SPAWN))
                        .executes(this::spawnCommand)
                        .build(),
                "Teleport to spawn");
        commands.register(Commands.literal("setspawn")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtil.PERMISSION_COMMAND_SETSPAWN))
                        .executes(this::setSpawnCommand)
                        .build(),
                "Set the world spawn");
    }

    private int spawnCommand(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            StaplePlayer staplePlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
            staplePlayer.setOldLocation(player.getLocation());

            final Location location = plugin.getConfigManager().getSpawnLocation();
            if (location != null) {
                player.teleport(location);
                player.sendMessage(Component.text("Teleported to spawn!", NamedTextColor.GOLD));
            }
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int setSpawnCommand(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            final Location location = player.getLocation();
            plugin.getConfigManager().setSpawnLocation(location);
            player.getWorld().setSpawnLocation(location);
            player.sendMessage(Component.text("Set spawn to your location.", NamedTextColor.GOLD));
        } else {
            sender.sendMessage(StapleUtil.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }
}
