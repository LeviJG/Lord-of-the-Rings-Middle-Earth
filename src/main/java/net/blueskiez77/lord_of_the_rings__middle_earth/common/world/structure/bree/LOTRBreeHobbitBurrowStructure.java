package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitBurrowStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRBreeHobbitBurrowStructure extends LOTRHobbitBurrowStructure {
    public LOTRBreeHobbitBurrowStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRHobbitEntity createHobbit(WorldGenLevel world) {
        return create(LOTREntities.BREE_HOBBIT, world);
    }

    @Override
    public String[] getHobbitCoupleAndHomeNames(RandomSource random) {
        return LOTRNames.getBreeHobbitCoupleAndHomeNames(random);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        LOTRBreeHouseStructure breeBlockProxy = new LOTRBreeHouseStructure(false);
        breeBlockProxy.setupRandomBlocks(random);
        brickBlock = breeBlockProxy.brickBlock;
        brickMeta = breeBlockProxy.brickMeta;
        floorBlock = LOTRLegacyBlocks.vanilla("cobblestone");
        floorMeta = 0;
        plankBlock = breeBlockProxy.plankBlock;
        plankMeta = breeBlockProxy.plankMeta;
        plankSlabBlock = breeBlockProxy.plankSlabBlock;
        plankSlabMeta = breeBlockProxy.plankSlabMeta;
        plankStairBlock = breeBlockProxy.plankStairBlock;
        fenceBlock = breeBlockProxy.fenceBlock;
        fenceMeta = breeBlockProxy.fenceMeta;
        fenceGateBlock = breeBlockProxy.fenceGateBlock;
        doorBlock = breeBlockProxy.doorBlock;
        beamBlock = breeBlockProxy.beamBlock;
        beamMeta = breeBlockProxy.beamMeta;
        tableBlock = breeBlockProxy.tableBlock;
        burrowLoot = LOTRChestContents.BREE_HOUSE;
        foodPool = LOTRFoods.BREE;
    }
}
