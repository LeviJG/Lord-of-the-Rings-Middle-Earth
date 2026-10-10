package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTROrcTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
 * LOTREntityMirkTroll: a troll of Dol Guldur, a fifth again as big, 70 strong
 * with armour 12, hitting for 6 with a poisoning blow (the difficulty's number
 * times three, less one, in seconds). It does not fear the sun, charges at
 * 2.0, looks for Dol Guldur's enemies as the orcs do, has no name of its own,
 * will not be tickled or speak, and, slain by a player, sometimes leaves orc
 * steel.
 */
public class LOTRMirkTrollEntity extends LOTRTrollEntity {

    public LOTRMirkTrollEntity(EntityType<? extends LOTRMirkTrollEntity> type, Level level) {
        super(type, level);
        this.trollImmuneToSun = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRTrollEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 70.0)
                .add(Attributes.ARMOR, 12.0)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 6.0);
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
        return 1.2f;
    }

    @Override
    public boolean hasTrollName() {
        return false;
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            int duration = level.getDifficulty().getId() * 3 - 1;
            if (target instanceof LivingEntity living && duration > 0) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, duration * 20, 0), this);
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
        return LOTRFaction.DOL_GULDUR;
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
        return 4 + this.random.nextInt(7);
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_MIRK_TROLL;
    }
}
