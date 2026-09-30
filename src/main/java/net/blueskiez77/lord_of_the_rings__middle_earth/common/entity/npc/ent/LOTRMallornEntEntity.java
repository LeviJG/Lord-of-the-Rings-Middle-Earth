package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRMallornLeafBombEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBossJumpAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBoss;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRMallornEntHealPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRMallornEntSummonPayload;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMallornEnt: the great Ent of the golden wood, called up by the
 * third Ent slain beside a corrupt mallorn -- half as big again as an Ent,
 * 300 strong, hitting for 8 with a reach of 12 blocks, hurling leaf bombs
 * (6) further off, seeing 32 blocks.
 *
 * <p>It rises out of the ground over seven and a half seconds, untouchable and
 * unmoving, and leaps as it comes clear. Anything but a player (or their hired
 * units) does it at most one point of harm, and whatever would not fell a tree
 * half. Brought to nothing the first time, it raises its weapon shield and is
 * whole again: from then on only fire hurts it, and only while it burns does
 * the shield fail. The more it is hurt, the more it leaps at those about it and
 * at the last player to hurt it when far or high above, draws strength from
 * leaves within 16 blocks (up to five at once, two a second for 15 to 30
 * seconds), and calls up Ents and Huorns (one in three) near it, while fewer
 * than six trees are near. The branches on its head fall away with its health
 * until the shield is up. Fire does not send it into a panic, and when it dies
 * it puts out every fire within 12 blocks.
 *
 * <p>It leaves 20 to 30 mallorn logs and 30 to 40 mallorn sticks (more with
 * looting), an Ent's leavings, its trophy and sometimes the charred mallorn
 * mace. It is worth 50 alignment and 100 experience, and has no speech bank
 * of its own: it speaks only as it is called up and as its shield rises.
 *
 * <p>NOT ported yet: the gold leaves falling from its crown, swirling about it
 * as it rises and bursting from it as it dies (the leaf particles, left for
 * now); the killMallornEnt achievement (D7).
 */
public class LOTRMallornEntEntity extends LOTREntEntity implements LOTRBoss {

    public static final float BOSS_SCALE = 1.5f;
    public static final int SPAWN_TIME = 150;
    public static final int MAX_LEAF_HEALINGS = 5;

    private static final EntityDataAccessor<Integer> DATA_SPAWN_TICK =
            SynchedEntityData.defineId(LOTRMallornEntEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_WEAPON_SHIELD =
            SynchedEntityData.defineId(LOTRMallornEntEntity.class, EntityDataSerializers.BOOLEAN);

    /** handleHealthUpdate 20: the landing's ring of splinters. */
    private static final byte EVENT_LANDING = 20;

    public LeafHealInfo[] leafHealings;
    private @Nullable Goal meleeAttackAI;
    private @Nullable Goal rangedAttackAI;

    public LOTRMallornEntEntity(EntityType<? extends LOTRMallornEntEntity> type, Level level) {
        super(type, level);
        resetLeafHealings();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTREntEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 300.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 8.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SPAWN_TICK, 0);
        builder.define(DATA_WEAPON_SHIELD, false);
    }

