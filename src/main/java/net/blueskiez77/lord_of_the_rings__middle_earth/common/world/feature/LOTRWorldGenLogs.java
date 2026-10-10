package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenLogs extends LOTRFeature {
    public LOTRWorldGenLogs() {
        super(false);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (!isSuitablePositionForLog(world, i, j, k)) {
            return false;
        }
        int logType = random.nextInt(5);
        if (logType == 0) {
            int length = 2 + random.nextInt(6);
            for (int i1 = i; i1 < i + length && isSuitablePositionForLog(world, i1, j, k); ++i1) {
                setBlockAndNotifyAdequately(world, i1, j, k, LOTRLegacyBlocks.mod("rottenLog"), 4);
                onPlantGrow(world, i1, j - 1, k);
            }
            return true;
        }
        if (logType == 1) {
            int length = 2 + random.nextInt(6);
            for (int k1 = k; k1 < k + length && isSuitablePositionForLog(world, i, j, k1); ++k1) {
                setBlockAndNotifyAdequately(world, i, j, k1, LOTRLegacyBlocks.mod("rottenLog"), 8);
                onPlantGrow(world, i, j - 1, k1);
            }
            return true;
        }
        setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("rottenLog"), 0);
        onPlantGrow(world, i, j - 1, k);
        return true;
    }

    public boolean isSuitablePositionForLog(WorldGenLevel world, int i, int j, int k) {
        if (!canSustainPlant(world, i, j - 1, k)) {
            return false;
        }
        return isBlockReplaceable(getBlock(world, i, j, k));
    }
}
