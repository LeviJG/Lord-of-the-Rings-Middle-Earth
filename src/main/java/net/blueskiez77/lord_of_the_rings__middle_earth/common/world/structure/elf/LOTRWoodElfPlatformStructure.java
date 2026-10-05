package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRWoodElfPlatformStructure extends LOTRStructureBase {
    public LOTRWoodElfPlatformStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int rotation = -1;
        if (restrictions) {
            rotation = random.nextInt(4);
            switch (rotation) {
                case 0: {
                    k -= 3;
                    break;
                }
                case 1: {
                    i += 3;
                    break;
                }
                case 2: {
                    k += 3;
                    break;
                }
                case 3: {
                    i -= 3;
                }
            }
        } else if (usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        boolean flag = false;
        switch (rotation) {
            case 0: {
                flag = generateFacingSouth(world, random, i, j, k);
                break;
            }
            case 1: {
                flag = generateFacingWest(world, random, i, j, k);
                break;
            }
            case 2: {
                flag = generateFacingNorth(world, random, i, j, k);
                break;
            }
            case 3: {
                flag = generateFacingEast(world, random, i, j, k);
            }
        }
        if (flag) {
            LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
            respawner.setSpawnClass(LOTREntities.WOOD_ELF);
            respawner.setCheckRanges(8, -8, 8, 2);
            respawner.setSpawnRanges(3, -2, 2, 8);
            placeNPCRespawner(respawner, world, i, j + 1, k);
        }
        return false;
    }

    public boolean generateFacingEast(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int i1;
        if (restrictions) {
            for (k1 = k - 2; k1 <= k + 2; ++k1) {
                for (j1 = j; j1 <= j + 4; ++j1) {
                    if (!world.getBlockState(new BlockPos(i + 1, j1, k1)).is(BlockTags.LOGS)) {
                        return false;
                    }
                    for (i1 = i; i1 >= i - 3; --i1) {
                        if (world.isEmptyBlock(new BlockPos(i1, j1, k1))) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        } else {
            for (k1 = k - 2; k1 <= k + 2; ++k1) {
                for (j1 = j; j1 <= j + 4; ++j1) {
                    for (i1 = i; i1 >= i - 3; --i1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
            }
        }
        for (k1 = k - 1; k1 <= k + 1; ++k1) {
            for (int i12 = i; i12 >= i - 2; --i12) {
                setBlockAndNotifyAdequately(world, i12, j, k1, LOTRLegacyBlocks.mod("planks2"), 13);
            }
        }
        for (k1 = k - 2; k1 <= k + 2; ++k1) {
            setBlockAndNotifyAdequately(world, i - 3, j, k1, LOTRLegacyBlocks.mod("stairsGreenOak"), 4);
            setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, LOTRLegacyBlocks.mod("fence2"), 13);
        }
        for (int i13 = i; i13 >= i - 2; --i13) {
            setBlockAndNotifyAdequately(world, i13, j, k - 2, LOTRLegacyBlocks.mod("stairsGreenOak"), 6);
            setBlockAndNotifyAdequately(world, i13, j, k + 2, LOTRLegacyBlocks.mod("stairsGreenOak"), 7);
            setBlockAndNotifyAdequately(world, i13, j + 1, k - 2, LOTRLegacyBlocks.mod("fence2"), 13);
            setBlockAndNotifyAdequately(world, i13, j + 1, k + 2, LOTRLegacyBlocks.mod("fence2"), 13);
        }
        setBlockAndNotifyAdequately(world, i, j + 2, k - 2, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i, j + 3, k - 2, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i, j + 2, k + 2, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i, j + 3, k + 2, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 2, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i - 3, j + 3, k - 2, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 2, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i - 3, j + 3, k + 2, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        for (int j12 = j; j12 >= world.getMinY() && isOpaqueAt(world, i + 1, j12, k) && (j12 >= j || !isOpaqueAt(world, i, j12, k)); --j12) {
            setBlockAndNotifyAdequately(world, i, j12, k, LOTRLegacyBlocks.vanilla("ladder"), 4);
        }
        return true;
    }

    public boolean generateFacingNorth(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int i1;
        if (restrictions) {
            for (i1 = i - 2; i1 <= i + 2; ++i1) {
                for (j1 = j; j1 <= j + 4; ++j1) {
                    if (!world.getBlockState(new BlockPos(i1, j1, k - 1)).is(BlockTags.LOGS)) {
                        return false;
                    }
                    for (k1 = k; k1 <= k + 3; ++k1) {
                        if (world.isEmptyBlock(new BlockPos(i1, j1, k1))) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        } else {
            for (i1 = i - 2; i1 <= i + 2; ++i1) {
                for (j1 = j; j1 <= j + 4; ++j1) {
                    for (k1 = k; k1 <= k + 3; ++k1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
            }
        }
        for (i1 = i - 1; i1 <= i + 1; ++i1) {
            for (int k12 = k; k12 <= k + 2; ++k12) {
                setBlockAndNotifyAdequately(world, i1, j, k12, LOTRLegacyBlocks.mod("planks2"), 13);
            }
        }
        for (i1 = i - 2; i1 <= i + 2; ++i1) {
            setBlockAndNotifyAdequately(world, i1, j, k + 3, LOTRLegacyBlocks.mod("stairsGreenOak"), 7);
            setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, LOTRLegacyBlocks.mod("fence2"), 13);
        }
        for (int k13 = k; k13 <= k + 2; ++k13) {
            setBlockAndNotifyAdequately(world, i - 2, j, k13, LOTRLegacyBlocks.mod("stairsGreenOak"), 4);
            setBlockAndNotifyAdequately(world, i + 2, j, k13, LOTRLegacyBlocks.mod("stairsGreenOak"), 5);
            setBlockAndNotifyAdequately(world, i - 2, j + 1, k13, LOTRLegacyBlocks.mod("fence2"), 13);
            setBlockAndNotifyAdequately(world, i + 2, j + 1, k13, LOTRLegacyBlocks.mod("fence2"), 13);
        }
        setBlockAndNotifyAdequately(world, i - 2, j + 2, k, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i - 2, j + 3, k, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i + 2, j + 2, k, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i + 2, j + 3, k, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 3, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i - 2, j + 3, k + 3, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 3, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i + 2, j + 3, k + 3, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        for (int j12 = j; j12 >= world.getMinY() && isOpaqueAt(world, i, j12, k - 1) && (j12 >= j || !isOpaqueAt(world, i, j12, k)); --j12) {
            setBlockAndNotifyAdequately(world, i, j12, k, LOTRLegacyBlocks.vanilla("ladder"), 3);
        }
        return true;
    }

    public boolean generateFacingSouth(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int i1;
        if (restrictions) {
            for (i1 = i - 2; i1 <= i + 2; ++i1) {
                for (j1 = j; j1 <= j + 4; ++j1) {
                    if (!world.getBlockState(new BlockPos(i1, j1, k + 1)).is(BlockTags.LOGS)) {
                        return false;
                    }
                    for (k1 = k; k1 >= k - 3; --k1) {
                        if (world.isEmptyBlock(new BlockPos(i1, j1, k1))) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        } else {
            for (i1 = i - 2; i1 <= i + 2; ++i1) {
                for (j1 = j; j1 <= j + 4; ++j1) {
                    for (k1 = k; k1 >= k - 3; --k1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
            }
        }
        for (i1 = i - 1; i1 <= i + 1; ++i1) {
            for (int k12 = k; k12 >= k - 2; --k12) {
                setBlockAndNotifyAdequately(world, i1, j, k12, LOTRLegacyBlocks.mod("planks2"), 13);
            }
        }
        for (i1 = i - 2; i1 <= i + 2; ++i1) {
            setBlockAndNotifyAdequately(world, i1, j, k - 3, LOTRLegacyBlocks.mod("stairsGreenOak"), 6);
            setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, LOTRLegacyBlocks.mod("fence2"), 13);
        }
        for (int k13 = k; k13 >= k - 2; --k13) {
            setBlockAndNotifyAdequately(world, i - 2, j, k13, LOTRLegacyBlocks.mod("stairsGreenOak"), 4);
            setBlockAndNotifyAdequately(world, i + 2, j, k13, LOTRLegacyBlocks.mod("stairsGreenOak"), 5);
            setBlockAndNotifyAdequately(world, i - 2, j + 1, k13, LOTRLegacyBlocks.mod("fence2"), 13);
            setBlockAndNotifyAdequately(world, i + 2, j + 1, k13, LOTRLegacyBlocks.mod("fence2"), 13);
        }
        setBlockAndNotifyAdequately(world, i - 2, j + 2, k, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i - 2, j + 3, k, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i + 2, j + 2, k, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i + 2, j + 3, k, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 3, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i - 2, j + 3, k - 3, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 3, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i + 2, j + 3, k - 3, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        for (int j12 = j; j12 >= world.getMinY() && isOpaqueAt(world, i, j12, k + 1) && (j12 >= j || !isOpaqueAt(world, i, j12, k)); --j12) {
            setBlockAndNotifyAdequately(world, i, j12, k, LOTRLegacyBlocks.vanilla("ladder"), 2);
        }
        return true;
    }

    public boolean generateFacingWest(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int i1;
        if (restrictions) {
            for (k1 = k - 2; k1 <= k + 2; ++k1) {
                for (j1 = j; j1 <= j + 4; ++j1) {
                    if (!world.getBlockState(new BlockPos(i - 1, j1, k1)).is(BlockTags.LOGS)) {
                        return false;
                    }
                    for (i1 = i; i1 <= i + 3; ++i1) {
                        if (world.isEmptyBlock(new BlockPos(i1, j1, k1))) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        } else {
            for (k1 = k - 2; k1 <= k + 2; ++k1) {
                for (j1 = j; j1 <= j + 4; ++j1) {
                    for (i1 = i; i1 <= i + 3; ++i1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
            }
        }
        for (k1 = k - 1; k1 <= k + 1; ++k1) {
            for (int i12 = i; i12 <= i + 2; ++i12) {
                setBlockAndNotifyAdequately(world, i12, j, k1, LOTRLegacyBlocks.mod("planks2"), 13);
            }
        }
        for (k1 = k - 2; k1 <= k + 2; ++k1) {
            setBlockAndNotifyAdequately(world, i + 3, j, k1, LOTRLegacyBlocks.mod("stairsGreenOak"), 5);
            setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, LOTRLegacyBlocks.mod("fence2"), 13);
        }
        for (int i13 = i; i13 <= i + 2; ++i13) {
            setBlockAndNotifyAdequately(world, i13, j, k - 2, LOTRLegacyBlocks.mod("stairsGreenOak"), 6);
            setBlockAndNotifyAdequately(world, i13, j, k + 2, LOTRLegacyBlocks.mod("stairsGreenOak"), 7);
            setBlockAndNotifyAdequately(world, i13, j + 1, k - 2, LOTRLegacyBlocks.mod("fence2"), 13);
            setBlockAndNotifyAdequately(world, i13, j + 1, k + 2, LOTRLegacyBlocks.mod("fence2"), 13);
        }
        setBlockAndNotifyAdequately(world, i, j + 2, k - 2, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i, j + 3, k - 2, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i, j + 2, k + 2, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i, j + 3, k + 2, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 2, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i + 3, j + 3, k - 2, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 2, LOTRLegacyBlocks.mod("fence2"), 13);
        setBlockAndNotifyAdequately(world, i + 3, j + 3, k + 2, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
        for (int j12 = j; j12 >= world.getMinY() && isOpaqueAt(world, i - 1, j12, k) && (j12 >= j || !isOpaqueAt(world, i, j12, k)); --j12) {
            setBlockAndNotifyAdequately(world, i, j12, k, LOTRLegacyBlocks.vanilla("ladder"), 5);
        }
        return true;
    }
}
