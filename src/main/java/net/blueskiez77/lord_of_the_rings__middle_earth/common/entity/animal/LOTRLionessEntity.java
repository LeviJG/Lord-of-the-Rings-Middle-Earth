package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityLioness. */
public class LOTRLionessEntity extends LOTRLionBaseEntity {

    public LOTRLionessEntity(EntityType<? extends LOTRLionessEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean isMale() {
        return false;
    }
}
