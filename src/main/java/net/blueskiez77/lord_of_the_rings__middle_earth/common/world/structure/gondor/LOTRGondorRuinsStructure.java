package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRWorldGenGondorRuins: tumbled Gondorian brick and slabs strewn over the grass about a hidden
 * shaft, which leads down a ladder to a crypt -- a chest of bones, and a chest of treasure guarded
 * by a ruins wraith.
 */
public class LOTRGondorRuinsStructure extends LOTRStructureBase {

    public LOTRGondorRuinsStructure() {
        super(false);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int k12;
        int i1;
        int k13;
        if (!LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i, j - 1, k)))) {
            return false;
        }
        int slabRuinParts = 3 + random.nextInt(4);
        for (int l = 0; l < slabRuinParts; ++l) {
            int i12 = i - 5 + random.nextInt(10);
            k12 = k - 5 + random.nextInt(10);
            if (i12 == i && k12 == k) {
                continue;
            }
            j1 = LOTRWorldGenUtil.getHeightValue(world, i12, k12);
            if (isOpaqueAt(world, i12, j1 - 1, k12)) {
                placeRandomSlab(world, random, i12, j1, k12);
            }
            setGrassToDirt(world, i12, j1 - 1, k12);
        }
        int smallRuinParts = 3 + random.nextInt(4);
        for (int l = 0; l < smallRuinParts; ++l) {
            i1 = i - 5 + random.nextInt(10);
            k13 = k - 5 + random.nextInt(10);
            if (i1 == i && k13 == k) {
                continue;
            }
            int j12 = LOTRWorldGenUtil.getHeightValue(world, i1, k13);
            if (isOpaqueAt(world, i1, j12 - 1, k13)) {
                int height = 1 + random.nextInt(3);
                for (int j2 = 0; j2 < height; ++j2) {
                    placeRandomBrick(world, random, i1, j12 + j2, k13);
                }
            }
            setGrassToDirt(world, i1, j12 - 1, k13);
        }
        int largeRuinParts = 3 + random.nextInt(5);
        for (int l = 0; l < largeRuinParts; ++l) {
            int i13 = i - 5 + random.nextInt(10);
            k1 = k - 5 + random.nextInt(10);
            if (i13 == i && k1 == k) {
                continue;
            }
            int j13 = LOTRWorldGenUtil.getHeightValue(world, i13, k1);
            if (isOpaqueAt(world, i13, j13 - 1, k1)) {
                int height = 4 + random.nextInt(7);
                for (int j2 = 0; j2 < height; ++j2) {
                    placeRandomBrick(world, random, i13, j13 + j2, k1);
                }
            }
            setGrassToDirt(world, i13, j13 - 1, k1);
        }
        for (i1 = i - 1; i1 <= i + 1; ++i1) {
            for (j1 = j - 2; j1 >= j - 5; --j1) {
                for (k1 = k - 1; k1 <= k + 1; ++k1) {
                    if (!isOpaqueAt(world, i1, j1, k1)) {
                        return true;
                    }
                }
            }
        }
        for (i1 = i - 1; i1 <= i + 8; ++i1) {
            for (j1 = j - 6; j1 >= j - 11; --j1) {
                for (k1 = k - 3; k1 <= k + 3; ++k1) {
                    if (!isOpaqueAt(world, i1, j1, k1)) {
                        return true;
                    }
                }
            }
        }
        LegacyBlock rock = LOTRLegacyBlocks.mod("rock");
        LegacyBlock brick = LOTRLegacyBlocks.mod("brick");
        LegacyBlock slabSingle = LOTRLegacyBlocks.mod("slabSingle");
        for (i1 = i - 1; i1 <= i + 8; ++i1) {
            for (j1 = j - 6; j1 >= j - 11; --j1) {
                for (k1 = k - 3; k1 <= k + 3; ++k1) {
                    if (j1 == j - 6 || j1 < j - 9) {
                        setBlockState(world, i1, j1, k1, rock.state(1));
                        continue;
                    }
                    setBlockState(world, i1, j1, k1, brick.state(1));
                }
            }
        }
        for (i1 = i; i1 <= i + 7; ++i1) {
            for (j1 = j - 7; j1 >= j - 9; --j1) {
                for (k1 = k - 2; k1 <= k + 2; ++k1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        for (k12 = k - 2; k12 <= k + 2; ++k12) {
            setBlockState(world, i + 7, j - 9, k12, brick.state(1));
            setBlockState(world, i + 7, j - 7, k12, slabSingle.state(11));
            setBlockState(world, i, j - 7, k12, slabSingle.state(11));
        }
        for (i1 = i + 1; i1 <= i + 5; ++i1) {
            for (k13 = k - 1; k13 <= k + 1; ++k13) {
                setBlockState(world, i1, j - 10, k13, LOTRLegacyBlocks.mod("slabDouble").state(2));
            }
        }
        setBlockState(world, i + 2, j - 9, k, slabSingle.state(2));
        setBlockState(world, i + 3, j - 9, k, slabSingle.state(2));
        setBlockState(world, i + 4, j - 9, k, slabSingle.state(2));
        placeSpawnerChest(world, i + 4, j - 10, k, LOTRLegacyBlocks.mod("spawnerChestStone"), 4, LOTREntities.GONDOR_RUINS_WRAITH);
        LOTRChestContents.fillChest(world, random, new BlockPos(i + 4, j - 10, k), LOTRChestContents.GONDOR_RUINS_TREASURE, -1);
        setBlockState(world, i + 2, j - 10, k, LOTRLegacyBlocks.mod("chestStone").state(4));
        LOTRChestContents.fillChest(world, random, new BlockPos(i + 2, j - 10, k), LOTRChestContents.GONDOR_RUINS_BONES, -1);
        for (int j14 = j - 2; j14 >= j - 9; --j14) {
            setBlockState(world, i, j14, k, LOTRLegacyBlocks.vanilla("ladder").state(5));
        }
        setBlockState(world, i, j - 1, k, brick.state(5));
        return true;
    }

    public void placeRandomBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(4) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 2 + random.nextInt(2));
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 1);
        }
    }

    public void placeRandomSlab(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(4) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("slabSingle"), 4 + random.nextInt(2));
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("slabSingle"), 3);
        }
    }
}
