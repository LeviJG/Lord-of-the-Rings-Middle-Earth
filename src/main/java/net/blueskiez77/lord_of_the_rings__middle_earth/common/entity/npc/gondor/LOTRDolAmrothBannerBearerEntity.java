package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityDolAmrothBannerBearer: carries the banner of Dol Amroth, always on foot. */
public class LOTRDolAmrothBannerBearerEntity extends LOTRDolAmrothSoldierEntity implements LOTRBannerBearer {

    public LOTRDolAmrothBannerBearerEntity(EntityType<? extends LOTRDolAmrothBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.DOL_AMROTH;
    }
}
