package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRRuinedRohanWatchtowerStructure extends LOTRStructureBase {
    public LegacyBlock plankBlock = LOTRLegacyBlocks.mod("planks");
    public int plankMeta = 3;
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood");
    public int woodMeta = 3;
    public LegacyBlock stairBlock = LOTRLegacyBlocks.mod("stairsCharred");

    public LOTRRuinedRohanWatchtowerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (restrictions && (!LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i, j - 1, k))) || !isBiome(world, i, k, "rohanUrukHighlands"))) {
            return false;
        }
        int height = 5 + random.nextInt(4);
        j += height;
        if (restrictions) {
            for (int i1 = i - 4; i1 <= i + 4; ++i1) {
                for (int j1 = j - 3; j1 <= j + 4; ++j1) {
                    for (int k1 = k - 4; k1 <= k + 4; ++k1) {
                        if (world.isEmptyBlock(new BlockPos(i1, j1, k1))) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        }
        generateBasicStructure(world, random, i, j, k);
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                return generateFacingSouth(world, random, i, j, k);
            }
            case 1: {
                return generateFacingWest(world, random, i, j, k);
            }
            case 2: {
                return generateFacingNorth(world, random, i, j, k);
            }
            case 3: {
                return generateFacingEast(world, random, i, j, k);
            }
        }
        return true;
    }

    public void generateBasicStructure(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int i1;
        for (j1 = j + 3 - random.nextInt(8); !isOpaqueAt(world, i - 3, j1, k - 3) && j1 >= world.getMinY(); --j1) {
            setBlockAndNotifyAdequately(world, i - 3, j1, k - 3, plankBlock, plankMeta);
        }
        for (j1 = j + 3 - random.nextInt(8); !isOpaqueAt(world, i - 3, j1, k + 3) && j1 >= world.getMinY(); --j1) {
            setBlockAndNotifyAdequately(world, i - 3, j1, k + 3, plankBlock, plankMeta);
        }
        for (j1 = j + 3 - random.nextInt(8); !isOpaqueAt(world, i + 3, j1, k - 3) && j1 >= world.getMinY(); --j1) {
            setBlockAndNotifyAdequately(world, i + 3, j1, k - 3, plankBlock, plankMeta);
        }
        for (j1 = j + 3 - random.nextInt(8); !isOpaqueAt(world, i + 3, j1, k + 3) && j1 >= world.getMinY(); --j1) {
            setBlockAndNotifyAdequately(world, i + 3, j1, k + 3, plankBlock, plankMeta);
        }
        for (i1 = i - 2; i1 <= i + 2; ++i1) {
            for (int k12 = k - 2; k12 <= k + 2; ++k12) {
                if (random.nextInt(4) == 0) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j, k12, plankBlock, plankMeta);
            }
        }
        for (i1 = i - 2 + random.nextInt(3); i1 <= i + 2 - random.nextInt(3); ++i1) {
            setBlockAndNotifyAdequately(world, i1, j, k - 3, woodBlock, woodMeta | 4);
        }
        for (i1 = i - 2 + random.nextInt(3); i1 <= i + 2 - random.nextInt(3); ++i1) {
            setBlockAndNotifyAdequately(world, i1, j, k + 3, woodBlock, woodMeta | 4);
        }
        for (i1 = i - 2 + random.nextInt(3); i1 <= i + 2 - random.nextInt(3); ++i1) {
            setBlockAndNotifyAdequately(world, i1, j, k - 4, stairBlock, 6);
        }
        for (i1 = i - 2 + random.nextInt(3); i1 <= i + 2 - random.nextInt(3); ++i1) {
            setBlockAndNotifyAdequately(world, i1, j, k + 4, stairBlock, 7);
        }
        for (k1 = k - 2 + random.nextInt(3); k1 <= k + 2 - random.nextInt(3); ++k1) {
            setBlockAndNotifyAdequately(world, i - 3, j, k1, woodBlock, woodMeta | 8);
        }
        for (k1 = k - 2 + random.nextInt(3); k1 <= k + 2 - random.nextInt(3); ++k1) {
            setBlockAndNotifyAdequately(world, i + 3, j, k1, woodBlock, woodMeta | 8);
        }
        for (k1 = k - 2 + random.nextInt(3); k1 <= k + 2 - random.nextInt(3); ++k1) {
            setBlockAndNotifyAdequately(world, i - 4, j, k1, stairBlock, 4);
        }
        for (k1 = k - 2 + random.nextInt(3); k1 <= k + 2 - random.nextInt(3); ++k1) {
            setBlockAndNotifyAdequately(world, i + 4, j, k1, stairBlock, 5);
        }
    }

    public boolean generateFacingEast(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int j1 = j - 1 - random.nextInt(4); !isOpaqueAt(world, i + 3, j1, k) && j1 >= world.getMinY(); --j1) {
            setBlockAndNotifyAdequately(world, i + 3, j1, k, plankBlock, plankMeta);
        }
        for (int i1 = i - 2; i1 <= i + 2; ++i1) {
            int j1;
            int j2;
            int i2 = Math.abs(i - i1);
            for (j1 = j - 1; !isOpaqueAt(world, i1, j1, k - 3) && j1 >= world.getMinY(); --j1) {
                j2 = j - j1;
                if ((i2 != 2 || j2 % 4 != 1) && (i2 != 1 || j2 % 2 != 0) && (i2 != 0 || j2 % 4 != 3 || random.nextInt(3) != 0)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j1, k - 3, woodBlock, woodMeta);
            }
            for (j1 = j - 1; !isOpaqueAt(world, i1, j1, k + 3) && j1 >= world.getMinY(); --j1) {
                j2 = j - j1;
                if ((i2 != 2 || j2 % 4 != 1) && (i2 != 1 || j2 % 2 != 0) && (i2 != 0 || j2 % 4 != 3 || random.nextInt(3) != 0)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j1, k + 3, woodBlock, woodMeta);
            }
        }
        return true;
    }

    public boolean generateFacingNorth(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int j1 = j - 1 - random.nextInt(4); !isOpaqueAt(world, i, j1, k - 3) && j1 >= world.getMinY(); --j1) {
            setBlockAndNotifyAdequately(world, i, j1, k - 3, plankBlock, plankMeta);
        }
        for (int k1 = k - 2; k1 <= k + 2; ++k1) {
            int j1;
            int j2;
            int k2 = Math.abs(k - k1);
            for (j1 = j - 1; !isOpaqueAt(world, i - 3, j1, k1) && j1 >= world.getMinY(); --j1) {
                j2 = j - j1;
                if ((k2 != 2 || j2 % 4 != 1) && (k2 != 1 || j2 % 2 != 0) && (k2 != 0 || j2 % 4 != 3 || random.nextInt(3) != 0)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i - 3, j1, k1, woodBlock, woodMeta);
            }
            for (j1 = j - 1; !isOpaqueAt(world, i + 3, j1, k1) && j1 >= world.getMinY(); --j1) {
                j2 = j - j1;
                if ((k2 != 2 || j2 % 4 != 1) && (k2 != 1 || j2 % 2 != 0) && (k2 != 0 || j2 % 4 != 3 || random.nextInt(3) != 0)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i + 3, j1, k1, woodBlock, woodMeta);
            }
        }
        return true;
    }

    public boolean generateFacingSouth(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int j1 = j - 1 - random.nextInt(4); !isOpaqueAt(world, i, j1, k + 3) && j1 >= world.getMinY(); --j1) {
            setBlockAndNotifyAdequately(world, i, j1, k + 3, plankBlock, plankMeta);
        }
        for (int k1 = k - 2; k1 <= k + 2; ++k1) {
            int j1;
            int j2;
            int k2 = Math.abs(k - k1);
            for (j1 = j - 1; !isOpaqueAt(world, i - 3, j1, k1) && j1 >= world.getMinY(); --j1) {
                j2 = j - j1;
                if ((k2 != 2 || j2 % 4 != 1) && (k2 != 1 || j2 % 2 != 0) && (k2 != 0 || j2 % 4 != 3 || random.nextInt(3) != 0)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i - 3, j1, k1, woodBlock, woodMeta);
            }
            for (j1 = j - 1; !isOpaqueAt(world, i + 3, j1, k1) && j1 >= world.getMinY(); --j1) {
                j2 = j - j1;
                if ((k2 != 2 || j2 % 4 != 1) && (k2 != 1 || j2 % 2 != 0) && (k2 != 0 || j2 % 4 != 3 || random.nextInt(3) != 0)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i + 3, j1, k1, woodBlock, woodMeta);
            }
        }
        return true;
    }

    public boolean generateFacingWest(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int j1 = j - 1 - random.nextInt(4); !isOpaqueAt(world, i - 3, j1, k) && j1 >= world.getMinY(); --j1) {
            setBlockAndNotifyAdequately(world, i - 3, j1, k, plankBlock, plankMeta);
        }
        for (int i1 = i - 2; i1 <= i + 2; ++i1) {
            int j1;
            int j2;
            int i2 = Math.abs(i - i1);
            for (j1 = j - 1; !isOpaqueAt(world, i1, j1, k - 3) && j1 >= world.getMinY(); --j1) {
                j2 = j - j1;
                if ((i2 != 2 || j2 % 4 != 1) && (i2 != 1 || j2 % 2 != 0) && (i2 != 0 || j2 % 4 != 3 || random.nextInt(3) != 0)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j1, k - 3, woodBlock, woodMeta);
            }
            for (j1 = j - 1; !isOpaqueAt(world, i1, j1, k + 3) && j1 >= world.getMinY(); --j1) {
                j2 = j - j1;
                if ((i2 != 2 || j2 % 4 != 1) && (i2 != 1 || j2 % 2 != 0) && (i2 != 0 || j2 % 4 != 3 || random.nextInt(3) != 0)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j1, k + 3, woodBlock, woodMeta);
            }
        }
        return true;
    }
}
