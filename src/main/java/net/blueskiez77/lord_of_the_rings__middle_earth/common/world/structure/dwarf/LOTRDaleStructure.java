package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;

public abstract class LOTRDaleStructure extends LOTRStructureBase2 {
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock brickWallBlock;
    public int brickWallMeta;
    public LegacyBlock pillarBlock;
    public int pillarMeta;
    public LegacyBlock floorBlock;
    public int floorMeta;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock fenceGateBlock;
    public LegacyBlock woodBlock;
    public int woodMeta;
    public LegacyBlock woodBeamBlock;
    public int woodBeamMeta;
    public LegacyBlock doorBlock;
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock roofSlabBlock;
    public int roofSlabMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock barsBlock;
    public LegacyBlock plateBlock;
    public LegacyBlock trapdoorBlock;

    protected LOTRDaleStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        brickBlock = LOTRLegacyBlocks.mod("brick5");
        brickMeta = 1;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle9");
        brickSlabMeta = 6;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsDaleBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall3");
        brickWallMeta = 9;
        pillarBlock = LOTRLegacyBlocks.mod("pillar2");
        pillarMeta = 5;
        floorBlock = LOTRLegacyBlocks.vanilla("cobblestone");
        floorMeta = 0;
        int randomWood = random.nextInt(3);
        switch (randomWood) {
            case 0:
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 1;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 1;
                plankStairBlock = LOTRLegacyBlocks.vanilla("spruce_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 0;
                fenceGateBlock = LOTRLegacyBlocks.vanilla("fence_gate");
                woodBlock = LOTRLegacyBlocks.vanilla("log");
                woodMeta = 0;
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
                woodBeamMeta = 0;
                doorBlock = LOTRLegacyBlocks.mod("doorSpruce");
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorSpruce");
                break;
            case 1:
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 4;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
                plankSlabMeta = 4;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsPine");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 4;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGatePine");
                woodBlock = LOTRLegacyBlocks.mod("wood5");
                woodMeta = 0;
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam5");
                woodBeamMeta = 0;
                doorBlock = LOTRLegacyBlocks.mod("doorPine");
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorPine");
                break;
            case 2:
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 3;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
                plankSlabMeta = 3;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsFir");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 3;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateFir");
                woodBlock = LOTRLegacyBlocks.mod("wood4");
                woodMeta = 3;
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam4");
                woodBeamMeta = 3;
                doorBlock = LOTRLegacyBlocks.mod("doorFir");
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorFir");
                break;
            default:
                break;
        }
        int randomClay = random.nextInt(4);
        switch (randomClay) {
            case 0:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 1;
                roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle");
                roofSlabMeta = 1;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedOrange");
                break;
            case 1:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 14;
                roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                roofSlabMeta = 6;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedRed");
                break;
            case 2:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 12;
                roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                roofSlabMeta = 4;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedBrown");
                break;
            case 3:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 11;
                roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                roofSlabMeta = 3;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedBlue");
                break;
            default:
                break;
        }
        barsBlock = random.nextInt(3) == 0 ? LOTRLegacyBlocks.vanilla("iron_bars") : LOTRLegacyBlocks.mod("bronzeBars");
        plateBlock = random.nextBoolean() ? random.nextBoolean() ? LOTRLegacyBlocks.mod("plateBlock") : LOTRLegacyBlocks.mod("ceramicPlateBlock") : LOTRLegacyBlocks.mod("woodPlateBlock");
    }
}
