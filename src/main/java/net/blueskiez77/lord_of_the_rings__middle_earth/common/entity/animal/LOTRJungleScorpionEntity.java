package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * LOTREntityJungleScorpion. NOT ported yet: getCanSpawnHere's depth rule
 * (below y 60, or 1 in 100), with natural spawning (D12).
 */
public class LOTRJungleScorpionEntity extends LOTRScorpionEntity {

    public LOTRJungleScorpionEntity(EntityType<? extends LOTRJungleScorpionEntity> type, Level level) {
        super(type, level);
    }
}
