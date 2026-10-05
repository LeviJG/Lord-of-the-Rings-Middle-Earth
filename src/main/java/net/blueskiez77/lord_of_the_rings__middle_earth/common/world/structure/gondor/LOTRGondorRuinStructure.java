package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRGondorRuinStructure extends LOTRStructureBase {
    public LOTRGondorRuinStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int i1;
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
        j = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i, k);
        if (restrictions && !LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i, j - 1, k)))) {
            return false;
        }
        for (i1 = i - 7; i1 <= i + 7; ++i1) {
            for (k1 = k - 7; k1 <= k + 7; ++k1) {
                j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i1, k1);
                BlockState block = world.getBlockState(new BlockPos(i1, j1 - 1, k1));
                if (!block.isSolidRender()) {
                    continue;
                }
                if (random.nextInt(3) == 0) {
                    setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, LOTRLegacyBlocks.mod("rock"), 1);
                }
                if (random.nextInt(3) == 0) {
                    if (random.nextInt(3) == 0) {
                        placeRandomSlab(world, random, i1, j1, k1);
                    } else {
                        placeRandomBrick(world, random, i1, j1, k1);
                    }
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                if (!isOpaqueAt(world, i1, j1, k1) || random.nextInt(4) != 0) {
                    continue;
                }
                if (random.nextInt(5) == 0) {
                    setBlockAndNotifyAdequately(world, i1, j1 + 1, k1, LOTRLegacyBlocks.mod("wall"), 3);
                    placeSkull(world, random, i1, j1 + 2, k1);
                    continue;
                }
                if (random.nextInt(3) == 0) {
                    placeRandomSlab(world, random, i1, j1 + 1, k1);
                    continue;
                }
                placeRandomBrick(world, random, i1, j1 + 1, k1);
            }
        }
        for (i1 = i - 7; i1 <= i + 7; i1 += 7) {
            block9:
            for (k1 = k - 7; k1 <= k + 7; k1 += 7) {
                j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i1, k1);
                setGrassToDirt(world, i1, j1 - 1, k1);
                int j2 = j1;
                do {
                    placeRandomBrick(world, random, i1, j2, k1);
                    if (random.nextInt(4) == 0 || j2 > j1 + 4) {
                        if (i1 == i && k1 == k) {
                            setBlockAndNotifyAdequately(world, i1, j2 + 1, k1, LOTRLegacyBlocks.mod("beacon"), 0);
                            continue block9;
                        }
                        setBlockAndNotifyAdequately(world, i1, j2 + 1, k1, LOTRLegacyBlocks.mod("brick"), 5);
                        continue block9;
                    }
                    ++j2;
                } while (true);
            }
        }
        return true;
    }

    public void placeRandomBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(20) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 5);
        } else if (random.nextInt(4) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 2 + random.nextInt(2));
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 1);
        }
    }

    public void placeRandomSlab(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(5) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("slabSingle"), 2);
        } else if (random.nextInt(4) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("slabSingle"), 4 + random.nextInt(2));
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("slabSingle"), 3);
        }
    }
}
