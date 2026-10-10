package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

import org.jspecify.annotations.Nullable;

/**
 * LOTRUnitTradeable: an NPC who hires out the units of its list, and the
 * warhorn, if any, it sells from the hire screen's reward slot.
 */
public interface LOTRUnitTradeable extends LOTRHireableBase {

    LOTRUnitTradeEntries getUnits();

    @Nullable LOTRInvasions getWarhorn();
}
