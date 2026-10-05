package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad.LOTRMoredainMercenaryEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRCampBaseStructure;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRMoredainMercCampStructure extends LOTRCampBaseStructure {
    public LOTRMoredainMercCampStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRStructureBase2 createTent(boolean flag, RandomSource random) {
        return new LOTRMoredainMercTentStructure(false);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        if (super.generateWithSetRotation(world, random, i, j, k, rotation)) {
            int dummies = 1 + random.nextInt(3);
            for (int l = 0; l < dummies; ++l) {
                int r;
                int k1;
                int i1;
                float ang;
                //noinspection StatementWithEmptyBody
                for (int att = 0; att < 8 && !generateSubstructureWithRestrictionFlag(new LOTRMoredainMercDummyStructure(notifyChanges), world, random, i1 = (int) ((r = Mth.randomBetweenInclusive(random, 8, 15)) * Mth.cos(ang = random.nextFloat() * 3.1415927f * 2.0f)), getTopBlock(world, i1, k1 = (int) (r * Mth.sin(ang))), k1, random.nextInt(4), true); ++att) {
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public LOTRNPCEntity getCampCaptain(WorldGenLevel world, RandomSource random) {
        return null;
    }

    @Override
    public void placeNPCRespawner(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClass(LOTREntities.MOREDAIN_MERCENARY);
        respawner.setCheckRanges(24, -12, 12, 10);
        respawner.setSpawnRanges(8, -4, 4, 16);
        placeNPCRespawner(respawner, world, i, j, k);
        int mercs = 2 + random.nextInt(5);
        for (int l = 0; l < mercs; ++l) {
            LOTRMoredainMercenaryEntity merc = create(LOTREntities.MOREDAIN_MERCENARY, world);
            spawnNPCAndSetHome(merc, world, 0, 1, 0, 16);
        }
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        tableBlock = LOTRLegacyBlocks.mod("commandTable");
        brickBlock = LOTRLegacyBlocks.mod("planks2");
        brickMeta = 2;
        brickSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
        brickSlabMeta = 2;
        fenceBlock = LOTRLegacyBlocks.mod("fence2");
        fenceMeta = 2;
        fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateCedar");
    }
}
