package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor.LOTRBlackUrukCaptainEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor.LOTRBlackUrukEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRBlackUrukFortStructure extends LOTRMordorStructure {
    public LOTRBlackUrukFortStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 19);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -18; i1 <= 26; ++i1) {
                for (int k1 = -18; k1 <= 20; ++k1) {
                    int j1 = getTopBlock(world, i1, k1) - 1;
                    if (!isSurface(world, i1, j1, k1)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 16) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (int i1 = -17; i1 <= 25; ++i1) {
            for (int k1 = -18; k1 <= 19; ++k1) {
                for (int j1 = 1; j1 <= 16; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("black_uruk_fort");
        associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
        associateBlockMetaAlias("BRICK_SLAB", brickSlabBlock, brickSlabMeta);
        associateBlockMetaAlias("BRICK_SLAB_INV", brickSlabBlock, brickSlabMeta | 8);
        associateBlockAlias("BRICK_STAIR", brickStairBlock);
        associateBlockMetaAlias("BRICK_WALL", brickWallBlock, brickWallMeta);
        associateBlockMetaAlias("BRICK_CARVED", brickCarvedBlock, brickCarvedMeta);
        associateBlockMetaAlias("PILLAR", pillarBlock, pillarMeta);
        associateBlockMetaAlias("SMOOTH", smoothBlock, smoothMeta);
        associateBlockMetaAlias("SMOOTH_SLAB", smoothSlabBlock, smoothSlabMeta);
        associateBlockMetaAlias("TILE", tileBlock, tileMeta);
        associateBlockMetaAlias("TILE_SLAB", tileSlabBlock, tileSlabMeta);
        associateBlockMetaAlias("TILE_SLAB_INV", tileSlabBlock, tileSlabMeta | 8);
        associateBlockAlias("TILE_STAIR", tileStairBlock);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockAlias("PLANK_STAIR", plankStairBlock);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockMetaAlias("BEAM", beamBlock, beamMeta);
        associateBlockMetaAlias("BEAM|4", beamBlock, beamMeta | 4);
        associateBlockMetaAlias("BEAM|8", beamBlock, beamMeta | 8);
        addBlockMetaAliasOption("GROUND", 6, LOTRLegacyBlocks.mod("rock"), 0);
        addBlockMetaAliasOption("GROUND", 2, LOTRLegacyBlocks.mod("mordorDirt"), 0);
        addBlockMetaAliasOption("GROUND", 2, LOTRLegacyBlocks.mod("mordorGravel"), 0);
        associateBlockAlias("TRAPDOOR", trapdoorBlock);
        associateBlockAlias("GATE_IRON", gateIronBlock);
        associateBlockAlias("GATE_ORC", gateOrcBlock);
        associateBlockMetaAlias("BARS", barsBlock, 0);
        associateBlockMetaAlias("CHANDELIER", chandelierBlock, chandelierMeta);
        generateStrScan(world, random, 0, 0, 0);
        placeWallBanner(world, -2, 5, -17, "BLACK_URUK", 2);
        placeWallBanner(world, 2, 5, -17, "BLACK_URUK", 2);
        placeWallBanner(world, 0, 14, -20, "MORDOR", 2);
        setBlockAndMetadata(world, -2, 11, -15, LOTRLegacyBlocks.mod("commandTable"), 0);
        placeOrcTorch(world, -3, 2, -18);
        placeOrcTorch(world, 3, 2, -18);
        placeOrcTorch(world, 19, 2, -11);
        placeOrcTorch(world, -3, 2, -10);
        placeOrcTorch(world, 3, 2, -10);
        placeOrcTorch(world, 2, 2, 4);
        placeOrcTorch(world, 5, 2, 4);
        placeOrcTorch(world, -11, 2, 12);
        placeOrcTorch(world, 18, 4, -13);
        placeOrcTorch(world, 10, 4, -13);
        placeOrcTorch(world, 21, 4, -10);
        placeOrcTorch(world, 21, 4, -2);
        placeOrcTorch(world, 24, 4, 1);
        placeOrcTorch(world, 20, 4, 1);
        placeOrcTorch(world, -13, 4, 3);
        placeOrcTorch(world, 5, 4, 9);
        placeOrcTorch(world, 2, 4, 9);
        placeOrcTorch(world, -13, 4, 11);
        placeOrcTorch(world, 11, 4, 14);
        placeOrcTorch(world, -4, 4, 14);
        placeOrcTorch(world, -10, 4, 14);
        placeOrcTorch(world, 20, 4, 17);
        placeOrcTorch(world, 22, 10, -14);
        placeOrcTorch(world, 22, 15, -14);
        placeWeaponRack(world, 9, 2, 2, 6, getRandomUrukWeapon(random));
        placeWeaponRack(world, 10, 2, 3, 5, getRandomUrukWeapon(random));
        placeWeaponRack(world, 8, 2, 3, 7, getRandomUrukWeapon(random));
        placeWeaponRack(world, 9, 2, 4, 4, getRandomUrukWeapon(random));
        placeWeaponRack(world, 15, 2, 7, 6, getRandomUrukWeapon(random));
        placeWeaponRack(world, 16, 2, 8, 5, getRandomUrukWeapon(random));
        placeWeaponRack(world, 14, 2, 8, 7, getRandomUrukWeapon(random));
        placeWeaponRack(world, 15, 2, 9, 4, getRandomUrukWeapon(random));
        placeWeaponRack(world, 6, 5, 10, 4, getRandomUrukWeapon(random));
        placeWeaponRack(world, 1, 5, 10, 4, getRandomUrukWeapon(random));
        placeWeaponRack(world, 10, 5, 12, 4, getRandomUrukWeapon(random));
        placeWeaponRack(world, -3, 5, 12, 4, getRandomUrukWeapon(random));
        placeWeaponRack(world, 6, 6, 10, 4, getRandomUrukWeapon(random));
        placeWeaponRack(world, 1, 6, 10, 4, getRandomUrukWeapon(random));
        placeWeaponRack(world, 10, 6, 12, 4, getRandomUrukWeapon(random));
        placeWeaponRack(world, -3, 6, 12, 4, getRandomUrukWeapon(random));
        placeArmorStand(world, 15, 1, -9, 2, new ItemStack[]{LOTRLegacyItems.modStack("helmetGondor", 1, 0), LOTRLegacyItems.modStack("bodyGondorGambeson", 1, 0), null, null});
        placeUrukArmor(world, random, 8, 4, 10, 2);
        placeUrukArmor(world, random, -1, 4, 10, 2);
        placeUrukArmor(world, random, 7, 4, 12, 3);
        placeUrukArmor(world, random, 0, 4, 12, 1);
        placeUrukArmor(world, random, 6, 4, 13, 2);
        placeUrukArmor(world, random, 1, 4, 13, 2);
        placeChest(world, random, 15, 1, 7, 2, LOTRChestContents.BLACK_URUK_FORT);
        placeChest(world, random, 9, 4, 11, 3, LOTRChestContents.BLACK_URUK_FORT);
        placeChest(world, random, -2, 4, 11, 3, LOTRChestContents.BLACK_URUK_FORT);
        placeChest(world, random, 12, 4, 13, 5, LOTRChestContents.BLACK_URUK_FORT);
        placeChest(world, random, -5, 4, 13, 4, LOTRChestContents.BLACK_URUK_FORT);
        placeChest(world, random, 12, 4, 17, 5, LOTRChestContents.BLACK_URUK_FORT);
        placeChest(world, random, -5, 4, 17, 4, LOTRChestContents.BLACK_URUK_FORT);
        for (int j1 = 4; j1 <= 5; ++j1) {
            for (int i1 : new int[]{-3, -1, 1}) {
                setBlockAndMetadata(world, i1, j1, 17, bedBlock, 0);
                setBlockAndMetadata(world, i1, j1, 18, bedBlock, 8);
            }
            for (int i1 : new int[]{6, 8, 10}) {
                setBlockAndMetadata(world, i1, j1, 17, bedBlock, 0);
                setBlockAndMetadata(world, i1, j1, 18, bedBlock, 8);
            }
        }
        placeBarrel(world, random, -11, 5, -3, 3, LOTRFoods.ORC_DRINK);
        placeBarrel(world, random, -13, 5, -3, 3, LOTRFoods.ORC_DRINK);
        for (int k1 = -7; k1 <= -4; ++k1) {
            for (int i1 : new int[]{-13, -11}) {
                if (random.nextBoolean()) {
                    placePlate(world, random, i1, 5, k1, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRFoods.ORC);
                    continue;
                }
                placeMug(world, random, i1, 5, k1, random.nextInt(4), LOTRFoods.ORC_DRINK);
            }
        }
        LOTRBlackUrukCaptainEntity captain = create(LOTREntities.BLACK_URUK_CAPTAIN, world);
        captain.spawnRidingHorse = false;
        spawnNPCAndSetHome(captain, world, 0, 1, 0, 8);
        int uruks = 12;
        for (int l = 0; l < uruks; ++l) {
            LOTRBlackUrukEntity uruk = random.nextInt(3) == 0 ? create(LOTREntities.BLACK_URUK_ARCHER, world) : create(LOTREntities.BLACK_URUK, world);
            uruk.spawnRidingHorse = false;
            spawnNPCAndSetHome(uruk, world, 0, 1, 0, 32);
        }
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClasses(LOTREntities.BLACK_URUK, LOTREntities.BLACK_URUK_ARCHER);
        respawner.setCheckRanges(32, -16, 20, 24);
        respawner.setSpawnRanges(24, -4, 8, 24);
        placeNPCRespawner(respawner, world, 0, 0, 0);
        return true;
    }

    public ItemStack getRandomUrukWeapon(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("scimitarBlackUruk", 1, 0), LOTRLegacyItems.modStack("daggerBlackUruk", 1, 0), LOTRLegacyItems.modStack("daggerBlackUrukPoisoned", 1, 0), LOTRLegacyItems.modStack("spearBlackUruk", 1, 0), LOTRLegacyItems.modStack("battleaxeBlackUruk", 1, 0), LOTRLegacyItems.modStack("hammerBlackUruk", 1, 0), LOTRLegacyItems.modStack("blackUrukBow", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public void placeUrukArmor(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta) {
        ItemStack[] armor = random.nextInt(4) != 0 ? new ItemStack[]{null, null, null, null} : new ItemStack[]{LOTRLegacyItems.modStack("helmetBlackUruk", 1, 0), LOTRLegacyItems.modStack("bodyBlackUruk", 1, 0), LOTRLegacyItems.modStack("legsBlackUruk", 1, 0), LOTRLegacyItems.modStack("bootsBlackUruk", 1, 0)};
        placeArmorStand(world, i, j, k, meta, armor);
    }
}
