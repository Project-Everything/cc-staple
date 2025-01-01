package net.cc.staple.basic;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.cc.staple.util.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"UnstableApiUsage"})

public final class JumpCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        // Check if sender is not player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("The console cannot use this command.").color(NamedTextColor.RED));
            return;
        }

        // Attempt to find target block
        Block targetBlock = player.getTargetBlockExact(100);
        if (targetBlock == null) {
            player.sendMessage(Component.text("Could not find a block within 100m.").color(NamedTextColor.RED));
            return;
        }

        Location location = targetBlock.getLocation().add(0, 1, 0);
        location.setDirection(player.getLocation().getDirection());
        location.setPitch(player.getLocation().getPitch());

        // Teleport player to target block
        player.teleport(location);
        player.sendMessage(Component.text("You've jumped to a block!").color(NamedTextColor.GOLD));
    }

    @Override
    public @NotNull String permission() {
        return StapleUtil.PERMISSION_COMMAND_JUMP;
    }
}
