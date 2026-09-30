package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityBlueDwarfBannerBearer: a warrior who carries the banner of the Blue Mountains. */
public class LOTRBlueDwarfBannerBearerEntity extends LOTRBlueDwarfWarriorEntity implements LOTRBannerBearer {

    public LOTRBlueDwarfBannerBearerEntity(EntityType<? extends LOTRBlueDwarfBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.BLUE_MOUNTAINS;
    }
}
