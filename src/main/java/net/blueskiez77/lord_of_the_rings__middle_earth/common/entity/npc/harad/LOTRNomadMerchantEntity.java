package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTravellingTrader;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRHaradTurbanItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityNomadMerchant: a travelling merchant of the desert, with a nomad
 * for escort, who trades with anyone Near Harad does not dislike, in a full
 * set of Harad robes of one of six colours, the turban half the time set
 * with a gold ornament.
 *
 * He holds a small pouch (the original's damage value 3, drawn as the small one).
 */
public class LOTRNomadMerchantEntity extends LOTRNomadEntity implements LOTRTravellingTrader {

    private static final int[] ROBE_COLOURS = {15723226, 13551017, 6512465, 2499615, 11376219, 7825215};

    public LOTRNomadMerchantEntity(EntityType<? extends LOTRNomadMerchantEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.NOMAD_MERCHANT_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.NOMAD_MERCHANT_SELL;
    }

    /** canTradeWith: not disliked, and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
    }

    @Override
    public @Nullable LOTRNPCEntity createTravellingEscort(ServerLevel level) {
        return LOTREntities.NOMAD.create(level, EntitySpawnReason.EVENT);
    }

    @Override
    public String getDepartureSpeech() {
        return "nearHarad/nomad/merchant/departure";
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
        return isFriendly(player) ? "nearHarad/nomad/merchant/friendly" : "nearHarad/nomad/merchant/hostile";
    }

    private static ItemStack robe(Item item, int colour) {
        ItemStack robe = new ItemStack(item);
        robe.set(DataComponents.DYED_COLOR, new DyedItemColor(colour));
        return robe;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setIdleItem(new ItemStack(LOTRMiscItems.SMALL_POUCH));
        int robeColour = ROBE_COLOURS[this.random.nextInt(ROBE_COLOURS.length)];
        boolean ornament = this.random.nextBoolean();
        setItemSlot(EquipmentSlot.FEET, robe(LOTRMiscItems.HARAD_ROBE_SHOES, robeColour));
        setItemSlot(EquipmentSlot.LEGS, robe(LOTRMiscItems.HARAD_ROBE_LEGGINGS, robeColour));
        setItemSlot(EquipmentSlot.CHEST, robe(LOTRMiscItems.HARAD_ROBE, robeColour));
        setItemSlot(EquipmentSlot.HEAD, LOTRHaradTurbanItem.setHasOrnament(turban(robeColour), ornament));
        return data;
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_NOMAD_MERCHANT);
    }
}
