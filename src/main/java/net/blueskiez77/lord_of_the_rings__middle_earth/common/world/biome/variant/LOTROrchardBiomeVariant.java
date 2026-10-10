package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoads;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** LOTRBiomeVariantOrchard: rows of fruit trees, two to a chunk, between rows of fence, away from the roads. */
public class LOTROrchardBiomeVariant extends LOTRBiomeVariant {

    public LOTROrchardBiomeVariant(int i, String s) {
        super(i, s, VariantScale.SMALL);
        setTemperatureRainfall(0.1f, 0.2f);
        setHeight(0.0f, 0.4f);
        setTrees(0.0f);
        setGrass(0.5f);
        disableStructuresVillages();
    }

    @Override
    public void decorateVariant(WorldGenLevel world, RandomSource random, int i, int k, LOTRBiome biome) {
        for (int i1 : new int[]{i + 3, i + 11}) {
            int k1 = k + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
            getRandomTree(random).create(false, random).generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generateVariantTerrain(LOTRChunkTerrain terrain, RandomSource random, int i, int k, int height, LOTRBiome biome) {
        int xzIndex = (i & 0xF) * 16 + (k & 0xF);
        if (LOTRRoads.isRoadAt(i, k)) {
            return;
        }
        for (int j = 128; j >= 0; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState above = terrain.blocks[index + 1];
            BlockState block = terrain.blocks[index];
            if (!LOTRChunkTerrain.isOpaque(block) || !above.isAir()) {
                continue;
            }
            int i1 = Mth.positiveModulo(i, 32);
            int k1 = Mth.positiveModulo(k, 16);
            if (i1 == 6 || i1 == 7 || i1 == 8 || k1 != 0) {
                break;
            }
            terrain.blocks[index + 1] = Blocks.OAK_FENCE.defaultBlockState();
            break;
        }
    }
}
