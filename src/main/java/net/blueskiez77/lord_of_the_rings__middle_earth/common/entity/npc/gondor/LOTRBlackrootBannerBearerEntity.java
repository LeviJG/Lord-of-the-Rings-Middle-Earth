package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityBlackrootBannerBearer: carries the banner of the Blackroot Vale, always on foot. */
public class LOTRBlackrootBannerBearerEntity extends LOTRBlackrootSoldierEntity implements LOTRBannerBearer {

    public LOTRBlackrootBannerBearerEntity(EntityType<? extends LOTRBlackrootBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.BLACKROOT_VALE;
    }
}
