package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.player.Player;

/**
 * LOTRUnitTradeEntry.PledgeType: what a unit asks of the player who hires it
 * and keeps it -- nothing, a pledge to the unit's faction, or a pledge to any
 * elven (not mannish) or any dwarven faction.
 */
public enum LOTRUnitPledgeType {
    NONE(0), FACTION(1), ANY_ELF(2), ANY_DWARF(3);

    public final int typeID;

    LOTRUnitPledgeType(int id) {
        this.typeID = id;
    }

    public static LOTRUnitPledgeType forID(int id) {
        for (LOTRUnitPledgeType type : values()) {
            if (type.typeID == id) {
                return type;
            }
        }
        return NONE;
    }

    public boolean canAcceptPlayer(Player player, LOTRFaction faction) {
        LOTRFaction pledged = LOTRPlayerAlignments.get(player).pledgeFaction();
        return switch (this) {
            case NONE -> true;
            case FACTION -> LOTRPlayerAlignments.isPledgedTo(player, faction);
            case ANY_ELF -> pledged != null && pledged.isOfType(LOTRFaction.FactionType.TYPE_ELF)
                    && !pledged.isOfType(LOTRFaction.FactionType.TYPE_MAN);
            case ANY_DWARF -> pledged != null && pledged.isOfType(LOTRFaction.FactionType.TYPE_DWARF);
        };
    }
}
