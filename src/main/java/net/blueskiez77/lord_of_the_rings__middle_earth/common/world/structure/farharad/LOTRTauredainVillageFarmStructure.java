package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad.LOTRTauredainFarmerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad.LOTRTauredainFarmhandEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRTauredainVillageFarmStructure extends LOTRTauredainHouseStructure {
    public LegacyBlock cropBlock;
    public int cropMeta;
    public Item seedItem;
    public boolean melon;

    public LOTRTauredainVillageFarmStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int i1;
        if (!super.generateWithSetRotation(world, random, i, j, k, rotation)) {
            return false;
        }
        int randomCrop = random.nextInt(8);
        switch (randomCrop) {
            case 0:
            case 1:
                cropBlock = LOTRLegacyBlocks.vanilla("potatoes");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.vanilla("potato", 0);
                melon = false;
                break;
            case 2:
            case 3:
                cropBlock = LOTRLegacyBlocks.mod("cornStalk");
                cropMeta = 0;
                seedItem = LOTRLegacyBlocks.mod("cornStalk").state().getBlock().asItem();
                melon = false;
                break;
            case 4:
                cropBlock = LOTRLegacyBlocks.vanilla("wheat");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.vanilla("wheat_seeds", 0);
                melon = false;
                break;
            case 5:
                cropBlock = LOTRLegacyBlocks.vanilla("carrots");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.vanilla("carrot", 0);
                melon = false;
                break;
            case 6:
            case 7:
                cropBlock = LOTRLegacyBlocks.vanilla("melon_stem");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.vanilla("melon_seeds", 0);
                melon = true;
                break;
            default:
                break;
        }
        for (i1 = -4; i1 <= 4; ++i1) {
            for (int k1 = -3; k1 <= 3; ++k1) {
                for (int j1 = 1; j1 <= 4; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("taurethrim_farm");
        associateBlockAlias("BRICK_STAIR", brickStairBlock);
        associateBlockMetaAlias("BRICK_WALL", brickWallBlock, brickWallMeta);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockMetaAlias("CROP", cropBlock, cropMeta);
        generateStrScan(world, random, 0, 0, 0);
        if (melon) {
            for (int k1 = -2; k1 <= 2; ++k1) {
                setBlockAndMetadata(world, 0, 1, k1, brickBlock, brickMeta);
            }
            for (i1 = -1; i1 <= 1; ++i1) {
                setBlockAndMetadata(world, i1, 0, 0, LOTRLegacyBlocks.vanilla("stained_hardened_clay"), 12);
                setBlockAndMetadata(world, i1, 1, 0, LOTRLegacyBlocks.vanilla("water"), 0);
                setAir(world, i1, 2, 0);
            }
            for (int k1 : new int[]{-1, 1}) {
                for (int i12 = -3; i12 <= 3; ++i12) {
                    if (i12 == 0) {
                        continue;
                    }
                    setBlockAndMetadata(world, i12, 0, k1, LOTRLegacyBlocks.vanilla("sand"), 0);
                    setBlockAndMetadata(world, i12, 1, k1, LOTRLegacyBlocks.mod("mudGrass"), 0);
                }
            }
        }
        if (random.nextInt(3) == 0) {
            LOTRTauredainFarmerEntity farmer = create(LOTREntities.TAUREDAIN_FARMER, world);
            spawnNPCAndSetHome(farmer, world, 0, 2, 1, 4);
        }
        LOTRTauredainFarmhandEntity farmhand1 = create(LOTREntities.TAUREDAIN_FARMHAND, world);
        farmhand1.seedsItem = seedItem;
        spawnNPCAndSetHome(farmhand1, world, -2, 2, 0, 6);
        LOTRTauredainFarmhandEntity farmhand2 = create(LOTREntities.TAUREDAIN_FARMHAND, world);
        farmhand2.seedsItem = seedItem;
        spawnNPCAndSetHome(farmhand2, world, 2, 2, 0, 6);
        return true;
    }

    @Override
    public int getOffset() {
        return 4;
    }
}
