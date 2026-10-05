package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorFarmerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorFarmhandEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRHarnedorFarmStructure extends LOTRHarnedorStructure {
    public LegacyBlock crop1Block;
    public Item seed1;
    public LegacyBlock crop2Block;
    public Item seed2;

    public LOTRHarnedorFarmStructure(boolean flag) {
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
            for (int i1 = -4; i1 <= 4; ++i1) {
                for (int k1 = -4; k1 <= 4; ++k1) {
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
                    if (maxHeight - minHeight <= 6) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (int i1 = -4; i1 <= 4; ++i1) {
            for (int k1 = -4; k1 <= 4; ++k1) {
                int j12 = -1;
                while (!isOpaque(world, i1, j12, k1) && getY(j12) >= world.getMinY()) {
                    setBlockAndMetadata(world, i1, j12, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i1, j12 - 1, k1);
                    --j12;
                }
                for (j12 = 1; j12 <= 4; ++j12) {
                    setAir(world, i1, j12, k1);
                }
            }
        }
        loadStrScan("harnedor_farm");
        associateBlockMetaAlias("WOOD", woodBlock, woodMeta);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockMetaAlias("ROOF", roofBlock, roofMeta);
        associateBlockAlias("CROP1", crop1Block);
        associateBlockAlias("CROP2", crop2Block);
        generateStrScan(world, random, 0, 0, 0);
        placeSkull(world, random, 0, 4, 0);
        block6:
        for (int i1 : new int[]{-2, 2}) {
            j1 = 0;
            for (int step = 0; step < 6; ++step) {
                int j2;
                int k1 = -5 - step;
                if (isOpaque(world, i1, j1 + 1, k1)) {
                    setAir(world, i1, j1 + 1, k1);
                    setAir(world, i1, j1 + 2, k1);
                    setAir(world, i1, j1 + 3, k1);
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    j2 = j1 - 1;
                    while (!isOpaque(world, i1, j2, k1) && getY(j2) >= world.getMinY()) {
                        setBlockAndMetadata(world, i1, j2, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                        setGrassToDirt(world, i1, j2 - 1, k1);
                        --j2;
                    }
                    ++j1;
                    continue;
                }
                if (isOpaque(world, i1, j1, k1)) {
                    continue block6;
                }
                setAir(world, i1, j1 + 1, k1);
                setAir(world, i1, j1 + 2, k1);
                setAir(world, i1, j1 + 3, k1);
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                setGrassToDirt(world, i1, j1 - 1, k1);
                j2 = j1 - 1;
                while (!isOpaque(world, i1, j2, k1) && getY(j2) >= world.getMinY()) {
                    setBlockAndMetadata(world, i1, j2, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i1, j2 - 1, k1);
                    --j2;
                }
                --j1;
            }
        }
        if (random.nextInt(4) == 0) {
            LOTRHarnedorFarmerEntity farmer = create(LOTREntities.HARNEDOR_FARMER, world);
            spawnNPCAndSetHome(farmer, world, 0, 1, 1, 8);
        }
        LOTRHarnedorFarmhandEntity farmhand1 = create(LOTREntities.HARNEDOR_FARMHAND, world);
        farmhand1.seedsItem = seed1;
        spawnNPCAndSetHome(farmhand1, world, -2, 1, 0, 8);
        LOTRHarnedorFarmhandEntity farmhand2 = create(LOTREntities.HARNEDOR_FARMHAND, world);
        farmhand2.seedsItem = seed2;
        spawnNPCAndSetHome(farmhand2, world, 2, 1, 0, 8);
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        int randomCrop;
        super.setupRandomBlocks(random);
        if (random.nextBoolean()) {
            crop1Block = LOTRLegacyBlocks.vanilla("wheat");
            seed1 = LOTRLegacyItems.vanilla("wheat_seeds", 0);
        } else {
            randomCrop = random.nextInt(4);
            switch (randomCrop) {
                case 0:
                    crop1Block = LOTRLegacyBlocks.vanilla("carrots");
                    seed1 = LOTRLegacyItems.vanilla("carrot", 0);
                    break;
                case 1:
                    crop1Block = LOTRLegacyBlocks.vanilla("potatoes");
                    seed1 = LOTRLegacyItems.vanilla("potato", 0);
                    break;
                case 2:
                    crop1Block = LOTRLegacyBlocks.mod("lettuceCrop");
                    seed1 = LOTRLegacyItems.mod("lettuce", 0);
                    break;
                case 3:
                    crop1Block = LOTRLegacyBlocks.mod("turnipCrop");
                    seed1 = LOTRLegacyItems.mod("turnip", 0);
                    break;
                default:
                    break;
            }
        }
        if (random.nextBoolean()) {
            crop2Block = LOTRLegacyBlocks.vanilla("wheat");
            seed2 = LOTRLegacyItems.vanilla("wheat_seeds", 0);
        } else {
            randomCrop = random.nextInt(4);
            switch (randomCrop) {
                case 0:
                    crop2Block = LOTRLegacyBlocks.vanilla("carrots");
                    seed2 = LOTRLegacyItems.vanilla("carrot", 0);
                    break;
                case 1:
                    crop2Block = LOTRLegacyBlocks.vanilla("potatoes");
                    seed2 = LOTRLegacyItems.vanilla("potato", 0);
                    break;
                case 2:
                    crop2Block = LOTRLegacyBlocks.mod("lettuceCrop");
                    seed2 = LOTRLegacyItems.mod("lettuce", 0);
                    break;
                case 3:
                    crop2Block = LOTRLegacyBlocks.mod("turnipCrop");
                    seed2 = LOTRLegacyItems.mod("turnip", 0);
                    break;
                default:
                    break;
            }
        }
    }
}
