package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;

import net.minecraft.world.entity.EntityTypes;

/**
 * LOTRBiomeGenErynVorn.
 */
public class LOTRErynVornBiome extends LOTREriadorBiome {

    public LOTRErynVornBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 8, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 10, 1, 4));
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 10;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 1;
        decorator.doubleGrassPerChunk = 2;
        decorator.addTree(LOTRTreeType.PINE, 1000);
        decorator.addTree(LOTRTreeType.FIR, 200);
        decorator.addTree(LOTRTreeType.SPRUCE, 200);
        registerForestFlowers();
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.5f;
    }
}
