package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimBaseEntity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRUmbarBarracksStructure extends LOTRSouthronBarracksStructure {
    public LOTRUmbarBarracksStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRNearHaradrimBaseEntity createWarrior(WorldGenLevel world, RandomSource random) {
        return random.nextInt(3) == 0 ? create(LOTREntities.UMBAR_ARCHER, world) : create(LOTREntities.UMBAR_WARRIOR, world);
    }

    @Override
    public boolean isUmbar() {
        return true;
    }
}
