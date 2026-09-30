package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRLionChaseGoal;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityLionBase: lions and lionesses, which breed with each other.
 *
 * <p>Peaceable until provoked. Hurting an adult makes it angry for ten
 * seconds after it loses its target (the {@code Angry} tick); hurting a cub
 * angers every adult within 12 blocks. An angry adult attacks, and also turns
 * on any player it can see; a cub only panics. The original did this by
 * adding and removing tasks as the lion aged -- which, because it never
 * updated prevIsChild, it re-ran every tick for adults -- and that is
 * expressed here as goals gated on age and anger. (A cub also kept the
 * nearest-player target task, but with no attack task it never acted on it,
 * so that is left out.)
 *
 * <p>An angry lion cannot be fed or interacted with, and falls out of love.
 * Drops are the loot tables {@code lotr:entities/lion} and
 * {@code lotr:entities/lioness}: two to four furs, one or two lion meat plus
 * looting, and -- only when a player killed it -- a rug, 1 in (30 - 5 per
 * looting level).
 */
public abstract class LOTRLionBaseEntity extends LOTRAnimalMF {

    /** dataWatcher 20. */
    private static final EntityDataAccessor<Boolean> DATA_HOSTILE =
            SynchedEntityData.defineId(LOTRLionBaseEntity.class, EntityDataSerializers.BOOLEAN);

    private int hostileTick;

    protected LOTRLionBaseEntity(EntityType<? extends LOTRLionBaseEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    private boolean angryAdult() {
        return !isBaby() && this.hostileTick > 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 1.5, false) {
            @Override
            public boolean canUse() {
                return angryAdult() && super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.5) {
            @Override
            public boolean canUse() {
                return isBaby() && super.canUse();
            }
        });
        // LOTREntityAIMFMate: vanilla's breeding goal, looking for any lion.
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0, LOTRLionBaseEntity.class));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.4, this::isFood, false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.4));
        this.goalSelector.addGoal(6, new LOTRLionChaseGoal(this, 1.5));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null) {
            @Override
            public boolean canUse() {
                return angryAdult() && super.canUse();
            }
        });
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HOSTILE, false);
    }

    /** attackEntityAsMob: just the attack damage, no knockback. */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        return target.hurtServer(level, damageSources().mobAttack(this),
                (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt && source.getEntity() instanceof LivingEntity attacker) {
            if (isBaby()) {
                for (LOTRLionBaseEntity lion : level.getEntitiesOfClass(LOTRLionBaseEntity.class,
                        getBoundingBox().inflate(12.0), lion -> lion != this && !lion.isBaby())) {
                    lion.becomeAngryAt(attacker);
                }
            } else {
                becomeAngryAt(attacker);
            }
        }
        return hurt;
    }

    public void becomeAngryAt(LivingEntity entity) {
        setTarget(entity);
        this.hostileTick = 200;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level() instanceof ServerLevel) {
            LivingEntity target = getTarget();
            if (target != null && (!target.isAlive() || target instanceof Player player && player.isCreative())) {
                setTarget(null);
            }
            if (this.hostileTick > 0 && getTarget() == null) {
                --this.hostileTick;
            }
            setHostile(this.hostileTick > 0);
            if (isHostile()) {
                resetLove();
            }
        }
    }

    public boolean isHostile() {
        return this.entityData.get(DATA_HOSTILE);
    }

    public void setHostile(boolean hostile) {
        this.entityData.set(DATA_HOSTILE, hostile);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (isHostile()) {
            return InteractionResult.PASS;
        }
        return super.mobInteract(player, hand);
    }

    /** isBreedingItem: Items.fish, which in 1.7.10 was every raw fish. */
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.SALMON) || stack.is(Items.TROPICAL_FISH)
                || stack.is(Items.PUFFERFISH);
    }

    /** createChild: a lion or a lioness, even odds. */
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return (this.random.nextBoolean() ? LOTREntities.LION : LOTREntities.LIONESS)
                .create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    public Class<?> getAnimalMFBaseClass() {
        return LOTRLionBaseEntity.class;
    }

    /** getExperiencePoints: 2-4. */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 2 + this.random.nextInt(3);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Angry", this.hostileTick);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.hostileTick = input.getIntOr("Angry", 0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LOTRSounds.LION_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.LION_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.LION_DEATH;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }
}
