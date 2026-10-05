package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRGaladhrimForgeStructure extends LOTRElvenForgeStructure {
    public LOTRGaladhrimForgeStructure(boolean flag) {
        super(flag);
        brickBlock = LOTRLegacyBlocks.mod("brick");
        brickMeta = 11;
        pillarBlock = LOTRLegacyBlocks.mod("pillar");
        pillarMeta = 1;
        slabBlock = LOTRLegacyBlocks.mod("slabSingle2");
        slabMeta = 3;
        carvedBrickBlock = LOTRLegacyBlocks.mod("brick2");
        carvedBrickMeta = 15;
        wallBlock = LOTRLegacyBlocks.mod("wall");
        wallMeta = 10;
        stairBlock = LOTRLegacyBlocks.mod("stairsElvenBrick");
        torchBlock = LOTRLegacyBlocks.mod("mallornTorchSilver");
        tableBlock = LOTRLegacyBlocks.mod("elvenTable");
        barsBlock = LOTRLegacyBlocks.mod("galadhrimBars");
        woodBarsBlock = LOTRLegacyBlocks.mod("galadhrimWoodBars");
        roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
        roofMeta = 4;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedYellow");
        chestBlock = LOTRLegacyBlocks.mod("chestMallorn");
    }

    @Override
    public LOTRElfEntity getElf(WorldGenLevel world) {
        return create(LOTREntities.GALADHRIM_SMITH, world);
    }

    @Override
    public LegacyBlock getTorchBlock(RandomSource random) {
        return LOTRElfHouseStructure.getRandomTorch(random);
    }
}
