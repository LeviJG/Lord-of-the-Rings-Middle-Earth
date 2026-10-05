package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRDorwinionElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRDorwinionElfHouseStructure extends LOTRDorwinionHouseStructure {
    public LegacyBlock grapevineBlock;
    public Item wineItem;
    public Item grapeItem;

    public LOTRDorwinionElfHouseStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int j1;
        int k2;
        int k12;
        int k13;
        int i1;
        int i12;
        int i13;
        int j12;
        int i14;
        setOriginAndRotation(world, i, j, k, rotation, 1);
        setupRandomBlocks(random);
        if (restrictions) {
            for (int i15 = -4; i15 <= 8; ++i15) {
                for (k13 = -1; k13 <= 20; ++k13) {
                    j1 = getTopBlock(world, i15, k13) - 1;
                    BlockState block = getBlockState(world, i15, j1, k13);
                    if (LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        boolean generateBackGate = true;
        for (i12 = 1; i12 <= 3; ++i12) {
            k12 = 20;
            j12 = getTopBlock(world, i12, k12) - 1;
            if (j12 == 0) {
                continue;
            }
            generateBackGate = false;
        }
        for (i12 = -4; i12 <= 8; ++i12) {
            for (k12 = 0; k12 <= 20; ++k12) {
                for (j12 = 1; j12 <= 6; ++j12) {
                    setAir(world, i12, j12, k12);
                }
                setBlockAndMetadata(world, i12, 0, k12, LOTRLegacyBlocks.vanilla("grass"), 0);
                j12 = -1;
                while (!isOpaque(world, i12, j12, k12) && getY(j12) >= world.getMinY()) {
                    setBlockAndMetadata(world, i12, j12, k12, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i12, j12 - 1, k12);
                    --j12;
                }
            }
        }
        for (i12 = -3; i12 <= 7; ++i12) {
            for (k12 = 0; k12 <= 8; ++k12) {
                if (i12 >= 3 && k12 <= 2) {
                    if (random.nextInt(3) != 0) {
                        continue;
                    }
                    int j13 = 1;
                    plantFlower(world, random, i12, j13, k12);
                    continue;
                }
                if (k12 == 0 && (i12 == -3 || i12 == 2) || k12 == 3 && (i12 == 2 || i12 == 7) || k12 == 8 && (i12 == -3 || i12 == 7)) {
                    for (j12 = 0; j12 <= 4; ++j12) {
                        setBlockAndMetadata(world, i12, j12, k12, woodBeamBlock, woodBeamMeta);
                    }
                    continue;
                }
                if (i12 == -3 || i12 == 2 && k12 <= 3 || i12 == 7 || k12 == 0 || k12 == 3 && i12 >= 2 || k12 == 8) {
                    for (j12 = 0; j12 <= 1; ++j12) {
                        setBlockAndMetadata(world, i12, j12, k12, wallBlock, wallMeta);
                    }
                    for (j12 = 2; j12 <= 4; ++j12) {
                        setBlockAndMetadata(world, i12, j12, k12, brickBlock, brickMeta);
                    }
                    continue;
                }
                setBlockAndMetadata(world, i12, 0, k12, floorBlock, floorMeta);
            }
        }
        for (k13 = 1; k13 <= 7; ++k13) {
            k2 = Math.floorMod(k13, 3);
            if (k2 == 1) {
                setBlockAndMetadata(world, -4, 1, k13, brickStairBlock, 1);
                setGrassToDirt(world, -4, 0, k13);
                continue;
            }
            if (k2 == 2) {
                setAir(world, -3, 2, k13);
                setBlockAndMetadata(world, -3, 3, k13, brickStairBlock, 7);
                setBlockAndMetadata(world, -4, 1, k13, leafBlock, leafMeta);
                continue;
            }
            if (k2 != 0) {
                continue;
            }
            setAir(world, -3, 2, k13);
            setBlockAndMetadata(world, -3, 3, k13, brickStairBlock, 6);
            setBlockAndMetadata(world, -4, 1, k13, leafBlock, leafMeta);
        }
        for (int k14 : new int[]{0, 8}) {
            setAir(world, -1, 2, k14);
            setAir(world, 0, 2, k14);
            setBlockAndMetadata(world, -1, 3, k14, brickStairBlock, 4);
            setBlockAndMetadata(world, 0, 3, k14, brickStairBlock, 5);
        }
        for (int k14 : new int[]{3, 8}) {
            setAir(world, 4, 2, k14);
            setAir(world, 5, 2, k14);
            setBlockAndMetadata(world, 4, 3, k14, brickStairBlock, 4);
            setBlockAndMetadata(world, 5, 3, k14, brickStairBlock, 5);
        }
        setBlockAndMetadata(world, 3, 1, 2, brickStairBlock, 2);
        setGrassToDirt(world, 3, 0, 2);
        setBlockAndMetadata(world, 4, 1, 2, leafBlock, leafMeta);
        setBlockAndMetadata(world, 5, 1, 2, leafBlock, leafMeta);
        setBlockAndMetadata(world, 6, 1, 2, brickStairBlock, 2);
        setGrassToDirt(world, 6, 0, 2);
        setBlockAndMetadata(world, 8, 1, 4, brickStairBlock, 0);
        setGrassToDirt(world, 8, 0, 4);
        setBlockAndMetadata(world, 8, 1, 5, leafBlock, leafMeta);
        setBlockAndMetadata(world, 8, 1, 6, leafBlock, leafMeta);
        setBlockAndMetadata(world, 8, 1, 7, brickStairBlock, 0);
        setGrassToDirt(world, 8, 0, 7);
        setAir(world, 7, 2, 5);
        setAir(world, 7, 2, 6);
        setBlockAndMetadata(world, 7, 3, 5, brickStairBlock, 7);
        setBlockAndMetadata(world, 7, 3, 6, brickStairBlock, 6);
        for (int i16 : new int[]{-1, 0}) {
            setBlockAndMetadata(world, i16, 0, 0, floorBlock, floorMeta);
            setAir(world, i16, 1, 0);
        }
        for (i1 = -3; i1 <= 2; ++i1) {
            setBlockAndMetadata(world, i1, 4, -1, brickStairBlock, 6);
        }
        for (k1 = -1; k1 <= 2; ++k1) {
            setBlockAndMetadata(world, 3, 4, k1, brickStairBlock, 4);
            if (Math.floorMod(k1, 2) != 1) {
                continue;
            }
            setBlockAndMetadata(world, 3, 5, k1, brickSlabBlock, brickSlabMeta);
        }
        for (i1 = 4; i1 <= 8; ++i1) {
            setBlockAndMetadata(world, i1, 4, 2, brickStairBlock, 6);
            if (Math.floorMod(i1, 2) != 0) {
                continue;
            }
            setBlockAndMetadata(world, i1, 5, 2, brickSlabBlock, brickSlabMeta);
        }
        for (k1 = 3; k1 <= 8; ++k1) {
            setBlockAndMetadata(world, 8, 4, k1, brickStairBlock, 4);
        }
        for (i1 = 8; i1 >= -4; --i1) {
            setBlockAndMetadata(world, i1, 4, 9, brickStairBlock, 7);
            if (Math.floorMod(i1, 2) != 0) {
                continue;
            }
            setBlockAndMetadata(world, i1, 5, 9, brickSlabBlock, brickSlabMeta);
        }
        for (k1 = 8; k1 >= -1; --k1) {
            setBlockAndMetadata(world, -4, 4, k1, brickStairBlock, 5);
            if (Math.floorMod(k1, 2) != 1) {
                continue;
            }
            setBlockAndMetadata(world, -4, 5, k1, brickSlabBlock, brickSlabMeta);
        }
        for (k1 = 1; k1 <= 7; ++k1) {
            setBlockAndMetadata(world, -2, 4, k1, plankSlabBlock, plankSlabMeta | 8);
            if (k1 <= 3) {
                setBlockAndMetadata(world, 1, 4, k1, plankSlabBlock, plankSlabMeta | 8);
            }
            if (k1 < 4) {
                continue;
            }
            setBlockAndMetadata(world, 2, 4, k1, plankSlabBlock, plankSlabMeta | 8);
        }
        for (i1 = -2; i1 <= 6; ++i1) {
            setBlockAndMetadata(world, i1, 4, 7, plankSlabBlock, plankSlabMeta | 8);
            if (i1 <= 1) {
                setBlockAndMetadata(world, i1, 4, 3, plankSlabBlock, plankSlabMeta | 8);
            }
            if (i1 < 2) {
                continue;
            }
            setBlockAndMetadata(world, i1, 4, 4, plankSlabBlock, plankSlabMeta | 8);
        }
        for (k1 = 1; k1 <= 6; ++k1) {
            setBlockAndMetadata(world, -2, 5, k1, plankStairBlock, 4);
            if (k1 <= 5) {
                setBlockAndMetadata(world, -1, 6, k1, plankStairBlock, 4);
            }
            if (k1 <= 4) {
                setBlockAndMetadata(world, 0, 6, k1, plankStairBlock, 5);
            }
            if (k1 > 3) {
                continue;
            }
            setBlockAndMetadata(world, 1, 5, k1, plankStairBlock, 5);
        }
        for (i1 = -2; i1 <= 6; ++i1) {
            setBlockAndMetadata(world, i1, 5, 7, plankStairBlock, 6);
            if (i1 >= -1) {
                setBlockAndMetadata(world, i1, 6, 6, plankStairBlock, 6);
            }
            if (i1 >= 0) {
                setBlockAndMetadata(world, i1, 6, 5, plankStairBlock, 7);
            }
            if (i1 < 1) {
                continue;
            }
            setBlockAndMetadata(world, i1, 5, 4, plankStairBlock, 7);
        }
        setBlockAndMetadata(world, -2, 5, 0, plankBlock, plankMeta);
        setBlockAndMetadata(world, -1, 5, 0, plankStairBlock, 4);
        setBlockAndMetadata(world, -1, 6, 0, plankBlock, plankMeta);
        setBlockAndMetadata(world, 0, 6, 0, plankBlock, plankMeta);
        setBlockAndMetadata(world, 0, 5, 0, plankStairBlock, 5);
        setBlockAndMetadata(world, 1, 5, 0, plankBlock, plankMeta);
        setBlockAndMetadata(world, 7, 5, 4, plankBlock, plankMeta);
        setBlockAndMetadata(world, 7, 5, 5, plankStairBlock, 7);
        setBlockAndMetadata(world, 7, 6, 5, plankBlock, plankMeta);
        setBlockAndMetadata(world, 7, 6, 6, plankBlock, plankMeta);
        setBlockAndMetadata(world, 7, 5, 6, plankStairBlock, 6);
        setBlockAndMetadata(world, 7, 5, 7, plankBlock, plankMeta);
        for (k1 = -1; k1 <= 7; ++k1) {
            setBlockAndMetadata(world, -3, 5, k1, clayStairBlock, 1);
            if (k1 <= 6) {
                setBlockAndMetadata(world, -2, 6, k1, clayStairBlock, 1);
            }
            if (k1 <= 5) {
                setBlockAndMetadata(world, -1, 7, k1, clayStairBlock, 1);
            }
            if (k1 <= 4) {
                setBlockAndMetadata(world, 0, 7, k1, clayStairBlock, 0);
            }
            if (k1 <= 3) {
                setBlockAndMetadata(world, 1, 6, k1, clayStairBlock, 0);
            }
            if (k1 > 2) {
                continue;
            }
            setBlockAndMetadata(world, 2, 5, k1, clayStairBlock, 0);
        }
        for (i1 = -3; i1 <= 8; ++i1) {
            setBlockAndMetadata(world, i1, 5, 8, clayStairBlock, 3);
            if (i1 >= -2) {
                setBlockAndMetadata(world, i1, 6, 7, clayStairBlock, 3);
            }
            if (i1 >= -1) {
                setBlockAndMetadata(world, i1, 7, 6, clayStairBlock, 3);
            }
            if (i1 >= 0) {
                setBlockAndMetadata(world, i1, 7, 5, clayStairBlock, 2);
            }
            if (i1 >= 1) {
                setBlockAndMetadata(world, i1, 6, 4, clayStairBlock, 2);
            }
            if (i1 < 2) {
                continue;
            }
            setBlockAndMetadata(world, i1, 5, 3, clayStairBlock, 2);
        }
        setBlockAndMetadata(world, -2, 5, -1, clayStairBlock, 4);
        setBlockAndMetadata(world, -1, 6, -1, clayStairBlock, 4);
        setBlockAndMetadata(world, 0, 6, -1, clayStairBlock, 5);
        setBlockAndMetadata(world, 1, 5, -1, clayStairBlock, 5);
        setBlockAndMetadata(world, 8, 5, 4, clayStairBlock, 7);
        setBlockAndMetadata(world, 8, 6, 5, clayStairBlock, 7);
        setBlockAndMetadata(world, 8, 6, 6, clayStairBlock, 6);
        setBlockAndMetadata(world, 8, 5, 7, clayStairBlock, 6);
        setBlockAndMetadata(world, -2, 3, 1, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, -2, 3, 4, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, -2, 3, 7, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 6, 3, 7, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 6, 3, 4, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, 1, 3, 1, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, -2, 1, 4, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        placeChest(world, random, -2, 1, 5, 5, LOTRChestContents.DORWINION_HOUSE);
        placeChest(world, random, -2, 1, 6, 5, LOTRChestContents.DORWINION_HOUSE);
        setBlockAndMetadata(world, -2, 1, 7, LOTRLegacyBlocks.mod("dorwinionTable"), 0);
        setBlockAndMetadata(world, -1, 1, 6, LOTRLegacyBlocks.vanilla("bed"), 0);
        setBlockAndMetadata(world, -1, 1, 7, LOTRLegacyBlocks.vanilla("bed"), 8);
        setBlockAndMetadata(world, 2, 1, 4, LOTRLegacyBlocks.vanilla("furnace"), 3);
        setBlockAndMetadata(world, 3, 1, 4, LOTRLegacyBlocks.vanilla("cauldron"), 3);
        setBlockAndMetadata(world, 4, 1, 4, plankStairBlock, 4);
        setBlockAndMetadata(world, 5, 1, 4, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, 6, 1, 4, plankBlock, plankMeta);
        setBlockAndMetadata(world, 6, 1, 5, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, 6, 1, 6, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, 6, 1, 7, plankBlock, plankMeta);
        setBlockAndMetadata(world, 5, 1, 7, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, 4, 1, 7, plankStairBlock, 4);
        int[] i17 = {4, 7};
        k2 = i17.length;
        for (j12 = 0; j12 < k2; ++j12) {
            int k14;
            k14 = i17[j12];
            for (int i18 = 4; i18 <= 5; ++i18) {
                placePlate(world, random, i18, 2, k14, plateBlock, LOTRFoods.DORWINION);
            }
            placeBarrel(world, random, 6, 2, k14, 5, new ItemStack(wineItem));
        }
        placeMug(world, random, 6, 2, 5, 1, new ItemStack(wineItem), LOTRFoods.DORWINION_DRINK);
        placeMug(world, random, 6, 2, 6, 1, new ItemStack(wineItem), LOTRFoods.DORWINION_DRINK);
        setBlockAndMetadata(world, 2, 0, 8, floorBlock, floorMeta);
        setBlockAndMetadata(world, 2, 1, 8, doorBlock, 3);
        setBlockAndMetadata(world, 2, 2, 8, doorBlock, 8);
        spawnItemFrame(world, 2, 3, 8, 2, new ItemStack(grapeItem));
        setBlockAndMetadata(world, 2, 3, 9, LOTRLegacyBlocks.vanilla("torch"), 3);
        for (i14 = -3; i14 <= 7; ++i14) {
            for (k12 = 9; k12 <= 19; ++k12) {
                if (i14 == -3 || i14 == 7 || k12 == 19) {
                    setGrassToDirt(world, i14, 0, k12);
                    setBlockAndMetadata(world, i14, 1, k12, wallBlock, wallMeta);
                    setBlockAndMetadata(world, i14, 2, k12, brickBlock, brickMeta);
                    if (Math.floorMod(i14 + k12, 2) != 0) {
                        continue;
                    }
                    setBlockAndMetadata(world, i14, 3, k12, brickSlabBlock, brickSlabMeta);
                    continue;
                }
                setBlockAndMetadata(world, i14, 0, k12, LOTRLegacyBlocks.mod("dirtPath"), 0);
                if (Math.floorMod(i14, 2) != 1) {
                    continue;
                }
                if (k12 == 14) {
                    setBlockAndMetadata(world, i14, 0, 14, LOTRLegacyBlocks.vanilla("water"), 0);
                    setBlockAndMetadata(world, i14, 1, 14, fenceBlock, fenceMeta);
                    setBlockAndMetadata(world, i14, 2, 14, LOTRLegacyBlocks.vanilla("torch"), 5);
                    continue;
                }
                if (k12 < 11 || k12 > 17) {
                    continue;
                }
                setBlockAndMetadata(world, i14, 0, k12, LOTRLegacyBlocks.vanilla("farmland"), 7);
                setBlockAndMetadata(world, i14, 1, k12, grapevineBlock, 7);
                setBlockAndMetadata(world, i14, 2, k12, grapevineBlock, 7);
            }
        }
        for (i14 = 0; i14 <= 4; ++i14) {
            setBlockAndMetadata(world, i14, 3, 19, brickBlock, brickMeta);
        }
        for (i14 = 1; i14 <= 3; ++i14) {
            setBlockAndMetadata(world, i14, 4, 19, brickBlock, brickMeta);
        }
        setBlockAndMetadata(world, 0, 4, 19, brickStairBlock, 1);
        setBlockAndMetadata(world, 4, 4, 19, brickStairBlock, 0);
        setBlockAndMetadata(world, 1, 5, 19, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, 3, 5, 19, brickSlabBlock, brickSlabMeta);
        for (int i16 : new int[]{-3, 4}) {
            setGrassToDirt(world, i16, 0, 20);
            setBlockAndMetadata(world, i16, 1, 20, brickStairBlock, 3);
            setBlockAndMetadata(world, i16 + 1, 1, 20, leafBlock, leafMeta);
            setBlockAndMetadata(world, i16 + 2, 1, 20, leafBlock, leafMeta);
            setGrassToDirt(world, i16 + 3, 0, 20);
            setBlockAndMetadata(world, i16 + 3, 1, 20, brickStairBlock, 3);
        }
        if (generateBackGate) {
            for (i13 = 1; i13 <= 3; ++i13) {
                setBlockAndMetadata(world, i13, 0, 19, LOTRLegacyBlocks.mod("dirtPath"), 0);
                for (j1 = 1; j1 <= 3; ++j1) {
                    setBlockAndMetadata(world, i13, j1, 19, LOTRLegacyBlocks.mod("gateWooden"), 2);
                }
            }
        } else {
            for (i13 = 1; i13 <= 3; ++i13) {
                setBlockAndMetadata(world, i13, 1, 20, leafBlock, leafMeta);
            }
        }
        LOTRDorwinionElfEntity dorwinionElf = create(LOTREntities.DORWINION_ELF, world);
        spawnNPCAndSetHome(dorwinionElf, world, 0, 1, 5, 16);
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        if (random.nextBoolean()) {
            grapevineBlock = LOTRLegacyBlocks.mod("grapevineRed");
            wineItem = LOTRLegacyItems.mod("mugRedWine", 0);
            grapeItem = LOTRLegacyItems.mod("grapeRed", 0);
        } else {
            grapevineBlock = LOTRLegacyBlocks.mod("grapevineWhite");
            wineItem = LOTRLegacyItems.mod("mugWhiteWine", 0);
            grapeItem = LOTRLegacyItems.mod("grapeWhite", 0);
        }
    }
}
