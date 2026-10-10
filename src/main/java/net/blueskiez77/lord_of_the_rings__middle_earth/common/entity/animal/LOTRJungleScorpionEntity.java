package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.EntitySpawnReason;

/**
 * LOTREntityJungleScorpion.
 */
public class LOTRJungleScorpionEntity extends LOTRScorpionEntity {

    public LOTRJungleScorpionEntity(EntityType<? extends LOTRJungleScorpionEntity> type, Level level) {
        super(type, level);
    }

    /** getCanSpawnHere: underground, or one time in a hundred above. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        return super.checkSpawnRules(level, reason) && (getY() < 60.0 || this.random.nextInt(100) == 0);
    }
}
