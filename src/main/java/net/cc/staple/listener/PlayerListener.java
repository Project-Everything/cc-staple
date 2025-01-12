package net.cc.staple.listener;

import net.cc.staple.StapleConfig;
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

    private final StaplePlugin staplePlugin;

    public PlayerListener() {
        this.staplePlugin = StaplePlugin.getInstance();
        staplePlugin.getServer().getPluginManager().registerEvents(this, staplePlugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        StaplePlayer staplePlayer = staplePlugin.getPlayerManager().getAndLoad(player);

        if (Objects.equals(StaplePlugin.serverName, "hub")) {
            Location location = StapleConfig.getSpawnLocation(player.getWorld());
            player.teleport(location);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID mojangId = player.getUniqueId();
        StaplePlayer staplePlayer = staplePlugin.getPlayerManager().get(mojangId);
        staplePlugin.getStorage().savePlayer(staplePlayer);
    }
}
