package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityFlamingo. Wades rather than avoids water, flutters down instead of
 * falling hard, and now and then, standing in water, plunges its head under to
 * fish for ten seconds -- the fishing timer, synced for the model, with
 * bubbles on the client. Being hurt or falling in love cuts the fishing short.
 *
 * <p>The wing flap is the chicken's: {@link #flap} and friends are
 * EntityChicken's field_752_b etc., kept under their chicken names in 26.2.
 * Drops are {@code lotr:entities/flamingo}: getDropItem's feathers.
 */
public class LOTRFlamingoEntity extends Animal {

    public static final int NECK_TIME = 20;
    public static final int FISHING_TIME = 160;
    public static final int FISHING_TIME_TOTAL = 200;

    /** dataWatcher 16: the previous fishing tick in the high half, the current in the low. */
    private static final EntityDataAccessor<Integer> DATA_FISHING =
            SynchedEntityData.defineId(LOTRFlamingoEntity.class, EntityDataSerializers.INT);

    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 5.0f;

    public LOTRFlamingoEntity(EntityType<? extends LOTRFlamingoEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    /** setAvoidsWater(false) with EntityAIWander: the plain stroll, water and all. */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.3));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2, this::isFood, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.2));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FISHING, 0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    /** isBreedingItem: Items.fish, which in 1.7.10 was every raw fish. */
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.SALMON) || stack.is(Items.TROPICAL_FISH)
                || stack.is(Items.PUFFERFISH);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return LOTREntities.FLAMINGO.create(level, EntitySpawnReason.BREEDING);
    }

    public int getFishingTickCur() {
        return this.entityData.get(DATA_FISHING) & 0xFFFF;
    }

    public int getFishingTickPre() {
        return this.entityData.get(DATA_FISHING) >> 16;
    }

    public void setFishingTick(int pre, int cur) {
        this.entityData.set(DATA_FISHING, pre << 16 | cur & 0xFFFF);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt && getFishingTickCur() > NECK_TIME) {
            setFishingTick(NECK_TIME, NECK_TIME);
        }
        return hurt;
    }

    /** fall: no fall damage. */
    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        boolean grounded = onGround() || isInWater();
        this.flapSpeed = Mth.clamp(this.flapSpeed + (grounded ? -1 : 4) * 0.3f, 0.0f, 1.0f);
        if (!grounded && this.flapping < 1.0f) {
            this.flapping = 1.0f;
        }
        this.flapping *= 0.9f;
        Vec3 motion = getDeltaMovement();
        if (!grounded && motion.y < 0.0) {
            setDeltaMovement(motion.multiply(1.0, 0.6, 1.0));
        }
        this.flap += this.flapping * 2.0f;

        if (!level().isClientSide() && !isBaby() && !isInLove() && getFishingTickCur() == 0
                && this.random.nextInt(600) == 0 && level().getBlockState(blockPosition()).is(Blocks.WATER)) {
            setFishingTick(FISHING_TIME_TOTAL, FISHING_TIME_TOTAL);
        }
        if (getFishingTickCur() > 0) {
            if (level().isClientSide()) {
                for (int i = 0; i < 3; ++i) {
                    double x = getX() + Mth.nextDouble(this.random, -0.3, 0.3);
                    double y = getBoundingBox().minY + Mth.nextDouble(this.random, -0.3, 0.3);
                    double z = getZ() + Mth.nextDouble(this.random, -0.3, 0.3);
                    level().addParticle(ParticleTypes.BUBBLE, x, y, z, 0.0, 0.0, 0.0);
                }
            } else {
                int cur = getFishingTickCur();
                setFishingTick(cur, cur - 1);
            }
        }
        if (!level().isClientSide() && isInLove() && getFishingTickCur() > NECK_TIME) {
            setFishingTick(NECK_TIME, NECK_TIME);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LOTRSounds.FLAMINGO_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.FLAMINGO_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.FLAMINGO_DEATH;
    }
}
