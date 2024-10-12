package com.spektrsoyuz.staple.player;

import java.util.UUID;

public final class StaplePlayer {

    private final UUID playerId;
    private boolean tpDisabled;
    private boolean vanished;

    public StaplePlayer(final UUID playerId) {
        this.playerId = playerId;
    }

    public StaplePlayer(final UUID playerId, final boolean tpDisabled, final boolean vanished) {
        this.playerId = playerId;
        this.tpDisabled = tpDisabled;
        this.vanished = vanished;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public boolean isTpDisabled() {
        return tpDisabled;
    }

    public void setTpDisabled(boolean tpDisabled) {
        this.tpDisabled = tpDisabled;
    }

    public boolean isVanished() {
        return vanished;
    }

    public void setVanished(boolean vanished) {
        this.vanished = vanished;
    }
}
