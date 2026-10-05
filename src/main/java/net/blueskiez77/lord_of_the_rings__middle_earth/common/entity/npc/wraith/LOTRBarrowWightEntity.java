package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBarrowWight: a wight of the barrows -- 50 strong, hitting for 6,
 * seeing 40 blocks, undead, hostile to all and seeking them out by day or
 * dark. Its blow slows, weakens the arm and withers (the difficulty's number
 * times itself and five, halved, in seconds). It drifts soundlessly, trailing
 * Morgul sparks and smoke, and lets its victim know it is watched (the target
 * is synced for that). Slain, it leaves bones and, half the time, some of the
 * barrows' grave-goods; it can drop rares.
 *
 * <p>NOT ported yet: the darkening of the screen of the one it hunts, and
 * the barrows' ambience about it (LOTRTickHandlerClient, with the HUD); its
 * spawning above y 62 on the biome's top block (D10); the killBarrowWight
 * achievement (D7).
 */
public class LOTRBarrowWightEntity extends LOTRNPCEntity {

    private static final EntityDataAccessor<Integer> DATA_TARGET_ID =
            SynchedEntityData.defineId(LOTRBarrowWightEntity.class, EntityDataSerializers.INT);

    /** attackEffects. */
    private static final java.util.List<Holder<MobEffect>> ATTACK_EFFECTS =
            java.util.List.of(MobEffects.SLOWNESS, MobEffects.MINING_FATIGUE, MobEffects.WITHER);

    public LOTRBarrowWightEntity(EntityType<? extends LOTRBarrowWightEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        this.spawnsInDarkness = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 6.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TARGET_ID, -1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(2, getWightAttackAI());
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 12.0f, 0.02f));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mob.class, 12.0f, 0.02f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    protected Goal getWightAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, false);
    }

    /** getTargetEntityID: whom it hunts, for that one's client; -1 for no one. */
    public int getTargetEntityID() {
        return this.entityData.get(DATA_TARGET_ID);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target, boolean speak) {
        super.setTarget(target, speak);
        if (!level().isClientSide()) {
            this.entityData.set(DATA_TARGET_ID, target == null ? -1 : target.getId());
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            int difficulty = level.getDifficulty().getId();
            int duration = difficulty * (difficulty + 5) / 2;
            if (target instanceof LivingEntity living && duration > 0) {
                for (Holder<MobEffect> effect : ATTACK_EFFECTS) {
                    living.addEffect(new MobEffectInstance(effect, duration * 20, 0), this);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide()) {
            double x = getX() + getBbWidth() * Mth.nextDouble(this.random, -0.5, 0.5);
            double y = getY() + getBbHeight() * Mth.nextDouble(this.random, 0.4, 0.8);
            double z = getZ() + getBbWidth() * Mth.nextDouble(this.random, -0.5, 0.5);
            double dx = Mth.nextDouble(this.random, -0.1, 0.1);
            double dy = Mth.nextDouble(this.random, -0.2, -0.05);
            double dz = Mth.nextDouble(this.random, -0.1, 0.1);
            level().addParticle(this.random.nextBoolean() ? LOTRParticles.MORGUL_PORTAL : ParticleTypes.SMOKE,
                    x, y, z, dx, dy, dz);
        }
    }

    /** func_145780_a: it makes no sound as it goes. */
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public boolean canDropRares() {
        return true;
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = 1 + this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, Items.BONE);
        }
        if (this.random.nextBoolean()) {
            dropChestContents(level, LOTRChestContents.BARROW_DOWNS, 1, 2 + looting + 1);
        }
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.HOSTILE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.WIGHT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.WIGHT_DEATH;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 4 + this.random.nextInt(5);
    }

    /** canReEquipHired: its player cannot dress it. */
    @Override
    public boolean canReEquipHired(int slot, ItemStack stack) {
        return false;
    }
}
