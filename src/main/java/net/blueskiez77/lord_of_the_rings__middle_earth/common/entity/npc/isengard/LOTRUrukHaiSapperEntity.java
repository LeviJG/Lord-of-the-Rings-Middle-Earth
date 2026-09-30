package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.isengard;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
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

/**
 * LOTREntityUrukHaiSapper: an Uruk with an orc bomb and a torch, always
 * helmeted, who does not flee bombs, runs in at 1.5 to drop his own lit
 * ({@code LOTROrcPlaceBombGoal}) and then charges at 2.0.
 */
public class LOTRUrukHaiSapperEntity extends LOTRUrukHaiEntity {

    public LOTRUrukHaiSapperEntity(EntityType<? extends LOTRUrukHaiSapperEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 2.0, false);
    }

    @Override
    protected boolean avoidsOrcBombs() {
        return false;
    }

    @Override
    protected double getPlaceBombSpeed() {
        return 1.5;
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
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.URUK_HELMET));
        return data;
    }
}
