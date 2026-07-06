package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

/**
 * Map-pixel <-> world-block coordinate conversion, extracted from the
 * 1.7.10 LOTRWaypoint / LOTRGenLayerWorld classes. Worldgen is not ported,
 * but this conversion is still needed by control zones and the map UI.
 * scale = 2^7 = 128 world blocks per map pixel; origin pixel (810, 730) = world (0,0).
 */
public final class LOTRMapCoords {

    public static final int SCALE_POWER = 7;
    public static final int SCALE = 1 << SCALE_POWER;
    public static final double ORIGIN_X = 810.0;
    public static final double ORIGIN_Z = 730.0;

    private LOTRMapCoords() {
    }

    public static int mapToWorldX(double x) {
        return (int) Math.round((x - ORIGIN_X + 0.5) * SCALE);
    }

    public static int mapToWorldZ(double z) {
        return (int) Math.round((z - ORIGIN_Z + 0.5) * SCALE);
    }

    public static int mapToWorldR(double r) {
        return (int) Math.round(r * SCALE);
    }

    public static int worldToMapR(double r) {
        return (int) Math.round(r / SCALE);
    }
}