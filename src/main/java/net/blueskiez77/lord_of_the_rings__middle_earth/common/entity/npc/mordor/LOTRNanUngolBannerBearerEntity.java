package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityNanUngolBannerBearer: a Mordor orc who carries the banner of Nan Ungol. */
public class LOTRNanUngolBannerBearerEntity extends LOTRMordorOrcEntity implements LOTRBannerBearer {

    public LOTRNanUngolBannerBearerEntity(EntityType<? extends LOTRNanUngolBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.NAN_UNGOL;
    }
}
