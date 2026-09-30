package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRTrollSnowballEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntitySnowTroll: a small troll of the snows, four fifths the size and
 * 40 strong, untroubled by frost, with no name of its own and no speech, who
 * will not be tickled. Its blows slow (the difficulty's number times itself
 * and five, halved, in seconds); within twelve blocks it closes to fight,
 * further off it throws snowballs that hit for 3. Turned to stone by the sun
 * it bursts into snow. It leaves fur and snowballs, and only half the time
 * what it has eaten.
 *
 * <p>NOT ported yet: the killSnowTroll achievement (D7).
 */
public class LOTRSnowTrollEntity extends LOTRTrollEntity {

    private static final EntityDataAccessor<Boolean> DATA_THROWING =
            SynchedEntityData.defineId(LOTRSnowTrollEntity.class, EntityDataSerializers.BOOLEAN);

    private @Nullable Goal meleeAttackAI;
    private @Nullable Goal rangedAttackAI;

    public LOTRSnowTrollEntity(EntityType<? extends LOTRSnowTrollEntity> type, Level level) {
        super(type, level);
        this.isImmuneToFrost = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRTrollEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_THROWING, false);
    }

    @Override
    protected Goal getTrollAttackAI() {
        return meleeAttackAI();
    }

    private Goal meleeAttackAI() {
        if (this.meleeAttackAI == null) {
            this.meleeAttackAI = new LOTRAttackOnCollideGoal(this, 1.6, false);
        }
        return this.meleeAttackAI;
    }

    private Goal rangedAttackAI() {
        if (this.rangedAttackAI == null) {
            this.rangedAttackAI = new LOTRRangedAttackGoal(this, 1.2, 20, 30, 24.0f);
        }
        return this.rangedAttackAI;
    }

    @Override
    public float getTrollScale() {
        return 0.8f;
    }

    @Override
    public boolean hasTrollName() {
        return false;
    }

    @Override
    public double getMeleeRange() {
        return 12.0;
    }

    public boolean isThrowingSnow() {
        return this.entityData.get(DATA_THROWING);
    }

    public void setThrowingSnow(boolean flag) {
        this.entityData.set(DATA_THROWING, flag);
    }

    @Override
    public boolean isThrowing() {
        return isThrowingSnow();
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        Goal melee = meleeAttackAI();
        Goal ranged = rangedAttackAI();
        this.goalSelector.removeGoal(melee);
        this.goalSelector.removeGoal(ranged);
        if (mode == AttackMode.MELEE) {
            this.goalSelector.addGoal(3, melee);
        } else if (mode == AttackMode.RANGED) {
            this.goalSelector.addGoal(3, ranged);
        }
        setThrowingSnow(mode == AttackMode.RANGED);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            int difficulty = level.getDifficulty().getId();
            int duration = difficulty * (difficulty + 5) / 2;
            if (target instanceof LivingEntity living && duration > 0) {
                living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration * 20, 0), this);
            }
            return true;
        }
        return false;
    }

    /** A snowball, thrown as an arrow at six tenths more than the shot's strength. */
    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        LOTRTrollSnowballEntity snowball = new LOTRTrollSnowballEntity(level(), this);
        aimLikeArrow(snowball, target, power * 1.6f, 0.5f);
        level().addFreshEntity(snowball);
        playSound(SoundEvents.ARROW_SHOOT, 1.0f, 1.0f / (this.random.nextFloat() * 0.4f + 0.8f));
        swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public boolean canTrollBeTickled(Player player) {
        return false;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return null;
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int furs = 1 + this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < furs; ++l) {
            spawnAtLocation(level, LOTRMaterialItems.FUR);
        }
        int snows = 2 + this.random.nextInt(4) + this.random.nextInt(looting * 2 + 1);
        for (int l = 0; l < snows; ++l) {
            spawnAtLocation(level, Items.SNOWBALL);
        }
    }

    @Override
    protected void dropTrollItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextBoolean()) {
            super.dropTrollItems(level, killedByPlayer, looting);
        }
    }

    /** onTrollDeathBySun: it bursts into snow. */
    @Override
    public void onTrollDeathBySun() {
        playSound(LOTRSounds.TROLL_TRANSFORM, getSoundVolume(), getVoicePitch());
        level().broadcastEntityEvent(this, (byte) 15);
        discard();
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 15) {
            for (int l = 0; l < 64; ++l) {
                level().addParticle(ParticleTypes.ITEM_SNOWBALL, getX() + this.random.nextGaussian() * getBbWidth() * 0.5,
                        getY() + this.random.nextDouble() * getBbHeight(),
                        getZ() + this.random.nextGaussian() * getBbWidth() * 0.5, 0.0, 0.0, 0.0);
            }
        }
    }
}
