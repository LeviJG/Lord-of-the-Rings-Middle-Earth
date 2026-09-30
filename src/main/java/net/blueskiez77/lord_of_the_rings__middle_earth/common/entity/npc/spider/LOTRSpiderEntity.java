package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRUntamedPanicGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCRideableEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntitySpiderBase: the great spiders. Each has a size (0 to 3) and a
 * venom -- none, slowness or poison -- that it lays on what it bites, for
 * longer the harder the difficulty, and is itself immune to. Its size sets
 * its health (12 + 6 a size), speed, bite (2 + a size), and how large it is
 * ({@code 0.5 + size / 2}). It leaps at its prey, climbs walls, never takes a
 * fall, walks through cobwebs (though not a quagmire), and leaves string and
 * sometimes an eye. Any but the smallest can be ridden by a player the
 * faction holds at +50, and tamed by riding; a tame one mends on bones. A
 * ridden one climbs walls for five seconds before it must touch the ground
 * again. Poison venom can be drawn off into a glass bottle.
 *
 * <p>NOT ported yet: the climbing meter on the HUD (with the HUD); spawning in
 * darkness (D12); the hired-unit icon and health bar over it (with the other
 * mounts').
 */
public abstract class LOTRSpiderEntity extends LOTRNPCRideableEntity {

    public static final int VENOM_NONE = 0;
    public static final int VENOM_SLOWNESS = 1;
    public static final int VENOM_POISON = 2;

    private static final TagKey<Item> BONES_TAG = TagKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath("c", "bones"));

    private static final EntityDataAccessor<Byte> DATA_CLIMBING =
            SynchedEntityData.defineId(LOTRSpiderEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRSpiderEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_SCALE =
            SynchedEntityData.defineId(LOTRSpiderEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_CLIMB_TIME =
            SynchedEntityData.defineId(LOTRSpiderEntity.class, EntityDataSerializers.INT);

    protected LOTRSpiderEntity(EntityType<? extends LOTRSpiderEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        this.spawnsInDarkness = true;
        // applyEntityAttributes, which ran after entityInit had picked the size.
        int scale = getSpiderScale();
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(12.0 + scale * 6.0);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35 - scale * 0.03);
        getAttribute(LOTRNPCAttributes.NPC_ATTACK_DAMAGE).setBaseValue(2.0 + scale);
        setHealth(getMaxHealth());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CLIMBING, (byte) 0);
        builder.define(DATA_SCALE, (byte) getRandomSpiderScale());
        builder.define(DATA_TYPE, (byte) getRandomSpiderType());
        builder.define(DATA_CLIMB_TIME, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(3, new LOTRAttackOnCollideGoal(this, 1.2, false));
        this.goalSelector.addGoal(4, new LOTRUntamedPanicGoal(this, 1.2));
        this.goalSelector.addGoal(5, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    /** entityInit: the size and venom it is born with. */
    protected abstract int getRandomSpiderScale();

    protected abstract int getRandomSpiderType();

    public int getSpiderType() {
        return this.entityData.get(DATA_TYPE);
    }

    public void setSpiderType(int type) {
        this.entityData.set(DATA_TYPE, (byte) type);
    }

    public int getSpiderScale() {
        return this.entityData.get(DATA_SCALE);
    }

    public void setSpiderScale(int scale) {
        this.entityData.set(DATA_SCALE, (byte) scale);
    }

    public float getSpiderScaleAmount() {
        return 0.5f + getSpiderScale() / 2.0f;
    }

    /** getNPCScale: its size, which rescales its box as the original's rescaleNPC did. */
    @Override
    public float getNPCScale() {
        return getSpiderScaleAmount();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (DATA_SCALE.equals(accessor)) {
            refreshDimensions();
        }
    }

    public int getSpiderClimbTime() {
        return this.entityData.get(DATA_CLIMB_TIME);
    }

    public void setSpiderClimbTime(int time) {
        this.entityData.set(DATA_CLIMB_TIME, time);
    }

    public boolean isSpiderClimbing() {
        return (this.entityData.get(DATA_CLIMBING) & 1) != 0;
    }

    public void setSpiderClimbing(boolean flag) {
        byte b = this.entityData.get(DATA_CLIMBING);
        this.entityData.set(DATA_CLIMBING, flag ? (byte) (b | 1) : (byte) (b & ~1));
    }

    /** getClimbFractionRemaining, for the climbing meter (with the HUD). */
    public float getClimbFractionRemaining() {
        return 1.0f - Math.min(getSpiderClimbTime() / 100.0f, 1.0f);
    }

    public boolean shouldRenderClimbingMeter() {
        return !onGround() && getSpiderClimbTime() > 0;
    }

    public boolean canRideSpider() {
        return getSpiderScale() > 0;
    }

    /** isOnLadder. */
    @Override
    public boolean onClimbable() {
        return isSpiderClimbing();
    }

    /** setInWeb does nothing: cobwebs do not hold it. A quagmire still does (setInQuag). */
    @Override
    public void makeStuckInBlock(BlockState state, Vec3 speedMultiplier) {
        if (!(state.getBlock() instanceof WebBlock)) {
            super.makeStuckInBlock(state, speedMultiplier);
        }
    }

    /** attackEntityFrom: no fall damage. */
    @Override
    public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource source) {
        return false;
    }

    /** attackEntityAsMob: and its venom, for difficulty x (difficulty + 5) / 2 seconds. */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (!super.doHurtTarget(level, target)) {
            return false;
        }
        int difficulty = level.getDifficulty().getId();
        int duration = difficulty * (difficulty + 5) / 2;
        if (target instanceof LivingEntity living && duration > 0) {
            if (getSpiderType() == VENOM_SLOWNESS) {
                living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration * 20, 0), this);
            } else if (getSpiderType() == VENOM_POISON) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, duration * 20, 0), this);
            }
        }
        return true;
    }

    /** isPotionApplicable: not its own venom. */
    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        if (getSpiderType() == VENOM_SLOWNESS && effect.is(MobEffects.SLOWNESS)
                || getSpiderType() == VENOM_POISON && effect.is(MobEffects.POISON)) {
            return false;
        }
        return super.canBeAffected(effect);
    }

    /** getBaseMountedYOffset: its height less 0.7. */
    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return new Vec3(0.0, dimensions.height() - 0.7, 0.0);
    }

    @Override
    public boolean isMountSaddled() {
        return isNPCTamed() && getFirstPassenger() instanceof Player;
    }

    @Override
    public boolean getBelongsToNPC() {
        return false;
    }

    @Override
    public void setBelongsToNPC(boolean flag) {
    }

    @Override
    public boolean canBeLeashed() {
        return isNPCTamed();
    }

    @Override
    public boolean canDropRares() {
        return false;
    }

    /**
     * interact: poison drawn off into a glass bottle. Then, only by one the
     * faction holds at +50 (anyone else is told so), and only for a spider big
     * enough: a tame, hurt one mends on a bone; a free one may be ridden (or
     * leashed, or named, by what is in hand).
     */
    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (getSpiderType() == VENOM_POISON && stack.is(Items.GLASS_BOTTLE)) {
            player.setItemInHand(hand,
                    net.minecraft.world.item.ItemUtils.createFilledResult(stack, player, new ItemStack(LOTRMiscItems.BOTTLE_OF_POISON)));
            return InteractionResult.SUCCESS;
        }
        if (level().isClientSide() || this.hiredNPCInfo.isActive) {
            return InteractionResult.PASS;
        }
        if (canRideSpider() && getTarget() != player) {
            boolean hasRequiredAlignment = LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 50.0f;
            boolean notifyNotEnoughAlignment = false;
            if ((stack.is(Items.BONE) || stack.is(BONES_TAG)) && isNPCTamed() && getHealth() < getMaxHealth()) {
                if (hasRequiredAlignment) {
                    stack.consume(1, player);
                    heal(4.0f);
                    playSound(SoundEvents.SPIDER_AMBIENT, getSoundVolume(), getVoicePitch() * 1.5f);
                    return InteractionResult.SUCCESS;
                }
                notifyNotEnoughAlignment = true;
            }
            if (!notifyNotEnoughAlignment && !isVehicle()) {
                InteractionResult used = stack.interactLivingEntity(player, this, hand);
                if (used.consumesAction()) {
                    return used;
                }
                if (hasRequiredAlignment) {
                    player.startRiding(this);
                    setTarget(null);
                    getNavigation().stop();
                    return InteractionResult.SUCCESS;
                }
                notifyNotEnoughAlignment = true;
            }
            if (notifyNotEnoughAlignment) {
                LOTRAlignmentValues.notifyAlignmentNotHighEnough(player, 50.0f, getFaction());
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level() instanceof ServerLevel) {
            if (getFirstPassenger() instanceof Player && !onGround()) {
                if (this.horizontalCollision) {
                    setSpiderClimbTime(getSpiderClimbTime() + 1);
                }
            } else {
                setSpiderClimbTime(0);
            }
            if (getSpiderClimbTime() >= 100) {
                setSpiderClimbing(false);
                if (onGround()) {
                    setSpiderClimbTime(0);
                }
            } else {
                setSpiderClimbing(this.horizontalCollision);
            }
            if (getFirstPassenger() instanceof Player player
                    && LOTRPlayerAlignments.getAlignment(player, getFaction()) < 50.0f) {
                player.stopRiding();
            }
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.SPIDER_STEP, 0.15f, 1.0f);
    }

    /** getExperiencePoints: 2 + size + up to size + 1 more. */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        int scale = getSpiderScale();
        return 2 + scale + this.random.nextInt(scale + 2);
    }

    @Override
    protected void dropEquipment(ServerLevel level) {
    }

    /** dropFewItems: string, and -- slain by a player -- often an eye. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int string = this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int j = 0; j < string; ++j) {
            spawnAtLocation(level, Items.STRING);
        }
        if (killedByPlayer && (this.random.nextInt(3) == 0 || this.random.nextInt(1 + looting) > 0)) {
            spawnAtLocation(level, Items.SPIDER_EYE);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("SpiderType", (byte) getSpiderType());
        output.putByte("SpiderScale", (byte) getSpiderScale());
        output.putShort("SpiderRideTime", (short) getSpiderClimbTime());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setSpiderType(input.getByteOr("SpiderType", (byte) 0));
        setSpiderScale(input.getByteOr("SpiderScale", (byte) 0));
        getAttribute(LOTRNPCAttributes.NPC_ATTACK_DAMAGE).setBaseValue(2.0 + getSpiderScale());
        setSpiderClimbTime(input.getShortOr("SpiderRideTime", (short) 0));
    }
}
