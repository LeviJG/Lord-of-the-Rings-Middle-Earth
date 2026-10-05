package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRDolGuldurSpiderPitStructure extends LOTRWargPitBaseStructure {
    public LOTRDolGuldurSpiderPitStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void associateGroundBlocks() {
        super.associateGroundBlocks();
        clearScanAlias("GROUND_COVER");
        addBlockMetaAliasOption("GROUND_COVER", 1, LOTRLegacyBlocks.mod("webUngoliant"), 0);
        setBlockAliasChance("GROUND_COVER", 0.04f);
    }

    @Override
    public LOTRNPCEntity getOrc(WorldGenLevel world) {
        return create(LOTREntities.DOL_GULDUR_ORC, world);
    }

    @Override
    public LOTRNPCEntity getWarg(WorldGenLevel world) {
        return create(LOTREntities.MIRKWOOD_SPIDER, world);
    }

    @Override
    public void setOrcSpawner(LOTRNPCRespawnerEntity spawner) {
        spawner.setSpawnClass(LOTREntities.DOL_GULDUR_ORC);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        brickBlock = LOTRLegacyBlocks.mod("brick2");
        brickMeta = 8;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle4");
        brickSlabMeta = 5;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsDolGuldurBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall2");
        brickWallMeta = 8;
        pillarBlock = beamBlock;
        pillarMeta = beamMeta;
        woolBlock = LOTRLegacyBlocks.vanilla("wool");
        woolMeta = 15;
        carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
        carpetMeta = 15;
        gateMetalBlock = LOTRLegacyBlocks.mod("gateIronBars");
        tableBlock = LOTRLegacyBlocks.mod("dolGuldurTable");
        banner = "DOL_GULDUR";
        chestContents = LOTRChestContents.DOL_GULDUR_TENT;
    }

    @Override
    public void setWargSpawner(LOTRNPCRespawnerEntity spawner) {
        spawner.setSpawnClass(LOTREntities.MIRKWOOD_SPIDER);
    }
}
