package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBandit;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;

/**
 * LOTREntityAINearestAttackableTargetBandit: a bandit looks only for
 * players, and only for one he could not rob -- one with something to take
 * he steals from instead -- and not at all while he carries loot.
 */
public class LOTRBanditTargetGoal extends LOTRNearestAttackableTargetGoal {

    private final LOTRBandit bandit;

    public LOTRBanditTargetGoal(PathfinderMob mob) {
        super(mob, Player.class, 0, true, null);
        this.bandit = (LOTRBandit) mob;
    }

    @Override
    protected boolean isPlayerSuitableTarget(Player player) {
        return !LOTRBandit.canStealFromPlayerInv(player) && super.isPlayerSuitableTarget(player);
    }

    @Override
    public boolean canUse() {
        return this.bandit.getBanditInventory().isEmpty() && super.canUse();
    }
}
