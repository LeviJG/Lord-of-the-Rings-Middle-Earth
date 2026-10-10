package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenShrub extends LOTRFeature {
    public LegacyBlock woodBlock;
    public int woodMeta;
    public LegacyBlock leafBlock;
    public int leafMeta;

    public LOTRWorldGenShrub(LegacyBlock w1, int w2, LegacyBlock l1, int l2) {
        super(false);
        woodBlock = w1;
        woodMeta = w2;
        leafBlock = l1;
        leafMeta = l2;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState block;
        //noinspection StatementWithEmptyBody
        while ((isLeaves((block = getBlock(world, i, j, k))) || block.isAir()) && --j > 0) {
        }
        if (canSustainPlant(world, i, j, k)) {
            j++;
            setBlockAndNotifyAdequately(world, i, j, k, woodBlock, woodMeta);
            for (int j1 = j; j1 <= j + 2; ++j1) {
                int j2 = j1 - j;
                int range = 2 - j2;
                for (int i1 = i - range; i1 <= i + range; ++i1) {
                    for (int k1 = k - range; k1 <= k + range; ++k1) {
                        int i2 = i1 - i;
                        int k2 = k1 - k;
                        if (Math.abs(i2) == range && Math.abs(k2) == range && random.nextInt(2) == 0 || !canBeReplacedByLeaves(getBlock(world, i1, j1, k1))) {
                            continue;
                        }
                        setBlockAndNotifyAdequately(world, i1, j1, k1, leafBlock, leafMeta);
                    }
                }
            }
        }
        return true;
    }
}
