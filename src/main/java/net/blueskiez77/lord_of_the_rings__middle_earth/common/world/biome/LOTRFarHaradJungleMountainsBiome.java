package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenStreams;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenVolcanoCrater;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenFarHaradJungleMountains.
 */
public class LOTRFarHaradJungleMountainsBiome extends LOTRFarHaradJungleBiome {

    public LOTRFarHaradJungleMountainsBiome(int i, boolean major) {
        super(i, major);
        obsidianGravelRarity = 5;
        npcSpawnList.clear();
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        decorator.biomeGemFactor = 1.0f;
        decorator.treesPerChunk = 0;
        biomeColors.setSky(10659994);
        biomeColors.setFog(9805451);
        invasionSpawns.clearInvasions();
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        int j1;
        int l;
        super.decorate(world, random, i, k);
        for (l = 0; l < 3; ++l) {
            int craters = 1 + random.nextInt(3);
            for (int l1 = 0; l1 < craters; ++l1) {
                int i1 = i + random.nextInt(16) + 8;
                int j12 = LOTRWorldGenUtil.getHeightValue(world, i1, k1 = k + random.nextInt(16) + 8);
                if (j12 <= 110) {
                    continue;
                }
                new LOTRWorldGenVolcanoCrater().generate(world, random, i1, j12, k1);
            }
        }
        for (l = 0; l < 12; ++l) {
            int k12;
            int i1 = i + random.nextInt(16) + 8;
            j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k12 = k + random.nextInt(16) + 8);
            if (j1 >= 120 && random.nextInt(20) != 0) {
                continue;
            }
            decorator.genTree(world, random, i1, j1, k12);
        }
        LOTRWorldGenStreams lavaGen = new LOTRWorldGenStreams(LOTRLegacyBlocks.vanilla("flowing_lava"));
        for (int l2 = 0; l2 < 5; ++l2) {
            int i1 = i + random.nextInt(16) + 8;
            j1 = 140 + random.nextInt(50);
            k1 = k + random.nextInt(16) + 8;
            lavaGen.generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int stoneHeight = 120 - rockDepth;
        boolean generateMud = false;
        int muds = 0;
        double d1 = BIOME_TERRAIN_NOISE.getValue(i * 0.09, k * 0.09);
        if (d1 + BIOME_TERRAIN_NOISE.getValue(i * 0.4, k * 0.4) > 0.4) {
            generateMud = true;
        }
        for (int j = ySize - 1; j >= stoneHeight; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState block = blocks[index];
            if (!block.is(topBlock.getBlock()) && !block.is(fillerBlock.getBlock()) || generateMud) {
                continue;
            }
            blocks[index] = LOTRLegacyBlocks.vanilla("stone").state(0);
        }
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public boolean hasJungleLakes() {
        return false;
    }
}
