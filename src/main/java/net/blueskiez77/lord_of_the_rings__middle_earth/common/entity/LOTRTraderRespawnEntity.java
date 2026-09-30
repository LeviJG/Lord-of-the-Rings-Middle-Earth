package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityTraderRespawn: left behind by a slain trader who should come
 * back. It rises, grows into a spinning silver coin and, after 10-30 minutes
 * -- spinning faster and bobbing for the last one -- brings the trader back
 * with its home, its tavern's name and its trades, popping as it does. If the
 * spot is blocked it rises a block and tries again three seconds later. Only
 * a creative player can break it.
 */
public class LOTRTraderRespawnEntity extends Entity {

    public static final int MAX_SCALE = 40;
    private static final byte EVENT_BREAK = 16;

    private static final EntityDataAccessor<Integer> DATA_SCALE =
            SynchedEntityData.defineId(LOTRTraderRespawnEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_SPAWN_IMMINENT =
            SynchedEntityData.defineId(LOTRTraderRespawnEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_TRADER =
            SynchedEntityData.defineId(LOTRTraderRespawnEntity.class, EntityDataSerializers.STRING);

    private int timeUntilSpawn;
    public int prevBobbingTime;
    public int bobbingTime;
    public float spawnerSpin;
    public float prevSpawnerSpin;
    private String traderClassID = "";
    private boolean traderHasHome;
    private BlockPos traderHome = BlockPos.ZERO;
    private float traderHomeRadius;
    private @Nullable String traderLocationName;
    private boolean shouldTraderRespawn;
    /** The trader's trades, as its own save wrote them. */
    private @Nullable CompoundTag traderData;

    public LOTRTraderRespawnEntity(EntityType<? extends LOTRTraderRespawnEntity> type, Level level) {
        super(type, level);
        this.spawnerSpin = this.random.nextFloat() * 360.0f;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_SCALE, 0);
        builder.define(DATA_SPAWN_IMMINENT, false);
        builder.define(DATA_TRADER, "");
    }

    private static Optional<EntityType<?>> typeOf(String id) {
        Identifier key = Identifier.tryParse(id);
        return key == null ? Optional.empty() : BuiltInRegistries.ENTITY_TYPE.getOptional(key);
    }

    public void copyTraderDataFrom(LOTRNPCEntity trader) {
        this.traderClassID = BuiltInRegistries.ENTITY_TYPE.getKey(trader.getType()).toString();
        this.traderHasHome = trader.hasHome();
        if (this.traderHasHome) {
            this.traderHome = trader.getHomePosition();
            this.traderHomeRadius = trader.getHomeRadius();
        }
        this.shouldTraderRespawn = trader.shouldTraderRespawn();
        if (trader.hasSpecificLocationName) {
            this.traderLocationName = trader.npcLocationName;
        }
        if (trader.traderNPCInfo != null) {
            TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registryAccess());
            trader.traderNPCInfo.save(output);
            this.traderData = output.buildResult();
        }
    }

    /** onSpawn: a hop up, and 10 to 30 minutes to wait. */
    public void onSpawn() {
        setDeltaMovement(0.0, 0.25, 0.0);
        this.timeUntilSpawn = Mth.nextInt(this.random, 10, 30) * 1200;
    }

    public int getScale() {
        return this.entityData.get(DATA_SCALE);
    }

    public float getScaleFloat(float partialTick) {
        float scale = getScale();
        if (scale < MAX_SCALE) {
            scale += partialTick;
        }
        return scale / MAX_SCALE;
    }

    public float getBobbingOffset(float partialTick) {
        float f = this.bobbingTime - this.prevBobbingTime;
        return Mth.sin((this.prevBobbingTime + f * partialTick) / 5.0f) * 0.25f;
    }

    public boolean isSpawnImminent() {
        return this.entityData.get(DATA_SPAWN_IMMINENT);
    }

