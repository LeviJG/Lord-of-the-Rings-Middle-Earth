package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenBanana extends LOTRFeature {
    public LOTRWorldGenBanana(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int l1;
        net.minecraft.core.Direction dir;
        int l;
        int height = 2 + random.nextInt(3);
        int[] leaves = new int[4];
        for (int l2 = 0; l2 < 4; ++l2) {
            leaves[l2] = 1 + random.nextInt(3);
        }
        if (j < 1 || j + height + 5 > 256 || !isReplaceable(world, i, j, k)) {
            return false;
        }
        BlockState below = getBlock(world, i, j - 1, k);
        if (!canSustainPlant(world, i, j - 1, k)) {
            return false;
        }
        for (l = 0; l < height + 2; ++l) {
            if (isReplaceable(world, i, j + l, k)) {
                continue;
            }
            return false;
        }
        for (l = 0; l < 4; ++l) {
            dir = net.minecraft.core.Direction.from3DDataValue(l + 2);
            for (l1 = -1; l1 < leaves[l]; ++l1) {
                if (isReplaceable(world, i + dir.getStepX(), j + height + l1, k + dir.getStepZ())) {
                    continue;
                }
                return false;
            }
            for (l1 = -1; l1 < 1; ++l1) {
                if (isReplaceable(world, i + dir.getStepX() * 2, j + height + leaves[l] + l1, k + dir.getStepZ() * 2)) {
                    continue;
                }
                return false;
            }
        }
        for (l = 0; l < height + 2; ++l) {
            setBlockAndNotifyAdequately(world, i, j + l, k, LOTRLegacyBlocks.mod("wood2"), 3);
        }
        for (l = 0; l < 4; ++l) {
            dir = net.minecraft.core.Direction.from3DDataValue(l + 2);
            for (l1 = 0; l1 < leaves[l]; ++l1) {
                setBlockAndNotifyAdequately(world, i + dir.getStepX(), j + height + l1, k + dir.getStepZ(), LOTRLegacyBlocks.mod("leaves2"), 3);
            }
            setBlockAndNotifyAdequately(world, i + dir.getOpposite().getStepX(), j + height - 1, k + dir.getOpposite().getStepZ(), LOTRLegacyBlocks.mod("bananaBlock"), l);
            for (l1 = -1; l1 < 1; ++l1) {
                setBlockAndNotifyAdequately(world, i + dir.getStepX() * 2, j + height + leaves[l] + l1, k + dir.getStepZ() * 2, LOTRLegacyBlocks.mod("leaves2"), 3);
            }
        }
        onPlantGrow(world, i, j - 1, k);
        return true;
    }
}
