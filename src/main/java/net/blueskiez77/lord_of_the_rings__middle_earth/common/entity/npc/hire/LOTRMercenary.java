package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

/**
 * LOTRMercenary: an NPC who hires himself out, for his base cost, to anyone
 * with the alignment he asks (LOTRMercenaryTradeEntry) -- once, while no one
 * else has him.
 */
public interface LOTRMercenary extends LOTRHireableBase {

    float getMercAlignmentRequired();

    int getMercBaseCost();
}
