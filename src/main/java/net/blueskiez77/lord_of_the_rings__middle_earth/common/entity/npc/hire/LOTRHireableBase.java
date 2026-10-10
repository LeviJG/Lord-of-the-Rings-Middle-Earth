package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.world.entity.player.Player;

/** LOTRHireableBase: one who hires out units -- a captain, a farmer, a mercenary. */
public interface LOTRHireableBase {

    boolean canTradeWith(Player player);

    LOTRFaction getFaction();

    /** onUnitTrade: the hirer's own reaction (its achievements). */
    default void onUnitTrade(Player player) {
    }
}
