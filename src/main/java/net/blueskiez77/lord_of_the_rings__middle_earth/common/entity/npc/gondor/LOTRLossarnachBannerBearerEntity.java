package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityLossarnachBannerBearer: carries the banner of Lossarnach, always on foot. */
public class LOTRLossarnachBannerBearerEntity extends LOTRLossarnachAxemanEntity implements LOTRBannerBearer {

    public LOTRLossarnachBannerBearerEntity(EntityType<? extends LOTRLossarnachBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.LOSSARNACH;
    }
}
