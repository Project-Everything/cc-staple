package net.cc.staple.command;

import net.cc.staple.StaplePlugin;
import net.cc.staple.util.StapleColor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"UnstableApiUsage"})

public final class SetSpawnCommand implements BasicCommand {

    private final StaplePlugin plugin;

    public SetSpawnCommand() {
        this.plugin = StaplePlugin.getInstance();
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        // Check if sender is not player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("The console cannot use this command.").color(StapleColor.RED));
            return;
        }

        if (!(player.hasPermission(permission()))) {
            player.sendMessage(Component.text("You do not have permission to use this command!").color(StapleColor.RED));
            return;
        }

        Location location = player.getLocation();
        player.sendMessage(Component.text()
                .append(Component.text("Server Spawn has been set to: ").color(StapleColor.GOLD))
                .append(Component.text(location.getX() + " " + location.getY() + " " + location.getZ()).color(StapleColor.YELLOW))
                .build());
        this.plugin.getConfig().set("spawn-x", location.getX());
        this.plugin.getConfig().set("spawn-y", location.getY());
        this.plugin.getConfig().set("spawn-z", location.getZ());
        this.plugin.saveConfig();
    }

    @Override
    public @NotNull String permission() {
        return "cc.command.setspawn";
    }
}
