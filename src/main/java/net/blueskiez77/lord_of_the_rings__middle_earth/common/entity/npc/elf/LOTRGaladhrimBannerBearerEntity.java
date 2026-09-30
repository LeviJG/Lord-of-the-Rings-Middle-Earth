package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityGaladhrimBannerBearer: a warrior who carries the banner of Lothlórien, always on foot. */
public class LOTRGaladhrimBannerBearerEntity extends LOTRGaladhrimWarriorEntity implements LOTRBannerBearer {

    public LOTRGaladhrimBannerBearerEntity(EntityType<? extends LOTRGaladhrimBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.LOTHLORIEN;
    }
}
