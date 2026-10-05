package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import java.util.List;
import java.util.function.BooleanSupplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLegacyWorld;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRRespawnerPayloads;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityNPCRespawner: an unseen marker, left by a structure or placed by
 * a creative player, that keeps its people in the place. Every three minutes
 * (by default), when no player is within 24 blocks and the chunks about it
 * are loaded, it counts its kinds within its check box; while fewer than its
 * cap, it tries sixteen spots within its spawn range -- solid ground below,
 * room above -- and brings one of them there (the second kind one time in
 * three), kept for good, riding as its mount setting says, homed on itself or
 * on the spot. It may also keep its people's enemies from spawning within a
 * range of it. Only a creative player sees it: a spinning respawner icon, two
 * blocks across.
 *
 * <p>A creative player right-clicks it for its screen (LOTRPacketNPCRespawner),
 * which sends its settings back as it closes, and may destroy it
 * (LOTRPacketEditNPCRespawner).
 */
public class LOTRNPCRespawnerEntity extends Entity {

    public static final int MAX_SPAWN_BLOCK_RANGE = 64;
    private static final byte EVENT_BREAK = 16;

    /** Set by the client: whether its player is in creative, who alone sees and can pick it. */
    public static BooleanSupplier clientCreative = () -> false;

    public float spawnerSpin;
    public float prevSpawnerSpin;
    public int spawnInterval = 3600;
    public int noPlayerRange = 24;
    public @Nullable EntityType<?> spawnClass1;
    public @Nullable EntityType<?> spawnClass2;
    public int checkHorizontalRange = 8;
    public int checkVerticalMin = -4;
    public int checkVerticalMax = 4;
    public int spawnCap = 4;
    public int spawnHorizontalRange = 4;
    public int spawnVerticalMin = -2;
    public int spawnVerticalMax = 2;
    public int homeRange = -1;
    public boolean setHomePosFromSpawn;
    public int mountSetting;
    public int blockEnemySpawns;

    public LOTRNPCRespawnerEntity(EntityType<? extends LOTRNPCRespawnerEntity> type, Level level) {
        super(type, level);
        this.spawnerSpin = this.random.nextFloat() * 360.0f;
    }

