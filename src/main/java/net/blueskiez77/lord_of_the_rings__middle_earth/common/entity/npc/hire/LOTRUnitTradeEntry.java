package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCoins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRUnitTradeEntry: a unit for hire -- its price in silver, the alignment
 * it asks, any pledge it demands, and whether it fights or farms. The price
 * doubles for a player not pledged to the hirer's faction, and falls by up
 * to half as the player's alignment climbs above what the unit asks (all of
 * it at 1500 over for the pledged, 2000 for the rest).
 *
 * <p>A unit may come mounted, its mount perhaps barded.
 */
public class LOTRUnitTradeEntry {

    private final Supplier<? extends EntityType<? extends LOTRNPCEntity>> entityType;
    private @Nullable Supplier<? extends EntityType<? extends Mob>> mountType;
    /** The unit's name when it comes mounted: "lotr.unit.<name>". */
    public @Nullable String name;
    private @Nullable Supplier<Item> mountArmor;
    private float mountArmorChance;
    public final int initialCost;
    public float alignmentRequired;
    private LOTRUnitPledgeType pledgeType = LOTRUnitPledgeType.NONE;
    public LOTRHiredTask task = LOTRHiredTask.WARRIOR;
    /** A line on the unit for the hire screen: "lotr.unitinfo.<extraInfo>". */
    public @Nullable String extraInfo;

    public LOTRUnitTradeEntry(Supplier<? extends EntityType<? extends LOTRNPCEntity>> type, int cost, float alignment) {
        this.entityType = type;
        this.initialCost = cost;
        this.alignmentRequired = alignment;
    }

    /** A unit that comes on a mount of the given kind. */
    public LOTRUnitTradeEntry(Supplier<? extends EntityType<? extends LOTRNPCEntity>> type,
                              Supplier<? extends EntityType<? extends Mob>> mount, String name, int cost, float alignment) {
        this(type, cost, alignment);
        this.mountType = mount;
        this.name = name;
    }

    public LOTRUnitTradeEntry setMountArmor(Supplier<Item> armor, float chance) {
        this.mountArmor = armor;
        this.mountArmorChance = chance;
        return this;
    }

    /**
     * createHiredMount: equipped as on spawning -- a mount that is itself an
     * NPC hired alongside -- and barded if the chance allows.
     */
    private @Nullable Mob createHiredMount(ServerLevel level) {
        if (this.mountType == null) {
            return null;
        }
        Mob mount = this.mountType.get().create(level, EntitySpawnReason.MOB_SUMMONED);
        if (mount instanceof LOTRNPCEntity npcMount) {
            npcMount.initCreatureForHire(level);
            npcMount.refreshCurrentAttackMode();
        } else if (mount != null) {
            mount.finalizeSpawn(level, level.getCurrentDifficultyAt(mount.blockPosition()), EntitySpawnReason.MOB_SUMMONED, null);
        }
        if (mount != null && this.mountArmor != null && level.getRandom().nextFloat() < this.mountArmorChance) {
            mount.setItemSlot(EquipmentSlot.BODY, new ItemStack(this.mountArmor.get()));
        }
        return mount;
    }

    public EntityType<? extends LOTRNPCEntity> getEntityType() {
        return this.entityType.get();
    }

    public int getCost(Player player, LOTRHireableBase trader) {
        float cost = this.initialCost;
        LOTRFaction faction = trader.getFaction();
        float alignment = LOTRPlayerAlignments.getAlignment(player, faction);
        float alignSurplus = Math.max(alignment - this.alignmentRequired, 0.0f);
        float f;
        if (LOTRPlayerAlignments.isPledgedTo(player, faction)) {
            f = alignSurplus / 1500.0f;
        } else {
            cost *= 2.0f;
            f = alignSurplus / 2000.0f;
        }
        cost *= 1.0f - Mth.clamp(f, 0.0f, 1.0f) * 0.5f;
        return Math.max(Math.round(cost), 1);
    }

    public LOTRUnitPledgeType getPledgeType() {
        return this.pledgeType;
    }

    public LOTRUnitTradeEntry setPledgeType(LOTRUnitPledgeType type) {
        this.pledgeType = type;
        return this;
    }

    public LOTRUnitTradeEntry setPledgeExclusive() {
        return setPledgeType(LOTRUnitPledgeType.FACTION);
    }

    /**
     * The original gave every LOTRBannerBearer unit the "Banner" line from
     * its class; an EntityType does not tell its class, so the lists mark
     * their banner bearers.
     */
    public LOTRUnitTradeEntry setBannerBearer() {
        this.extraInfo = "Banner";
        return this;
    }

    /** setExtraInfo: a line of its own on the hire screen, "lotr.unitinfo.<info>". */
    public LOTRUnitTradeEntry setExtraInfo(String info) {
        this.extraInfo = info;
        return this;
    }

    public boolean hasExtraInfo() {
        return this.extraInfo != null;
    }

    public LOTRUnitTradeEntry setTask(LOTRHiredTask task) {
        this.task = task;
        return this;
    }

    public boolean hasRequiredCostAndAlignment(Player player, LOTRHireableBase trader) {
        if (LOTRCoins.getInventoryValue(player) < getCost(player, trader)) {
            return false;
        }
        LOTRFaction faction = trader.getFaction();
        if (!this.pledgeType.canAcceptPlayer(player, faction)) {
            return false;
        }
        return LOTRPlayerAlignments.getAlignment(player, faction) >= this.alignmentRequired;
    }

    /** getOrCreateHiredNPC: a new unit, equipped as it would spawn but never mounted. */
    private @Nullable LOTRNPCEntity getOrCreateHiredNPC(ServerLevel level) {
        LOTRNPCEntity npc = getEntityType().create(level, EntitySpawnReason.MOB_SUMMONED);
        if (npc != null) {
            npc.initCreatureForHire(level);
            npc.refreshCurrentAttackMode();
        }
        return npc;
    }

    /**
     * hireUnit: if the player can pay and meets the terms, the hirer is paid,
     * with the trade sound, and the new unit takes service at the player's
     * feet.
     */
    public void hireUnit(Player player, LOTRHireableBase trader, @Nullable String squadron) {
        if (!(player.level() instanceof ServerLevel level) || !hasRequiredCostAndAlignment(player, trader)) {
            return;
        }
        trader.onUnitTrade(player);
        LOTRCoins.takeCoins(getCost(player, trader), player);
        ((LOTRNPCEntity) trader).playTradeSound();
        LOTRNPCEntity hiredNPC = getOrCreateHiredNPC(level);
        if (hiredNPC != null) {
            Mob mount = createHiredMount(level);
            hiredNPC.hiredNPCInfo.hireUnit(player, true, trader.getFaction(), this, squadron, mount);
            level.addFreshEntity(hiredNPC);
            if (mount != null) {
                level.addFreshEntity(mount);
                hiredNPC.startRiding(mount, true, false);
            }
        }
    }
}
