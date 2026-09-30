package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

/**
 * LOTRMercenary: an NPC who hires himself out, for his base cost, to anyone
 * with the alignment he asks (LOTRMercenaryTradeEntry) -- once, while no one
 * else has him.
 *
 * <p>NOT ported yet: the mercenary's screens (LOTRGuiMercenaryInteract,
 * LOTRGuiMercenaryHire) and LOTRPacketBuyUnit's mercenary branch, with the
 * other hire screens (D16).
 */
public interface LOTRMercenary extends LOTRHireableBase {

    float getMercAlignmentRequired();

    int getMercBaseCost();
}
