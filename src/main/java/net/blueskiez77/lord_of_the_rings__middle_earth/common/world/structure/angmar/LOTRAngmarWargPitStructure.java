package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRWargPitBaseStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRAngmarWargPitStructure extends LOTRWargPitBaseStructure {
    public LOTRAngmarWargPitStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void associateGroundBlocks() {
        super.associateGroundBlocks();
        clearScanAlias("GROUND_COVER");
        addBlockMetaAliasOption("GROUND_COVER", 1, LOTRLegacyBlocks.vanilla("snow_layer"), 0);
        setBlockAliasChance("GROUND_COVER", 0.25f);
    }

    @Override
    public LOTRNPCEntity getOrc(WorldGenLevel world) {
        return create(LOTREntities.ANGMAR_ORC, world);
    }

    @Override
    public LOTRNPCEntity getWarg(WorldGenLevel world) {
        return create(LOTREntities.ANGMAR_WARG, world);
    }

    @Override
    public void setOrcSpawner(LOTRNPCRespawnerEntity spawner) {
        spawner.setSpawnClass(LOTREntities.ANGMAR_ORC);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        brickBlock = LOTRLegacyBlocks.mod("brick2");
        brickMeta = 0;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle3");
        brickSlabMeta = 3;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsAngmarBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall2");
        brickWallMeta = 0;
        pillarBlock = LOTRLegacyBlocks.mod("pillar2");
        pillarMeta = 4;
        woolBlock = LOTRLegacyBlocks.vanilla("wool");
        woolMeta = 15;
        carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
        carpetMeta = 15;
        tableBlock = LOTRLegacyBlocks.mod("angmarTable");
        banner = "ANGMAR";
        chestContents = LOTRChestContents.ANGMAR_TENT;
    }

    @Override
    public void setWargSpawner(LOTRNPCRespawnerEntity spawner) {
        spawner.setSpawnClass(LOTREntities.ANGMAR_WARG);
    }
}
