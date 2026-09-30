package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRAttackRules;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/** LOTREntityAIHiringPlayerHurtTarget: a hired unit joins in on whatever its player last struck. */
public class LOTRHiringPlayerHurtTargetGoal extends TargetGoal {

    private final LOTRNPCEntity npc;
    private @Nullable LivingEntity theTarget;
    private int playerLastAttackerTime;

    public LOTRHiringPlayerHurtTargetGoal(LOTRNPCEntity npc) {
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
        this.theTarget = player.getLastHurtMob();
        if (player.getLastHurtMobTimestamp() == this.playerLastAttackerTime) {
            return false;
        }
        return LOTRAttackRules.canNPCAttackEntity(this.npc, this.theTarget, true)
                && canAttack(this.theTarget, TargetingConditions.DEFAULT);
    }

    @Override
    public void start() {
        this.npc.setTarget(this.theTarget);
        this.npc.hiredNPCInfo.wasAttackCommanded = true;
        Player player = this.npc.hiredNPCInfo.getHiringPlayer();
        if (player != null) {
            this.playerLastAttackerTime = player.getLastHurtMobTimestamp();
        }
        super.start();
    }
}
