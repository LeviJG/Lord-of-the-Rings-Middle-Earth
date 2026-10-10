package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSmith;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMordorOrcTrader: an orc in furs with a
 * poisoned dagger, who trades with those at +100 or better. He seeks no one
 * out -- he only answers attacks.
 */
public class LOTRMordorOrcTraderEntity extends LOTRMordorOrcEntity implements LOTRSmith {

    public LOTRMordorOrcTraderEntity(EntityType<? extends LOTRMordorOrcTraderEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.MORDOR_TRADER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.MORDOR_TRADER_SELL;
    }

    /** canTradeWith: +100 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f && isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "mordor/trader/friendly" : "mordor/trader/neutral";
        }
        return "mordor/orc/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.POISONED_MORDOR_DAGGER));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.FUR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.FUR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.FUR_TUNIC));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.FUR_HAT));
        return data;
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_ORC_TRADER);
    }
}
