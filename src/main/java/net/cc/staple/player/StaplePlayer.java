package net.cc.staple.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@Setter
public final class StaplePlayer {

    private final UUID mojangId;
    private boolean tpDisabled;
    private Location oldLocation;

    public StaplePlayer(UUID mojangId, boolean tpDisabled, Location oldLocation) {
        this.mojangId = mojangId;
        this.tpDisabled = tpDisabled;
        this.oldLocation = oldLocation;
    }
}
