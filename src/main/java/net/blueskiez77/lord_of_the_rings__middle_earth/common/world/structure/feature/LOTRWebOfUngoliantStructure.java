package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRWebOfUngoliantStructure extends LOTRStructureBase {
    public int attempts;

    public LOTRWebOfUngoliantStructure(boolean flag, int i) {
        super(flag);
        attempts = i;
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int l = 0; l < attempts; ++l) {
            int j1;
            int k1;
            int i1 = i - random.nextInt(8) + random.nextInt(8);
            if (!world.isEmptyBlock(new BlockPos(i1, j1 = j - random.nextInt(6) + random.nextInt(6), k1 = k - random.nextInt(8) + random.nextInt(8)))) {
                continue;
            }
            boolean flag = isSuitableBlock(world, i1 - 1, j1, k1);
            if (isSuitableBlock(world, i1 + 1, j1, k1)) {
                flag = true;
            }
            if (isSuitableBlock(world, i1, j1 - 1, k1)) {
                flag = true;
            }
            if (isSuitableBlock(world, i1, j1 + 1, k1)) {
                flag = true;
            }
            if (isSuitableBlock(world, i1, j1, k1 - 1)) {
                flag = true;
            }
            if (isSuitableBlock(world, i1, j1, k1 + 1)) {
                flag = true;
            }
            if (!flag) {
                continue;
            }
            setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("webUngoliant"), 0);
        }
        return true;
    }

    public boolean isSuitableBlock(WorldGenLevel world, int i, int j, int k) {
        return world.getBlockState(new BlockPos(i, j, k)).isSolidRender();
    }
}
