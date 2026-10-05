package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree;

import java.util.Arrays;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree.LOTRBreeGuardEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;

import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRBreeMarketStallStructure extends LOTRBreeStructure {
    public static Class<?>[] allStallTypes = {Baker.class, Butcher.class, Brewer.class, Mason.class, Lumber.class, Smith.class, Florist.class, Farmer.class};
    public LegacyBlock wool1Block;
    public int wool1Meta;
    public LegacyBlock wool2Block;
    public int wool2Meta;

    protected LOTRBreeMarketStallStructure(boolean flag) {
        super(flag);
    }

    public static LOTRBreeMarketStallStructure getRandomStall(RandomSource random, boolean flag) {
        try {
            Class<?> cls = allStallTypes[random.nextInt(allStallTypes.length)];
            return (LOTRBreeMarketStallStructure) cls.getConstructor(Boolean.TYPE).newInstance(flag);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LOTRBreeMarketStallStructure[] getRandomStalls(RandomSource random, boolean flag, int num) {
        List<Class<?>> types = Arrays.asList(Arrays.copyOf(allStallTypes, allStallTypes.length));
        Util.shuffle(types, random);
        LOTRBreeMarketStallStructure[] ret = new LOTRBreeMarketStallStructure[num];
        for (int i = 0; i < ret.length; ++i) {
            int listIndex = i % types.size();
            Class<?> cls = types.get(listIndex);
            try {
                ret[i] = (LOTRBreeMarketStallStructure) cls.getConstructor(Boolean.TYPE).newInstance(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public abstract LOTRNPCEntity createTrader(WorldGenLevel var1, RandomSource var2);

    public abstract void decorateStall(WorldGenLevel var1, RandomSource var2);

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k2;
        int k1;
        int i2;
        int j1;
        int i1;
        int step;
        int i12;
        int j2;
        int k12;
        int j12;
        setOriginAndRotation(world, i, j, k, rotation, 3);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (i1 = -3; i1 <= 3; ++i1) {
                for (int k13 = -3; k13 <= 3; ++k13) {
                    j12 = getTopBlock(world, i1, k13) - 1;
                    if (!isSurface(world, i1, j12, k13)) {
                        return false;
                    }
                    if (j12 < minHeight) {
                        minHeight = j12;
                    }
                    if (j12 > maxHeight) {
                        maxHeight = j12;
                    }
                    if (maxHeight - minHeight <= 6) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i12 = -2; i12 <= 2; ++i12) {
            for (k12 = -2; k12 <= 2; ++k12) {
                i2 = Math.abs(i12);
                k2 = Math.abs(k12);
                if (i2 == 2 && k2 == 2) {
                    for (j12 = 3; (j12 >= 0 || !isOpaque(world, i12, j12, k12)) && getY(j12) >= 0; --j12) {
                        setBlockAndMetadata(world, i12, j12, k12, beamBlock, beamMeta);
                        setGrassToDirt(world, i12, j12 - 1, k12);
                    }
                    continue;
                }
                placeRandomFloor(world, random, i12, 0, k12);
                setGrassToDirt(world, i12, -1, k12);
                j12 = -1;
                while (!isOpaque(world, i12, j12, k12) && getY(j12) >= 0) {
                    setBlockAndMetadata(world, i12, j12, k12, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i12, j12 - 1, k12);
                    --j12;
                }
                for (j12 = 1; j12 <= 4; ++j12) {
                    setAir(world, i12, j12, k12);
                }
                if ((i2 != 2 || k2 > 1) && (k2 != 2 || i2 > 1)) {
                    continue;
                }
                setBlockAndMetadata(world, i12, 3, k12, fenceBlock, fenceMeta);
            }
        }
        for (i12 = -3; i12 <= 3; ++i12) {
            for (k12 = -3; k12 <= 3; ++k12) {
                i2 = Math.abs(i12);
                k2 = Math.abs(k12);
                if (i2 == 3 && k2 >= 1 && k2 <= 2 || k2 == 3 && i2 >= 1 && i2 <= 2) {
                    setBlockAndMetadata(world, i12, 3, k12, wool1Block, wool1Meta);
                }
                if (i2 + k2 == 3 || i2 + k2 == 4) {
                    if (i2 == 2 && k2 == 2) {
                        setBlockAndMetadata(world, i12, 4, k12, wool2Block, wool2Meta);
                    } else {
                        setBlockAndMetadata(world, i12, 4, k12, wool1Block, wool1Meta);
                    }
                }
                if (i2 + k2 > 2) {
                    continue;
                }
                if (i2 == k2) {
                    setBlockAndMetadata(world, i12, 5, k12, wool2Block, wool2Meta);
                    continue;
                }
                setBlockAndMetadata(world, i12, 5, k12, wool1Block, wool1Meta);
            }
        }
        setBlockAndMetadata(world, -1, 1, -2, plankStairBlock, 4);
        setBlockAndMetadata(world, 0, 1, -2, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, 1, 1, -2, plankStairBlock, 5);
        setBlockAndMetadata(world, 2, 1, -1, fenceGateBlock, 3);
        setBlockAndMetadata(world, 2, 1, 0, plankStairBlock, 7);
        setBlockAndMetadata(world, 2, 1, 1, plankStairBlock, 6);
        setBlockAndMetadata(world, 1, 1, 2, plankStairBlock, 5);
        setBlockAndMetadata(world, 0, 1, 2, plankStairBlock, 4);
        setBlockAndMetadata(world, -1, 1, 2, fenceGateBlock, 0);
        setBlockAndMetadata(world, -2, 1, 1, plankStairBlock, 6);
        setBlockAndMetadata(world, -2, 1, 0, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, -2, 1, -1, plankStairBlock, 7);
        for (i12 = -1; i12 <= 1; ++i12) {
            setBlockAndMetadata(world, i12, 1, -3, trapdoorBlock, 12);
        }
        for (int k14 = -1; k14 <= 1; ++k14) {
            setBlockAndMetadata(world, -3, 1, k14, trapdoorBlock, 15);
        }
        if (random.nextBoolean()) {
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.vanilla("chest"), 2);
        } else {
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("chestBasket"), 2);
        }
        for (step = 0; step < 12 && !isOpaque(world, i1 = 3 + step, j1 = -step, k1 = -1); ++step) {
            placeRandomFloor(world, random, i1, j1, k1);
            setGrassToDirt(world, i1, j1 - 1, k1);
            j2 = j1 - 1;
            while (!isOpaque(world, i1, j2, k1) && getY(j2) >= 0) {
                setBlockAndMetadata(world, i1, j2, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                setGrassToDirt(world, i1, j2 - 1, k1);
                --j2;
            }
        }
        for (step = 0; step < 12 && !isOpaque(world, i1 = -1, j1 = -step, k1 = 3 + step); ++step) {
            placeRandomFloor(world, random, i1, j1, k1);
            setGrassToDirt(world, i1, j1 - 1, k1);
            j2 = j1 - 1;
            while (!isOpaque(world, i1, j2, k1) && getY(j2) >= 0) {
                setBlockAndMetadata(world, i1, j2, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                setGrassToDirt(world, i1, j2 - 1, k1);
                --j2;
            }
        }
        decorateStall(world, random);
        LOTRNPCEntity trader = createTrader(world, random);
        spawnNPCAndSetHome(trader, world, 0, 1, 0, 1);
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        wool1Block = LOTRLegacyBlocks.vanilla("wool");
        wool1Meta = 14;
        wool2Block = LOTRLegacyBlocks.vanilla("wool");
        wool2Meta = 0;
    }

    public static class Baker extends LOTRBreeMarketStallStructure {
        public Baker(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRNPCEntity createTrader(WorldGenLevel world, RandomSource random) {
            return random.nextBoolean() ? create(LOTREntities.BREE_HOBBIT_BAKER, world) : create(LOTREntities.BREE_BAKER, world);
        }

        @Override
        public void decorateStall(WorldGenLevel world, RandomSource random) {
            placePlate_item(world, random, -1, 2, -2, LOTRLegacyBlocks.mod("woodPlateBlock"), getRandomBakeryItem(random), true);
            placePlate_item(world, random, 1, 2, 2, LOTRLegacyBlocks.mod("ceramicPlateBlock"), getRandomBakeryItem(random), true);
            placePlate_item(world, random, -2, 2, 1, LOTRLegacyBlocks.mod("plateBlock"), getRandomBakeryItem(random), true);
            setBlockAndMetadata(world, 1, 2, -2, LOTRBreeStructure.getRandomPieBlock(random), 0);
            setBlockAndMetadata(world, -2, 2, -1, LOTRBreeStructure.getRandomPieBlock(random), 0);
            setBlockAndMetadata(world, 2, 2, 1, LOTRBreeStructure.getRandomPieBlock(random), 0);
        }

        public ItemStack getRandomBakeryItem(RandomSource random) {
            ItemStack[] foods = {LOTRLegacyItems.vanillaStack("bread", 1, 0), LOTRLegacyItems.modStack("cornBread", 1, 0), LOTRLegacyItems.modStack("hobbitPancake", 1, 0), LOTRLegacyItems.modStack("hobbitPancakeMapleSyrup", 1, 0)};
            ItemStack ret = foods[random.nextInt(foods.length)].copy();
            ret.setCount(1 + random.nextInt(3));
            return ret;
        }

        @Override
        public void setupRandomBlocks(RandomSource random) {
            super.setupRandomBlocks(random);
            wool1Block = LOTRLegacyBlocks.vanilla("wool");
            wool1Meta = 4;
        }
    }

    public static class Brewer extends LOTRBreeMarketStallStructure {
        public Brewer(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRNPCEntity createTrader(WorldGenLevel world, RandomSource random) {
            return random.nextBoolean() ? create(LOTREntities.BREE_HOBBIT_BREWER, world) : create(LOTREntities.BREE_BREWER, world);
        }

        @Override
        public void decorateStall(WorldGenLevel world, RandomSource random) {
            placeMug(world, random, -1, 2, -2, 0, LOTRFoods.BREE_DRINK);
            placeMug(world, random, 1, 2, -2, 0, LOTRFoods.BREE_DRINK);
            placeMug(world, random, 1, 2, 2, 2, LOTRFoods.BREE_DRINK);
            setBlockAndMetadata(world, -1, 1, -1, LOTRLegacyBlocks.mod("barrel"), 3);
            setBlockAndMetadata(world, -2, 2, 1, LOTRLegacyBlocks.mod("barrel"), 2);
            setBlockAndMetadata(world, 2, 2, 1, LOTRLegacyBlocks.mod("barrel"), 5);
        }

        @Override
        public void setupRandomBlocks(RandomSource random) {
            super.setupRandomBlocks(random);
            wool1Block = LOTRLegacyBlocks.vanilla("wool");
            wool1Meta = 1;
        }
    }

    public static class Butcher extends LOTRBreeMarketStallStructure {
        public Butcher(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRNPCEntity createTrader(WorldGenLevel world, RandomSource random) {
            return random.nextBoolean() ? create(LOTREntities.BREE_HOBBIT_BUTCHER, world) : create(LOTREntities.BREE_BUTCHER, world);
        }

        @Override
        public void decorateStall(WorldGenLevel world, RandomSource random) {
            placePlate_item(world, random, -1, 2, -2, LOTRLegacyBlocks.mod("plateBlock"), getRandomButcherItem(random), true);
            placePlate_item(world, random, 1, 2, -2, LOTRLegacyBlocks.mod("ceramicPlateBlock"), getRandomButcherItem(random), true);
            placePlate_item(world, random, -2, 2, 0, LOTRLegacyBlocks.mod("woodPlateBlock"), getRandomButcherItem(random), true);
            placePlate_item(world, random, 2, 2, 1, LOTRLegacyBlocks.mod("plateBlock"), getRandomButcherItem(random), true);
            placePlate_item(world, random, 0, 2, 2, LOTRLegacyBlocks.mod("ceramicPlateBlock"), getRandomButcherItem(random), true);
        }

        public ItemStack getRandomButcherItem(RandomSource random) {
            ItemStack[] foods = {LOTRLegacyItems.vanillaStack("beef", 1, 0), LOTRLegacyItems.vanillaStack("porkchop", 1, 0), LOTRLegacyItems.modStack("gammon", 1, 0), LOTRLegacyItems.vanillaStack("chicken", 1, 0), LOTRLegacyItems.modStack("muttonRaw", 1, 0), LOTRLegacyItems.modStack("rabbitRaw", 1, 0), LOTRLegacyItems.modStack("deerRaw", 1, 0)};
            ItemStack ret = foods[random.nextInt(foods.length)].copy();
            ret.setCount(1 + random.nextInt(3));
            return ret;
        }

        @Override
        public void setupRandomBlocks(RandomSource random) {
            super.setupRandomBlocks(random);
            wool1Block = LOTRLegacyBlocks.vanilla("wool");
            wool1Meta = 14;
        }
    }

    public static class Farmer extends LOTRBreeMarketStallStructure {
        public Farmer(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRNPCEntity createTrader(WorldGenLevel world, RandomSource random) {
            return create(LOTREntities.BREE_FARMER, world);
        }

        @Override
        public void decorateStall(WorldGenLevel world, RandomSource random) {
            placePlate_item(world, random, -1, 2, -2, LOTRLegacyBlocks.mod("plateBlock"), getRandomFarmerItem(random), true);
            placePlate_item(world, random, 0, 2, -2, LOTRLegacyBlocks.mod("woodPlateBlock"), getRandomFarmerItem(random), true);
            placePlate_item(world, random, -2, 2, -1, LOTRLegacyBlocks.mod("woodPlateBlock"), getRandomFarmerItem(random), true);
            placePlate_item(world, random, -2, 2, 1, LOTRLegacyBlocks.mod("ceramicPlateBlock"), getRandomFarmerItem(random), true);
            placePlate_item(world, random, 2, 2, 0, LOTRLegacyBlocks.mod("ceramicPlateBlock"), getRandomFarmerItem(random), true);
            setBlockAndMetadata(world, -1, 1, -1, LOTRLegacyBlocks.vanilla("pumpkin"), 3);
            setGrassToDirt(world, -1, 0, -1);
        }

        public ItemStack getRandomFarmerItem(RandomSource random) {
            ItemStack[] foods = {LOTRLegacyItems.vanillaStack("carrot", 1, 0), LOTRLegacyItems.vanillaStack("potato", 1, 0), LOTRLegacyItems.modStack("lettuce", 1, 0), LOTRLegacyItems.modStack("turnip", 1, 0), LOTRLegacyItems.modStack("leek", 1, 0), LOTRLegacyItems.vanillaStack("apple", 1, 0), LOTRLegacyItems.modStack("appleGreen", 1, 0), LOTRLegacyItems.modStack("pear", 1, 0), LOTRLegacyItems.modStack("plum", 1, 0)};
            ItemStack ret = foods[random.nextInt(foods.length)].copy();
            ret.setCount(1 + random.nextInt(3));
            return ret;
        }

        @Override
        public void setupRandomBlocks(RandomSource random) {
            super.setupRandomBlocks(random);
            wool1Block = LOTRLegacyBlocks.vanilla("wool");
            wool1Meta = 13;
        }
    }

    public static class Florist extends LOTRBreeMarketStallStructure {
        public Florist(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRNPCEntity createTrader(WorldGenLevel world, RandomSource random) {
            return random.nextBoolean() ? create(LOTREntities.BREE_HOBBIT_FLORIST, world) : create(LOTREntities.BREE_FLORIST, world);
        }

        @Override
        public void decorateStall(WorldGenLevel world, RandomSource random) {
            placeRandomFlowerPot(world, random, -1, 2, -2);
            placeRandomFlowerPot(world, random, 1, 2, -2);
            placeRandomFlowerPot(world, random, -2, 2, 0);
            placeRandomFlowerPot(world, random, 2, 2, 1);
            placeRandomFlowerPot(world, random, 0, 2, 2);
        }

        @Override
        public void setupRandomBlocks(RandomSource random) {
            super.setupRandomBlocks(random);
            wool1Block = LOTRLegacyBlocks.vanilla("wool");
            wool1Meta = 10;
        }
    }

    public static class Lumber extends LOTRBreeMarketStallStructure {
        public Lumber(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRNPCEntity createTrader(WorldGenLevel world, RandomSource random) {
            return create(LOTREntities.BREE_LUMBERMAN, world);
        }

        @Override
        public void decorateStall(WorldGenLevel world, RandomSource random) {
            setBlockAndMetadata(world, 0, 1, 1, LOTRLegacyBlocks.vanilla("log"), 0);
            setGrassToDirt(world, 0, 0, 1);
            setBlockAndMetadata(world, -1, 1, -1, LOTRLegacyBlocks.mod("wood5"), 4);
            setGrassToDirt(world, -1, 0, -1);
            placeWeaponRack(world, 1, 2, -2, 7, LOTRLegacyItems.vanillaStack("iron_axe", 1, 0));
        }

        @Override
        public void setupRandomBlocks(RandomSource random) {
            super.setupRandomBlocks(random);
            wool1Block = LOTRLegacyBlocks.vanilla("wool");
            wool1Meta = 12;
        }
    }

    public static class Mason extends LOTRBreeMarketStallStructure {
        public Mason(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRNPCEntity createTrader(WorldGenLevel world, RandomSource random) {
            return create(LOTREntities.BREE_MASON, world);
        }

        @Override
        public void decorateStall(WorldGenLevel world, RandomSource random) {
            setBlockAndMetadata(world, 0, 1, 1, brickBlock, brickMeta);
            setBlockAndMetadata(world, 0, 2, 1, brickBlock, brickMeta);
            setGrassToDirt(world, 0, 0, 1);
            setBlockAndMetadata(world, -1, 1, -1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
            setGrassToDirt(world, -1, 0, -1);
            placeWeaponRack(world, 1, 2, -2, 7, LOTRLegacyItems.vanillaStack("iron_pickaxe", 1, 0));
            placeWeaponRack(world, -2, 2, 1, 6, LOTRLegacyItems.modStack("pickaxeBronze", 1, 0));
        }

        @Override
        public void setupRandomBlocks(RandomSource random) {
            super.setupRandomBlocks(random);
            wool1Block = LOTRLegacyBlocks.vanilla("wool");
            wool1Meta = 8;
        }
    }

    public static class Smith extends LOTRBreeMarketStallStructure {
        public Smith(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRNPCEntity createTrader(WorldGenLevel world, RandomSource random) {
            return create(LOTREntities.BREE_BLACKSMITH, world);
        }

        @Override
        public void decorateStall(WorldGenLevel world, RandomSource random) {
            placeWeaponRack(world, 1, 2, -2, 7, LOTRLegacyItems.modStack("ironCrossbow", 1, 0));
            placeWeaponRack(world, -2, 2, 1, 3, LOTRLegacyItems.modStack("battleaxeIron", 1, 0));
            placeWeaponRack(world, 2, 2, 1, 1, LOTRLegacyItems.vanillaStack("iron_sword", 1, 0));
            LOTRBreeGuardEntity armorGuard = create(LOTREntities.BREE_GUARD, world);
            armorGuard.finalizeSpawn(world, world.getCurrentDifficultyAt(armorGuard.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            placeArmorStand(world, 0, 1, 1, 0, new ItemStack[]{armorGuard.getItemBySlot(EquipmentSlot.HEAD), armorGuard.getItemBySlot(EquipmentSlot.CHEST), null, null});
        }

        @Override
        public void setupRandomBlocks(RandomSource random) {
            super.setupRandomBlocks(random);
            wool1Block = LOTRLegacyBlocks.vanilla("wool");
            wool1Meta = 7;
        }
    }

}
