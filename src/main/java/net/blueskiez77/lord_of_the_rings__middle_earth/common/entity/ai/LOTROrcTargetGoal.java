package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;

/**
 * LOTREntityAINearestAttackableTargetOrc: Mordor's orcs trust no one short
 * of +100. A player below it but not disliked may still be set upon, one
 * chance in five times their alignment (at least one in one). Other orcs look
 * for players as any NPC does.
 */
public class LOTROrcTargetGoal extends LOTRNearestAttackableTargetGoal {

    public LOTROrcTargetGoal(PathfinderMob mob) {
        super(mob, Player.class, 0, true, null);
    }

    @Override
    protected boolean isPlayerSuitableTarget(Player player) {
        if (factionOf(this.mob) == LOTRFaction.MORDOR) {
            float alignment = LOTRPlayerAlignments.getAlignment(player, LOTRFaction.MORDOR);
            if (alignment >= 100.0f) {
                return false;
            }
            if (alignment >= 0.0f) {
                int chance = Math.max(Math.round(alignment * 5.0f), 1);
                return this.mob.getRandom().nextInt(chance) == 0;
            }
        }
        return super.isPlayerSuitableTarget(player);
    }
}
