package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrownRockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBossJumpAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBoss;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * LOTREntityMountainTrollChieftain: the Hill-troll Chieftain, called up by a
 * completed troll totem -- a two-headed Hill-troll of twice a troll's size,
 * armour 12, hitting for 8 and throwing rocks for 8.
 *
 * <p>It rises out of the ground over five seconds, untouchable and unmoving,
 * and leaps as it comes clear. It wears three coats of armour: brought to
 * nothing, it sheds one, doubles its health and is whole again -- and with the
 * last gone it is half again as quick, and smokes. Anything but a player (or
 * their hired units) does it at most one point of harm. The more it is hurt,
 * and the less armour it has left, the more it leaps at those about it and at
 * the last player to hurt it when far or high above, flings rocks that bring
 * other trolls (while fewer than five are near), and drains nearby trolls to
 * heal itself. Caught by the sun, it thrashes for ten seconds as it turns to
 * stone and crumbles.
 *
 * <p>It leaves a great hoard: several helpings of a troll's leavings, troll
 * bones, fifty to a hundred silver coins and more with looting, a troll's
 * hoard, its trophy, and sometimes Gondolin's sword and armour. It is worth
 * 50 alignment and 100 experience, and drops no totem.
 */
public class LOTRMountainTrollChieftainEntity extends LOTRMountainTrollEntity implements LOTRBoss {

