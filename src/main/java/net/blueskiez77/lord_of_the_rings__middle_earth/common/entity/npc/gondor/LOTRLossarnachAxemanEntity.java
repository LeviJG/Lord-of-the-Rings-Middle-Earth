package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrowingAxeEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
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
 * LOTREntityLossarnachAxeman: an axeman of Lossarnach, always on foot, with
 * a Lossarnach battleaxe up close and Lossarnach throwing axes from 16 blocks
 * out, in Lossarnach armour; two in three wear its helmet.
 *
 * <p>NOT ported yet: the Lossarnach shield (LOTRShields.ALIGNMENT_LOSSARNACH, D7).
 */
public class LOTRLossarnachAxemanEntity extends LOTRGondorSoldierEntity {

    private @Nullable Goal rangedAttackAI;
    private @Nullable Goal meleeAttackAI;

    public LOTRLossarnachAxemanEntity(EntityType<? extends LOTRLossarnachAxemanEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = false;
    }

    @Override
    protected Goal createGondorAttackAI() {
        this.meleeAttackAI = new LOTRAttackOnCollideGoal(this, 1.6, false);
        return this.meleeAttackAI;
    }

    private Goal rangedAttackAI() {
        if (this.rangedAttackAI == null) {
            this.rangedAttackAI = new LOTRRangedAttackGoal(this, 1.3, 30, 50, 16.0f);
        }
        return this.rangedAttackAI;
    }

    /**
     * attackEntityWithRangedAttack: a throwing axe aimed a little below the
     * target's eyes, as true as the axeman's ranged accuracy.
     */
    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        ItemStack axeItem = this.npcItemsInv.getRangedWeapon();
        if (axeItem.isEmpty()) {
            axeItem = new ItemStack(LOTRCombatItems.LOSSARNACH_THROWING_AXE);
        }
        LOTRThrowingAxeEntity axe = new LOTRThrowingAxeEntity(LOTREntities.THROWING_AXE, this, level, axeItem.copy());
        double dx = target.getX() - getX();
        double dy = target.getY() + target.getEyeHeight() - 0.7 - axe.getY();
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        axe.shoot(dx, dy + horizontal * 0.2, dz, 1.5f,
                (float) getAttributeValue(LOTRNPCAttributes.NPC_RANGED_ACCURACY));
        playSound(SoundEvents.ARROW_SHOOT, 1.0f, 1.0f / (this.random.nextFloat() * 0.4f + 0.8f));
        level.addFreshEntity(axe);
        swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        Goal ranged = rangedAttackAI();
        if (this.meleeAttackAI != null) {
            this.goalSelector.removeGoal(this.meleeAttackAI);
        }
        this.goalSelector.removeGoal(ranged);
        if (mode == AttackMode.IDLE) {
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getIdleItem());
        } else if (mode == AttackMode.MELEE) {
            if (this.meleeAttackAI != null) {
                this.goalSelector.addGoal(2, this.meleeAttackAI);
            }
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getMeleeWeapon());
        } else if (mode == AttackMode.RANGED) {
            this.goalSelector.addGoal(2, ranged);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getRangedWeapon());
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.LOSSARNACH_BATTLEAXE));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.LOSSARNACH_THROWING_AXE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.LOSSARNACH_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.LOSSARNACH_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.LOSSARNACH_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, this.random.nextInt(3) == 0
                ? ItemStack.EMPTY : new ItemStack(LOTRCombatItems.LOSSARNACH_HELMET));
        return data;
    }
}
