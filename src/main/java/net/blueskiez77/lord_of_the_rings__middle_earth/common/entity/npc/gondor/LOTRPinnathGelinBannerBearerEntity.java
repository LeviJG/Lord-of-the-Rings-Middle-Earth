package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityPinnathGelinBannerBearer: carries the banner of Pinnath Gelin, always on foot. */
public class LOTRPinnathGelinBannerBearerEntity extends LOTRPinnathGelinSoldierEntity implements LOTRBannerBearer {

    public LOTRPinnathGelinBannerBearerEntity(EntityType<? extends LOTRPinnathGelinBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.PINNATH_GELIN;
    }
}
