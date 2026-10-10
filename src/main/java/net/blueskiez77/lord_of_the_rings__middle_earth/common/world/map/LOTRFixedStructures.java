package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

/**
 * LOTRFixedStructures: the things built at fixed places on the map -- the spawn point, the
 * entrance to Utumno, the cherry tree in Mordor -- and the map features' say over the biome
 * variants: no variant by a mountain, and no random lake or river near a fixed structure, a
 * waypoint or a road.
 */
public enum LOTRFixedStructures {
    SPAWN(809.5, 729.5),
    UTUMNO_ENTRANCE(1139.0, 394.0),
    MORDOR_CHERRY_TREE(1630.0, 1170.0);

    public final int xCoord;
    public final int zCoord;

    LOTRFixedStructures(double x, double z) {
        this.xCoord = LOTRMapCoords.mapToWorldX(x);
        this.zCoord = LOTRMapCoords.mapToWorldZ(z);
    }

    /**
     * hasMapFeatures: roads, the fixed hills and mountains and the fixed structures, unless the
     * config turns them off (the Middle-earth Classic world type has none either, D10k).
     */
    public static boolean hasMapFeatures() {
        return LOTRConfig.generateMapFeatures;
    }

    /** _mountainNear_structureNear's mountain half. */
    public static boolean mountainNear(int x, int z) {
        return hasMapFeatures() && LOTRMountains.mountainAt(x, z);
    }

    /** _mountainNear_structureNear's structure half: a fixed structure or waypoint within 256 blocks, or a road within 32. */
    public static boolean structureNear(int x, int z) {
        if (!hasMapFeatures()) {
            return false;
        }
        if (structureNear(x, z, 256)) {
            return true;
        }
        for (LOTRWaypoint wp : LOTRWaypoint.values()) {
            double dx = x - wp.xCoord;
            double dz = z - wp.zCoord;
            if (dx * dx + dz * dz < 256.0 * 256.0) {
                return true;
            }
        }
        return LOTRRoads.isRoadNear(x, z, 32) >= 0.0f;
    }

    public static boolean structureNear(int x, int z, int range) {
        for (LOTRFixedStructures str : values()) {
            double dx = x - str.xCoord;
            double dz = z - str.zCoord;
            if (dx * dx + dz * dz < (double) range * range) {
                return true;
            }
        }
        return false;
    }

    /** Whether chunk-relative position (i, k)'s chunk holds (x, z). */
    public static boolean generatesAt(int i, int k, int x, int z) {
        return i >> 4 == x >> 4 && k >> 4 == z >> 4;
    }

    public static boolean generatesAtMapImageCoords(int i, int k, double x, double z) {
        return generatesAt(i, k, LOTRMapCoords.mapToWorldX(x), LOTRMapCoords.mapToWorldZ(z));
    }

    public boolean isAt(int x, int z) {
        return hasMapFeatures() && this.xCoord == x && this.zCoord == z;
    }
}
