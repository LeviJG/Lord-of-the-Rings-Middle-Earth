package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenSeaBlock extends LOTRFeature {
    public LegacyBlock theBlock;
    public int theMeta;
    public int tries;

    public LOTRWorldGenSeaBlock(LegacyBlock block, int i, int t) {
        theBlock = block;
        theMeta = i;
        tries = t;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int l = 0; l < tries; ++l) {
            int i1 = i - random.nextInt(6) + random.nextInt(6);
            int j1 = j - random.nextInt(4) + random.nextInt(4);
            int k1 = k - random.nextInt(6) + random.nextInt(6);
            BlockState below = getBlock(world, i1, j1 - 1, k1);
            BlockState block = getBlock(world, i1, j1, k1);
            // Material.sand (sand, gravel) or Material.ground (dirt) under the water.
            if (!below.is(net.minecraft.tags.BlockTags.SAND) && !below.is(net.minecraft.world.level.block.Blocks.GRAVEL)
                    && !below.is(net.minecraft.tags.BlockTags.DIRT) || !isWater(block)) {
                continue;
            }
            setBlock(world, i1, j1, k1, theBlock, theMeta, 2);
        }
        return true;
    }
}
