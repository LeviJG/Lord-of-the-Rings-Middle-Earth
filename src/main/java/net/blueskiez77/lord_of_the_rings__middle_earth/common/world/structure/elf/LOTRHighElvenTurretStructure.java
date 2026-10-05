package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRHighElvenTurretStructure extends LOTRStructureBase {
    public LOTRHighElvenTurretStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int j12;
        int j13;
        int k1;
        BlockState block;
        int i1;
        int k12;
        if (restrictions && !LOTRLegacyBlocks.vanilla("grass").matches(block = world.getBlockState(new BlockPos(i, j - 1, k))) && !LOTRLegacyBlocks.vanilla("dirt").matches(block) && !LOTRLegacyBlocks.vanilla("stone").matches(block)) {
            return false;
        }
        --j;
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                k += 6;
                break;
            }
            case 1: {
                i -= 6;
                break;
            }
            case 2: {
                k -= 6;
                break;
            }
            case 3: {
                i += 6;
            }
        }
        for (i1 = i - 4; i1 <= i + 4; ++i1) {
            for (k1 = k - 4; k1 <= k + 4; ++k1) {
                for (j12 = j; (j12 == j || !isOpaqueAt(world, i1, j12, k1)) && j12 >= 0; --j12) {
                    setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.mod("brick3"), 2);
                    setGrassToDirt(world, i1, j12 - 1, k1);
                }
                for (j12 = j + 1; j12 <= j + 7; ++j12) {
                    if (Math.abs(i1 - i) == 4 || Math.abs(k1 - k) == 4) {
                        setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.mod("brick3"), 2);
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                }
            }
        }
        for (i1 = i - 3; i1 <= i + 3; ++i1) {
            for (k1 = k - 3; k1 <= k + 3; ++k1) {
                if (Math.abs(i1 - i) % 2 == Math.abs(k1 - k) % 2) {
                    setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.mod("pillar"), 10);
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.vanilla("double_stone_slab"), 0);
            }
        }
        for (j13 = j + 1; j13 <= j + 7; ++j13) {
            setBlockAndNotifyAdequately(world, i - 3, j13, k - 3, LOTRLegacyBlocks.mod("pillar"), 10);
            setBlockAndNotifyAdequately(world, i - 3, j13, k + 3, LOTRLegacyBlocks.mod("pillar"), 10);
            setBlockAndNotifyAdequately(world, i + 3, j13, k - 3, LOTRLegacyBlocks.mod("pillar"), 10);
            setBlockAndNotifyAdequately(world, i + 3, j13, k + 3, LOTRLegacyBlocks.mod("pillar"), 10);
        }
        for (i1 = i - 4; i1 <= i + 4; ++i1) {
            setBlockAndNotifyAdequately(world, i1, j + 7, k - 4, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 2);
            setBlockAndNotifyAdequately(world, i1, j + 7, k + 4, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 3);
        }
        for (k12 = k - 3; k12 <= k + 3; ++k12) {
            setBlockAndNotifyAdequately(world, i - 4, j + 7, k12, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 0);
            setBlockAndNotifyAdequately(world, i + 4, j + 7, k12, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 1);
        }
        for (i1 = i - 3; i1 <= i + 3; ++i1) {
            for (k1 = k - 3; k1 <= k + 3; ++k1) {
                for (j12 = j + 7; j12 <= j + 15; ++j12) {
                    if (Math.abs(i1 - i) == 3 || Math.abs(k1 - k) == 3) {
                        if (j12 - j >= 10 && j12 - j <= 14 && Math.abs(i1 - i) >= 3 && Math.abs(k1 - k) >= 3) {
                            continue;
                        }
                        setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.mod("brick3"), 2);
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                }
            }
        }
        for (i1 = i - 4; i1 <= i + 4; ++i1) {
            for (k1 = k - 4; k1 <= k + 4; ++k1) {
                for (j12 = j + 16; j12 <= j + 18; ++j12) {
                    if (j12 - j == 16 || Math.abs(i1 - i) == 4 || Math.abs(k1 - k) == 4) {
                        //noinspection BadOddness
                        if (j12 - j == 18 && (Math.abs(i1 - i) % 2 == 1 || Math.abs(k1 - k) % 2 == 1)) {
                            setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                            continue;
                        }
                        setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.mod("brick3"), 2);
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                }
            }
        }
        for (i1 = i - 4; i1 <= i + 4; ++i1) {
            setBlockAndNotifyAdequately(world, i1, j + 16, k - 4, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 6);
            setBlockAndNotifyAdequately(world, i1, j + 16, k + 4, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 7);
        }
        for (k12 = k - 3; k12 <= k + 3; ++k12) {
            setBlockAndNotifyAdequately(world, i - 4, j + 16, k12, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 4);
            setBlockAndNotifyAdequately(world, i + 4, j + 16, k12, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 5);
        }
        switch (rotation) {
            case 0:
                for (i1 = i - 1; i1 <= i + 1; ++i1) {
                    setBlockAndNotifyAdequately(world, i1, j, k - 5, LOTRLegacyBlocks.vanilla("double_stone_slab"), 0);
                    setBlockAndNotifyAdequately(world, i1, j, k - 4, LOTRLegacyBlocks.vanilla("double_stone_slab"), 0);
                }
                for (j13 = j + 1; j13 <= j + 2; ++j13) {
                    setBlockAndNotifyAdequately(world, i - 1, j13, k - 5, LOTRLegacyBlocks.mod("brick3"), 2);
                    setBlockAndNotifyAdequately(world, i, j13, k - 5, LOTRLegacyBlocks.vanilla("air"), 0);
                    setBlockAndNotifyAdequately(world, i, j13, k - 4, LOTRLegacyBlocks.vanilla("air"), 0);
                    setBlockAndNotifyAdequately(world, i + 1, j13, k - 5, LOTRLegacyBlocks.mod("brick3"), 2);
                }
                setBlockAndNotifyAdequately(world, i - 1, j + 3, k - 5, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 0);
                setBlockAndNotifyAdequately(world, i, j + 3, k - 5, LOTRLegacyBlocks.mod("brick3"), 2);
                setBlockAndNotifyAdequately(world, i + 1, j + 3, k - 5, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 1);
                for (i1 = i + 1; i1 <= i + 2; ++i1) {
                    for (j1 = j + 1; j1 <= j + 7; ++j1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k + 3, LOTRLegacyBlocks.mod("brick3"), 2);
                    }
                    for (j1 = j + 1; j1 <= j + 16; ++j1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k + 2, LOTRLegacyBlocks.vanilla("ladder"), 2);
                    }
                }
                break;
            case 1:
                for (k12 = k - 1; k12 <= k + 1; ++k12) {
                    setBlockAndNotifyAdequately(world, i + 5, j, k12, LOTRLegacyBlocks.vanilla("double_stone_slab"), 0);
                    setBlockAndNotifyAdequately(world, i + 4, j, k12, LOTRLegacyBlocks.vanilla("double_stone_slab"), 0);
                }
                for (j13 = j + 1; j13 <= j + 2; ++j13) {
                    setBlockAndNotifyAdequately(world, i + 5, j13, k - 1, LOTRLegacyBlocks.mod("brick3"), 2);
                    setBlockAndNotifyAdequately(world, i + 5, j13, k, LOTRLegacyBlocks.vanilla("air"), 0);
                    setBlockAndNotifyAdequately(world, i + 4, j13, k, LOTRLegacyBlocks.vanilla("air"), 0);
                    setBlockAndNotifyAdequately(world, i + 5, j13, k + 1, LOTRLegacyBlocks.mod("brick3"), 2);
                }
                setBlockAndNotifyAdequately(world, i + 5, j + 3, k - 1, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 2);
                setBlockAndNotifyAdequately(world, i + 5, j + 3, k, LOTRLegacyBlocks.mod("brick3"), 2);
                setBlockAndNotifyAdequately(world, i + 5, j + 3, k + 1, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 3);
                for (k12 = k - 1; k12 >= k - 2; --k12) {
                    for (j1 = j + 1; j1 <= j + 7; ++j1) {
                        setBlockAndNotifyAdequately(world, i - 3, j1, k12, LOTRLegacyBlocks.mod("brick3"), 2);
                    }
                    for (j1 = j + 1; j1 <= j + 16; ++j1) {
                        setBlockAndNotifyAdequately(world, i - 2, j1, k12, LOTRLegacyBlocks.vanilla("ladder"), 5);
                    }
                }
                break;
            case 2:
                for (i1 = i - 1; i1 <= i + 1; ++i1) {
                    setBlockAndNotifyAdequately(world, i1, j, k + 5, LOTRLegacyBlocks.vanilla("double_stone_slab"), 0);
                    setBlockAndNotifyAdequately(world, i1, j, k + 4, LOTRLegacyBlocks.vanilla("double_stone_slab"), 0);
                }
                for (j13 = j + 1; j13 <= j + 2; ++j13) {
                    setBlockAndNotifyAdequately(world, i - 1, j13, k + 5, LOTRLegacyBlocks.mod("brick3"), 2);
                    setBlockAndNotifyAdequately(world, i, j13, k + 5, LOTRLegacyBlocks.vanilla("air"), 0);
                    setBlockAndNotifyAdequately(world, i, j13, k + 4, LOTRLegacyBlocks.vanilla("air"), 0);
                    setBlockAndNotifyAdequately(world, i + 1, j13, k + 5, LOTRLegacyBlocks.mod("brick3"), 2);
                }
                setBlockAndNotifyAdequately(world, i - 1, j + 3, k + 5, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 0);
                setBlockAndNotifyAdequately(world, i, j + 3, k + 5, LOTRLegacyBlocks.mod("brick3"), 2);
                setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 5, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 1);
                for (i1 = i - 1; i1 >= i - 2; --i1) {
                    for (j1 = j + 1; j1 <= j + 7; ++j1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k - 3, LOTRLegacyBlocks.mod("brick3"), 2);
                    }
                    for (j1 = j + 1; j1 <= j + 16; ++j1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k - 2, LOTRLegacyBlocks.vanilla("ladder"), 3);
                    }
                }
                break;
            case 3:
                for (k12 = k - 1; k12 <= k + 1; ++k12) {
                    setBlockAndNotifyAdequately(world, i - 5, j, k12, LOTRLegacyBlocks.vanilla("double_stone_slab"), 0);
                    setBlockAndNotifyAdequately(world, i - 4, j, k12, LOTRLegacyBlocks.vanilla("double_stone_slab"), 0);
                }
                for (j13 = j + 1; j13 <= j + 2; ++j13) {
                    setBlockAndNotifyAdequately(world, i - 5, j13, k - 1, LOTRLegacyBlocks.mod("brick3"), 2);
                    setBlockAndNotifyAdequately(world, i - 5, j13, k, LOTRLegacyBlocks.vanilla("air"), 0);
                    setBlockAndNotifyAdequately(world, i - 4, j13, k, LOTRLegacyBlocks.vanilla("air"), 0);
                    setBlockAndNotifyAdequately(world, i - 5, j13, k + 1, LOTRLegacyBlocks.mod("brick3"), 2);
                }
                setBlockAndNotifyAdequately(world, i - 5, j + 3, k - 1, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 2);
                setBlockAndNotifyAdequately(world, i - 5, j + 3, k, LOTRLegacyBlocks.mod("brick3"), 2);
                setBlockAndNotifyAdequately(world, i - 5, j + 3, k + 1, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 3);
                for (k12 = k + 1; k12 <= k + 2; ++k12) {
                    for (j1 = j + 1; j1 <= j + 7; ++j1) {
                        setBlockAndNotifyAdequately(world, i + 3, j1, k12, LOTRLegacyBlocks.mod("brick3"), 2);
                    }
                    for (j1 = j + 1; j1 <= j + 16; ++j1) {
                        setBlockAndNotifyAdequately(world, i + 2, j1, k12, LOTRLegacyBlocks.vanilla("ladder"), 4);
                    }
                }
                break;
            default:
                break;
        }
        setBlockAndNotifyAdequately(world, i - 3, j + 3, k, LOTRLegacyBlocks.mod("highElvenTorch"), 1);
        setBlockAndNotifyAdequately(world, i + 3, j + 3, k, LOTRLegacyBlocks.mod("highElvenTorch"), 2);
        setBlockAndNotifyAdequately(world, i, j + 3, k - 3, LOTRLegacyBlocks.mod("highElvenTorch"), 3);
        setBlockAndNotifyAdequately(world, i, j + 3, k + 3, LOTRLegacyBlocks.mod("highElvenTorch"), 4);
        setBlockAndNotifyAdequately(world, i - 3, j + 18, k, LOTRLegacyBlocks.mod("highElvenTorch"), 1);
        setBlockAndNotifyAdequately(world, i + 3, j + 18, k, LOTRLegacyBlocks.mod("highElvenTorch"), 2);
        setBlockAndNotifyAdequately(world, i, j + 18, k - 3, LOTRLegacyBlocks.mod("highElvenTorch"), 3);
        setBlockAndNotifyAdequately(world, i, j + 18, k + 3, LOTRLegacyBlocks.mod("highElvenTorch"), 4);
        return true;
    }
}
