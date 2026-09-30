package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LOTRHobbitWindmillStructure extends LOTRStructureBase2 {
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock woodBlock;
    public int woodMeta;
    public LegacyBlock doorBlock;

    public LOTRHobbitWindmillStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k2;
        BlockEntity te;
        int i1;
        int j1;
        int j12;
        LegacyBlock fillBlock;
        int k1;
        int j13;
        int i2;
        int fillMeta;
        setOriginAndRotation(world, i, j, k, rotation, 5);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -4; i1 <= 4; ++i1) {
                for (k1 = -4; k1 <= 4; ++k1) {
                    j12 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j12, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -4; i1 <= 4; ++i1) {
            for (k1 = -4; k1 <= 4; ++k1) {
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                if (i2 >= 3 && k2 > 3 || k2 >= 3 && i2 > 3) {
                    continue;
                }
                fillMeta = 0;
                if (i2 == 3 && k2 == 3) {
                    fillBlock = plankBlock;
                    fillMeta = plankMeta;
                } else if (i2 == 4 && k2 == 2 || i2 == 2 && k2 == 4) {
                    fillBlock = woodBlock;
                    fillMeta = woodMeta;
                } else if (i2 == 4 || k2 == 4) {
                    fillBlock = plankBlock;
                    fillMeta = plankMeta;
                } else {
                    fillBlock = LOTRLegacyBlocks.vanilla("air");
                }
                for (j13 = 4; (j13 >= 0 || !isOpaque(world, i1, j13, k1)) && getY(j13) >= 0; --j13) {
                    if (fillBlock == LOTRLegacyBlocks.vanilla("air")) {
                        if (j13 == 4 || j13 <= 0) {
                            setBlockAndMetadata(world, i1, j13, k1, plankBlock, plankMeta);
                            setGrassToDirt(world, i1, j13 - 1, k1);
                            continue;
                        }
                        setAir(world, i1, j13, k1);
                        continue;
                    }
                    setBlockAndMetadata(world, i1, j13, k1, fillBlock, fillMeta);
                    setGrassToDirt(world, i1, j13 - 1, k1);
                }
            }
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            for (k1 = -3; k1 <= 3; ++k1) {
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                if (i2 == 3 && k2 == 3) {
                    continue;
                }
                fillMeta = 0;
                if (i2 == 3 && k2 == 2 || i2 == 2 && k2 == 3) {
                    fillBlock = woodBlock;
                    fillMeta = woodMeta;
                } else if (i2 == 3 || k2 == 3) {
                    fillBlock = plankBlock;
                    fillMeta = plankMeta;
                } else {
                    fillBlock = LOTRLegacyBlocks.vanilla("air");
                }
                for (j13 = 5; j13 <= 8; ++j13) {
                    if (fillBlock == LOTRLegacyBlocks.vanilla("air")) {
                        setAir(world, i1, j13, k1);
                        continue;
                    }
                    setBlockAndMetadata(world, i1, j13, k1, fillBlock, fillMeta);
                }
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -2; k1 <= 2; ++k1) {
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                for (j1 = 9; j1 <= 12; ++j1) {
                    if (i2 == 2 && k2 == 2) {
                        setBlockAndMetadata(world, i1, j1, k1, woodBlock, woodMeta);
                        continue;
                    }
                    if (i2 == 2 || k2 == 2) {
                        setBlockAndMetadata(world, i1, j1, k1, plankBlock, plankMeta);
                        continue;
                    }
                    setAir(world, i1, j1, k1);
                }
            }
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            for (k1 = -1; k1 <= 1; ++k1) {
                for (j12 = 11; j12 <= 12; ++j12) {
                    setBlockAndMetadata(world, i1, j12, k1, plankBlock, plankMeta);
                }
            }
        }
        setBlockAndMetadata(world, 0, 10, 0, LOTRLegacyBlocks.mod("chandelier"), 2);
        int originX = 0;
        int originY = 13;
        int originZ = 0;
        int radius = 4;
        for (int i12 = originX - radius; i12 <= originX + radius; ++i12) {
            for (int j14 = originY - radius; j14 <= originY + radius; ++j14) {
                for (int k12 = originZ - radius; k12 <= originZ + radius; ++k12) {
                    int i22 = i12 - originX;
                    int j2 = j14 - originY;
                    int k22 = k12 - originZ;
                    int dist = i22 * i22 + j2 * j2 + k22 * k22;
                    if (dist >= radius * radius || j14 < originY) {
                        continue;
                    }
                    setBlockAndMetadata(world, i12, j14, k12, LOTRLegacyBlocks.mod("clayTileDyed"), 13);
                }
            }
        }
        setBlockAndMetadata(world, -3, 6, 0, LOTRLegacyBlocks.mod("glassPane"), 0);
        setBlockAndMetadata(world, 3, 6, 0, LOTRLegacyBlocks.mod("glassPane"), 0);
        setBlockAndMetadata(world, 0, 6, -3, LOTRLegacyBlocks.mod("glassPane"), 0);
        setBlockAndMetadata(world, 0, 6, 3, LOTRLegacyBlocks.mod("glassPane"), 0);
        placeFenceTorch(world, -2, 2, -3);
        placeFenceTorch(world, -2, 2, 3);
        placeFenceTorch(world, 2, 2, -3);
        placeFenceTorch(world, 2, 2, 3);
        placeFenceTorch(world, -3, 2, -2);
        placeFenceTorch(world, 3, 2, -2);
        placeFenceTorch(world, -3, 2, 2);
        placeFenceTorch(world, 3, 2, 2);
        setBlockAndMetadata(world, 0, 1, -4, doorBlock, 1);
        setBlockAndMetadata(world, 0, 2, -4, doorBlock, 8);
        setBlockAndMetadata(world, -3, 1, -1, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -3, 1, 0, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -2, 1, 0, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -3, 1, 1, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -2, 1, 1, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -3, 2, 1, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        for (j1 = 1; j1 <= 4; ++j1) {
            setBlockAndMetadata(world, 0, j1, 2, woodBlock, woodMeta);
            setBlockAndMetadata(world, 0, j1, 1, LOTRLegacyBlocks.vanilla("ladder"), 2);
        }
        setBlockAndMetadata(world, 1, 5, -2, LOTRLegacyBlocks.vanilla("bed"), 1);
        setBlockAndMetadata(world, 2, 5, -2, LOTRLegacyBlocks.vanilla("bed"), 9);
        setBlockAndMetadata(world, -2, 5, -2, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
        setBlockAndMetadata(world, -1, 5, -2, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
        setBlockAndMetadata(world, -2, 6, -2, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
        setBlockAndMetadata(world, -1, 6, -2, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
        setBlockAndMetadata(world, -2, 5, -1, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, -2, 5, 1, plankBlock, plankMeta);
        setBlockAndMetadata(world, -2, 5, 2, plankBlock, plankMeta);
        setBlockAndMetadata(world, -2, 6, 1, LOTRHobbitStructure.getRandomCakeBlock(random), 0);
        placeBarrel(world, random, -2, 6, 2, 4, LOTRFoods.HOBBIT_DRINK);
        setBlockAndMetadata(world, 2, 5, 1, LOTRLegacyBlocks.mod("hobbitOven"), 5);
        setBlockAndMetadata(world, 2, 5, 2, LOTRLegacyBlocks.mod("hobbitOven"), 5);
        placeChest(world, random, -2, 5, 0, 4, LOTRChestContents.HOBBIT_HOLE_STUDY);
        placeChest(world, random, 2, 5, 0, 5, LOTRChestContents.HOBBIT_HOLE_LARDER);
        if (random.nextInt(20) == 0 && (te = getTileEntity(world, 2, 5, 0)) instanceof Container) {
            Container chest = (Container) te;
            ItemStack hooch = LOTRLegacyItems.modStack("mugLemonLiqueur", 1, 0);
            hooch.set(LOTRDataComponents.DRINK_STRENGTH, 1);
            hooch.set(LOTRDataComponents.VESSEL, LOTRVessel.MUG);
            hooch.set(DataComponents.CUSTOM_NAME, Component.literal("Bad Windmill Hooch"));
            hooch.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("Really nothing compared to the Spoons Hooch."))));
            int slot = random.nextInt(chest.getContainerSize());
            chest.setItem(slot, hooch);
        }
        setBlockAndMetadata(world, 0, 10, -3, plankBlock, plankMeta);
        setBlockAndMetadata(world, 0, 10, -4, LOTRLegacyBlocks.vanilla("wool"), 15);
        for (j1 = 7; j1 <= 13; ++j1) {
            for (int i13 = -3; i13 <= 3; ++i13) {
                int j2 = Math.abs(j1 - 10);
                if (j2 != Math.abs(i13) || j2 == 0) {
                    continue;
                }
                setBlockAndMetadata(world, i13, j1, -4, LOTRLegacyBlocks.vanilla("wool"), 0);
            }
        }
        LOTRHobbitEntity hobbit = create(LOTREntities.HOBBIT, world);
        spawnNPCAndSetHome(hobbit, world, 0, 1, 0, 8);
        return true;
    }

    public void placeFenceTorch(WorldGenLevel world, int i, int j, int k) {
        setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("fence"), 0);
        setBlockAndMetadata(world, i, j + 1, k, LOTRLegacyBlocks.vanilla("torch"), 5);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        if (random.nextBoolean()) {
            woodBlock = LOTRLegacyBlocks.vanilla("log");
            woodMeta = 0;
            plankBlock = LOTRLegacyBlocks.vanilla("planks");
            plankMeta = 0;
            doorBlock = LOTRLegacyBlocks.vanilla("wooden_door");
        } else {
            woodBlock = LOTRLegacyBlocks.mod("wood");
            woodMeta = 0;
            plankBlock = LOTRLegacyBlocks.mod("planks");
            plankMeta = 0;
            doorBlock = LOTRLegacyBlocks.mod("doorShirePine");
        }
    }
}
