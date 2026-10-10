package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

/**
 * LOTREntityHobbitFarmer: a hobbit who stands his ground with his iron hoe
 * rather than panicking, in a brown hat.
 *
 * He also hires out farmhands.
 */
public class LOTRHobbitFarmerEntity extends LOTRHobbitEntity implements LOTRTradeable, LOTRUnitTradeable {

    public LOTRHobbitFarmerEntity(EntityType<? extends LOTRHobbitFarmerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new LOTRAttackOnCollideGoal(this, 1.2, false));
        addTargetTasks(false);
    }

    @Override
    protected boolean panics() {
        return false;
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.HOBBIT_FARMER;
    }

    @Override
    public @Nullable LOTRInvasions getWarhorn() {
        return null;
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.HOBBIT_FARMER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.HOBBIT_FARMER_SELL;
    }

    /** canTradeWith: not disliked, and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "hobbit/farmer/friendly" : "hobbit/farmer/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(Items.IRON_HOE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.HEAD, LOTRHobbitBounderEntity.hat(10390131, -1));
        return data;
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        if (type == LOTRTradeEntries.TradeType.BUY && stack.is(net.minecraft.world.item.Items.POTATO)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.BUY_POTATO_HOBBIT_FARMER);
        }
    }

    @Override
    public void onUnitTrade(Player player) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.HIRE_HOBBIT_FARMER);
    }
}
