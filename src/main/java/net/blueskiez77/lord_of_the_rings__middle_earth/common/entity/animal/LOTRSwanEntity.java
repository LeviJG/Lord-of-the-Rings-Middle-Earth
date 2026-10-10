package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.world.level.LevelAccessor;
import java.util.List;
import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntitySwan. Half of all swans (by UUID) are violent: they go for
 * whatever hurt them and any player within four blocks; the rest flee. Every
 * swan hisses at players nearby now and then, turning to face them, and
 * pecks when it bites -- entity events 21 and 20, for the neck. It swims,
 * floats rather than sinks, and flaps down gently. Drops are
 * {@code lotr:entities/swan}: swan feathers.
 *
 * <p>Left out: the "wreck Balrogs" hooks, behind a static flag the original
 * never set.
 */
public class LOTRSwanEntity extends PathfinderMob {

    private static final Random VIOLENCE_RAND = new Random();
    private static final byte EVENT_PECK = 100;
    private static final byte EVENT_HISS = 101;

    public float flapPhase;
    public float flapPower;
    public float prevFlapPower;
    public float prevFlapPhase;
    private float flapAccel = 1.0f;
    public int peckTime;
    public int peckLength;
    private int timeUntilHiss;

    public LOTRSwanEntity(EntityType<? extends LOTRSwanEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    /** isViolentSwan: fixed by the UUID. */
    public boolean isViolentSwan() {
        VIOLENCE_RAND.setSeed(getUUID().getLeastSignificantBits());
        return VIOLENCE_RAND.nextBoolean();
    }

    /**
     * The original chose between the attack and flee tasks on its first AI
     * update, once the UUID was settled; the goals ask it each time instead.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 1.4, true) {
            @Override
            public boolean canUse() {
                return isViolentSwan() && super.canUse();
            }
        });
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.8) {
            @Override
            public boolean canUse() {
                return !isViolentSwan() && super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 10.0f, 0.05f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false,
                (target, level) -> target.isAlive() && distanceToSqr(target) < 16.0));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (target.hurtServer(level, damageSources().mobAttack(this), (float) getAttributeValue(Attributes.ATTACK_DAMAGE))) {
            level.broadcastEntityEvent(this, EVENT_PECK);
            return true;
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_PECK) {
            this.peckLength = 10;
        } else if (id == EVENT_HISS) {
            this.peckLength = 40;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public float getPeckAngle(float tick) {
        if (this.peckLength == 0) {
            return 0.0f;
        }
        float peck = (this.peckTime + tick) / this.peckLength;
        float cutoff = 0.2f;
        if (peck < cutoff) {
            return peck / cutoff;
        }
        if (peck < 1.0f - cutoff) {
            return 1.0f;
        }
        return (1.0f - peck) / cutoff;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.prevFlapPhase = this.flapPhase;
        this.prevFlapPower = this.flapPower;
        this.flapPower = Math.max(0.0f, Math.min(this.flapPower + (onGround() ? -0.02f : 0.05f), 1.0f));
        if (!onGround()) {
            this.flapAccel = 0.6f;
        }
        this.flapPhase += this.flapAccel;
        this.flapAccel *= 0.95f;
        Vec3 m = getDeltaMovement();
        if (!onGround() && m.y < 0.0) {
            setDeltaMovement(m.multiply(1.0, 0.6, 1.0));
        }
        m = getDeltaMovement();
        if (isInWater() && m.y < 0.0) {
            setDeltaMovement(m.multiply(1.0, 0.01, 1.0));
        }
        LivingEntity target = getTarget();
        if (!level().isClientSide() && target != null
                && (!target.isAlive() || target instanceof Player player && player.isCreative())) {
            setTarget(null);
        }
        if (this.peckLength > 0) {
            if (++this.peckTime >= this.peckLength) {
                this.peckTime = 0;
                this.peckLength = 0;
            }
        } else {
            this.peckTime = 0;
        }
    }

    /** updateAITasks: the hiss. */
    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        if (this.timeUntilHiss <= 0) {
            if (getTarget() == null && this.random.nextInt(3) == 0) {
                List<Player> players = level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(8.0),
                        p -> !p.isCreative() && !p.isSpectator());
                if (!players.isEmpty()) {
                    Player player = players.get(this.random.nextInt(players.size()));
                    getNavigation().stop();
                    float look = (float) Math.toDegrees(Math.atan2(player.getZ() - getZ(), player.getX() - getX()));
                    setYRot(look - 90.0f);
                    setYHeadRot(look - 90.0f);
                    level.broadcastEntityEvent(this, EVENT_HISS);
                    playSound(LOTRSounds.SWAN_HISS, getSoundVolume(), getVoicePitch());
                    this.timeUntilHiss = 80 + this.random.nextInt(80);
                }
            }
        } else {
            --this.timeUntilHiss;
        }
    }

    /** getBlockPathWeight: the biome's top block (grass until the LOTR biomes), else the light. */
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome biome = net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes.of(level.getBiome(pos));
        net.minecraft.world.level.block.state.BlockState top = biome == null ? Blocks.GRASS_BLOCK.defaultBlockState() : biome.topBlock;
        return level.getBlockState(pos.below()).is(top.getBlock()) ? 10.0f : level.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 1 + this.random.nextInt(2);
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return true;
    }

    /** getCanSpawnHere: by water, on its biome's top block, in light enough (LOTRAmbientSpawnChecks). */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        return super.checkSpawnRules(level, reason)
                && LOTRAmbientSpawnChecks.canSpawn(this, level, 16, 8, 40, 2, LOTRAmbientSpawnChecks.WATER);
    }
}
