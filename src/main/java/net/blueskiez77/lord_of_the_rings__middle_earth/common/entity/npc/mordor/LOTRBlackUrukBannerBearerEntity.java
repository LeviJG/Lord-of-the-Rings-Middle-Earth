package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityBlackUrukBannerBearer: a Black Uruk who carries the banner of the Black Uruks. */
public class LOTRBlackUrukBannerBearerEntity extends LOTRBlackUrukEntity implements LOTRBannerBearer {

    public LOTRBlackUrukBannerBearerEntity(EntityType<? extends LOTRBlackUrukBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.BLACK_URUK;
    }
}
