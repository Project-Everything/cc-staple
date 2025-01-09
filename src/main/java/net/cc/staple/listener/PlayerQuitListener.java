package net.cc.staple.listener;

import net.cc.staple.StaplePlugin;
import net.cc.staple.player.StaplePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public final class PlayerQuitListener implements Listener {

    private final StaplePlugin staplePlugin;

    public PlayerQuitListener() {
        this.staplePlugin = StaplePlugin.getInstance();
        staplePlugin.getServer().getPluginManager().registerEvents(this, staplePlugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID mojangId = player.getUniqueId();
        StaplePlayer staplePlayer = staplePlugin.getPlayerManager().get(mojangId);
        staplePlugin.getStorage().savePlayer(staplePlayer);
    }
}
