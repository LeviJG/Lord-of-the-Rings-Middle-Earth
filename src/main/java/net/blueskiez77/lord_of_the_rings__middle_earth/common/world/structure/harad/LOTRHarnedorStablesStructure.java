package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRHorseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedhrimEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRHarnedorStablesStructure extends LOTRHarnedorStructure {
    public LOTRHarnedorStablesStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        setOriginAndRotation(world, i, j, k, rotation, 9);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -7; i1 <= 7; ++i1) {
                for (int k1 = -10; k1 <= 10; ++k1) {
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
        for (int i1 = -7; i1 <= 7; ++i1) {
            for (int k1 = -10; k1 <= 10; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                if ((i2 > 5 || k2 > 4) && (i2 > 4 || k2 > 6) && (i2 > 3 || k2 > 7) && (i2 > 2 || k2 > 8) && (i2 > 1 || k2 > 9)) {
                    continue;
                }
                for (j1 = 1; j1 <= 6; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("harnedor_stables");
        associateBlockMetaAlias("WOOD", woodBlock, woodMeta);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockAlias("PLANK_STAIR", plankStairBlock);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockMetaAlias("ROOF", roofBlock, roofMeta);
        addBlockMetaAliasOption("GROUND", 5, LOTRLegacyBlocks.vanilla("grass"), 0);
        addBlockMetaAliasOption("GROUND", 4, LOTRLegacyBlocks.vanilla("dirt"), 1);
        addBlockMetaAliasOption("GROUND", 1, LOTRLegacyBlocks.vanilla("sand"), 0);
        generateStrScan(world, random, 0, 0, 0);
        placeWallBanner(world, -2, 4, -4, "NEAR_HARAD", 2);
        placeWallBanner(world, 2, 4, -4, "NEAR_HARAD", 2);
        placeWallBanner(world, -2, 4, 4, "NEAR_HARAD", 0);
        placeWallBanner(world, 2, 4, 4, "NEAR_HARAD", 0);
        spawnItemFrame(world, -2, 2, 0, 1, LOTRLegacyItems.vanillaStack("saddle", 1, 0));
        spawnItemFrame(world, 2, 2, 0, 3, LOTRLegacyItems.vanillaStack("lead", 1, 0));
        setBlockAndMetadata(world, -3, 1, 6, bedBlock, 0);
        setBlockAndMetadata(world, -3, 1, 7, bedBlock, 8);
        placeChest(world, random, -4, 1, 6, LOTRLegacyBlocks.mod("chestBasket"), 4, LOTRChestContents.HARNENNOR_HOUSE);
        placePlateWithCertainty(world, random, 4, 2, 6, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRFoods.HARNEDOR);
        placeMug(world, random, 4, 2, 5, 1, LOTRFoods.HARNEDOR_DRINK);
        LOTRHarnedhrimEntity harnedhrim = create(LOTREntities.HARNEDHRIM, world);
        spawnNPCAndSetHome(harnedhrim, world, 0, 1, 0, 12);
        for (int k1 : new int[]{-2, 2}) {
            for (int i1 : new int[]{-4, 4}) {
                int j12 = 1;
                LOTRHorseEntity horse = create(LOTREntities.HORSE, world);
                spawnNPCAndSetHome(horse, world, i1, j12, k1, 0);
                horse.saddleMountForWorldGen();
                horse.clearHome();
            }
        }
        return true;
    }
}
