package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorBathStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRDorwinionBathStructure extends LOTRGondorBathStructure {
    public LOTRDorwinionHouseStructure houseGenForBlocks;

    public LOTRDorwinionBathStructure(boolean flag) {
        super(flag);
        houseGenForBlocks = new LOTRDorwinionHouseStructure(flag);
    }

    @Override
    public LOTRNPCEntity createBather(WorldGenLevel world) {
        return create(LOTREntities.DORWINION_MAN, world);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        houseGenForBlocks.setupRandomBlocks(random);
        brickBlock = houseGenForBlocks.brickBlock;
        brickMeta = houseGenForBlocks.brickMeta;
        brickSlabBlock = houseGenForBlocks.brickSlabBlock;
        brickSlabMeta = houseGenForBlocks.brickSlabMeta;
        brickStairBlock = houseGenForBlocks.brickStairBlock;
        brickWallBlock = houseGenForBlocks.brickWallBlock;
        brickWallMeta = houseGenForBlocks.brickWallMeta;
        pillarBlock = houseGenForBlocks.pillarBlock;
        pillarMeta = houseGenForBlocks.pillarMeta;
        brick2Block = houseGenForBlocks.clayBlock;
        brick2Meta = houseGenForBlocks.clayMeta;
        brick2SlabBlock = houseGenForBlocks.claySlabBlock;
        brick2SlabMeta = houseGenForBlocks.claySlabMeta;
        brick2StairBlock = houseGenForBlocks.clayStairBlock;
    }
}
