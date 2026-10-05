package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.world.level.WorldGenLevel;

public class LOTRHighElvenForgeStructure extends LOTRElvenForgeStructure {
    public LOTRHighElvenForgeStructure(boolean flag) {
        super(flag);
        brickBlock = LOTRLegacyBlocks.mod("brick3");
        brickMeta = 2;
        pillarBlock = LOTRLegacyBlocks.mod("pillar");
        pillarMeta = 10;
        slabBlock = LOTRLegacyBlocks.mod("slabSingle5");
        slabMeta = 5;
        carvedBrickBlock = LOTRLegacyBlocks.mod("brick2");
        carvedBrickMeta = 13;
        wallBlock = LOTRLegacyBlocks.mod("wall2");
        wallMeta = 11;
        stairBlock = LOTRLegacyBlocks.mod("stairsHighElvenBrick");
        torchBlock = LOTRLegacyBlocks.mod("highElvenTorch");
        tableBlock = LOTRLegacyBlocks.mod("highElvenTable");
        barsBlock = LOTRLegacyBlocks.mod("highElfBars");
        woodBarsBlock = LOTRLegacyBlocks.mod("highElfWoodBars");
        roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
        roofMeta = 3;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedLightBlue");
    }

    @Override
    public LOTRElfEntity getElf(WorldGenLevel world) {
        return create(LOTREntities.HIGH_ELF_SMITH, world);
    }
}
