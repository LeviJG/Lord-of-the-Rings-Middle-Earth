package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfBakerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfBlacksmithEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfBrewerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfButcherEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfFarmerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfFishmongerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfGoldsmithEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfHaradWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfHunterEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfLumbermanEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfMasonEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfMinerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRGulfBazaarStructure extends LOTRGulfStructure {
    public static Class<?>[] stalls = {Mason.class, Butcher.class, Brewer.class, Fish.class, Baker.class, Miner.class, Goldsmith.class, Lumber.class, Hunter.class, Blacksmith.class, Farmer.class};

    public LOTRGulfBazaarStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        setOriginAndRotation(world, i, j, k, rotation, 8);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -17; i1 <= 17; ++i1) {
                for (int k1 = -12; k1 <= 8; ++k1) {
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
                    if (maxHeight - minHeight <= 12) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (int i1 = -17; i1 <= 17; ++i1) {
            for (int k1 = -12; k1 <= 8; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                if (i2 >= 5 && i2 <= 9 && k2 >= 10) {
                    for (j1 = 1; j1 <= 5; ++j1) {
                        setAir(world, i1, j1, k1);
                    }
                }
                if ((k1 < -6 || k1 > -5 || i2 > 4) && (k1 != -5 || i2 < 10 || i2 > 12) && (k2 != 4 || i2 > 14) && (k2 < 2 || k2 > 3 || i2 > 15) && (k2 > 1 || i2 > 16) && (k1 != 5 || i2 > 12) && (k1 != 6 || i2 > 9) && (k1 != 7 || i2 > 4)) {
                    continue;
                }
                for (j1 = 1; j1 <= 6; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("gulf_bazaar");
        addBlockMetaAliasOption("BRICK", 6, brickBlock, brickMeta);
        addBlockMetaAliasOption("BRICK", 2, LOTRLegacyBlocks.mod("brick3"), 11);
        addBlockMetaAliasOption("BRICK", 8, LOTRLegacyBlocks.vanilla("sandstone"), 0);
        addBlockAliasOption("BRICK_STAIR", 6, brickStairBlock);
        addBlockAliasOption("BRICK_STAIR", 2, LOTRLegacyBlocks.mod("stairsNearHaradBrickCracked"));
        addBlockAliasOption("BRICK_STAIR", 8, LOTRLegacyBlocks.vanilla("sandstone_stairs"));
        addBlockMetaAliasOption("BRICK_WALL", 6, brickWallBlock, brickWallMeta);
        addBlockMetaAliasOption("BRICK_WALL", 2, LOTRLegacyBlocks.mod("wall3"), 3);
        addBlockMetaAliasOption("BRICK_WALL", 8, LOTRLegacyBlocks.mod("wallStoneV"), 4);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockAlias("PLANK_STAIR", plankStairBlock);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockMetaAlias("BEAM", beamBlock, beamMeta);
        associateBlockMetaAlias("BEAM2", beam2Block, beam2Meta);
        associateBlockMetaAlias("BEAM2|4", beam2Block, beam2Meta | 4);
        associateBlockMetaAlias("BEAM2|8", beam2Block, beam2Meta | 8);
        addBlockMetaAliasOption("GROUND", 10, LOTRLegacyBlocks.vanilla("sand"), 0);
        addBlockMetaAliasOption("GROUND", 3, LOTRLegacyBlocks.vanilla("dirt"), 1);
        addBlockMetaAliasOption("GROUND", 2, LOTRLegacyBlocks.mod("dirtPath"), 0);
        associateBlockMetaAlias("WOOL", LOTRLegacyBlocks.vanilla("wool"), 14);
        associateBlockMetaAlias("CARPET", LOTRLegacyBlocks.vanilla("carpet"), 14);
        associateBlockMetaAlias("WOOL2", LOTRLegacyBlocks.vanilla("wool"), 15);
        associateBlockMetaAlias("CARPET2", LOTRLegacyBlocks.vanilla("carpet"), 15);
        associateBlockMetaAlias("BONE", boneBlock, boneMeta);
        generateStrScan(world, random, 0, 0, 0);
        placeAnimalJar(world, -5, 4, -2, LOTRLegacyBlocks.mod("birdCageWood"), 0, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, -7, 5, 0, LOTRLegacyBlocks.mod("birdCageWood"), 0, create(LOTREntities.BIRD, world));
        placeWallBanner(world, -3, 4, -7, "HARAD_GULF", 2);
        placeWallBanner(world, 3, 4, -7, "HARAD_GULF", 2);
        placeWallBanner(world, -7, 10, -8, "HARAD_GULF", 2);
        placeWallBanner(world, -7, 10, -6, "HARAD_GULF", 0);
        placeWallBanner(world, -8, 10, -7, "HARAD_GULF", 3);
        placeWallBanner(world, -6, 10, -7, "HARAD_GULF", 1);
        placeWallBanner(world, 7, 10, -8, "HARAD_GULF", 2);
        placeWallBanner(world, 7, 10, -6, "HARAD_GULF", 0);
        placeWallBanner(world, 6, 10, -7, "HARAD_GULF", 3);
        placeWallBanner(world, 8, 10, -7, "HARAD_GULF", 1);
        for (int i1 : new int[]{-7, 7}) {
            j1 = 1;
            int k1 = -11;
            LOTRGulfHaradWarriorEntity guard = create(LOTREntities.GULF_WARRIOR, world);
            guard.spawnRidingHorse = false;
            spawnNPCAndSetHome(guard, world, i1, j1, k1, 4);
        }
        List<Class<?>> stallClasses = new ArrayList<>(Arrays.asList(stalls));
        while (stallClasses.size() > 5) {
            stallClasses.remove(random.nextInt(stallClasses.size()));
        }
        try {
            LOTRStructureBase2 stall0 = (LOTRStructureBase2) stallClasses.get(0).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall1 = (LOTRStructureBase2) stallClasses.get(1).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall2 = (LOTRStructureBase2) stallClasses.get(2).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall3 = (LOTRStructureBase2) stallClasses.get(3).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall4 = (LOTRStructureBase2) stallClasses.get(4).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            generateSubstructure(stall0, world, random, -9, 1, 0, 3);
            generateSubstructure(stall1, world, random, -5, 1, 5, 1);
            generateSubstructure(stall2, world, random, 0, 1, 6, 1);
            generateSubstructure(stall3, world, random, 8, 1, 2, 3);
            generateSubstructure(stall4, world, random, 11, 1, -2, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static class Baker extends LOTRStructureBase2 {
        public Baker(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 2, 1, 2, LOTRLegacyBlocks.vanilla("furnace"), 2);
            setBlockAndMetadata(world, 1, 1, 2, LOTRLegacyBlocks.mod("chestBasket"), 2);
            placePlate_item(world, random, 1, 2, 0, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRLegacyItems.vanillaStack("bread", 1 + random.nextInt(3), 0), true);
            setBlockAndMetadata(world, 3, 2, 2, LOTRLegacyBlocks.mod("bananaCake"), 0);
            placeWeaponRack(world, 0, 2, 2, 1, LOTRLegacyItems.modStack("rollingPin", 1, 0));
            LOTRGulfBakerEntity trader = create(LOTREntities.GULF_BAKER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

    public static class Blacksmith extends LOTRStructureBase2 {
        public Blacksmith(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 2, 1, 2, LOTRLegacyBlocks.vanilla("anvil"), 3);
            placeArmorStand(world, 1, 1, 2, 0, new ItemStack[]{null, LOTRLegacyItems.modStack("bodyGulfHarad", 1, 0), null, null});
            placeWeaponRack(world, 0, 2, 2, 1, new LOTRGulfBazaarStructure(false).getRandomGulfWeapon(random));
            placeWeaponRack(world, 3, 2, 2, 3, new LOTRGulfBazaarStructure(false).getRandomGulfWeapon(random));
            LOTRGulfBlacksmithEntity trader = create(LOTREntities.GULF_BLACKSMITH, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

    public static class Brewer extends LOTRStructureBase2 {
        public Brewer(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("barrel"), 3);
            placeMug(world, random, 1, 2, 0, 0, LOTRFoods.GULF_HARAD_DRINK);
            placeMug(world, random, 0, 2, 2, 3, LOTRFoods.GULF_HARAD_DRINK);
            placeMug(world, random, 3, 2, 1, 1, LOTRFoods.GULF_HARAD_DRINK);
            placeFlowerPot(world, 2, 2, 3, getRandomFlower(world, random));
            LOTRGulfBrewerEntity trader = create(LOTREntities.GULF_BREWER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

    public static class Butcher extends LOTRStructureBase2 {
        public Butcher(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            placePlate_item(world, random, 1, 2, 0, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("rabbitRaw", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 0, 2, 2, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("camelRaw", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 3, 2, 1, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("muttonRaw", 1 + random.nextInt(3), 0), true);
            placeSkull(world, random, 2, 2, 3);
            LOTRGulfButcherEntity trader = create(LOTREntities.GULF_BUTCHER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

    public static class Farmer extends LOTRStructureBase2 {
        public Farmer(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 2, 1, 2, LOTRLegacyBlocks.vanilla("cauldron"), 3);
            setBlockAndMetadata(world, 1, 2, 3, LOTRLegacyBlocks.vanilla("hay_block"), 0);
            placePlate_item(world, random, 3, 2, 1, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("orange", 1 + random.nextInt(3), 0), true);
            placeFlowerPot(world, 0, 2, 2, getRandomFlower(world, random));
            LOTRGulfFarmerEntity trader = create(LOTREntities.GULF_FARMER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

    public static class Fish extends LOTRStructureBase2 {
        public Fish(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 2, 1, 2, LOTRLegacyBlocks.vanilla("cauldron"), 3);
            placePlate_item(world, random, 1, 2, 0, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.vanillaStack("fish", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 0, 2, 2, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.vanillaStack("fish", 1 + random.nextInt(3), 1), true);
            placePlate_item(world, random, 3, 2, 1, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.vanillaStack("fish", 1 + random.nextInt(3), 0), true);
            placeWeaponRack(world, 1, 2, 3, 0, LOTRLegacyItems.vanillaStack("fishing_rod", 1, 0));
            LOTRGulfFishmongerEntity trader = create(LOTREntities.GULF_FISHMONGER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

    public static class Goldsmith extends LOTRStructureBase2 {
        public Goldsmith(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 2, 2, 2, LOTRLegacyBlocks.mod("birdCage"), 3);
            setBlockAndMetadata(world, 2, 3, 2, LOTRLegacyBlocks.mod("goldBars"), 0);
            placeFlowerPot(world, 0, 2, 1, getRandomFlower(world, random));
            LOTRGulfGoldsmithEntity trader = create(LOTREntities.GULF_GOLDSMITH, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

    public static class Hunter extends LOTRStructureBase2 {
        public Hunter(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 2, 1, 2, LOTRLegacyBlocks.mod("wood8"), 3);
            setBlockAndMetadata(world, 2, 2, 2, LOTRLegacyBlocks.mod("wood8"), 3);
            placeSkull(world, random, 2, 3, 2);
            placeSkull(world, random, 3, 2, 2);
            spawnItemFrame(world, 2, 2, 2, 2, LOTRLegacyItems.modStack("lionFur", 1, 0));
            placePlate_item(world, random, 1, 2, 0, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("rabbitRaw", 1 + random.nextInt(3), 0), true);
            placeWeaponRack(world, 0, 2, 2, 1, LOTRLegacyItems.modStack("spearHarad", 1, 0));
            LOTRGulfHunterEntity trader = create(LOTREntities.GULF_HUNTER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

    public static class Lumber extends LOTRStructureBase2 {
        public Lumber(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 2, 1, 2, LOTRLegacyBlocks.mod("wood8"), 3);
            setBlockAndMetadata(world, 2, 2, 2, LOTRLegacyBlocks.mod("wood8"), 3);
            placeFlowerPot(world, 0, 2, 2, LOTRLegacyBlocks.vanilla("sapling").stack(1, 4));
            placeFlowerPot(world, 3, 2, 1, LOTRLegacyBlocks.mod("sapling8").stack(1, 3));
            LOTRGulfLumbermanEntity trader = create(LOTREntities.GULF_LUMBERMAN, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

    public static class Mason extends LOTRStructureBase2 {
        public Mason(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 2, 1, 2, LOTRLegacyBlocks.mod("brick"), 15);
            setBlockAndMetadata(world, 2, 2, 2, LOTRLegacyBlocks.mod("brick3"), 13);
            placeFlowerPot(world, 0, 2, 2, getRandomFlower(world, random));
            placeWeaponRack(world, 3, 2, 2, 3, LOTRLegacyItems.modStack("pickaxeBronze", 1, 0));
            LOTRGulfMasonEntity trader = create(LOTREntities.GULF_MASON, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

    public static class Miner extends LOTRStructureBase2 {
        public Miner(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 1, 1, 2, LOTRLegacyBlocks.mod("chestBasket"), 2);
            setBlockAndMetadata(world, 2, 1, 2, LOTRLegacyBlocks.mod("oreTin"), 0);
            setBlockAndMetadata(world, 2, 2, 2, LOTRLegacyBlocks.mod("oreCopper"), 0);
            placeWeaponRack(world, 0, 2, 2, 1, LOTRLegacyItems.modStack("pickaxeBronze", 1, 0));
            LOTRGulfMinerEntity trader = create(LOTREntities.GULF_MINER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 1, 4);
            return true;
        }
    }

}
