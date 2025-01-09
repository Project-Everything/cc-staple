package net.cc.staple.listener;

import net.cc.staple.StapleConfig;
import net.cc.staple.StaplePlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public final class PlayerJoinListener implements Listener {

    private final StaplePlugin staplePlugin;

    public PlayerJoinListener() {
        this.staplePlugin = StaplePlugin.getInstance();
        staplePlugin.getServer().getPluginManager().registerEvents(this, staplePlugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        staplePlugin.getPlayerManager().getAndLoad(player);

        if (Objects.equals(StaplePlugin.serverName, "hub")) {
            Location location = StapleConfig.getSpawnLocation(player.getWorld());
            player.teleport(location);
        }
    }
}
