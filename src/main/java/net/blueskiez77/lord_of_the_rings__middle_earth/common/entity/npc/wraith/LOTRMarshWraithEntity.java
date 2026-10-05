package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith;

import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLegacyWorld;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRMarshWraithBallEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMarshWraith: a dead face in the Dead Marshes, risen for one who
 * strays there. It fades in over a second and a half, hovers two blocks above
 * the mire and the water, and from twelve blocks casts its cold lights at its
 * mark (LOTRMarshWraithBallEntity) -- nothing touches it but a blade of
 * Wraithbane. Once its mark has been out of the water for five seconds, or is
 * gone, it fades out again. It seeks no one else, ignores webs and water, is
 * undead, and leaves rotten flesh and what the drowned left behind.
 *
 * <p>NOT ported yet: rising in the Dead Marshes for whoever enters them
 * (LOTREventHandler.spawnMarshWraithIfConditionsMet), with the biomes (D10);
 * spawned by an egg it has no mark and fades at once, as it did then. The
 * killMarshWraith achievement (D7).
 */
public class LOTRMarshWraithEntity extends LOTRNPCEntity {

    private static final EntityDataAccessor<Integer> DATA_SPAWN_FADE =
            SynchedEntityData.defineId(LOTRMarshWraithEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_DEATH_FADE =
            SynchedEntityData.defineId(LOTRMarshWraithEntity.class, EntityDataSerializers.INT);

    public @Nullable UUID attackTargetUUID;
    public boolean checkedForAttackTarget;
    public int timeUntilDespawn = -1;

    public LOTRMarshWraithEntity(EntityType<? extends LOTRMarshWraithEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SPAWN_FADE, 0);
        builder.define(DATA_DEATH_FADE, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LOTRRangedAttackGoal(this, 1.6, 40, 40, 12.0f));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    public int getSpawnFadeTime() {
        return this.entityData.get(DATA_SPAWN_FADE);
    }

    public void setSpawnFadeTime(int i) {
        this.entityData.set(DATA_SPAWN_FADE, i);
    }

    public int getDeathFadeTime() {
        return this.entityData.get(DATA_DEATH_FADE);
    }

    public void setDeathFadeTime(int i) {
        this.entityData.set(DATA_DEATH_FADE, i);
    }

    /** attackEntityFrom: only a blow struck by hand with a Wraithbane blade, and not while fading. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        Entity entity = source.getEntity();
        boolean vulnerable = entity instanceof LivingEntity attacker && entity == source.getDirectEntity()
                && LOTRModifiers.has(attacker.getMainHandItem(), LOTRModifier.BANE_WRAITH);
        if (vulnerable && getDeathFadeTime() == 0) {
            boolean flag = super.hurtServer(level, source, damage);
            if (flag) {
                this.timeUntilDespawn = 100;
            }
            return flag;
        }
        return false;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (getSpawnFadeTime() == 30 && getDeathFadeTime() == 0) {
            LOTRMarshWraithBallEntity ball = new LOTRMarshWraithBallEntity(level(), this, target);
            playSound(LOTRSounds.WRAITH_MARSH_WRAITH_SHOOT, 1.0f, 1.0f / (this.random.nextFloat() * 0.4f + 0.8f));
            level().addFreshEntity(ball);
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level().isClientSide()) {
            setDeathFadeTime(30);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide() && !isRemoved()) {
            // It hovers: up to two blocks above anything solid or liquid below it.
            int hover = 2;
            int i = Mth.floor(getX());
            int j = Mth.floor(getY());
            int k = Mth.floor(getZ());
            double newY = getY();
            for (int j1 = 0; j1 <= hover; ++j1) {
                BlockState state = level().getBlockState(new BlockPos(i, j - j1, k));
                if (!LOTRLegacyWorld.isSolid(state) && !LOTRLegacyWorld.isLiquid(state)) {
                    continue;
                }
                newY = Math.max(newY, j + j1 + 1);
            }
            setDeltaMovement(getDeltaMovement().add(0.0, (newY - getY()) * 0.04, 0.0));
        }
        if (this.random.nextBoolean()) {
            level().addParticle(ParticleTypes.SMOKE, getX() + (this.random.nextDouble() - 0.5) * getBbWidth(),
                    getY() + this.random.nextDouble() * getBbHeight(),
                    getZ() + (this.random.nextDouble() - 0.5) * getBbWidth(), 0.0, 0.0, 0.0);
        }
        if (level() instanceof ServerLevel level) {
            if (getTarget() == null && this.attackTargetUUID != null && !this.checkedForAttackTarget) {
                Entity entity = level.getEntity(this.attackTargetUUID);
                if (entity instanceof net.minecraft.world.entity.Mob || entity instanceof Player) {
                    setTarget((LivingEntity) entity);
                }
                this.checkedForAttackTarget = true;
            }
            if (getSpawnFadeTime() < 30) {
                setSpawnFadeTime(getSpawnFadeTime() + 1);
            }
            if (getDeathFadeTime() > 0) {
                setDeathFadeTime(getDeathFadeTime() - 1);
            }
            if (getSpawnFadeTime() == 30 && getDeathFadeTime() == 0) {
                LivingEntity target = getTarget();
                if (target == null || !target.isAlive()) {
                    setDeathFadeTime(30);
                } else {
                    if (this.timeUntilDespawn == -1) {
                        this.timeUntilDespawn = 100;
                    }
                    BlockPos pos = BlockPos.containing(target.getX(), target.getBoundingBox().minY, target.getZ());
                    if (level.getFluidState(pos).is(FluidTags.WATER) || level.getFluidState(pos.below()).is(FluidTags.WATER)) {
                        this.timeUntilDespawn = 100;
                    } else if (this.timeUntilDespawn > 0) {
                        --this.timeUntilDespawn;
                    } else {
                        setDeathFadeTime(30);
                        setTarget(null);
                    }
                }
            }
            if (getDeathFadeTime() == 1) {
                discard();
            }
        }
    }

    /** handleWaterMovement: water does not hold it or push it. */
    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    /** setInWeb: webs do not hold it. */
    @Override
    public void makeStuckInBlock(BlockState state, Vec3 speedMultiplier) {
    }

    /** func_145780_a: it makes no sound as it goes. */
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int flesh = 1 + this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < flesh; ++l) {
            spawnAtLocation(level, Items.ROTTEN_FLESH);
        }
        dropChestContents(level, LOTRChestContents.MARSH_REMAINS, 1, 3 + looting);
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
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("SpawnFadeTime", getSpawnFadeTime());
        output.putInt("DeathFadeTime", getDeathFadeTime());
        if (this.attackTargetUUID != null) {
            output.putLong("TargetUUIDMost", this.attackTargetUUID.getMostSignificantBits());
            output.putLong("TargetUUIDLeast", this.attackTargetUUID.getLeastSignificantBits());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setSpawnFadeTime(input.getIntOr("SpawnFadeTime", 0));
        setDeathFadeTime(input.getIntOr("DeathFadeTime", 0));
        input.getLong("TargetUUIDMost").ifPresent(most -> input.getLong("TargetUUIDLeast")
                .ifPresent(least -> this.attackTargetUUID = new UUID(most, least)));
    }

    /** canReEquipHired: its player cannot dress it. */
    @Override
    public boolean canReEquipHired(int slot, ItemStack stack) {
        return false;
    }
}
