package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;

/**
 * LOTREntityAINearestAttackableTargetTroll: a troll leaves alone those at
 * +100 with its people. A player below it but not disliked may still be set
 * upon, one chance in ten times their alignment (at least one in one).
 */
public class LOTRTrollTargetGoal extends LOTRNearestAttackableTargetGoal {

    public LOTRTrollTargetGoal(PathfinderMob mob) {
        super(mob, Player.class, 0, true, null);
    }

    @Override
    protected boolean isPlayerSuitableTarget(Player player) {
        float alignment = LOTRPlayerAlignments.getAlignment(player, factionOf(this.mob));
        if (alignment >= 100.0f) {
            return false;
        }
        if (alignment >= 0.0f) {
            int chance = Math.max(Math.round(alignment * 10.0f), 1);
            return this.mob.getRandom().nextInt(chance) == 0;
        }
        return super.isPlayerSuitableTarget(player);
    }
}
