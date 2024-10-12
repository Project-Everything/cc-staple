package com.spektrsoyuz.staple.player;

import com.spektrsoyuz.staple.StaplePlugin;
import com.spektrsoyuz.staple.util.StapleColor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.*;

public final class TpaManager {

    private final StaplePlugin plugin;
    private final Map<UUID, TpaRequest> tpaRequests;

    public TpaManager() {
        this.plugin = StaplePlugin.getInstance();
        this.tpaRequests = new HashMap<>();
    }

    public Set<TpaRequest> getRequests() {
        return new HashSet<>(tpaRequests.values());
    }

    public void sendRequest(Player sourcePlayer, Player targetPlayer, Player destinationPlayer) {
        // Check if source player = target player
        if (sourcePlayer.equals(targetPlayer)) {
            sourcePlayer.sendMessage(Component.text("You cannot send a request to yourself!").color(StapleColor.RED));
            return;
        }

        // Check if there is an outstanding request from the source player
        for (TpaRequest request : getRequests()) {
            if (sourcePlayer.equals(request.sourcePlayer())) {
                sourcePlayer.sendMessage(Component.text("You have already sent a request to " + request.targetPlayer().getName() + ".").color(StapleColor.RED));
                return;
            }
        }

        // Get StaplePlayer instance from cache
        StaplePlayer staplePlayer = plugin.getPlayerManager().get(targetPlayer.getUniqueId());
        if (staplePlayer == null) {
            sourcePlayer.sendMessage(Component.text("Command execution failed. Please notify an admin of this error.").color(StapleColor.RED));
            return;
        }

        // Check if target player has tp disabled
        boolean isTpDisabled = staplePlayer.isTpDisabled();
        if (isTpDisabled) {
            sourcePlayer.sendMessage(Component.text(targetPlayer.getName() + " has teleportation disabled!").color(StapleColor.RED));
            return;
        }

        // Send request message to target player
        sourcePlayer.sendMessage(Component.text("Teleport request sent to " + targetPlayer.getName() + ".").color(StapleColor.GOLD));
        if (destinationPlayer.equals(sourcePlayer)) {
            targetPlayer.sendMessage(Component.text().append(Component.text("You received a request to teleport to " + sourcePlayer.getName() + ".").color(StapleColor.GOLD)).append(Component.text("\nType /tpaccept to accept or /tpdeny to deny").color(StapleColor.GOLD)).build());
        } else if (destinationPlayer.equals(targetPlayer)) {
            targetPlayer.sendMessage(Component.text().append(Component.text("You received a teleport request from " + sourcePlayer.getName() + ".").color(StapleColor.GOLD)).append(Component.text("\nType /tpaccept to accept or /tpdeny to deny").color(StapleColor.GOLD)).build());
        } else {
            sourcePlayer.sendMessage(Component.text("Command execution failed. Please notify an admin of this error.").color(StapleColor.RED));
            return;
        }

        tpaRequests.put(sourcePlayer.getUniqueId(), new TpaRequest(sourcePlayer, targetPlayer, destinationPlayer));
    }

    public void acceptRequest(Player sourcePlayer, Player targetPlayer) {
        TpaRequest tpaRequest = tpaRequests.get(sourcePlayer.getUniqueId());

        if (tpaRequest == null) {
            targetPlayer.sendMessage(Component.text("You do not have request from " + sourcePlayer.getName()).color(StapleColor.RED));
            return;
        }

        Player destinationPlayer = tpaRequest.destinationPlayer();

        // Accept request and teleport player to destination
        if (destinationPlayer.equals(sourcePlayer)) {
            targetPlayer.sendMessage(Component.text(targetPlayer.getName() + " accepted your request.").color(StapleColor.GOLD));
            sourcePlayer.sendMessage(Component.text("Teleporting to " + destinationPlayer.getName() + ".").color(StapleColor.GOLD));
            targetPlayer.teleport(destinationPlayer);
            tpaRequests.remove(sourcePlayer.getUniqueId());
        } else if (destinationPlayer.equals(targetPlayer)) {
            targetPlayer.sendMessage(Component.text(sourcePlayer.getName() + " accepted your request.").color(StapleColor.GOLD));
            sourcePlayer.sendMessage(Component.text("Teleporting to " + destinationPlayer.getName() + ".").color(StapleColor.GOLD));
            targetPlayer.teleport(destinationPlayer);
            tpaRequests.remove(sourcePlayer.getUniqueId());
        } else {
            sourcePlayer.sendMessage(Component.text("Command execution failed. Please notify an admin of this error.").color(StapleColor.RED));
        }
    }

    public void denyRequest(Player sourcePlayer, Player targetPlayer) {
        TpaRequest tpaRequest = tpaRequests.get(sourcePlayer.getUniqueId());

        if (tpaRequest == null) {
            targetPlayer.sendMessage(Component.text("You do not have request from " + sourcePlayer.getName()).color(StapleColor.RED));
            return;
        }

        sourcePlayer.sendMessage(Component.text(sourcePlayer.getName() + " denied your request.").color(StapleColor.GOLD));
        targetPlayer.sendMessage(Component.text("Request denied.").color(StapleColor.GOLD));
        tpaRequests.remove(sourcePlayer.getUniqueId());
    }

    public void cancelRequest(Player sourcePlayer, Player targetPlayer) {
        TpaRequest tpaRequest = tpaRequests.get(sourcePlayer.getUniqueId());

        if (tpaRequest == null) {
            sourcePlayer.sendMessage(Component.text("You have not sent a request to " + targetPlayer.getName()).color(StapleColor.RED));
            return;
        }

        sourcePlayer.sendMessage(Component.text("Request cancelled.").color(StapleColor.GOLD));
        targetPlayer.sendMessage(Component.text(sourcePlayer.getName() + " has cancelled their teleport request.").color(StapleColor.GOLD));
        tpaRequests.remove(sourcePlayer.getUniqueId());
    }
}
