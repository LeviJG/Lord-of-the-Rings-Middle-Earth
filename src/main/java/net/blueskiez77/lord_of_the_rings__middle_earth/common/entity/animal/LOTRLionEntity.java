package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityLion. */
public class LOTRLionEntity extends LOTRLionBaseEntity {

    public LOTRLionEntity(EntityType<? extends LOTRLionEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean isMale() {
        return true;
    }

    @Override
    public net.minecraft.world.item.Item getLionRug() {
        return net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.LION_RUG;
    }
}
