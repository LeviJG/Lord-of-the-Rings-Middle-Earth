package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRCampBaseStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRAngmarCampStructure extends LOTRCampBaseStructure {
    public LOTRAngmarCampStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRStructureBase2 createTent(boolean flag, RandomSource random) {
        if (random.nextInt(6) == 0) {
            return new LOTRAngmarForgeTentStructure(false);
        }
        return new LOTRAngmarTentStructure(false);
    }

    @Override
    public LOTRNPCEntity getCampCaptain(WorldGenLevel world, RandomSource random) {
        if (random.nextBoolean()) {
            return create(LOTREntities.ANGMAR_ORC_TRADER, world);
        }
        return null;
    }

    @Override
    public void placeNPCRespawner(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClasses(LOTREntities.ANGMAR_ORC, LOTREntities.ANGMAR_ORC_ARCHER);
        respawner.setCheckRanges(24, -12, 12, 12);
        respawner.setSpawnRanges(8, -4, 4, 16);
        placeNPCRespawner(respawner, world, i, j, k);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        tableBlock = LOTRLegacyBlocks.mod("commandTable");
        brickBlock = LOTRLegacyBlocks.mod("brick2");
        brickMeta = 0;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle3");
        brickSlabMeta = 3;
        fenceBlock = LOTRLegacyBlocks.mod("fence");
        fenceMeta = 3;
        fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateCharred");
        hasOrcTorches = true;
        hasSkulls = true;
    }
}
