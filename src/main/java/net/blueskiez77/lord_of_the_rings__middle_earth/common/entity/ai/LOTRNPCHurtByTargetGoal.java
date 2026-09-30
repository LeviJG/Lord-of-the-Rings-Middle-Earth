package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

/**
 * LOTREntityAINPCHurtByTarget: fight back against whoever hurt it -- but not
 * its own mount or rider, and whether or not the attacker stands outside its
 * home area (the original lifted the home for the check).
 */
public class LOTRNPCHurtByTargetGoal extends HurtByTargetGoal {

    public LOTRNPCHurtByTargetGoal(PathfinderMob mob) {
        super(mob);
    }

    @Override
    protected boolean canAttack(LivingEntity target, TargetingConditions conditions) {
        if (target == this.mob.getVehicle() || target == this.mob.getFirstPassenger()) {
            return false;
        }
        var home = this.mob.getHomePosition();
        int radius = this.mob.getHomeRadius();
        boolean hadHome = this.mob.hasHome();
        this.mob.clearHome();
        boolean result = super.canAttack(target, conditions);
        if (hadHome) {
            this.mob.setHomeTo(home, radius);
        }
        return result;
    }
}
