package net.cc.staple.player;

import org.bukkit.Location;

import java.util.UUID;

public final class StaplePlayer {

    private final UUID mojangId;
    private boolean tpDisabled;
    private Location oldLocation;

    public StaplePlayer(UUID mojangId, boolean tpDisabled, Location oldLocation) {
        this.mojangId = mojangId;
        this.tpDisabled = tpDisabled;
        this.oldLocation = oldLocation;
    }

    public UUID getMojangId() {
        return mojangId;
    }

    public boolean isTpDisabled() {
        return tpDisabled;
    }

    public void setTpDisabled(boolean tpDisabled) {
        this.tpDisabled = tpDisabled;
    }

    public Location getOldLocation() {
        return oldLocation;
    }

    public void setOldLocation(Location oldLocation) {
        this.oldLocation = oldLocation;
    }
}
