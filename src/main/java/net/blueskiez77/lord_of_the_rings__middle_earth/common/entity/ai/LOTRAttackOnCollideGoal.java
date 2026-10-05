package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIAttackOnCollide: the mod's melee attack. Run at the target,
 * re-path every 10-19 ticks while it can be seen (always, if sight is not
 * required), and hit it whenever it is within the bounding box's average edge
 * plus one block, every 20 ticks.
 *
 * <p>A passive NPC never attacks. The held weapon sets the reach and the
 * time between blows (LOTRWeaponStats, from the weapon's own reach and
 * speed); a rider reaches half as far again. A mount turns its rider with it.
 * An attacker that keeps out of water (a hobbit) wades in after its target.
 * A mounted NPC paths through its mount, as 26.2 steers the mount by its
 * rider.
 *
 * <p>Not ported, by the user's ruling that spears keep vanilla's mechanics:
 * an NPC throwing its spear from 5 blocks out when it has a spare
 * (LOTREntitySpear); 26.2's spears are not thrown.
 */
public class LOTRAttackOnCollideGoal extends Goal {

    private final PathfinderMob owner;
    private final double moveSpeed;
    private final boolean sightNotRequired;
    private @Nullable LivingEntity attackTarget;
    private @Nullable Path path;
    private int attackTick;
    private int pathCheckTimer;
    private float waterMalus;

    public LOTRAttackOnCollideGoal(PathfinderMob owner, double speed, boolean sightNotRequired) {
        this.owner = owner;
        this.moveSpeed = speed;
        this.sightNotRequired = sightNotRequired;
        // setMutexBits(3): movement and looking.
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.owner instanceof LOTRNPCEntity npc && npc.isPassive) {
            return false;
        }
        LivingEntity target = this.owner.getTarget();
        if (target == null) {
            return false;
        }
        this.attackTarget = target;
        // avoidsWater off while it paths at the target.
        this.waterMalus = this.owner.getPathfindingMalus(PathType.WATER);
        this.owner.setPathfindingMalus(PathType.WATER, Math.max(this.waterMalus, 0.0f));
        this.path = this.owner.getNavigation().createPath(target, 0);
        if (this.path != null) {
            return true;
        }
        this.owner.setPathfindingMalus(PathType.WATER, this.waterMalus);
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.owner.isAlive()) {
            return false;
        }
        this.attackTarget = this.owner.getTarget();
        if (this.attackTarget == null || !this.attackTarget.isAlive()) {
            return false;
        }
        if (this.sightNotRequired) {
            return this.owner.isWithinHome(this.attackTarget.blockPosition());
        }
        return !this.owner.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.owner.getNavigation().moveTo(this.path, speed());
        this.pathCheckTimer = 0;
    }

    /**
     * The pace: its own, or -- an NPC on a mount of its own kind -- the mount's
     * own speed (user: a vanilla horse's pace). The original drove the mount at
     * the rider's horse attack speed, 1.7 times that or more
     * (LOTREntityAIHorseMoveToRiderTarget).
     */
    private double speed() {
        if (this.owner instanceof LOTRNPCEntity npc && npc.ridingMount) {
            return 1.0;
        }
        return this.moveSpeed;
    }

    @Override
    public void stop() {
        this.attackTarget = null;
        this.owner.getNavigation().stop();
        this.owner.setPathfindingMalus(PathType.WATER, this.waterMalus);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.attackTarget;
        if (target == null) {
            return;
        }
        this.owner.getLookControl().setLookAt(target, 30.0f, 30.0f);
        if (this.owner.getFirstPassenger() instanceof Mob rider) {
            rider.setYRot(this.owner.getYRot());
            rider.setYHeadRot(this.owner.getYHeadRot());
        }
        if ((this.sightNotRequired || this.owner.getSensing().hasLineOfSight(target)) && --this.pathCheckTimer <= 0) {
            this.pathCheckTimer = 10 + this.owner.getRandom().nextInt(10);
            Path newPath = this.owner.getNavigation().createPath(target, 0);
            if (newPath != null) {
                this.owner.getNavigation().moveTo(newPath, speed());
            }
        }
        if (this.attackTick > 0) {
            --this.attackTick;
        }
        ItemStack weapon = this.owner.getMainHandItem();
        float weaponReach = this.owner.getVehicle() != null ? LOTRNPCEntity.MOUNT_RANGE_BONUS : 1.0f;
        float meleeRange = (float) this.owner.getBoundingBox().getSize()
                + weaponReach * LOTRModifiers.getMeleeReachFactor(weapon);
        if (this.owner.distanceToSqr(target) <= meleeRange * meleeRange && this.attackTick <= 0) {
            this.attackTick = LOTRModifiers.mobAttackTime(weapon);
            if (this.owner.level() instanceof ServerLevel level) {
                this.owner.doHurtTarget(level, target);
            }
            this.owner.swing(InteractionHand.MAIN_HAND);
        }
    }
}
