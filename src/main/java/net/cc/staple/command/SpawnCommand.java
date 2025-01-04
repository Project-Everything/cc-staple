package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StaplePlugin;
import net.cc.staple.StapleConfig;
import net.cc.staple.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"UnstableApiUsage"})

public final class SpawnCommand {

    private final StaplePlugin staplePlugin;

    public SpawnCommand(Commands commands) {
        staplePlugin = StaplePlugin.getInstance();
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
            final Location location = StapleConfig.getSpawnLocation(player.getWorld());
            player.teleport(location);
            player.sendMessage(Component.text("Teleported to spawn!", NamedTextColor.GOLD));
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }

    private int setSpawnCommand(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            final Location location = player.getLocation();
            StapleConfig.setSpawnLocation(location);
            StapleConfig.save(staplePlugin);
            player.getWorld().setSpawnLocation(location);
            player.sendMessage(Component.text("Set spawn to your location.", NamedTextColor.GOLD));
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }
}
