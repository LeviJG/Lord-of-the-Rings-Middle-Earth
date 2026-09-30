package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityMoredainBannerBearer: a Moredain warrior who carries the banner of the Moredain. */
public class LOTRMoredainBannerBearerEntity extends LOTRMoredainWarriorEntity implements LOTRBannerBearer {

    public LOTRMoredainBannerBearerEntity(EntityType<? extends LOTRMoredainBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.MOREDAIN;
    }
}
