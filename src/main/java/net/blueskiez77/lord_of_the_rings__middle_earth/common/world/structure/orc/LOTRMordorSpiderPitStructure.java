package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor.LOTRMordorOrcSpiderKeeperEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRMordorSpiderPitStructure extends LOTRMordorWargPitStructure {
    public LOTRMordorSpiderPitStructure(boolean flag) {
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
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        if (super.generateWithSetRotation(world, random, i, j, k, rotation)) {
            LOTRMordorOrcSpiderKeeperEntity spiderKeeper = create(LOTREntities.MORDOR_ORC_SPIDER_KEEPER, world);
            spawnNPCAndSetHome(spiderKeeper, world, 0, 1, 0, 8);
            return true;
        }
        return false;
    }

    @Override
    public LOTRNPCEntity getWarg(WorldGenLevel world) {
        return create(LOTREntities.MORDOR_SPIDER, world);
    }

    @Override
    public void setWargSpawner(LOTRNPCRespawnerEntity spawner) {
        spawner.setSpawnClass(LOTREntities.MORDOR_SPIDER);
    }
}
