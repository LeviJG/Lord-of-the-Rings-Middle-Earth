package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;

import net.minecraft.world.entity.EntityTypes;

/**
 * LOTRBiomeGenRhunForest.
 */
public class LOTRRhunForestBiome extends LOTRRhunBiome {

    public LOTRRhunForestBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 16, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 20, 4, 6));
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 8;
        decorator.logsPerChunk = 1;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 2;
        decorator.addTree(LOTRTreeType.OAK_LARGE, 2000);
        decorator.addTree(LOTRTreeType.OAK_PARTY, 100);
        registerRhunForestFlowers();
        biomeColors.resetGrass();
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.5f;
    }
}
