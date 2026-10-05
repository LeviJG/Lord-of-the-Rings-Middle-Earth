package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimBaseEntity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRUmbarFortressStructure extends LOTRSouthronFortressStructure {
    public LOTRUmbarFortressStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRNearHaradrimBaseEntity createCaptain(WorldGenLevel world, RandomSource random) {
        return create(LOTREntities.UMBAR_CAPTAIN, world);
    }

    @Override
    public LOTRNearHaradrimBaseEntity createWarrior(WorldGenLevel world, RandomSource random) {
        return random.nextInt(3) == 0 ? create(LOTREntities.UMBAR_ARCHER, world) : create(LOTREntities.UMBAR_WARRIOR, world);
    }

    @Override
    public boolean isUmbar() {
        return true;
    }

    @Override
    public void setSpawnClasses(LOTRNPCRespawnerEntity spawner) {
        spawner.setSpawnClasses(LOTREntities.UMBAR_WARRIOR, LOTREntities.UMBAR_ARCHER);
    }
}
