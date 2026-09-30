package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBerryBushBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRScarecrows;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRValuableItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBird: a small bird -- a common songbird, a crow, a magpie or a
 * Far Harad bird -- that flits about, perches on solid blocks, and steals.
 * Every kind robs crop blocks and berry bushes (unless a scarecrow is near,
 * or mobGriefing is off), and snatches up to four items at a time -- from the
 * ground, or from the hand of a player who is not in creative -- into a
 * nine-slot crop; a songbird takes seeds, a crow food and bones, a magpie
 * valuables (LOTRValuableItems). The latest theft shows in its beak for ten
 * seconds. Everything it stole drops when it dies. It sings less at night.
 *
 * <p>Kept as the original had it: the stolen goods are not saved. Its
 * readEntityFromNBT wrote the crop and writeEntityToNBT read it, so a bird
 * reloaded from disk always came back with nothing (only the item in its beak,
 * saved as equipment, survived). Fixing that is left to the user.
 *
 * <p>NOT ported yet: the kind from the biome (Far Harad birds and crows there,
 * D10) -- elsewhere it is a crow one time in six, a magpie one in twelve and a
 * songbird otherwise, as in any other biome -- and LOTRAmbientSpawnChecks
 * (D12). The animal jar's updateInAnimalJar is with the jar's other hooks
 * (see the Deferred-port tracker).
 */
public class LOTRBirdEntity extends Mob {

    /** dataWatcher 16 and 17. */
    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRBirdEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_STILL =
            SynchedEntityData.defineId(LOTRBirdEntity.class, EntityDataSerializers.BOOLEAN);

    private @Nullable BlockPos currentFlightTarget;
    private int flightTargetTime;
    /** Client only: ticks left of a perched bird's wing-flutter. */
    public int flapTime;
    private final SimpleContainer birdInv = new SimpleContainer(9);
    private @Nullable ItemEntity stealTargetItem;
    private @Nullable Player stealTargetPlayer;
    private int stolenTime;
    private boolean stealingCrops;

