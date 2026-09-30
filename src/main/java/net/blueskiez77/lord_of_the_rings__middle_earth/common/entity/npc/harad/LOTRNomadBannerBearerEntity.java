package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityNomadBannerBearer: a nomad warrior who carries the banner of the nomads. */
public class LOTRNomadBannerBearerEntity extends LOTRNomadWarriorEntity implements LOTRBannerBearer {

    public LOTRNomadBannerBearerEntity(EntityType<? extends LOTRNomadBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.HARAD_NOMAD;
    }
}
