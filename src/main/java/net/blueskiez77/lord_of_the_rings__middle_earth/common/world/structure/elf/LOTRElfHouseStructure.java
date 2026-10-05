package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRGaladhrimElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature.LOTRMallornExtremeStructure;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRElfHouseStructure extends LOTRStructureBase2 {
    public LOTRElfHouseStructure(boolean flag) {
        super(flag);
    }

    public static ItemStack getRandomChandelier(RandomSource random) {
        if (random.nextBoolean()) {
            int i = random.nextInt(3);
            switch (i) {
                case 0:
                    return LOTRLegacyBlocks.mod("chandelier").stack(1, 13);
                case 1:
                    return LOTRLegacyBlocks.mod("chandelier").stack(1, 14);
                case 2:
                    return LOTRLegacyBlocks.mod("chandelier").stack(1, 15);
                default:
                    break;
            }
        }
        return LOTRLegacyBlocks.mod("chandelier").stack(1, 5);
    }

    public static ItemStack getRandomPlant(RandomSource random) {
        return random.nextBoolean() ? LOTRLegacyBlocks.mod("elanor").stack(1, 0) : LOTRLegacyBlocks.mod("niphredil").stack(1, 0);
    }

    public static LegacyBlock getRandomTorch(RandomSource random) {
        if (random.nextBoolean()) {
            int i = random.nextInt(3);
            switch (i) {
                case 0:
                    return LOTRLegacyBlocks.mod("mallornTorchBlue");
                case 1:
                    return LOTRLegacyBlocks.mod("mallornTorchGold");
                case 2:
                    return LOTRLegacyBlocks.mod("mallornTorchGreen");
                default:
                    break;
            }
        }
        return LOTRLegacyBlocks.mod("mallornTorchSilver");
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int j12;
        int i1;
        int k1;
        int k12;
        int k13;
        int k14;
        setOriginAndRotation(world, i, j, k, rotation, usingPlayer != null ? 2 : 0);
        if (usingPlayer != null) {
            LOTRMallornExtremeStructure treeGen = new LOTRMallornExtremeStructure(true);
            int i12 = 0;
            j12 = 0;
            k14 = 0;
            int height = treeGen.generateAndReturnHeight(world, random, getX(i12, k14), getY(j12), getZ(i12, k14), true);
            originY += Mth.floor(height * Mth.randomBetween(random, LOTRMallornExtremeStructure.HOUSE_HEIGHT_MIN, LOTRMallornExtremeStructure.HOUSE_HEIGHT_MAX));
        }
        if (restrictions) {
            for (int i13 = -8; i13 <= 8; ++i13) {
                for (j1 = -3; j1 <= 6; ++j1) {
                    for (k1 = -8; k1 <= 8; ++k1) {
                        if (Math.abs(i13) <= 2 && Math.abs(k1) <= 2 || isAir(world, i13, j1, k1)) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        } else if (usingPlayer != null) {
            for (int i14 = -2; i14 <= 2; ++i14) {
                for (k12 = -2; k12 <= 2; ++k12) {
                    j12 = 0;
                    while (!isOpaque(world, i14, j12, k12) && getY(j12) >= world.getMinY()) {
                        setBlockAndMetadata(world, i14, j12, k12, LOTRLegacyBlocks.mod("wood"), 1);
                        --j12;
                    }
                }
            }
        }
        for (i1 = -7; i1 <= 7; ++i1) {
            for (j1 = 1; j1 <= 4; ++j1) {
                for (k1 = -7; k1 <= 7; ++k1) {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                }
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (j1 = -1; j1 <= 5; ++j1) {
                for (k1 = -2; k1 <= 2; ++k1) {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("wood"), 1);
                    if (j1 < 1 || j1 > 2 || Math.abs(i1) != 2 || Math.abs(k1) != 2) {
                        continue;
                    }
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("fence"), 1);
                }
            }
        }
        for (i1 = -6; i1 <= 6; ++i1) {
            for (k12 = -6; k12 <= 6; ++k12) {
                if (Math.abs(i1) <= 2 && Math.abs(k12) <= 2 || Math.abs(i1) == 6 || Math.abs(k12) == 6) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 0, k12, LOTRLegacyBlocks.mod("planks"), 1);
            }
        }
        for (i1 = -5; i1 <= 5; ++i1) {
            setBlockAndMetadata(world, i1, 0, -6, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, i1, 0, 6, LOTRLegacyBlocks.mod("planks"), 1);
        }
        for (k13 = -5; k13 <= 5; ++k13) {
            setBlockAndMetadata(world, -6, 0, k13, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, 6, 0, k13, LOTRLegacyBlocks.mod("planks"), 1);
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            setBlockAndMetadata(world, i1, 0, -7, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, i1, 0, 7, LOTRLegacyBlocks.mod("planks"), 1);
        }
        for (k13 = -3; k13 <= 3; ++k13) {
            setBlockAndMetadata(world, -7, 0, k13, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, 7, 0, k13, LOTRLegacyBlocks.mod("planks"), 1);
        }
        for (int j13 = 1; j13 <= 4; ++j13) {
            setBlockAndMetadata(world, -5, j13, -5, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, 5, j13, -5, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, -5, j13, 5, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, 5, j13, 5, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, -6, j13, -3, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndMetadata(world, -6, j13, 3, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndMetadata(world, 6, j13, -3, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndMetadata(world, 6, j13, 3, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndMetadata(world, -3, j13, -6, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndMetadata(world, -3, j13, 6, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndMetadata(world, 3, j13, -6, LOTRLegacyBlocks.mod("wood"), 1);
            setBlockAndMetadata(world, 3, j13, 6, LOTRLegacyBlocks.mod("wood"), 1);
        }
        setBlockAndMetadata(world, -4, 2, -5, getRandomTorch(random), 2);
        setBlockAndMetadata(world, -5, 2, -4, getRandomTorch(random), 3);
        setBlockAndMetadata(world, 4, 2, -5, getRandomTorch(random), 1);
        setBlockAndMetadata(world, 5, 2, -4, getRandomTorch(random), 3);
        setBlockAndMetadata(world, -4, 2, 5, getRandomTorch(random), 2);
        setBlockAndMetadata(world, -5, 2, 4, getRandomTorch(random), 4);
        setBlockAndMetadata(world, 4, 2, 5, getRandomTorch(random), 1);
        setBlockAndMetadata(world, 5, 2, 4, getRandomTorch(random), 4);
        for (i1 = -3; i1 <= 3; ++i1) {
            setBlockAndMetadata(world, i1, 1, -7, LOTRLegacyBlocks.mod("fence"), 1);
            setBlockAndMetadata(world, i1, 1, 7, LOTRLegacyBlocks.mod("fence"), 1);
        }
        for (k13 = -3; k13 <= 3; ++k13) {
            setBlockAndMetadata(world, -7, 1, k13, LOTRLegacyBlocks.mod("fence"), 1);
            setBlockAndMetadata(world, 7, 1, k13, LOTRLegacyBlocks.mod("fence"), 1);
        }
        setBlockAndMetadata(world, -4, 1, -6, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, -5, 1, -6, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, -4, 1, 6, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, -5, 1, 6, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, 4, 1, -6, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, 5, 1, -6, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, 4, 1, 6, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, 5, 1, 6, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, -6, 1, -4, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, -6, 1, -5, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, 6, 1, -4, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, 6, 1, -5, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, -6, 1, 4, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, -6, 1, 5, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, 6, 1, 4, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, 6, 1, 5, LOTRLegacyBlocks.mod("fence"), 1);
        setBlockAndMetadata(world, -6, 4, -2, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
        setBlockAndMetadata(world, -6, 4, -1, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, -6, 4, 0, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, -6, 4, 1, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, -6, 4, 2, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
        setBlockAndMetadata(world, 6, 4, -2, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
        setBlockAndMetadata(world, 6, 4, -1, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, 6, 4, 0, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, 6, 4, 1, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, 6, 4, 2, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
        setBlockAndMetadata(world, -2, 4, -6, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
        setBlockAndMetadata(world, -1, 4, -6, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, 0, 4, -6, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, 1, 4, -6, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, 2, 4, -6, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
        setBlockAndMetadata(world, -2, 4, 6, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
        setBlockAndMetadata(world, -1, 4, 6, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, 0, 4, 6, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, 1, 4, 6, LOTRLegacyBlocks.mod("woodSlabSingle"), 9);
        setBlockAndMetadata(world, 2, 4, 6, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
        for (i1 = -6; i1 <= -4; ++i1) {
            setBlockAndMetadata(world, i1, 4, -6, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
            setBlockAndMetadata(world, i1, 4, 6, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
        }
        for (i1 = 4; i1 <= 6; ++i1) {
            setBlockAndMetadata(world, i1, 4, -6, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
            setBlockAndMetadata(world, i1, 4, 6, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
        }
        for (k13 = -6; k13 <= -4; ++k13) {
            setBlockAndMetadata(world, -6, 4, k13, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
            setBlockAndMetadata(world, 6, 4, k13, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
        }
        for (k13 = 4; k13 <= 6; ++k13) {
            setBlockAndMetadata(world, -6, 4, k13, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
            setBlockAndMetadata(world, 6, 4, k13, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
        }
        for (i1 = -4; i1 <= 4; ++i1) {
            if (Math.abs(i1) <= 1) {
                continue;
            }
            setBlockAndMetadata(world, i1, 4, -5, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
            setBlockAndMetadata(world, i1, 4, 5, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
        }
        for (k13 = -4; k13 <= 4; ++k13) {
            if (Math.abs(k13) <= 1) {
                continue;
            }
            setBlockAndMetadata(world, -5, 4, k13, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
            setBlockAndMetadata(world, 5, 4, k13, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
        }
        for (i1 = -6; i1 <= 6; ++i1) {
            for (k12 = -6; k12 <= 6; ++k12) {
                if (restrictions && i1 >= -2 && i1 <= 2 && k12 >= -2 && k12 <= 2 || (i1 == -6 || i1 == 6) && (k12 == -6 || k12 == 6)) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 5, k12, LOTRLegacyBlocks.mod("planks"), 1);
            }
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            setBlockAndMetadata(world, i1, 5, -7, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, i1, 5, 7, LOTRLegacyBlocks.mod("planks"), 1);
        }
        for (k13 = -3; k13 <= 3; ++k13) {
            setBlockAndMetadata(world, -7, 5, k13, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, 7, 5, k13, LOTRLegacyBlocks.mod("planks"), 1);
        }
        for (i1 = -5; i1 <= 5; ++i1) {
            for (k12 = -5; k12 <= 5; ++k12) {
                if (restrictions && i1 >= -2 && i1 <= 2 && k12 >= -2 && k12 <= 2 || (i1 == -5 || i1 == 5) && (k12 == -5 || k12 == 5)) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 6, k12, LOTRLegacyBlocks.mod("planks"), 1);
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            setBlockAndMetadata(world, i1, 6, -6, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, i1, 6, 6, LOTRLegacyBlocks.mod("planks"), 1);
        }
        for (k13 = -2; k13 <= 2; ++k13) {
            setBlockAndMetadata(world, -6, 6, k13, LOTRLegacyBlocks.mod("planks"), 1);
            setBlockAndMetadata(world, 6, 6, k13, LOTRLegacyBlocks.mod("planks"), 1);
        }
        for (i1 = -8; i1 <= 8; ++i1) {
            int stairZ;
            int stairX = i1;
            int i2 = Math.abs(i1);
            stairZ = i2 <= 3 ? 8 : i2 <= 5 ? 7 : i2 <= 7 ? 6 : 4;
            setBlockAndMetadata(world, stairX, 5, -stairZ, LOTRLegacyBlocks.mod("stairsMallorn"), 2);
            setBlockAndMetadata(world, stairX, 5, stairZ, LOTRLegacyBlocks.mod("stairsMallorn"), 3);
            stairX = Integer.signum(stairX) * (Math.abs(stairX) - 1);
            stairZ--;
            setBlockAndMetadata(world, stairX, 6, -stairZ, LOTRLegacyBlocks.mod("stairsMallorn"), 2);
            setBlockAndMetadata(world, stairX, 6, stairZ, LOTRLegacyBlocks.mod("stairsMallorn"), 3);
        }
        for (k13 = -8; k13 <= 8; ++k13) {
            int stairX;
            int stairZ = k13;
            int k2 = Math.abs(k13);
            stairX = k2 <= 3 ? 8 : k2 <= 5 ? 7 : k2 <= 7 ? 6 : 4;
            setBlockAndMetadata(world, -stairX, 5, stairZ, LOTRLegacyBlocks.mod("stairsMallorn"), 1);
            setBlockAndMetadata(world, stairX, 5, stairZ, LOTRLegacyBlocks.mod("stairsMallorn"), 0);
            stairZ = Integer.signum(stairZ) * (Math.abs(stairZ) - 1);
            stairX--;
            setBlockAndMetadata(world, -stairX, 6, stairZ, LOTRLegacyBlocks.mod("stairsMallorn"), 1);
            setBlockAndMetadata(world, stairX, 6, stairZ, LOTRLegacyBlocks.mod("stairsMallorn"), 0);
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            setBlockAndMetadata(world, i1, 4, -3, LOTRLegacyBlocks.mod("stairsMallorn"), 6);
            setBlockAndMetadata(world, i1, 4, 3, LOTRLegacyBlocks.mod("stairsMallorn"), 7);
        }
        for (k13 = -2; k13 <= 2; ++k13) {
            setBlockAndMetadata(world, -3, 4, k13, LOTRLegacyBlocks.mod("stairsMallorn"), 5);
            setBlockAndMetadata(world, 3, 4, k13, LOTRLegacyBlocks.mod("stairsMallorn"), 4);
        }
        for (int bough = 0; bough <= 2; ++bough) {
            j1 = -3 + bough;
            k14 = 3 + bough;
            for (int i2 = -bough; i2 <= bough; ++i2) {
                for (int k2 = -k14; k2 <= k14; ++k2) {
                    setBlockAndMetadata(world, i2, j1, k2, LOTRLegacyBlocks.mod("wood"), 13);
                    setBlockAndMetadata(world, k2, j1, i2, LOTRLegacyBlocks.mod("wood"), 13);
                }
            }
        }
        LegacyBlock ladder = random.nextBoolean() ? LOTRLegacyBlocks.mod("hithlainLadder") : LOTRLegacyBlocks.mod("mallornLadder");
        for (j1 = 3; j1 >= -3 || !isOpaque(world, 0, j1, -3) && getY(j1) >= world.getMinY(); --j1) {
            setBlockAndMetadata(world, 0, j1, -3, ladder, 2);
        }
        setBlockAndMetadata(world, -2, 1, 0, LOTRLegacyBlocks.mod("elvenTable"), 0);
        setBlockAndMetadata(world, -2, 2, 0, LOTRLegacyBlocks.vanilla("air"), 0);
        setBlockAndMetadata(world, -2, 3, 0, LOTRLegacyBlocks.vanilla("air"), 0);
        setBlockAndMetadata(world, -2, 4, 0, LOTRLegacyBlocks.mod("wood"), 5);
        setBlockAndMetadata(world, 2, 1, 0, LOTRLegacyBlocks.mod("elvenTable"), 0);
        setBlockAndMetadata(world, 2, 2, 0, LOTRLegacyBlocks.vanilla("air"), 0);
        setBlockAndMetadata(world, 2, 3, 0, LOTRLegacyBlocks.vanilla("air"), 0);
        setBlockAndMetadata(world, 2, 4, 0, LOTRLegacyBlocks.mod("wood"), 5);
        placeChest(world, random, 0, 1, 2, LOTRLegacyBlocks.mod("chestMallorn"), 0, LOTRChestContents.ELF_HOUSE);
        setBlockAndMetadata(world, 0, 2, 2, LOTRLegacyBlocks.vanilla("air"), 0);
        setBlockAndMetadata(world, 0, 3, 2, LOTRLegacyBlocks.vanilla("air"), 0);
        setBlockAndMetadata(world, 0, 4, 2, LOTRLegacyBlocks.mod("wood"), 9);
        tryPlaceLight(world, -7, -1, -3, random);
        tryPlaceLight(world, -7, -1, 3, random);
        tryPlaceLight(world, 7, -1, -3, random);
        tryPlaceLight(world, 7, -1, 3, random);
        tryPlaceLight(world, -3, -1, -7, random);
        tryPlaceLight(world, 3, -1, -7, random);
        tryPlaceLight(world, -3, -1, 7, random);
        tryPlaceLight(world, 3, -1, 7, random);
        placeFlowerPot(world, -4, 1, -5, getRandomPlant(random));
        placeFlowerPot(world, -5, 1, -4, getRandomPlant(random));
        placeFlowerPot(world, -5, 1, 4, getRandomPlant(random));
        placeFlowerPot(world, -4, 1, 5, getRandomPlant(random));
        placeFlowerPot(world, 4, 1, -5, getRandomPlant(random));
        placeFlowerPot(world, 5, 1, -4, getRandomPlant(random));
        placeFlowerPot(world, 5, 1, 4, getRandomPlant(random));
        placeFlowerPot(world, 4, 1, 5, getRandomPlant(random));
        setBlockAndMetadata(world, -2, 1, 5, LOTRLegacyBlocks.mod("elvenBed"), 3);
        setBlockAndMetadata(world, -3, 1, 5, LOTRLegacyBlocks.mod("elvenBed"), 11);
        LOTRGaladhrimElfEntity elf = create(LOTREntities.GALADHRIM_ELF, world);
        elf.spawnRidingHorse = false;
        spawnNPCAndSetHome(elf, world, 0, 1, 4, 8);
        return true;
    }

    public void tryPlaceLight(WorldGenLevel world, int i, int j, int k, RandomSource random) {
        int j1;
        int height = 2 + random.nextInt(6);
        for (j1 = j; j1 >= -height; --j1) {
            if (!restrictions) {
                continue;
            }
            if (!isAir(world, i, j1, k)) {
                return;
            }
            if (j1 != -height || isAir(world, i, j1, k - 1) && isAir(world, i, j1, k + 1) && isAir(world, i - 1, j1, k) && isAir(world, i + 1, j1, k)) {
                continue;
            }
            return;
        }
        for (j1 = j; j1 >= j - height; --j1) {
            if (j1 == j - height) {
                setBlockAndMetadata(world, i, j1, k, LOTRLegacyBlocks.mod("planks"), 1);
                setBlockAndMetadata(world, i, j1, k - 1, getRandomTorch(random), 4);
                setBlockAndMetadata(world, i, j1, k + 1, getRandomTorch(random), 3);
                setBlockAndMetadata(world, i - 1, j1, k, getRandomTorch(random), 1);
                setBlockAndMetadata(world, i + 1, j1, k, getRandomTorch(random), 2);
                continue;
            }
            setBlockAndMetadata(world, i, j1, k, LOTRLegacyBlocks.mod("fence"), 1);
        }
    }
}
