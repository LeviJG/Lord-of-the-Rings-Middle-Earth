package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith.LOTRHaradPyramidWraithEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityScorpion: the jungle and desert scorpions. Each is born small,
 * middling or large (the scale, 0-2), which sets its size (x0.5, x1, x1.5),
 * health, speed, bite and experience. Its sting poisons -- longer on harder
 * difficulties -- and flicks the tail for a second (the strike time, for the
 * model); it is immune to poison itself. A glass bottle used on one milks it
 * for a bottle of poison. Arthropod, for Bane of Arthropods
 * ({@code minecraft:arthropod}); spider sounds.
 *
 * <p>Drops are {@code lotr:entities/*_scorpion}: one to three rotten flesh
 * plus looting.
 *
 * <p>It spares the Harad pyramid wraiths (noWraiths).
 *
 * <p>NOT ported yet: LOTRMobSpawnerCondition's spawner flag (D12).
 */
public abstract class LOTRScorpionEntity extends Monster {

    /** dataWatcher 18. */
    private static final EntityDataAccessor<Byte> DATA_SCALE =
            SynchedEntityData.defineId(LOTRScorpionEntity.class, EntityDataSerializers.BYTE);
    /** dataWatcher 19. */
    private static final EntityDataAccessor<Integer> DATA_STRIKE =
            SynchedEntityData.defineId(LOTRScorpionEntity.class, EntityDataSerializers.INT);

    protected LOTRScorpionEntity(EntityType<? extends LOTRScorpionEntity> type, Level level) {
        super(type, level);
        setScorpionScale(getRandomScorpionScale());
        applyScaleAttributes();
        setHealth(getMaxHealth());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes();
    }

    /** applyEntityAttributes, which read the scale the entity had just been given. */
    private void applyScaleAttributes() {
        int scale = getScorpionScale();
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(12.0 + scale * 6.0);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35 - scale * 0.05);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0 + scale);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 1.2, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.05f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LOTRNPCEntity.class, 0, true, false,
                (target, level) -> !(target instanceof LOTRHaradPyramidWraithEntity)));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SCALE, (byte) 0);
        builder.define(DATA_STRIKE, 0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    public int getRandomScorpionScale() {
        return this.random.nextInt(3);
    }

    public int getScorpionScale() {
        return this.entityData.get(DATA_SCALE);
    }

    public void setScorpionScale(int scale) {
        this.entityData.set(DATA_SCALE, (byte) scale);
    }

    public float getScorpionScaleAmount() {
        return 0.5f + getScorpionScale() / 2.0f;
    }

    public int getStrikeTime() {
        return this.entityData.get(DATA_STRIKE);
    }

    public void setStrikeTime(int time) {
        this.entityData.set(DATA_STRIKE, time);
    }

    /** rescaleScorpion: the hitbox follows the scale. */
    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return super.getDefaultDimensions(pose).scale(getScorpionScaleAmount());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (DATA_SCALE.equals(accessor)) {
            refreshDimensions();
        }
    }

    /** attackEntityAsMob: the sting poisons for difficulty x (difficulty + 5) / 2 seconds. */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            setStrikeTime(20);
            int difficulty = level.getDifficulty().getId();
            int duration = difficulty * (difficulty + 5) / 2;
            if (target instanceof LivingEntity living && duration > 0) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, duration * 20, 0), this);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        if (effect.is(MobEffects.POISON)) {
            return false;
        }
        return super.canBeAffected(effect);
    }

    /** interact: a glass bottle comes away as a bottle of poison. */
    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.GLASS_BOTTLE)) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player,
                    new ItemStack(LOTRMiscItems.BOTTLE_OF_POISON), false));
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        int strike;
        if (!level().isClientSide() && (strike = getStrikeTime()) > 0) {
            setStrikeTime(strike - 1);
        }
    }

    /** getExperiencePoints. */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        int scale = getScorpionScale();
        return 2 + scale + this.random.nextInt(scale + 2);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("ScorpionScale", (byte) getScorpionScale());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setScorpionScale(input.getByteOr("ScorpionScale", (byte) 0));
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0 + getScorpionScale());
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.SPIDER_STEP, 0.15f, 1.0f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
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
}
