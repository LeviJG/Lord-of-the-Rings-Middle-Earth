package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityRohanBannerBearer: a warrior who carries the banner of Rohan, always on foot. */
public class LOTRRohanBannerBearerEntity extends LOTRRohirrimWarriorEntity implements LOTRBannerBearer {

    public LOTRRohanBannerBearerEntity(EntityType<? extends LOTRRohanBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.ROHAN;
    }
}
