package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTravellingTrader;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRLeatherHatItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

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
 * LOTREntityDaleMerchant: a travelling merchant of Dale in a feathered leather
 * hat, with a man of Dale for an escort, trading with any who are friendly.
 *
 * <p>NOT ported yet: the tradeDaleMerchant achievement (D7); the travelling
 * traders' spawning (D12).
 */
public class LOTRDaleMerchantEntity extends LOTRDaleManEntity implements LOTRTravellingTrader {

    private static final int[] HAT_COLORS = {8874591, 11895125, 4949452, 8298956, 5657939};
    private static final int[] FEATHER_COLORS = {16777215, 6736967, 15358290, 156402, 15719168};

    public LOTRDaleMerchantEntity(EntityType<? extends LOTRDaleMerchantEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.DALE_MERCHANT_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.DALE_MERCHANT_SELL;
    }

    /** canTradeWith: +0 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
    }

    @Override
    public @Nullable LOTRNPCEntity createTravellingEscort(ServerLevel level) {
        return LOTREntities.DALE_MAN.create(level, EntitySpawnReason.EVENT);
    }

    @Override
    public String getDepartureSpeech() {
        return "dale/merchant/departure";
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
        return isFriendly(player) ? "dale/merchant/friendly" : "dale/merchant/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        ItemStack hat = new ItemStack(LOTRMiscItems.LEATHER_HAT);
        LOTRLeatherHatItem.setHatColor(hat, HAT_COLORS[this.random.nextInt(HAT_COLORS.length)]);
        LOTRLeatherHatItem.setFeatherColor(hat, FEATHER_COLORS[this.random.nextInt(FEATHER_COLORS.length)]);
        setItemSlot(EquipmentSlot.HEAD, hat);
        return data;
    }
}
