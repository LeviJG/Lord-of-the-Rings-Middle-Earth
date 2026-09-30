package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityDorwinionElfBannerBearer: a warrior of Bladorthin who carries the banner of Dorwinion. */
public class LOTRDorwinionElfBannerBearerEntity extends LOTRDorwinionElfWarriorEntity implements LOTRBannerBearer {

    public LOTRDorwinionElfBannerBearerEntity(EntityType<? extends LOTRDorwinionElfBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.DORWINION;
    }
}
