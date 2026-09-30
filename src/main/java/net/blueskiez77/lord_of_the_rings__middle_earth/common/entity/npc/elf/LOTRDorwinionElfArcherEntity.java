package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/** LOTREntityDorwinionElfArcher (the Archer of Bladorthin): a warrior with a Dorwinion elven bow, from 24 blocks, in any fight. */
public class LOTRDorwinionElfArcherEntity extends LOTRDorwinionElfWarriorEntity {

    public LOTRDorwinionElfArcherEntity(EntityType<? extends LOTRDorwinionElfArcherEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected Goal createElfRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 30, 50, 24.0f);
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        Goal ranged = rangedAttackAI();
        this.goalSelector.removeGoal(meleeAttackAI());
        this.goalSelector.removeGoal(ranged);
        if (mode == AttackMode.IDLE) {
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getIdleItem());
        } else {
            this.goalSelector.addGoal(2, ranged);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getRangedWeapon());
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.DORWINION_ELVEN_BOW));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        return data;
    }
}
