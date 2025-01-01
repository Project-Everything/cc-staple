package net.cc.staple.listener;

import net.cc.staple.StaplePlugin;
import net.cc.staple.player.StaplePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public final class PlayerQuitListener implements Listener {

    private final StaplePlugin plugin;

    public PlayerQuitListener() {
        this.plugin = StaplePlugin.getInstance();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        plugin.getLogger().info("Player " + player.getName() + " left. Removing from cache.");
        StaplePlayer staplePlayer = plugin.getPlayerManager().get(playerId);

        plugin.getStorage().savePlayer(staplePlayer);
        plugin.getPlayerManager().remove(playerId);
    }
}
