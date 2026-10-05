package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHaradSlaveEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRSouthronFarmStructure extends LOTRSouthronStructure {
    public LegacyBlock crop1Block;
    public Item seed1;
    public LegacyBlock crop2Block;
    public Item seed2;

    public LOTRSouthronFarmStructure(boolean flag) {
        super(flag);
    }

    public LOTRNearHaradrimBaseEntity createFarmer(WorldGenLevel world) {
        return create(LOTREntities.SOUTHRON_FARMER, world);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        setOriginAndRotation(world, i, j, k, rotation, 6);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -4; i1 <= 4; ++i1) {
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
                    if (maxHeight - minHeight <= 6) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (int i1 = -4; i1 <= 4; ++i1) {
            for (int k1 = -6; k1 <= 6; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                if (i2 == 4 && k2 == 6) {
                    continue;
                }
                j1 = -1;
                while (!isOpaque(world, i1, j1, k1) && getY(j1) >= 0) {
                    setBlockAndMetadata(world, i1, j1, k1, stoneBlock, stoneMeta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    --j1;
                }
                for (j1 = 1; j1 <= 4; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("southron_farm");
        associateBlockMetaAlias("STONE", stoneBlock, stoneMeta);
        associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
        associateBlockMetaAlias("BRICK_SLAB", brickSlabBlock, brickSlabMeta);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockAlias("CROP1", crop1Block);
        associateBlockAlias("CROP2", crop2Block);
        generateStrScan(world, random, 0, 0, 0);
        placeSkull(world, random, -4, 4, 0);
        placeSkull(world, random, 4, 4, 0);
        if (random.nextInt(4) == 0) {
            LOTRNearHaradrimBaseEntity farmer = createFarmer(world);
            spawnNPCAndSetHome(farmer, world, 0, 1, 1, 8);
        }
        LOTRHaradSlaveEntity farmhand1 = create(LOTREntities.HARAD_SLAVE, world);
        farmhand1.seedsItem = seed1;
        spawnNPCAndSetHome(farmhand1, world, -2, 1, 0, 8);
        LOTRHaradSlaveEntity farmhand2 = create(LOTREntities.HARAD_SLAVE, world);
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
