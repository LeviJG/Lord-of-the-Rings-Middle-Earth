package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

/**
 * LOTRBoss: an NPC fought as a boss -- with a boss bar (IBossDisplayData),
 * a jump attack that crushes all about it, and a record of who hurt it.
 *
 * <p>NOT ported yet: getBossKillAchievement, awarded to everyone who dealt it
 * forty damage (D7).
 */
public interface LOTRBoss {

    /** How readily it uses its powers: more, the more it is hurt. */
    float getBaseChanceModifier();

    /** What it does as it lands from a jump attack. */
    void onJumpAttackFall();
}
