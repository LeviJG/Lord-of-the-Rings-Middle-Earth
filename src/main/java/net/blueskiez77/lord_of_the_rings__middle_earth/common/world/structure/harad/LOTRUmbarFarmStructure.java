package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimBaseEntity;

import net.minecraft.world.level.WorldGenLevel;

public class LOTRUmbarFarmStructure extends LOTRSouthronFarmStructure {
    public LOTRUmbarFarmStructure(boolean flag) {
        super(flag);
    }

    @Override
    public LOTRNearHaradrimBaseEntity createFarmer(WorldGenLevel world) {
        return create(LOTREntities.UMBAR_FARMER, world);
    }

    @Override
    public boolean isUmbar() {
        return true;
    }
}
