package net.cc.staple.listener;

import net.cc.staple.StaplePlugin;
import net.cc.staple.player.StaplePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;
import java.util.UUID;

public final class PlayerListener implements Listener {

    private final StaplePlugin plugin;

    public PlayerListener(final StaplePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        StaplePlayer staplePlayer = plugin.getPlayerManager().loadPlayer(player);

        if (Objects.equals(plugin.getConfigManager().getServerName(), "hub")) {
            Location location = plugin.getConfigManager().getSpawnLocation();
            if (location != null) {
                player.teleport(location);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID mojangId = player.getUniqueId();
        StaplePlayer staplePlayer = plugin.getPlayerManager().getPlayer(mojangId);
        plugin.getDatabaseManager().savePlayer(staplePlayer);
    }
}
