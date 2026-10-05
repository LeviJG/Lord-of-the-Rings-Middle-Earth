package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTravellingTrader;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRHaradTurbanItem;

import net.minecraft.server.level.ServerLevel;
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
 * LOTREntityNearHaradMerchant: a travelling merchant of the South, with an
 * escort of Southrons, who trades with anyone Near Harad does not dislike,
 * robeless but for a turban of one of five colours, half the time set with a
 * gold ornament.
 *
 * <p>Not here: the pouch he held (pouches are not ported).
 * NOT ported yet: the tradeNearHaradMerchant achievement (D7).
 */
public class LOTRNearHaradMerchantEntity extends LOTRNearHaradrimEntity implements LOTRTravellingTrader {

    private static final int[] ROBE_COLOURS = {15723226, 14829087, 12653845, 8526876, 2625038};

    public LOTRNearHaradMerchantEntity(EntityType<? extends LOTRNearHaradMerchantEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.NEAR_HARAD_MERCHANT_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.NEAR_HARAD_MERCHANT_SELL;
    }

    /** canTradeWith: not disliked, and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
    }

    @Override
    public @Nullable LOTRNPCEntity createTravellingEscort(ServerLevel level) {
        return LOTREntities.NEAR_HARADRIM.create(level, EntitySpawnReason.EVENT);
    }

    @Override
    public String getDepartureSpeech() {
        return "nearHarad/merchant/departure";
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
        return isFriendly(player) ? "nearHarad/merchant/friendly" : "nearHarad/merchant/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        int robeColour = ROBE_COLOURS[this.random.nextInt(ROBE_COLOURS.length)];
        ItemStack turban = turban(robeColour);
        LOTRHaradTurbanItem.setHasOrnament(turban, this.random.nextBoolean());
        setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
        setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
        setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
        setItemSlot(EquipmentSlot.HEAD, turban);
        return data;
    }
}
