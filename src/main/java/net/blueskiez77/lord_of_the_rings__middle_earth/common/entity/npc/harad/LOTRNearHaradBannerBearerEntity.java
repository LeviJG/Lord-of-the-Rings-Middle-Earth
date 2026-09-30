package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityNearHaradBannerBearer: a Southron warrior who carries the banner of Near Harad. */
public class LOTRNearHaradBannerBearerEntity extends LOTRNearHaradrimWarriorEntity implements LOTRBannerBearer {

    public LOTRNearHaradBannerBearerEntity(EntityType<? extends LOTRNearHaradBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.NEAR_HARAD;
    }
}
