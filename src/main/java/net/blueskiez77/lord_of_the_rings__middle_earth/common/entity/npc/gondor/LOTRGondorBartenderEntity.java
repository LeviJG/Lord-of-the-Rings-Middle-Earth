package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBartender;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGondorBartender: keeps a tavern ("%s's Tavern"), a mug in hand.
 * NPCs who drink near a friendly bartender may get drunk. Slain, it leaves
 * one to four and more with looting of Gondor's drinks in their vessels,
 * besides a Gondorian's drops.
 */
public class LOTRGondorBartenderEntity extends LOTRGondorManEntity implements LOTRBartender {

    public LOTRGondorBartenderEntity(EntityType<? extends LOTRGondorBartenderEntity> type, Level level) {
        super(type, level);
        this.npcLocationName = "entity.lotr.gondor_bartender.locationName";
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.GONDOR_BARTENDER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.GONDOR_BARTENDER_SELL;
    }

    @Override
    public boolean canTradeWith(Player player) {
        return isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "gondor/bartender/friendly" : "gondor/bartender/hostile";
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int drinks = 1 + this.random.nextInt(4) + looting;
        for (int l = 0; l < drinks; ++l) {
            spawnAtLocation(level, LOTRFoods.GONDOR_DRINK.getRandomFood(this.random), 0.0f);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setIdleItem(new ItemStack(LOTRFoodItems.MUG));
        return data;
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_GONDOR_BARTENDER);
    }
}