    private static final EntityDataAccessor<Integer> DATA_SPAWN_TICK =
            SynchedEntityData.defineId(LOTRMountainTrollChieftainEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_HEALING_ID =
            SynchedEntityData.defineId(LOTRMountainTrollChieftainEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_ARMOR_LEVEL =
            SynchedEntityData.defineId(LOTRMountainTrollChieftainEntity.class, EntityDataSerializers.BYTE);

    /** handleHealthUpdate 20: the landing's ring of stone; 21: armour breaking away. */
    private static final byte EVENT_LANDING = 20;
    private static final byte EVENT_ARMOR = 21;

    public int trollDeathTick;
    public int healAmount;

    public LOTRMountainTrollChieftainEntity(EntityType<? extends LOTRMountainTrollChieftainEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRMountainTrollEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ARMOR, 12.0)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 8.0)
                .add(LOTRNPCAttributes.THROWN_ROCK_DAMAGE, 8.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SPAWN_TICK, 0);
        builder.define(DATA_HEALING_ID, -1);
        builder.define(DATA_ARMOR_LEVEL, (byte) 2);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new LOTRBossJumpAttackGoal(this, 1.5, 0.03f));
    }

    @Override
    protected Goal getTrollRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.2, 20, 50, 24.0f);
    }

    @Override
    public float getTrollScale() {
        return 2.0f;
    }

    @Override
    public boolean hasTwoHeads() {
        return true;
    }

    public int getTrollSpawnTick() {
        return this.entityData.get(DATA_SPAWN_TICK);
    }

    public void setTrollSpawnTick(int i) {
        this.entityData.set(DATA_SPAWN_TICK, i);
    }

    public int getHealingEntityID() {
        return this.entityData.get(DATA_HEALING_ID);
    }

    public void setHealingEntityID(int i) {
        this.entityData.set(DATA_HEALING_ID, i);
    }

    public int getTrollArmorLevel() {
        return this.entityData.get(DATA_ARMOR_LEVEL);
    }

    public void setTrollArmorLevel(int i) {
        this.entityData.set(DATA_ARMOR_LEVEL, (byte) i);
    }

    /** getSpawningOffset: how far below ground it still is as it rises, in blocks. */
    public float getSpawningOffset(float partialTick) {
        float f = Math.min((getTrollSpawnTick() + partialTick) / 100.0f, 1.0f);
        return (1.0f - f) * -5.0f;
    }

    public float getArmorLevelChanceModifier() {
        return Math.max(3 - getTrollArmorLevel(), 1);
    }

    @Override
    public float getBaseChanceModifier() {
        return this.bossInfo.getHealthChanceModifier() * getArmorLevelChanceModifier();
    }

    @Override
    public void onJumpAttackFall() {
        level().broadcastEntityEvent(this, EVENT_LANDING);
        playSound(LOTRSounds.TROLL_ROCK_SMASH, 1.5f, 0.75f);
    }

    /** attackEntityFrom: untouchable while rising or dying; anything but a player's side does at most 1. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (getTrollSpawnTick() < 100 || this.trollDeathTick > 0) {
            return false;
        }
        boolean byPlayerSide = source.getEntity() instanceof Player
                || source.getEntity() instanceof net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity npc
                && npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() != null;
        if (!byPlayerSide && damage > 1.0f) {
            damage = 1.0f;
        }
        return super.hurtServer(level, source, damage);
    }

    /** damageEntity: brought to nothing while it has armour left, it sheds a coat and is whole again. */
    @Override
    protected void actuallyHurt(ServerLevel level, DamageSource source, float damage) {
        super.actuallyHurt(level, source, damage);
        if (getTrollArmorLevel() > 0 && getHealth() <= 0.0f) {
            setTrollArmorLevel(getTrollArmorLevel() - 1);
            if (getTrollArmorLevel() == 0) {
                AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
                speed.setBaseValue(speed.getValue() * 1.5);
            }
            AttributeInstance maxHealth = getAttribute(Attributes.MAX_HEALTH);
            maxHealth.setBaseValue(maxHealth.getValue() * 2.0);
            setHealth(getMaxHealth());
            level.broadcastEntityEvent(this, EVENT_ARMOR);
        }
    }

    /** isMovementBlocked: while rising or dying. */
    @Override
    protected boolean isImmobile() {
        return getTrollSpawnTick() < 100 || this.trollDeathTick > 0 || super.isImmobile();
    }

    /** getThrownRock: now and then a rock that brings a troll, while fewer than five are near. */
    @Override
    public LOTRThrownRockEntity getThrownRock() {
        LOTRThrownRockEntity rock = super.getThrownRock();
        float f = getBaseChanceModifier() * 0.4f;
        int maxNearbyTrolls = 5;
        List<LOTRTrollEntity> nearbyTrolls = level().getEntitiesOfClass(LOTRTrollEntity.class,
                getBoundingBox().inflate(24.0, 8.0, 24.0));
        f *= (float) (maxNearbyTrolls - nearbyTrolls.size()) / maxNearbyTrolls;
        if (this.random.nextFloat() < f) {
            rock.setSpawnsTroll(true);
        }
        return rock;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (getTrollSpawnTick() < 100) {
            if (level().isClientSide()) {
                for (int l = 0; l < 32; ++l) {
                    level().addParticle(LOTRParticles.MTC_SPAWN, getX() + this.random.nextGaussian() * getBbWidth() * 0.5,
                            getY() + this.random.nextDouble() * getBbHeight() + getSpawningOffset(0.0f),
                            getZ() + this.random.nextGaussian() * getBbWidth() * 0.5, 0.0, 0.0, 0.0);
                }
            } else {
                setTrollSpawnTick(getTrollSpawnTick() + 1);
                if (getTrollSpawnTick() == 100) {
                    this.bossInfo.doJumpAttack(1.5);
                }
            }
        }
        if (level().isClientSide() && getTrollArmorLevel() == 0) {
            for (int i = 0; i < 4; ++i) {
                level().addParticle(ParticleTypes.LARGE_SMOKE, getX() + (this.random.nextDouble() - 0.5) * getBbWidth(),
                        getY() + this.random.nextDouble() * getBbHeight(),
                        getZ() + (this.random.nextDouble() - 0.5) * getBbWidth(), 0.0, 0.0, 0.0);
            }
        }
        if (!(level() instanceof ServerLevel level)) {
            tickHealingClient();
            return;
        }
        // Turning to stone: ten seconds of thrashing.
        if (getTrollBurnTime() >= 0 || this.trollDeathTick > 0) {
            if (this.trollDeathTick == 0) {
                playSound(LOTRSounds.TROLL_TRANSFORM, getSoundVolume(), getVoicePitch());
            }
            if (this.trollDeathTick % 5 == 0) {
                level.broadcastEntityEvent(this, (byte) 15);
            }
            if (this.trollDeathTick % 10 == 0) {
                playSound(LOTRSounds.TROLL_SAY, getSoundVolume() * 2.0f, 0.8f);
            }
            ++this.trollDeathTick;
            setYRot(getYRot() + 60.0f * (this.random.nextFloat() - 0.5f));
            setYHeadRot(getYHeadRot() + 60.0f * (this.random.nextFloat() - 0.5f));
            setXRot(getXRot() + 60.0f * (this.random.nextFloat() - 0.5f));
            if (this.trollDeathTick >= 200) {
                discard();
            }
        }
        // Drawing on a nearby troll to heal.
        if (getHealth() < getMaxHealth() && this.random.nextFloat() < getBaseChanceModifier() * 0.02f) {
            List<LOTRTrollEntity> nearbyTrolls = level.getEntitiesOfClass(LOTRTrollEntity.class,
                    getBoundingBox().inflate(24.0, 8.0, 24.0));
            if (!nearbyTrolls.isEmpty()) {
                LOTRTrollEntity troll = nearbyTrolls.get(this.random.nextInt(nearbyTrolls.size()));
                if (!(troll instanceof LOTRMountainTrollChieftainEntity) && troll.isAlive()) {
                    setHealingEntityID(troll.getId());
                    this.healAmount = 8 + this.random.nextInt(3);
                }
            }
        }
        if (getHealingEntityID() != -1) {
            Entity entity = level.getEntity(getHealingEntityID());
            if (entity instanceof LOTRTrollEntity && entity.isAlive()) {
                if (this.tickCount % 20 == 0) {
                    heal(3.0f);
                    entity.hurtServer(level, damageSources().generic(), 3.0f);
                    --this.healAmount;
                    if (!entity.isAlive() || getHealth() >= getMaxHealth() || this.healAmount <= 0) {
                        setHealingEntityID(-1);
                    }
                }
            } else {
                setHealingEntityID(-1);
            }
        }
        // Now and then, hurt, it throws a troll-bringing rock straight up.
        if (getHealth() < getMaxHealth() && this.random.nextInt(50) == 0 && !isThrowingRocks()) {
            LOTRThrownRockEntity rock = getThrownRock();
            if (rock.getSpawnsTroll()) {
                rock.snapTo(getX(), getY() + getBbHeight(), getZ(), 0.0f, 0.0f);
                rock.setDeltaMovement(0.0, 1.5, 0.0);
                level.addFreshEntity(rock);
                swing(InteractionHand.MAIN_HAND);
            }
        }
        if (this.random.nextFloat() < getBaseChanceModifier() * 0.05f) {
            this.bossInfo.doTargetedJumpAttack(1.5);
        }
    }

    /** The red wisps from the troll it drains. */
    private void tickHealingClient() {
        if (getHealingEntityID() == -1) {
            return;
        }
        Entity entity = level().getEntity(getHealingEntityID());
        if (entity instanceof LOTRTrollEntity && entity.isAlive()) {
            double d = entity.getX();
            double d1 = entity.getY() + entity.getBbHeight() / 2.0;
            double d2 = entity.getZ();
            level().addParticle(LOTRParticles.MTC_HEAL, d, d1, d2, (getX() - d) / 30.0,
                    (getY() + getBbHeight() / 2.0 - d1) / 30.0, (getZ() - d2) / 30.0);
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
                level().addParticle(LOTRParticles.LARGE_STONE, getX() + d, getBoundingBox().minY + 0.1, getZ() + d1,
                        d * 0.2, 0.2, d1 * 0.2);
            }
        } else if (id == EVENT_ARMOR) {
            for (int i = 0; i < 64; ++i) {
                level().addParticle(LOTRParticles.MTC_ARMOR, getX() + (this.random.nextDouble() - 0.5) * getBbWidth(),
                        getY() + this.random.nextDouble() * getBbHeight(),
                        getZ() + (this.random.nextDouble() - 0.5) * getBbWidth(), 0.0, 0.0, 0.0);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void dropTrollTotemPart(ServerLevel level, int looting) {
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int drops = 3 + this.random.nextInt(4) + this.random.nextInt(looting * 2 + 1);
        for (int j = 0; j < drops; ++j) {
            dropTrollItems(level, killedByPlayer, looting);
        }
        int bones = Mth.randomBetweenInclusive(this.random, 4, 8) + this.random.nextInt(looting * 3 + 1);
        for (int j = 0; j < bones; ++j) {
            spawnAtLocation(level, LOTRMaterialItems.TROLL_BONE);
        }
        int dropped;
        for (int coins = Mth.randomBetweenInclusive(this.random, 50, 100 + looting * 100); coins > 0; coins -= dropped) {
            dropped = Math.min(20, coins);
            spawnAtLocation(level, new ItemStack(LOTRMiscItems.SILVER_COIN, dropped), 0.0f);
        }
        dropChestContents(level, LOTRChestContents.TROLL_HOARD, 5, 8 + looting * 3);
        spawnAtLocation(level, new ItemStack(LOTRItems.MOUNTAIN_TROLL_CHIEFTAIN_TROPHY), 0.0f);
        float swordChance = 0.3f + looting * 0.1f;
        if (this.random.nextFloat() < swordChance) {
            spawnAtLocation(level, LOTRCombatItems.GONDOLIN_SWORD);
        }
        float armorChance = 0.2f + looting * 0.05f;
        if (this.random.nextFloat() < armorChance) {
            spawnAtLocation(level, LOTRCombatItems.GONDOLIN_HELMET);
        }
        if (this.random.nextFloat() < armorChance) {
            spawnAtLocation(level, LOTRCombatItems.GONDOLIN_CHESTPLATE);
        }
        if (this.random.nextFloat() < armorChance) {
            spawnAtLocation(level, LOTRCombatItems.GONDOLIN_LEGGINGS);
        }
        if (this.random.nextFloat() < armorChance) {
            spawnAtLocation(level, LOTRCombatItems.GONDOLIN_BOOTS);
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
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("TrollSpawnTick", getTrollSpawnTick());
        output.putInt("TrollDeathTick", this.trollDeathTick);
        output.putInt("TrollArmorLevel", getTrollArmorLevel());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setTrollSpawnTick(input.getIntOr("TrollSpawnTick", 0));
        this.trollDeathTick = input.getIntOr("TrollDeathTick", 0);
        input.getInt("TrollArmorLevel").ifPresent(this::setTrollArmorLevel);
    }

    @Override
    public LOTRAchievement getBossKillAchievement() {
        return LOTRAchievement.KILL_MOUNTAIN_TROLL_CHIEFTAIN;
    }
}
