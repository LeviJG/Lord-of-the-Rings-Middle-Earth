package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRAttackRules;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTROrcTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityOlogHai: Sauron's troll, a quarter again as big, 80 strong with
 * armour 15, hitting for 7 -- and every blow, thrown twice as far as a
 * troll's, a hammer-stroke that knocks down
 * all about the one it strikes, within four blocks, the nearer the harder. It
 * does not fear the sun, charges at 2.0, looks for Mordor's enemies as the
 * orcs do, has no name of its own, will not be tickled or speak, and, slain
 * by a player, sometimes leaves orc steel.
 *
 * <p>NOT ported yet: the killOlogHai achievement (D7).
 */
public class LOTROlogHaiEntity extends LOTRTrollEntity {

    public LOTROlogHaiEntity(EntityType<? extends LOTROlogHaiEntity> type, Level level) {
        super(type, level);
        this.trollImmuneToSun = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRTrollEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 80.0)
                .add(Attributes.ARMOR, 15.0)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 7.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(2, new LOTRAttackOnCollideGoal(this, 2.0, false));
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 12.0f, 0.02f));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mob.class, 12.0f, 0.01f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        addTargetTasks(true, LOTROrcTargetGoal::new);
    }

    @Override
    public float getTrollScale() {
        return 1.25f;
    }

    @Override
    public boolean hasTrollName() {
        return false;
    }

    /**
     * attackEntityAsMob: the hammer's sound over the one struck, and a blow to
     * everything else it may attack within four blocks of it -- up to the full
     * damage at a block's distance, less by a quarter a block further -- each
     * thrown back and up.
     */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            float attackDamage = (float) getAttributeValue(LOTRNPCAttributes.NPC_ATTACK_DAMAGE);
            float knockbackModifier = 0.25f * attackDamage;
            float targetYaw = getYRot() * Mth.DEG_TO_RAD;
            target.push(-Mth.sin(targetYaw) * knockbackModifier * 0.5f, 0.0, Mth.cos(targetYaw) * knockbackModifier * 0.5f);
            level.playSound(null, target.getX(), target.getY(), target.getZ(), LOTRSounds.TROLL_OLOG_HAI_HAMMER,
                    getSoundSource(), 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(4.0));
            for (LivingEntity hitEntity : entities) {
                if (hitEntity == this || hitEntity == target || !LOTRAttackRules.canNPCAttackEntity(this, hitEntity, false)) {
                    continue;
                }
                float strength = Math.min(4.0f - target.distanceTo(hitEntity) + 1.0f, 4.0f);
                if (!hitEntity.hurtServer(level, damageSources().mobAttack(this), strength / 4.0f * attackDamage)) {
                    continue;
                }
                float knockback = Math.max(strength * 0.25f, 0.75f);
                float yaw = getYRot() * Mth.DEG_TO_RAD;
                hitEntity.push(-Mth.sin(yaw) * knockback * 0.5f, 0.2 + 0.12 * knockback, Mth.cos(yaw) * knockback * 0.5f);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canTrollBeTickled(Player player) {
        return false;
    }

    /** Slain by a player, now and then some orc steel -- more often with looting. */
    @Override
    protected void dropTrollItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (killedByPlayer) {
            int rareDropChance = Math.max(8 - looting, 1);
            if (this.random.nextInt(rareDropChance) == 0) {
                int drops = 1 + this.random.nextInt(2) + this.random.nextInt(looting + 1);
                for (int j = 0; j < drops; ++j) {
                    spawnAtLocation(level, LOTRMaterialItems.ORC_STEEL_INGOT);
                }
            }
        }
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.MORDOR;
    }

    @Override
    public float getAlignmentBonus() {
        return 4.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return null;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 5 + this.random.nextInt(8);
    }
}
