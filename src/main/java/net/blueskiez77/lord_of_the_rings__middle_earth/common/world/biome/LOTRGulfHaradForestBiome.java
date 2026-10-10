package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;

/**
 * LOTRBiomeGenGulfHaradForest.
 */
public class LOTRGulfHaradForestBiome extends LOTRGulfHaradBiome {

    public LOTRGulfHaradForestBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 5;
        decorator.addTree(LOTRTreeType.DRAGONBLOOD, 1000);
        decorator.addTree(LOTRTreeType.DRAGONBLOOD_LARGE, 400);
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
