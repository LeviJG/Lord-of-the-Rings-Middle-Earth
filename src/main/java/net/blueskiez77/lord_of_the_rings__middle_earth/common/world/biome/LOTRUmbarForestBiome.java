package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;

/**
 * LOTRBiomeGenUmbarForest.
 */
public class LOTRUmbarForestBiome extends LOTRUmbarBiome {

    public LOTRUmbarForestBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 7;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 12;
        decorator.doubleGrassPerChunk = 4;
        registerForestFlowers();
        decorator.clearVillages();
    }

    @Override
    public int spawnCountMultiplier() {
        return super.spawnCountMultiplier() * 2;
    }
}
