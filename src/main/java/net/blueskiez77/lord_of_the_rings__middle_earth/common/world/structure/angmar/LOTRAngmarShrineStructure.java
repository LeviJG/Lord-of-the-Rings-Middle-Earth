package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRAngmarShrineStructure extends LOTRStructureBase {
    public LOTRAngmarShrineStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int i1;
        int k1;
        if (restrictions && !LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i, j - 1, k)))) {
            return false;
        }
        --j;
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                k += 4;
                break;
            }
            case 1: {
                i -= 4;
                break;
            }
            case 2: {
                k -= 4;
                break;
            }
            case 3: {
                i += 4;
            }
        }
        if (restrictions) {
            int minHeight = j;
            int maxHeight = j;
            for (i1 = i - 3; i1 <= i + 3; ++i1) {
                for (k1 = k - 3; k1 <= k + 3; ++k1) {
                    int j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i1, k1) - 1;
                    BlockState l = world.getBlockState(new BlockPos(i1, j1, k1));
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(l) && !LOTRLegacyBlocks.vanilla("dirt").matches(l) && !LOTRLegacyBlocks.vanilla("stone").matches(l)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 <= maxHeight) {
                        continue;
                    }
                    maxHeight = j1;
                }
            }
            if (maxHeight - minHeight > 3) {
                return false;
            }
        }
        for (int i12 = i - 3; i12 <= i + 3; ++i12) {
            for (int k12 = k - 3; k12 <= k + 3; ++k12) {
                for (int j1 = j; !isOpaqueAt(world, i12, j1, k12) && j1 >= world.getMinY(); --j1) {
                    placeRandomBrick(world, random, i12, j1, k12);
                    setGrassToDirt(world, i12, j1 - 1, k12);
                }
            }
        }
        for (int l = 0; l <= 2; ++l) {
            for (int i13 = i - 3 + l; i13 <= i + 3 - l; ++i13) {
                for (int k13 = k - 3 + l; k13 <= k + 3 - l; ++k13) {
                    placeRandomBrick(world, random, i13, j + 1 + l, k13);
                }
            }
            placeRandomStairs(world, random, i - 3 + l, j + 1 + l, k, 0);
            placeRandomStairs(world, random, i + 3 - l, j + 1 + l, k, 1);
            placeRandomStairs(world, random, i, j + 1 + l, k - 3 + l, 2);
            placeRandomStairs(world, random, i, j + 1 + l, k + 3 - l, 3);
        }
        setBlockAndNotifyAdequately(world, i, j + 4, k, LOTRLegacyBlocks.mod("angmarTable"), 0);
        setBlockAndNotifyAdequately(world, i - 2, j + 3, k - 2, LOTRLegacyBlocks.mod("morgulTorch"), 5);
        setBlockAndNotifyAdequately(world, i - 2, j + 3, k + 2, LOTRLegacyBlocks.mod("morgulTorch"), 5);
        setBlockAndNotifyAdequately(world, i + 2, j + 3, k - 2, LOTRLegacyBlocks.mod("morgulTorch"), 5);
        setBlockAndNotifyAdequately(world, i + 2, j + 3, k + 2, LOTRLegacyBlocks.mod("morgulTorch"), 5);
        int pillars = 4 + random.nextInt(5);
        for (int p = 0; p < pillars; ++p) {
            int j1;
            BlockState l;
            i1 = 4 + random.nextInt(4);
            k1 = 4 + random.nextInt(4);
            if (random.nextBoolean()) {
                i1 *= -1;
            }
            if (random.nextBoolean()) {
                k1 *= -1;
            }
            int height = 2 + random.nextInt(3);
            l = world.getBlockState(new BlockPos(i1 += i, j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i1, k1 += k) - 1, k1));
            if (!LOTRLegacyBlocks.vanilla("grass").matches(l) && !LOTRLegacyBlocks.vanilla("dirt").matches(l) && !LOTRLegacyBlocks.vanilla("stone").matches(l)) {
                continue;
            }
            setGrassToDirt(world, i1, j1, k1);
            for (int j2 = j1; j2 < j1 + height; ++j2) {
                placeRandomBrick(world, random, i1, j2, k1);
            }
            setBlockAndNotifyAdequately(world, i1, j1 + height, k1, LOTRLegacyBlocks.mod("guldurilBrick"), 2);
        }
        return true;
    }

    public void placeRandomBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(4) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick2"), 1);
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick2"), 0);
        }
    }

    public void placeRandomStairs(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta) {
        if (random.nextInt(4) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("stairsAngmarBrickCracked"), meta);
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("stairsAngmarBrick"), meta);
        }
    }
}
