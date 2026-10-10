package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenDoubleFlower;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenFarHarad.
 */
public abstract class LOTRFarHaradBiome extends LOTRBiome {

    protected LOTRFarHaradBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.LION, 4, 2, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.LIONESS, 4, 2, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.GIRAFFE, 4, 4, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.ZEBRA, 8, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.RHINO, 8, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.GEMSBOK, 8, 4, 8));
        spawnableLOTRAmbientList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 5, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BIRD, 8, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.DIK_DIK, 8, 4, 6));
        spawnableMonsterList.add(new LOTRSpawnEntry(LOTREntities.CROCODILE, 10, 4, 4));
        npcSpawnList.clear();
        decorator.biomeGemFactor = 0.75f;
        decorator.treesPerChunk = 0;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 12;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.addTree(LOTRTreeType.ACACIA, 1000);
        decorator.addTree(LOTRTreeType.OAK_DESERT, 300);
        decorator.addTree(LOTRTreeType.BAOBAB, 20);
        decorator.addTree(LOTRTreeType.MANGO, 1);
        registerHaradFlowers();
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        LOTRBiomeVariant variant = LOTRWorldChunkManager.of(world).getBiomeVariantAt(i + 8, k + 8);
        if (variant == LOTRBiomeVariant.RIVER && random.nextInt(3) == 0) {
            LOTRWorldGenerator bananaTree = LOTRTreeType.BANANA.create(false, random);
            int bananas = 3 + random.nextInt(8);
            for (int l = 0; l < bananas; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                int j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1, k1);
                bananaTree.generate(world, random, i1, j1, k1);
            }
        }
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.FAR_HARAD;
    }

    @Override
    public LOTRWorldGenerator getRandomWorldGenForDoubleFlower(RandomSource random) {
        LOTRWorldGenDoubleFlower doubleFlowerGen = new LOTRWorldGenDoubleFlower();
        if (random.nextInt(5) == 0) {
            doubleFlowerGen.setFlowerType(3);
        } else {
            doubleFlowerGen.setFlowerType(2);
        }
        return doubleFlowerGen;
    }
}
