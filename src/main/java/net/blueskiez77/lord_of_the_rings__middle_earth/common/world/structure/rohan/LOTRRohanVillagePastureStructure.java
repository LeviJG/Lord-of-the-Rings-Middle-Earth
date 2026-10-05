package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorBarnStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRRohanVillagePastureStructure extends LOTRRohanStructure {
    public LOTRRohanVillagePastureStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int i1;
        int j1;
        int k1;
        setOriginAndRotation(world, i, j, k, rotation, 5);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i12 = -4; i12 <= 4; ++i12) {
                for (int k12 = -4; k12 <= 4; ++k12) {
                    j1 = getTopBlock(world, i12, k12) - 1;
                    if (!isSurface(world, i12, j1, k12)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 4) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -4; i1 <= 4; ++i1) {
            for (k1 = -4; k1 <= 4; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                    if (j1 == 0) {
                        int randomFloor = random.nextInt(3);
                        switch (randomFloor) {
                            case 0:
                                setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                                break;
                            case 1:
                                setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("dirt"), 1);
                                break;
                            case 2:
                                setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.mod("dirtPath"), 0);
                                break;
                            default:
                                break;
                        }
                    } else {
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    }
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                for (j1 = 1; j1 <= 5; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                if (i2 == 4 && k2 == 4) {
                    setGrassToDirt(world, i1, -1, k1);
                    for (j1 = 1; j1 <= 2; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, logBlock, logMeta);
                    }
                    setBlockAndMetadata(world, i1, 3, k1, LOTRLegacyBlocks.vanilla("torch"), 5);
                    continue;
                }
                if (i2 != 4 && k2 != 4) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 1, k1, fenceBlock, fenceMeta);
            }
        }
        setBlockAndMetadata(world, 0, 1, -4, fenceGateBlock, 0);
        for (i1 = -1; i1 <= 1; ++i1) {
            for (k1 = -1; k1 <= 1; ++k1) {
                if (random.nextInt(3) != 0) {
                    continue;
                }
                int j12 = 1;
                int j2 = 1;
                if (i1 == 0 && k1 == 0 && random.nextBoolean()) {
                    ++j2;
                }
                for (int j3 = j12; j3 <= j2; ++j3) {
                    setBlockAndMetadata(world, i1, j3, k1, LOTRLegacyBlocks.vanilla("hay_block"), 0);
                }
            }
        }
        int animals = 4 + random.nextInt(5);
        for (int l = 0; l < animals; ++l) {
            Animal animal = LOTRGondorBarnStructure.getRandomAnimal(world, random);
            int i13 = 3 * (random.nextBoolean() ? 1 : -1);
            int k13 = 3 * (random.nextBoolean() ? 1 : -1);
            if (random.nextBoolean()) {
                spawnNPCAndSetHome(animal, world, i13, 1, 0, 0);
            } else {
                spawnNPCAndSetHome(animal, world, 0, 1, k13, 0);
            }
            animal.clearHome();
        }
        return true;
    }
}
