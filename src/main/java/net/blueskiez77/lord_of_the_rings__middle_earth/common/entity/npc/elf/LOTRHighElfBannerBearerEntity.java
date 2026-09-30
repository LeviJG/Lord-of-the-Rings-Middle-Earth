package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityHighElfBannerBearer: a warrior who carries the banner of Lindon, always on foot. */
public class LOTRHighElfBannerBearerEntity extends LOTRHighElfWarriorEntity implements LOTRBannerBearer {

    public LOTRHighElfBannerBearerEntity(EntityType<? extends LOTRHighElfBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.HIGH_ELF;
    }
}
