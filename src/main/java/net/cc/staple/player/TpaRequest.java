package net.cc.staple.player;

import org.bukkit.entity.Player;

public record TpaRequest(Player sourcePlayer, Player targetPlayer, Player destinationPlayer) {

}