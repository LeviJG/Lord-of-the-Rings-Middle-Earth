package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dunland;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRBearRugEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRBearEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland.LOTRDunlendingWarlordEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland.LOTRDunlendingWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRDunlandHillFortStructure extends LOTRDunlandStructure {
    public LOTRDunlandHillFortStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int i1;
        int j1;
        setOriginAndRotation(world, i, j, k, rotation, 10);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i12 = -12; i12 <= 12; ++i12) {
                for (int k1 = -12; k1 <= 12; ++k1) {
                    j1 = getTopBlock(world, i12, k1) - 1;
                    if (!isSurface(world, i12, j1, k1)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 12) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -11; i1 <= 11; ++i1) {
            for (int k1 = -11; k1 <= 11; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                for (j1 = 1; j1 <= 8; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                if (i2 <= 8 && k2 <= 8 || i2 <= 1 && k1 < 0) {
                    int randomGround = random.nextInt(3);
                    switch (randomGround) {
                        case 0:
                            setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                            break;
                        case 1:
                            setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("dirt"), 1);
                            break;
                        case 2:
                            setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.mod("dirtPath"), 0);
                            break;
                        default:
                            break;
                    }
                    if ((i2 > 3 || k1 < -3 || k1 > 2) && random.nextInt(5) == 0) {
                        setBlockAndMetadata(world, i1, 1, k1, LOTRLegacyBlocks.mod("thatchFloor"), 0);
                    }
                } else {
                    setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                }
                setGrassToDirt(world, i1, -1, k1);
                j1 = -1;
                while (!isOpaque(world, i1, j1, k1) && getY(j1) >= 0) {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    --j1;
                }
            }
        }
        loadStrScan("dunland_fort");
        associateBlockMetaAlias("FLOOR", floorBlock, floorMeta);
        associateBlockMetaAlias("WOOD", woodBlock, woodMeta);
        associateBlockMetaAlias("WOOD|8", woodBlock, woodMeta | 8);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockAlias("PLANK_STAIR", plankStairBlock);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("DOOR", doorBlock);
        associateBlockMetaAlias("ROOF", roofBlock, roofMeta);
        associateBlockMetaAlias("ROOF_SLAB", roofSlabBlock, roofSlabMeta);
        associateBlockMetaAlias("ROOF_SLAB_INV", roofSlabBlock, roofSlabMeta | 8);
        associateBlockAlias("ROOF_STAIR", roofStairBlock);
        associateBlockMetaAlias("BARS", barsBlock, barsMeta);
        generateStrScan(world, random, 0, 1, 0);
        setBlockAndMetadata(world, 8, 1, 5, bedBlock, 9);
        setBlockAndMetadata(world, 7, 1, 5, bedBlock, 1);
        setBlockAndMetadata(world, 7, 1, 7, bedBlock, 0);
        setBlockAndMetadata(world, 7, 1, 8, bedBlock, 8);
        setBlockAndMetadata(world, 5, 1, 7, bedBlock, 0);
        setBlockAndMetadata(world, 5, 1, 8, bedBlock, 8);
        placeChest(world, random, 5, 1, 5, LOTRLegacyBlocks.mod("chestBasket"), 3, LOTRChestContents.DUNLENDING_HOUSE);
        placeChest(world, random, -4, 1, 8, LOTRLegacyBlocks.mod("chestBasket"), 2, LOTRChestContents.DUNLENDING_HOUSE);
        placeChest(world, random, 6, 1, -8, LOTRLegacyBlocks.vanilla("chest"), 3, LOTRChestContents.DUNLENDING_HOUSE);
        placeChest(world, random, 5, 1, -8, LOTRLegacyBlocks.vanilla("chest"), 3, LOTRChestContents.DUNLENDING_HOUSE);
        for (i1 = -6; i1 <= -5; ++i1) {
            int j12 = 1;
            int k1 = 8;
            if (random.nextBoolean()) {
                placeArmorStand(world, i1, j12, k1, 0, new ItemStack[]{LOTRLegacyItems.modStack("helmetDunlending", 1, 0), LOTRLegacyItems.modStack("bodyDunlending", 1, 0), LOTRLegacyItems.modStack("legsDunlending", 1, 0), LOTRLegacyItems.modStack("bootsDunlending", 1, 0)});
                continue;
            }
            placeArmorStand(world, i1, j12, k1, 0, new ItemStack[]{LOTRLegacyItems.modStack("helmetFur", 1, 0), LOTRLegacyItems.modStack("bodyFur", 1, 0), LOTRLegacyItems.modStack("legsFur", 1, 0), LOTRLegacyItems.modStack("bootsFur", 1, 0)});
        }
        placeWeaponRack(world, -7, 2, -3, 5, getRandomDunlandWeapon(random));
        placeBarrel(world, random, 8, 2, 7, 2, LOTRFoods.DUNLENDING_DRINK);
        placeSkull(world, random, -2, 7, -11);
        placeSkull(world, random, 2, 7, -11);
        placeSkull(world, random, -11, 7, 2);
        placeSkull(world, random, 3, 7, 8);
        placeSkull(world, random, 11, 8, -3);
        placeAnimalJar(world, 8, 2, -6, LOTRLegacyBlocks.mod("birdCageWood"), 0, create(LOTREntities.CREBAIN, world));
        setBlockAndMetadata(world, 6, 1, -3, LOTRLegacyBlocks.mod("commandTable"), 0);
        placeWallBanner(world, -2, 5, -11, "DUNLAND", 2);
        placeWallBanner(world, 2, 5, -11, "DUNLAND", 2);
        placeWallBanner(world, -8, 4, 0, "DUNLAND", 1);
        placeWallBanner(world, 8, 4, 0, "DUNLAND", 3);
        LOTRBearRugEntity rug = create(LOTREntities.BEAR_RUG, world);
        LOTRBearEntity.BearType[] bearTypes = {LOTRBearEntity.BearType.LIGHT, LOTRBearEntity.BearType.DARK, LOTRBearEntity.BearType.BLACK};
        rug.setRugType(bearTypes[random.nextInt(bearTypes.length)]);
        placeRug(rug, world, -5, 1, -4, -45.0f);
        LOTRDunlendingWarlordEntity warlord = create(LOTREntities.DUNLENDING_WARLORD, world);
        spawnNPCAndSetHome(warlord, world, 0, 1, 2, 8);
        int warriors = 6;
        for (int l = 0; l < warriors; ++l) {
            LOTRDunlendingWarriorEntity warrior = random.nextInt(3) == 0 ? create(LOTREntities.DUNLENDING_ARCHER, world) : create(LOTREntities.DUNLENDING_WARRIOR, world);
            warrior.spawnRidingHorse = false;
            spawnNPCAndSetHome(warrior, world, 0, 1, 2, 16);
        }
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClasses(LOTREntities.DUNLENDING_WARRIOR, LOTREntities.DUNLENDING_ARCHER);
        respawner.setCheckRanges(20, -8, 12, 12);
        respawner.setSpawnRanges(6, -1, 4, 16);
        placeNPCRespawner(respawner, world, 0, 0, 0);
        return true;
    }
}
