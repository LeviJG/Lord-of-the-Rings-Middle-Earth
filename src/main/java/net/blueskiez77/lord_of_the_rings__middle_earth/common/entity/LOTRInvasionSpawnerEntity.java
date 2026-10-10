package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.banner.LOTRBannerProtection;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRWarhornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRInvasionPayloads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWeightedRandom;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityInvasionSpawner: an invasion -- a spinning icon of the warband's people, out of which
 * its NPCs come, a few at a time, while a player is within 80 blocks, until as many as it numbered
 * have been killed by players. Those who helped and stand near when it ends, if no friend of its
 * faction, earn defeatInvasion. A creative player strikes it to end it; one who uses it sees the
 * factions killing its NPCs also counts for.
 *
 * <p>NOT ported yet: the conquest boost its defeat gave the pledged enemies of its faction (conquest).
 */
public class LOTRInvasionSpawnerEntity extends Entity {
    public static final int MAX_INVASION_SIZE = 10000;
    public static final double INVASION_FOLLOW_RANGE = 40.0;

    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRInvasionSpawnerEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_SIZE =
            SynchedEntityData.defineId(LOTRInvasionSpawnerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_REMAINING =
            SynchedEntityData.defineId(LOTRInvasionSpawnerEntity.class, EntityDataSerializers.INT);

    private static final Codec<Map<UUID, Integer>> RECENT_PLAYERS_CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT);

    public float spawnerSpin;
    public float prevSpawnerSpin;
    public int invasionSize;
    public int invasionRemaining;
    public int successiveFailedSpawns;
    public int timeSincePlayerProgress;
    public final Map<UUID, Integer> recentPlayerContributors = new HashMap<>();
    public boolean isWarhorn;
    public boolean spawnsPersistent = true;
    public final Collection<LOTRFaction> bonusFactions = new ArrayList<>();

