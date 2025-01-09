package net.cc.staple.player;

import net.cc.staple.StaplePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerManager {

    private final StaplePlugin staplePlugin;
    private final Map<UUID, StaplePlayer> cache;

    public PlayerManager() {
        this.staplePlugin = StaplePlugin.getInstance();
        this.cache = new ConcurrentHashMap<>();
    }

    public StaplePlayer getAndLoad(Player player) {
        return getAndLoad(player.getUniqueId());
    }

    public StaplePlayer getAndLoad(UUID mojangId) {
        if (cache.containsKey(mojangId)) {
            return cache.get(mojangId);
        }

        Player player = Bukkit.getPlayer(mojangId);
        if (player != null) {
            StaplePlayer staplePlayer = new StaplePlayer(mojangId, false, player.getLocation());

            // Fetch staple player
            staplePlugin.getStorage().queryPlayer(mojangId).thenAccept(staplePlayerQuery -> {
                if (staplePlayerQuery.hasResults()) {
                    StaplePlayer found = staplePlayerQuery.getFirst();
                    staplePlayer.setTpDisabled(found.isTpDisabled());
                    staplePlayer.setOldLocation(found.getOldLocation());
                } else {
                    staplePlugin.getStorage().savePlayer(staplePlayer);
                }
            });

            cache.put(mojangId, staplePlayer);
            return staplePlayer;
        }
        return null;
    }

    public StaplePlayer get(UUID mojangId) {
        return cache.get(mojangId);
    }

    public StaplePlayer get(Player player) {
        return get(player.getUniqueId());
    }
}
