package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

/**
 * Faithful port of 1.7.10 lotr.common.fac.LOTRMapRegion.
 * Faction area-of-influence circle in map-pixel coordinates.
 * Was a mutable POJO; a record is the faithful modern equivalent
 * since it was never mutated after construction.
 */
public record LOTRMapRegion(int mapX, int mapY, int radius) {
}