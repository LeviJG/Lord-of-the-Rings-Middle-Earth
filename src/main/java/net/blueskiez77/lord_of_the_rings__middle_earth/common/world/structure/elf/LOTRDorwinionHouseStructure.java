package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dorwinion.LOTRDorwinionManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRDorwinionHouseStructure extends LOTRStructureBase2 {
    public LegacyBlock woodBeamBlock;
    public int woodBeamMeta;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock doorBlock;
    public LegacyBlock floorBlock;
    public int floorMeta;
    public LegacyBlock wallBlock;
    public int wallMeta;
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock brickWallBlock;
    public int brickWallMeta;
    public LegacyBlock pillarBlock;
    public int pillarMeta;
    public LegacyBlock clayBlock;
    public int clayMeta;
    public LegacyBlock claySlabBlock;
    public int claySlabMeta;
    public LegacyBlock clayStairBlock;
    public LegacyBlock leafBlock;
    public int leafMeta;
    public LegacyBlock plateBlock;

    public LOTRDorwinionHouseStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int i1;
        int beam;
        int k1;
        int k12;
        setOriginAndRotation(world, i, j, k, rotation, 1);
        setupRandomBlocks(random);
        if (restrictions) {
            for (int i12 = -10; i12 <= 3; ++i12) {
                for (k1 = 0; k1 <= 10; ++k1) {
                    j1 = getTopBlock(world, i12, k1) - 1;
                    BlockState block = getBlockState(world, i12, j1, k1);
                    if (LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (int i12 = -10; i12 <= 3; ++i12) {
            for (k1 = 0; k1 <= 10; ++k1) {
                boolean garden;
                int j12;
                for (j1 = 1; j1 <= 10; ++j1) {
                    setAir(world, i12, j1, k1);
                }
                beam = 0;
                if ((i12 == -2 || i12 == 3) && k1 == 0) {
                    beam = 1;
                }
                if (i12 == 3 && k1 == 5) {
                    beam = 1;
                }
                if ((i12 == 3 || i12 == -2 || i12 == -10) && k1 == 10) {
                    beam = 1;
                }
                if ((i12 == -10 || i12 == -2) && k1 == 4) {
                    beam = 1;
                }
                boolean wall = k1 == 0 || k1 == 10;
                if (i12 == 3 || i12 == -10) {
                    wall = true;
                }
                if (i12 == -2 && k1 <= 4) {
                    wall = true;
                }
                if (k1 == 4 && i12 <= -2) {
                    wall = true;
                }
                garden = i12 <= -3 && k1 >= 0 && k1 <= 3;
                if (garden) {
                    setBlockAndMetadata(world, i12, 0, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                    j12 = -1;
                    while (!isOpaque(world, i12, j12, k1) && getY(j12) >= world.getMinY()) {
                        setBlockAndMetadata(world, i12, j12, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                        setGrassToDirt(world, i12, j12 - 1, k1);
                        --j12;
                    }
                    if (random.nextInt(3) != 0) {
                        continue;
                    }
                    int j13 = 1;
                    plantFlower(world, random, i12, j13, k1);
                    continue;
                }
                if (beam != 0) {
                    for (int j122 = 1; j122 <= 8; ++j122) {
                        setBlockAndMetadata(world, i12, j122, k1, woodBeamBlock, woodBeamMeta);
                    }
                    for (j12 = 0; (j12 >= 0 || !isOpaque(world, i12, j12, k1)) && getY(j12) >= world.getMinY(); --j12) {
                        setBlockAndMetadata(world, i12, j12, k1, wallBlock, wallMeta);
                        setGrassToDirt(world, i12, j12 - 1, k1);
                    }
                    continue;
                }
                if (wall) {
                    for (j12 = 0; (j12 >= 0 || !isOpaque(world, i12, j12, k1)) && getY(j12) >= world.getMinY(); --j12) {
                        setBlockAndMetadata(world, i12, j12, k1, wallBlock, wallMeta);
                        setGrassToDirt(world, i12, j12 - 1, k1);
                    }
                    setBlockAndMetadata(world, i12, 1, k1, wallBlock, wallMeta);
                    setBlockAndMetadata(world, i12, 2, k1, brickBlock, brickMeta);
                    setBlockAndMetadata(world, i12, 3, k1, brickBlock, brickMeta);
                    setBlockAndMetadata(world, i12, 5, k1, wallBlock, wallMeta);
                    setBlockAndMetadata(world, i12, 6, k1, brickBlock, brickMeta);
                    setBlockAndMetadata(world, i12, 7, k1, brickBlock, brickMeta);
                    if (i12 == -10 || i12 == -2 || i12 == 3) {
                        setBlockAndMetadata(world, i12, 4, k1, woodBeamBlock, woodBeamMeta | 8);
                        setBlockAndMetadata(world, i12, 8, k1, woodBeamBlock, woodBeamMeta | 8);
                        continue;
                    }
                    setBlockAndMetadata(world, i12, 4, k1, woodBeamBlock, woodBeamMeta | 4);
                    setBlockAndMetadata(world, i12, 8, k1, woodBeamBlock, woodBeamMeta | 4);
                    continue;
                }
                for (j12 = 0; (j12 >= 0 || !isOpaque(world, i12, j12, k1)) && getY(j12) >= world.getMinY(); --j12) {
                    setBlockAndMetadata(world, i12, j12, k1, floorBlock, floorMeta);
                    setGrassToDirt(world, i12, j12 - 1, k1);
                }
                if ((i12 >= 0 && i12 <= 1 && k1 >= 2 || i12 >= -8 && i12 <= -2 && k1 >= 6) && k1 <= 8) {
                    setBlockAndMetadata(world, i12, 4, k1, plankSlabBlock, plankSlabMeta | 8);
                    continue;
                }
                setBlockAndMetadata(world, i12, 4, k1, plankBlock, plankMeta);
            }
        }
        for (int j14 : new int[]{2, 6}) {
            setAir(world, 0, j14, 0);
            setAir(world, 1, j14, 0);
            setBlockAndMetadata(world, 0, j14 + 1, 0, brickStairBlock, 4);
            setBlockAndMetadata(world, 1, j14 + 1, 0, brickStairBlock, 5);
            for (int k13 : new int[]{2, 7}) {
                setAir(world, 3, j14, k13);
                setAir(world, 3, j14, k13 + 1);
                setBlockAndMetadata(world, 3, j14 + 1, k13, brickStairBlock, 7);
                setBlockAndMetadata(world, 3, j14 + 1, k13 + 1, brickStairBlock, 6);
            }
            for (int i13 : new int[]{-4, -7}) {
                setAir(world, i13, j14, 10);
                setAir(world, i13 - 1, j14, 10);
                setBlockAndMetadata(world, i13, j14 + 1, 10, brickStairBlock, 5);
                setBlockAndMetadata(world, i13 - 1, j14 + 1, 10, brickStairBlock, 4);
            }
            setAir(world, -10, j14, 8);
            setAir(world, -10, j14, 7);
            setAir(world, -10, j14, 6);
            setBlockAndMetadata(world, -10, j14 + 1, 8, brickStairBlock, 6);
            setBlockAndMetadata(world, -10, j14 + 1, 7, brickSlabBlock, brickSlabMeta | 8);
            setBlockAndMetadata(world, -10, j14 + 1, 6, brickStairBlock, 7);
            for (int i13 : new int[]{-8, -5}) {
                setAir(world, i13, j14, 4);
                setAir(world, i13 + 1, j14, 4);
                setBlockAndMetadata(world, i13, j14 + 1, 4, brickStairBlock, 4);
                setBlockAndMetadata(world, i13 + 1, j14 + 1, 4, brickStairBlock, 5);
            }
            setAir(world, -2, j14, 2);
            setBlockAndMetadata(world, -2, j14 + 1, 2, brickSlabBlock, brickSlabMeta | 8);
        }
        setAir(world, 1, 6, 10);
        setAir(world, 0, 6, 10);
        setBlockAndMetadata(world, 1, 7, 10, brickStairBlock, 5);
        setBlockAndMetadata(world, 0, 7, 10, brickStairBlock, 4);
        for (int i14 = -9; i14 <= -3; ++i14) {
            if (i14 % 3 == 0) {
                setBlockAndMetadata(world, i14, 1, 3, brickBlock, brickMeta);
                setBlockAndMetadata(world, i14, 2, 3, brickSlabBlock, brickSlabMeta);
                setBlockAndMetadata(world, i14, 1, 2, brickSlabBlock, brickSlabMeta);
                setGrassToDirt(world, i14, 0, 3);
                setGrassToDirt(world, i14, 0, 2);
                continue;
            }
            setBlockAndMetadata(world, i14, 1, 3, leafBlock, leafMeta);
        }
        setBlockAndMetadata(world, 0, 0, 0, floorBlock, floorMeta);
        setBlockAndMetadata(world, 1, 0, 0, floorBlock, floorMeta);
        setAir(world, 0, 1, 0);
        setAir(world, 1, 1, 0);
        placeWallBanner(world, -2, 4, 0, "DORWINION", 2);
        placeWallBanner(world, 3, 4, 0, "DORWINION", 2);
        setBlockAndMetadata(world, -1, 2, 1, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, 2, 2, 1, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, -4, 4, 7, plankBlock, plankMeta);
        setBlockAndMetadata(world, -4, 3, 7, LOTRLegacyBlocks.mod("chandelier"), 2);
        setBlockAndMetadata(world, -9, 3, 5, plankStairBlock, 7);
        setBlockAndMetadata(world, -9, 3, 9, plankStairBlock, 6);
        setBlockAndMetadata(world, -9, 3, 7, LOTRLegacyBlocks.mod("chandelier"), 2);
        setBlockAndMetadata(world, -3, 1, 5, LOTRLegacyBlocks.vanilla("furnace"), 3);
        setBlockAndMetadata(world, -4, 1, 5, plankStairBlock, 5);
        setBlockAndMetadata(world, -5, 1, 5, plankStairBlock, 4);
        setBlockAndMetadata(world, -6, 1, 5, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, -7, 1, 5, plankStairBlock, 5);
        setBlockAndMetadata(world, -8, 1, 5, plankStairBlock, 4);
        setBlockAndMetadata(world, -9, 1, 5, plankBlock, plankMeta);
        placeFlowerPot(world, -9, 2, 5, getRandomFlower(world, random));
        setBlockAndMetadata(world, -9, 1, 6, plankStairBlock, 7);
        setBlockAndMetadata(world, -9, 1, 7, LOTRLegacyBlocks.mod("dorwinionTable"), 0);
        setBlockAndMetadata(world, -9, 1, 8, plankStairBlock, 6);
        setBlockAndMetadata(world, -9, 1, 9, plankBlock, plankMeta);
        placeFlowerPot(world, -9, 2, 9, getRandomFlower(world, random));
        setBlockAndMetadata(world, -8, 1, 9, plankStairBlock, 4);
        setBlockAndMetadata(world, -7, 1, 9, plankStairBlock, 5);
        setBlockAndMetadata(world, -6, 1, 9, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, -5, 1, 9, plankStairBlock, 4);
        setBlockAndMetadata(world, -4, 1, 9, plankStairBlock, 5);
        setBlockAndMetadata(world, -3, 1, 9, LOTRLegacyBlocks.vanilla("cauldron"), 3);
        for (int j15 = 1; j15 <= 9; ++j15) {
            setBlockAndMetadata(world, 1, j15, 8, woodBeamBlock, woodBeamMeta);
        }
        setBlockAndMetadata(world, 1, 2, 7, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 2, 1, 8, plankStairBlock, 2);
        setBlockAndMetadata(world, 2, 1, 9, plankBlock, plankMeta);
        setBlockAndMetadata(world, 1, 2, 9, plankStairBlock, 0);
        setBlockAndMetadata(world, 1, 1, 9, plankStairBlock, 5);
        setBlockAndMetadata(world, 0, 2, 9, plankBlock, plankMeta);
        setBlockAndMetadata(world, 0, 3, 8, plankStairBlock, 3);
        setBlockAndMetadata(world, 0, 2, 8, plankStairBlock, 6);
        setBlockAndMetadata(world, 0, 3, 7, plankBlock, plankMeta);
        setBlockAndMetadata(world, 0, 2, 7, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, 0, 1, 7, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, 1, 4, 7, plankStairBlock, 1);
        setBlockAndMetadata(world, 0, 4, 6, plankStairBlock, 3);
        setBlockAndMetadata(world, 1, 4, 6, plankStairBlock, 3);
        setAir(world, 0, 4, 7);
        setAir(world, 0, 4, 8);
        setAir(world, 0, 4, 9);
        setAir(world, 1, 4, 9);
        setAir(world, 2, 4, 9);
        setBlockAndMetadata(world, -1, 5, 7, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, -1, 5, 8, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, -1, 5, 9, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, 2, 5, 8, fenceBlock, fenceMeta);
        int[] j15 = {5, 9};
        k1 = j15.length;
        for (beam = 0; beam < k1; ++beam) {
            int k14 = j15[beam];
            setBlockAndMetadata(world, -3, 5, k14, plankBlock, plankMeta);
            setBlockAndMetadata(world, -4, 5, k14, plankStairBlock, 5);
            setBlockAndMetadata(world, -5, 5, k14, plankStairBlock, 4);
            setBlockAndMetadata(world, -8, 5, k14, LOTRLegacyBlocks.vanilla("bed"), 3);
            setBlockAndMetadata(world, -9, 5, k14, LOTRLegacyBlocks.vanilla("bed"), 11);
        }
        placeChest(world, random, -6, 5, 5, 3, LOTRChestContents.DORWINION_HOUSE);
        placeChest(world, random, -6, 5, 9, 2, LOTRChestContents.DORWINION_HOUSE);
        placeMug(world, random, -4, 6, 5, 2, LOTRFoods.DORWINION_DRINK);
        placeMug(world, random, -4, 6, 9, 0, LOTRFoods.DORWINION_DRINK);
        setBlockAndMetadata(world, -9, 5, 6, plankStairBlock, 7);
        setBlockAndMetadata(world, -9, 5, 7, LOTRLegacyBlocks.mod("dorwinionTable"), 0);
        setBlockAndMetadata(world, -9, 5, 8, plankStairBlock, 6);
        placeBarrel(world, random, -9, 6, 7, 4, LOTRFoods.DORWINION_DRINK);
        placeMug(world, random, -9, 6, 6, 3, LOTRFoods.DORWINION_DRINK);
        placeMug(world, random, -9, 6, 8, 3, LOTRFoods.DORWINION_DRINK);
        spawnItemFrame(world, -10, 8, 7, 1, LOTRLegacyItems.vanillaStack("clock", 1, 0));
        setBlockAndMetadata(world, 0, 5, 2, plankStairBlock, 4);
        setBlockAndMetadata(world, 0, 5, 3, plankStairBlock, 4);
        setBlockAndMetadata(world, 1, 5, 2, plankStairBlock, 5);
        setBlockAndMetadata(world, 1, 5, 3, plankStairBlock, 5);
        for (i1 = 0; i1 <= 1; ++i1) {
            for (k1 = 2; k1 <= 3; ++k1) {
                placePlate(world, random, i1, 6, k1, plateBlock, LOTRFoods.DORWINION);
            }
        }
        setBlockAndMetadata(world, -1, 6, 1, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, 2, 6, 1, LOTRLegacyBlocks.vanilla("torch"), 3);
        for (i1 = -2; i1 <= 3; ++i1) {
            setBlockAndMetadata(world, i1, 8, -1, brickStairBlock, 6);
        }
        for (int k122 = -1; k122 <= 11; ++k122) {
            setBlockAndMetadata(world, 4, 8, k122, brickStairBlock, 4);
            if (Math.floorMod(k122, 2) != 1) {
                continue;
            }
            setBlockAndMetadata(world, 4, 9, k122, brickSlabBlock, brickSlabMeta);
        }
        for (i1 = -11; i1 <= 3; ++i1) {
            setBlockAndMetadata(world, i1, 8, 11, brickStairBlock, 7);
            if (i1 > -3 || Math.floorMod(i1, 2) != 1) {
                continue;
            }
            setBlockAndMetadata(world, i1, 9, 11, brickSlabBlock, brickSlabMeta);
        }
        for (k12 = 4; k12 <= 10; ++k12) {
            setBlockAndMetadata(world, -11, 8, k12, brickStairBlock, 5);
        }
        for (i1 = -11; i1 <= -3; ++i1) {
            setBlockAndMetadata(world, i1, 8, 3, brickStairBlock, 6);
            if (Math.floorMod(i1, 2) != 1) {
                continue;
            }
            setBlockAndMetadata(world, i1, 9, 3, brickSlabBlock, brickSlabMeta);
        }
        for (k12 = -1; k12 <= 2; ++k12) {
            setBlockAndMetadata(world, -3, 8, k12, brickStairBlock, 5);
            if (Math.floorMod(k12, 2) != 1) {
                continue;
            }
            setBlockAndMetadata(world, -3, 9, k12, brickSlabBlock, brickSlabMeta);
        }
        for (i1 = -9; i1 <= -1; ++i1) {
            setBlockAndMetadata(world, i1, 9, 5, plankStairBlock, 7);
            setBlockAndMetadata(world, i1, 10, 6, plankStairBlock, 7);
            setBlockAndMetadata(world, i1, 10, 7, plankSlabBlock, plankSlabMeta | 8);
            setBlockAndMetadata(world, i1, 10, 8, plankStairBlock, 6);
            setBlockAndMetadata(world, i1, 9, 9, plankStairBlock, 6);
        }
        for (k12 = 1; k12 <= 9; ++k12) {
            if (k12 <= 5) {
                setBlockAndMetadata(world, -1, 9, k12, plankStairBlock, 4);
                setBlockAndMetadata(world, 0, 10, k12, plankStairBlock, 4);
            }
            setBlockAndMetadata(world, 1, 10, k12, plankStairBlock, 5);
            setBlockAndMetadata(world, 2, 9, k12, plankStairBlock, 5);
        }
        setBlockAndMetadata(world, -10, 9, 5, plankBlock, plankMeta);
        setBlockAndMetadata(world, -10, 9, 6, plankStairBlock, 7);
        setBlockAndMetadata(world, -10, 10, 6, plankBlock, plankMeta);
        setBlockAndMetadata(world, -10, 10, 7, plankBlock, plankMeta);
        setBlockAndMetadata(world, -10, 10, 8, plankBlock, plankMeta);
        setBlockAndMetadata(world, -10, 9, 8, plankStairBlock, 6);
        setBlockAndMetadata(world, -10, 9, 9, plankBlock, plankMeta);
        for (int i15 : new int[]{-8, -4, 0}) {
            setBlockAndMetadata(world, i15, 10, 7, plankBlock, plankMeta);
            setBlockAndMetadata(world, i15, 9, 7, LOTRLegacyBlocks.mod("chandelier"), 2);
        }
        setBlockAndMetadata(world, 0, 10, 6, plankStairBlock, 4);
        setBlockAndMetadata(world, -1, 9, 9, plankStairBlock, 4);
        setBlockAndMetadata(world, 0, 10, 8, plankStairBlock, 4);
        setBlockAndMetadata(world, 0, 10, 9, plankStairBlock, 4);
        setBlockAndMetadata(world, 1, 10, 8, woodBeamBlock, woodBeamMeta);
        setBlockAndMetadata(world, -1, 9, 10, plankBlock, plankMeta);
        setBlockAndMetadata(world, 0, 9, 10, plankStairBlock, 4);
        setBlockAndMetadata(world, 0, 10, 10, plankBlock, plankMeta);
        setBlockAndMetadata(world, 1, 10, 10, plankBlock, plankMeta);
        setBlockAndMetadata(world, 1, 9, 10, plankStairBlock, 5);
        setBlockAndMetadata(world, 2, 9, 10, plankBlock, plankMeta);
        setBlockAndMetadata(world, -1, 9, 0, plankBlock, plankMeta);
        setBlockAndMetadata(world, 0, 9, 0, plankStairBlock, 4);
        setBlockAndMetadata(world, 0, 10, 0, plankBlock, plankMeta);
        setBlockAndMetadata(world, 1, 10, 0, plankBlock, plankMeta);
        setBlockAndMetadata(world, 1, 9, 0, plankStairBlock, 5);
        setBlockAndMetadata(world, 2, 9, 0, plankBlock, plankMeta);
        for (int i16 = -11; i16 <= 0; ++i16) {
            if (i16 <= -2) {
                setBlockAndMetadata(world, i16, 9, 4, clayStairBlock, 2);
                setBlockAndMetadata(world, i16, 9, 10, clayStairBlock, 3);
            }
            if (i16 <= -1) {
                setBlockAndMetadata(world, i16, 10, 5, clayStairBlock, 2);
                setBlockAndMetadata(world, i16, 10, 9, clayStairBlock, 3);
            }
            setBlockAndMetadata(world, i16, 11, 6, clayStairBlock, 2);
            setBlockAndMetadata(world, i16, 11, 7, clayBlock, clayMeta);
            setBlockAndMetadata(world, i16, 11, 8, clayStairBlock, 3);
        }
        for (int k15 = -1; k15 <= 11; ++k15) {
            if (k15 <= 3 || k15 == 11) {
                setBlockAndMetadata(world, -2, 9, k15, clayStairBlock, 1);
            }
            if (k15 <= 4 || k15 >= 10) {
                setBlockAndMetadata(world, -1, 10, k15, clayStairBlock, 1);
            }
            if (k15 <= 5 || k15 >= 9) {
                setBlockAndMetadata(world, 0, 11, k15, clayStairBlock, 1);
            }
            setBlockAndMetadata(world, 1, 11, k15, clayStairBlock, 0);
            setBlockAndMetadata(world, 2, 10, k15, clayStairBlock, 0);
            setBlockAndMetadata(world, 3, 9, k15, clayStairBlock, 0);
        }
        setBlockAndMetadata(world, -11, 9, 5, clayStairBlock, 7);
        setBlockAndMetadata(world, -11, 10, 6, clayStairBlock, 7);
        setBlockAndMetadata(world, -11, 10, 8, clayStairBlock, 6);
        setBlockAndMetadata(world, -11, 9, 9, clayStairBlock, 6);
        setBlockAndMetadata(world, -1, 9, 11, clayStairBlock, 4);
        setBlockAndMetadata(world, 0, 10, 11, clayStairBlock, 4);
        setBlockAndMetadata(world, 1, 10, 11, clayStairBlock, 5);
        setBlockAndMetadata(world, 2, 9, 11, clayStairBlock, 5);
        setBlockAndMetadata(world, -1, 9, -1, clayStairBlock, 4);
        setBlockAndMetadata(world, 0, 10, -1, clayStairBlock, 4);
        setBlockAndMetadata(world, 1, 10, -1, clayStairBlock, 5);
        setBlockAndMetadata(world, 2, 9, -1, clayStairBlock, 5);
        String maleName = LOTRNames.getDorwinionName(random, true);
        String femaleName = LOTRNames.getDorwinionName(random, false);
        LOTRDorwinionManEntity dorwinionMale = create(LOTREntities.DORWINION_MAN, world);
        dorwinionMale.familyInfo.setName(maleName);
        dorwinionMale.familyInfo.setMale(true);
        spawnNPCAndSetHome(dorwinionMale, world, 0, 1, 6, 16);
        LOTRDorwinionManEntity dorwinionFemale = create(LOTREntities.DORWINION_MAN, world);
        dorwinionFemale.familyInfo.setName(femaleName);
        dorwinionFemale.familyInfo.setMale(false);
        spawnNPCAndSetHome(dorwinionFemale, world, 0, 1, 6, 16);
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        int randomWood = random.nextInt(3);
        switch (randomWood) {
            case 0:
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
                woodBeamMeta = 0;
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 0;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 0;
                plankStairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 0;
                doorBlock = LOTRLegacyBlocks.vanilla("wooden_door");
                break;
            case 1:
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam6");
                woodBeamMeta = 2;
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 10;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle4");
                plankSlabMeta = 2;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsCypress");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 10;
                doorBlock = LOTRLegacyBlocks.mod("doorCypress");
                break;
            case 2:
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam6");
                woodBeamMeta = 3;
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 11;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle4");
                plankSlabMeta = 3;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsOlive");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 11;
                doorBlock = LOTRLegacyBlocks.mod("doorOlive");
                break;
            default:
                break;
        }
        int randomFloor = random.nextInt(3);
        switch (randomFloor) {
            case 0:
                floorBlock = LOTRLegacyBlocks.vanilla("cobblestone");
                floorMeta = 0;
                break;
            case 1:
                floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                floorMeta = 8;
                break;
            case 2:
                floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                floorMeta = 10;
                break;
            default:
                break;
        }
        int randomWall = random.nextInt(3);
        switch (randomWall) {
            case 0:
                wallBlock = LOTRLegacyBlocks.vanilla("stonebrick");
                wallMeta = 0;
                break;
            case 1:
                wallBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                wallMeta = 0;
                break;
            case 2:
                wallBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                wallMeta = 4;
                break;
            default:
                break;
        }
        brickBlock = LOTRLegacyBlocks.mod("brick5");
        brickMeta = 2;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle9");
        brickSlabMeta = 7;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsDorwinionBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall3");
        brickWallMeta = 10;
        pillarBlock = LOTRLegacyBlocks.mod("pillar2");
        pillarMeta = 6;
        int randomClay = random.nextInt(5);
        switch (randomClay) {
            case 0:
                clayBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                clayMeta = 10;
                claySlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                claySlabMeta = 2;
                clayStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedPurple");
                break;
            case 1:
                clayBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                clayMeta = 2;
                claySlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle");
                claySlabMeta = 2;
                clayStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedMagenta");
                break;
            case 2:
                clayBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                clayMeta = 14;
                claySlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                claySlabMeta = 6;
                clayStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedRed");
                break;
            case 3:
                clayBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                clayMeta = 13;
                claySlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                claySlabMeta = 5;
                clayStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedGreen");
                break;
            case 4:
                clayBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                clayMeta = 12;
                claySlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                claySlabMeta = 4;
                clayStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedBrown");
                break;
            default:
                break;
        }
        leafBlock = LOTRLegacyBlocks.mod("leaves6");
        leafMeta = 6;
        plateBlock = random.nextBoolean() ? LOTRLegacyBlocks.mod("ceramicPlateBlock") : LOTRLegacyBlocks.mod("plateBlock");
    }
}
