package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityLebenninBannerBearer: carries the banner of Lebennin, always on foot. */
public class LOTRLebenninBannerBearerEntity extends LOTRLebenninLevymanEntity implements LOTRBannerBearer {

    public LOTRLebenninBannerBearerEntity(EntityType<? extends LOTRLebenninBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.LEBENNIN;
    }
}
