package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.halftroll;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityHalfTrollBannerBearer: a half-troll warrior who carries the banner of the half-trolls. */
public class LOTRHalfTrollBannerBearerEntity extends LOTRHalfTrollWarriorEntity implements LOTRBannerBearer {

    public LOTRHalfTrollBannerBearerEntity(EntityType<? extends LOTRHalfTrollBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.HALF_TROLL;
    }
}
