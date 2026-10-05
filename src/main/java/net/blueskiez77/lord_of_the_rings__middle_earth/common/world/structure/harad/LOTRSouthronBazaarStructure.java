package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradBlacksmithEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRSouthronBakerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRSouthronBrewerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRSouthronButcherEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRSouthronFarmerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRSouthronFishmongerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRSouthronFloristEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRSouthronGoldsmithEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRSouthronLumbermanEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRSouthronMasonEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRSouthronMinerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRSouthronBazaarStructure extends LOTRSouthronStructure {
    public static Class<?>[] stalls = {Lumber.class, Mason.class, Fish.class, Baker.class, Goldsmith.class, Farmer.class, Blacksmith.class, Brewer.class, Miner.class, Florist.class, Butcher.class};

    public LOTRSouthronBazaarStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 10);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -13; i1 <= 13; ++i1) {
                for (int k1 = -9; k1 <= 9; ++k1) {
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
                    if (maxHeight - minHeight <= 12) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (int i1 = -13; i1 <= 13; ++i1) {
            for (int k1 = -9; k1 <= 9; ++k1) {
                int j1;
                for (j1 = 1; j1 <= 8; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                j1 = -1;
                while (!isOpaque(world, i1, j1, k1) && getY(j1) >= 0) {
                    setBlockAndMetadata(world, i1, j1, k1, stoneBlock, stoneMeta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    --j1;
                }
            }
        }
        loadStrScan("southron_bazaar");
        associateBlockMetaAlias("STONE", stoneBlock, stoneMeta);
        associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
        associateBlockMetaAlias("BRICK_SLAB", brickSlabBlock, brickSlabMeta);
        associateBlockMetaAlias("BRICK_SLAB_INV", brickSlabBlock, brickSlabMeta | 8);
        associateBlockAlias("BRICK_STAIR", brickStairBlock);
        associateBlockMetaAlias("PILLAR", pillarBlock, pillarMeta);
        associateBlockMetaAlias("BRICK2", brick2Block, brick2Meta);
        associateBlockMetaAlias("BRICK2_SLAB", brick2SlabBlock, brick2SlabMeta);
        associateBlockMetaAlias("BRICK2_SLAB_INV", brick2SlabBlock, brick2SlabMeta | 8);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockMetaAlias("BEAM", woodBeamBlock, woodBeamMeta);
        generateStrScan(world, random, 0, 0, 0);
        placeArmorStand(world, -4, 1, -2, 0, new ItemStack[]{LOTRLegacyItems.modStack("helmetNearHaradWarlord", 1, 0), null, null, null});
        placeAnimalJar(world, -3, 1, -7, LOTRLegacyBlocks.mod("butterflyJar"), 0, create(LOTREntities.BUTTERFLY, world));
        placeAnimalJar(world, 11, 1, -1, LOTRLegacyBlocks.mod("birdCageWood"), 0, null);
        placeAnimalJar(world, 3, 1, 7, LOTRLegacyBlocks.mod("birdCage"), 0, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, -9, 3, 0, LOTRLegacyBlocks.mod("birdCageWood"), 0, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, 4, 3, 3, LOTRLegacyBlocks.mod("birdCageWood"), 0, create(LOTREntities.BIRD, world));
        List<Class<?>> stallClasses = new ArrayList<>(Arrays.asList(getStallClasses()));
        while (stallClasses.size() > 6) {
            stallClasses.remove(random.nextInt(stallClasses.size()));
        }
        try {
            LOTRStructureBase2 stall0 = (LOTRStructureBase2) stallClasses.get(0).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall1 = (LOTRStructureBase2) stallClasses.get(1).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall2 = (LOTRStructureBase2) stallClasses.get(2).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall3 = (LOTRStructureBase2) stallClasses.get(3).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall4 = (LOTRStructureBase2) stallClasses.get(4).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall5 = (LOTRStructureBase2) stallClasses.get(5).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            generateSubstructure(stall0, world, random, -8, 1, -4, 2);
            generateSubstructure(stall1, world, random, 0, 1, -4, 2);
            generateSubstructure(stall2, world, random, 8, 1, -4, 2);
            generateSubstructure(stall3, world, random, -8, 1, 4, 0);
            generateSubstructure(stall4, world, random, 0, 1, 4, 0);
            generateSubstructure(stall5, world, random, 8, 1, 4, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Class<?>[] getStallClasses() {
        return stalls;
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
            LOTRSouthronBakerEntity trader = create(LOTREntities.SOUTHRON_BAKER, world);
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
            placeWeaponRack(world, -2, 2, 0, 1, new LOTRSouthronBazaarStructure(false).getRandomHaradWeapon(random));
            placeWeaponRack(world, 2, 2, 0, 3, new LOTRSouthronBazaarStructure(false).getRandomHaradWeapon(random));
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
            LOTRSouthronBrewerEntity trader = create(LOTREntities.SOUTHRON_BREWER, world);
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
            LOTRSouthronButcherEntity trader = create(LOTREntities.SOUTHRON_BUTCHER, world);
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
            LOTRSouthronFarmerEntity trader = create(LOTREntities.SOUTHRON_FARMER, world);
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
            LOTRSouthronFishmongerEntity trader = create(LOTREntities.SOUTHRON_FISHMONGER, world);
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
            LOTRSouthronFloristEntity trader = create(LOTREntities.SOUTHRON_FLORIST, world);
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
            LOTRSouthronGoldsmithEntity trader = create(LOTREntities.SOUTHRON_GOLDSMITH, world);
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
            LOTRSouthronLumbermanEntity trader = create(LOTREntities.SOUTHRON_LUMBERMAN, world);
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
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("brick"), 15);
            setBlockAndMetadata(world, -1, 2, 1, LOTRLegacyBlocks.mod("slabSingle4"), 0);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("brick3"), 13);
            placeWeaponRack(world, 1, 2, 1, 2, LOTRLegacyItems.modStack("pickaxeBronze", 1, 0));
            LOTRSouthronMasonEntity trader = create(LOTREntities.SOUTHRON_MASON, world);
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
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("oreCopper"), 0);
            placeWeaponRack(world, 1, 2, 1, 2, LOTRLegacyItems.modStack("pickaxeBronze", 1, 0));
            LOTRSouthronMinerEntity trader = create(LOTREntities.SOUTHRON_MINER, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
            return true;
        }
    }

}
