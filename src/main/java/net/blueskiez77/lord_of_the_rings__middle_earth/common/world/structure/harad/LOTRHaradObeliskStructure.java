package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRHaradObeliskStructure extends LOTRStructureBase {
    public LOTRHaradObeliskStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int i1;
        if (restrictions && !LOTRLegacyBlocks.vanilla("sand").matches(world.getBlockState(new BlockPos(i, j - 1, k))) && !LOTRLegacyBlocks.vanilla("dirt").matches(world.getBlockState(new BlockPos(i, j - 1, k))) && !LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i, j - 1, k)))) {
            return false;
        }
        --j;
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                k += 8;
                break;
            }
            case 1: {
                i -= 8;
                break;
            }
            case 2: {
                k -= 8;
                break;
            }
            case 3: {
                i += 8;
            }
        }
        if (restrictions) {
            for (i1 = i - 7; i1 <= i + 7; ++i1) {
                for (k1 = k - 7; k1 <= k + 7; ++k1) {
                    j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i1, k1);
                    BlockState block = world.getBlockState(new BlockPos(i1, j1 - 1, k1));
                    if (LOTRLegacyBlocks.vanilla("sand").matches(block) || LOTRLegacyBlocks.vanilla("dirt").matches(block) || LOTRLegacyBlocks.vanilla("stone").matches(block) || LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = i - 7; i1 <= i + 7; ++i1) {
            for (k1 = k - 7; k1 <= k + 7; ++k1) {
                for (j1 = j; (j1 == j || !isOpaqueAt(world, i1, j1, k1)) && j1 >= 0; --j1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("sandstone"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
            }
        }
        for (i1 = i - 7; i1 <= i + 7; ++i1) {
            for (k1 = k - 7; k1 <= k + 7; ++k1) {
                int i2 = Math.abs(i1 - i);
                int k2 = Math.abs(k1 - k);
                if (i2 == 7 || k2 == 7) {
                    setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.vanilla("sandstone"), 0);
                }
                if (i2 == 5 && k2 <= 5 || k2 == 5 && i2 <= 5) {
                    setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.vanilla("sandstone"), 0);
                    setBlockAndNotifyAdequately(world, i1, j + 2, k1, LOTRLegacyBlocks.vanilla("sandstone"), 2);
                }
                if (i2 == 3 && k2 <= 3 || k2 == 3 && i2 <= 3) {
                    setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.vanilla("sandstone"), 0);
                    setBlockAndNotifyAdequately(world, i1, j + 2, k1, LOTRLegacyBlocks.vanilla("sandstone"), 2);
                    placeHaradBrick(world, random, i1, j + 3, k1);
                }
                if (i2 <= 1 && k2 <= 1) {
                    setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.vanilla("sandstone"), 0);
                    setBlockAndNotifyAdequately(world, i1, j + 2, k1, LOTRLegacyBlocks.vanilla("sandstone"), 2);
                    placeHaradBrick(world, random, i1, j + 3, k1);
                    setBlockAndNotifyAdequately(world, i1, j + 4, k1, LOTRLegacyBlocks.vanilla("sandstone"), 2);
                    placeHaradBrick(world, random, i1, j + 5, k1);
                }
                for (int l = 0; l <= 2; ++l) {
                    int l1 = 8 - (l * 2 + 1);
                    if (i2 != l1 || k2 != l1) {
                        continue;
                    }
                    placeHaradBrick(world, random, i1, j + l + 2, k1);
                    placeHaradWall(world, random, i1, j + l + 3, k1);
                    placeHaradWall(world, random, i1, j + l + 4, k1);
                }
            }
        }
        placeHaradBrick(world, random, i - 1, j + 6, k);
        placeHaradBrick(world, random, i + 1, j + 6, k);
        placeHaradBrick(world, random, i, j + 6, k - 1);
        placeHaradBrick(world, random, i, j + 6, k + 1);
        for (int j12 = j + 6; j12 <= j + 9; ++j12) {
            setBlockAndNotifyAdequately(world, i, j12, k, LOTRLegacyBlocks.vanilla("sandstone"), 2);
        }
        setBlockAndNotifyAdequately(world, i - 1, j + 10, k, LOTRLegacyBlocks.vanilla("sandstone"), 2);
        setBlockAndNotifyAdequately(world, i + 1, j + 10, k, LOTRLegacyBlocks.vanilla("sandstone"), 2);
        setBlockAndNotifyAdequately(world, i, j + 10, k - 1, LOTRLegacyBlocks.vanilla("sandstone"), 2);
        setBlockAndNotifyAdequately(world, i, j + 10, k + 1, LOTRLegacyBlocks.vanilla("sandstone"), 2);
        setBlockAndNotifyAdequately(world, i, j + 10, k, LOTRLegacyBlocks.mod("hearth"), 0);
        setBlockAndNotifyAdequately(world, i, j + 11, k, LOTRLegacyBlocks.vanilla("fire"), 0);
        return true;
    }

    public void placeHaradBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(3) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick3"), 11);
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 15);
        }
    }

    public void placeHaradWall(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(3) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("wall3"), 3);
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("wall"), 15);
        }
    }
}
