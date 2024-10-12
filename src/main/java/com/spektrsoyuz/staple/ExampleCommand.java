package com.spektrsoyuz.staple;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@SuppressWarnings({"UnstableApiUsage"})

public final class ExampleCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        return BasicCommand.super.suggest(stack, args);
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return BasicCommand.super.canUse(sender);
    }

    @Override
    public @NotNull String permission() {
        return "staple.example";
    }
}
