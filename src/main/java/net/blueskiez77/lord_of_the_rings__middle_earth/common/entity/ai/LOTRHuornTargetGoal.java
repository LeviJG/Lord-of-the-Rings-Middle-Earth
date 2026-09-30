package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRHuornBaseEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAINearestAttackableTargetHuorn: a huorn stirs to look for a
 * victim only one tick in 400 -- halved for every huorn within 24 blocks (8
 * up or down) already after one, down to one in twenty -- whether after a
 * player or a creature of a hostile faction.
 */
public class LOTRHuornTargetGoal extends LOTRNearestAttackableTargetGoal {

    private LOTRHuornTargetGoal(PathfinderMob mob, Class<? extends LivingEntity> targetClass,
                                @Nullable Predicate<LivingEntity> selector) {
        super(mob, targetClass, 0, true, selector);
    }

    /** The original built both of its target goals of this class: one for players... */
    public static LOTRHuornTargetGoal forPlayers(PathfinderMob mob) {
        return new LOTRHuornTargetGoal(mob, Player.class, null);
    }

    /** ...and one for creatures of hostile factions. */
    public static LOTRHuornTargetGoal forFactions(PathfinderMob mob) {
        return new LOTRHuornTargetGoal(mob, Mob.class, target -> isFactionTarget(mob, target));
    }

    @Override
    public boolean canUse() {
        int chance = 400;
        for (LOTRHuornBaseEntity huorn : this.mob.level().getEntitiesOfClass(LOTRHuornBaseEntity.class,
                this.mob.getBoundingBox().inflate(24.0, 8.0, 24.0))) {
            if (huorn.getTarget() != null) {
                chance /= 2;
            }
        }
        chance = Math.max(chance, 20);
        if (this.mob.getRandom().nextInt(chance) != 0) {
            return false;
        }
        return super.canUse();
    }
}
