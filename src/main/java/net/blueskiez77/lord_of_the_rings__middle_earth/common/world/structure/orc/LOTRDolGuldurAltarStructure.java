package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRDolGuldurAltarStructure extends LOTRStructureBase2 {
    public LOTRDolGuldurAltarStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int k1;
        int k12;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 6);
        if (restrictions) {
            for (i1 = -5; i1 <= 5; ++i1) {
                for (k12 = -5; k12 <= 5; ++k12) {
                    j1 = getTopBlock(world, i1, k12);
                    BlockState block = getBlockState(world, i1, j1 - 1, k12);
                    if (LOTRLegacyBlocks.vanilla("stone").matches(block) || LOTRLegacyBlocks.vanilla("dirt").matches(block) || LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -5; i1 <= 5; ++i1) {
            for (k12 = -5; k12 <= 5; ++k12) {
                j1 = 0;
                while (!isOpaque(world, i1, j1, k12) && getY(j1) >= world.getMinY()) {
                    placeRandomBrick(world, random, i1, j1, k12);
                    setGrassToDirt(world, i1, j1 - 1, k12);
                    --j1;
                }
                for (j1 = 1; j1 <= 8; ++j1) {
                    setAir(world, i1, j1, k12);
                }
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k12);
                if (i2 <= 4 && k2 <= 4) {
                    placeRandomBrick(world, random, i1, 1, k12);
                }
                if (i2 > 3 || k2 > 3) {
                    continue;
                }
                if (random.nextInt(10) == 0) {
                    setBlockAndMetadata(world, i1, 2, k12, LOTRLegacyBlocks.mod("guldurilBrick"), 4);
                    continue;
                }
                placeRandomBrick(world, random, i1, 2, k12);
            }
        }
        for (i1 = -5; i1 <= 5; ++i1) {
            placeRandomStairs(world, random, i1, 1, -5, 2);
            placeRandomStairs(world, random, i1, 1, 5, 3);
        }
        for (k1 = -5; k1 <= 5; ++k1) {
            placeRandomStairs(world, random, -5, 1, k1, 1);
            placeRandomStairs(world, random, 5, 1, k1, 0);
        }
        for (i1 = -4; i1 <= 4; ++i1) {
            placeRandomStairs(world, random, i1, 2, -4, 2);
            placeRandomStairs(world, random, i1, 2, 4, 3);
        }
        for (k1 = -4; k1 <= 4; ++k1) {
            placeRandomStairs(world, random, -4, 2, k1, 1);
            placeRandomStairs(world, random, 4, 2, k1, 0);
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            placeRandomStairs(world, random, i1, 3, -1, 2);
            placeRandomStairs(world, random, i1, 3, 1, 3);
        }
        for (k1 = -1; k1 <= 1; ++k1) {
            placeRandomStairs(world, random, -1, 3, k1, 1);
            placeRandomStairs(world, random, 1, 3, k1, 0);
        }
        placeRandomBrick(world, random, 0, 3, 0);
        setBlockAndMetadata(world, 0, 4, 0, LOTRLegacyBlocks.mod("dolGuldurTable"), 0);
        for (int x = -4; x <= 3; x += 7) {
            for (int z = -4; z <= 3; z += 7) {
                for (int i12 = x; i12 <= x + 1; ++i12) {
                    for (int k13 = z; k13 <= z + 1; ++k13) {
                        for (int j12 = 2; j12 <= 5; ++j12) {
                            placeRandomBrick(world, random, i12, j12, k13);
                        }
                    }
                }
            }
        }
        setBlockAndMetadata(world, -4, 6, -4, LOTRLegacyBlocks.mod("wall2"), 8);
        setBlockAndMetadata(world, -4, 7, -4, LOTRLegacyBlocks.mod("morgulTorch"), 5);
        setBlockAndMetadata(world, 4, 6, -4, LOTRLegacyBlocks.mod("wall2"), 8);
        setBlockAndMetadata(world, 4, 7, -4, LOTRLegacyBlocks.mod("morgulTorch"), 5);
        setBlockAndMetadata(world, -4, 6, 4, LOTRLegacyBlocks.mod("wall2"), 8);
        setBlockAndMetadata(world, -4, 7, 4, LOTRLegacyBlocks.mod("morgulTorch"), 5);
        setBlockAndMetadata(world, 4, 6, 4, LOTRLegacyBlocks.mod("wall2"), 8);
        setBlockAndMetadata(world, 4, 7, 4, LOTRLegacyBlocks.mod("morgulTorch"), 5);
        for (i1 = -4; i1 <= 4; i1 += 8) {
            placeRandomStairs(world, random, i1, 6, -3, 2);
            placeRandomStairs(world, random, i1, 6, -2, 7);
            setBlockAndMetadata(world, i1, 7, -2, LOTRLegacyBlocks.mod("guldurilBrick"), 4);
            placeRandomStairs(world, random, i1, 8, -2, 2);
            placeRandomStairs(world, random, i1, 8, -1, 7);
            placeRandomStairs(world, random, i1, 6, 3, 3);
            placeRandomStairs(world, random, i1, 6, 2, 6);
            setBlockAndMetadata(world, i1, 7, 2, LOTRLegacyBlocks.mod("guldurilBrick"), 4);
            placeRandomStairs(world, random, i1, 8, 2, 3);
            placeRandomStairs(world, random, i1, 8, 1, 6);
        }
        for (k1 = -4; k1 <= 4; k1 += 8) {
            placeRandomStairs(world, random, -3, 6, k1, 1);
            placeRandomStairs(world, random, -2, 6, k1, 4);
            setBlockAndMetadata(world, -2, 7, k1, LOTRLegacyBlocks.mod("guldurilBrick"), 4);
            placeRandomStairs(world, random, -2, 8, k1, 1);
            placeRandomStairs(world, random, -1, 8, k1, 4);
            placeRandomStairs(world, random, 3, 6, k1, 0);
            placeRandomStairs(world, random, 2, 6, k1, 5);
            setBlockAndMetadata(world, 2, 7, k1, LOTRLegacyBlocks.mod("guldurilBrick"), 4);
            placeRandomStairs(world, random, 2, 8, k1, 0);
            placeRandomStairs(world, random, 1, 8, k1, 5);
        }
        return true;
    }

    public void placeRandomBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(4) == 0) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("brick2"), 9);
        } else {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("brick2"), 8);
        }
    }

    public void placeRandomStairs(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta) {
        if (random.nextInt(4) == 0) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("stairsDolGuldurBrickCracked"), meta);
        } else {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("stairsDolGuldurBrick"), meta);
        }
    }
}