    public LOTRBirdEntity(EntityType<? extends LOTRBirdEntity> type, Level level) {
        super(type, level);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.nextDouble(this.random, 0.08, 0.13));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 12.0f, 0.05f));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Mob.class, 12.0f, 0.1f));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TYPE, (byte) 0);
        builder.define(DATA_STILL, true);
    }

    /** onSpawnWithEgg, outside the Far Harad (see the class note). */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        if (this.random.nextInt(6) == 0) {
            setBirdType(BirdType.CROW);
        } else if (this.random.nextInt(10) == 0) {
            setBirdType(BirdType.MAGPIE);
        } else {
            setBirdType(BirdType.COMMON);
        }
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    public BirdType getBirdType() {
        int i = this.entityData.get(DATA_TYPE);
        return i < 0 || i >= BirdType.values().length ? BirdType.COMMON : BirdType.values()[i];
    }

    public void setBirdType(BirdType type) {
        this.entityData.set(DATA_TYPE, (byte) type.ordinal());
    }

    /** getBirdTextureDir: the kind's, or the species' own for the gorcrow, crebain and seagull. */
    public String getBirdTextureDir() {
        return getBirdType().textureDir;
    }

    public boolean isBirdStill() {
        return this.entityData.get(DATA_STILL);
    }

    public void setBirdStill(boolean still) {
        this.entityData.set(DATA_STILL, still);
    }

    public ItemStack getStolenItem() {
        return getItemBySlot(EquipmentSlot.HEAD);
    }

    public void setStolenItem(ItemStack stack) {
        setItemSlot(EquipmentSlot.HEAD, stack);
    }

    // --- Stealing -------------------------------------------------------------

    public boolean canStealItems() {
        return getBirdType().canSteal;
    }

    public boolean isStealable(ItemStack stack) {
        return switch (getBirdType()) {
            // IPlantable of EnumPlantType.Crop: seeds and the like.
            case COMMON -> stack.getItem() instanceof BlockItem block && block.getBlock() instanceof CropBlock;
            // ItemFood, or "bone" in the ore dictionary.
            case CROW -> stack.has(DataComponents.FOOD) || stack.is(Items.BONE);
            case MAGPIE -> LOTRValuableItems.canMagpieSteal(stack);
            case FAR_HARAD -> false;
        };
    }

    public boolean canStealCrops(BlockPos pos) {
        BlockState state = level().getBlockState(pos);
        if (state.getBlock() instanceof CropBlock) {
            return true;
        }
        return state.getBlock() instanceof LOTRBerryBushBlock && state.getValue(LOTRBerryBushBlock.HAS_BERRIES);
    }

    private void eatCropBlock(BlockPos pos) {
        BlockState state = level().getBlockState(pos);
        if (state.getBlock() instanceof LOTRBerryBushBlock) {
            level().setBlock(pos, state.setValue(LOTRBerryBushBlock.HAS_BERRIES, false), 3);
        } else {
            level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    private boolean canStealItem(ItemEntity item) {
        return item.isAlive() && isStealable(item.getItem());
    }

    private boolean canStealPlayer(Player player) {
        return !player.isCreative() && player.isAlive() && !getStealablePlayerSlots(player).isEmpty();
    }

    /** getStealablePlayerSlots: only the hotbar slot in hand, and only if it is stealable. */
    private List<Integer> getStealablePlayerSlots(Player player) {
        List<Integer> slots = new ArrayList<>();
        int selected = player.getInventory().getSelectedSlot();
        ItemStack stack = player.getInventory().getItem(selected);
        if (!stack.isEmpty() && isStealable(stack)) {
            slots.add(selected);
        }
        return slots;
    }

    private static BlockPos itemFlightTarget(ItemEntity item) {
        return BlockPos.containing(item.getX(), item.getBoundingBox().minY, item.getZ());
    }

    private static BlockPos playerFlightTarget(Player player) {
        return BlockPos.containing(player.getX(), player.getBoundingBox().minY + 1.0, player.getZ());
    }

    private boolean isValidFlightTarget(BlockPos pos) {
        return pos.getY() >= 1 && !blocksMovement(pos);
    }

    /** Material.blocksMovement, as the collision shape (blocksMotion is deprecated). */
    private boolean blocksMovement(BlockPos pos) {
        return !level().getBlockState(pos).getCollisionShape(level(), pos).isEmpty();
    }

    private double distanceSqToFlightTarget() {
        BlockPos t = this.currentFlightTarget;
        return distanceToSqr(t.getX() + 0.5, t.getY() + 0.5, t.getZ() + 0.5);
    }

    private void cancelFlight() {
        this.currentFlightTarget = null;
        this.flightTargetTime = 0;
        this.stealTargetItem = null;
        this.stealTargetPlayer = null;
        this.stealingCrops = false;
    }

    /** canBirdSit: in open air, above a block with a solid top. */
    private boolean canBirdSit() {
        BlockPos pos = blockPosition();
        BlockPos below = pos.below();
        return !blocksMovement(pos) && level().getBlockState(below).isFaceSturdy(level(), below, Direction.UP);
    }

    private boolean isInventoryFull() {
        for (int i = 0; i < this.birdInv.getContainerSize(); ++i) {
            if (this.birdInv.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Steal up to four of {@code stack}; returns what was taken, having taken
     * it out of the stack, or empty if the crop had no room.
     */
    private ItemStack stealFrom(ItemStack stack) {
        int count = Mth.nextInt(this.random, 1, Math.min(stack.getCount(), 4));
        ItemStack stolen = stack.copyWithCount(count);
        ItemStack left = this.birdInv.addItem(stolen.copy());
        int taken = count - left.getCount();
        if (taken <= 0) {
            return ItemStack.EMPTY;
        }
        stack.shrink(taken);
        return stolen.copyWithCount(count);
    }

    // --- Ticking --------------------------------------------------------------

    @Override
    public void tick() {
        super.tick();
        if (isBirdStill()) {
            setDeltaMovement(Vec3.ZERO);
            setPosRaw(getX(), Mth.floor(getY()), getZ());
            if (level().isClientSide()) {
                if (this.random.nextInt(200) == 0) {
                    this.flapTime = 40;
                }
                if (this.flapTime > 0) {
                    --this.flapTime;
                }
            }
        } else {
            setDeltaMovement(getDeltaMovement().multiply(1.0, 0.6, 1.0));
            if (level().isClientSide()) {
                this.flapTime = 0;
            }
        }
    }

    /** updateAITasks. */
    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        if (!getStolenItem().isEmpty() && ++this.stolenTime >= 200) {
            setStolenItem(ItemStack.EMPTY);
            this.stolenTime = 0;
        }
        if (isBirdStill()) {
            if (!canBirdSit() || this.random.nextInt(400) == 0 || level.getNearestPlayer(this, 6.0) != null) {
                setBirdStill(false);
            }
            return;
        }
        boolean canGrief = level.getGameRules().get(GameRules.MOB_GRIEFING);
        if (canStealItems() && !this.stealingCrops && this.stealTargetItem == null && this.stealTargetPlayer == null
                && !isInventoryFull() && this.random.nextInt(100) == 0) {
            List<Player> players = level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(16.0),
                    p -> canStealPlayer(p) && isValidFlightTarget(playerFlightTarget(p)));
            if (players.isEmpty()) {
                List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(16.0),
                        e -> canStealItem(e) && isValidFlightTarget(itemFlightTarget(e)));
                if (!items.isEmpty()) {
                    this.stealTargetItem = items.get(this.random.nextInt(items.size()));
                    this.currentFlightTarget = itemFlightTarget(this.stealTargetItem);
                    this.flightTargetTime = 0;
                }
            } else {
                this.stealTargetPlayer = players.get(this.random.nextInt(players.size()));
                this.currentFlightTarget = playerFlightTarget(this.stealTargetPlayer);
                this.flightTargetTime = 0;
            }
        }
        if (this.stealTargetItem != null || this.stealTargetPlayer != null) {
            if (isInventoryFull() || this.currentFlightTarget == null || !isValidFlightTarget(this.currentFlightTarget)) {
                cancelFlight();
            } else if (this.stealTargetItem != null && !canStealItem(this.stealTargetItem)
                    || this.stealTargetPlayer != null && !canStealPlayer(this.stealTargetPlayer)) {
                cancelFlight();
            } else {
                this.currentFlightTarget = this.stealTargetItem != null ? itemFlightTarget(this.stealTargetItem)
                        : playerFlightTarget(this.stealTargetPlayer);
                if (distanceSqToFlightTarget() < 1.0) {
                    ItemStack stolen = ItemStack.EMPTY;
                    if (this.stealTargetItem != null) {
                        ItemStack stack = this.stealTargetItem.getItem();
                        stolen = stealFrom(stack);
                        if (stack.isEmpty()) {
                            this.stealTargetItem.discard();
                        }
                    } else {
                        List<Integer> slots = getStealablePlayerSlots(this.stealTargetPlayer);
                        int slot = slots.get(this.random.nextInt(slots.size()));
                        stolen = stealFrom(this.stealTargetPlayer.getInventory().getItem(slot));
                    }
                    if (!stolen.isEmpty()) {
                        this.stolenTime = 0;
                        setStolenItem(stolen);
                        playSound(SoundEvents.ITEM_PICKUP, 0.5f,
                                ((this.random.nextFloat() - this.random.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    }
                    cancelFlight();
                }
            }
        } else if (this.stealingCrops) {
            if (!canGrief) {
                this.stealingCrops = false;
            } else if (this.currentFlightTarget == null || !isValidFlightTarget(this.currentFlightTarget)) {
                cancelFlight();
            } else {
                BlockPos crop = this.currentFlightTarget;
                if (distanceSqToFlightTarget() < 1.0) {
                    if (canStealCrops(crop)) {
                        eatCropBlock(crop);
                        playSound(SoundEvents.GENERIC_EAT.value(), 1.0f,
                                (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                    }
                    cancelFlight();
                } else if (!canStealCrops(crop)
                        || this.flightTargetTime % 100 == 0 && LOTRScarecrows.anyScarecrowsNearby(level, crop)) {
                    cancelFlight();
                }
            }
        } else {
            if (canGrief && this.random.nextInt(100) == 0) {
                BlockPos here = blockPosition();
                for (int l = 0; l < 32; ++l) {
                    BlockPos crop = here.offset(Mth.nextInt(this.random, -16, 16), Mth.nextInt(this.random, -8, 8),
                            Mth.nextInt(this.random, -16, 16));
                    if (canStealCrops(crop) && !LOTRScarecrows.anyScarecrowsNearby(level, crop)) {
                        this.stealingCrops = true;
                        this.currentFlightTarget = crop;
                        this.flightTargetTime = 0;
                        break;
                    }
                }
            }
            if (!this.stealingCrops) {
                if (this.currentFlightTarget != null && !isValidFlightTarget(this.currentFlightTarget)) {
                    cancelFlight();
                }
                if (this.currentFlightTarget == null || this.random.nextInt(50) == 0 || distanceSqToFlightTarget() < 4.0) {
                    BlockPos here = blockPosition();
                    this.currentFlightTarget = here.offset(this.random.nextInt(16) - this.random.nextInt(16),
                            Mth.nextInt(this.random, -2, 3), this.random.nextInt(16) - this.random.nextInt(16));
                    this.flightTargetTime = 0;
                }
            }
        }
        if (this.currentFlightTarget != null) {
            double speed = getAttributeValue(Attributes.MOVEMENT_SPEED);
            double dx = this.currentFlightTarget.getX() + 0.5 - getX();
            double dy = this.currentFlightTarget.getY() + 0.5 - getY();
            double dz = this.currentFlightTarget.getZ() + 0.5 - getZ();
            Vec3 m = getDeltaMovement();
            Vec3 next = m.add((Math.signum(dx) * 0.5 - m.x) * speed, (Math.signum(dy) * 0.8 - m.y) * speed,
                    (Math.signum(dz) * 0.5 - m.z) * speed);
            setDeltaMovement(next);
            float yaw = (float) (Mth.atan2(next.z, next.x) * 180.0 / Math.PI) - 90.0f;
            this.zza = 0.5f;
            setYRot(getYRot() + Mth.wrapDegrees(yaw - getYRot()));
            if (++this.flightTargetTime >= 400) {
                cancelFlight();
            }
        }
        if (this.random.nextInt(200) == 0 && canBirdSit()) {
            setBirdStill(true);
            cancelFlight();
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt && isBirdStill()) {
            setBirdStill(false);
        }
        return hurt;
    }

    /** onDeath: the beakful goes, and the whole crop drops. */
    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (level() instanceof ServerLevel) {
            setStolenItem(ItemStack.EMPTY);
            Containers.dropContents(level(), this, this.birdInv);
        }
    }

    /** dropEquipment: nothing -- the item in its beak is only a show of what is in its crop. */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
    }

    /** playLivingSound: at night, only one call in twenty. */
    @Override
    public void playAmbientSound() {
        if (level().isBrightOutside() || this.random.nextInt(20) == 0) {
            super.playAmbientSound();
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return getBirdType() == BirdType.CROW ? LOTRSounds.BIRD_CROW_SAY : LOTRSounds.BIRD_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return getBirdType() == BirdType.CROW ? LOTRSounds.BIRD_CROW_HURT : LOTRSounds.BIRD_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return getHurtSound(damageSources().generic());
    }

    @Override
    protected float getSoundVolume() {
        return 1.0f;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 60;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    /** canDespawn: EntityLiving's, as an ambient creature. */
    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return true;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("BirdType", getBirdType().ordinal());
        output.putBoolean("BirdStill", isBirdStill());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(DATA_TYPE, (byte) input.getIntOr("BirdType", 0));
        setBirdStill(input.getBooleanOr("BirdStill", false));
        this.stolenTime = 0;
    }

    public enum BirdType {
        COMMON("common", true), CROW("crow", true), MAGPIE("magpie", true), FAR_HARAD("farHarad", true);

        public final String textureDir;
        public final boolean canSteal;

        BirdType(String dir, boolean canSteal) {
            this.textureDir = dir;
            this.canSteal = canSteal;
        }
    }
}
