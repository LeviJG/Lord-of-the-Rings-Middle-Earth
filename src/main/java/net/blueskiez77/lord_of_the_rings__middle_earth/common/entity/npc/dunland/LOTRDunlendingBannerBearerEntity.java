package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityDunlendingBannerBearer: a warrior who carries the banner of Dunland. */
public class LOTRDunlendingBannerBearerEntity extends LOTRDunlendingWarriorEntity implements LOTRBannerBearer {

    public LOTRDunlendingBannerBearerEntity(EntityType<? extends LOTRDunlendingBannerBearerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRBannerType getBannerType() {
        return LOTRBannerType.DUNLAND;
    }
}
