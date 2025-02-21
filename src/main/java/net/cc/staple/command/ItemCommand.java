package net.cc.staple.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.cc.staple.StapleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")

public final class ItemCommand {

    public ItemCommand(Commands commands) {
        commands.register(Commands.literal("item")
                        .requires(stack -> stack.getSender().hasPermission(StapleUtils.PERMISSION_COMMAND_ITEM))
                        .executes(this::execute0)
                        .then(Commands.argument("item", ArgumentTypes.itemStack())
                                .executes(this::execute1)
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0, 1000))
                                        .executes(this::execute2)))
                        .build(),
                "Spawn an item",
                List.of("i"));
    }

    private int execute0(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            player.sendMessage(Component.text("/" + context.getInput() + " <item> <amount>").color(NamedTextColor.RED));
        } else {
            sender.sendMessage(StapleUtils.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int execute1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            ItemStack item = context.getArgument("item", ItemStack.class);
            player.getInventory().addItem(item);
            player.sendMessage(Component.text()
                    .content("Received 1x")
                    .color(NamedTextColor.GOLD)
                    .append(Component.space(),
                            item.displayName().color(NamedTextColor.YELLOW))
                    .build());
        } else {
            sender.sendMessage(StapleUtils.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int execute2(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            int amount = IntegerArgumentType.getInteger(context, "amount");
            ItemStack item = context.getArgument("item", ItemStack.class).asQuantity(amount);
            player.getInventory().addItem(item);
            player.sendMessage(Component.text()
                    .content("Received " + amount + "x")
                    .color(NamedTextColor.GOLD)
                    .append(Component.space(),
                            item.displayName().color(NamedTextColor.YELLOW))
                    .build());
        } else {
            sender.sendMessage(StapleUtils.MESSAGE_CONSOLE_SENDER);
        }
        return Command.SINGLE_SUCCESS;
    }
}
