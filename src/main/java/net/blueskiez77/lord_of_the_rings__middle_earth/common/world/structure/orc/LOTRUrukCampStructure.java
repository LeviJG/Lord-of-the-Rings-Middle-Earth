package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRCampBaseStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRUrukCampStructure extends LOTRCampBaseStructure {
    public LOTRUrukCampStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRStructureBase2 createTent(boolean flag, RandomSource random) {
        if (random.nextInt(6) == 0) {
            return new LOTRUrukForgeTentStructure(false);
        }
        return new LOTRUrukTentStructure(false);
    }

    @Override
    public LOTRNPCEntity getCampCaptain(WorldGenLevel world, RandomSource random) {
        return random.nextBoolean() ? create(LOTREntities.URUK_HAI_TRADER, world) : create(LOTREntities.URUK_HAI_MERCENARY_CAPTAIN, world);
    }

    @Override
    public void placeNPCRespawner(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClasses(LOTREntities.URUK_HAI, LOTREntities.URUK_HAI_CROSSBOWER);
        respawner.setCheckRanges(24, -12, 12, 12);
        respawner.setSpawnRanges(8, -4, 4, 16);
        placeNPCRespawner(respawner, world, i, j, k);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        tableBlock = LOTRLegacyBlocks.mod("commandTable");
        brickBlock = LOTRLegacyBlocks.mod("brick2");
        brickMeta = 7;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle4");
        brickSlabMeta = 4;
        fenceBlock = LOTRLegacyBlocks.mod("fence");
        fenceMeta = 3;
        fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateCharred");
        hasOrcTorches = true;
        hasSkulls = true;
    }
}
