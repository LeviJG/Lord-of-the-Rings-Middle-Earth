package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityLamedonBannerBearer: carries the banner of Lamedon, always on foot. */
public class LOTRLamedonBannerBearerEntity extends LOTRLamedonSoldierEntity implements LOTRBannerBearer {

    public LOTRLamedonBannerBearerEntity(EntityType<? extends LOTRLamedonBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.LAMEDON;
    }
}
