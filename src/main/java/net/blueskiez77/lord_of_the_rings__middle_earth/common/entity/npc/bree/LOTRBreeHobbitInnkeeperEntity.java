package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBartender;
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
 * LOTREntityBreeHobbitInnkeeper: a Bree-hobbit who keeps an inn ("%s's Inn"),
 * with the Bree-man innkeeper's trades, a mug in hand and a bartender's
 * outfit.
 *
 * <p>NOT ported yet: the tradeBreeInnkeeper achievement.
 */
public class LOTRBreeHobbitInnkeeperEntity extends LOTRBreeHobbitEntity implements LOTRBartender {

    public LOTRBreeHobbitInnkeeperEntity(EntityType<? extends LOTRBreeHobbitInnkeeperEntity> type, Level level) {
        super(type, level);
        this.npcLocationName = "entity.lotr.bree_hobbit_innkeeper.locationName";
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
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
        return isFriendlyAndAligned(player) ? "bree/innkeeper/hobbit/friendly" : "bree/innkeeper/hobbit/hostile";
    }

    @Override
    protected void dropHobbitItems(ServerLevel level, int looting) {
        LOTRBreeInnkeeperEntity.dropInnItems(this, level, this.random, looting);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setIdleItem(new ItemStack(LOTRFoodItems.MUG));
        return data;
    }
}
