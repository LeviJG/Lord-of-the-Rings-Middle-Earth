package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfHaradWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRGulfTowerStructure extends LOTRGulfStructure {
    public LOTRGulfTowerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 4);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -3; i1 <= 3; ++i1) {
                for (k1 = -3; k1 <= 3; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            for (k1 = -3; k1 <= 3; ++k1) {
                for (j1 = 1; j1 <= 16; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("gulf_tower");
        associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
        associateBlockAlias("BRICK_STAIR", brickStairBlock);
        associateBlockMetaAlias("WOOD", woodBlock, woodMeta);
        associateBlockMetaAlias("WOOD|4", woodBlock, woodMeta | 4);
        associateBlockMetaAlias("WOOD|8", woodBlock, woodMeta | 8);
        associateBlockMetaAlias("WOOD|12", woodBlock, woodMeta | 0xC);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockAlias("PLANK_STAIR", plankStairBlock);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("ROOF_STAIR", roofStairBlock);
        associateBlockMetaAlias("FLAG", flagBlock, flagMeta);
        associateBlockMetaAlias("BONE", boneBlock, boneMeta);
        generateStrScan(world, random, 0, 0, 0);
        placeChest(world, random, -2, 1, 0, LOTRLegacyBlocks.mod("chestBasket"), 4, LOTRChestContents.GULF_HOUSE);
        placeSkull(world, random, 2, 2, 1);
        placeBarrel(world, random, -2, 2, -1, 4, LOTRFoods.GULF_HARAD_DRINK);
        placeMug(world, random, 2, 2, -1, 2, LOTRFoods.GULF_HARAD_DRINK);
        placePlate(world, random, 2, 2, 0, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRFoods.GULF_HARAD);
        placePlate(world, random, -2, 2, 1, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRFoods.GULF_HARAD);
        placeWallBanner(world, 0, 8, -3, "HARAD_GULF", 2);
        placeWallBanner(world, 0, 8, 3, "HARAD_GULF", 0);
        int warriors = 1 + random.nextInt(2);
        for (int l = 0; l < warriors; ++l) {
            LOTRGulfHaradWarriorEntity warrior = random.nextInt(3) == 0 ? create(LOTREntities.GULF_ARCHER, world) : create(LOTREntities.GULF_WARRIOR, world);
            warrior.spawnRidingHorse = false;
            spawnNPCAndSetHome(warrior, world, 0, 14, 0, 8);
        }
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClasses(LOTREntities.GULF_WARRIOR, LOTREntities.GULF_ARCHER);
        respawner.setCheckRanges(6, -20, 4, 4);
        respawner.setSpawnRanges(1, -2, 1, 8);
        placeNPCRespawner(respawner, world, 0, 14, 0);
        return true;
    }
}
