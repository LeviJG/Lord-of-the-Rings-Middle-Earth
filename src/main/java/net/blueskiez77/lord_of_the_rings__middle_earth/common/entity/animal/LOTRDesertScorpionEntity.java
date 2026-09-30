package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * LOTREntityDesertScorpion: never the largest size, and fireproof (the entity
 * type's fireImmune).
 *
 * <p>NOT ported yet: LOTRBiomeGenNearHarad.ImmuneToHeat (D10), and
 * getCanSpawnHere / isValidLightLevel's pyramid and depth rules (D11/D12).
 */
public class LOTRDesertScorpionEntity extends LOTRScorpionEntity {

    public LOTRDesertScorpionEntity(EntityType<? extends LOTRDesertScorpionEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public int getRandomScorpionScale() {
        return this.random.nextInt(2);
    }
}