    public LOTRNPCRespawnerEntity(Level level) {
        this(LOTREntities.NPC_RESPAWNER, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    /** isSpawnBlocked: whether a respawner within 64 blocks keeps this faction's kind from spawning here. */
    public static boolean isSpawnBlocked(Entity entity, LOTRFaction spawnFaction) {
        BlockPos pos = BlockPos.containing(entity.getX(), entity.getBoundingBox().minY, entity.getZ());
        AABB originBB = new AABB(pos);
        AABB searchBB = originBB.inflate(MAX_SPAWN_BLOCK_RANGE);
        for (LOTRNPCRespawnerEntity spawner : entity.level().getEntitiesOfClass(LOTRNPCRespawnerEntity.class, searchBB)) {
            AABB spawnBlockBB = spawner.createSpawnBlockRegion();
            if (spawnBlockBB != null && spawnBlockBB.intersects(searchBB) && spawnBlockBB.intersects(originBB)
                    && spawner.isEnemySpawnBlocked(spawnFaction)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSpawnBlocked(LOTRNPCEntity npc) {
        return isSpawnBlocked(npc, npc.getFaction());
    }

    public boolean blockEnemySpawns() {
        return this.blockEnemySpawns > 0;
    }

    public @Nullable AABB createSpawnBlockRegion() {
        if (!blockEnemySpawns()) {
            return null;
        }
        BlockPos pos = BlockPos.containing(getX(), getBoundingBox().minY, getZ());
        return new AABB(pos).inflate(this.blockEnemySpawns);
    }

    public boolean isEnemySpawnBlocked(LOTRFaction spawnFaction) {
        return factionBlocks(this.spawnClass1, spawnFaction) || factionBlocks(this.spawnClass2, spawnFaction);
    }

    private boolean factionBlocks(@Nullable EntityType<?> type, LOTRFaction spawnFaction) {
        return type != null && type.create(level(), EntitySpawnReason.LOAD) instanceof LOTRNPCEntity npc
                && npc.getFaction().isBadRelation(spawnFaction);
    }

    public boolean hasHomeRange() {
        return this.homeRange >= 0;
    }

    @Override
    public boolean isPickable() {
        return level().isClientSide() && clientCreative.getAsBoolean();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        return new ItemStack(LOTRMiscItems.NPC_RESPAWNER);
    }

    /** Nothing harms it: only its screen's Destroy button breaks it. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    /** interactFirst: a creative player is sent its settings, and its screen opens. */
    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (player.getAbilities().instabuild) {
            if (player instanceof ServerPlayer serverPlayer) {
                ServerPlayNetworking.send(serverPlayer, new LOTRRespawnerPayloads.Open(getId(), writeSpawnerData()));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    /** writeSpawnerDataToNBT. */
    public CompoundTag writeSpawnerData() {
        TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
        addAdditionalSaveData(output);
        return output.buildResult();
    }

    /** readSpawnerDataFromNBT. */
    public void readSpawnerData(CompoundTag data) {
        readAdditionalSaveData(TagValueInput.create(ProblemReporter.DISCARDING, level().registryAccess(), data));
    }

    public void onBreak() {
        SoundType glass = SoundType.GLASS;
        playSound(glass.getBreakSound(), (glass.getVolume() + 1.0f) / 2.0f, glass.getPitch() * 0.8f);
        level().broadcastEntityEvent(this, EVENT_BREAK);
        discard();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_BREAK) {
            ItemParticleOption icon = new ItemParticleOption(ParticleTypes.ITEM,
                    ItemStackTemplate.fromNonEmptyStack(new ItemStack(LOTRMiscItems.NPC_RESPAWNER)));
            for (int l = 0; l < 16; ++l) {
                level().addParticle(icon, getX() + (this.random.nextDouble() - 0.5) * getBbWidth(),
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
        this.spawnerSpin += 6.0f;
        this.prevSpawnerSpin = Mth.wrapDegrees(this.prevSpawnerSpin);
        this.spawnerSpin = Mth.wrapDegrees(this.spawnerSpin);
        setDeltaMovement(0.0, 0.0, 0.0);
        move(MoverType.SELF, getDeltaMovement());
        if (level() instanceof ServerLevel level && this.tickCount % this.spawnInterval == 0
                && (this.spawnClass1 != null || this.spawnClass2 != null)) {
            trySpawn(level);
        }
    }

    private void trySpawn(ServerLevel level) {
        int i = Mth.floor(getX());
        int j = Mth.floor(getBoundingBox().minY);
        int k = Mth.floor(getZ());
        int minX = i - this.checkHorizontalRange;
        int minY = j + this.checkVerticalMin;
        int minZ = k - this.checkHorizontalRange;
        int maxX = i + this.checkHorizontalRange;
        int maxY = j + this.checkVerticalMax;
        int maxZ = k + this.checkHorizontalRange;
        if (!LOTRLegacyWorld.hasChunksAt(level, minX, minZ, maxX, maxZ)
                || level.getNearestPlayer(i + 0.5, j + 0.5, k + 0.5, this.noPlayerRange, false) != null) {
            return;
        }
        // A kind counts its own subclasses too (Class.isAssignableFrom): a
        // Gondorian man's respawner counts the soldiers among them.
        Class<?> class1 = classOf(level, this.spawnClass1);
        Class<?> class2 = classOf(level, this.spawnClass2);
        List<Mob> present = level.getEntitiesOfClass(Mob.class, new AABB(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1),
                e -> e.isAlive() && (class1 != null && class1.isInstance(e) || class2 != null && class2.isInstance(e)));
        int entities = present.size();
        if (entities >= this.spawnCap) {
            return;
        }
        for (int l = 0; l < 16; ++l) {
            BlockPos spawn = new BlockPos(i + Mth.randomBetweenInclusive(this.random, -this.spawnHorizontalRange, this.spawnHorizontalRange),
                    j + Mth.randomBetweenInclusive(this.random, this.spawnVerticalMin, this.spawnVerticalMax),
                    k + Mth.randomBetweenInclusive(this.random, -this.spawnHorizontalRange, this.spawnHorizontalRange));
            BlockPos below = spawn.below();
            if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)
                    || level.getBlockState(spawn).isRedstoneConductor(level, spawn)
                    || level.getBlockState(spawn.above()).isRedstoneConductor(level, spawn.above())) {
                continue;
            }
            EntityType<?> type;
            if (this.spawnClass1 != null && this.spawnClass2 != null) {
                type = this.random.nextInt(3) == 0 ? this.spawnClass2 : this.spawnClass1;
            } else {
                type = this.spawnClass1 != null ? this.spawnClass1 : this.spawnClass2;
            }
            if (!(type.create(level, EntitySpawnReason.SPAWNER) instanceof LOTRNPCEntity npc)) {
                continue;
            }
            npc.snapTo(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, this.random.nextFloat() * 360.0f, 0.0f);
            npc.isNPCPersistent = true;
            npc.liftSpawnRestrictions = true;
            if (!npc.checkSpawnRules(level, EntitySpawnReason.SPAWNER) || !npc.checkSpawnObstruction(level)) {
                continue;
            }
            npc.liftSpawnRestrictions = false;
            if (this.mountSetting == 0) {
                npc.spawnRidingHorse = false;
            } else if (this.mountSetting == 1) {
                npc.spawnRidingHorse = true;
            }
            npc.finalizeSpawn(level, level.getCurrentDifficultyAt(spawn), EntitySpawnReason.SPAWNER, null);
            level.addFreshEntityWithPassengers(npc);
            if (hasHomeRange()) {
                npc.setHomeTo(this.setHomePosFromSpawn ? spawn : new BlockPos(i, j, k), this.homeRange);
            } else {
                npc.clearHome();
            }
            if (++entities >= this.spawnCap) {
                break;
            }
        }
    }

    private static @Nullable Class<?> classOf(Level level, @Nullable EntityType<?> type) {
        Entity sample = type == null ? null : type.create(level, EntitySpawnReason.LOAD);
        return sample == null ? null : sample.getClass();
    }

    public void setBlockEnemySpawnRange(int i) {
        this.blockEnemySpawns = Math.min(i, MAX_SPAWN_BLOCK_RANGE);
    }

    public void setCheckRanges(int xz, int y, int y1, int l) {
        this.checkHorizontalRange = xz;
        this.checkVerticalMin = y;
        this.checkVerticalMax = y1;
        this.spawnCap = l;
    }

    public void setHomePosFromSpawn() {
        this.setHomePosFromSpawn = true;
    }

    public void setMountSetting(int i) {
        this.mountSetting = i;
    }

    public void setNoPlayerRange(int i) {
        this.noPlayerRange = i;
    }

    public void setSpawnClass(EntityType<? extends LOTRNPCEntity> type) {
        this.spawnClass1 = type;
    }

    public void setSpawnClasses(@Nullable EntityType<? extends LOTRNPCEntity> type1,
                                @Nullable EntityType<? extends LOTRNPCEntity> type2) {
        this.spawnClass1 = type1;
        this.spawnClass2 = type2;
    }

    public void setSpawnInterval(int i) {
        this.spawnInterval = i;
    }

    public void setSpawnIntervalMinutes(int m) {
        this.spawnInterval = m * 60 * 20;
    }

    public void setSpawnRanges(int xz, int y, int y1, int h) {
        this.spawnHorizontalRange = xz;
        this.spawnVerticalMin = y;
        this.spawnVerticalMax = y1;
        this.homeRange = h;
    }

    public void toggleMountSetting() {
        this.mountSetting = this.mountSetting == 0 ? 1 : this.mountSetting == 1 ? 2 : 0;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("SpawnInterval", this.spawnInterval);
        output.putByte("NoPlayerRange", (byte) this.noPlayerRange);
        output.putString("SpawnClass1", this.spawnClass1 == null ? "" : BuiltInRegistries.ENTITY_TYPE.getKey(this.spawnClass1).toString());
        output.putString("SpawnClass2", this.spawnClass2 == null ? "" : BuiltInRegistries.ENTITY_TYPE.getKey(this.spawnClass2).toString());
        output.putByte("CheckHorizontal", (byte) this.checkHorizontalRange);
        output.putByte("CheckVerticalMin", (byte) this.checkVerticalMin);
        output.putByte("CheckVerticalMax", (byte) this.checkVerticalMax);
        output.putByte("SpawnCap", (byte) this.spawnCap);
        output.putByte("SpawnHorizontal", (byte) this.spawnHorizontalRange);
        output.putByte("SpawnVerticalMin", (byte) this.spawnVerticalMin);
        output.putByte("SpawnVerticalMax", (byte) this.spawnVerticalMax);
        output.putByte("HomeRange", (byte) this.homeRange);
        output.putBoolean("HomeSpawn", this.setHomePosFromSpawn);
        output.putByte("MountSetting", (byte) this.mountSetting);
        output.putByte("BlockEnemy", (byte) this.blockEnemySpawns);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.spawnInterval = input.getIntOr("SpawnInterval", 0);
        if (this.spawnInterval <= 0) {
            this.spawnInterval = 3600;
        }
        this.noPlayerRange = input.getByteOr("NoPlayerRange", (byte) 0);
        this.spawnClass1 = npcType(input.getStringOr("SpawnClass1", ""));
        this.spawnClass2 = npcType(input.getStringOr("SpawnClass2", ""));
        this.checkHorizontalRange = input.getByteOr("CheckHorizontal", (byte) 0);
        this.checkVerticalMin = input.getByteOr("CheckVerticalMin", (byte) 0);
        this.checkVerticalMax = input.getByteOr("CheckVerticalMax", (byte) 0);
        this.spawnCap = input.getByteOr("SpawnCap", (byte) 0);
        this.spawnHorizontalRange = input.getByteOr("SpawnHorizontal", (byte) 0);
        this.spawnVerticalMin = input.getByteOr("SpawnVerticalMin", (byte) 0);
        this.spawnVerticalMax = input.getByteOr("SpawnVerticalMax", (byte) 0);
        this.homeRange = input.getByteOr("HomeRange", (byte) 0);
        this.setHomePosFromSpawn = input.getBooleanOr("HomeSpawn", false);
        this.mountSetting = input.getByteOr("MountSetting", (byte) 0);
        this.blockEnemySpawns = input.getByteOr("BlockEnemy", (byte) 0);
    }

    /** A saved kind; one that is not an NPC's spawns nothing. */
    private static @Nullable EntityType<?> npcType(String id) {
        Identifier key = id.isEmpty() ? null : Identifier.tryParse(id);
        return key == null ? null : BuiltInRegistries.ENTITY_TYPE.getOptional(key).orElse(null);
    }
}
