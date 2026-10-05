package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRMordorWargPitStructure extends LOTRWargPitBaseStructure {
    public LOTRMordorWargPitStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void associateGroundBlocks() {
        addBlockMetaAliasOption("GROUND", 4, LOTRLegacyBlocks.mod("rock"), 0);
        addBlockMetaAliasOption("GROUND", 4, LOTRLegacyBlocks.mod("mordorDirt"), 0);
        addBlockMetaAliasOption("GROUND", 4, LOTRLegacyBlocks.mod("mordorGravel"), 0);
        addBlockMetaAliasOption("GROUND_SLAB", 4, LOTRLegacyBlocks.mod("slabSingle10"), 7);
        addBlockMetaAliasOption("GROUND_SLAB", 4, LOTRLegacyBlocks.mod("slabSingleDirt"), 3);
        addBlockMetaAliasOption("GROUND_SLAB", 4, LOTRLegacyBlocks.mod("slabSingleGravel"), 1);
        addBlockMetaAliasOption("GROUND_COVER", 1, LOTRLegacyBlocks.mod("mordorMoss"), 0);
        setBlockAliasChance("GROUND_COVER", 0.25f);
    }

    @Override
    public LOTRNPCEntity getOrc(WorldGenLevel world) {
        return create(LOTREntities.MORDOR_ORC, world);
    }

    @Override
    public LOTRNPCEntity getWarg(WorldGenLevel world) {
        return create(LOTREntities.MORDOR_WARG, world);
    }

    @Override
    public void setOrcSpawner(LOTRNPCRespawnerEntity spawner) {
        spawner.setSpawnClass(LOTREntities.MORDOR_ORC);
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
        pillarBlock = LOTRLegacyBlocks.mod("pillar");
        pillarMeta = 7;
        woolBlock = LOTRLegacyBlocks.vanilla("wool");
        woolMeta = 12;
        carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
        carpetMeta = 12;
        gateMetalBlock = LOTRLegacyBlocks.mod("gateIronBars");
        tableBlock = LOTRLegacyBlocks.mod("morgulTable");
        banner = "MORDOR";
        chestContents = LOTRChestContents.ORC_TENT;
    }

    @Override
    public void setWargSpawner(LOTRNPCRespawnerEntity spawner) {
        spawner.setSpawnClass(LOTREntities.MORDOR_WARG);
    }
}
