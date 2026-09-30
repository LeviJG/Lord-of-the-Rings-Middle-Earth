package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMordorOrcBombardier: a Mordor orc with an orc bomb in one hand and
 * an orc torch in the other, always helmeted, who runs up and drops the bomb
 * lit ({@code LOTROrcPlaceBombGoal}) and then fights with its blade.
 */
public class LOTRMordorOrcBombardierEntity extends LOTRMordorOrcEntity {

    public LOTRMordorOrcBombardierEntity(EntityType<? extends LOTRMordorOrcBombardierEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean isOrcBombardier() {
        return true;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setBombingItem(new ItemStack(LOTRDecorationBlocks.ORC_TORCH));
        this.npcItemsInv.setBomb(new ItemStack(LOTRCombatBlocks.ORC_BOMB));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.MORDOR_HELMET));
        return data;
    }
}
