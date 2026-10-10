package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

/**
 * LOTRBiomeGenRhunIsland.
 */
public class LOTRRhunIslandBiome extends LOTRRhunLandBiome {

    public LOTRRhunIslandBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK);
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
