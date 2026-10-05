package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRUnderwaterElvenRuinStructure extends LOTRStructureBase {
    public LOTRUnderwaterElvenRuinStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        if (restrictions && world.getBlockState(new BlockPos(i, j, k)).getFluidState().is(FluidTags.WATER) == false) {
            return false;
        }
        --j;
        int width = 3 + random.nextInt(3);
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                k += width + 1;
                break;
            }
            case 1: {
                i -= width + 1;
                break;
            }
            case 2: {
                k -= width + 1;
                break;
            }
            case 3: {
                i += width + 1;
            }
        }
        if (restrictions) {
            int minHeight = j + 1;
            int maxHeight = j + 1;
            for (int i1 = i - width; i1 <= i + width; ++i1) {
                for (int k1 = k - width; k1 <= k + width; ++k1) {
                    j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i1, k1);
                    if (world.getBlockState(new BlockPos(i1, j1, k1)).getFluidState().is(FluidTags.WATER) == false) {
                        return false;
                    }
                    BlockState block = world.getBlockState(new BlockPos(i1, j1 - 1, k1));
                    if (!LOTRLegacyBlocks.vanilla("dirt").matches(block) && !LOTRLegacyBlocks.vanilla("sand").matches(block) && !LOTRLegacyBlocks.vanilla("clay").matches(block)) {
                        return false;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (j1 >= minHeight) {
                        continue;
                    }
                    minHeight = j1;
                }
            }
            if (Math.abs(maxHeight - minHeight) > 5) {
                return false;
            }
        }
        for (int i1 = i - width - 3; i1 <= i + width + 3; ++i1) {
            for (int k1 = k - width - 3; k1 <= k + width + 3; ++k1) {
                int i2 = Math.abs(i1 - i);
                int k2 = Math.abs(k1 - k);
                if ((i2 > width || k2 > width) && random.nextInt(Math.max(1, i2 + k2)) != 0) {
                    continue;
                }
                j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i1, k1);
                placeRandomBrick(world, random, i1, j1, k1);
                setGrassToDirt(world, i1, j1 - 1, k1);
                if (random.nextInt(5) == 0) {
                    placeRandomBrick(world, random, i1, j1 + 1, k1);
                    if (random.nextInt(4) == 0) {
                        placeRandomBrick(world, random, i1, j1 + 2, k1);
                        if (random.nextInt(3) == 0) {
                            placeRandomBrick(world, random, i1, j1 + 3, k1);
                        }
                    }
                }
                if ((i2 != width || k2 != width) && random.nextInt(20) != 0) {
                    continue;
                }
                int height = 2 + random.nextInt(4);
                for (int j2 = j1; j2 < j1 + height; ++j2) {
                    placeRandomPillar(world, random, i1, j2, k1);
                }
            }
        }
        int j12 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i, k);
        setBlockAndNotifyAdequately(world, i, j12, k, LOTRLegacyBlocks.vanilla("glowstone"), 0);
        setBlockAndNotifyAdequately(world, i, j12 + 1, k, LOTRLegacyBlocks.mod("chestStone"), 0);
        LOTRChestContents.fillChest(world, random, new BlockPos(i, j12 + 1, k), LOTRChestContents.UNDERWATER_ELVEN_RUIN, -1);
        return true;
    }

    public void placeRandomBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int l = random.nextInt(3);
        switch (l) {
            case 0: {
                setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick3"), 2);
                break;
            }
            case 1: {
                setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick3"), 3);
                break;
            }
            case 2: {
                setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick3"), 4);
            }
        }
    }

    public void placeRandomPillar(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(3) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("pillar"), 11);
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("pillar"), 10);
        }
    }
}
