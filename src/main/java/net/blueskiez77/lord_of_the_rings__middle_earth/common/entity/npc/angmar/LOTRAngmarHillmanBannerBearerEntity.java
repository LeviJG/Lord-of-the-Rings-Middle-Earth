package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityAngmarHillmanBannerBearer: a hillman warrior who carries the banner of Rhudaur. */
public class LOTRAngmarHillmanBannerBearerEntity extends LOTRAngmarHillmanWarriorEntity implements LOTRBannerBearer {

    public LOTRAngmarHillmanBannerBearerEntity(EntityType<? extends LOTRAngmarHillmanBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.RHUDAUR;
    }
}
