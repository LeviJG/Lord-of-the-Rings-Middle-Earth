package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gundabad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityGundabadBannerBearer: a Gundabad orc who carries the banner of Gundabad. */
public class LOTRGundabadBannerBearerEntity extends LOTRGundabadOrcEntity implements LOTRBannerBearer {

    public LOTRGundabadBannerBearerEntity(EntityType<? extends LOTRGundabadBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.GUNDABAD;
    }
}
