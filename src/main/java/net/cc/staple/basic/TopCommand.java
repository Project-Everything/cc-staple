package net.cc.staple.basic;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.cc.staple.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"UnstableApiUsage"})

public final class TopCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        // Check if sender is not player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("The console cannot use this command.").color(NamedTextColor.RED));
            return;
        }

        Location location = player.getWorld().getHighestBlockAt(player.getLocation()).getLocation().add(0, 1, 0);
        location.setDirection(player.getLocation().getDirection());
        location.setPitch(player.getLocation().getPitch());

        // Teleport player to the highest block
        player.teleport(location);
        player.sendMessage(Component.text("You've been teleported up!").color(NamedTextColor.GOLD));
    }

    @Override
    public @NotNull String permission() {
        return StapleUtil.PERMISSION_COMMAND_TOP;
    }
}
