package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradBlacksmithEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarBakerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarBrewerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarButcherEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarFarmerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarFishmongerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarFloristEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarGoldsmithEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarLumbermanEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarMasonEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarMinerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRUmbarBazaarStructure extends LOTRSouthronBazaarStructure {
    public static Class<?>[] stallsUmbar = {Lumber.class, Mason.class, Fish.class, Baker.class, Goldsmith.class, Farmer.class, Blacksmith.class, Brewer.class, Miner.class, Florist.class, Butcher.class};

    public LOTRUmbarBazaarStructure(boolean flag) {
        super(flag);
    }

    @Override
    public Class<?>[] getStallClasses() {
        return stallsUmbar;
    }

    @Override
    public boolean isUmbar() {
        return true;
    }

    public static class Baker extends LOTRStructureBase2 {
        public Baker(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 0, 1, 1, LOTRLegacyBlocks.vanilla("furnace"), 2);
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("planks2"), 2);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("planks2"), 2);
            placePlate_item(world, random, -1, 2, 1, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRLegacyItems.vanillaStack("bread", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 1, 2, 1, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRLegacyItems.modStack("oliveBread", 1 + random.nextInt(3), 0), true);
            placeFlowerPot(world, random.nextBoolean() ? -2 : 2, 2, 0, getRandomFlower(world, random));
            LOTRUmbarBakerEntity trader = create(LOTREntities.UMBAR_BAKER, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
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
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.vanilla("anvil"), 3);
            placeArmorStand(world, 1, 1, 1, 0, new ItemStack[]{LOTRLegacyItems.modStack("helmetNearHarad", 1, 0), LOTRLegacyItems.modStack("bodyNearHarad", 1, 0), null, null});
            placeWeaponRack(world, -2, 2, 0, 1, new LOTRUmbarBazaarStructure(false).getRandomHaradWeapon(random));
            placeWeaponRack(world, 2, 2, 0, 3, new LOTRUmbarBazaarStructure(false).getRandomHaradWeapon(random));
            LOTRNearHaradBlacksmithEntity trader = create(LOTREntities.NEAR_HARAD_BLACKSMITH, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
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
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("stairsCedar"), 6);
            setBlockAndMetadata(world, -1, 2, 1, LOTRLegacyBlocks.mod("barrel"), 2);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("stairsCedar"), 6);
            setBlockAndMetadata(world, 1, 2, 1, LOTRLegacyBlocks.mod("barrel"), 2);
            placeMug(world, random, -2, 2, 0, 1, LOTRFoods.SOUTHRON_DRINK);
            placeMug(world, random, 2, 2, 0, 1, LOTRFoods.SOUTHRON_DRINK);
            LOTRUmbarBrewerEntity trader = create(LOTREntities.UMBAR_BREWER, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
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
            setBlockAndMetadata(world, 0, 1, 1, LOTRLegacyBlocks.vanilla("furnace"), 2);
            placeKebabStand(world, random, 0, 2, 1, LOTRLegacyBlocks.mod("kebabStand"), 3);
            placePlate_item(world, random, -2, 2, 0, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRLegacyItems.modStack("muttonRaw", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 2, 2, 0, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRLegacyItems.modStack("camelRaw", 1 + random.nextInt(3), 1), true);
            LOTRUmbarButcherEntity trader = create(LOTREntities.UMBAR_BUTCHER, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
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
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.vanilla("cauldron"), 3);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.vanilla("hay_block"), 0);
            setBlockAndMetadata(world, -1, 1, -1, LOTRLegacyBlocks.vanilla("hay_block"), 0);
            placePlate_item(world, random, -2, 2, 0, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("orange", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 2, 2, 0, LOTRLegacyBlocks.mod("woodPlateBlock"), LOTRLegacyItems.modStack("lettuce", 1 + random.nextInt(3), 1), true);
            LOTRUmbarFarmerEntity trader = create(LOTREntities.UMBAR_FARMER, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
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
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.vanilla("cauldron"), 3);
            setBlockAndMetadata(world, -1, 1, -1, LOTRLegacyBlocks.vanilla("sponge"), 0);
            placePlate_item(world, random, -2, 2, 0, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRLegacyItems.vanillaStack("fish", 1 + random.nextInt(3), 0), true);
            placePlate_item(world, random, 2, 2, 0, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRLegacyItems.vanillaStack("fish", 1 + random.nextInt(3), 1), true);
            LOTRUmbarFishmongerEntity trader = create(LOTREntities.UMBAR_FISHMONGER, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
            return true;
        }
    }

    public static class Florist extends LOTRStructureBase2 {
        public Florist(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            placeFlowerPot(world, -2, 2, 0, getRandomFlower(world, random));
            placeFlowerPot(world, 2, 2, 0, getRandomFlower(world, random));
            setBlockAndMetadata(world, -1, 0, 1, LOTRLegacyBlocks.vanilla("grass"), 0);
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("doubleFlower"), 3);
            setBlockAndMetadata(world, -1, 2, 1, LOTRLegacyBlocks.mod("doubleFlower"), 11);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.vanilla("grass"), 0);
            plantFlower(world, random, 1, 2, 1);
            setBlockAndMetadata(world, 1, 1, 0, LOTRLegacyBlocks.vanilla("trapdoor"), 12);
            setBlockAndMetadata(world, 0, 1, 1, LOTRLegacyBlocks.vanilla("trapdoor"), 15);
            LOTRUmbarFloristEntity trader = create(LOTREntities.UMBAR_FLORIST, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
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
            setBlockAndMetadata(world, -1, 1, -1, LOTRLegacyBlocks.mod("goldBars"), 0);
            setBlockAndMetadata(world, 1, 1, -1, LOTRLegacyBlocks.mod("goldBars"), 0);
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("goldBars"), 0);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("goldBars"), 0);
            setBlockAndMetadata(world, random.nextBoolean() ? -1 : 1, 2, -1, LOTRLegacyBlocks.mod("birdCage"), 2);
            setBlockAndMetadata(world, random.nextBoolean() ? -1 : 1, 2, 1, LOTRLegacyBlocks.mod("birdCage"), 3);
            LOTRUmbarGoldsmithEntity trader = create(LOTREntities.UMBAR_GOLDSMITH, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
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
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("wood4"), 10);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("wood4"), 2);
            setBlockAndMetadata(world, 1, 2, 1, LOTRLegacyBlocks.mod("wood4"), 2);
            placeFlowerPot(world, -2, 2, 0, LOTRLegacyBlocks.mod("sapling4").stack(1, 2));
            placeFlowerPot(world, 2, 2, 0, LOTRLegacyBlocks.mod("sapling8").stack(1, 3));
            LOTRUmbarLumbermanEntity trader = create(LOTREntities.UMBAR_LUMBERMAN, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
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
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("brick6"), 6);
            setBlockAndMetadata(world, -1, 2, 1, LOTRLegacyBlocks.mod("slabSingle13"), 2);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("brick6"), 9);
            placeWeaponRack(world, 1, 2, 1, 2, LOTRLegacyItems.vanillaStack("iron_pickaxe", 1, 0));
            LOTRUmbarMasonEntity trader = create(LOTREntities.UMBAR_MASON, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
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
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("oreTin"), 0);
            setBlockAndMetadata(world, -1, 2, 1, LOTRLegacyBlocks.mod("oreCopper"), 0);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.vanilla("iron_ore"), 0);
            placeWeaponRack(world, 1, 2, 1, 2, LOTRLegacyItems.vanillaStack("iron_pickaxe", 1, 0));
            LOTRUmbarMinerEntity trader = create(LOTREntities.UMBAR_MINER, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
            return true;
        }
    }

}
