package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRWorldGenUtumnoEntrance: the ruin over Utumno in the Forodwaith -- a great hollow block of ice
 * brick from forty up to a hundred and more, broken open at one corner with a stair winding down the
 * inside, ringed with tumbled pillars of brick; and in its floor a shaft of entrance brick down to the
 * portal at ten, with ice pillars standing at its corners.
 */
public class LOTRUtumnoEntranceStructure extends LOTRStructureBase2 {

    public LOTRUtumnoEntranceStructure() {
        super(false);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        LOTRLegacyBlocks.LegacyBlock brick = LOTRLegacyBlocks.mod("utumnoBrick");
        LOTRLegacyBlocks.LegacyBlock entranceBrick = LOTRLegacyBlocks.mod("utumnoBrickEntrance");
        LOTRLegacyBlocks.LegacyBlock pillar = LOTRLegacyBlocks.mod("utumnoPillar");
        rotation = 2;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        this.originY = 0;
        int radius = 8;
        int baseHeight = 40;
        int portalHeight = 10;
        int portalBase = portalHeight - 2 - 1;
        for (int i1 = -radius; i1 <= radius; ++i1) {
            for (int k1 = -radius; k1 <= radius; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                int maxHeight = 100 + random.nextInt(10);
                for (int j1 = baseHeight; j1 <= maxHeight; ++j1) {
                    if (i2 == radius || k2 == radius || j1 == baseHeight || j1 >= maxHeight - 10) {
                        setBlockAndMetadata(world, i1, j1, k1, brick, 2);
                        continue;
                    }
                    setAir(world, i1, j1, k1);
                }
                if (i2 >= radius || k2 >= radius || random.nextInt(16) != 0) {
                    continue;
                }
                int height = 1 + random.nextInt(2);
                for (int j1 = baseHeight; j1 <= baseHeight + height; ++j1) {
                    setBlockAndMetadata(world, i1, j1, k1, brick, 2);
                }
            }
        }
        for (int l = 0; l < 40; ++l) {
            int i1 = -random.nextInt(radius * 3) + random.nextInt(radius * 3);
            int k1 = -random.nextInt(radius * 3) + random.nextInt(radius * 3);
            int width = 1 + random.nextInt(3);
            int height = width * 4 + random.nextInt(4);
            for (int i2 = i1 - width; i2 <= i1 + width; ++i2) {
                for (int k2 = k1 - width; k2 <= k1 + width; ++k2) {
                    int base = getTopBlock(world, i2, k2);
                    int top = base + height - random.nextInt(3);
                    for (int j2 = base; j2 < top; ++j2) {
                        setBlockAndMetadata(world, i2, j2, k2, brick, 2);
                    }
                }
            }
        }
        int entranceX = -radius;
        int entranceZ = -radius;
        int entranceY = 80;
        int entranceSize = 6;
        int entranceSizeExtra = entranceSize + 3;
        for (int i1 = entranceX - entranceSize; i1 <= entranceX + entranceSize; ++i1) {
            for (int j1 = entranceY - entranceSize; j1 <= entranceY + entranceSize; ++j1) {
                for (int k1 = entranceZ - entranceSize; k1 <= entranceZ + entranceSize; ++k1) {
                    int i2 = i1 - entranceX;
                    int j2 = j1 - entranceY;
                    int k2 = k1 - entranceZ;
                    float dist = i2 * i2 + j2 * j2 + k2 * k2;
                    if (dist >= entranceSize * entranceSize && (dist >= entranceSizeExtra * entranceSizeExtra || random.nextInt(6) != 0)) {
                        continue;
                    }
                    setAir(world, i1, j1, k1);
                }
            }
        }
        int stairX = entranceX + 1;
        int stairY = entranceY - entranceSize - 1;
        int stairZ = entranceZ + 1;
        int stairDirection = 2;
        while (true) {
            setBlockAndMetadata(world, stairX, stairY, stairZ, brick, 2);
            if (stairY <= baseHeight) {
                break;
            }
            if (stairDirection == 0 && isOpaque(world, stairX, --stairY, stairZ + 1)) {
                stairDirection = 1;
            }
            if (stairDirection == 1 && isOpaque(world, stairX - 1, stairY, stairZ)) {
                stairDirection = 2;
            }
            if (stairDirection == 2 && isOpaque(world, stairX, stairY, stairZ - 1)) {
                stairDirection = 3;
            }
            if (stairDirection == 3 && isOpaque(world, stairX + 1, stairY, stairZ)) {
                stairDirection = 0;
            }
            if (stairDirection == 0) {
                ++stairZ;
            }
            if (stairDirection == 1) {
                --stairX;
            }
            if (stairDirection == 2) {
                --stairZ;
            }
            if (stairDirection == 3) {
                ++stairX;
            }
        }
        for (int i1 = -2; i1 <= 2; ++i1) {
            for (int k1 = -2; k1 <= 2; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                for (int j1 = portalBase; j1 <= baseHeight + 1; ++j1) {
                    if (i2 < 2 && k2 < 2) {
                        if (j1 == portalBase) {
                            setBlockAndMetadata(world, i1, j1, k1, entranceBrick, 0);
                        } else if (j1 == portalHeight) {
                            setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("utumnoPortal"), 0);
                        } else {
                            setAir(world, i1, j1, k1);
                        }
                        continue;
                    }
                    setBlockAndMetadata(world, i1, j1, k1, entranceBrick, 0);
                }
                if (i2 != 2 || k2 != 2) {
                    continue;
                }
                int min = baseHeight + 2;
                int max = min + 2 + random.nextInt(2);
                for (int j1 = min; j1 <= max; ++j1) {
                    setBlockAndMetadata(world, i1, j1, k1, pillar, 1);
                }
                setBlockAndMetadata(world, i1, max + 1, k1, brick, 2);
                min = max + 2;
                max = min + 2;
                for (int j1 = min; j1 <= max; ++j1) {
                    setBlockAndMetadata(world, i1, j1, k1, pillar, 1);
                }
            }
        }
        return true;
    }
}
