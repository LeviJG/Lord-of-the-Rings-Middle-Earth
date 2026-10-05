package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRGollumMenu;
import java.util.List;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRGollumAvoidEntityGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRGollumFishingGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRGollumFollowOwnerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRGollumPanicGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRGollumRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRCharacter;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRInventoryNPC;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGollum: Gollum -- 30 strong, small (0.6 by 1.2), keeping out of
 * water unless fishing or following. He runs from orcs and elves within 8
 * blocks and from whatever hurts him, arms over his head; heals now and then;
 * and, while no one owns him, gollums at players within 80 blocks -- the
 * nearer, the likelier.
 *
 * <p>Fed twenty raw fish (in handfuls or all at once), he is tamed: everyone
 * is told, he gives hearts, and he will ask half as many again and more of
 * the next who would tame him. His owner cannot hurt him (he complains); may
 * feed him fish or meat to heal him; tells him to stay or follow; and he
 * follows them, fishes for them, and drops what he catches at their feet.
 * His death, if owned, is told to everyone; he drops his pack and may be
 * found again.
 *
 * <p>NOT ported yet: his natural spawning near the High Pass
 * (LOTRGollumSpawner, with the Middle-earth dimension and waypoints; the
 * gollumSpawned flag is kept and saved meanwhile) and the tameGollum
 * achievement (D7).
 */
public class LOTRGollumEntity extends LOTRNPCEntity implements LOTRCharacter {

    public static final int INV_ROWS = 3;
    /** handleHealthUpdate 15: his splashing as he fishes. */
    public static final byte EVENT_SPLASH = 15;

