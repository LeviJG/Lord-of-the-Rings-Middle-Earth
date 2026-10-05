package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRBreeStructure extends LOTRStructureBase2 {
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brick2Block;
    public int brick2Meta;
    public LegacyBlock brick2SlabBlock;
    public int brick2SlabMeta;
    public LegacyBlock brick2StairBlock;
    public LegacyBlock brick2WallBlock;
    public int brick2WallMeta;
    public LegacyBlock floorBlock;
    public int floorMeta;
    public LegacyBlock stoneWallBlock;
    public int stoneWallMeta;
    public LegacyBlock woodBlock;
    public int woodMeta;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock fenceGateBlock;
    public LegacyBlock doorBlock;
    public LegacyBlock trapdoorBlock;
    public LegacyBlock beamBlock;
    public int beamMeta;
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock roofSlabBlock;
    public int roofSlabMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock carpetBlock;
    public int carpetMeta;
    public LegacyBlock bedBlock;
    public LegacyBlock tableBlock;

    protected LOTRBreeStructure(boolean flag) {
        super(flag);
    }

    public static LegacyBlock getRandomPieBlock(RandomSource random) {
        int i = random.nextInt(3);
        switch (i) {
            case 0:
                return LOTRLegacyBlocks.mod("appleCrumble");
            case 1:
                return LOTRLegacyBlocks.mod("cherryPie");
            case 2:
                return LOTRLegacyBlocks.mod("berryPie");
            default:
                break;
        }
        return LOTRLegacyBlocks.mod("appleCrumble");
    }

    public ItemStack getRandomBreeWeapon(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.vanillaStack("iron_sword", 1, 0), LOTRLegacyItems.modStack("daggerIron", 1, 0), LOTRLegacyItems.modStack("pikeIron", 1, 0), LOTRLegacyItems.modStack("rollingPin", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public ItemStack getRandomTavernItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("rollingPin", 1, 0), LOTRLegacyItems.modStack("mug", 1, 0), LOTRLegacyItems.modStack("ceramicMug", 1, 0), LOTRLegacyItems.vanillaStack("bow", 1, 0), LOTRLegacyItems.vanillaStack("wooden_axe", 1, 0), LOTRLegacyItems.vanillaStack("fishing_rod", 1, 0), LOTRLegacyItems.vanillaStack("feather", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public void placeRandomFloor(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        float randFloor = random.nextFloat();
        if (randFloor < 0.25f) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("grass"), 0);
        } else if (randFloor < 0.5f) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("dirt"), 1);
        } else if (randFloor < 0.75f) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("gravel"), 0);
        } else {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("dirtPath"), 0);
        }
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        brickBlock = LOTRLegacyBlocks.mod("cobblebrick");
        brickMeta = 0;
        brick2Block = LOTRLegacyBlocks.vanilla("stonebrick");
        brick2Meta = 0;
        brick2SlabBlock = LOTRLegacyBlocks.vanilla("stone_slab");
        brick2SlabMeta = 5;
        brick2StairBlock = LOTRLegacyBlocks.vanilla("stone_brick_stairs");
        brick2WallBlock = LOTRLegacyBlocks.mod("wallStoneV");
        brick2WallMeta = 1;
        floorBlock = LOTRLegacyBlocks.vanilla("cobblestone");
        floorMeta = 0;
        stoneWallBlock = LOTRLegacyBlocks.vanilla("cobblestone_wall");
        stoneWallMeta = 0;
        int randomWood = random.nextInt(7);
        switch (randomWood) {
            case 0:
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
                break;
            case 1:
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 9;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
                plankSlabMeta = 1;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsBeech");
                fenceBlock = LOTRLegacyBlocks.mod("fence");
                fenceMeta = 9;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateBeech");
                beamBlock = LOTRLegacyBlocks.mod("woodBeam2");
                beamMeta = 1;
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
                beamMeta = 2;
                break;
            case 3:
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 1;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 1;
                plankStairBlock = LOTRLegacyBlocks.vanilla("spruce_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 1;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateSpruce");
                beamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
                beamMeta = 1;
                break;
            case 4:
                woodBlock = LOTRLegacyBlocks.mod("wood4");
                woodMeta = 0;
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
                break;
            case 5:
                woodBlock = LOTRLegacyBlocks.mod("wood3");
                woodMeta = 0;
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 12;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
                plankSlabMeta = 4;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsMaple");
                fenceBlock = LOTRLegacyBlocks.mod("fence");
                fenceMeta = 12;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateMaple");
                beamBlock = LOTRLegacyBlocks.mod("woodBeam3");
                beamMeta = 0;
                break;
            case 6:
                woodBlock = LOTRLegacyBlocks.mod("wood7");
                woodMeta = 0;
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 12;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle4");
                plankSlabMeta = 4;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsAspen");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 12;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateAspen");
                beamBlock = LOTRLegacyBlocks.mod("woodBeam7");
                beamMeta = 0;
                break;
            default:
                break;
        }
        doorBlock = LOTRLegacyBlocks.mod("doorBeech");
        trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorBeech");
        roofBlock = LOTRLegacyBlocks.mod("thatch");
        roofMeta = 0;
        roofSlabBlock = LOTRLegacyBlocks.mod("slabSingleThatch");
        roofSlabMeta = 0;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsThatch");
        carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
        carpetMeta = 12;
        bedBlock = LOTRLegacyBlocks.mod("strawBed");
        tableBlock = LOTRLegacyBlocks.mod("breeTable");
    }
}
