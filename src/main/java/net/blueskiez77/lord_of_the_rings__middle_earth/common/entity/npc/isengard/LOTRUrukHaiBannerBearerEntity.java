package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.isengard;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityUrukHaiBannerBearer: an Uruk who carries the banner of Isengard. */
public class LOTRUrukHaiBannerBearerEntity extends LOTRUrukHaiEntity implements LOTRBannerBearer {

    public LOTRUrukHaiBannerBearerEntity(EntityType<? extends LOTRUrukHaiBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.ISENGARD;
    }
}
