package net.cc.staple.command;

import net.cc.staple.util.StapleColor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")

public final class SpeedCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        // Check if sender is not player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("The console cannot use this command.").color(StapleColor.RED));
            return;
        }

        // Check if player entered no arguments
        if (args.length == 0) {
            player.sendMessage(Component.text("Usage: /speed <value>").color(StapleColor.GRAY));
            return;
        }

        double speed;
        try {
            speed = Double.parseDouble(args[0]);
        } catch (Exception ignored) {
            player.sendMessage(Component.text("Value must be a number").color(StapleColor.RED));
            return;
        }

        // Cap speed at 10.0
        if (speed > 10) {
            speed = 10;
        } else if (speed < 0) {
            speed -= speed;
        }

        // Check if player is flying
        if (player.isFlying()) {
            setFlySpeed(player, speed);
        } else {
            setWalkSpeed(player, speed);
        }
    }

    @Override
    public @NotNull String permission() {
        return "cc.command.speed";
    }

    private void setFlySpeed(Player player, double speed) {
        double a = speed / 10;
        float value = (float) a;
        player.setFlySpeed(value);
        player.sendMessage(Component.text("Changed flight speed to " + speed).color(StapleColor.GOLD));
    }

    private void setWalkSpeed(Player player, double speed) {
        double a = speed / 10;
        float value = (float) a;
        player.setWalkSpeed(value);
        player.sendMessage(Component.text("Changed walk speed to " + speed).color(StapleColor.GOLD));
    }
}
