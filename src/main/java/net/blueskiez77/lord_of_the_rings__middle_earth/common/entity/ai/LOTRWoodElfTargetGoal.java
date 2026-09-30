package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRWoodElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;

/**
 * LOTREntityAINearestAttackableTargetWoodElf: the Wood-elves trust no one
 * short of their first rank. A player below it but not disliked may still be
 * set upon, one chance in twenty times their alignment (at least one in one).
 */
public class LOTRWoodElfTargetGoal extends LOTRNearestAttackableTargetGoal {

    public LOTRWoodElfTargetGoal(PathfinderMob mob) {
        super(mob, Player.class, 0, true, null);
    }

    @Override
    protected boolean isPlayerSuitableTarget(Player player) {
        float alignment = LOTRPlayerAlignments.getAlignment(player, factionOf(this.mob));
        if (alignment >= LOTRWoodElfEntity.getWoodlandTrustLevel()) {
            return false;
        }
        if (alignment >= 0.0f) {
            int chance = Math.max(Math.round(alignment * 20.0f), 1);
            return this.mob.getRandom().nextInt(chance) == 0;
        }
        return super.isPlayerSuitableTarget(player);
    }
}
