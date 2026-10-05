package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRWoodElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature.LOTRMirkOakStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWoodElfHouseStructure extends LOTRStructureBase2 {
    public LOTRStructureBase treeGen = new LOTRMirkOakStructure(true, 3, 4, 0, false).setGreenOak().disableRestrictions().disableRoots();
    public LegacyBlock plank1Block;
    public int plank1Meta;
    public LegacyBlock wood1Block;
    public int wood1Meta;
    public LegacyBlock fence1Block;
    public int fence1Meta;
    public LegacyBlock doorBlock;
    public LegacyBlock plank2Block;
    public int plank2Meta;
    public LegacyBlock fence2Block;
    public int fence2Meta;
    public LegacyBlock stair2Block;
    public LegacyBlock barsBlock;
    public LegacyBlock plateBlock;

    public LOTRWoodElfHouseStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int k2;
        int i1;
        int k12;
        int k13;
        int i12;
        int j1;
        int i13;
        int i2;
        setOriginAndRotation(world, i, j, k, rotation, 6);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (i13 = -6; i13 <= 6; ++i13) {
                for (k12 = -6; k12 <= 6; ++k12) {
                    j1 = getTopBlock(world, i13, k12);
                    BlockState block = getBlockState(world, i13, j1 - 1, k12);
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 3) {
                        continue;
                    }
                    return false;
                }
            }
        }
        if (random.nextInt(2) == 0) {
            plank1Block = LOTRLegacyBlocks.mod("planks2");
            plank1Meta = 13;
            wood1Block = LOTRLegacyBlocks.mod("wood7");
            wood1Meta = 1;
            fence1Block = LOTRLegacyBlocks.mod("fence2");
            fence1Meta = 13;
            doorBlock = LOTRLegacyBlocks.mod("doorGreenOak");
        } else {
            plank1Block = LOTRLegacyBlocks.mod("planks");
            plank1Meta = 9;
            wood1Block = LOTRLegacyBlocks.mod("wood2");
            wood1Meta = 1;
            fence1Block = LOTRLegacyBlocks.mod("fence");
            fence1Meta = 9;
            doorBlock = LOTRLegacyBlocks.mod("doorBeech");
        }
        int randomWood2 = random.nextInt(2);
        if (randomWood2 == 0) {
            plank2Block = LOTRLegacyBlocks.mod("planks2");
            plank2Meta = 13;
            fence2Block = LOTRLegacyBlocks.mod("fence2");
            fence2Meta = 13;
            stair2Block = LOTRLegacyBlocks.mod("stairsGreenOak");
        } else {
            plank2Block = LOTRLegacyBlocks.mod("planks");
            plank2Meta = 9;
            fence2Block = LOTRLegacyBlocks.mod("fence");
            fence2Meta = 9;
            stair2Block = LOTRLegacyBlocks.mod("stairsBeech");
        }
        barsBlock = LOTRLegacyBlocks.mod("woodElfWoodBars");
        plateBlock = LOTRLegacyBlocks.mod("woodPlateBlock");
        for (i13 = -6; i13 <= 6; ++i13) {
            for (k12 = -6; k12 <= 6; ++k12) {
                for (j1 = 1; j1 <= 7; ++j1) {
                    setAir(world, i13, j1, k12);
                }
                for (j1 = 0; (j1 == 0 || !isOpaque(world, i13, j1, k12)) && getY(j1) >= 0; --j1) {
                    if (getBlockState(world, i13, j1 + 1, k12).isSolidRender()) {
                        setBlockAndMetadata(world, i13, j1, k12, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    } else {
                        setBlockAndMetadata(world, i13, j1, k12, LOTRLegacyBlocks.vanilla("grass"), 0);
                    }
                    setGrassToDirt(world, i13, j1 - 1, k12);
                }
            }
        }
        for (i13 = -4; i13 <= 4; ++i13) {
            for (k12 = -4; k12 <= 4; ++k12) {
                setBlockAndMetadata(world, i13, 0, k12, LOTRLegacyBlocks.mod("brick3"), 5);
            }
        }
        for (i13 = -4; i13 <= 4; ++i13) {
            for (k12 = -4; k12 <= 4; ++k12) {
                i2 = Math.abs(i13);
                k2 = Math.abs(k12);
                for (int j12 = 1; j12 <= 3; ++j12) {
                    if (i2 == 4 && k2 == 4) {
                        setBlockAndMetadata(world, i13, j12, k12, wood1Block, wood1Meta);
                        continue;
                    }
                    if (i2 == 4 || k2 == 4) {
                        setBlockAndMetadata(world, i13, j12, k12, plank1Block, plank1Meta);
                        continue;
                    }
                    setAir(world, i13, j12, k12);
                }
                if (i2 != 4 && k2 != 4) {
                    continue;
                }
                setBlockAndMetadata(world, i13, 4, k12, plank2Block, plank2Meta);
            }
        }
        for (i13 = -5; i13 <= 5; ++i13) {
            setBlockAndMetadata(world, i13, 4, -5, stair2Block, 6);
            setBlockAndMetadata(world, i13, 4, 5, stair2Block, 7);
            for (int k14 : new int[]{-5, 5}) {
                if (getBlockState(world, i13, 1, k14).isSolidRender()) {
                    continue;
                }
                setBlockAndMetadata(world, i13, 1, k14, LOTRLegacyBlocks.mod("leaves7"), 5);
            }
        }
        for (k1 = -4; k1 <= 4; ++k1) {
            setBlockAndMetadata(world, -5, 4, k1, stair2Block, 5);
            setBlockAndMetadata(world, 5, 4, k1, stair2Block, 4);
            for (int i14 : new int[]{-5, 5}) {
                if (getBlockState(world, i14, 1, k1).isSolidRender()) {
                    continue;
                }
                setBlockAndMetadata(world, i14, 1, k1, LOTRLegacyBlocks.mod("leaves7"), 5);
            }
        }
        for (i13 = -3; i13 <= 3; ++i13) {
            setBlockAndMetadata(world, i13, 4, -3, stair2Block, 7);
            setBlockAndMetadata(world, i13, 4, 3, stair2Block, 6);
        }
        for (k1 = -2; k1 <= 2; ++k1) {
            setBlockAndMetadata(world, -3, 4, k1, stair2Block, 4);
            setBlockAndMetadata(world, 3, 4, k1, stair2Block, 5);
        }
        for (i13 = -5; i13 <= 5; ++i13) {
            for (int k15 = -5; k15 <= 5; ++k15) {
                i2 = Math.abs(i13);
                k2 = Math.abs(k15);
                if (i2 == 5 || k2 == 5) {
                    setBlockAndMetadata(world, i13, 5, k15, fence1Block, fence1Meta);
                }
                if (i2 == 5 && k2 == 5) {
                    setBlockAndMetadata(world, i13, 6, k15, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
                }
                if ((i2 != 5 || k2 != 0) && (k2 != 5 || i2 != 0)) {
                    continue;
                }
                setBlockAndMetadata(world, i13, 6, k15, fence1Block, fence1Meta);
                setBlockAndMetadata(world, i13, 7, k15, LOTRLegacyBlocks.mod("woodElvenTorch"), 5);
            }
        }
        setBlockAndMetadata(world, -3, 2, -1, LOTRLegacyBlocks.mod("woodElvenTorch"), 2);
        setBlockAndMetadata(world, -3, 2, 1, LOTRLegacyBlocks.mod("woodElvenTorch"), 2);
        setBlockAndMetadata(world, 3, 2, -1, LOTRLegacyBlocks.mod("woodElvenTorch"), 1);
        setBlockAndMetadata(world, 3, 2, 1, LOTRLegacyBlocks.mod("woodElvenTorch"), 1);
        setBlockAndMetadata(world, -1, 2, -3, LOTRLegacyBlocks.mod("woodElvenTorch"), 3);
        setBlockAndMetadata(world, 1, 2, -3, LOTRLegacyBlocks.mod("woodElvenTorch"), 3);
        setBlockAndMetadata(world, -1, 2, 3, LOTRLegacyBlocks.mod("woodElvenTorch"), 4);
        setBlockAndMetadata(world, 1, 2, 3, LOTRLegacyBlocks.mod("woodElvenTorch"), 4);
        int[] carpets = {12, 13, 14, 15};
        int carpetType = carpets[random.nextInt(carpets.length)];
        for (i1 = -4; i1 <= 4; ++i1) {
            for (int k16 = -4; k16 <= 4; ++k16) {
                int i22 = Math.abs(i1);
                int k22 = Math.abs(k16);
                setBlockAndMetadata(world, i1, -5, k16, LOTRLegacyBlocks.mod("brick3"), 5);
                for (int j13 = -4; j13 <= -1; ++j13) {
                    if (i22 == 4 || k22 == 4) {
                        if (j13 >= -3 && j13 <= -2) {
                            setBlockAndMetadata(world, i1, j13, k16, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
                            continue;
                        }
                        setBlockAndMetadata(world, i1, j13, k16, plank1Block, plank1Meta);
                        continue;
                    }
                    setAir(world, i1, j13, k16);
                }
                if (i22 > 2 || k22 > 2) {
                    continue;
                }
                setBlockAndMetadata(world, i1, -4, k16, LOTRLegacyBlocks.vanilla("carpet"), carpetType);
            }
        }
        for (j1 = -3; j1 <= -2; ++j1) {
            setBlockAndMetadata(world, -2, j1, -4, wood1Block, wood1Meta);
            setBlockAndMetadata(world, -1, j1, -4, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
            setBlockAndMetadata(world, 0, j1, -4, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
            setBlockAndMetadata(world, 1, j1, -4, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
            setBlockAndMetadata(world, 1, j1, -4, wood1Block, wood1Meta);
        }
        for (k13 = 2; k13 <= 3; ++k13) {
            for (i12 = -2; i12 <= 2; ++i12) {
                setAir(world, i12, 0, k13);
                int stairHeight = i12 + 2;
                for (int j14 = -4; j14 < -4 + stairHeight; ++j14) {
                    setBlockAndMetadata(world, i12, j14, k13, LOTRLegacyBlocks.mod("brick3"), 5);
                }
                setBlockAndMetadata(world, i12, -4 + stairHeight, k13, LOTRLegacyBlocks.mod("stairsWoodElvenBrick"), 1);
            }
            for (int j15 = -4; j15 <= -1; ++j15) {
                setBlockAndMetadata(world, 3, j15, k13, LOTRLegacyBlocks.mod("brick3"), 5);
            }
        }
        setBlockAndMetadata(world, -3, -2, -3, LOTRLegacyBlocks.mod("woodElvenTorch"), 3);
        setBlockAndMetadata(world, 3, -2, -3, LOTRLegacyBlocks.mod("woodElvenTorch"), 3);
        setBlockAndMetadata(world, -3, -2, 3, LOTRLegacyBlocks.mod("woodElvenTorch"), 4);
        setBlockAndMetadata(world, 3, -2, 1, LOTRLegacyBlocks.mod("woodElvenTorch"), 4);
        setBlockAndMetadata(world, 3, -4, 0, LOTRLegacyBlocks.mod("woodElvenBed"), 0);
        setBlockAndMetadata(world, 3, -4, 1, LOTRLegacyBlocks.mod("woodElvenBed"), 8);
        for (i1 = -3; i1 <= 3; ++i1) {
            setBlockAndMetadata(world, i1, -1, -3, barsBlock, 0);
        }
        for (k13 = -2; k13 <= 3; ++k13) {
            setBlockAndMetadata(world, -3, -1, k13, barsBlock, 0);
        }
        for (k13 = -2; k13 <= 1; ++k13) {
            setBlockAndMetadata(world, 3, -1, k13, barsBlock, 0);
        }
        for (j1 = 1; j1 <= 3; ++j1) {
            for (i12 = -1; i12 <= 1; ++i12) {
                setBlockAndMetadata(world, i12, j1, -4, wood1Block, wood1Meta);
            }
        }
        setBlockAndMetadata(world, 0, 0, -2, LOTRLegacyBlocks.mod("brick2"), 14);
        setBlockAndMetadata(world, -2, 0, 0, LOTRLegacyBlocks.mod("brick2"), 14);
        setBlockAndMetadata(world, 2, 0, 0, LOTRLegacyBlocks.mod("brick2"), 14);
        setBlockAndMetadata(world, 3, 0, 3, LOTRLegacyBlocks.mod("brick2"), 14);
        setAir(world, 0, 1, -5);
        setBlockAndMetadata(world, 0, 1, -4, doorBlock, 1);
        setBlockAndMetadata(world, 0, 2, -4, doorBlock, 8);
        setBlockAndMetadata(world, -1, 2, -5, LOTRLegacyBlocks.mod("woodElvenTorch"), 4);
        setBlockAndMetadata(world, 1, 2, -5, LOTRLegacyBlocks.mod("woodElvenTorch"), 4);
        setBlockAndMetadata(world, -3, 2, -4, barsBlock, 0);
        setBlockAndMetadata(world, -2, 2, -4, barsBlock, 0);
        setBlockAndMetadata(world, 2, 2, -4, barsBlock, 0);
        setBlockAndMetadata(world, 3, 2, -4, barsBlock, 0);
        setBlockAndMetadata(world, 3, 1, -3, plank2Block, plank2Meta);
        setBlockAndMetadata(world, 2, 1, -3, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, 3, 1, -2, LOTRLegacyBlocks.mod("woodElvenTable"), 0);
        setBlockAndMetadata(world, -3, 1, -3, plank2Block, plank2Meta);
        placeMug(world, random, -3, 2, -3, random.nextInt(4), LOTRFoods.WOOD_ELF_DRINK);
        setBlockAndMetadata(world, -2, 1, -3, plank2Block, plank2Meta);
        placePlate(world, random, -2, 2, -3, plateBlock, LOTRFoods.ELF);
        placeChest(world, random, -3, 1, -2, 0, LOTRChestContents.WOOD_ELF_HOUSE);
        placeWoodElfItemFrame(world, -4, 2, 0, 1, random);
        placeWoodElfItemFrame(world, 4, 2, 0, 3, random);
        for (j1 = 1; j1 <= 4; ++j1) {
            setBlockAndMetadata(world, 3, j1, 3, LOTRLegacyBlocks.vanilla("ladder"), 2);
        }
        for (j1 = -4; j1 <= 4; ++j1) {
            setBlockAndMetadata(world, 0, j1, 0, LOTRLegacyBlocks.mod("wood7"), 1);
        }
        treeGen.generate(world, random, getX(0, 0), getY(5), getZ(0, 0));
        LOTRWoodElfEntity elf = create(LOTREntities.WOOD_ELF, world);
        spawnNPCAndSetHome(elf, world, 1, 1, 1, 8);
        return true;
    }

    public void placeWoodElfItemFrame(WorldGenLevel world, int i, int j, int k, int direction, RandomSource random) {
        ItemStack item = null;
        int l = random.nextInt(3);
        switch (l) {
            case 0: {
                item = LOTRLegacyItems.modStack("mirkwoodBow", 1, 0);
                break;
            }
            case 1: {
                item = LOTRLegacyItems.vanillaStack("arrow", 1, 0);
                break;
            }
            case 2: {
                item = LOTRLegacyBlocks.mod("sapling7").stack(1, 1);
                break;
            }
            case 3: {
                item = LOTRLegacyBlocks.vanilla("red_flower").stack(1, 0);
                break;
            }
            case 4: {
                item = LOTRLegacyBlocks.vanilla("yellow_flower").stack(1, 0);
                break;
            }
            case 5: {
                item = LOTRLegacyItems.vanillaStack("book", 1, 0);
            }
        }
        spawnItemFrame(world, i, j, k, direction, item);
    }
}
