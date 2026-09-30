package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrowingAxeEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
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
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDwarfAxeThrower: a warrior who throws dwarven throwing axes
 * instead, at 12 blocks.
 */
public class LOTRDwarfAxeThrowerEntity extends LOTRDwarfWarriorEntity {

    public LOTRDwarfAxeThrowerEntity(EntityType<? extends LOTRDwarfAxeThrowerEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRDwarfEntity.createAttributes()
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.75);
    }

    @Override
    protected Goal createDwarfAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 40, 40, 12.0f);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        throwAxe(this, target, LOTRCombatItems.DWARVEN_THROWING_AXE);
    }

    /**
     * attackEntityWithRangedAttack: the thrower's axe (or a fresh one of its
     * people's) aimed a little below the target's eyes, as true as its ranged
     * accuracy.
     */
    public static void throwAxe(LOTRNPCEntity thrower, LivingEntity target, Item fallback) {
        if (!(thrower.level() instanceof ServerLevel level)) {
            return;
        }
        ItemStack axeItem = thrower.npcItemsInv.getRangedWeapon();
        if (axeItem.isEmpty()) {
            axeItem = new ItemStack(fallback);
        }
        LOTRThrowingAxeEntity axe = new LOTRThrowingAxeEntity(LOTREntities.THROWING_AXE, thrower, level, axeItem.copy());
        double dx = target.getX() - thrower.getX();
        double dy = target.getY() + target.getEyeHeight() - 0.7 - axe.getY();
        double dz = target.getZ() - thrower.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        axe.shoot(dx, dy + horizontal * 0.2, dz, 1.5f,
                (float) thrower.getAttributeValue(LOTRNPCAttributes.NPC_RANGED_ACCURACY));
        thrower.playSound(SoundEvents.ARROW_SHOOT, 1.0f, 1.0f / (thrower.getRandom().nextFloat() * 0.4f + 0.8f));
        level.addFreshEntity(axe);
        thrower.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getRangedWeapon());
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.DWARVEN_THROWING_AXE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        return data;
    }
}
