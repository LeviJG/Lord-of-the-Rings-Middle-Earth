package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

/**
 * LOTRBiomeGenDorwinionHills.
 */
public class LOTRDorwinionHillsBiome extends LOTRDorwinionBiome {

    public LOTRDorwinionHillsBiome(int i, boolean major) {
        super(i, major);
        fillerBlock = LOTRLegacyBlocks.mod("rock").state(5);
        biomeTerrain.setXZScale(50.0);
        clearBiomeVariants();
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        decorator.flowersPerChunk = 3;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 5;
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRStoneRuinStructure.DORWINION(1, 4), 800);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_DORWINION_HILLS;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }

    @Override
    public boolean hasDomesticAnimals() {
        return false;
    }

    @Override
    public int spawnCountMultiplier() {
        return super.spawnCountMultiplier() * 3;
    }
}
