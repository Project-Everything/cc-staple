package net.cc.staple.player;

import net.cc.staple.StaplePlugin;
import net.cc.staple.util.StapleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class TpaManager {

    private final StaplePlugin plugin;
    private final Map<UUID, TpaRequest> tpaRequests;
    private final Map<UUID, Player> mostRecentRequests;

    public TpaManager(StaplePlugin plugin) {
        this.plugin = plugin;
        this.tpaRequests = new ConcurrentHashMap<>();
        this.mostRecentRequests = new ConcurrentHashMap<>();
    }

    public Set<TpaRequest> getRequests() {
        return new HashSet<>(tpaRequests.values());
    }

    public void sendRequest(Player sourcePlayer, Player targetPlayer, Player destinationPlayer) {
        // Check if source player = target player
        if (sourcePlayer.equals(targetPlayer)) {
            sourcePlayer.sendMessage(StapleUtil.getErrorComponent("You cannot send a request to yourself!"));
            return;
        }

        // Check if there is an outstanding request from the source player
        for (TpaRequest request : getRequests()) {
            if (sourcePlayer.equals(request.sourcePlayer())) {
                sourcePlayer.sendMessage(StapleUtil.getErrorComponent("You have already sent a request to " + request.targetPlayer().getName() + "."));
                return;
            }
        }

        // Get StaplePlayer instance from cache
        StaplePlayer staplePlayer = plugin.getPlayerManager().get(targetPlayer.getUniqueId());
        if (staplePlayer == null) {
            sourcePlayer.sendMessage(StapleUtil.getErrorComponent("Command execution failed. Please notify an admin of this error."));
            return;
        }

        // Check if target player has tp disabled
        boolean isTpDisabled = staplePlayer.isTpDisabled();
        if (isTpDisabled) {
            sourcePlayer.sendMessage(StapleUtil.getErrorComponent(targetPlayer.getName() + " has teleportation disabled!"));
            return;
        }

        // Send request message to target player
        sourcePlayer.sendMessage(Component.text("Teleport request sent to " + targetPlayer.getName() + ".").color(NamedTextColor.GOLD));
        if (destinationPlayer.equals(sourcePlayer)) {
            targetPlayer.sendMessage(Component.text().append(Component.text("You received a request to teleport to " + sourcePlayer.getName() + ".").color(NamedTextColor.GOLD)).append(Component.text("\nType /tpaccept to accept or /tpdeny to deny").color(NamedTextColor.GOLD)).build());
        } else if (destinationPlayer.equals(targetPlayer)) {
            targetPlayer.sendMessage(Component.text().append(Component.text("You received a teleport request from " + sourcePlayer.getName() + ".").color(NamedTextColor.GOLD)).append(Component.text("\nType /tpaccept to accept or /tpdeny to deny").color(NamedTextColor.GOLD)).build());
        } else {
            sourcePlayer.sendMessage(StapleUtil.getErrorComponent("Command execution failed. Please notify an admin of this error."));
            return;
        }

        tpaRequests.put(sourcePlayer.getUniqueId(), new TpaRequest(sourcePlayer, targetPlayer, destinationPlayer));
        mostRecentRequests.put(targetPlayer.getUniqueId(), sourcePlayer);
    }

    public void acceptRequest(Player targetPlayer) {
        acceptRequest(mostRecentRequests.get(targetPlayer.getUniqueId()), targetPlayer);
    }

    public void acceptRequest(Player sourcePlayer, Player targetPlayer) {
        TpaRequest tpaRequest = tpaRequests.get(sourcePlayer.getUniqueId());

        if (tpaRequest == null) {
            targetPlayer.sendMessage(StapleUtil.getErrorComponent("You do not have request from " + sourcePlayer.getName()));
            return;
        }

        Player destinationPlayer = tpaRequest.destinationPlayer();

        // Accept request and teleport player to destination
        if (destinationPlayer.equals(sourcePlayer)) {
            sourcePlayer.sendMessage(Component.text(targetPlayer.getName() + " accepted your request.", NamedTextColor.GOLD));
            targetPlayer.sendMessage(Component.text("Request accepted. Teleporting to " + destinationPlayer.getName() + ".", NamedTextColor.GOLD));
            StaplePlayer staplePlayer = plugin.getPlayerManager().get(sourcePlayer);
            staplePlayer.setOldLocation(sourcePlayer.getLocation());

            targetPlayer.teleport(destinationPlayer);
            tpaRequests.remove(sourcePlayer.getUniqueId());
        } else if (destinationPlayer.equals(targetPlayer)) {
            targetPlayer.sendMessage(Component.text("Request accepted.").color(NamedTextColor.GOLD));
            sourcePlayer.sendMessage(Component.text("Teleporting to " + destinationPlayer.getName() + ".", NamedTextColor.GOLD));
            sourcePlayer.teleport(destinationPlayer);
            tpaRequests.remove(sourcePlayer.getUniqueId());
        } else {
            sourcePlayer.sendMessage(StapleUtil.getErrorComponent("Command execution failed. Please notify an admin of this error."));
        }
    }

    public void denyRequest(Player targetPlayer) {
        denyRequest(mostRecentRequests.get(targetPlayer.getUniqueId()), targetPlayer);
    }

    public void denyRequest(Player sourcePlayer, Player targetPlayer) {
        TpaRequest tpaRequest = tpaRequests.get(sourcePlayer.getUniqueId());

        if (tpaRequest == null) {
            targetPlayer.sendMessage(StapleUtil.getErrorComponent("You do not have request from " + sourcePlayer.getName()));
            return;
        }

        sourcePlayer.sendMessage(Component.text(targetPlayer.getName() + " denied your request.", NamedTextColor.GOLD));
        targetPlayer.sendMessage(Component.text("Request denied.").color(NamedTextColor.GOLD));
        tpaRequests.remove(sourcePlayer.getUniqueId());
    }

    public void cancelRequest(Player targetPlayer) {
        cancelRequest(mostRecentRequests.get(targetPlayer.getUniqueId()), targetPlayer);
    }

    public void cancelRequest(Player sourcePlayer, Player targetPlayer) {
        TpaRequest tpaRequest = tpaRequests.get(sourcePlayer.getUniqueId());

        if (tpaRequest == null) {
            sourcePlayer.sendMessage(StapleUtil.getErrorComponent("You have not sent a request to " + targetPlayer.getName()));
            return;
        }

        sourcePlayer.sendMessage(Component.text("Request cancelled.", NamedTextColor.GOLD));
        targetPlayer.sendMessage(Component.text(sourcePlayer.getName() + " has cancelled their teleport request.", NamedTextColor.GOLD));
        tpaRequests.remove(sourcePlayer.getUniqueId());
    }
}
