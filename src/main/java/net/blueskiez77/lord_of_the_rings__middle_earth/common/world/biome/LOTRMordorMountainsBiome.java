package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRMordorTowerStructure;

/**
 * LOTRBiomeGenMordorMountains.
 */
public class LOTRMordorMountainsBiome extends LOTRMordorBiome {

    public LOTRMordorMountainsBiome(int i, boolean major) {
        super(i, major);
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRMordorTowerStructure(false), 400);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return null;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.MORDOR.getSubregion("mountains");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return null;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }
}
