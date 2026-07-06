package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapCoords;

/**
 * Faithful port of 1.7.10 lotr.common.fac.LOTRControlZone.
 * Circular territory zone (map pixels, cached in world blocks). Logic is
 * identical to the original; only the coordinate conversion moved to
 * LOTRMapCoords. The waypoint-based constructor is deferred until waypoints port.
 */
public class LOTRControlZone {
    public final double mapX;
    public final double mapY;
    public final int radius;
    public final int xCoord;
    public final int zCoord;
    public final int radiusCoord;
    public final long radiusCoordSq;

    public LOTRControlZone(double x, double y, int r) {
        mapX = x;
        mapY = y;
        radius = r;
        xCoord = LOTRMapCoords.mapToWorldX(mapX);
        zCoord = LOTRMapCoords.mapToWorldZ(mapY);
        radiusCoord = LOTRMapCoords.mapToWorldR(radius);
        radiusCoordSq = (long) radiusCoord * radiusCoord;
    }

    public boolean intersectsWith(LOTRControlZone other, int extraMapRadius) {
        double dx = other.xCoord - xCoord;
        double dz = other.zCoord - zCoord;
        double distSq = dx * dx + dz * dz;
        double r12 = radiusCoord + other.radiusCoord + LOTRMapCoords.mapToWorldR(extraMapRadius * 2);
        double r12Sq = r12 * r12;
        return distSq <= r12Sq;
    }

    public boolean inZone(double x, double y, double z, int extraMapRange) {
        double dx = x - xCoord;
        double dz = z - zCoord;
        double distSq = dx * dx + dz * dz;
        if (extraMapRange == 0) {
            return distSq <= radiusCoordSq;
        }
        int checkRadius = LOTRMapCoords.mapToWorldR(radius + extraMapRange);
        long checkRadiusSq = (long) checkRadius * checkRadius;
        return distSq <= checkRadiusSq;
    }
}