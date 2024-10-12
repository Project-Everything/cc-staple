package com.spektrsoyuz.staple.listener;

import com.spektrsoyuz.staple.StaplePlugin;
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
        plugin.getPlayerManager().load(playerId);
    }
}
