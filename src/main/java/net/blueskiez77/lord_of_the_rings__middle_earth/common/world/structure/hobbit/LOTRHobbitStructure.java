package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRHobbitStructure extends LOTRStructureBase2 {
    public boolean isWealthy;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock fenceGateBlock;
    public LegacyBlock beamBlock;
    public int beamMeta;
    public LegacyBlock doorBlock;
    public LegacyBlock plank2Block;
    public int plank2Meta;
    public LegacyBlock plank2SlabBlock;
    public int plank2SlabMeta;
    public LegacyBlock plank2StairBlock;
    public LegacyBlock fence2Block;
    public int fence2Meta;
    public LegacyBlock fenceGate2Block;
    public LegacyBlock floorBlock;
    public int floorMeta;
    public LegacyBlock floorStairBlock;
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock wallBlock;
    public int wallMeta;
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock roofSlabBlock;
    public int roofSlabMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock carpetBlock;
    public int carpetMeta;
    public LegacyBlock chandelierBlock;
    public int chandelierMeta;
    public LegacyBlock barsBlock;
    public LegacyBlock outFenceBlock;
    public int outFenceMeta;
    public LegacyBlock outFenceGateBlock;
    public LegacyBlock pathBlock;
    public int pathMeta;
    public LegacyBlock pathSlabBlock;
    public int pathSlabMeta;
    public LegacyBlock hedgeBlock;
    public int hedgeMeta;
    public LegacyBlock tileSlabBlock;
    public int tileSlabMeta;
    public LegacyBlock bedBlock;
    public LegacyBlock tableBlock;
    public LegacyBlock gateBlock;
    public LegacyBlock plateBlock;
    public String maleName;
    public String femaleName;
    public String homeName1;
    public String homeName2;

    protected LOTRHobbitStructure(boolean flag) {
        super(flag);
    }

    public static LegacyBlock getRandomCakeBlock(RandomSource random) {
        int i = random.nextInt(5);
        switch (i) {
            case 0:
                return LOTRLegacyBlocks.vanilla("cake");
            case 1:
                return LOTRLegacyBlocks.mod("appleCrumble");
            case 2:
                return LOTRLegacyBlocks.mod("cherryPie");
            case 3:
                return LOTRLegacyBlocks.mod("berryPie");
            case 4:
                return LOTRLegacyBlocks.mod("marzipanBlock");
            default:
                break;
        }
        return LOTRLegacyBlocks.vanilla("cake");
    }

    public LOTRHobbitEntity createHobbit(WorldGenLevel world) {
        return create(LOTREntities.HOBBIT, world);
    }

    public String[] getHobbitCoupleAndHomeNames(RandomSource random) {
        return LOTRNames.getHobbitCoupleAndHomeNames(random);
    }

    public ItemStack getRandomHobbitDecoration(WorldGenLevel world, RandomSource random) {
        if (random.nextInt(3) == 0) {
            return getRandomFlower(world, random);
        }
        ItemStack[] items = {LOTRLegacyItems.modStack("rollingPin", 1, 0), LOTRLegacyItems.modStack("mug", 1, 0), LOTRLegacyItems.modStack("ceramicMug", 1, 0), LOTRLegacyItems.vanillaStack("bow", 1, 0), LOTRLegacyItems.vanillaStack("fishing_rod", 1, 0), LOTRLegacyItems.vanillaStack("feather", 1, 0), LOTRLegacyItems.vanillaStack("clock", 1, 0), LOTRLegacyItems.modStack("leatherHat", 1, 0), LOTRLegacyItems.modStack("hobbitPipe", 1, 0), LOTRLegacyBlocks.vanilla("brown_mushroom").stack(1, 0), LOTRLegacyBlocks.vanilla("red_mushroom").stack(1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public boolean makeWealthy(RandomSource random) {
        return random.nextInt(5) == 0;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        int randomChandelier;
        int randomWood2;
        isWealthy = makeWealthy(random);
        int randomWood = random.nextInt(5);
        switch (randomWood) {
            case 0:
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 0;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
                plankSlabMeta = 0;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsShirePine");
                fenceBlock = LOTRLegacyBlocks.mod("fence");
                fenceMeta = 0;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateShirePine");
                beamBlock = LOTRLegacyBlocks.mod("woodBeam1");
                beamMeta = 0;
                doorBlock = LOTRLegacyBlocks.vanilla("wooden_door");
                break;
            case 1:
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 0;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 0;
                plankStairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 0;
                fenceGateBlock = LOTRLegacyBlocks.vanilla("fence_gate");
                beamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
                beamMeta = 0;
                doorBlock = LOTRLegacyBlocks.vanilla("wooden_door");
                break;
            case 2:
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 2;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 2;
                plankStairBlock = LOTRLegacyBlocks.vanilla("birch_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 2;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateBirch");
                beamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
                beamMeta = 0;
                doorBlock = LOTRLegacyBlocks.mod("doorBirch");
                break;
            case 3:
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 0;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
                plankSlabMeta = 0;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsChestnut");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 0;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateChestnut");
                beamBlock = LOTRLegacyBlocks.mod("woodBeam4");
                beamMeta = 0;
                doorBlock = LOTRLegacyBlocks.mod("doorChestnut");
                break;
            case 4:
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 9;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
                plankSlabMeta = 1;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsBeech");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 9;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateBeech");
                beamBlock = LOTRLegacyBlocks.mod("woodBeam2");
                beamMeta = 1;
                doorBlock = LOTRLegacyBlocks.mod("doorBeech");
                break;
            default:
                break;
        }
        if (random.nextBoolean()) {
            doorBlock = LOTRLegacyBlocks.mod("doorShirePine");
        }
        if (isWealthy) {
            randomWood2 = random.nextInt(3);
            switch (randomWood2) {
                case 0:
                    plank2Block = LOTRLegacyBlocks.mod("planks");
                    plank2Meta = 4;
                    plank2SlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
                    plank2SlabMeta = 4;
                    plank2StairBlock = LOTRLegacyBlocks.mod("stairsApple");
                    fence2Block = LOTRLegacyBlocks.mod("fence");
                    fence2Meta = 4;
                    fenceGate2Block = LOTRLegacyBlocks.mod("fenceGateApple");
                    break;
                case 1:
                    plank2Block = LOTRLegacyBlocks.mod("planks");
                    plank2Meta = 5;
                    plank2SlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
                    plank2SlabMeta = 5;
                    plank2StairBlock = LOTRLegacyBlocks.mod("stairsPear");
                    fence2Block = LOTRLegacyBlocks.mod("fence");
                    fence2Meta = 5;
                    fenceGate2Block = LOTRLegacyBlocks.mod("fenceGatePear");
                    break;
                case 2:
                    plank2Block = LOTRLegacyBlocks.mod("planks");
                    plank2Meta = 6;
                    plank2SlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
                    plank2SlabMeta = 6;
                    plank2StairBlock = LOTRLegacyBlocks.mod("stairsCherry");
                    fence2Block = LOTRLegacyBlocks.mod("fence");
                    fence2Meta = 6;
                    fenceGate2Block = LOTRLegacyBlocks.mod("fenceGateCherry");
                    break;
                default:
                    break;
            }
        } else {
            randomWood2 = random.nextInt(3);
            switch (randomWood2) {
                case 0:
                    plank2Block = LOTRLegacyBlocks.vanilla("planks");
                    plank2Meta = 0;
                    plank2SlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                    plank2SlabMeta = 0;
                    plank2StairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                    fence2Block = LOTRLegacyBlocks.vanilla("fence");
                    fence2Meta = 0;
                    fenceGate2Block = LOTRLegacyBlocks.vanilla("fence_gate");
                    break;
                case 1:
                    plank2Block = LOTRLegacyBlocks.vanilla("planks");
                    plank2Meta = 1;
                    plank2SlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                    plank2SlabMeta = 1;
                    plank2StairBlock = LOTRLegacyBlocks.vanilla("spruce_stairs");
                    fence2Block = LOTRLegacyBlocks.vanilla("fence");
                    fence2Meta = 1;
                    fenceGate2Block = LOTRLegacyBlocks.mod("fenceGateSpruce");
                    break;
                case 2:
                    plank2Block = LOTRLegacyBlocks.mod("planks2");
                    plank2Meta = 9;
                    plank2SlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle4");
                    plank2SlabMeta = 1;
                    plank2StairBlock = LOTRLegacyBlocks.mod("stairsWillow");
                    fence2Block = LOTRLegacyBlocks.mod("fence2");
                    fence2Meta = 9;
                    fenceGate2Block = LOTRLegacyBlocks.mod("fenceGateWillow");
                    break;
                default:
                    break;
            }
        }
        int randomFloor = random.nextInt(3);
        switch (randomFloor) {
            case 0:
                floorBlock = LOTRLegacyBlocks.vanilla("brick_block");
                floorMeta = 0;
                floorStairBlock = LOTRLegacyBlocks.vanilla("brick_stairs");
                break;
            case 1:
                floorBlock = LOTRLegacyBlocks.vanilla("cobblestone");
                floorMeta = 0;
                floorStairBlock = LOTRLegacyBlocks.vanilla("stone_stairs");
                break;
            case 2:
                floorBlock = LOTRLegacyBlocks.vanilla("stonebrick");
                floorMeta = 0;
                floorStairBlock = LOTRLegacyBlocks.vanilla("stone_brick_stairs");
                break;
            default:
                break;
        }
        brickBlock = LOTRLegacyBlocks.vanilla("brick_block");
        brickMeta = 0;
        brickStairBlock = LOTRLegacyBlocks.vanilla("brick_stairs");
        if (random.nextBoolean()) {
            wallBlock = LOTRLegacyBlocks.mod("daub");
            wallMeta = 0;
        } else {
            wallBlock = plankBlock;
            wallMeta = plankMeta;
        }
        roofBlock = LOTRLegacyBlocks.mod("thatch");
        roofMeta = 0;
        roofSlabBlock = LOTRLegacyBlocks.mod("slabSingleThatch");
        roofSlabMeta = 0;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsThatch");
        int randomCarpet = random.nextInt(5);
        switch (randomCarpet) {
            case 0:
                carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
                carpetMeta = 15;
                break;
            case 1:
                carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
                carpetMeta = 14;
                break;
            case 2:
                carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
                carpetMeta = 13;
                break;
            case 3:
                carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
                carpetMeta = 12;
                break;
            case 4:
                carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
                carpetMeta = 7;
                break;
            default:
                break;
        }
        if (isWealthy) {
            randomChandelier = random.nextInt(2);
            if (randomChandelier == 0) {
                chandelierBlock = LOTRLegacyBlocks.mod("chandelier");
                chandelierMeta = 2;
            } else {
                chandelierBlock = LOTRLegacyBlocks.mod("chandelier");
                chandelierMeta = 3;
            }
        } else {
            randomChandelier = random.nextInt(2);
            if (randomChandelier == 0) {
                chandelierBlock = LOTRLegacyBlocks.mod("chandelier");
                chandelierMeta = 0;
            } else {
                chandelierBlock = LOTRLegacyBlocks.mod("chandelier");
                chandelierMeta = 1;
            }
        }
        barsBlock = random.nextBoolean() ? LOTRLegacyBlocks.vanilla("iron_bars") : LOTRLegacyBlocks.mod("bronzeBars");
        if (random.nextInt(3) == 0) {
            outFenceBlock = LOTRLegacyBlocks.vanilla("fence");
        } else {
            outFenceBlock = LOTRLegacyBlocks.vanilla("cobblestone_wall");
        }
        outFenceMeta = 0;
        outFenceGateBlock = LOTRLegacyBlocks.vanilla("fence_gate");
        if (random.nextInt(3) == 0) {
            pathBlock = LOTRLegacyBlocks.mod("dirtPath");
            pathMeta = 0;
            pathSlabBlock = LOTRLegacyBlocks.mod("slabSingleDirt");
            pathSlabMeta = 1;
        } else {
            pathBlock = LOTRLegacyBlocks.vanilla("gravel");
            pathMeta = 0;
            pathSlabBlock = LOTRLegacyBlocks.mod("slabSingleGravel");
            pathSlabMeta = 0;
        }
        hedgeBlock = LOTRLegacyBlocks.vanilla("leaves");
        hedgeMeta = 4;
        if (random.nextBoolean()) {
            tileSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
            tileSlabMeta = 4;
        } else {
            tileSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
            tileSlabMeta = 5;
        }
        bedBlock = LOTRLegacyBlocks.vanilla("bed");
        tableBlock = LOTRLegacyBlocks.mod("hobbitTable");
        int randomGate = random.nextInt(4);
        switch (randomGate) {
            case 0:
                gateBlock = LOTRLegacyBlocks.mod("gateHobbitGreen");
                break;
            case 1:
                gateBlock = LOTRLegacyBlocks.mod("gateHobbitBlue");
                break;
            case 2:
                gateBlock = LOTRLegacyBlocks.mod("gateHobbitRed");
                break;
            case 3:
                gateBlock = LOTRLegacyBlocks.mod("gateHobbitYellow");
                break;
            default:
                break;
        }
        plateBlock = random.nextBoolean() ? LOTRLegacyBlocks.mod("ceramicPlateBlock") : LOTRLegacyBlocks.mod("plateBlock");
        String[] hobbitNames = getHobbitCoupleAndHomeNames(random);
        maleName = hobbitNames[0];
        femaleName = hobbitNames[1];
        homeName1 = hobbitNames[2];
        homeName2 = hobbitNames[3];
    }

    public void spawnHobbitCouple(WorldGenLevel world, int i, int j, int k, int homeRange) {
        LOTRHobbitEntity hobbitMale = createHobbit(world);
        hobbitMale.familyInfo.setName(maleName);
        hobbitMale.familyInfo.setMale(true);
        spawnNPCAndSetHome(hobbitMale, world, i, j, k, homeRange);
        LOTRHobbitEntity hobbitFemale = createHobbit(world);
        hobbitFemale.familyInfo.setName(femaleName);
        hobbitFemale.familyInfo.setMale(false);
        spawnNPCAndSetHome(hobbitFemale, world, i, j, k, homeRange);
        int maxChildren = hobbitMale.familyInfo.getRandomMaxChildren();
        hobbitMale.setItemSlot(EquipmentSlot.HEAD, LOTRLegacyItems.modStack("hobbitRing", 1, 0));
        hobbitMale.familyInfo.spouseUniqueID = hobbitFemale.getUUID();
        hobbitMale.familyInfo.setMaxBreedingDelay();
        hobbitMale.familyInfo.maxChildren = maxChildren;
        hobbitFemale.setItemSlot(EquipmentSlot.HEAD, LOTRLegacyItems.modStack("hobbitRing", 1, 0));
        hobbitFemale.familyInfo.spouseUniqueID = hobbitMale.getUUID();
        hobbitFemale.familyInfo.setMaxBreedingDelay();
        hobbitFemale.familyInfo.maxChildren = maxChildren;
    }
}
