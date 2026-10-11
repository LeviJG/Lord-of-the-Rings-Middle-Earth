package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

/**
 * LOTRAbstractWaypoint: a place one may fast travel to -- one of Middle-earth's fixed waypoints, or a
 * player's own (custom) one.
 */
public interface LOTRAbstractWaypoint {

    String getCodeName();

    Component getDisplayName();

    int getID();

    WaypointLockState getLockState(Player player);

    Component getLoreText(Player player);

    /** The map image's coordinates. */
    double getX();

    double getY();

    /** The world's coordinates. */
    int getXCoord();

    /** Where one arrives, worked out in the level. */
    int getYCoord(ServerLevel level, int i, int k);

    /** The height counted in the travel time's distance. */
    int getYCoordSaved();

    int getZCoord();

    boolean hasPlayerUnlocked(Player player);

    boolean isHidden();

    /** The map icon of each state: its place on the map's sheet. */
    enum WaypointLockState {
        STANDARD_LOCKED(0, 200), STANDARD_UNLOCKED(4, 200), STANDARD_UNLOCKED_CONQUEST(8, 200),
        CUSTOM_LOCKED(0, 204), CUSTOM_UNLOCKED(4, 204), SHARED_CUSTOM_LOCKED(0, 208), SHARED_CUSTOM_UNLOCKED(4, 208);

        public final int iconU;
        public final int iconV;

        WaypointLockState(int u, int v) {
            this.iconU = u;
            this.iconV = v;
        }
    }
}
