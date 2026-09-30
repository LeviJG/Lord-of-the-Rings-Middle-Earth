package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityWoodElfBannerBearer: a warrior who carries the banner of the Woodland Realm, always on foot. */
public class LOTRWoodElfBannerBearerEntity extends LOTRWoodElfWarriorEntity implements LOTRBannerBearer {

    public LOTRWoodElfBannerBearerEntity(EntityType<? extends LOTRWoodElfBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.MIRKWOOD;
    }
}
