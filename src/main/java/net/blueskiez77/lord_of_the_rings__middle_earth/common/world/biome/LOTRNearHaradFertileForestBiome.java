package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;

/**
 * LOTRBiomeGenNearHaradFertileForest.
 */
public class LOTRNearHaradFertileForestBiome extends LOTRNearHaradFertileBiome {

    public LOTRNearHaradFertileForestBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 6;
        decorator.addTree(LOTRTreeType.CEDAR, 6000);
        decorator.addTree(LOTRTreeType.CEDAR_LARGE, 1500);
        decorator.clearRandomStructures();
        decorator.clearVillages();
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 1.0f;
    }

    @Override
    public int spawnCountMultiplier() {
        return super.spawnCountMultiplier() * 2;
    }
}
