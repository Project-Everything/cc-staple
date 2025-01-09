package net.cc.staple.storage;

import net.cc.staple.player.StaplePlayer;
import net.cc.staple.storage.query.StaplePlayerQuery;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"unused"})

public abstract class Storage {

    public abstract CompletableFuture<Void> savePlayer(StaplePlayer staplePlayer);

    public abstract CompletableFuture<StaplePlayerQuery> queryPlayer(UUID mojangId);

    public abstract CompletableFuture<StaplePlayerQuery> queryPlayers();
}
