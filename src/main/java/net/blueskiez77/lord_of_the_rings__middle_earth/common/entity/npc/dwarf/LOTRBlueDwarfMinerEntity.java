package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

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
 * LOTREntityBlueDwarfMiner: a Blue Mountains dwarf with a pickaxe, who trades
 * food and drink for the mine's ore and gems with those at +100 or better.
 */
public class LOTRBlueDwarfMinerEntity extends LOTRBlueDwarfEntity implements LOTRTradeable {

    public LOTRBlueDwarfMinerEntity(EntityType<? extends LOTRBlueDwarfMinerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.BLUE_DWARF_MINER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.BLUE_DWARF_MINER_SELL;
    }

    /** canTradeWith: +100 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f && isFriendly(player);
    }

    /** A miner brought back by a respawner comes back only once. */
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
        if (isFriendly(player)) {
            return canTradeWith(player) ? "blueDwarf/miner/friendly" : "blueDwarf/miner/neutral";
        }
        return "blueDwarf/dwarf/hostile";
    }

    /** dropFewItems: slain by a player, sometimes a mine's haul, and one time in fifteen a mithril nugget. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        if (killedByPlayer) {
            if (this.random.nextInt(4) == 0) {
                dropChestContents(level, LOTRChestContents.DWARVEN_MINE_CORRIDOR, 1, 2 + looting);
            }
            if (this.random.nextInt(15) == 0) {
                spawnAtLocation(level, new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET));
            }
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRToolItems.BLUE_DWARVEN_PICKAXE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        return data;
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_BLUE_DWARF_MINER);
    }

    /** Only underground. */
    @Override
    public boolean canDwarfSpawnAboveGround() {
        return false;
    }
}
