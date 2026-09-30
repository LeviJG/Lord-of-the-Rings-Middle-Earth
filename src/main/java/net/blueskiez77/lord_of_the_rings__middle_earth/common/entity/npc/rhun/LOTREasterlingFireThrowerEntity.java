package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rhun;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRFirePotEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityEasterlingFireThrower: an Easterling warrior who throws Rhûnic
 * fire pots from afar and falls back on a Rhûnic dagger up close -- or at any
 * range once his target is burning. He never rides out.
 */
public class LOTREasterlingFireThrowerEntity extends LOTREasterlingWarriorEntity {

    private @Nullable Goal meleeAttackAI;
    private @Nullable Goal rangedAttackAI;

    public LOTREasterlingFireThrowerEntity(EntityType<? extends LOTREasterlingFireThrowerEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = false;
    }

    @Override
    protected Goal createEasterlingAttackAI() {
        return meleeAttackAI();
    }

    private Goal meleeAttackAI() {
        if (this.meleeAttackAI == null) {
            this.meleeAttackAI = new LOTRAttackOnCollideGoal(this, 1.4, false);
        }
        return this.meleeAttackAI;
    }

    private Goal rangedAttackAI() {
        if (this.rangedAttackAI == null) {
            this.rangedAttackAI = new LOTRRangedAttackGoal(this, 1.3, 20, 30, 16.0f);
        }
        return this.rangedAttackAI;
    }

    /** A burning target is fought hand to hand from any distance. */
    @Override
    public double getMeleeRange() {
        LivingEntity target = getTarget();
        if (target != null && target.isOnFire()) {
            return Double.MAX_VALUE;
        }
        return super.getMeleeRange();
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        Goal melee = meleeAttackAI();
        Goal ranged = rangedAttackAI();
        this.goalSelector.removeGoal(melee);
        this.goalSelector.removeGoal(ranged);
        if (mode == AttackMode.IDLE) {
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getIdleItem());
        } else if (mode == AttackMode.MELEE) {
            this.goalSelector.addGoal(2, melee);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getMeleeWeapon());
        } else {
            this.goalSelector.addGoal(2, ranged);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getRangedWeapon());
        }
    }

    /** A fire pot, aimed as an arrow would be at speed 1.0 and spread 0.5. */
    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        LOTRFirePotEntity pot = new LOTRFirePotEntity(LOTREntities.FIRE_POT, this, level,
                new ItemStack(LOTRCombatItems.RHUNIC_FIRE_POT));
        double dx = target.getX() - getX();
        double dy = target.getY() + target.getEyeHeight() - 0.7 - pot.getY();
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        pot.shoot(dx, dy + horizontal * 0.2, dz, 1.0f, 0.5f);
        playSound(SoundEvents.ARROW_SHOOT, 1.0f, 1.0f / (this.random.nextFloat() * 0.4f + 0.8f));
        level.addFreshEntity(pot);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.RHUNIC_DAGGER));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.RHUNIC_FIRE_POT));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        return data;
    }
}
