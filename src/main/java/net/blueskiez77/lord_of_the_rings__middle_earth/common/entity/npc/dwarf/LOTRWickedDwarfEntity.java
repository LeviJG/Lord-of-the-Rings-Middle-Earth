package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSmith;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;
import net.minecraft.world.level.LevelAccessor;

/**
 * LOTREntityWickedDwarf: a dwarf gone over to the Enemy, of Mordor's faction.
 * He seeks out Mordor's enemies with a dwarven sword, battleaxe or warhammer,
 * carries a dwarven pickaxe when idle, and one time in four wears dwarven
 * armour without the helmet. He works the forge for anyone at +100 with
 * Mordor, Angmar or Rhûn.
 */
public class LOTRWickedDwarfEntity extends LOTRDwarfEntity implements LOTRSmith {

    private static final Item[] WICKED_WEAPONS = {LOTRCombatItems.DWARVEN_SWORD, LOTRCombatItems.DWARVEN_BATTLEAXE,
            LOTRCombatItems.DWARVEN_WARHAMMER};

    public LOTRWickedDwarfEntity(EntityType<? extends LOTRWickedDwarfEntity> type, Level level) {
        super(type, level);
    }

    public static LOTRFaction[] getTradeFactions() {
        return new LOTRFaction[]{LOTRFaction.MORDOR, LOTRFaction.ANGMAR, LOTRFaction.RHUDEL};
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.MORDOR;
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.WICKED_DWARF_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.WICKED_DWARF_SELL;
    }

    /** canTradeWith: +100 with any of Mordor, Angmar or Rhûn, and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        boolean hasSuitableAlignment = false;
        for (LOTRFaction faction : getTradeFactions()) {
            if (LOTRPlayerAlignments.getAlignment(player, faction) >= 100.0f) {
                hasSuitableAlignment = true;
                break;
            }
        }
        return hasSuitableAlignment && isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendlyAndAligned(player)) {
            return canTradeWith(player) ? "dwarf/wicked/friendly" : "dwarf/wicked/neutral";
        }
        return "dwarf/wicked/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WICKED_WEAPONS[this.random.nextInt(WICKED_WEAPONS.length)]));
        this.npcItemsInv.setIdleItem(new ItemStack(LOTRToolItems.DWARVEN_PICKAXE));
        if (this.random.nextInt(4) == 0) {
            setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.DWARVEN_BOOTS));
            setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.DWARVEN_LEGGINGS));
            setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DWARVEN_CHESTPLATE));
        } else {
            setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
            setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
            setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
        }
        setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return null;
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_WICKED_DWARF;
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_WICKED_DWARF);
    }

    /** Above y 62, on the biome's own top block. */
    @Override
    public boolean canDwarfSpawnHere(LevelAccessor level) {
        return isAboveSeaOnTopBlock(level, false);
    }
}
