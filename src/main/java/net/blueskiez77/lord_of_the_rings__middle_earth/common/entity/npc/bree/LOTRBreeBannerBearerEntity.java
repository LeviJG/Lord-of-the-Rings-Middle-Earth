package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityBreeBannerBearer: a guard who carries the banner of Bree-land. */
public class LOTRBreeBannerBearerEntity extends LOTRBreeGuardEntity implements LOTRBannerBearer {

    public LOTRBreeBannerBearerEntity(EntityType<? extends LOTRBreeBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.BREE;
    }
}