    /** The Ent's tasks cleared: no sapling healing; melee or leaf bombs by attack mode. */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new LOTRBossJumpAttackGoal(this, 1.5, 0.02f));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0f, 0.02f));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Mob.class, 10.0f, 0.02f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    private Goal meleeAttackAI() {
        if (this.meleeAttackAI == null) {
            this.meleeAttackAI = new LOTRAttackOnCollideGoal(this, 2.0, false);
        }
        return this.meleeAttackAI;
    }

    private Goal rangedAttackAI() {
        if (this.rangedAttackAI == null) {
            this.rangedAttackAI = new LOTRRangedAttackGoal(this, 1.5, 30, 50, 24.0f);
        }
        return this.rangedAttackAI;
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        this.goalSelector.removeGoal(meleeAttackAI());
        this.goalSelector.removeGoal(rangedAttackAI());
        this.goalSelector.addGoal(1, mode == AttackMode.RANGED ? rangedAttackAI() : meleeAttackAI());
    }

    @Override
    public double getMeleeRange() {
        return 12.0;
    }

    @Override
    public boolean shouldBurningPanic() {
        return false;
    }

    public int getEntSpawnTick() {
        return this.entityData.get(DATA_SPAWN_TICK);
    }

    public void setEntSpawnTick(int i) {
        this.entityData.set(DATA_SPAWN_TICK, i);
    }

    public boolean hasWeaponShield() {
        return this.entityData.get(DATA_WEAPON_SHIELD);
    }

    public void setHasWeaponShield(boolean flag) {
        this.entityData.set(DATA_WEAPON_SHIELD, flag);
    }

    public boolean isWeaponShieldActive() {
        return hasWeaponShield() && !isOnFire();
    }

    /** getSpawningOffset: how far below ground it still is as it rises, in blocks. */
    public float getSpawningOffset(float partialTick) {
        float f = Math.min((getEntSpawnTick() + partialTick) / SPAWN_TIME, 1.0f);
        return (1.0f - f) * -5.0f;
    }

    /** One to eight, falling away with its health; none once the shield is up. */
    @Override
    public int getExtraHeadBranches() {
        if (hasWeaponShield()) {
            return 0;
        }
        int max = 8;
        int branches = Mth.ceil(getHealth() / getMaxHealth() * max);
        return Mth.clamp(branches, 1, max);
    }

    @Override
    public float getBaseChanceModifier() {
        return this.bossInfo.getHealthChanceModifier();
    }

    @Override
    public void onJumpAttackFall() {
        level().broadcastEntityEvent(this, EVENT_LANDING);
        playSound(LOTRSounds.TROLL_ROCK_SMASH, 1.5f, 0.75f);
    }

    @Override
    public boolean doTreeDamageCalculation() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (getEntSpawnTick() < SPAWN_TIME) {
            return false;
        }
        boolean byPlayerSide = source.getEntity() instanceof Player
                || source.getEntity() instanceof LOTRNPCEntity npc
                && npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() != null;
        if (!byPlayerSide && damage > 1.0f) {
            damage = 1.0f;
        }
        if (!isTreeEffectiveDamage(source)) {
            damage *= 0.5f;
        }
        if (isWeaponShieldActive() && !source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE)) {
            damage = 0.0f;
        }
        return super.hurtServer(level, source, damage);
    }

    /** damageEntity: brought to nothing the first time, the shield rises and it is whole again. */
    @Override
    protected void actuallyHurt(ServerLevel level, DamageSource source, float damage) {
        super.actuallyHurt(level, source, damage);
        if (!hasWeaponShield() && getHealth() <= 0.0f) {
            setHasWeaponShield(true);
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(300.0);
            setHealth(getMaxHealth());
            sendEntBossSpeech("shield");
        }
    }

    @Override
    protected boolean isImmobile() {
        return getEntSpawnTick() < SPAWN_TIME || super.isImmobile();
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        LOTRMallornLeafBombEntity leaves = new LOTRMallornLeafBombEntity(level(), this, target);
        leaves.leavesDamage = 6.0f;
        level().addFreshEntity(leaves);
        playSound(LOTRSounds.ENT_MALLORN_LEAF_ATTACK, getSoundVolume(), getVoicePitch());
        swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (getEntSpawnTick() < SPAWN_TIME) {
            if (level().isClientSide()) {
                for (int l = 0; l < 16; ++l) {
                    level().addParticle(LOTRParticles.MALLORN_ENT_SPAWN,
                            getX() + this.random.nextGaussian() * getBbWidth() * 0.5,
                            getY() + this.random.nextDouble() * getBbHeight() + getSpawningOffset(0.0f),
                            getZ() + this.random.nextGaussian() * getBbWidth() * 0.5, 0.0, 0.0, 0.0);
                }
            } else {
                setEntSpawnTick(getEntSpawnTick() + 1);
                if (getEntSpawnTick() == SPAWN_TIME) {
                    this.bossInfo.doJumpAttack(1.5);
                }
            }
        }
        if (!(level() instanceof ServerLevel level)) {
            tickLeafHealingsClient();
            return;
        }
        if (this.random.nextFloat() < getBaseChanceModifier() * 0.05f) {
            this.bossInfo.doTargetedJumpAttack(1.5);
        }
        if (getHealth() < getMaxHealth()) {
            for (LeafHealInfo healing : this.leafHealings) {
                if (healing.active || this.random.nextFloat() >= getBaseChanceModifier() * 0.02f) {
                    continue;
                }
                int range = 16;
                BlockPos pos = blockPosition();
                for (int l = 0; l < 30; ++l) {
                    BlockPos leaf = pos.offset(Mth.randomBetweenInclusive(this.random, -range, range),
                            Mth.randomBetweenInclusive(this.random, -range, range),
                            Mth.randomBetweenInclusive(this.random, -range, range));
                    if (level.getBlockState(leaf).getBlock() instanceof LeavesBlock) {
                        healing.active = true;
                        healing.leaf = leaf;
                        healing.healTime = 15 + this.random.nextInt(15);
                        sendHealInfoToWatchers(healing);
                        break;
                    }
                }
            }
        }
        for (LeafHealInfo healing : this.leafHealings) {
            if (!healing.active) {
                continue;
            }
            if (level.getBlockState(healing.leaf).getBlock() instanceof LeavesBlock) {
                if (this.tickCount % 20 != 0) {
                    continue;
                }
                heal(2.0f);
                --healing.healTime;
                if (getHealth() < getMaxHealth() && healing.healTime > 0) {
                    continue;
                }
            }
            healing.active = false;
            sendHealInfoToWatchers(healing);
        }
        if (getHealth() < getMaxHealth() && this.random.nextInt(50) == 0) {
            trySummonEnts(level);
        }
    }

    /** The leaves feeding it, drawn from each block to its crown. */
    private void tickLeafHealingsClient() {
        for (LeafHealInfo healing : this.leafHealings) {
            if (!healing.active) {
                continue;
            }
            BlockState state = level().getBlockState(healing.leaf);
            if (state.getBlock() instanceof LeavesBlock) {
                double d = healing.leaf.getX() + 0.5;
                double d1 = healing.leaf.getY() + 0.5;
                double d2 = healing.leaf.getZ() + 0.5;
                level().addParticle(new BlockParticleOption(LOTRParticles.MALLORN_ENT_HEAL, state), d, d1, d2,
                        (getX() - d) / 25.0, (getY() + getBbHeight() * 0.9 - d1) / 25.0, (getZ() - d2) / 25.0);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_LANDING) {
            for (int i = 0; i < 360; i += 2) {
                float angle = i * Mth.DEG_TO_RAD;
                double distance = 2.0;
                double d = distance * Mth.sin(angle);
                double d1 = distance * Mth.cos(angle);
                level().addParticle(LOTRParticles.MALLORN_ENT_JUMP_SMASH, getX() + d, getBoundingBox().minY + 0.1,
                        getZ() + d1, d * 0.2, 0.2, d1 * 0.2);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    /** trySummonEnts: now and then, fewer than six trees being near, an Ent or a Huorn within 12 blocks. */
    public void trySummonEnts(ServerLevel level) {
        float f = getBaseChanceModifier() * 0.5f;
        List<LOTRTreeEntity> nearbyTrees = level.getEntitiesOfClass(LOTRTreeEntity.class,
                getBoundingBox().inflate(24.0, 8.0, 24.0));
        int maxNearbyTrees = 6;
        f *= (float) (maxNearbyTrees - nearbyTrees.size()) / maxNearbyTrees;
        if (this.random.nextFloat() >= f) {
            return;
        }
        LOTRTreeEntity tree = this.random.nextInt(3) == 0
                ? LOTREntities.HUORN.create(level, EntitySpawnReason.MOB_SUMMONED)
                : LOTREntities.ENT.create(level, EntitySpawnReason.MOB_SUMMONED);
        if (tree == null) {
            return;
        }
        int range = 12;
        BlockPos pos = blockPosition();
        for (int l = 0; l < 30; ++l) {
            BlockPos at = pos.offset(Mth.randomBetweenInclusive(this.random, -range, range),
                    Mth.randomBetweenInclusive(this.random, -range, range),
                    Mth.randomBetweenInclusive(this.random, -range, range));
            if (!level.getBlockState(at.below()).isRedstoneConductor(level, at.below())
                    || level.getBlockState(at).isRedstoneConductor(level, at)
                    || level.getBlockState(at.above()).isRedstoneConductor(level, at.above())) {
                continue;
            }
            tree.snapTo(at.getX() + 0.5, at.getY(), at.getZ() + 0.5, this.random.nextFloat() * 360.0f, 0.0f);
            tree.liftSpawnRestrictions = true;
            if (!tree.checkSpawnRules(level, EntitySpawnReason.MOB_SUMMONED) || !tree.checkSpawnObstruction(level)) {
                continue;
            }
            tree.liftSpawnRestrictions = false;
            tree.finalizeSpawn(level, level.getCurrentDifficultyAt(at), EntitySpawnReason.MOB_SUMMONED, null);
            level.addFreshEntity(tree);
            LOTRMallornEntSummonPayload payload = new LOTRMallornEntSummonPayload(getId(), tree.getId());
            for (ServerPlayer player : level.players()) {
                if (player.distanceToSqr(tree) <= 64.0 * 64.0) {
                    ServerPlayNetworking.send(player, payload);
                }
            }
            level.playSound(null, tree.getX(), tree.getY(), tree.getZ(), LOTRSounds.ENT_MALLORN_SUMMON_ENT,
                    SoundSource.NEUTRAL, getSoundVolume(), getVoicePitch());
            break;
        }
    }

    /** Its words to every player within 64 blocks. */
    public void sendEntBossSpeech(String speechBank) {
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        for (ServerPlayer player : level.players()) {
            if (distanceToSqr(player) <= 64.0 * 64.0) {
                sendSpeechBank(player, "ent/mallornEnt/" + speechBank);
            }
        }
    }

    private void sendHealInfoToWatchers(LeafHealInfo healing) {
        LOTRMallornEntHealPayload payload = healing.toPayload(getId());
        for (ServerPlayer player : PlayerLookup.tracking(this)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        for (LeafHealInfo healing : this.leafHealings) {
            ServerPlayNetworking.send(player, healing.toPayload(getId()));
        }
    }

    public void receiveClientHealing(LOTRMallornEntHealPayload payload) {
        if (payload.slot() < 0 || payload.slot() >= this.leafHealings.length) {
            return;
        }
        LeafHealInfo healing = new LeafHealInfo(payload.slot());
        healing.active = payload.active();
        healing.leaf = payload.leaf();
        healing.healTime = payload.healTime();
        this.leafHealings[healing.slot] = healing;
    }

    public void resetLeafHealings() {
        this.leafHealings = new LeafHealInfo[MAX_LEAF_HEALINGS];
        for (int i = 0; i < MAX_LEAF_HEALINGS; ++i) {
            this.leafHealings[i] = new LeafHealInfo(i);
        }
    }

    /** onDeath: every fire within 12 blocks goes out. */
    @Override
    public void die(DamageSource source) {
        if (level() instanceof ServerLevel level) {
            int fireRange = 12;
            for (BlockPos pos : BlockPos.betweenClosed(blockPosition().offset(-fireRange, -fireRange, -fireRange),
                    blockPosition().offset(fireRange, fireRange, fireRange))) {
                if (level.getBlockState(pos).getBlock() instanceof BaseFireBlock) {
                    level.removeBlock(pos, false);
                }
            }
        }
        super.die(source);
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int dropped;
        for (int wood = Mth.randomBetweenInclusive(this.random, 20, 30 + looting * 20); wood > 0; wood -= dropped) {
            dropped = Math.min(20, wood);
            spawnAtLocation(level, new ItemStack(LOTRBuildingBlocks.MALLORN_LOG, dropped), 0.0f);
        }
        for (int sticks = Mth.randomBetweenInclusive(this.random, 30, 40 + looting * 20); sticks > 0; sticks -= dropped) {
            dropped = Math.min(20, sticks);
            spawnAtLocation(level, new ItemStack(LOTRMaterialItems.MALLORN_STICK, dropped), 0.0f);
        }
        spawnAtLocation(level, new ItemStack(LOTRItems.MALLORN_ENT_TROPHY), 0.0f);
        float maceChance = 0.3f + looting * 0.1f;
        if (this.random.nextFloat() < maceChance) {
            spawnAtLocation(level, LOTRCombatItems.CHARRED_MALLORN_MACE);
        }
    }

    @Override
    public float getAlignmentBonus() {
        return 50.0f;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 100;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return null;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        ValueOutput.ValueOutputList list = output.childrenList("LeafHealings");
        for (LeafHealInfo healing : this.leafHealings) {
            healing.save(list.addChild());
        }
        output.putInt("EntSpawnTick", getEntSpawnTick());
        output.putBoolean("EntWeaponShield", hasWeaponShield());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        resetLeafHealings();
        for (ValueInput tag : input.childrenListOrEmpty("LeafHealings")) {
            int slot = tag.getByteOr("Slot", (byte) -1);
            if (slot >= 0 && slot < this.leafHealings.length) {
                this.leafHealings[slot].load(tag);
            }
        }
        setEntSpawnTick(input.getIntOr("EntSpawnTick", 0));
        setHasWeaponShield(input.getBooleanOr("EntWeaponShield", false));
    }

    /** LeafHealInfo: one leaf block feeding the Ent, and the seconds it has left. */
    public static class LeafHealInfo {
        public final int slot;
        public boolean active;
        public BlockPos leaf = BlockPos.ZERO;
        public int healTime;

        public LeafHealInfo(int slot) {
            this.slot = slot;
        }

        LOTRMallornEntHealPayload toPayload(int entityId) {
            return new LOTRMallornEntHealPayload(entityId, this.slot, this.active, this.leaf, this.healTime);
        }

        /** healTime was written as "Time" but read as "healTime", so it came back as 0; so it does here. */
        void save(ValueOutput output) {
            output.putByte("Slot", (byte) this.slot);
            output.putBoolean("Active", this.active);
            output.putInt("X", this.leaf.getX());
            output.putInt("Y", this.leaf.getY());
            output.putInt("Z", this.leaf.getZ());
            output.putShort("Time", (short) this.healTime);
        }

        void load(ValueInput input) {
            this.active = input.getBooleanOr("Active", false);
            this.leaf = new BlockPos(input.getIntOr("X", 0), input.getIntOr("Y", 0), input.getIntOr("Z", 0));
            this.healTime = input.getShortOr("healTime", (short) 0);
        }
    }
}
