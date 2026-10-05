package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorBakerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorBlacksmithEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorBrewerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorButcherEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorFarmerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorFishmongerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorHunterEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorLumbermanEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorMasonEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorMinerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRHarnedorMarketStructure extends LOTRHarnedorStructure {
    public static Class<?>[] stalls = {Brewer.class, Fish.class, Butcher.class, Baker.class, Lumber.class, Miner.class, Mason.class, Hunter.class, Blacksmith.class, Farmer.class};

    public LOTRHarnedorMarketStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j2;
        int k1;
        int i1;
        int j1;
        int k12;
        int i12;
        setOriginAndRotation(world, i, j, k, rotation, 8);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i13 = -9; i13 <= 9; ++i13) {
                for (int k13 = -9; k13 <= 9; ++k13) {
                    j1 = getTopBlock(world, i13, k13) - 1;
                    if (!isSurface(world, i13, j1, k13)) {
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
        for (int i14 = -8; i14 <= 8; ++i14) {
            for (int k14 = -8; k14 <= 8; ++k14) {
                int i2 = Math.abs(i14);
                int k2 = Math.abs(k14);
                if ((i2 > 6 || k2 > 6) && (i2 != 7 || k2 > 4) && (k2 != 7 || i2 > 4) && (i2 != 8 || k2 > 1) && (k2 != 8 || i2 > 1)) {
                    continue;
                }
                for (j1 = 1; j1 <= 8; ++j1) {
                    setAir(world, i14, j1, k14);
                }
                j1 = -1;
                while (!isOpaque(world, i14, j1, k14) && getY(j1) >= 0) {
                    setBlockAndMetadata(world, i14, j1, k14, plank2Block, plank2Meta);
                    setGrassToDirt(world, i14, j1 - 1, k14);
                    --j1;
                }
            }
        }
        loadStrScan("harnedor_market");
        associateBlockMetaAlias("WOOD", woodBlock, woodMeta);
        associateBlockMetaAlias("WOOD|12", woodBlock, woodMeta | 0xC);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockAlias("PLANK_STAIR", plankStairBlock);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockMetaAlias("PLANK2", plank2Block, plank2Meta);
        associateBlockMetaAlias("ROOF", roofBlock, roofMeta);
        generateStrScan(world, random, 0, 1, 0);
        placeWallBanner(world, 0, 5, -2, "NEAR_HARAD", 2);
        placeWallBanner(world, 0, 5, 2, "NEAR_HARAD", 0);
        placeWallBanner(world, -2, 5, 0, "NEAR_HARAD", 3);
        placeWallBanner(world, 2, 5, 0, "NEAR_HARAD", 1);
        spawnItemFrame(world, 2, 2, -3, 3, getHarnedorFramedItem(random));
        spawnItemFrame(world, -2, 2, 3, 1, getHarnedorFramedItem(random));
        placeWeaponRack(world, -3, 2, 1, 6, getRandomHarnedorWeapon(random));
        placeArmorStand(world, 2, 1, -2, 2, new ItemStack[]{LOTRLegacyItems.modStack("helmetHarnedor", 1, 0), null, null, null});
        placeFlowerPot(world, -2, 2, 2, getRandomFlower(world, random));
        placeAnimalJar(world, 2, 1, 1, LOTRLegacyBlocks.mod("butterflyJar"), 0, create(LOTREntities.BUTTERFLY, world));
        placeAnimalJar(world, -3, 1, -1, LOTRLegacyBlocks.mod("birdCageWood"), 0, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, -2, 3, -2, LOTRLegacyBlocks.mod("birdCage"), 0, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, 6, 3, 1, LOTRLegacyBlocks.mod("birdCage"), 0, create(LOTREntities.BIRD, world));
        placeSkull(world, random, 2, 4, -5);
        List<Class<?>> stallClasses = new ArrayList<>(Arrays.asList(stalls));
        while (stallClasses.size() > 4) {
            stallClasses.remove(random.nextInt(stallClasses.size()));
        }
        try {
            LOTRStructureBase2 stall0 = (LOTRStructureBase2) stallClasses.get(0).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall1 = (LOTRStructureBase2) stallClasses.get(1).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall2 = (LOTRStructureBase2) stallClasses.get(2).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall3 = (LOTRStructureBase2) stallClasses.get(3).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            generateSubstructure(stall0, world, random, 2, 1, 2, 0);
            generateSubstructure(stall1, world, random, 2, 1, -2, 1);
            generateSubstructure(stall2, world, random, -2, 1, -2, 2);
            generateSubstructure(stall3, world, random, -2, 1, 2, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            int j12;
            for (int step = 0; step < 12 && !isOpaque(world, i1, j12 = -step, k12 = -9 - step); ++step) {
                setBlockAndMetadata(world, i1, j12, k12, plank2StairBlock, 2);
                setGrassToDirt(world, i1, j12 - 1, k12);
                j2 = j12 - 1;
                while (!isOpaque(world, i1, j2, k12) && getY(j2) >= 0) {
                    setBlockAndMetadata(world, i1, j2, k12, plank2Block, plank2Meta);
                    setGrassToDirt(world, i1, j2 - 1, k12);
                    --j2;
                }
            }
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            int j13;
            for (int step = 0; step < 12 && !isOpaque(world, i1, j13 = -step, k12 = 9 + step); ++step) {
                setBlockAndMetadata(world, i1, j13, k12, plank2StairBlock, 3);
                setGrassToDirt(world, i1, j13 - 1, k12);
                j2 = j13 - 1;
                while (!isOpaque(world, i1, j2, k12) && getY(j2) >= 0) {
                    setBlockAndMetadata(world, i1, j2, k12, plank2Block, plank2Meta);
                    setGrassToDirt(world, i1, j2 - 1, k12);
                    --j2;
                }
            }
        }
        for (k1 = -1; k1 <= 1; ++k1) {
            int j14;
            for (int step = 0; step < 12 && !isOpaque(world, i12 = -9 - step, j14 = -step, k1); ++step) {
                setBlockAndMetadata(world, i12, j14, k1, plank2StairBlock, 1);
                setGrassToDirt(world, i12, j14 - 1, k1);
                j2 = j14 - 1;
                while (!isOpaque(world, i12, j2, k1) && getY(j2) >= 0) {
                    setBlockAndMetadata(world, i12, j2, k1, plank2Block, plank2Meta);
                    setGrassToDirt(world, i12, j2 - 1, k1);
                    --j2;
                }
            }
        }
        for (k1 = -1; k1 <= 1; ++k1) {
            int j15;
            for (int step = 0; step < 12 && !isOpaque(world, i12 = 9 + step, j15 = -step, k1); ++step) {
                setBlockAndMetadata(world, i12, j15, k1, plank2StairBlock, 0);
                setGrassToDirt(world, i12, j15 - 1, k1);
                j2 = j15 - 1;
                while (!isOpaque(world, i12, j2, k1) && getY(j2) >= 0) {
                    setBlockAndMetadata(world, i12, j2, k1, plank2Block, plank2Meta);
                    setGrassToDirt(world, i12, j2 - 1, k1);
                    --j2;
                }
            }
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
            placeFlowerPot(world, 2, 2, 0, getRandomFlower(world, random));
            placePlate_item(world, random, 2, 2, 0, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("oliveBread", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 0, 2, 2, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRLegacyItems.vanillaStack("bread", 1 + random.nextInt(3), 0), true);
            setBlockAndMetadata(world, 0, 2, 4, LOTRLegacyBlocks.mod("lemonCake"), 0);
            setBlockAndMetadata(world, 3, 1, 3, LOTRLegacyBlocks.mod("woodSlabSingle4"), 15);
            setBlockAndMetadata(world, 3, 2, 3, LOTRLegacyBlocks.mod("marzipanBlock"), 0);
            placeWeaponRack(world, 2, 2, 4, 7, LOTRLegacyItems.modStack("rollingPin", 1, 0));
            LOTRHarnedorBakerEntity trader = create(LOTREntities.HARNEDOR_BAKER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 2, 4);
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
            placeWeaponRack(world, 3, 2, 0, 2, new LOTRHarnedorMarketStructure(false).getRandomHarnedorWeapon(random));
            placeWeaponRack(world, 0, 2, 4, 3, new LOTRHarnedorMarketStructure(false).getRandomHarnedorWeapon(random));
            placeFlowerPot(world, 0, 2, 2, getRandomFlower(world, random));
            setBlockAndMetadata(world, 3, 1, 3, LOTRLegacyBlocks.vanilla("anvil"), 1);
            placeArmorStand(world, 4, 1, 2, 0, new ItemStack[]{LOTRLegacyItems.modStack("helmetHarnedor", 1, 0), LOTRLegacyItems.modStack("bodyHarnedor", 1, 0), null, null});
            placeArmorStand(world, 2, 1, 4, 1, null);
            LOTRHarnedorBlacksmithEntity trader = create(LOTREntities.HARNEDOR_BLACKSMITH, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 2, 4);
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
            placeMug(world, random, 3, 2, 0, 0, LOTRFoods.HARNEDOR_DRINK);
            placeMug(world, random, 0, 2, 2, 1, LOTRFoods.HARNEDOR_DRINK);
            setBlockAndMetadata(world, 0, 2, 4, LOTRLegacyBlocks.mod("barrel"), 4);
            setBlockAndMetadata(world, 3, 1, 3, LOTRLegacyBlocks.mod("woodSlabSingle4"), 15);
            setBlockAndMetadata(world, 3, 2, 3, LOTRLegacyBlocks.mod("barrel"), 2);
            LOTRHarnedorBrewerEntity trader = create(LOTREntities.HARNEDOR_BREWER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 2, 4);
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
            placePlate_item(world, random, 2, 2, 0, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRLegacyItems.modStack("camelRaw", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 0, 2, 2, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("kebab", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 0, 2, 4, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("kebab", 1 + random.nextInt(3), 0), true);
            setBlockAndMetadata(world, 3, 1, 3, LOTRLegacyBlocks.vanilla("furnace"), 2);
            placeKebabStand(world, random, 3, 2, 3, LOTRLegacyBlocks.mod("kebabStand"), 2);
            setBlockAndMetadata(world, 2, 3, 3, LOTRLegacyBlocks.mod("kebabBlock"), 0);
            setBlockAndMetadata(world, 2, 4, 3, LOTRLegacyBlocks.mod("fence2"), 2);
            setBlockAndMetadata(world, 2, 5, 3, LOTRLegacyBlocks.mod("fence2"), 2);
            LOTRHarnedorButcherEntity trader = create(LOTREntities.HARNEDOR_BUTCHER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 2, 4);
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
            setBlockAndMetadata(world, 2, 1, 4, LOTRLegacyBlocks.vanilla("hay_block"), 0);
            setBlockAndMetadata(world, 3, 1, 3, LOTRLegacyBlocks.vanilla("hay_block"), 0);
            setBlockAndMetadata(world, 3, 1, 2, LOTRLegacyBlocks.mod("berryBush"), 9);
            setBlockAndMetadata(world, 4, 1, 2, LOTRLegacyBlocks.mod("berryBush"), 9);
            placePlate_item(world, random, 3, 2, 0, LOTRLegacyBlocks.mod("woodPlateBlock"), getRandomFarmFood(random), true);
            placePlate_item(world, random, 0, 2, 2, LOTRLegacyBlocks.mod("woodPlateBlock"), getRandomFarmFood(random), true);
            placePlate_item(world, random, 0, 2, 4, LOTRLegacyBlocks.mod("woodPlateBlock"), getRandomFarmFood(random), true);
            LOTRHarnedorFarmerEntity trader = create(LOTREntities.HARNEDOR_FARMER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 2, 4);
            return true;
        }

        public ItemStack getRandomFarmFood(RandomSource random) {
            ItemStack[] items = {LOTRLegacyItems.modStack("orange", 1, 0), LOTRLegacyItems.modStack("lemon", 1, 0), LOTRLegacyItems.modStack("lime", 1, 0), LOTRLegacyItems.vanillaStack("carrot", 1, 0), LOTRLegacyItems.vanillaStack("potato", 1, 0), LOTRLegacyItems.modStack("lettuce", 1, 0), LOTRLegacyItems.modStack("turnip", 1, 0)};
            ItemStack ret = items[random.nextInt(items.length)].copy();
            ret.setCount(1 + random.nextInt(3));
            return ret;
        }
    }

    public static class Fish extends LOTRStructureBase2 {
        public Fish(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            placePlate_item(world, random, 2, 2, 0, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRLegacyItems.vanillaStack("fish", 1 + random.nextInt(3), 1), true);
            placePlate_item(world, random, 0, 2, 3, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.vanillaStack("fish", 1 + random.nextInt(3), 0), true);
            placeFlowerPot(world, 0, 2, 4, getRandomFlower(world, random));
            setBlockAndMetadata(world, 3, 1, 3, LOTRLegacyBlocks.mod("woodSlabSingle4"), 15);
            placePlate_item(world, random, 3, 2, 3, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.vanillaStack("fish", 1 + random.nextInt(3), 0), true);
            setBlockAndMetadata(world, 2, 1, 4, LOTRLegacyBlocks.vanilla("cauldron"), 3);
            placeWeaponRack(world, 4, 2, 2, 6, LOTRLegacyItems.vanillaStack("fishing_rod", 1, 0));
            LOTRHarnedorFishmongerEntity trader = create(LOTREntities.HARNEDOR_FISHMONGER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 2, 4);
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
            placePlate_item(world, random, 2, 2, 0, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("camelRaw", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 0, 2, 3, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("rabbitRaw", 1 + random.nextInt(3), 0), true);
            setBlockAndMetadata(world, 3, 1, 3, LOTRLegacyBlocks.mod("woodSlabSingle4"), 15);
            placePlate_item(world, random, 3, 2, 3, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("deerRaw", 1 + random.nextInt(3), 0), true);
            spawnItemFrame(world, 4, 2, 3, 2, LOTRLegacyItems.modStack("fur", 1, 0));
            spawnItemFrame(world, 3, 2, 4, 3, LOTRLegacyItems.vanillaStack("leather", 1, 0));
            LOTRHarnedorHunterEntity trader = create(LOTREntities.HARNEDOR_HUNTER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 2, 4);
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
            placeFlowerPot(world, 2, 2, 0, LOTRLegacyBlocks.mod("sapling4").stack(1, 2));
            placeFlowerPot(world, 0, 2, 2, LOTRLegacyBlocks.mod("sapling8").stack(1, 3));
            placeFlowerPot(world, 0, 2, 4, LOTRLegacyBlocks.mod("sapling7").stack(1, 3));
            setBlockAndMetadata(world, 3, 1, 3, LOTRLegacyBlocks.mod("wood8"), 3);
            setBlockAndMetadata(world, 3, 2, 3, LOTRLegacyBlocks.mod("wood8"), 3);
            setBlockAndMetadata(world, 2, 1, 4, LOTRLegacyBlocks.mod("wood6"), 3);
            setBlockAndMetadata(world, 2, 1, 3, LOTRLegacyBlocks.mod("wood6"), 11);
            setBlockAndMetadata(world, 4, 1, 2, LOTRLegacyBlocks.mod("woodBeam8"), 11);
            placeWeaponRack(world, 2, 2, 4, 7, LOTRLegacyItems.modStack("axeBronze", 1, 0));
            LOTRHarnedorLumbermanEntity trader = create(LOTREntities.HARNEDOR_LUMBERMAN, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 2, 4);
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
            placeFlowerPot(world, 2, 2, 0, getRandomFlower(world, random));
            placeWeaponRack(world, 0, 2, 3, 3, LOTRLegacyItems.modStack("pickaxeBronze", 1, 0));
            setBlockAndMetadata(world, 4, 1, 2, LOTRLegacyBlocks.vanilla("sandstone"), 0);
            setBlockAndMetadata(world, 2, 1, 3, LOTRLegacyBlocks.vanilla("sandstone"), 0);
            setBlockAndMetadata(world, 3, 1, 3, LOTRLegacyBlocks.mod("redSandstone"), 0);
            setBlockAndMetadata(world, 3, 2, 3, LOTRLegacyBlocks.mod("redSandstone"), 0);
            setBlockAndMetadata(world, 2, 1, 4, LOTRLegacyBlocks.mod("redSandstone"), 0);
            LOTRHarnedorMasonEntity trader = create(LOTREntities.HARNEDOR_MASON, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 2, 4);
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
            placeWeaponRack(world, 2, 2, 0, 2, LOTRLegacyItems.modStack("pickaxeBronze", 1, 0));
            placeWeaponRack(world, 0, 2, 3, 3, LOTRLegacyItems.modStack("shovelBronze", 1, 0));
            setBlockAndMetadata(world, 4, 1, 2, LOTRLegacyBlocks.mod("oreCopper"), 0);
            setBlockAndMetadata(world, 2, 1, 3, LOTRLegacyBlocks.mod("oreCopper"), 0);
            setBlockAndMetadata(world, 3, 1, 3, LOTRLegacyBlocks.mod("oreTin"), 0);
            setBlockAndMetadata(world, 3, 2, 3, LOTRLegacyBlocks.mod("oreCopper"), 0);
            setBlockAndMetadata(world, 2, 1, 4, LOTRLegacyBlocks.mod("oreTin"), 0);
            LOTRHarnedorMinerEntity trader = create(LOTREntities.HARNEDOR_MINER, world);
            spawnNPCAndSetHome(trader, world, 2, 1, 2, 4);
            return true;
        }
    }

}
