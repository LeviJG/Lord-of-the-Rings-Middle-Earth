package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.world.level.WorldGenLevel;

public class LOTRWoodElvenForgeStructure extends LOTRElvenForgeStructure {
    public LOTRWoodElvenForgeStructure(boolean flag) {
        super(flag);
        brickBlock = LOTRLegacyBlocks.mod("brick3");
        brickMeta = 5;
        pillarBlock = LOTRLegacyBlocks.mod("pillar");
        pillarMeta = 12;
        slabBlock = LOTRLegacyBlocks.mod("slabSingle6");
        slabMeta = 2;
        carvedBrickBlock = LOTRLegacyBlocks.mod("brick2");
        carvedBrickMeta = 14;
        wallBlock = LOTRLegacyBlocks.mod("wall3");
        wallMeta = 0;
        stairBlock = LOTRLegacyBlocks.mod("stairsWoodElvenBrick");
        torchBlock = LOTRLegacyBlocks.mod("woodElvenTorch");
        tableBlock = LOTRLegacyBlocks.mod("woodElvenTable");
        barsBlock = LOTRLegacyBlocks.mod("woodElfBars");
        woodBarsBlock = LOTRLegacyBlocks.mod("woodElfWoodBars");
        roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
        roofMeta = 13;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedGreen");
    }

    @Override
    public LOTRElfEntity getElf(WorldGenLevel world) {
        return create(LOTREntities.WOOD_ELF_SMITH, world);
    }
}
