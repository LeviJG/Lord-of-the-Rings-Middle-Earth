package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.world.level.LevelAccessor;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAvoidWithChanceGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDikDik: a tiny antelope. Not an animal in the breeding sense --
 * an EntityCreature that never mates and despawns like an ambient creature
 * (LOTRAmbientCreature). It bolts from lions, and now and then from players.
 * Its calls are the deer's, pitched up by 1.3.
 *
 * <p>It drops nothing, as the original had no dropFewItems.
 */
public class LOTRDikDikEntity extends PathfinderMob {

    public LOTRDikDikEntity(EntityType<? extends LOTRDikDikEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LOTRLionBaseEntity.class, 12.0f, 1.5, 2.0));
        this.goalSelector.addGoal(1, new LOTRAvoidWithChanceGoal<>(this, Player.class, 12.0f, 1.5, 2.0, 0.1f));
        this.goalSelector.addGoal(2, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.2));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    /** getBlockPathWeight: grass is best, otherwise the brighter the better. */
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) ? 10.0f : level.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LOTRSounds.DEER_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.DEER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.DEER_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 1.3f;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }

    /** getCanSpawnHere: among plants, on its biome's top block, in light enough (LOTRAmbientSpawnChecks). */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        return super.checkSpawnRules(level, reason)
                && LOTRAmbientSpawnChecks.canSpawn(this, level, 8, 4, 32, 4, LOTRAmbientSpawnChecks.PLANTS);
    }
}
