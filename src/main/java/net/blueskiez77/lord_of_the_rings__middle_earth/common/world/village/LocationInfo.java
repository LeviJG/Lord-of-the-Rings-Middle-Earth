package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

import org.jspecify.annotations.Nullable;

/**
 * LocationInfo: where a village is, and why -- generated at random, spawned
 * by a player, or one of the fixed settlements at a waypoint (Bree and the
 * like), which keep a name and rotation of their own.
 */
public class LocationInfo {
    public static final LocationInfo RANDOM_GEN_HERE = new LocationInfo(0, 0, 0, "RANDOM_GEN");
    public static final LocationInfo SPAWNED_BY_PLAYER = new LocationInfo(0, 0, 0, "PLAYER_SPAWNED");
    public static final LocationInfo NONE_HERE = new LocationInfo(0, 0, 0, "NONE") {
        @Override
        public boolean isPresent() {
            return false;
        }
    };

    public int posX;
    public int posZ;
    public int rotation;
    public String name;
    public boolean isFixedLocation;
    public @Nullable LOTRWaypoint associatedWaypoint;

    public LocationInfo(int x, int z, int r, String s) {
        this.posX = x;
        this.posZ = z;
        this.rotation = r;
        this.name = s;
    }

    /** The waypoint a fixed settlement belongs to. */
    public @Nullable LOTRWaypoint getAssociatedWaypoint() {
        return this.associatedWaypoint;
    }

    public boolean isFixedLocation() {
        return this.isFixedLocation;
    }

    public LocationInfo setFixedLocation(LOTRWaypoint waypoint) {
        this.isFixedLocation = true;
        this.associatedWaypoint = waypoint;
        return this;
    }

    public boolean isPresent() {
        return true;
    }
}
