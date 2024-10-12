package com.spektrsoyuz.staple.player;

import com.spektrsoyuz.staple.StaplePlugin;
import com.spektrsoyuz.staple.util.CacheManager;

import java.util.UUID;
import java.util.function.Consumer;

public final class PlayerManager extends CacheManager<StaplePlayer> {

    private final StaplePlugin plugin;

    public PlayerManager() {
        this.plugin = StaplePlugin.getInstance();
    }

    @Override
    public StaplePlayer createInstance(UUID playerId) {
        return new StaplePlayer(playerId);
    }

    @Override
    public void loadData(UUID playerId, StaplePlayer instance, Consumer<StaplePlayer> callback) {
        plugin.getStorage().queryPlayer(playerId).thenAccept(staplePlayerQuery -> {
            if (staplePlayerQuery.hasResults()) {
                StaplePlayer existing = staplePlayerQuery.getFirst();
                instance.setTpDisabled(existing.isTpDisabled());
                instance.setVanished(existing.isVanished());
            } else {
                instance.setTpDisabled(false);
                instance.setVanished(false);

                plugin.getStorage().addPlayer(instance);
            }

            callback.accept(instance);
        });
    }

    @Override
    public void saveData(StaplePlayer instance) {
        plugin.getStorage().savePlayer(instance);
    }
}
