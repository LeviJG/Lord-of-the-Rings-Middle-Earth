package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

/**
 * LOTRBiomeGenRhunIslandForest.
 */
public class LOTRRhunIslandForestBiome extends LOTRRhunRedForestBiome {

    public LOTRRhunIslandForestBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        decorator.treesPerChunk = 10;
        biomeColors.setFog(6132078);
        decorator.clearRandomStructures();
        decorator.clearVillages();
        clearTravellingTraders();
        invasionSpawns.clearInvasions();
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_RHUN_ISLAND;
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.TOL_RHUNAER;
    }
}
