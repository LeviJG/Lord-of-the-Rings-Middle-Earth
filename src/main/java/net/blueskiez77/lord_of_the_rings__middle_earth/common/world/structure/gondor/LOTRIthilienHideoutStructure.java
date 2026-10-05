package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger.LOTRRangerIthilienEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRIthilienHideoutStructure extends LOTRStructureBase2 {
    public LOTRIthilienHideoutStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int ladderY;
        int k1;
        int i1;
        int i12;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        int width = 5;
        int height = 4;
        int baseY = -(height + 2 + random.nextInt(4));
        if (restrictions) {
            if (!isSurface(world, 0, -1, 0)) {
                return false;
            }
            for (i12 = -width; i12 <= width; ++i12) {
                for (k1 = -width; k1 <= width; ++k1) {
                    for (int j1 = baseY; j1 <= baseY + height + 2; ++j1) {
                        if (isOpaque(world, i12, j1, k1)) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        }
        for (i12 = -width - 1; i12 <= width + 1; ++i12) {
            for (k1 = -width - 1; k1 <= width + 1; ++k1) {
                int i2 = Math.abs(i12);
                int k2 = Math.abs(k1);
                boolean withinWalls = i2 <= width && k2 <= width;
                setBlockAndMetadata(world, i12, baseY, k1, LOTRLegacyBlocks.vanilla("stone"), 0);
                setBlockAndMetadata(world, i12, baseY + height + 1, k1, LOTRLegacyBlocks.vanilla("stone"), 0);
                for (int j1 = baseY + 1; j1 <= baseY + height; ++j1) {
                    if (withinWalls) {
                        setAir(world, i12, j1, k1);
                        continue;
                    }
                    setBlockAndMetadata(world, i12, j1, k1, LOTRLegacyBlocks.vanilla("stone"), 0);
                }
                if (!withinWalls) {
                    continue;
                }
                if (i2 <= 2 && k2 <= 2 || random.nextInt(3) == 0) {
                    setBlockAndMetadata(world, i12, baseY + 1, k1, LOTRLegacyBlocks.mod("thatchFloor"), 0);
                }
                if (i2 != width && k2 != width) {
                    continue;
                }
                setBlockAndMetadata(world, i12, baseY + 1, k1, LOTRLegacyBlocks.mod("planks"), 8);
            }
        }
        for (ladderY = baseY + 1; ladderY <= baseY + height || isOpaque(world, 0, ladderY, 0) || isOpaque(world, -1, ladderY, 0) && isOpaque(world, 1, ladderY, 0) && isOpaque(world, 0, ladderY, -1) && isOpaque(world, 0, ladderY, 1); ++ladderY) {
            if (!isOpaque(world, 0, ladderY, -1)) {
                setBlockAndMetadata(world, 0, ladderY, -1, LOTRLegacyBlocks.vanilla("stone"), 0);
            }
            setBlockAndMetadata(world, 0, ladderY, 0, LOTRLegacyBlocks.vanilla("ladder"), 3);
        }
        for (int pass = 0; pass <= 1; ++pass) {
            for (i1 = -1; i1 <= 1; ++i1) {
                block9:
                for (int k12 = -1; k12 <= 1; ++k12) {
                    int i2 = Math.abs(i1);
                    int k2 = Math.abs(k12);
                    if (i1 == 0 && k12 == 0) {
                        continue;
                    }
                    if (pass == 0 && i1 == 0 && k12 == 1) {
                        for (int j1 = 0; j1 <= 3; ++j1) {
                            int j2 = ladderY + j1;
                            if (placeTree(world, random, "OAK_ITHILIEN_HIDEOUT", getX(0, 1), getY(j2), getZ(0, 1))) {
                                break;
                            }
                        }
                    }
                    if (pass != 1) {
                        continue;
                    }
                    boolean doublegrass = i2 != k2;
                    for (int j1 = -3; j1 <= 3; ++j1) {
                        int j2 = ladderY + j1;
                        BlockState below = getBlockState(world, i1, j2 - 1, k12);
                        if (!LOTRLegacyBlocks.vanilla("grass").matches(below) && !LOTRLegacyBlocks.vanilla("dirt").matches(below) || isOpaque(world, i1, j2, k12) || isOpaque(world, i1, j2 + 1, k12)) {
                            continue;
                        }
                        if (doublegrass) {
                            setBlockAndMetadata(world, i1, j2, k12, LOTRLegacyBlocks.vanilla("double_plant"), 2);
                            setBlockAndMetadata(world, i1, j2 + 1, k12, LOTRLegacyBlocks.vanilla("double_plant"), 8);
                            continue block9;
                        }
                        setBlockAndMetadata(world, i1, j2, k12, LOTRLegacyBlocks.vanilla("tallgrass"), 1);
                        continue block9;
                    }
                }
            }
        }
        setBlockAndMetadata(world, -width, baseY + 3, -width, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, -width, baseY + 3, width, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, width, baseY + 3, -width, LOTRLegacyBlocks.vanilla("torch"), 1);
        setBlockAndMetadata(world, width, baseY + 3, width, LOTRLegacyBlocks.vanilla("torch"), 1);
        placeWallBanner(world, -width - 1, baseY + 4, 0, "ITHILIEN", 1);
        placeWallBanner(world, 0, baseY + 4, -width - 1, "ITHILIEN", 0);
        placeWallBanner(world, width + 1, baseY + 4, 0, "ITHILIEN", 3);
        placeWallBanner(world, -2, baseY + 4, width + 1, "ITHILIEN", 2);
        placeWallBanner(world, 0, baseY + 4, width + 1, "GONDOR", 2);
        placeWallBanner(world, 2, baseY + 4, width + 1, "ITHILIEN", 2);
        setBlockAndMetadata(world, -2, baseY + 1, width, LOTRLegacyBlocks.mod("gondorianTable"), 0);
        setBlockAndMetadata(world, 0, baseY + 1, width, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, 2, baseY + 1, width, LOTRLegacyBlocks.vanilla("furnace"), 2);
        placeChest(world, random, width, baseY + 1, 0, LOTRLegacyBlocks.mod("chestLebethron"), 5, LOTRChestContents.GONDOR_FORTRESS_DRINKS);
        ItemStack drink = LOTRFoods.GONDOR_DRINK.getRandomBrewableDrink(random);
        placeBarrel(world, random, width, baseY + 2, -3, 5, drink);
        placeBarrel(world, random, width, baseY + 2, -2, 5, drink);
        placeBarrel(world, random, width, baseY + 2, 2, 5, drink);
        placeBarrel(world, random, width, baseY + 2, 3, 5, drink);
        for (i1 = -3; i1 <= 3; i1 += 2) {
            setBlockAndMetadata(world, i1, baseY + 1, -width + 1, LOTRLegacyBlocks.mod("strawBed"), 2);
            setBlockAndMetadata(world, i1, baseY + 1, -width, LOTRLegacyBlocks.mod("strawBed"), 10);
        }
        placeChest(world, random, -width, baseY + 1, 0, LOTRLegacyBlocks.mod("chestLebethron"), 4, LOTRChestContents.GONDOR_FORTRESS_SUPPLIES);
        ItemStack[] rangerArmor = {LOTRLegacyItems.modStack("helmetRangerIthilien", 1, 0), LOTRLegacyItems.modStack("bodyRangerIthilien", 1, 0), LOTRLegacyItems.modStack("legsRangerIthilien", 1, 0), LOTRLegacyItems.modStack("bootsRangerIthilien", 1, 0)};
        placeArmorStand(world, -width, baseY + 2, -2, 3, rangerArmor);
        placeArmorStand(world, -width, baseY + 2, 2, 3, rangerArmor);
        int rangers = 2 + random.nextInt(3);
        for (int l = 0; l < rangers; ++l) {
            LOTRRangerIthilienEntity ranger = create(LOTREntities.RANGER_ITHILIEN, world);
            spawnNPCAndSetHome(ranger, world, -2, baseY + 1, -2, 16);
        }
        spawnNPCAndSetHome(create(LOTREntities.RANGER_ITHILIEN_CAPTAIN, world), world, -2, baseY + 1, -2, 16);
        return true;
    }
}
