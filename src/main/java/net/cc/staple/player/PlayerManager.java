package net.cc.staple.player;

import net.cc.staple.StaplePlugin;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerManager {

    private final StaplePlugin plugin;
    private final Map<UUID, StaplePlayer> cache;

    public PlayerManager(StaplePlugin plugin) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();

        loadAll();
    }

    public StaplePlayer loadPlayer(Player player) {
        UUID mojangId = player.getUniqueId();

        if (cache.containsKey(mojangId)) {
            return cache.get(mojangId);
        }

        StaplePlayer staplePlayer = new StaplePlayer(mojangId, false, player.getLocation());

        plugin.getDatabaseManager().queryPlayer(mojangId).thenAccept(staplePlayerQuery -> {
            if (staplePlayerQuery.hasResults()) {
                StaplePlayer found = staplePlayerQuery.getFirst();
                staplePlayer.setTpDisabled(found.isTpDisabled());
                staplePlayer.setOldLocation(found.getOldLocation());
            } else {
                plugin.getDatabaseManager().savePlayer(staplePlayer);
            }
        });

        cache.put(mojangId, staplePlayer);
        return staplePlayer;
    }

    public StaplePlayer getPlayer(UUID mojangId) {
        return cache.get(mojangId);
    }

    public StaplePlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    private void loadAll() {
        plugin.getDatabaseManager().queryPlayers().thenAccept(staplePlayerQuery -> {
            if (staplePlayerQuery.hasResults()) {
                for (StaplePlayer staplePlayer : staplePlayerQuery.getResults()) {
                    cache.put(staplePlayer.getMojangId(), staplePlayer);
                }
            }
        });
    }
}
