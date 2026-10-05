package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRBDBarrowStructure extends LOTRStructureBase2 {
    public LOTRStructureBase2 ruinsGen = new LOTRStoneRuinStructure.STONE(3, 3);

    public LOTRBDBarrowStructure(boolean flag) {
        super(flag);
        ruinsGen.restrictions = false;
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int i1;
        int i12;
        int j12;
        int k1;
        int radius = 12;
        int height = 7;
        int base = -4;
        setOriginAndRotation(world, i, j, k, rotation, usingPlayer != null ? radius : 0);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i13 = -radius; i13 <= radius; ++i13) {
                for (k1 = -radius; k1 <= radius; ++k1) {
                    j1 = getTopBlock(world, i13, k1) - 1;
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(getBlockState(world, i13, j1, k1))) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 <= maxHeight) {
                        continue;
                    }
                    maxHeight = j1;
                }
            }
            if (maxHeight - minHeight > 5) {
                return false;
            }
        }
        for (i1 = -radius; i1 <= radius; ++i1) {
            for (int j13 = height; j13 >= base; --j13) {
                for (int k12 = -radius; k12 <= radius; ++k12) {
                    if (i1 * i1 + (j13 - base) * (j13 - base) + k12 * k12 > radius * radius) {
                        continue;
                    }
                    boolean grass = !isOpaque(world, i1, j13 + 1, k12);
                    setBlockAndMetadata(world, i1, j13, k12, grass ? LOTRLegacyBlocks.vanilla("grass") : LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i1, j13 - 1, k12);
                }
            }
        }
        for (i1 = -radius; i1 <= radius; ++i1) {
            for (int k13 = -radius; k13 <= radius; ++k13) {
                int j14 = base - 1;
                while (!isOpaque(world, i1, j14, k13) && getY(j14) >= world.getMinY()) {
                    if (i1 * i1 + k13 * k13 <= radius * radius) {
                        setBlockAndMetadata(world, i1, j14, k13, LOTRLegacyBlocks.vanilla("dirt"), 0);
                        setGrassToDirt(world, i1, j14 - 1, k13);
                    }
                    --j14;
                }
            }
        }
        int innerR = 5;
        int innerH = 5;
        int innerB = -2;
        for (i12 = -innerR - 1; i12 <= innerR + 1; ++i12) {
            for (int k14 = -innerR - 1; k14 <= innerR + 1; ++k14) {
                for (j12 = innerB + 1; j12 <= innerB + innerH + 1; ++j12) {
                    int d = i12 * i12 + (j12 - innerB - 1) * (j12 - innerB - 1) + k14 * k14;
                    if (d < innerR * innerR) {
                        setAir(world, i12, j12, k14);
                        if (d <= (innerR - 1) * (innerR - 1) || random.nextInt(3) != 0) {
                            continue;
                        }
                        placeRandomBrick(world, random, i12, j12, k14);
                        continue;
                    }
                    if (d >= (innerR + 1) * (innerR + 1)) {
                        continue;
                    }
                    placeRandomBrick(world, random, i12, j12, k14);
                }
                placeRandomBrick(world, random, i12, innerB, k14);
            }
        }
        placeSpawnerChest(world, random, 0, innerB + 1, 0, LOTRLegacyBlocks.mod("spawnerChestStone"), 0, LOTREntities.BARROW_WIGHT, LOTRChestContents.BARROW_DOWNS);
        setBlockAndMetadata(world, 1, innerB + 1, 0, LOTRLegacyBlocks.vanilla("stone_stairs"), 0);
        setBlockAndMetadata(world, -1, innerB + 1, 0, LOTRLegacyBlocks.vanilla("stone_stairs"), 1);
        setBlockAndMetadata(world, 0, innerB + 1, -1, LOTRLegacyBlocks.vanilla("stone_stairs"), 2);
        setBlockAndMetadata(world, 0, innerB + 1, 1, LOTRLegacyBlocks.vanilla("stone_stairs"), 3);
        for (k1 = -radius + 2; k1 <= -innerR + 1; ++k1) {
            for (int i14 = -1; i14 <= 1; ++i14) {
                placeRandomBrick(world, random, i14, 0, k1);
                for (j12 = 1; j12 <= 3; ++j12) {
                    setAir(world, i14, j12, k1);
                }
                placeRandomBrick(world, random, i14, 4, k1);
            }
            for (j1 = 0; j1 <= 4; ++j1) {
                placeRandomBrick(world, random, -2, j1, k1);
                placeRandomBrick(world, random, 2, j1, k1);
            }
        }
        for (i12 = -1; i12 <= 1; ++i12) {
            setBlockAndMetadata(world, i12, innerB + 1, -innerR + 1, LOTRLegacyBlocks.vanilla("stone_stairs"), 3);
            for (j1 = 0; j1 <= 3; ++j1) {
                setAir(world, i12, j1, -innerR + 1);
                setAir(world, i12, j1, -innerR + 2);
            }
            setBlockAndMetadata(world, i12, 0, -innerR, LOTRLegacyBlocks.vanilla("stone_stairs"), 3);
        }
        placeRandomBrick(world, random, -2, innerB + 1, -innerR + 1);
        placeRandomBrick(world, random, 2, innerB + 1, -innerR + 1);
        for (int i15 : new int[]{-3, 3}) {
            placeRandomBrick(world, random, i15, 1, -radius + 1);
            placeRandomBrick(world, random, i15, 0, -radius + 1);
            placeRandomBrick(world, random, i15, -1, -radius + 1);
            placeRandomBrick(world, random, i15, 2, -radius + 2);
            placeRandomBrick(world, random, i15, 1, -radius + 2);
        }
        for (int i16 = -2; i16 <= 2; ++i16) {
            placeRandomBrick(world, random, i16, 5, -radius + 4);
            if (Math.abs(i16) > 1) {
                continue;
            }
            placeRandomBrick(world, random, i16, 5, -radius + 3);
        }
        for (int j15 = 1; j15 <= 3; ++j15) {
            placeRandomBrick(world, random, -1, j15, -radius + 4);
            placeRandomBrick(world, random, 0, j15, -radius + 6);
            placeRandomBrick(world, random, 1, j15, -radius + 4);
        }
        int rX = 0;
        int rY = height + 1;
        int rZ = 0;
        ruinsGen.generateWithSetRotation(world, random, getX(rX, rZ), getY(rY), getZ(rX, rZ), getRotationMode());
        return true;
    }

    public void placeRandomBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextBoolean()) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("stone"), 0);
        } else if (random.nextInt(3) > 0) {
            int l = random.nextInt(2);
            if (l == 0) {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
            }
            if (l == 1) {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("mossy_cobblestone"), 0);
            }
        } else {
            int l = random.nextInt(3);
            if (l == 0) {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
            }
            if (l == 1) {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
            }
            if (l == 2) {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
            }
        }
    }
}
