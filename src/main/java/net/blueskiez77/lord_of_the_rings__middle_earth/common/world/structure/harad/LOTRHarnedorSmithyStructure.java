package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorBlacksmithEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRHarnedorSmithyStructure extends LOTRHarnedorStructure {
    public LOTRHarnedorSmithyStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        setOriginAndRotation(world, i, j, k, rotation, 5);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -12; i1 <= 8; ++i1) {
                for (int k1 = -6; k1 <= 6; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (!isSurface(world, i1, j1, k1)) {
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
        for (int i1 = -10; i1 <= 6; ++i1) {
            for (int k1 = -6; k1 <= 6; ++k1) {
                int k2 = Math.abs(k1);
                if ((i1 < -8 || i1 > 4 || k2 != 4) && k2 > 3) {
                    continue;
                }
                j1 = -1;
                while (!isOpaque(world, i1, j1, k1) && getY(j1) >= world.getMinY()) {
                    setBlockAndMetadata(world, i1, j1, k1, plank2Block, plank2Meta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    --j1;
                }
                for (j1 = 1; j1 <= 8; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("harnedor_smithy");
        associateBlockMetaAlias("WOOD", woodBlock, woodMeta);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockAlias("PLANK_STAIR", plankStairBlock);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockAlias("DOOR", doorBlock);
        associateBlockMetaAlias("PLANK2", plank2Block, plank2Meta);
        associateBlockMetaAlias("ROOF", roofBlock, roofMeta);
        generateStrScan(world, random, 0, 1, 0);
        placeWeaponRack(world, -1, 2, -1, 5, getRandomHarnedorWeapon(random));
        placeWeaponRack(world, -1, 2, 1, 5, getRandomHarnedorWeapon(random));
        placeArmorStand(world, 3, 1, 3, 2, null);
        if (random.nextBoolean()) {
            placeArmorStand(world, 0, 1, 3, 0, new ItemStack[]{LOTRLegacyItems.modStack("helmetHarnedor", 1, 0), LOTRLegacyItems.modStack("bodyHarnedor", 1, 0), LOTRLegacyItems.modStack("legsHarnedor", 1, 0), LOTRLegacyItems.modStack("bootsHarnedor", 1, 0)});
        } else {
            placeArmorStand(world, 0, 1, 3, 0, new ItemStack[]{null, LOTRLegacyItems.modStack("bodyHarnedor", 1, 0), null, null});
        }
        placeChest(world, random, 5, 1, -2, LOTRLegacyBlocks.mod("chestBasket"), 5, LOTRChestContents.HARNENNOR_HOUSE);
        placeChest(world, random, -7, 1, 3, LOTRLegacyBlocks.mod("chestBasket"), 2, LOTRChestContents.HARNENNOR_HOUSE);
        placeBarrel(world, random, -3, 2, -1, 5, LOTRFoods.HARNEDOR_DRINK);
        placeMug(world, random, -3, 2, 0, 2, LOTRFoods.HARNEDOR_DRINK);
        placeMug(world, random, -9, 2, -2, 3, LOTRFoods.HARNEDOR_DRINK);
        placePlate(world, random, -5, 2, 3, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRFoods.HARNEDOR);
        placePlate(world, random, -3, 2, 3, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRFoods.HARNEDOR);
        placeFlowerPot(world, -4, 2, 3, getRandomFlower(world, random));
        setBlockAndMetadata(world, -8, 1, 1, bedBlock, 3);
        setBlockAndMetadata(world, -9, 1, 1, bedBlock, 11);
        LOTRHarnedorBlacksmithEntity smith = create(LOTREntities.HARNEDOR_BLACKSMITH, world);
        spawnNPCAndSetHome(smith, world, 0, 1, 0, 8);
        return true;
    }
}
