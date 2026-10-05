package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRRuinedGondorTowerStructure extends LOTRStructureBase {
    public LOTRRuinedGondorTowerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState id;
        int i1;
        int j1;
        int j12;
        int k1;
        int k12;
        int i12;
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
            for (i1 = i - 3; i1 <= i + 3; ++i1) {
                for (k12 = k + 3; k12 <= k + 3; ++k12) {
                    j12 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i1, k12);
                    BlockState block = world.getBlockState(new BlockPos(i1, j12 - 1, k12));
                    if (LOTRLegacyBlocks.vanilla("grass").matches(block) || block.is(BlockTags.LOGS) || block.is(BlockTags.LEAVES)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = i - 3; i1 <= i + 3; ++i1) {
            for (k12 = k - 3; k12 <= k + 3; ++k12) {
                if (Math.abs(i1 - i) == 3 || Math.abs(k12 - k) == 3) {
                    for (j12 = j + 8; (j12 >= j || !isOpaqueAt(world, i1, j12, k12)) && j12 >= 0; --j12) {
                        placeRandomBrick(world, random, i1, j12, k12);
                        setGrassToDirt(world, i1, j12 - 1, k12);
                    }
                } else {
                    for (j12 = j; !isOpaqueAt(world, i1, j12, k12) && j12 >= 0; --j12) {
                        placeRandomBrick(world, random, i1, j12, k12);
                        setGrassToDirt(world, i1, j12 - 1, k12);
                    }
                    for (j12 = j + 1; j12 <= j + 8; ++j12) {
                        setBlockAndNotifyAdequately(world, i1, j12, k12, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
                if (Math.abs(i1 - i) >= 3 || Math.abs(k12 - k) >= 3 || random.nextInt(20) == 0) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j + 5, k12, LOTRLegacyBlocks.vanilla("planks"), 0);
            }
        }
        setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 2, LOTRLegacyBlocks.mod("chestLebethron"), 0);
        if (random.nextInt(3) == 0) {
            LOTRChestContents.fillChest(world, random, new BlockPos(i - 2, j + 1, k - 2), LOTRChestContents.GONDOR_FORTRESS_SUPPLIES, -1);
        }
        setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 2, LOTRLegacyBlocks.mod("gondorianTable"), 0);
        if (random.nextInt(3) != 0) {
            setBlockAndNotifyAdequately(world, i + 2, j + 6, k - 2, LOTRLegacyBlocks.mod("strawBed"), 10);
            setBlockAndNotifyAdequately(world, i + 2, j + 6, k - 1, LOTRLegacyBlocks.mod("strawBed"), 2);
        }
        if (random.nextBoolean()) {
            setBlockAndNotifyAdequately(world, i + 2, j + 6, k + 2, LOTRLegacyBlocks.vanilla("anvil"), 8);
        }
        for (k1 = k; k1 <= k + 2; ++k1) {
            setBlockAndNotifyAdequately(world, i - 2, j + 6, k1, LOTRLegacyBlocks.mod("slabSingle"), 10);
        }
        if (random.nextBoolean()) {
            setBlockAndNotifyAdequately(world, i - 2, j + 7, k, LOTRLegacyBlocks.mod("mugBlock"), 1);
        }
        if (random.nextBoolean()) {
            setBlockAndNotifyAdequately(world, i - 2, j + 7, k + 1, LOTRLegacyBlocks.mod("plateBlock"), 0);
        }
        if (random.nextBoolean()) {
            setBlockAndNotifyAdequately(world, i - 2, j + 7, k + 2, LOTRLegacyBlocks.mod("barrel"), 5);
        }
        for (i1 = i - 4; i1 <= i + 4; ++i1) {
            for (k12 = k - 4; k12 <= k + 4; ++k12) {
                placeRandomBrick(world, random, i1, j + 9, k12);
                if ((Math.abs(i1 - i) != 4 || Math.abs(k12 - k) % 2 != 0) && (Math.abs(k12 - k) != 4 || Math.abs(i1 - i) % 2 != 0) || !isOpaqueAt(world, i1, j + 9, k12)) {
                    continue;
                }
                placeRandomBrick(world, random, i1, j + 10, k12);
            }
        }
        for (j1 = j + 1; j1 <= j + 9; ++j1) {
            if (rotation == 2) {
                if (!isOpaqueAt(world, i + 3, j1, k)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i + 2, j1, k, LOTRLegacyBlocks.vanilla("ladder"), 4);
                continue;
            }
            if (!isOpaqueAt(world, i, j1, k + 3)) {
                continue;
            }
            setBlockAndNotifyAdequately(world, i, j1, k + 2, LOTRLegacyBlocks.vanilla("ladder"), 2);
        }
        if (rotation == 0) {
            for (j1 = j + 1; j1 <= j + 2; ++j1) {
                setBlockAndNotifyAdequately(world, i - 1, j1, k - 3, LOTRLegacyBlocks.mod("brick"), 5);
                setBlockAndNotifyAdequately(world, i, j1, k - 3, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i + 1, j1, k - 3, LOTRLegacyBlocks.mod("brick"), 5);
            }
            for (k1 = k - 4; k1 >= k - 7; --k1) {
                for (i12 = i - 2; i12 <= i + 2; i12 += 4) {
                    j12 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i12, k1);
                    id = world.getBlockState(new BlockPos(i12, j12 - 1, k1));
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(id) || random.nextInt(4) == 0) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i12, j12, k1, LOTRLegacyBlocks.mod("wall"), 3 + random.nextInt(3));
                }
            }
        }
        if (rotation == 1) {
            for (j1 = j + 1; j1 <= j + 2; ++j1) {
                setBlockAndNotifyAdequately(world, i + 3, j1, k - 1, LOTRLegacyBlocks.mod("brick"), 5);
                setBlockAndNotifyAdequately(world, i + 3, j1, k, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i + 3, j1, k + 1, LOTRLegacyBlocks.mod("brick"), 5);
            }
            for (i1 = i + 4; i1 <= i + 7; ++i1) {
                for (k12 = k - 2; k12 <= k + 2; k12 += 4) {
                    j12 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i1, k12);
                    id = world.getBlockState(new BlockPos(i1, j12 - 1, k12));
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(id) || random.nextInt(4) == 0) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j12, k12, LOTRLegacyBlocks.mod("wall"), 3 + random.nextInt(3));
                }
            }
        }
        if (rotation == 2) {
            for (j1 = j + 1; j1 <= j + 2; ++j1) {
                setBlockAndNotifyAdequately(world, i - 1, j1, k + 3, LOTRLegacyBlocks.mod("brick"), 5);
                setBlockAndNotifyAdequately(world, i, j1, k + 3, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i + 1, j1, k + 3, LOTRLegacyBlocks.mod("brick"), 5);
            }
            for (k1 = k + 4; k1 <= k + 7; ++k1) {
                for (i12 = i - 2; i12 <= i + 2; i12 += 4) {
                    j12 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i12, k1);
                    id = world.getBlockState(new BlockPos(i12, j12 - 1, k1));
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(id) || random.nextInt(4) == 0) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i12, j12, k1, LOTRLegacyBlocks.mod("wall"), 3 + random.nextInt(3));
                }
            }
        }
        if (rotation == 3) {
            for (j1 = j + 1; j1 <= j + 2; ++j1) {
                setBlockAndNotifyAdequately(world, i - 3, j1, k - 1, LOTRLegacyBlocks.mod("brick"), 5);
                setBlockAndNotifyAdequately(world, i - 3, j1, k, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i - 3, j1, k + 1, LOTRLegacyBlocks.mod("brick"), 5);
            }
            for (i1 = i - 4; i1 >= i - 7; --i1) {
                for (k12 = k - 2; k12 <= k + 2; k12 += 4) {
                    j12 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i1, k12);
                    id = world.getBlockState(new BlockPos(i1, j12 - 1, k12));
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(id) || random.nextInt(4) == 0) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j12, k12, LOTRLegacyBlocks.mod("wall"), 3 + random.nextInt(3));
                }
            }
        }
        int radius = 8;
        int ruinParts = 2 + random.nextInt(6);
        for (int l = 0; l < ruinParts; ++l) {
            int j13;
            int k13;
            int j2;
            int i13 = i - random.nextInt(radius * 2) + random.nextInt(radius * 2);
            BlockState id2 = world.getBlockState(new BlockPos(i13, (j13 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i13, k13 = k - random.nextInt(radius * 2) + random.nextInt(radius * 2))) - 1, k13));
            if (!LOTRLegacyBlocks.vanilla("grass").matches(id2)) {
                continue;
            }
            int height = 1 + random.nextInt(3);
            boolean flag = true;
            for (j2 = j13; j2 < j13 + height && flag; ++j2) {
                flag = !isOpaqueAt(world, i13, j2, k13);
            }
            if (!flag) {
                continue;
            }
            for (j2 = j13; j2 < j13 + height; ++j2) {
                setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 2 + random.nextInt(2));
            }
            setGrassToDirt(world, i13, j13 - 1, k13);
        }
        return true;
    }

    public void placeRandomBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(16) == 0) {
            return;
        }
        if (random.nextInt(4) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 2 + random.nextInt(2));
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 1);
        }
    }
}
