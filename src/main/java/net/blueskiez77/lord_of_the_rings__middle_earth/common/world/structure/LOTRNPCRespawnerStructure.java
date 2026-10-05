package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRWorldGenNPCRespawner: a "structure" that is only a respawner, set one
 * block up from its spot -- what the villages place to keep themselves
 * peopled, each saying in {@link #setupRespawner} what it brings back.
 */
public abstract class LOTRNPCRespawnerStructure extends LOTRStructureBase2 {

    protected LOTRNPCRespawnerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        LOTRNPCRespawnerEntity spawner = create(LOTREntities.NPC_RESPAWNER, world);
        setupRespawner(spawner);
        placeNPCRespawner(spawner, world, 0, 1, 0);
        return true;
    }

    public abstract void setupRespawner(LOTRNPCRespawnerEntity spawner);
}
