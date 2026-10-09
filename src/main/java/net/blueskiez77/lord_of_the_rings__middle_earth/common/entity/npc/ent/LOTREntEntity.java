package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent;

import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLeafParticleOptions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCorruptMallornBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREntHealSaplingGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTREntDraughtItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityEnt: a shepherd of the trees -- 100 strong, hitting for 7 and
 * throwing its victim back, seeing 24 blocks, of Fangorn. It seeks out its
 * people's enemies, but when not fighting goes to heal any corrupt mallorn
 * nearby (LOTREntHealSaplingGoal); struck, it stops, and does not start again
 * until it has no target. Slain by a player beside a sapling it was healing,
 * it counts against that sapling, and the third such death calls up the
 * Mallorn Ent. It has an Ent's name, a slow heavy tread,
 * its eyes blink shut now and then, and it may have extra branches on its
 * head. Slain by a player it may leave an Ent-draught.
 *
 * <p>NOT ported yet: the killEnt and talkEnt achievements (D7).
 */
public class LOTREntEntity extends LOTRTreeEntity {

    private static final EntityDataAccessor<Boolean> DATA_HEALING =
            SynchedEntityData.defineId(LOTREntEntity.class, EntityDataSerializers.BOOLEAN);

    private final Random branchRand = new Random();
    /** Client side: ticks left with the eyes shut. */
    public int eyesClosed;
    public @Nullable BlockPos saplingHealTarget;
    public boolean canHealSapling = true;

    public LOTREntEntity(EntityType<? extends LOTREntEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 7.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HEALING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTREntHealSaplingGoal(this, 1.5));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 2.0, false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0f, 0.02f));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Mob.class, 10.0f, 0.02f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    public boolean isHealingSapling() {
        return this.entityData.get(DATA_HEALING);
    }

    public void setHealingSapling(boolean flag) {
        this.entityData.set(DATA_HEALING, flag);
    }

    /** getExtraHeadBranches: none half the time, else two to five, fixed by the Ent's UUID. */
    public int getExtraHeadBranches() {
        long l = getUUID().getLeastSignificantBits();
        l = l * 365620672396L ^ l * 12784892284L ^ l;
        l = l * l * 18569660L + l * 6639092L;
        this.branchRand.setSeed(l);
        if (this.branchRand.nextBoolean()) {
            return 0;
        }
        return 2 + this.branchRand.nextInt(4);
    }

    /** attackEntityAsMob: and the one hit is thrown back and up. */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            float knockback = 1.5f;
            float yaw = getYRot() * Mth.DEG_TO_RAD;
            target.push(-Mth.sin(yaw) * knockback * 0.5f, 0.15, Mth.cos(yaw) * knockback * 0.5f);
            return true;
        }
        return false;
    }

    /** Struck by someone, it leaves off healing; in a fight, it will not start again. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean flag = super.hurtServer(level, source, damage);
        if (flag) {
            if (source.getEntity() != null) {
                setHealingSapling(false);
            }
            if (getTarget() != null) {
                this.canHealSapling = false;
            }
        }
        return flag;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target, boolean speak) {
        super.setTarget(target, speak);
        if (getTarget() == null) {
            this.canHealSapling = true;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide()) {
            if (this.eyesClosed > 0) {
                --this.eyesClosed;
            } else if (this.random.nextInt(400) == 0) {
                this.eyesClosed = 30;
            }
            if (isHealingSapling()) {
                // Gold leaves falling from its outstretched branches.
                for (int l = 0; l < 2; ++l) {
                    float angle = (this.yHeadRot + 90.0f + Mth.randomBetween(this.random, -40.0f, 40.0f)) * Mth.DEG_TO_RAD;
                    double d = getX() + Mth.cos(angle) * 1.5;
                    double d1 = getBoundingBox().minY + getBbHeight() * Mth.randomBetween(this.random, 0.3f, 0.6f);
                    double d2 = getZ() + Mth.sin(angle) * 1.5;
                    level().addParticle(LOTRLeafParticleOptions.of(LOTRParticles.LEAF_GOLD, 30, 0), d, d1, d2,
                            Mth.cos(angle) * 0.06, -0.03, Mth.sin(angle) * 0.06);
                }
            }
        }
    }

    /** onDeath: slain by a player, it counts against the sapling it was healing. */
    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (level() instanceof ServerLevel level && source.getEntity() instanceof Player && this.saplingHealTarget != null) {
            BlockState state = level.getBlockState(this.saplingHealTarget);
            if (state.is(LOTRDecorationBlocks.CORRUPT_MALLORN)) {
                int kills = state.getValue(LOTRCorruptMallornBlock.KILLS) + 1;
                if (kills >= LOTRCorruptMallornBlock.ENT_KILLS) {
                    LOTRCorruptMallornBlock.summonEntBoss(level, this.saplingHealTarget);
                } else {
                    level.setBlockAndUpdate(this.saplingHealTarget, state.setValue(LOTRCorruptMallornBlock.KILLS, kills));
                }
            }
        }
    }

    /** Slain by a player, one in ten (better with looting) an Ent-draught of any kind. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        if (killedByPlayer) {
            int dropChance = Math.max(10 - looting * 2, 1);
            if (this.random.nextInt(dropChance) == 0) {
                spawnAtLocation(level, LOTREntDraughtItem.stack(LOTRFoodItems.ENT_DRAUGHT,
                        this.random.nextInt(LOTREntDraughtItem.COUNT)), 0.0f);
            }
        }
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.FANGORN;
    }

    @Override
    public float getAlignmentBonus() {
        return 3.0f;
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getEntName(this.random));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "ent/ent/friendly" : "ent/ent/hostile";
    }

    @Override
    protected float getSoundVolume() {
        return 1.5f;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(LOTRSounds.ENT_STEP, 0.75f, getVoicePitch());
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 5 + this.random.nextInt(6);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (this.saplingHealTarget != null) {
            output.putInt("SaplingHealX", this.saplingHealTarget.getX());
            output.putInt("SaplingHealY", this.saplingHealTarget.getY());
            output.putInt("SaplingHealZ", this.saplingHealTarget.getZ());
        }
    }

    /** The name was once kept as "EntName". */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getString("EntName").ifPresent(this.familyInfo::setName);
        input.getInt("SaplingHealX").ifPresent(x -> this.saplingHealTarget = new BlockPos(x,
                input.getIntOr("SaplingHealY", 0), input.getIntOr("SaplingHealZ", 0)));
    }
}
