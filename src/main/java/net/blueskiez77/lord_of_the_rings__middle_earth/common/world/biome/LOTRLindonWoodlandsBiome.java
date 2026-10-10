package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;

/**
 * LOTRBiomeGenLindonWoodlands.
 */
public class LOTRLindonWoodlandsBiome extends LOTRLindonBiome {

    public LOTRLindonWoodlandsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 30, 4, 6));
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 6;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 3;
        registerForestFlowers();
    }
}
