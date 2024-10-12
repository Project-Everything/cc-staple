package com.spektrsoyuz.staple.player;

import java.util.UUID;

public final class StaplePlayer {

    private final UUID playerId;
    private boolean tpDisabled;

    public StaplePlayer(final UUID playerId) {
        this.playerId = playerId;
    }

    public StaplePlayer(final UUID playerId, final boolean tpDisabled) {
        this.playerId = playerId;
        this.tpDisabled = tpDisabled;
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
}