    public LOTRInvasionSpawnerEntity(EntityType<? extends LOTRInvasionSpawnerEntity> type, Level level) {
        super(type, level);
        this.spawnerSpin = this.random.nextFloat() * 360.0f;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TYPE, (byte) 0);
        builder.define(DATA_SIZE, 0);
        builder.define(DATA_REMAINING, 0);
    }

    /** locateInvasionNearby: the invasion of this id within 256 blocks, if any. */
    public static @Nullable LOTRInvasionSpawnerEntity locateInvasionNearby(Entity seeker, UUID id) {
        double search = 256.0;
        List<LOTRInvasionSpawnerEntity> invasions = seeker.level().getEntitiesOfClass(LOTRInvasionSpawnerEntity.class,
                seeker.getBoundingBox().inflate(search), e -> e.getUUID().equals(id));
        return invasions.isEmpty() ? null : invasions.getFirst();
    }

    public void addPlayerKill(Player player) {
        --this.invasionRemaining;
        this.timeSincePlayerProgress = 0;
        this.recentPlayerContributors.put(player.getUUID(), 2400);
    }

    public void announceInvasionTo(Player player) {
        player.sendSystemMessage(Component.translatable("chat.lotr.invasion.start", getInvasionType().invasionName()));
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity entity) {
        return true;
    }

    /** A creative player's blow ends it. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (source.getEntity() instanceof Player player && player.getAbilities().instabuild) {
            endInvasion(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        if (entity instanceof Player player && level() instanceof ServerLevel level) {
            hurtServer(level, level.damageSources().playerAttack(player), 0.0f);
        }
        return true;
    }

    /** attemptSpawnMob: the NPC set down within six blocks, on solid ground, joined to the invasion. */
    public boolean attemptSpawnMob(ServerLevel level, LOTRNPCEntity npc) {
        for (int at = 0; at < 40; ++at) {
            int i = Mth.floor(getX()) + LOTRWorldGenUtil.getRandomIntegerInRange(this.random, -6, 6);
            int k = Mth.floor(getZ()) + LOTRWorldGenUtil.getRandomIntegerInRange(this.random, -6, 6);
            int j = Mth.floor(getY()) + LOTRWorldGenUtil.getRandomIntegerInRange(this.random, -8, 4);
            BlockPos below = new BlockPos(i, j - 1, k);
            if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
                continue;
            }
            npc.snapTo(i + 0.5, j, k + 0.5, this.random.nextFloat() * 360.0f, 0.0f);
            npc.liftSpawnRestrictions = true;
            if (!npc.checkSpawnRules(level, EntitySpawnReason.EVENT) || !npc.checkSpawnObstruction(level)) {
                continue;
            }
            npc.liftSpawnRestrictions = false;
            npc.finalizeSpawn(level, level.getCurrentDifficultyAt(npc.blockPosition()), EntitySpawnReason.EVENT, null);
            npc.isNPCPersistent = this.spawnsPersistent;
            npc.setInvasionID(getInvasionID());
            npc.killBonusFactions.addAll(this.bonusFactions);
            level.addFreshEntityWithPassengers(npc);
            AttributeInstance followRangeAttrib = npc.getAttribute(Attributes.FOLLOW_RANGE);
            if (followRangeAttrib != null) {
                followRangeAttrib.setBaseValue(Math.max(followRangeAttrib.getBaseValue(), INVASION_FOLLOW_RANGE));
            }
            return true;
        }
        return false;
    }

    /** canInvasionSpawnHere: no banner of an enemy, no respawner against its faction, and room. */
    public boolean canInvasionSpawnHere() {
        if (LOTRBannerProtection.isProtected(level(), this, LOTRBannerProtection.forInvasionSpawner(this), false)
                || LOTRNPCRespawnerEntity.isSpawnBlocked(this, getInvasionType().invasionFaction)) {
            return false;
        }
        return level().isUnobstructed(this) && level().noCollision(this) && !level().containsAnyLiquid(getBoundingBox());
    }

    public void endInvasion(boolean completed) {
        if (completed && level() instanceof ServerLevel level) {
            LOTRFaction invasionFac = getInvasionType().invasionFaction;
            Collection<Player> achievementPlayers = new HashSet<>();
            for (UUID id : this.recentPlayerContributors.keySet()) {
                Player player = level.getPlayerByUUID(id);
                double range = 100.0;
                if (player == null || player.distanceToSqr(this) >= range * range) {
                    continue;
                }
                if (LOTRPlayerAlignments.getAlignment(player, invasionFac) <= 0.0f) {
                    achievementPlayers.add(player);
                }
            }
            for (Player player : achievementPlayers) {
                LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.DEFEAT_INVASION);
            }
        }
        level().explode(this, getX(), getY() + getBbHeight() / 2.0, getZ(), 0.0f, Level.ExplosionInteraction.NONE);
        discard();
    }

    public float getInvasionHealthStatus() {
        int size = getInvasionSize();
        return size <= 0 ? 0.0f : (float) getInvasionRemaining() / size;
    }

    public UUID getInvasionID() {
        return getUUID();
    }

    public ItemStack getInvasionItem() {
        return getInvasionType().getInvasionIcon();
    }

    public int getInvasionSize() {
        return level().isClientSide() ? this.entityData.get(DATA_SIZE) : this.invasionSize;
    }

    public int getInvasionRemaining() {
        return level().isClientSide() ? this.entityData.get(DATA_REMAINING) : this.invasionRemaining;
    }

    public LOTRInvasions getInvasionType() {
        int i = this.entityData.get(DATA_TYPE);
        LOTRInvasions[] types = LOTRInvasions.values();
        return i >= 0 && i < types.length ? types[i] : LOTRInvasions.HOBBIT;
    }

    public void setInvasionType(LOTRInvasions type) {
        this.entityData.set(DATA_TYPE, (byte) type.ordinal());
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        return LOTRWarhornItem.createHorn(getInvasionType());
    }

    /** interactFirst: a creative player is told the factions its kills also count for. */
    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (!level().isClientSide() && player.getAbilities().instabuild && !this.bonusFactions.isEmpty()) {
            MutableComponent message = Component.empty();
            for (LOTRFaction f : this.bonusFactions) {
                if (!message.getSiblings().isEmpty()) {
                    message.append(", ");
                }
                message.append(f.factionName());
            }
            player.sendSystemMessage(message);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void tick() {
        if (level() instanceof ServerLevel level && level.getDifficulty() == Difficulty.PEACEFUL) {
            endInvasion(false);
            return;
        }
        super.tick();
        this.prevSpawnerSpin = this.spawnerSpin;
        this.spawnerSpin += 6.0f;
        this.prevSpawnerSpin = Mth.wrapDegrees(this.prevSpawnerSpin);
        this.spawnerSpin = Mth.wrapDegrees(this.spawnerSpin);
        setDeltaMovement(Vec3.ZERO);
        if (level() instanceof ServerLevel level) {
            if (this.invasionRemaining > 0) {
                ++this.timeSincePlayerProgress;
                if (LOTRConfig.invasionProgressReverts && this.timeSincePlayerProgress >= 6000 && !this.isWarhorn
                        && this.timeSincePlayerProgress % 1200 == 0) {
                    this.invasionRemaining = Math.min(this.invasionRemaining + 1, this.invasionSize);
                }
                this.recentPlayerContributors.replaceAll((player, time) -> time - 1);
                this.recentPlayerContributors.values().removeIf(time -> time <= 0);
            } else {
                endInvasion(true);
                return;
            }
            if (level.getGameRules().get(GameRules.SPAWN_MOBS)) {
                LOTRInvasions invasionType = getInvasionType();
                Player closePlayer = level.getNearestPlayer(getX(), getY(), getZ(), 80.0, false);
                double nearbySearch = INVASION_FOLLOW_RANGE * 2.0;
                if (closePlayer != null && this.invasionRemaining > 0 && !invasionType.invasionMobs.isEmpty()
                        && level.getEntitiesOfClass(LOTRNPCEntity.class, getBoundingBox().inflate(nearbySearch),
                        npc -> npc.isAlive() && getInvasionID().equals(npc.getInvasionID())).size() < 16
                        && this.random.nextInt(160) == 0) {
                    int spawnAttempts = Math.min(LOTRWorldGenUtil.getRandomIntegerInRange(this.random, 1, 6), this.invasionRemaining);
                    boolean spawnedAnyMobs = false;
                    for (int l = 0; l < spawnAttempts; ++l) {
                        LOTRInvasions.InvasionSpawnEntry entry = LOTRWeightedRandom.getRandomItem(this.random, invasionType.invasionMobs);
                        LOTRNPCEntity npc = entry.entityClass().create(level, EntitySpawnReason.EVENT);
                        if (npc != null && attemptSpawnMob(level, npc)) {
                            spawnedAnyMobs = true;
                        }
                    }
                    if (spawnedAnyMobs) {
                        this.successiveFailedSpawns = 0;
                        playHorn();
                    } else if (++this.successiveFailedSpawns >= 16) {
                        endInvasion(false);
                        return;
                    }
                }
            }
            this.entityData.set(DATA_SIZE, this.invasionSize);
            this.entityData.set(DATA_REMAINING, this.invasionRemaining);
        } else {
            level().addParticle(this.random.nextBoolean() ? ParticleTypes.SMOKE : ParticleTypes.FLAME,
                    getX() + (this.random.nextDouble() - 0.5) * getBbWidth(), getY() + this.random.nextDouble() * getBbHeight(),
                    getZ() + (this.random.nextDouble() - 0.5) * getBbWidth(), 0.0, 0.0, 0.0);
        }
    }

    public void playHorn() {
        level().playSound(null, this, LOTRSounds.ITEM_HORN, SoundSource.HOSTILE, 4.0f, 0.65f + this.random.nextFloat() * 0.1f);
    }

    /** selectAppropriateBonusFactions: the factions its kills also count for -- those whose control zone it is in, or the nearest within 150. */
    public void selectAppropriateBonusFactions() {
        if (LOTRFaction.controlZonesEnabled(level())) {
            LOTRFaction invasionFaction = getInvasionType().invasionFaction;
            for (LOTRFaction faction : invasionFaction.getBonusesForKilling()) {
                if (!faction.isolationist && faction.inDefinedControlZone(level(), getX(), getY(), getZ(), 50)) {
                    this.bonusFactions.add(faction);
                }
            }
            if (this.bonusFactions.isEmpty()) {
                int nearestRange = 150;
                LOTRFaction nearest = null;
                double nearestDist = Double.MAX_VALUE;
                for (LOTRFaction faction : invasionFaction.getBonusesForKilling()) {
                    double dist;
                    if (faction.isolationist || (dist = faction.distanceToNearestControlZoneInRange(level(), getX(), getY(), getZ(), nearestRange)) < 0.0
                            || nearest != null && dist >= nearestDist) {
                        continue;
                    }
                    nearest = faction;
                    nearestDist = dist;
                }
                if (nearest != null) {
                    this.bonusFactions.add(nearest);
                }
            }
        }
    }

    /** setWatchingInvasion: the player's invasion bar shows this one. */
    public void setWatchingInvasion(ServerPlayer player, boolean overrideAlreadyWatched) {
        ServerPlayNetworking.send(player, new LOTRInvasionPayloads.Watch(getId(), overrideAlreadyWatched));
    }

    public void startInvasion() {
        startInvasion(null);
    }

    public void startInvasion(@Nullable Player announcePlayer) {
        startInvasion(announcePlayer, -1);
    }

    /** startInvasion: 30 to 70 strong unless told; the horn, and word to those near who are its faction's enemies. */
    public void startInvasion(@Nullable Player announcePlayer, int size) {
        if (size < 0) {
            size = LOTRWorldGenUtil.getRandomIntegerInRange(this.random, 30, 70);
        }
        this.invasionRemaining = this.invasionSize = size;
        this.entityData.set(DATA_SIZE, this.invasionSize);
        this.entityData.set(DATA_REMAINING, this.invasionRemaining);
        playHorn();
        double announceRange = INVASION_FOLLOW_RANGE * 2.0;
        List<Player> nearbyPlayers = new ArrayList<>(level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(announceRange)));
        if (announcePlayer != null && !nearbyPlayers.contains(announcePlayer)) {
            nearbyPlayers.add(announcePlayer);
        }
        for (Player player : nearbyPlayers) {
            boolean announce = LOTRPlayerAlignments.getAlignment(player, getInvasionType().invasionFaction) < 0.0f || player == announcePlayer;
            if (!announce) {
                continue;
            }
            announceInvasionTo(player);
            if (player instanceof ServerPlayer serverPlayer) {
                setWatchingInvasion(serverPlayer, false);
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        LOTRInvasions type = LOTRInvasions.forName(input.getStringOr("InvasionType", ""));
        if (type == null) {
            type = LOTRInvasions.forName(input.getStringOr("Faction", ""));
        }
        if (type == null || type.invasionMobs.isEmpty()) {
            discard();
            return;
        }
        setInvasionType(type);
        int mobsRemaining = input.getIntOr("MobsRemaining", -1);
        if (mobsRemaining >= 0) {
            this.invasionSize = this.invasionRemaining = mobsRemaining;
        } else {
            this.invasionSize = input.getIntOr("InvasionSize", 0);
            this.invasionRemaining = input.getIntOr("InvasionRemaining", this.invasionSize);
        }
        this.successiveFailedSpawns = input.getIntOr("SuccessiveFailedSpawns", 0);
        this.timeSincePlayerProgress = input.getIntOr("TimeSinceProgress", 0);
        this.recentPlayerContributors.clear();
        input.read("RecentPlayers", RECENT_PLAYERS_CODEC).ifPresent(this.recentPlayerContributors::putAll);
        this.isWarhorn = input.getBooleanOr("Warhorn", this.isWarhorn);
        this.spawnsPersistent = input.getBooleanOr("NPCPersistent", this.spawnsPersistent);
        this.bonusFactions.clear();
        input.read("BonusFactions", LOTRFaction.CODEC.listOf()).ifPresent(this.bonusFactions::addAll);
        this.entityData.set(DATA_SIZE, this.invasionSize);
        this.entityData.set(DATA_REMAINING, this.invasionRemaining);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putString("InvasionType", getInvasionType().codeName());
        output.putInt("InvasionSize", this.invasionSize);
        output.putInt("InvasionRemaining", this.invasionRemaining);
        output.putInt("SuccessiveFailedSpawns", this.successiveFailedSpawns);
        output.putInt("TimeSinceProgress", this.timeSincePlayerProgress);
        if (!this.recentPlayerContributors.isEmpty()) {
            output.store("RecentPlayers", RECENT_PLAYERS_CODEC, this.recentPlayerContributors);
        }
        output.putBoolean("Warhorn", this.isWarhorn);
        output.putBoolean("NPCPersistent", this.spawnsPersistent);
        if (!this.bonusFactions.isEmpty()) {
            output.store("BonusFactions", LOTRFaction.CODEC.listOf(), List.copyOf(this.bonusFactions));
        }
    }
}
