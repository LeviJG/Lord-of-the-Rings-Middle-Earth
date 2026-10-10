package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;

/**
 * LOTRBiomeGenNearHaradOasis.
 */
public class LOTRNearHaradOasisBiome extends LOTRNearHaradRiverbankBiome {

    public LOTRNearHaradOasisBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        decorator.treesPerChunk = 3;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 4;
        decorator.flowersPerChunk = 5;
        decorator.doubleFlowersPerChunk = 2;
        decorator.addTree(LOTRTreeType.DATE_PALM, 2000);
        decorator.addTree(LOTRTreeType.OLIVE, 500);
        decorator.addTree(LOTRTreeType.OLIVE_LARGE, 200);
        decorator.addTree(LOTRTreeType.OAK_SHRUB, 3000);
        decorator.clearRandomStructures();
        decorator.clearVillages();
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_NEAR_HARAD_OASIS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.NEAR_HARAD.getSubregion("oasis");
    }

    @Override
    public boolean hasMixedHaradSoils() {
        return false;
    }
}
