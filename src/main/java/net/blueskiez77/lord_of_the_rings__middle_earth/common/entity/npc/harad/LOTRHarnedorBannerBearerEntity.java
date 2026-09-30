package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityHarnedorBannerBearer: a Harnennor warrior who carries the banner of Near Harad. */
public class LOTRHarnedorBannerBearerEntity extends LOTRHarnedorWarriorEntity implements LOTRBannerBearer {

    public LOTRHarnedorBannerBearerEntity(EntityType<? extends LOTRHarnedorBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.NEAR_HARAD;
    }
}
