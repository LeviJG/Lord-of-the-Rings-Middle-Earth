package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

/**
 * LOTRUnitTradeable: an NPC who hires out the units of its list.
 *
 * <p>NOT ported yet: the hire screen (LOTRGuiUnitTrade, LOTRContainerUnitTrade)
 * through which a player hires (D16), and the warhorn some captains sell
 * (getWarhorn, with invasions, D12).
 */
public interface LOTRUnitTradeable extends LOTRHireableBase {

    LOTRUnitTradeEntries getUnits();
}
