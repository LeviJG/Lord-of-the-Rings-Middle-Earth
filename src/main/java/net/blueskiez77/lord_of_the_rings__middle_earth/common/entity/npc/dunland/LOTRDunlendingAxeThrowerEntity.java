package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRDwarfAxeThrowerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

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
 * LOTREntityDunlendingAxeThrower: a Dunlending warrior who throws iron or
 * bronze axes instead, at 12 blocks.
 */
public class LOTRDunlendingAxeThrowerEntity extends LOTRDunlendingWarriorEntity {

    public LOTRDunlendingAxeThrowerEntity(EntityType<? extends LOTRDunlendingAxeThrowerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected Goal createDunlendingAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.4, 40, 60, 12.0f);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        LOTRDwarfAxeThrowerEntity.throwAxe(this, target, LOTRCombatItems.IRON_THROWING_AXE);
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
        this.npcItemsInv.setRangedWeapon(new ItemStack(this.random.nextBoolean()
                ? LOTRCombatItems.IRON_THROWING_AXE : LOTRCombatItems.BRONZE_THROWING_AXE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        return data;
    }
}
