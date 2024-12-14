package net.cc.staple.listener;

import net.cc.staple.StaplePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

        if (plugin.isHubServer()) {
            String worldName = player.getWorld().getName();
            Location location = new Location(Bukkit.getWorld(worldName),
                    plugin.getConfig().getDouble("spawn-x"),
                    plugin.getConfig().getDouble("spawn-y"),
                    plugin.getConfig().getDouble("spawn-z"));
            // Teleport player to spawn
            player.teleport(location);
        }
    }
}
