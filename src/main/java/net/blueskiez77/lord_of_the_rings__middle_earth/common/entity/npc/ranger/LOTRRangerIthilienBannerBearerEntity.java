package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityRangerIthilienBannerBearer: an Ithilien ranger who carries Ithilien's banner. */
public class LOTRRangerIthilienBannerBearerEntity extends LOTRRangerIthilienEntity implements LOTRBannerBearer {

    public LOTRRangerIthilienBannerBearerEntity(EntityType<? extends LOTRRangerIthilienBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.ITHILIEN;
    }
}
