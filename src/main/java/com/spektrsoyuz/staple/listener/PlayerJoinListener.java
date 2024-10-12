package com.spektrsoyuz.staple.listener;

import com.spektrsoyuz.staple.StaplePlugin;
import com.spektrsoyuz.staple.player.StaplePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public final class PlayerJoinListener implements Listener {

    private final StaplePlugin plugin;

    public PlayerJoinListener() {
        this.plugin = StaplePlugin.getInstance();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        plugin.getLogger().info("Player " + player.getName() + " joined. Loading into cache.");

        // Retrieve StaplePlayer instance from storage, load into cache
        plugin.getPlayerManager().getAndLoad(playerId, staplePlayer -> {

            // Check if player is vanished
            boolean isVanished = staplePlayer.isVanished();
            for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                // Hide player from online player
                if (isVanished) {
                    onlinePlayer.hidePlayer(plugin, player);
                }

                // Hide online player from player
                StaplePlayer onlineStaplePlayer = plugin.getPlayerManager().get(onlinePlayer.getUniqueId());
                if (onlineStaplePlayer != null) {
                    if (onlineStaplePlayer.isVanished()) {
                        player.hidePlayer(plugin, onlinePlayer);
                    }
                }
            }

        });
    }
}
