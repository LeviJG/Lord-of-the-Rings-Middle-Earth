package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dorwinion;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityDorwinionBannerBearer: a guard who carries the banner of Dorwinion. */
public class LOTRDorwinionBannerBearerEntity extends LOTRDorwinionGuardEntity implements LOTRBannerBearer {

    public LOTRDorwinionBannerBearerEntity(EntityType<? extends LOTRDorwinionBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.DORWINION;
    }
}
