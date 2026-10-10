package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRAttackRules;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/** LOTREntityAIHiringPlayerHurtByTarget: a hired unit turns on whoever last hurt its player. */
public class LOTRHiringPlayerHurtByTargetGoal extends TargetGoal {

    private final LOTRNPCEntity npc;
    private @Nullable LivingEntity theTarget;
    /** isSuitableTarget(target, false): the target need not be in sight. */
    private static final TargetingConditions UNSEEN = TargetingConditions.forCombat().ignoreLineOfSight();

    private int playerRevengeTimer;

    public LOTRHiringPlayerHurtByTargetGoal(LOTRNPCEntity npc) {
        super(npc, false);
        this.npc = npc;
        setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (!this.npc.hiredNPCInfo.isActive || this.npc.hiredNPCInfo.isHalted()) {
            return false;
        }
        Player player = this.npc.hiredNPCInfo.getHiringPlayer();
        if (player == null) {
            return false;
        }
        this.theTarget = player.getLastHurtByMob();
        if (player.getLastHurtByMobTimestamp() == this.playerRevengeTimer) {
            return false;
        }
        return LOTRAttackRules.canNPCAttackEntity(this.npc, this.theTarget, true)
                && canAttack(this.theTarget, UNSEEN);
    }

    @Override
    public void start() {
        this.npc.setTarget(this.theTarget);
        Player player = this.npc.hiredNPCInfo.getHiringPlayer();
        if (player != null) {
            this.playerRevengeTimer = player.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}
