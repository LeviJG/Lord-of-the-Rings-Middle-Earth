package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRUntamedPanicGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRDeerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCRideableEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityWarg: the great wolves the orcs ride. A warg is an NPC of its
 * faction -- it hunts that faction's enemies (and, now and then, rabbits and
 * deer), can be hired, and gives alignment when killed -- and a mount: a
 * player the faction holds at +50 or better may ride one, tame it by
 * riding it (LOTRUntamedPanicGoal), and, once it wears a saddle, steer it.
 * A tame one eats meat to mend. Its coat is brown, now and then grey, rarely
 * black and very rarely white; its health, bite and armour vary.
 *
 * <p>Its saddle and barding are its SADDLE and BODY equipment (the original's
 * two-slot AnimalChest, "WargSaddleItem" and "WargArmorItem").
 *
 * <p>NOT ported yet: the saddle-and-armour screen that is the only way to
 * put them on (openGUI, D16 -- until then a tame warg is never saddled by a
 * player); the rideWarg achievement (D7).
 */
public abstract class LOTRWargEntity extends LOTRNPCRideableEntity {

    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRWargEntity.class, EntityDataSerializers.BYTE);

    private int eatingTick;

    protected LOTRWargEntity(EntityType<? extends LOTRWargEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        this.isImmuneToFrost = true;
        this.spawnsInDarkness = true;
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(Mth.nextInt(this.random, 20, 32));
        getAttribute(LOTRNPCAttributes.NPC_ATTACK_DAMAGE).setBaseValue(Mth.nextInt(this.random, 3, 5));
        setHealth(getMaxHealth());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.MOVEMENT_SPEED, 0.22);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TYPE, (byte) randomType().wargID);
    }

    /** entityInit: brown, one in three grey, one in twenty black, one in 500 white. */
    protected LOTRWargType randomType() {
        if (this.random.nextInt(500) == 0) {
            return LOTRWargType.WHITE;
        }
        if (this.random.nextInt(20) == 0) {
            return LOTRWargType.BLACK;
        }
        return this.random.nextInt(3) == 0 ? LOTRWargType.GREY : LOTRWargType.BROWN;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(2, getWargAttackAI());
        this.goalSelector.addGoal(3, new LOTRUntamedPanicGoal(this, 1.2));
        this.goalSelector.addGoal(4, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0f, 0.02f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Mob.class, 12.0f, 0.02f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        int target = addTargetTasks(true);
        if (!(this instanceof LOTRWargBombardierEntity)) {
            this.targetSelector.addGoal(target + 1, new LOTRNearestAttackableTargetGoal(this, Rabbit.class, 500, false, null));
            this.targetSelector.addGoal(target + 1, new LOTRNearestAttackableTargetGoal(this, LOTRDeerEntity.class, 1000, false, null));
        }
    }

    protected Goal getWargAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.6, false);
    }

    public LOTRWargType getWargType() {
        return LOTRWargType.forID(this.entityData.get(DATA_TYPE));
    }

    public void setWargType(LOTRWargType type) {
        this.entityData.set(DATA_TYPE, (byte) type.wargID);
    }

    public boolean canWargBeRidden() {
        return true;
    }

    // --- Saddle and armour ------------------------------------------------------

    @Override
    public boolean isMountSaddled() {
        return !getItemBySlot(EquipmentSlot.SADDLE).isEmpty();
    }

    @Override
    public boolean getBelongsToNPC() {
        return false;
    }

    @Override
    public void setBelongsToNPC(boolean flag) {
    }

    public ItemStack getWargArmor() {
        return getItemBySlot(EquipmentSlot.BODY);
    }

    public void setWargArmor(ItemStack stack) {
        setItemSlot(EquipmentSlot.BODY, stack);
    }

    /** onInventoryChanged: the horse's leather and armour sounds as they go on. */
    @Override
    public void onEquipItem(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack) {
        super.onEquipItem(slot, oldStack, newStack);
        if (this.tickCount > 20 && !level().isClientSide()) {
            if (slot == EquipmentSlot.SADDLE && oldStack.isEmpty() && !newStack.isEmpty()) {
                playSound(SoundEvents.HORSE_SADDLE.value(), 0.5f, 1.0f);
            } else if (slot == EquipmentSlot.BODY && !ItemStack.matches(oldStack, newStack)) {
                playSound(SoundEvents.HORSE_ARMOR.value(), 0.5f, 1.0f);
            }
        }
    }

    /** Its gear is its own to drop, not an NPC's equipment to lose. */
    @Override
    protected void dropEquipment(ServerLevel level) {
    }

    @Override
    public boolean canDropRares() {
        return false;
    }

    public float getTailRotation() {
        return (getMaxHealth() - getHealth()) / getMaxHealth() * -1.2f;
    }

    // --- Being handled ------------------------------------------------------------

    /**
     * interact: only by one the faction holds at +50 -- anyone else is told
     * so. A tame warg eats meat when hurt; a free one may be ridden (or
     * leashed, or named, by what is in hand).
     */
    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide() || this.hiredNPCInfo.isActive) {
            return InteractionResult.PASS;
        }
        if (getTarget() == player) {
            return super.mobInteract(player, hand);
        }
        boolean hasRequiredAlignment = LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 50.0f;
        boolean notifyNotEnoughAlignment = false;
        ItemStack stack = player.getItemInHand(hand);
        if (isNPCTamed() && player.isShiftKeyDown()) {
            if (hasRequiredAlignment) {
                // openGUI: the saddle-and-armour screen (D16).
                return InteractionResult.SUCCESS;
            }
            notifyNotEnoughAlignment = true;
        }
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (!notifyNotEnoughAlignment && isNPCTamed() && food != null && stack.is(ItemTags.WOLF_FOOD)
                && getHealth() < getMaxHealth()) {
            if (hasRequiredAlignment) {
                stack.consume(1, player);
                heal(food.nutrition());
                this.eatingTick = 20;
                return InteractionResult.SUCCESS;
            }
            notifyNotEnoughAlignment = true;
        }
        if (!notifyNotEnoughAlignment && isNPCTamed() && !isMountSaddled() && canWargBeRidden() && !isVehicle()
                && stack.is(Items.SADDLE)) {
            if (hasRequiredAlignment) {
                // openGUI (D16).
                return InteractionResult.SUCCESS;
            }
            notifyNotEnoughAlignment = true;
        }
        if (!notifyNotEnoughAlignment && !isBaby() && canWargBeRidden() && !isVehicle()) {
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
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean canBeLeashed() {
        return isNPCTamed();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level() instanceof ServerLevel && getFirstPassenger() instanceof Player player
                && LOTRPlayerAlignments.getAlignment(player, getFaction()) < 50.0f) {
            player.stopRiding();
        }
        if (this.eatingTick > 0) {
            if (this.eatingTick % 4 == 0) {
                playSound(SoundEvents.GENERIC_EAT.value(), 0.5f + 0.5f * this.random.nextInt(2),
                        0.4f + this.random.nextFloat() * 0.2f);
            }
            --this.eatingTick;
        }
    }

    // --- Spawning and dying -------------------------------------------------------

    /** dropFewItems: fur, bones, and -- slain by a player -- sometimes its skin. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int furs = 1 + this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < furs; ++l) {
            spawnAtLocation(level, LOTRMaterialItems.FUR);
        }
        int bones = 2 + this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, LOTRMaterialItems.WARG_BONE);
        }
        if (killedByPlayer && this.random.nextInt(Math.max(50 - looting * 8, 1)) == 0) {
            spawnAtLocation(level, new ItemStack(getWargType().rug()), 0.0f);
        }
    }

    /** onDeath: a tame warg's saddle and barding fall off. */
    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (level() instanceof ServerLevel level && isNPCTamed()) {
            if (isMountSaddled()) {
                setItemSlot(EquipmentSlot.SADDLE, ItemStack.EMPTY);
                spawnAtLocation(level, Items.SADDLE);
            }
            ItemStack armor = getWargArmor();
            if (!armor.isEmpty()) {
                spawnAtLocation(level, armor, 0.0f);
                setWargArmor(ItemStack.EMPTY);
            }
        }
    }

    // --- Sounds -------------------------------------------------------------------

    @Override
    public @Nullable SoundEvent getAttackSound() {
        return LOTRSounds.WARG_ATTACK;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return LOTRSounds.WARG_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.WARG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.WARG_DEATH;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("WargType", (byte) getWargType().wargID);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setWargType(LOTRWargType.forID(input.getByteOr("WargType", (byte) 0)));
    }

    /**
     * createWargRider: the orc a warg of this people spawns carrying, now and
     * then barding the warg as it does; null for none.
     */
    protected @Nullable LOTRNPCEntity createWargRider(ServerLevel level) {
        return null;
    }

    /** onSpawnWithEgg: one time in three, a rider of its people climbs on. */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(accessor, difficulty, reason, groupData);
        if (canWargBeRidden() && this.random.nextInt(3) == 0) {
            ServerLevel level = accessor.getLevel();
            LOTRNPCEntity rider = createWargRider(level);
            if (rider != null) {
                rider.snapTo(getX(), getY(), getZ(), getYRot(), 0.0f);
                rider.finalizeSpawn(accessor, difficulty, EntitySpawnReason.JOCKEY, null);
                rider.isNPCPersistent = this.isNPCPersistent;
                level.addFreshEntity(rider);
                rider.startRiding(this, true, false);
            }
        }
        return data;
    }
}
