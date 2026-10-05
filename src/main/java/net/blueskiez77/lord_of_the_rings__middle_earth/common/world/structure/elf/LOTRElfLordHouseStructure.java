package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRGaladhrimLordEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature.LOTRMallornExtremeStructure;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRElfLordHouseStructure extends LOTRStructureBase {
    public LOTRElfLordHouseStructure(boolean flag) {
        super(flag);
    }

    public void buildStaircase(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState block;
        int i1 = i - 3;
        int j1 = j - 1;
        int k1 = k - 2;
        int l = 0;
        while (j1 >= 0 && (!(block = world.getBlockState(new BlockPos(i1, j1, k1))).isSolidRender() || block.is(BlockTags.LOGS))) {
            int i2;
            int k2;
            int k22;
            int j2;
            int k23;
            int l1 = l % 24;
            if (l1 < 5) {
                for (i2 = i1; i2 >= i1 - 2; --i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        setBlockAndNotifyAdequately(world, i2, j2, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
                setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("stairsMallorn"), 3);
                setBlockAndNotifyAdequately(world, i1 - 1, j1, k1, LOTRLegacyBlocks.mod("stairsMallorn"), 3);
                setBlockAndNotifyAdequately(world, i1 - 2, j1, k1, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
                setBlockAndNotifyAdequately(world, i1 - 2, j1 + 1, k1, LOTRLegacyBlocks.mod("fence"), 1);
                if (l1 > 0) {
                    setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1, LOTRLegacyBlocks.mod("fence"), 1);
                }
                --j1;
                ++k1;
            } else if (l1 == 5) {
                for (i2 = i1; i2 >= i1 - 2; --i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        for (k22 = k1; k22 <= k1 + 2; ++k22) {
                            setBlockAndNotifyAdequately(world, i2, j2, k22, LOTRLegacyBlocks.vanilla("air"), 0);
                        }
                    }
                }
                for (i2 = i1; i2 >= i1 - 1; --i2) {
                    for (k2 = k1; k2 <= k1 + 1; ++k2) {
                        setBlockAndNotifyAdequately(world, i2, j1, k2, LOTRLegacyBlocks.mod("planks"), 1);
                    }
                }
                for (k23 = k1; k23 <= k1 + 2; ++k23) {
                    setBlockAndNotifyAdequately(world, i1 - 2, j1, k23, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
                    setBlockAndNotifyAdequately(world, i1 - 2, j1 + 1, k23, LOTRLegacyBlocks.mod("fence"), 1);
                }
                for (i2 = i1; i2 >= i1 - 1; --i2) {
                    setBlockAndNotifyAdequately(world, i2, j1, k1 + 2, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
                    setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 + 2, LOTRLegacyBlocks.mod("fence"), 1);
                }
                setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1, LOTRLegacyBlocks.mod("fence"), 1);
                setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1 + 2, LOTRElfHouseStructure.getRandomTorch(random), 5);
                ++i1;
            } else if (l1 < 11) {
                for (k23 = k1; k23 <= k1 + 2; ++k23) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        setBlockAndNotifyAdequately(world, i1, j2, k23, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
                setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("stairsMallorn"), 1);
                setBlockAndNotifyAdequately(world, i1, j1, k1 + 1, LOTRLegacyBlocks.mod("stairsMallorn"), 1);
                setBlockAndNotifyAdequately(world, i1, j1, k1 + 2, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
                setBlockAndNotifyAdequately(world, i1, j1 + 1, k1 + 2, LOTRLegacyBlocks.mod("fence"), 1);
                if (l1 > 6) {
                    setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 + 2, LOTRLegacyBlocks.mod("fence"), 1);
                }
                --j1;
                ++i1;
            } else if (l1 == 11) {
                for (i2 = i1; i2 <= i1 + 2; ++i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        for (k22 = k1; k22 <= k1 + 2; ++k22) {
                            setBlockAndNotifyAdequately(world, i2, j2, k22, LOTRLegacyBlocks.vanilla("air"), 0);
                        }
                    }
                }
                for (i2 = i1; i2 <= i1 + 1; ++i2) {
                    for (k2 = k1; k2 <= k1 + 1; ++k2) {
                        setBlockAndNotifyAdequately(world, i2, j1, k2, LOTRLegacyBlocks.mod("planks"), 1);
                    }
                }
                for (i2 = i1; i2 <= i1 + 2; ++i2) {
                    setBlockAndNotifyAdequately(world, i2, j1, k1 + 2, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
                    setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 + 2, LOTRLegacyBlocks.mod("fence"), 1);
                }
                for (k23 = k1; k23 <= k1 + 1; ++k23) {
                    setBlockAndNotifyAdequately(world, i1 + 2, j1, k23, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
                    setBlockAndNotifyAdequately(world, i1 + 2, j1 + 1, k23, LOTRLegacyBlocks.mod("fence"), 1);
                }
                setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 + 2, LOTRLegacyBlocks.mod("fence"), 1);
                setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1 + 2, LOTRElfHouseStructure.getRandomTorch(random), 5);
                --k1;
            } else if (l1 < 17) {
                for (i2 = i1; i2 <= i1 + 2; ++i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        setBlockAndNotifyAdequately(world, i2, j2, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
                setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("stairsMallorn"), 2);
                setBlockAndNotifyAdequately(world, i1 + 1, j1, k1, LOTRLegacyBlocks.mod("stairsMallorn"), 2);
                setBlockAndNotifyAdequately(world, i1 + 2, j1, k1, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
                setBlockAndNotifyAdequately(world, i1 + 2, j1 + 1, k1, LOTRLegacyBlocks.mod("fence"), 1);
                if (l1 > 12) {
                    setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1, LOTRLegacyBlocks.mod("fence"), 1);
                }
                --j1;
                --k1;
            } else if (l1 == 17) {
                for (i2 = i1; i2 <= i1 + 2; ++i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        for (k22 = k1; k22 >= k1 - 2; --k22) {
                            setBlockAndNotifyAdequately(world, i2, j2, k22, LOTRLegacyBlocks.vanilla("air"), 0);
                        }
                    }
                }
                for (i2 = i1; i2 <= i1 + 1; ++i2) {
                    for (k2 = k1; k2 >= k1 - 1; --k2) {
                        setBlockAndNotifyAdequately(world, i2, j1, k2, LOTRLegacyBlocks.mod("planks"), 1);
                    }
                }
                for (k23 = k1; k23 >= k1 - 2; --k23) {
                    setBlockAndNotifyAdequately(world, i1 + 2, j1, k23, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
                    setBlockAndNotifyAdequately(world, i1 + 2, j1 + 1, k23, LOTRLegacyBlocks.mod("fence"), 1);
                }
                for (i2 = i1; i2 <= i1 + 1; ++i2) {
                    setBlockAndNotifyAdequately(world, i2, j1, k1 - 2, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
                    setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 - 2, LOTRLegacyBlocks.mod("fence"), 1);
                }
                setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1, LOTRLegacyBlocks.mod("fence"), 1);
                setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1 - 2, LOTRElfHouseStructure.getRandomTorch(random), 5);
                --i1;
            } else if (l1 < 23) {
                for (k23 = k1; k23 >= k1 - 2; --k23) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        setBlockAndNotifyAdequately(world, i1, j2, k23, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
                setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("stairsMallorn"), 0);
                setBlockAndNotifyAdequately(world, i1, j1, k1 - 1, LOTRLegacyBlocks.mod("stairsMallorn"), 0);
                setBlockAndNotifyAdequately(world, i1, j1, k1 - 2, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
                setBlockAndNotifyAdequately(world, i1, j1 + 1, k1 - 2, LOTRLegacyBlocks.mod("fence"), 1);
                if (l1 > 18) {
                    setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 - 2, LOTRLegacyBlocks.mod("fence"), 1);
                }
                --j1;
                --i1;
            } else {
                for (i2 = i1; i2 >= i1 - 2; --i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        for (k22 = k1; k22 >= k1 - 2; --k22) {
                            setBlockAndNotifyAdequately(world, i2, j2, k22, LOTRLegacyBlocks.vanilla("air"), 0);
                        }
                    }
                }
                for (i2 = i1; i2 >= i1 - 1; --i2) {
                    for (k2 = k1; k2 >= k1 - 1; --k2) {
                        setBlockAndNotifyAdequately(world, i2, j1, k2, LOTRLegacyBlocks.mod("planks"), 1);
                    }
                }
                for (i2 = i1; i2 >= i1 - 2; --i2) {
                    setBlockAndNotifyAdequately(world, i2, j1, k1 - 2, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
                    setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 - 2, LOTRLegacyBlocks.mod("fence"), 1);
                }
                for (k23 = k1; k23 >= k1 - 1; --k23) {
                    setBlockAndNotifyAdequately(world, i1 - 2, j1, k23, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
                    setBlockAndNotifyAdequately(world, i1 - 2, j1 + 1, k23, LOTRLegacyBlocks.mod("fence"), 1);
                }
                setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 - 2, LOTRLegacyBlocks.mod("fence"), 1);
                setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1 - 2, LOTRElfHouseStructure.getRandomTorch(random), 5);
                ++k1;
            }
            ++l;
        }
    }

    public void buildStairCircle(WorldGenLevel world, int i, int j, int k, int range, boolean upsideDown, boolean insideOut) {
        for (int i1 = i - range; i1 <= i + range; ++i1) {
            for (int k1 = k - range; k1 <= k + range; ++k1) {
                if (!world.isEmptyBlock(new BlockPos(i1, j, k1))) {
                    continue;
                }
                int direction = -1;
                if (isMallornPlanks(world, i1 - 1, j, k1)) {
                    direction = 1;
                } else if (isMallornPlanks(world, i1 + 1, j, k1)) {
                    direction = 3;
                } else if (isMallornPlanks(world, i1, j, k1 - 1)) {
                    direction = 2;
                } else if (isMallornPlanks(world, i1, j, k1 + 1)) {
                    direction = 0;
                } else if (isMallornPlanks(world, i1 - 1, j, k1 - 1) || isMallornPlanks(world, i1 + 1, j, k1 - 1)) {
                    direction = 2;
                } else if (isMallornPlanks(world, i1 - 1, j, k1 + 1) || isMallornPlanks(world, i1 + 1, j, k1 + 1)) {
                    direction = 0;
                }
                if (direction == -1) {
                    continue;
                }
                if (insideOut) {
                    direction += 4;
                    direction &= 3;
                }
                int meta = 0;
                switch (direction) {
                    case 0: {
                        meta = 2;
                        break;
                    }
                    case 1: {
                        meta = 1;
                        break;
                    }
                    case 2: {
                        meta = 3;
                        break;
                    }
                    case 3: {
                        meta = 0;
                    }
                }
                if (upsideDown) {
                    meta |= 4;
                }
                setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.mod("stairsMallorn"), meta);
            }
        }
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int distSq;
        int k1;
        int k12;
        int i2;
        int k2;
        int j12;
        int i1;
        int k13;
        if (restrictions) {
            for (i1 = i - 14; i1 <= i + 14; ++i1) {
                for (j12 = j; j12 <= j + 7; ++j12) {
                    for (k1 = k - 14; k1 <= k + 14; ++k1) {
                        if (Math.abs(i1 - i) <= 2 && Math.abs(k1 - k) <= 2 || world.isEmptyBlock(new BlockPos(i1, j12, k1))) {
                            continue;
                        }
                        return false;
                    }
                }
            }
            int totalGrass = 0;
            int numGrass = 0;
            for (int i12 = i - 5; i12 <= i + 5; ++i12) {
                block4:
                for (int k14 = k - 5; k14 <= k + 5; ++k14) {
                    if (Math.abs(i12 - i) <= 2 && Math.abs(k14 - k) <= 2) {
                        continue;
                    }
                    for (int j13 = j; j13 >= 0; --j13) {
                        if (!LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i12, j13, k14)))) {
                            continue;
                        }
                        totalGrass += j13;
                        ++numGrass;
                        continue block4;
                    }
                }
            }
            int lowestGrass = totalGrass / numGrass;
            for (int i13 = i - 5; i13 <= i + 5; ++i13) {
                for (int k15 = k - 5; k15 <= k + 5; ++k15) {
                    if (Math.abs(i13 - i) <= 2 && Math.abs(k15 - k) <= 2) {
                        continue;
                    }
                    for (int j14 = j; j14 > lowestGrass; --j14) {
                        setBlockAndNotifyAdequately(world, i13, j14, k15, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                    setBlockAndNotifyAdequately(world, i13, lowestGrass, k15, LOTRLegacyBlocks.vanilla("grass"), 0);
                }
            }
        } else if (usingPlayer != null) {
            for (int i14 = i - 2; i14 <= i + 2; ++i14) {
                for (k13 = k - 2; k13 <= k + 2; ++k13) {
                    for (j1 = j; !isOpaqueAt(world, i14, j1, k13) && j1 >= 0; --j1) {
                        setBlockAndNotifyAdequately(world, i14, j1, k13, LOTRLegacyBlocks.mod("wood"), 1);
                    }
                }
            }
            LOTRMallornExtremeStructure treeGen = new LOTRMallornExtremeStructure(true);
            j--;
            j12 = treeGen.generateAndReturnHeight(world, random, i, j, k, true);
            j += Mth.floor(j12 * Mth.randomBetween(random, LOTRMallornExtremeStructure.HOUSE_HEIGHT_MIN, LOTRMallornExtremeStructure.HOUSE_HEIGHT_MAX));
        }
        buildStaircase(world, random, i, j, k);
        for (i1 = i - 14; i1 <= i + 14; ++i1) {
            for (j12 = j; j12 <= j + 6; ++j12) {
                for (k1 = k - 14; k1 <= k + 14; ++k1) {
                    setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                }
            }
        }
        for (i1 = i - 2; i1 <= i + 2; ++i1) {
            for (j12 = j; j12 <= j + 7; ++j12) {
                for (k1 = k - 2; k1 <= k + 2; ++k1) {
                    setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.mod("wood"), 1);
                }
            }
        }
        for (i1 = i - 12; i1 <= i + 12; ++i1) {
            for (k13 = k - 12; k13 <= k + 12; ++k13) {
                i2 = i1 - i;
                k2 = k13 - k;
                if (Math.abs(i2) <= 2 && Math.abs(k2) <= 2) {
                    continue;
                }
                distSq = i2 * i2 + k2 * k2;
                if (distSq < 100) {
                    setBlockAndNotifyAdequately(world, i1, j, k13, LOTRLegacyBlocks.mod("planks"), 1);
                    continue;
                }
                if (distSq >= 169) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j + 1, k13, LOTRLegacyBlocks.mod("planks"), 1);
                if (distSq <= 132) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j + 2, k13, LOTRLegacyBlocks.mod("fence"), 1);
            }
        }
        for (i1 = i - 12; i1 <= i + 12; ++i1) {
            for (k13 = k - 12; k13 <= k + 12; ++k13) {
                i2 = i1 - i;
                k2 = k13 - k;
                distSq = i2 * i2 + k2 * k2;
                if (Math.abs(i2) <= 2 && Math.abs(k2) <= 2 || distSq >= 169) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j + 6, k13, LOTRLegacyBlocks.mod("planks"), 1);
                int i3 = i1;
                int k3 = k13;
                if (i3 > i) {
                    --i3;
                }
                if (i3 < i) {
                    ++i3;
                }
                if (k3 > k) {
                    --k3;
                }
                if (k3 < k) {
                    ++k3;
                }
                setBlockAndNotifyAdequately(world, i3, j + 7, k3, LOTRLegacyBlocks.mod("planks"), 1);
            }
        }
        buildStairCircle(world, i, j, k, 10, true, false);
        buildStairCircle(world, i, j + 1, k, 9, false, true);
        buildStairCircle(world, i, j + 6, k, 13, false, false);
        buildStairCircle(world, i, j + 7, k, 12, false, false);
        setBlockAndNotifyAdequately(world, i + 3, j + 3, k, LOTRElfHouseStructure.getRandomTorch(random), 1);
        setBlockAndNotifyAdequately(world, i - 3, j + 3, k, LOTRElfHouseStructure.getRandomTorch(random), 2);
        setBlockAndNotifyAdequately(world, i, j + 3, k + 3, LOTRElfHouseStructure.getRandomTorch(random), 3);
        setBlockAndNotifyAdequately(world, i, j + 3, k - 3, LOTRElfHouseStructure.getRandomTorch(random), 4);
        for (i1 = i - 3; i1 <= i + 3; ++i1) {
            setBlockAndNotifyAdequately(world, i1, j + 5, k - 3, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
            setBlockAndNotifyAdequately(world, i1, j + 5, k + 3, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
        }
        for (k12 = k - 2; k12 <= k + 2; ++k12) {
            setBlockAndNotifyAdequately(world, i - 3, j + 5, k12, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
            setBlockAndNotifyAdequately(world, i + 3, j + 5, k12, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
        }
        for (i1 = i - 4; i1 <= i + 4; i1 += 8) {
            for (k13 = k - 4; k13 <= k + 4; k13 += 8) {
                for (j1 = j + 1; j1 <= j + 5; ++j1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k13, LOTRLegacyBlocks.mod("wood"), 1);
                }
                setBlockAndNotifyAdequately(world, i1 + 1, j + 3, k13, LOTRElfHouseStructure.getRandomTorch(random), 1);
                setBlockAndNotifyAdequately(world, i1 - 1, j + 3, k13, LOTRElfHouseStructure.getRandomTorch(random), 2);
                setBlockAndNotifyAdequately(world, i1, j + 3, k13 + 1, LOTRElfHouseStructure.getRandomTorch(random), 3);
                setBlockAndNotifyAdequately(world, i1, j + 3, k13 - 1, LOTRElfHouseStructure.getRandomTorch(random), 4);
            }
        }
        setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 5, LOTRLegacyBlocks.mod("elvenTable"), 0);
        setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 4, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
        placeFlowerPot(world, i - 5, j + 2, k - 4, LOTRElfHouseStructure.getRandomPlant(random));
        setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 5, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
        placeFlowerPot(world, i - 4, j + 2, k - 5, LOTRElfHouseStructure.getRandomPlant(random));
        setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 5, LOTRLegacyBlocks.mod("elvenTable"), 0);
        setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 4, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
        placeFlowerPot(world, i + 5, j + 2, k - 4, LOTRElfHouseStructure.getRandomPlant(random));
        setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 5, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
        placeFlowerPot(world, i + 4, j + 2, k - 5, LOTRElfHouseStructure.getRandomPlant(random));
        setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 5, LOTRLegacyBlocks.mod("elvenTable"), 0);
        setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 4, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
        placeFlowerPot(world, i - 5, j + 2, k + 4, LOTRElfHouseStructure.getRandomPlant(random));
        setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 5, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
        placeFlowerPot(world, i - 4, j + 2, k + 5, LOTRElfHouseStructure.getRandomPlant(random));
        setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 5, LOTRLegacyBlocks.mod("elvenTable"), 0);
        setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 4, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
        placeFlowerPot(world, i + 5, j + 2, k + 4, LOTRElfHouseStructure.getRandomPlant(random));
        setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 5, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
        placeFlowerPot(world, i + 4, j + 2, k + 5, LOTRElfHouseStructure.getRandomPlant(random));
        placeRandomChandelier(world, random, i - 8, j + 5, k);
        placeRandomChandelier(world, random, i + 8, j + 5, k);
        placeRandomChandelier(world, random, i, j + 5, k - 8);
        placeRandomChandelier(world, random, i, j + 5, k + 8);
        for (i1 = i - 8; i1 <= i + 8; i1 += 16) {
            for (k13 = k - 8; k13 <= k + 8; k13 += 16) {
                for (j1 = j + 2; j1 <= j + 5; ++j1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k13, LOTRLegacyBlocks.mod("planks"), 1);
                }
                for (i2 = i1 - 1; i2 <= i1 + 1; ++i2) {
                    setBlockAndNotifyAdequately(world, i2, j + 5, k13 - 1, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
                    setBlockAndNotifyAdequately(world, i2, j + 5, k13 + 1, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
                }
                setBlockAndNotifyAdequately(world, i1 - 1, j + 5, k13, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
                setBlockAndNotifyAdequately(world, i1 + 1, j + 5, k13, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
            }
        }
        for (int j15 = j + 2; j15 <= j + 5; ++j15) {
            setBlockAndNotifyAdequately(world, i - 12, j15, k - 4, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndNotifyAdequately(world, i - 12, j15, k + 4, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndNotifyAdequately(world, i + 12, j15, k - 4, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndNotifyAdequately(world, i + 12, j15, k + 4, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndNotifyAdequately(world, i - 4, j15, k - 12, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndNotifyAdequately(world, i + 4, j15, k - 12, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndNotifyAdequately(world, i - 4, j15, k + 12, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndNotifyAdequately(world, i + 4, j15, k + 12, LOTRLegacyBlocks.mod("wood"), 1);
        }
        for (k12 = k - 5; k12 <= k + 5; ++k12) {
            if (Math.abs(k12 - k) <= 2) {
                setBlockAndNotifyAdequately(world, i - 12, j + 5, k12, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
                setBlockAndNotifyAdequately(world, i + 12, j + 5, k12, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
            } else {
                setBlockAndNotifyAdequately(world, i - 11, j + 5, k12, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
                setBlockAndNotifyAdequately(world, i + 11, j + 5, k12, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
            }
            if (k12 - k == -5 || k12 - k == 3) {
                setBlockAndNotifyAdequately(world, i - 12, j + 5, k12, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
                setBlockAndNotifyAdequately(world, i + 12, j + 5, k12, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
                continue;
            }
            if (k12 - k != -3 && k12 - k != 5) {
                continue;
            }
            setBlockAndNotifyAdequately(world, i - 12, j + 5, k12, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
            setBlockAndNotifyAdequately(world, i + 12, j + 5, k12, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
        }
        for (i1 = i - 5; i1 <= i + 5; ++i1) {
            if (Math.abs(i1 - i) <= 2) {
                setBlockAndNotifyAdequately(world, i1, j + 5, k - 12, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
                setBlockAndNotifyAdequately(world, i1, j + 5, k + 12, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
            } else {
                setBlockAndNotifyAdequately(world, i1, j + 5, k - 11, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
                setBlockAndNotifyAdequately(world, i1, j + 5, k + 11, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
            }
            if (i1 - i == -5 || i1 - i == 3) {
                setBlockAndNotifyAdequately(world, i1, j + 5, k - 12, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
                setBlockAndNotifyAdequately(world, i1, j + 5, k + 12, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
                continue;
            }
            if (i1 - i != -3 && i1 - i != 5) {
                continue;
            }
            setBlockAndNotifyAdequately(world, i1, j + 5, k - 12, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
            setBlockAndNotifyAdequately(world, i1, j + 5, k + 12, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
        }
        setBlockAndNotifyAdequately(world, i + 6, j + 1, k, LOTRLegacyBlocks.mod("elvenBed"), 3);
        setBlockAndNotifyAdequately(world, i + 7, j + 1, k, LOTRLegacyBlocks.mod("elvenBed"), 11);
        setBlockAndNotifyAdequately(world, i, j + 1, k + 7, LOTRLegacyBlocks.mod("commandTable"), 0);
        placeBanner(world, i, j + 2, k - 11, 0, "GALADHRIM");
        placeBanner(world, i + 11, j + 2, k, 1, "GALADHRIM");
        placeBanner(world, i, j + 2, k + 11, 2, "GALADHRIM");
        placeBanner(world, i - 11, j + 2, k, 3, "GALADHRIM");
        tryPlaceLight(world, i - 12, j, k - 2, random);
        tryPlaceLight(world, i - 12, j, k + 2, random);
        tryPlaceLight(world, i - 9, j, k + 9, random);
        tryPlaceLight(world, i - 2, j, k + 12, random);
        tryPlaceLight(world, i + 2, j, k + 12, random);
        tryPlaceLight(world, i + 9, j, k + 9, random);
        tryPlaceLight(world, i + 12, j, k + 2, random);
        tryPlaceLight(world, i + 12, j, k - 2, random);
        tryPlaceLight(world, i + 9, j, k - 9, random);
        tryPlaceLight(world, i + 2, j, k - 12, random);
        tryPlaceLight(world, i - 2, j, k - 12, random);
        tryPlaceLight(world, i - 9, j, k - 9, random);
        for (i1 = i - 4; i1 <= i - 3; ++i1) {
            for (k13 = k - 3; k13 <= k; ++k13) {
                setBlockAndNotifyAdequately(world, i1, j, k13, LOTRLegacyBlocks.vanilla("air"), 0);
            }
            setBlockAndNotifyAdequately(world, i1, j, k - 3, LOTRLegacyBlocks.mod("stairsMallorn"), 3);
        }
        LOTRGaladhrimLordEntity elfLord = create(LOTREntities.GALADHRIM_LORD, world);
        elfLord.snapTo(i + 0.5, j + 1, k + 3.5, 0.0f, 0.0f);
        elfLord.spawnRidingHorse = false;
        elfLord.finalizeSpawn(world, world.getCurrentDifficultyAt(elfLord.blockPosition()), EntitySpawnReason.STRUCTURE, null);
        elfLord.setHomeTo(new BlockPos(i, j, k), 8);
        world.addFreshEntity(elfLord);
        return true;
    }

    public boolean isMallornPlanks(WorldGenLevel world, int i, int j, int k) {
        return world.getBlockState(new BlockPos(i, j, k)).is(LOTRLegacyBlocks.mod("planks").state(1).getBlock());
    }

    public void placeRandomChandelier(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        ItemStack itemstack = LOTRElfHouseStructure.getRandomChandelier(random);
        setBlockState(world, i, j, k, Block.byItem(itemstack.getItem()).defaultBlockState());
    }

    public void tryPlaceLight(WorldGenLevel world, int i, int j, int k, RandomSource random) {
        int j1;
        int height = 3 + random.nextInt(7);
        for (j1 = j; j1 >= j - height; --j1) {
            if (!restrictions) {
                continue;
            }
            if (!world.isEmptyBlock(new BlockPos(i, j1, k))) {
                return;
            }
            if (j1 != j - height || world.isEmptyBlock(new BlockPos(i, j1, k - 1)) && world.isEmptyBlock(new BlockPos(i, j1, k + 1)) && world.isEmptyBlock(new BlockPos(i - 1, j1, k)) && world.isEmptyBlock(new BlockPos(i + 1, j1, k))) {
                continue;
            }
            return;
        }
        for (j1 = j; j1 >= j - height; --j1) {
            if (j1 == j - height) {
                setBlockAndNotifyAdequately(world, i, j1, k, LOTRLegacyBlocks.mod("planks"), 1);
                setBlockAndNotifyAdequately(world, i, j1, k - 1, LOTRElfHouseStructure.getRandomTorch(random), 4);
                setBlockAndNotifyAdequately(world, i, j1, k + 1, LOTRElfHouseStructure.getRandomTorch(random), 3);
                setBlockAndNotifyAdequately(world, i - 1, j1, k, LOTRElfHouseStructure.getRandomTorch(random), 2);
                setBlockAndNotifyAdequately(world, i + 1, j1, k, LOTRElfHouseStructure.getRandomTorch(random), 1);
                continue;
            }
            setBlockAndNotifyAdequately(world, i, j1, k, LOTRLegacyBlocks.mod("fence"), 1);
        }
    }
}
