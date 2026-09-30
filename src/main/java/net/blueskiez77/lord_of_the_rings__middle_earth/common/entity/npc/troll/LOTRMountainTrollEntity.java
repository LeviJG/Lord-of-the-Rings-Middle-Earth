package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrownRockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMountainTroll (the Hill-troll): a troll 1.6 times as big, 70 strong, hitting for
 * 7, with no name of its own and no speech, who will not be tickled. Within
 * twelve blocks it closes to fight hand to hand; further off it hurls rocks
 * (its thrown rock damage, 5 by default) with its arms raised. Turned to stone
 * by the sun it crumbles away rather than standing as a statue. It may leave
 * a part of a troll totem -- one in fifteen, better with looting -- unless it
 * came out of a totem's own rock.
 *
 * <p>NOT ported yet: the killMountainTroll achievement (D7).
 */
public class LOTRMountainTrollEntity extends LOTRTrollEntity {

    private static final EntityDataAccessor<Boolean> DATA_THROWING =
            SynchedEntityData.defineId(LOTRMountainTrollEntity.class, EntityDataSerializers.BOOLEAN);

    private @Nullable Goal meleeAttackAI;
    private @Nullable Goal rangedAttackAI;
    public boolean canDropTrollTotem = true;

    public LOTRMountainTrollEntity(EntityType<? extends LOTRMountainTrollEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRTrollEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 70.0)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 7.0)
                .add(LOTRNPCAttributes.THROWN_ROCK_DAMAGE);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_THROWING, false);
    }

    @Override
    protected Goal getTrollAttackAI() {
        return meleeAttackAI();
    }

    private Goal meleeAttackAI() {
        if (this.meleeAttackAI == null) {
            this.meleeAttackAI = new LOTRAttackOnCollideGoal(this, 1.8, false);
        }
        return this.meleeAttackAI;
    }

    private Goal rangedAttackAI() {
        if (this.rangedAttackAI == null) {
            this.rangedAttackAI = getTrollRangedAttackAI();
        }
        return this.rangedAttackAI;
    }

    protected Goal getTrollRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.2, 30, 60, 24.0f);
    }

    @Override
    public float getTrollScale() {
        return 1.6f;
    }

    @Override
    public boolean hasTrollName() {
        return false;
    }

    @Override
    public double getMeleeRange() {
        return 12.0;
    }

    public boolean isThrowingRocks() {
        return this.entityData.get(DATA_THROWING);
    }

    public void setThrowingRocks(boolean flag) {
        this.entityData.set(DATA_THROWING, flag);
    }

    @Override
    public boolean isThrowing() {
        return isThrowingRocks();
    }

    public LOTRMountainTrollEntity setCanDropTrollTotem(boolean flag) {
        this.canDropTrollTotem = flag;
        return this;
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        Goal melee = meleeAttackAI();
        Goal ranged = rangedAttackAI();
        this.goalSelector.removeGoal(melee);
        this.goalSelector.removeGoal(ranged);
        if (mode == AttackMode.MELEE) {
            this.goalSelector.addGoal(3, melee);
        } else if (mode == AttackMode.RANGED) {
            this.goalSelector.addGoal(3, ranged);
        }
        setThrowingRocks(mode == AttackMode.RANGED);
    }

    public LOTRThrownRockEntity getThrownRock() {
        LOTRThrownRockEntity rock = new LOTRThrownRockEntity(level(), this);
        rock.setDamage((float) getAttributeValue(LOTRNPCAttributes.THROWN_ROCK_DAMAGE));
        return rock;
    }

    /** A rock, thrown as an arrow at half again the shot's strength, lofted six tenths more. */
    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        LOTRThrownRockEntity rock = getThrownRock();
        aimLikeArrow(rock, target, power * 1.5f, 0.5f);
        rock.setDeltaMovement(rock.getDeltaMovement().add(0.0, 0.6, 0.0));
        level().addFreshEntity(rock);
        playSound(LOTRSounds.TROLL_SAY, getSoundVolume(), getVoicePitch() * 0.75f);
        swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public boolean canTrollBeTickled(Player player) {
        return false;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return null;
    }

    @Override
    public float getAlignmentBonus() {
        return 4.0f;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 7 + this.random.nextInt(6);
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        if (this.canDropTrollTotem) {
            dropTrollTotemPart(level, looting);
        }
    }

    protected void dropTrollTotemPart(ServerLevel level, int looting) {
        int totemChance = 15 - looting * 3;
        if (this.random.nextInt(Math.max(totemChance, 1)) == 0) {
            Item[] parts = {LOTRUtilityBlocks.TROLL_TOTEM_HEAD.asItem(), LOTRUtilityBlocks.TROLL_TOTEM_BODY.asItem(),
                    LOTRUtilityBlocks.TROLL_TOTEM_BASE.asItem()};
            spawnAtLocation(level, parts[this.random.nextInt(3)]);
        }
    }

    /** onTrollDeathBySun: it crumbles away to nothing. */
    @Override
    public void onTrollDeathBySun() {
        playSound(LOTRSounds.TROLL_TRANSFORM, getSoundVolume(), getVoicePitch());
        level().broadcastEntityEvent(this, (byte) 15);
        discard();
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 15) {
            for (int l = 0; l < 64; ++l) {
                level().addParticle(LOTRParticles.LARGE_STONE, getX() + this.random.nextGaussian() * getBbWidth() * 0.5,
                        getY() + this.random.nextDouble() * getBbHeight(),
                        getZ() + this.random.nextGaussian() * getBbWidth() * 0.5, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("CanDropTrollTotem", this.canDropTrollTotem);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.canDropTrollTotem = input.getBooleanOr("CanDropTrollTotem", this.canDropTrollTotem);
    }
}
