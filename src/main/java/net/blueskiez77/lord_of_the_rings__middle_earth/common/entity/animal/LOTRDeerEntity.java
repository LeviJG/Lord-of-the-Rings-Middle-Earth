package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDeer. Stags carry antlers; does and fawns do not.
 *
 * <p>The drops -- zero to two leather and zero to two venison, cooked if it
 * died burning, each plus up to the looting level -- are the loot table
 * {@code lotr:entities/deer}. The skin is picked on the client from the
 * entity's UUID (LOTRRandomSkinEntity); the UUID reaches the client with the
 * spawn packet now, so the interface's setUniqueID hook is not needed.
 */
public class LOTRDeerEntity extends LOTRAnimalMF {

    /** dataWatcher 20. */
    private static final EntityDataAccessor<Boolean> DATA_MALE =
            SynchedEntityData.defineId(LOTRDeerEntity.class, EntityDataSerializers.BOOLEAN);

    public LOTRDeerEntity(EntityType<? extends LOTRDeerEntity> type, Level level) {
        super(type, level);
        setMale(this.random.nextBoolean());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    /**
     * The original's tasks. getNavigator().setAvoidsWater(true) with
     * EntityAIWander is vanilla's WaterAvoidingRandomStrollGoal, as for cows.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.8));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2, stack -> stack.is(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.4));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.4));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_MALE, false);
    }

    /** EntityAnimal.isBreedingItem: wheat. */
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.WHEAT);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        LOTRDeerEntity deer = LOTREntities.DEER.create(level, EntitySpawnReason.BREEDING);
        if (deer != null) {
            deer.setMale(this.random.nextBoolean());
        }
        return deer;
    }

    @Override
    public Class<?> getAnimalMFBaseClass() {
        return getClass();
    }

    @Override
    public boolean isMale() {
        return this.entityData.get(DATA_MALE);
    }

    public void setMale(boolean male) {
        this.entityData.set(DATA_MALE, male);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("DeerMale", isMale());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setMale(input.getBooleanOr("DeerMale", false));
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
    protected float getSoundVolume() {
        return 0.5f;
    }

    /** getTalkInterval. */
    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }
}
