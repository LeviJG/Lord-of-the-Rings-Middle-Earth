package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

public class LOTRTowerHillsTowerStructure extends LOTRHighElvenTowerStructure {
    public LOTRTowerHillsTowerStructure(boolean flag) {
        super(flag);
        brickBlock = LOTRLegacyBlocks.mod("brick4");
        brickMeta = 15;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle9");
        brickSlabMeta = 0;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsChalkBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall3");
        brickWallMeta = 6;
        pillarBlock = LOTRLegacyBlocks.mod("pillar2");
        pillarMeta = 1;
        floorBlock = LOTRLegacyBlocks.mod("slabDouble8");
        floorMeta = 7;
        roofBlock = brickBlock;
        roofMeta = brickMeta;
        roofSlabBlock = brickSlabBlock;
        roofSlabMeta = brickSlabMeta;
        roofStairBlock = brickStairBlock;
    }
}
