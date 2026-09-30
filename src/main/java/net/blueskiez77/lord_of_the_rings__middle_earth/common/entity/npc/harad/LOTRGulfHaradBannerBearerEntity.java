package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityGulfHaradBannerBearer: a Gulf warrior who carries the banner of the Gulf. */
public class LOTRGulfHaradBannerBearerEntity extends LOTRGulfHaradWarriorEntity implements LOTRBannerBearer {

    public LOTRGulfHaradBannerBearerEntity(EntityType<? extends LOTRGulfHaradBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.HARAD_GULF;
    }
}
