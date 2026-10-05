package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

/**
 * LOTRUnitTradeable: an NPC who hires out the units of its list.
 *
 * <p>NOT ported yet: the warhorn some captains sell from the hire screen's
 * reward slot (getWarhorn, LOTRSlotAlignmentReward, with invasions, D12).
 */
public interface LOTRUnitTradeable extends LOTRHireableBase {

    LOTRUnitTradeEntries getUnits();
}