    private static final EntityDataAccessor<String> DATA_OWNER =
            SynchedEntityData.defineId(LOTRGollumEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_FLEEING =
            SynchedEntityData.defineId(LOTRGollumEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SITTING =
            SynchedEntityData.defineId(LOTRGollumEntity.class, EntityDataSerializers.BOOLEAN);

    public int eatingTick;
    public int prevFishTime = 400;
    public boolean isFishing;
    public final LOTRInventoryNPC inventory = new LOTRInventoryNPC("gollum", this, INV_ROWS * 9);
    public int prevFishRequired = 20;
    public int fishRequired = 20;

    public LOTRGollumEntity(EntityType<? extends LOTRGollumEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OWNER, "");
        builder.define(DATA_FLEEING, false);
        builder.define(DATA_SITTING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRGollumRemainStillGoal(this));
        this.goalSelector.addGoal(2, new LOTRGollumPanicGoal(this, 1.4));
        this.goalSelector.addGoal(3, new LOTRGollumAvoidEntityGoal<>(this, LOTROrcEntity.class, 8.0f, 1.2, 1.4));
        this.goalSelector.addGoal(3, new LOTRGollumAvoidEntityGoal<>(this, LOTRElfEntity.class, 8.0f, 1.2, 1.4));
        this.goalSelector.addGoal(4, new LOTRGollumFishingGoal(this, 1.5));
        this.goalSelector.addGoal(5, new LOTRGollumFollowOwnerGoal(this, 1.2, 6.0f, 4.0f));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.1f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public String getGollumOwnerUUID() {
        return this.entityData.get(DATA_OWNER);
    }

    public void setGollumOwnerUUID(String s) {
        this.entityData.set(DATA_OWNER, s);
    }

    public @Nullable Player getGollumOwner() {
        try {
            return level().getPlayerByUUID(UUID.fromString(getGollumOwnerUUID()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isGollumFleeing() {
        return this.entityData.get(DATA_FLEEING);
    }

    public void setGollumFleeing(boolean flag) {
        this.entityData.set(DATA_FLEEING, flag);
    }

    public boolean isGollumSitting() {
        return this.entityData.get(DATA_SITTING);
    }

    public void setGollumSitting(boolean flag) {
        this.entityData.set(DATA_SITTING, flag);
    }

    /** Raw fish of any kind: what taming him takes (1.7.10's one fish item). */
    private static boolean isRawFish(ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.SALMON) || stack.is(Items.TROPICAL_FISH) || stack.is(Items.PUFFERFISH);
    }

    /** canGollumEat: fish, raw or cooked, or the meat a wolf likes. */
    public boolean canGollumEat(ItemStack stack) {
        return stack.is(ItemTags.FISHES) || stack.is(ItemTags.MEAT);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        Player owner = getGollumOwner();
        if (owner != null && source.getEntity() == owner) {
            damage = 0.0f;
            LOTRSpeech.sendSpeech(owner, this, LOTRSpeech.getRandomSpeechForPlayer(this, "char/gollum/hurt", owner, null, null));
        }
        if (super.hurtServer(level, source, damage)) {
            setGollumSitting(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean canDropRares() {
        return false;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return LOTRSounds.GOLLUM_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.GOLLUM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.GOLLUM_DEATH;
    }

    public SoundEvent getSwimSplashSoundPublic() {
        return getSwimSplashSound();
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isGollumFleeing() ? super.getSpeechBank(player) : "char/gollum/say";
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_SPLASH) {
            for (int i = 0; i < 4; ++i) {
                level().addParticle(this.random.nextBoolean() ? ParticleTypes.BUBBLE : ParticleTypes.SPLASH,
                        getX() + this.random.nextFloat() * getBbWidth() * 2.0f - getBbWidth(),
                        getY() + 0.5 + this.random.nextFloat() * getBbHeight(),
                        getZ() + this.random.nextFloat() * getBbWidth() * 2.0f - getBbWidth(),
                        this.random.nextGaussian() * 0.02, this.random.nextGaussian() * 0.02,
                        this.random.nextGaussian() * 0.02);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    /** interact: his owner feeds him, opens his pack or tells him to stay or follow; anyone else may tame him with fish. */
    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) {
            return super.mobInteract(player, hand);
        }
        ItemStack stack = player.getItemInHand(hand);
        Player owner = getGollumOwner();
        if (owner != null && player == owner) {
            FoodProperties food = stack.get(DataComponents.FOOD);
            if (food != null && canGollumEat(stack) && getHealth() < getMaxHealth()) {
                stack.consume(1, player);
                heal(food.nutrition());
                this.eatingTick = 20;
                return InteractionResult.SUCCESS;
            }
            if (player.isShiftKeyDown()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.openMenu(new ExtendedMenuProvider<Integer>() {
                        @Override
                        public Integer getScreenOpeningData(ServerPlayer p) {
                            return getId();
                        }

                        @Override
                        public Component getDisplayName() {
                            return LOTRGollumEntity.this.getName();
                        }

                        @Override
                        public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player p) {
                            return new LOTRGollumMenu(containerId, inv, LOTRGollumEntity.this);
                        }
                    });
                }
                return InteractionResult.SUCCESS;
            }
            setGollumSitting(!isGollumSitting());
            LOTRSpeech.sendSpeech(owner, this, LOTRSpeech.getRandomSpeechForPlayer(this,
                    isGollumSitting() ? "char/gollum/stay" : "char/gollum/follow", owner, null, null));
            return InteractionResult.SUCCESS;
        }
        if (isRawFish(stack)) {
            if (stack.getCount() >= this.fishRequired) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(this.fishRequired);
                }
                this.fishRequired = 0;
            } else {
                this.fishRequired -= stack.getCount();
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, ItemStack.EMPTY);
                }
            }
            this.eatingTick = 20;
            if (this.fishRequired <= 0) {
                setGollumOwnerUUID(player.getUUID().toString());
                LOTRSpeech.sendSpeech(player, this, LOTRSpeech.getRandomSpeechForPlayer(this, "char/gollum/tame", player, null, null));
                if (level() instanceof ServerLevel level) {
                    level.getServer().getPlayerList().broadcastSystemMessage(
                            Component.translatable("chat.lotr.tameGollum", player.getName(), getName()), false);
                }
                spawnHearts();
                this.prevFishRequired = Math.round(this.prevFishRequired * (1.5f + this.random.nextFloat() * 0.25f));
                this.fishRequired = this.prevFishRequired;
            } else {
                LOTRSpeech.sendSpeech(player, this, LOTRSpeech.getRandomSpeechForPlayer(this, "char/gollum/tameProgress", player, null, null));
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void die(DamageSource source) {
        if (level() instanceof ServerLevel level && !getGollumOwnerUUID().isEmpty()) {
            level.getServer().getPlayerList().broadcastSystemMessage(getCombatTracker().getDeathMessage(), false);
        }
        super.die(source);
        if (!level().isClientSide()) {
            this.inventory.dropAllItems();
            LOTRLevelData.setGollumSpawned(false);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide() && this.random.nextInt(500) == 0) {
            heal(1.0f);
        }
        if (this.eatingTick > 0) {
            if (this.eatingTick % 4 == 0) {
                playSound(SoundEvents.GENERIC_EAT.value(), 0.5f + 0.5f * this.random.nextInt(2),
                        (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }
            --this.eatingTick;
        }
        if (this.prevFishTime > 0) {
            --this.prevFishTime;
        }
        if (isGollumSitting() && !level().isClientSide() && onGround()) {
            getJumpControl().jump();
        }
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        Player owner = getGollumOwner();
        if (!getMainHandItem().isEmpty() && owner != null && distanceToSqr(owner) < 4.0) {
            // He gives what he holds to his owner, tossed from his mouth.
            getLookControl().setLookAt(owner, 100.0f, 100.0f);
            getLookControl().tick();
            ItemEntity item = new ItemEntity(level, getX(), getEyeY(), getZ(), getMainHandItem());
            item.setPickUpDelay(40);
            float f = 0.3f;
            float yaw = this.yHeadRot * Mth.DEG_TO_RAD;
            float pitch = getXRot() * Mth.DEG_TO_RAD;
            double mx = -Mth.sin(yaw) * Mth.cos(pitch) * f;
            double mz = Mth.cos(yaw) * Mth.cos(pitch) * f;
            double my = -Mth.sin(pitch) * f + 0.1f;
            f = 0.02f * this.random.nextFloat();
            float angle = this.random.nextFloat() * Mth.TWO_PI;
            item.setDeltaMovement(mx + Math.cos(angle) * f, my + (this.random.nextFloat() - this.random.nextFloat()) * 0.1f,
                    mz + Math.sin(angle) * f);
            level.addFreshEntity(item);
            setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        if (getGollumOwnerUUID().isEmpty() && this.random.nextInt(40) == 0) {
            List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(80.0));
            for (Player player : nearbyPlayers) {
                int chance = (int) (distanceTo(player) / 8.0);
                if (this.random.nextInt(Math.max(2, chance)) != 0) {
                    continue;
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), LOTRSounds.GOLLUM_SAY,
                        getSoundSource(), getSoundVolume(), getVoicePitch());
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.inventory.save(output);
        output.putString("GollumOwnerUUID", getGollumOwnerUUID());
        output.putBoolean("GollumSitting", isGollumSitting());
        output.putInt("GollumFishTime", this.prevFishTime);
        output.putInt("FishReq", this.fishRequired);
        output.putInt("FishReqPrev", this.prevFishRequired);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.inventory.load(input);
        input.getString("GollumOwnerUUID").ifPresent(this::setGollumOwnerUUID);
        setGollumSitting(input.getBooleanOr("GollumSitting", false));
        this.prevFishTime = input.getIntOr("GollumFishTime", 0);
        input.getInt("FishReq").ifPresent(i -> {
            this.fishRequired = i;
            this.prevFishRequired = input.getIntOr("FishReqPrev", 20);
        });
    }

    /** canReEquipHired: its player cannot dress it. */
    @Override
    public boolean canReEquipHired(int slot, ItemStack stack) {
        return false;
    }
}
