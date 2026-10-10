package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;

import net.minecraft.world.entity.EntityTypes;

/**
 * LOTRBiomeGenGondorWoodlands.
 */
public class LOTRGondorWoodlandsBiome extends LOTRGondorBiome {

    public LOTRGondorWoodlandsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 8, 4, 8));
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 6;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 4;
        registerForestFlowers();
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.75f;
    }

    @Override
    public int spawnCountMultiplier() {
        return super.spawnCountMultiplier() * 2;
    }
}
