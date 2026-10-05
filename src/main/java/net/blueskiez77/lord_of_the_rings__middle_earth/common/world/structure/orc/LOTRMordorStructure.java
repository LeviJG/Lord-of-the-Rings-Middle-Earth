package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;

public abstract class LOTRMordorStructure extends LOTRStructureBase2 {
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock brickWallBlock;
    public int brickWallMeta;
    public LegacyBlock brickCarvedBlock;
    public int brickCarvedMeta;
    public LegacyBlock pillarBlock;
    public int pillarMeta;
    public LegacyBlock smoothBlock;
    public int smoothMeta;
    public LegacyBlock smoothSlabBlock;
    public int smoothSlabMeta;
    public LegacyBlock tileBlock;
    public int tileMeta;
    public LegacyBlock tileSlabBlock;
    public int tileSlabMeta;
    public LegacyBlock tileStairBlock;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock beamBlock;
    public int beamMeta;
    public LegacyBlock bedBlock;
    public LegacyBlock gateIronBlock;
    public LegacyBlock gateOrcBlock;
    public LegacyBlock barsBlock;
    public LegacyBlock chandelierBlock;
    public int chandelierMeta;
    public LegacyBlock trapdoorBlock;

    protected LOTRMordorStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        brickBlock = LOTRLegacyBlocks.mod("brick");
        brickMeta = 0;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle");
        brickSlabMeta = 1;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsMordorBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall");
        brickWallMeta = 1;
        brickCarvedBlock = LOTRLegacyBlocks.mod("brick2");
        brickCarvedMeta = 10;
        pillarBlock = LOTRLegacyBlocks.mod("pillar");
        pillarMeta = 7;
        smoothBlock = LOTRLegacyBlocks.mod("smoothStone");
        smoothMeta = 0;
        smoothSlabBlock = LOTRLegacyBlocks.mod("slabSingle");
        smoothSlabMeta = 0;
        tileBlock = LOTRLegacyBlocks.mod("clayTileDyed");
        tileMeta = 15;
        tileSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
        tileSlabMeta = 7;
        tileStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedBlack");
        plankBlock = LOTRLegacyBlocks.mod("planks");
        plankMeta = 3;
        plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
        plankSlabMeta = 3;
        plankStairBlock = LOTRLegacyBlocks.mod("stairsCharred");
        fenceBlock = LOTRLegacyBlocks.mod("fence");
        fenceMeta = 3;
        beamBlock = LOTRLegacyBlocks.mod("woodBeam1");
        beamMeta = 3;
        bedBlock = LOTRLegacyBlocks.mod("orcBed");
        gateIronBlock = LOTRLegacyBlocks.mod("gateIronBars");
        gateOrcBlock = LOTRLegacyBlocks.mod("gateOrc");
        barsBlock = LOTRLegacyBlocks.mod("orcSteelBars");
        chandelierBlock = LOTRLegacyBlocks.mod("chandelier");
        chandelierMeta = 7;
        trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorCharred");
    }
}
