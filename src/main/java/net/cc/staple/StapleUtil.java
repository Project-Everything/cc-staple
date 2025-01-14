package net.cc.staple;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

public final class StapleUtil {

    /* Permission Constants */
    public static final String PERMISSION_COMMAND_BROADCAST = "cc.command.broadcast";
    public static final String PERMISSION_COMMAND_ENDERCHEST = "cc.command.enderchest";
    public static final String PERMISSION_COMMAND_ENDERCHEST_OTHER = "cc.command.enderchest.other";
    public static final String PERMISSION_COMMAND_GAMEMODE = "cc.command.gamemode";
    public static final String PERMISSION_COMMAND_GAMEMODE_OTHER = "cc.command.gamemode.other";
    public static final String PERMISSION_COMMAND_HAT = "cc.command.hat";
    public static final String PERMISSION_COMMAND_HELP = "cc.command.help";
    public static final String PERMISSION_COMMAND_ITEM = "cc.command.item";
    public static final String PERMISSION_COMMAND_JUMP = "cc.command.jump";
    public static final String PERMISSION_COMMAND_PING = "cc.command.ping";
    public static final String PERMISSION_COMMAND_PLAYERTIME = "cc.command.playertime";
    public static final String PERMISSION_COMMAND_RESPAWN = "cc.command.respawn";
    public static final String PERMISSION_COMMAND_RULES = "cc.command.rules";
    public static final String PERMISSION_COMMAND_SETSPAWN = "cc.command.setspawn";
    public static final String PERMISSION_COMMAND_SPAWN = "cc.command.spawn";
    public static final String PERMISSION_COMMAND_SPEED = "cc.command.speed";
    public static final String PERMISSION_COMMAND_TELEPORT = "cc.command.teleport";
    public static final String PERMISSION_COMMAND_TOP = "cc.command.top";
    public static final String PERMISSION_COMMAND_TPA = "cc.command.tpa";
    public static final String PERMISSION_COMMAND_TPTOGGLE = "cc.command.tptoggle";
    public static final String PERMISSION_COMMAND_VOTE = "cc.command.vote";

    /* Common Components */
    public static final Component MESSAGE_PERMISSION_MISSING = Component.text()
            .append(Component.text("! ").color(NamedTextColor.RED).decorate(TextDecoration.BOLD),
                    Component.text("You do not have permission to use that command!", NamedTextColor.RED)
            ).build();

    public static final Component MESSAGE_CONSOLE_SENDER = Component.text()
            .append(Component.text("! ").color(NamedTextColor.RED).decorate(TextDecoration.BOLD),
                    Component.text("Only players can use that command.", NamedTextColor.RED)
            ).build();

    public static final Component COMMAND_HEADER = Component.text()
            .content(" ".repeat(50))
            .color(NamedTextColor.GRAY)
            .decorate(TextDecoration.STRIKETHROUGH)
            .build();

    public static @NotNull Component getUsageComponent(String input) {
        return Component.text()
                .append(Component.text("! ").color(NamedTextColor.RED).decorate(TextDecoration.BOLD),
                        Component.text(input, NamedTextColor.RED)
                ).build();
    }
}
