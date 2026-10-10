package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dorwinion;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitBounderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTravellingTrader;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
 * LOTREntityDorwinionMerchantMan (the Dorwinion Merchant): a travelling
 * trader of Dorwinion, with an escort of Vintner Guards, who trades with
 * anyone Dorwinion does not dislike, in a feathered hat of one of the
 * merchants' colours.
 */
public class LOTRDorwinionMerchantManEntity extends LOTRDorwinionManEntity implements LOTRTravellingTrader {

    private static final int[] HAT_COLOURS = {15387062, 12361599, 7422850, 12677797, 13401212, 11350064, 9523548,
            12502137, 11718290, 8817612, 6316484};
    private static final int[] FEATHER_COLOURS = {16777215, 12887724, 15061504, 0, 7475245, 4402118, 8311657};

    public LOTRDorwinionMerchantManEntity(EntityType<? extends LOTRDorwinionMerchantManEntity> type, Level level) {
        super(type, level);
    }

    /** A merchant's hat, of one of the merchants' colours with a feather of one of theirs. */
    public static ItemStack merchantHat(RandomSource random) {
        return LOTRHobbitBounderEntity.hat(HAT_COLOURS[random.nextInt(HAT_COLOURS.length)],
                FEATHER_COLOURS[random.nextInt(FEATHER_COLOURS.length)]);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.DORWINION_MERCHANT_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.DORWINION_MERCHANT_SELL;
    }

    /** canTradeWith: not disliked, and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
    }

    @Override
    public @Nullable LOTRNPCEntity createTravellingEscort(ServerLevel level) {
        return LOTREntities.DORWINION_GUARD.create(level, EntitySpawnReason.EVENT);
    }

    @Override
    public String getDepartureSpeech() {
        return "dorwinion/merchant/departure";
    }

    /** A travelling trader brought back by a respawner comes back only once. */
    @Override
    public boolean shouldTraderRespawn() {
        return false;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "dorwinion/merchant/friendly" : "dorwinion/merchant/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setItemSlot(EquipmentSlot.HEAD, merchantHat(this.random));
        return data;
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_DORWINION_MERCHANT);
    }
}
