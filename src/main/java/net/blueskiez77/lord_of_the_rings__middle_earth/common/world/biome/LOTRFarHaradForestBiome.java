package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;

/**
 * LOTRBiomeGenFarHaradForest.
 */
public class LOTRFarHaradForestBiome extends LOTRFarHaradSavannahBiome {

    public LOTRFarHaradForestBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        variantChance = 0.4f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 7;
        decorator.vinesPerChunk = 10;
        decorator.logsPerChunk = 3;
        decorator.grassPerChunk = 8;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 3;
        decorator.melonPerChunk = 0.08f;
        biomeColors.setGrass(11659848);
        biomeColors.setFoliage(8376636);
    }
}
