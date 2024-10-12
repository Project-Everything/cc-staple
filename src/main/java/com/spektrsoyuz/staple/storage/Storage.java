package com.spektrsoyuz.staple.storage;

import com.spektrsoyuz.staple.player.StaplePlayer;
import com.spektrsoyuz.staple.storage.query.StaplePlayerQuery;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class Storage {

    public abstract CompletableFuture<Void> addPlayer(StaplePlayer staplePlayer);

    public abstract CompletableFuture<Void> savePlayer(StaplePlayer staplePlayer);

    public abstract CompletableFuture<StaplePlayerQuery> queryPlayer(UUID playerId);
}
