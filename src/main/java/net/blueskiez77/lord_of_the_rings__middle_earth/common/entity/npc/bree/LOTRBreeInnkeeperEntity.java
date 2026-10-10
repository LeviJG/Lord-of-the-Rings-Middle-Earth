package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBartender;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBreeInnkeeper: keeps an inn, which its speech names after it
 * ("%s's Inn"), with a mug in hand. NPCs who drink near a friendly
 * innkeeper may get drunk. Slain, it leaves an inn's odds and ends instead
 * of a house's.
 */
public class LOTRBreeInnkeeperEntity extends LOTRBreeManEntity implements LOTRBartender {

    public LOTRBreeInnkeeperEntity(EntityType<? extends LOTRBreeInnkeeperEntity> type, Level level) {
        super(type, level);
        this.npcLocationName = "entity.lotr.bree_innkeeper.locationName";
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.BREE_INNKEEPER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.BREE_INNKEEPER_SELL;
    }

    @Override
    public boolean canTradeWith(Player player) {
        return isFriendlyAndAligned(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendlyAndAligned(player) ? "bree/innkeeper/man/friendly" : "bree/innkeeper/man/hostile";
    }

    @Override
    protected void dropBreeItems(ServerLevel level, int looting) {
        dropInnItems(this, level, this.random, looting);
    }

    /**
     * dropBreeItems (and the Bree-hobbit innkeeper's dropHobbitItems): up to
     * two and more with looting of Bree-land food, gold nuggets and mugs. The
     * original's last case gave a food at a drink's strength, which on a food
     * meant nothing, so it is a further food.
     */
    static void dropInnItems(LOTRNPCEntity npc, ServerLevel level, RandomSource random, int looting) {
        int count = random.nextInt(3) + random.nextInt(looting + 1);
        for (int k = 0; k < count; ++k) {
            ItemStack drop = switch (random.nextInt(7)) {
                case 3 -> new ItemStack(Items.GOLD_NUGGET, 2 + random.nextInt(3));
                case 4, 5 -> new ItemStack(LOTRFoodItems.MUG);
                default -> new ItemStack(LOTRFoods.BREE.getRandomFood(random).getItem());
            };
            npc.spawnAtLocation(level, drop, 0.0f);
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
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_BREE_INNKEEPER);
    }
}
