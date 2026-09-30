package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dolguldur;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/** LOTREntityDolGuldurOrcArcher: a Dol Guldur orc who shoots with an orc bow instead, at 16 blocks, and leaves arrows. */
public class LOTRDolGuldurOrcArcherEntity extends LOTRDolGuldurOrcEntity {

    public LOTRDolGuldurOrcArcherEntity(EntityType<? extends LOTRDolGuldurOrcArcherEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 30, 60, 16.0f);
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getRangedWeapon());
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        dropNPCAmmo(level, Items.ARROW, looting);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.ORC_BOW));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        return data;
    }
}
