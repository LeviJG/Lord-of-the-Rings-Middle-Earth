package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAnimalMF: an animal that is male or female, and mates only with
 * the other sex of its own kind.
 *
 * <p>getPickedResult, which gave the creature's LOTR spawn egg, is vanilla's
 * Mob.getPickResult now: it finds the egg through the egg's entity data.
 */
public abstract class LOTRAnimalMF extends Animal {

    protected LOTRAnimalMF(EntityType<? extends LOTRAnimalMF> type, Level level) {
        super(type, level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    /** canMateWith: same kind, both in love, and one of each sex. */
    @Override
    public boolean canMate(Animal partner) {
        if (partner == this || !(partner instanceof LOTRAnimalMF mfMate)) {
            return false;
        }
        if (getAnimalMFBaseClass().equals(mfMate.getAnimalMFBaseClass()) && isInLove() && partner.isInLove()) {
            return isMale() != mfMate.isMale();
        }
        return false;
    }

    /** The class two animals must share to mate; lions and lionesses share one. */
    public abstract Class<?> getAnimalMFBaseClass();

    public abstract boolean isMale();
}
