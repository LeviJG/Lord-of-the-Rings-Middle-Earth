package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rhun;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRHorseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rhun.LOTREasterlingEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTREasterlingStablesStructure extends LOTREasterlingStructure {
    public LOTREasterlingStablesStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int k2;
        int i1;
        int i12;
        int j1;
        int k12;
        int i2;
        setOriginAndRotation(world, i, j, k, rotation, 1);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (i1 = -9; i1 <= 9; ++i1) {
                for (int k13 = -1; k13 <= 13; ++k13) {
                    j1 = getTopBlock(world, i1, k13) - 1;
                    if (!isSurface(world, i1, j1, k13)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 8) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i12 = -8; i12 <= 8; ++i12) {
            for (k1 = 0; k1 <= 12; ++k1) {
                i2 = Math.abs(i12);
                k2 = Math.floorMod(k1, 4);
                for (j1 = 1; j1 <= 6; ++j1) {
                    setAir(world, i12, j1, k1);
                }
                if (i2 == 0 && (k1 == 0 || k1 == 12)) {
                    for (j1 = 5; (j1 >= 0 || !isOpaque(world, i12, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                        setBlockAndMetadata(world, i12, j1, k1, woodBeamBlock, woodBeamMeta);
                        setGrassToDirt(world, i12, j1 - 1, k1);
                    }
                    continue;
                }
                if (i2 == 4 && k2 == 0) {
                    for (j1 = 4; (j1 >= 0 || !isOpaque(world, i12, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                        setBlockAndMetadata(world, i12, j1, k1, woodBeamBlock, woodBeamMeta);
                        setGrassToDirt(world, i12, j1 - 1, k1);
                    }
                    continue;
                }
                if (i2 == 8 && k2 == 0) {
                    for (j1 = 3; (j1 >= 0 || !isOpaque(world, i12, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                        setBlockAndMetadata(world, i12, j1, k1, woodBeamBlock, woodBeamMeta);
                        setGrassToDirt(world, i12, j1 - 1, k1);
                    }
                    continue;
                }
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i12, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                    setBlockAndMetadata(world, i12, j1, k1, brickBlock, brickMeta);
                    setGrassToDirt(world, i12, j1 - 1, k1);
                }
                if (!random.nextBoolean()) {
                    continue;
                }
                setBlockAndMetadata(world, i12, 1, k1, LOTRLegacyBlocks.mod("thatchFloor"), 0);
            }
        }
        for (k12 = 1; k12 <= 11; ++k12) {
            setBlockAndMetadata(world, 0, 5, k12, woodBeamBlock, woodBeamMeta | 8);
        }
        for (i12 = -3; i12 <= 3; ++i12) {
            setBlockAndMetadata(world, i12, 0, 0, woodBeamBlock, woodBeamMeta | 4);
        }
        for (i12 = -8; i12 <= 8; ++i12) {
            for (k1 = 0; k1 <= 12; ++k1) {
                i2 = Math.abs(i12);
                k2 = Math.floorMod(k1, 4);
                if (i2 >= 5 && i2 <= 7) {
                    if (k1 == 0) {
                        setBlockAndMetadata(world, i12, 1, 0, plankStairBlock, 3);
                        setBlockAndMetadata(world, i12, 2, 0, plankStairBlock, 2);
                    } else if (k1 == 12) {
                        setBlockAndMetadata(world, i12, 1, 12, plankStairBlock, 2);
                        setBlockAndMetadata(world, i12, 2, 12, plankStairBlock, 3);
                    } else if (k2 == 0) {
                        setBlockAndMetadata(world, i12, 1, k1, plankBlock, plankMeta);
                        setBlockAndMetadata(world, i12, 2, k1, plankBlock, plankMeta);
                    } else {
                        int randomGround = random.nextInt(2);
                        if (randomGround == 0) {
                            setBlockAndMetadata(world, i12, 0, k1, LOTRLegacyBlocks.vanilla("dirt"), 1);
                        } else {
                            setBlockAndMetadata(world, i12, 0, k1, LOTRLegacyBlocks.mod("dirtPath"), 1);
                        }
                    }
                }
                if (i2 >= 1 && i2 <= 3 && k1 == 12) {
                    setBlockAndMetadata(world, i12, 1, 12, plankStairBlock, 2);
                    setBlockAndMetadata(world, i12, 2, 12, plankStairBlock, 3);
                    setBlockAndMetadata(world, i12, 3, 12, fenceBlock, fenceMeta);
                }
                if (i2 == 4 && k2 != 0) {
                    setBlockAndMetadata(world, i12, 1, k1, fenceGateBlock, i12 > 0 ? 1 : 3);
                }
                if (i2 == 8 && k2 != 0) {
                    setBlockAndMetadata(world, i12, 1, k1, plankStairBlock, i12 > 0 ? 1 : 0);
                    setBlockAndMetadata(world, i12, 2, k1, plankStairBlock, i12 > 0 ? 0 : 1);
                }
                if (i2 == 6 && k2 == 2) {
                    LOTRHorseEntity horse = create(LOTREntities.HORSE, world);
                    spawnNPCAndSetHome(horse, world, i12, 1, k1, 0);
                    horse.saddleMountForWorldGen();
                    horse.clearHome();
                }
                if (i2 == 4) {
                    if (k2 == 1) {
                        setBlockAndMetadata(world, i12, 3, k1, LOTRLegacyBlocks.vanilla("torch"), 3);
                    } else if (k2 == 3) {
                        setBlockAndMetadata(world, i12, 3, k1, LOTRLegacyBlocks.vanilla("torch"), 4);
                    }
                }
                if (i2 != 0 || k2 != 2) {
                    continue;
                }
                setBlockAndMetadata(world, i12, 4, k1, LOTRLegacyBlocks.mod("chandelier"), 0);
            }
        }
        for (k12 = 0; k12 <= 12; ++k12) {
            int k22 = Math.floorMod(k12, 4);
            setBlockAndMetadata(world, -8, 4, k12, roofStairBlock, 1);
            for (i1 = -7; i1 <= -5; ++i1) {
                setBlockAndMetadata(world, i1, 4, k12, roofBlock, roofMeta);
            }
            if (k22 != 0) {
                setBlockAndMetadata(world, -4, 4, k12, roofStairBlock, 4);
            }
            setBlockAndMetadata(world, -5, 5, k12, roofStairBlock, 1);
            for (i1 = -4; i1 <= -2; ++i1) {
                setBlockAndMetadata(world, i1, 5, k12, roofBlock, roofMeta);
            }
            setBlockAndMetadata(world, -1, 5, k12, roofStairBlock, 4);
            setBlockAndMetadata(world, -2, 6, k12, roofStairBlock, 1);
            for (i1 = -1; i1 <= 1; ++i1) {
                setBlockAndMetadata(world, i1, 6, k12, roofBlock, roofMeta);
            }
            setBlockAndMetadata(world, 2, 6, k12, roofStairBlock, 0);
            setBlockAndMetadata(world, 1, 5, k12, roofStairBlock, 5);
            for (i1 = 2; i1 <= 4; ++i1) {
                setBlockAndMetadata(world, i1, 5, k12, roofBlock, roofMeta);
            }
            setBlockAndMetadata(world, 5, 5, k12, roofStairBlock, 0);
            if (k22 != 0) {
                setBlockAndMetadata(world, 4, 4, k12, roofStairBlock, 5);
            }
            for (i1 = 5; i1 <= 7; ++i1) {
                setBlockAndMetadata(world, i1, 4, k12, roofBlock, roofMeta);
            }
            setBlockAndMetadata(world, 8, 4, k12, roofStairBlock, 0);
        }
        for (int k13 : new int[]{-1, 13}) {
            setBlockAndMetadata(world, -8, 3, k13, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, -4, 4, k13, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, 0, 5, k13, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, 4, 4, k13, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, 8, 3, k13, fenceBlock, fenceMeta);
        }
        for (int k14 = 0; k14 <= 12; ++k14) {
            if (Math.floorMod(k14, 4) != 0) {
                continue;
            }
            setBlockAndMetadata(world, -9, 3, k14, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, 9, 3, k14, fenceBlock, fenceMeta);
        }
        setBlockAndMetadata(world, 0, 4, -1, plankBlock, plankMeta);
        spawnItemFrame(world, 0, 4, -1, 2, LOTRLegacyItems.vanillaStack("saddle", 1, 0));
        spawnItemFrame(world, 0, 4, -1, 1, LOTRLegacyItems.vanillaStack("saddle", 1, 0));
        spawnItemFrame(world, 0, 4, -1, 3, LOTRLegacyItems.vanillaStack("saddle", 1, 0));
        placeChest(world, random, -3, 1, 4, 4, chestContents);
        placeChest(world, random, 3, 1, 4, 5, chestContents);
        setBlockAndMetadata(world, 0, 1, 4, plankStairBlock, 2);
        setBlockAndMetadata(world, 0, 1, 5, LOTRLegacyBlocks.vanilla("cauldron"), 3);
        setBlockAndMetadata(world, 0, 1, 6, LOTRLegacyBlocks.vanilla("cauldron"), 3);
        setBlockAndMetadata(world, 0, 1, 7, plankStairBlock, 3);
        for (int i13 = -2; i13 <= 2; ++i13) {
            int h = 3 - Math.abs(i13);
            for (int j12 = 1; j12 < 1 + h; ++j12) {
                setBlockAndMetadata(world, i13, j12, 11, LOTRLegacyBlocks.vanilla("hay_block"), 0);
            }
            int h1 = h - 1;
            if (h1 < 1) {
                continue;
            }
            for (int j13 = 1; j13 < 1 + h1; ++j13) {
                setBlockAndMetadata(world, i13, j13, 10, LOTRLegacyBlocks.vanilla("hay_block"), 0);
            }
        }
        int men = 1 + random.nextInt(2);
        for (int l = 0; l < men; ++l) {
            LOTREasterlingEntity easterling = create(LOTREntities.EASTERLING, world);
            spawnNPCAndSetHome(easterling, world, 0, 1, 3, 8);
        }
        return true;
    }
}
