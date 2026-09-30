package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityAngmarBannerBearer: an Angmar orc who carries the banner of Angmar. */
public class LOTRAngmarBannerBearerEntity extends LOTRAngmarOrcEntity implements LOTRBannerBearer {

    public LOTRAngmarBannerBearerEntity(EntityType<? extends LOTRAngmarBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.ANGMAR;
    }
}
