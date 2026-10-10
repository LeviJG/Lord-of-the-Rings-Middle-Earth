package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenMountainsideBush;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenWindMountains.
 */
public class LOTRWindMountainsBiome extends LOTRBiome {

    public LOTRWindMountainsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        npcSpawnList.clear();
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.3f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.3f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_MAPLE, 0.3f);
        decorator.biomeGemFactor = 1.0f;
        decorator.flowersPerChunk = 1;
        decorator.doubleFlowersPerChunk = 0;
        decorator.grassPerChunk = 4;
        decorator.doubleGrassPerChunk = 1;
        decorator.addTree(LOTRTreeType.SPRUCE, 400);
        decorator.addTree(LOTRTreeType.SPRUCE_THIN, 400);
        decorator.addTree(LOTRTreeType.SPRUCE_MEGA, 50);
        decorator.addTree(LOTRTreeType.SPRUCE_MEGA_THIN, 10);
        decorator.addTree(LOTRTreeType.LARCH, 500);
        decorator.addTree(LOTRTreeType.FIR, 500);
        decorator.addTree(LOTRTreeType.PINE, 500);
        decorator.addTree(LOTRTreeType.MAPLE, 300);
        registerMountainsFlowers();
        biomeColors.setSky(11653858);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        for (int l = 0; l < 3; ++l) {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, 70, 160);
            int k1 = k + random.nextInt(16) + 8;
            new LOTRWorldGenMountainsideBush(LOTRLegacyBlocks.mod("leaves5"), 0).generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int snowHeight = 150 - rockDepth;
        int stoneHeight = snowHeight - 40;
        for (int j = ySize - 1; j >= stoneHeight; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState block = blocks[index];
            if (j >= snowHeight && block.is(topBlock.getBlock())) {
                blocks[index] = LOTRLegacyBlocks.vanilla("snow").state(0);
            } else if (block.is(topBlock.getBlock()) || block.is(fillerBlock.getBlock())) {
                blocks[index] = LOTRLegacyBlocks.vanilla("stone").state(0);
            }
            block = blocks[index];
            if (!LOTRLegacyBlocks.vanilla("stone").matches(block)) {
                continue;
            }
            if (random.nextInt(6) == 0) {
                int h = 1 + random.nextInt(6);
                for (int j1 = j; j1 > j - h && j1 > 0; --j1) {
                    int indexH = LOTRChunkTerrain.index(xzIndex, j1);
                    if (!LOTRLegacyBlocks.vanilla("stone").matches(blocks[indexH])) {
                        continue;
                    }
                    blocks[indexH] = LOTRLegacyBlocks.vanilla("stained_hardened_clay").state(9);
                }
                continue;
            }
            if (random.nextInt(16) != 0) {
                continue;
            }
            blocks[index] = LOTRLegacyBlocks.vanilla("clay").state(0);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_MOUNTAINS_WIND;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.RHUN.getSubregion("windMountains");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.RHUN;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.0f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.2f;
    }
}
