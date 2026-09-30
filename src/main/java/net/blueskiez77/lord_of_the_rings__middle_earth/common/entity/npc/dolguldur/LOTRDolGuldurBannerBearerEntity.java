package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dolguldur;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityDolGuldurBannerBearer: a Dol Guldur orc who carries the banner of Dol Guldur. */
public class LOTRDolGuldurBannerBearerEntity extends LOTRDolGuldurOrcEntity implements LOTRBannerBearer {

    public LOTRDolGuldurBannerBearerEntity(EntityType<? extends LOTRDolGuldurBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.DOL_GULDUR;
    }
}
