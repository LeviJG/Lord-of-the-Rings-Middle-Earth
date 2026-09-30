package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityGondorBannerBearer: a soldier who carries the banner of Gondor, always on foot. */
public class LOTRGondorBannerBearerEntity extends LOTRGondorSoldierEntity implements LOTRBannerBearer {

    public LOTRGondorBannerBearerEntity(EntityType<? extends LOTRGondorBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.GONDOR;
    }
}
