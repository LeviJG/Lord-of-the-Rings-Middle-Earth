package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityPelargirBannerBearer: carries the banner of Pelargir, always on foot. */
public class LOTRPelargirBannerBearerEntity extends LOTRPelargirMarineEntity implements LOTRBannerBearer {

    public LOTRPelargirBannerBearerEntity(EntityType<? extends LOTRPelargirBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.PELARGIR;
    }
}
