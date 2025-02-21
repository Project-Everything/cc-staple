package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.cc.staple.StapleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("UnstableApiUsage")

public final class HatCommand {

    public HatCommand(Commands commands) {
        commands.register(Commands.literal("hat")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtils.PERMISSION_COMMAND_HAT))
                        .executes(this::execute0)
                        .build(),
                "Put an item on your head");
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            ItemStack handItem = player.getInventory().getItemInMainHand();
            ItemStack helmetItem = player.getInventory().getHelmet();

            if (handItem.getType() == Material.AIR) {
                player.sendMessage(Component.text()
                        .content("You must have an item in hand.")
                        .color(NamedTextColor.RED)
                        .build());
            } else if (helmetItem != null) {
                player.getInventory().setHelmet(handItem);
                player.getInventory().setItemInMainHand(helmetItem);

                player.sendMessage(Component.text()
                        .content("Enjoy your new hat!")
                        .color(NamedTextColor.GREEN)
                        .build());
            } else {
                player.getInventory().setHelmet(handItem);
                player.getInventory().setItemInMainHand(null);

                player.sendMessage(Component.text()
                        .content("Enjoy your new hat!")
                        .color(NamedTextColor.GREEN)
                        .build());
            }
        } else {
            sender.sendMessage(StapleUtils.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

}
