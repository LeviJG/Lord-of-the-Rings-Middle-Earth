package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRRohanBarrowStructure extends LOTRStructureBase {
    public LOTRRohanBarrowStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int i2;
        int k1;
        int i1;
        int k12;
        int i12;
        if (restrictions && (!LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i, j - 1, k))) || !(isBiome(world, i, k, "rohan")))) {
            return false;
        }
        --j;
        int radius = 7;
        int height = 4;
        if (!restrictions && usingPlayer != null) {
            int playerRotation = usingPlayerRotation();
            switch (playerRotation) {
                case 0: {
                    k += radius;
                    break;
                }
                case 1: {
                    i -= radius;
                    break;
                }
                case 2: {
                    k -= radius;
                    break;
                }
                case 3: {
                    i += radius;
                }
            }
        }
        if (restrictions) {
            int minHeight = j;
            int maxHeight = j;
            for (int i13 = i - radius; i13 <= i + radius; ++i13) {
                for (int k13 = k - radius; k13 <= k + radius; ++k13) {
                    int j12 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i13, k13) - 1;
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i13, j12, k13)))) {
                        return false;
                    }
                    if (j12 < minHeight) {
                        minHeight = j12;
                    }
                    if (j12 <= maxHeight) {
                        continue;
                    }
                    maxHeight = j12;
                }
            }
            if (maxHeight - minHeight > 3) {
                return false;
            }
        }
        for (i12 = i - radius; i12 <= i + radius; ++i12) {
            for (int j13 = j + height; j13 >= j; --j13) {
                for (k12 = k - radius; k12 <= k + radius; ++k12) {
                    i2 = i12 - i;
                    int j2 = j13 - j;
                    int k2 = k12 - k;
                    if (i2 * i2 + j2 * j2 + k2 * k2 > radius * radius) {
                        continue;
                    }
                    boolean grass = !isOpaqueAt(world, i12, j13 + 1, k12);
                    setBlockAndNotifyAdequately(world, i12, j13, k12, grass ? LOTRLegacyBlocks.vanilla("grass") : LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i12, j13 - 1, k12);
                }
            }
        }
        for (i12 = i - radius; i12 <= i + radius; ++i12) {
            for (k1 = k - radius; k1 <= k + radius; ++k1) {
                for (j1 = j - 1; !isOpaqueAt(world, i12, j1, k1) && j1 >= 0; --j1) {
                    i2 = i12 - i;
                    int k2 = k1 - k;
                    if (i2 * i2 + k2 * k2 > radius * radius) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i12, j1, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i12, j1 - 1, k1);
                }
            }
        }
        for (int l = 0; l < 12; ++l) {
            int j14;
            i1 = i - random.nextInt(radius) + random.nextInt(radius);
            if (!LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i1, (j14 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i1, k12 = k - random.nextInt(radius) + random.nextInt(radius))) - 1, k12)))) {
                continue;
            }
            setBlockAndNotifyAdequately(world, i1, j14, k12, LOTRLegacyBlocks.mod("simbelmyne"), 0);
        }
        j += height;
        for (i12 = i - 1; i12 < i + 1; ++i12) {
            for (k1 = k - 1; k1 <= k + 1; ++k1) {
                setBlockAndNotifyAdequately(world, i12, j - 1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
            }
        }
        for (i12 = i - 2; i12 <= i + 2; ++i12) {
            for (k1 = k - 2; k1 <= k + 2; ++k1) {
                for (j1 = j - 2; j1 >= j - 4; --j1) {
                    setBlockAndNotifyAdequately(world, i12, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                }
                setBlockAndNotifyAdequately(world, i12, j - 5, k1, LOTRLegacyBlocks.mod("slabDouble2"), 1);
            }
        }
        for (int j15 = j - 3; j15 >= j - 4; --j15) {
            for (i1 = i - 4; i1 <= i + 4; ++i1) {
                for (k12 = k - 1; k12 <= k + 1; ++k12) {
                    setBlockAndNotifyAdequately(world, i1, j15, k12, LOTRLegacyBlocks.vanilla("air"), 0);
                    if (Math.abs(i1 - i) <= 2) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j - 5, k12, LOTRLegacyBlocks.mod("brick"), 4);
                }
            }
            for (i1 = i - 1; i1 <= i + 1; ++i1) {
                for (k12 = k - 4; k12 <= k + 4; ++k12) {
                    setBlockAndNotifyAdequately(world, i1, j15, k12, LOTRLegacyBlocks.vanilla("air"), 0);
                    if (Math.abs(k12 - k) <= 2) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j - 5, k12, LOTRLegacyBlocks.mod("brick"), 4);
                }
            }
            setBlockAndNotifyAdequately(world, i - 4, j15, k - 1, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i - 5, j15, k, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i - 4, j15, k + 1, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i + 4, j15, k - 1, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i + 5, j15, k, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i + 4, j15, k + 1, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i - 1, j15, k - 4, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i, j15, k - 5, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i + 1, j15, k - 4, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i - 1, j15, k + 4, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i, j15, k + 5, LOTRLegacyBlocks.mod("rock"), 2);
            setBlockAndNotifyAdequately(world, i + 1, j15, k + 4, LOTRLegacyBlocks.mod("rock"), 2);
        }
        setBlockAndNotifyAdequately(world, i - 4, j - 3, k, LOTRLegacyBlocks.vanilla("torch"), 1);
        setBlockAndNotifyAdequately(world, i - 4, j - 4, k, LOTRLegacyBlocks.mod("slabSingle2"), 9);
        setBlockAndNotifyAdequately(world, i + 4, j - 3, k, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndNotifyAdequately(world, i + 4, j - 4, k, LOTRLegacyBlocks.mod("slabSingle2"), 9);
        setBlockAndNotifyAdequately(world, i, j - 3, k - 4, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndNotifyAdequately(world, i, j - 4, k - 4, LOTRLegacyBlocks.mod("slabSingle2"), 9);
        setBlockAndNotifyAdequately(world, i, j - 3, k + 4, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndNotifyAdequately(world, i, j - 4, k + 4, LOTRLegacyBlocks.mod("slabSingle2"), 9);
        for (i12 = i - 1; i12 <= i + 1; ++i12) {
            setBlockAndNotifyAdequately(world, i12, j - 4, k - 1, LOTRLegacyBlocks.mod("stairsRohanBrick"), 2);
            setBlockAndNotifyAdequately(world, i12, j - 4, k + 1, LOTRLegacyBlocks.mod("stairsRohanBrick"), 3);
        }
        setBlockAndNotifyAdequately(world, i - 1, j - 4, k, LOTRLegacyBlocks.mod("stairsRohanBrick"), 0);
        setBlockAndNotifyAdequately(world, i + 1, j - 4, k, LOTRLegacyBlocks.mod("stairsRohanBrick"), 1);
        placeSpawnerChest(world, i, j - 5, k, LOTRLegacyBlocks.mod("spawnerChestStone"), 4, LOTREntities.ROHAN_BARROW_WRAITH);
        LOTRChestContents.fillChest(world, random, new BlockPos(i, j - 5, k), LOTRChestContents.ROHAN_BARROWS, -1);
        setBlockAndNotifyAdequately(world, i, j - 3, k, LOTRLegacyBlocks.mod("slabSingle2"), 1);
        return true;
    }
}
