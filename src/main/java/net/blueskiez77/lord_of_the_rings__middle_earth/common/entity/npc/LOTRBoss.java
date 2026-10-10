package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;

/**
 * LOTRBoss: an NPC fought as a boss -- with a boss bar (IBossDisplayData),
 * a jump attack that crushes all about it, and a record of who hurt it --
 * everyone who dealt it forty damage earns its kill achievement.
 */
public interface LOTRBoss {

    /** How readily it uses its powers: more, the more it is hurt. */
    float getBaseChanceModifier();

    /** What it does as it lands from a jump attack. */
    void onJumpAttackFall();

    /** Earned by everyone who dealt it forty damage, when it dies. */
    LOTRAchievement getBossKillAchievement();
}
