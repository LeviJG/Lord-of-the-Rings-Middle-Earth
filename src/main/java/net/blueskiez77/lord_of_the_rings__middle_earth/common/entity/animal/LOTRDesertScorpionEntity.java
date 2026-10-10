package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.EntitySpawnReason;

/**
 * LOTREntityDesertScorpion: never the largest size, and fireproof (the entity
 * type's fireImmune).
 */
public class LOTRDesertScorpionEntity extends LOTRScorpionEntity implements net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRNearHaradBiome.ImmuneToHeat {

    public LOTRDesertScorpionEntity(EntityType<? extends LOTRDesertScorpionEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public int getRandomScorpionScale() {
        return this.random.nextInt(2);
    }

    /**
     * getCanSpawnHere: from a spawner (LOTRMobSpawnerCondition: the pyramids' spawners and the
     * wraith's spawner chest), underground, or one time in five hundred above. (The original's
     * pyramidSpawned exemption was never set.)
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        return super.checkSpawnRules(level, reason)
                && (reason == EntitySpawnReason.SPAWNER || getY() < 60.0 || this.random.nextInt(500) == 0);
    }
}
