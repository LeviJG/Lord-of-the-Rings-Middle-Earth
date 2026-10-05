package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityDaleBannerBearer: a soldier of Dale who carries Dale's banner. */
public class LOTRDaleBannerBearerEntity extends LOTRDaleSoldierEntity implements LOTRBannerBearer {

    public LOTRDaleBannerBearerEntity(EntityType<? extends LOTRDaleBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.DALE;
    }
}