    /** canBeCollidedWith: it can be clicked, though nothing bumps into it. */
    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        Optional<EntityType<?>> type = typeOf(this.entityData.get(DATA_TRADER));
        return type.flatMap(SpawnEggItem::byId).map(egg -> new ItemStack(egg.value())).orElse(null);
    }

    /** Only a creative player breaks it, with glass's sound and a burst of coin. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (source.getEntity() instanceof Player player && player.getAbilities().instabuild) {
            SoundType glass = SoundType.GLASS;
            playSound(glass.getBreakSound(), (glass.getVolume() + 1.0f) / 2.0f, glass.getPitch() * 0.8f);
            level.broadcastEntityEvent(this, EVENT_BREAK);
            discard();
            return true;
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_BREAK) {
            ItemParticleOption coin = new ItemParticleOption(ParticleTypes.ITEM,
                    ItemStackTemplate.fromNonEmptyStack(new ItemStack(LOTRMiscItems.SILVER_COIN)));
            for (int l = 0; l < 16; ++l) {
                level().addParticle(coin, getX() + (this.random.nextDouble() - 0.5) * getBbWidth(),
                        getY() + this.random.nextDouble() * getBbHeight(),
                        getZ() + (this.random.nextDouble() - 0.5) * getBbWidth(), 0.0, 0.0, 0.0);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.prevSpawnerSpin = this.spawnerSpin;
        this.spawnerSpin = isSpawnImminent() ? this.spawnerSpin + 24.0f : this.spawnerSpin + 6.0f;
        this.prevSpawnerSpin = Mth.wrapDegrees(this.prevSpawnerSpin);
        this.spawnerSpin = Mth.wrapDegrees(this.spawnerSpin);
        if (getScale() < MAX_SCALE) {
            if (!level().isClientSide()) {
                this.entityData.set(DATA_SCALE, getScale() + 1);
            }
            setDeltaMovement(0.0, getDeltaMovement().y * 0.9, 0.0);
        } else {
            setDeltaMovement(0.0, 0.0, 0.0);
        }
        move(MoverType.SELF, getDeltaMovement());
        if (level() instanceof ServerLevel level) {
            this.entityData.set(DATA_TRADER, this.traderClassID);
            if (!isSpawnImminent() && this.timeUntilSpawn <= 1200) {
                this.entityData.set(DATA_SPAWN_IMMINENT, true);
            }
            if (this.timeUntilSpawn > 0) {
                --this.timeUntilSpawn;
            } else if (respawnTrader(level)) {
                playSound(SoundEvents.ITEM_PICKUP, 1.0f, 0.5f + this.random.nextFloat() * 0.5f);
                discard();
            } else {
                this.timeUntilSpawn = 60;
                snapTo(getX(), getY() + 1.0, getZ(), getYRot(), getXRot());
            }
        } else if (isSpawnImminent()) {
            this.prevBobbingTime = this.bobbingTime++;
        }
    }

    /** The trader, back where the coin is, if there is room for it. */
    private boolean respawnTrader(ServerLevel level) {
        Optional<EntityType<?>> type = typeOf(this.traderClassID);
        if (type.isEmpty() || !(type.get().create(level, EntitySpawnReason.TRIGGERED) instanceof LOTRNPCEntity trader)) {
            return false;
        }
        trader.snapTo(getX(), getY(), getZ(), this.random.nextFloat() * 360.0f, 0.0f);
        // getCanSpawnHere with liftSpawnRestrictions: only room to stand.
        if (!trader.checkSpawnObstruction(level)) {
            return false;
        }
        trader.finalizeSpawn(level, level.getCurrentDifficultyAt(trader.blockPosition()), EntitySpawnReason.TRIGGERED, null);
        if (this.traderHasHome) {
            trader.setHomeTo(this.traderHome, Math.round(this.traderHomeRadius));
        }
        if (this.traderLocationName != null) {
            trader.npcLocationName = this.traderLocationName;
            trader.hasSpecificLocationName = true;
        }
        trader.shouldTraderRespawn = this.shouldTraderRespawn;
        boolean spawned = level.addFreshEntity(trader);
        if (trader.traderNPCInfo != null && this.traderData != null) {
            trader.traderNPCInfo.load(TagValueInput.create(ProblemReporter.DISCARDING, registryAccess(), this.traderData));
        }
        return spawned;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.entityData.set(DATA_SCALE, input.getIntOr("Scale", 0));
        this.timeUntilSpawn = input.getIntOr("TimeUntilSpawn", 0);
        if (this.timeUntilSpawn <= 1200) {
            this.entityData.set(DATA_SPAWN_IMMINENT, true);
        }
        this.traderClassID = input.getStringOr("TraderClassID", "");
        this.traderHasHome = input.getBooleanOr("TraderHasHome", false);
        this.traderHome = new BlockPos(input.getIntOr("TraderHomeX", 0), input.getIntOr("TraderHomeY", 0),
                input.getIntOr("TraderHomeZ", 0));
        this.traderHomeRadius = input.getFloatOr("TraderHomeRadius", 0.0f);
        this.shouldTraderRespawn = input.getBooleanOr("TraderShouldRespawn", true);
        this.traderLocationName = input.getString("TraderLocationName").orElse(null);
        this.traderData = input.read("TraderData", CompoundTag.CODEC).orElse(null);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("Scale", getScale());
        output.putInt("TimeUntilSpawn", this.timeUntilSpawn);
        output.putString("TraderClassID", this.traderClassID);
        output.putBoolean("TraderHasHome", this.traderHasHome);
        output.putInt("TraderHomeX", this.traderHome.getX());
        output.putInt("TraderHomeY", this.traderHome.getY());
        output.putInt("TraderHomeZ", this.traderHome.getZ());
        output.putFloat("TraderHomeRadius", this.traderHomeRadius);
        output.putBoolean("TraderShouldRespawn", this.shouldTraderRespawn);
        if (this.traderLocationName != null) {
            output.putString("TraderLocationName", this.traderLocationName);
        }
        if (this.traderData != null) {
            output.store("TraderData", CompoundTag.CODEC, this.traderData);
        }
    }
}
