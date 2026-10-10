package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenStalactites extends LOTRFeature {
    public LegacyBlock stalactiteBlock;

    public LOTRWorldGenStalactites() {
        this(LOTRLegacyBlocks.mod("stalactite"));
    }

    public LOTRWorldGenStalactites(LegacyBlock block) {
        stalactiteBlock = block;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int l = 0; l < 64; ++l) {
            int j1;
            int k1;
            int i1 = i - random.nextInt(8) + random.nextInt(8);
            if (!isAirBlock(world, i1, j1 = j - random.nextInt(4) + random.nextInt(4), k1 = k - random.nextInt(8) + random.nextInt(8))) {
                continue;
            }
            BlockState above = getBlock(world, i1, j1 + 1, k1);
            BlockState below = getBlock(world, i1, j1 - 1, k1);
            if (isOpaqueCube(above) && isRock(above)) {
                setBlock(world, i1, j1, k1, stalactiteBlock, 0, 2);
                continue;
            }
            if (!isOpaqueCube(below) || !isRock(below)) {
                continue;
            }
            setBlock(world, i1, j1, k1, stalactiteBlock, 1, 2);
        }
        return true;
    }
}
