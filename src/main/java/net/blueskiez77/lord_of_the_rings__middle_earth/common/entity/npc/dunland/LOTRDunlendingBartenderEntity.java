package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBartender;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;

import net.minecraft.server.level.ServerLevel;
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

/**
 * LOTREntityDunlendingBartender: the keeper of a Dunland inn, trading with
 * anyone Dunland is friendly to, with a mug in hand and an apron on. He seeks
 * no one out -- he only answers attacks. Slain, he leaves no house goods but
 * a few of Dunland's foods, gold nuggets, mugs and drinks.
 *
 * <p>NOT ported yet: the tradeDunlendingBartender achievement (D7).
 */
public class LOTRDunlendingBartenderEntity extends LOTRDunlendingEntity implements LOTRBartender {

    public LOTRDunlendingBartenderEntity(EntityType<? extends LOTRDunlendingBartenderEntity> type, Level level) {
        super(type, level);
        this.npcLocationName = "entity.lotr.dunlending_bartender.locationName";
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.DUNLENDING_BARTENDER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.DUNLENDING_BARTENDER_SELL;
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
        return isFriendly(player) ? "dunlending/bartender/friendly" : "dunlending/dunlending/hostile";
    }

    /** dropDunlendingItems: foods, gold nuggets, mugs and drinks of the original's middle strengths. */
    @Override
    protected void dropDunlendingItems(ServerLevel level, int looting) {
        int j = this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int k = 0; k < j; ++k) {
            switch (this.random.nextInt(7)) {
                case 0, 1, 2 -> spawnAtLocation(level, new ItemStack(LOTRFoods.DUNLENDING.getRandomFood(this.random).getItem()), 0.0f);
                case 3 -> spawnAtLocation(level, new ItemStack(Items.GOLD_NUGGET, 2 + this.random.nextInt(3)), 0.0f);
                case 4, 5 -> spawnAtLocation(level, new ItemStack(LOTRFoodItems.MUG), 0.0f);
                default -> spawnAtLocation(level, LOTRDrinkItem.stack(
                        LOTRFoods.DUNLENDING_DRINK.getRandomFood(this.random).getItem(), 1 + this.random.nextInt(3)), 0.0f);
            }
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setIdleItem(new ItemStack(LOTRFoodItems.MUG));
        return data;
    }
}
