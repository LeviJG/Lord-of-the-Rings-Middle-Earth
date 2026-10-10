package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIBurningPanic: an NPC on fire with no one to fight runs for
 * water within eight blocks, or anywhere at all -- into the water even if it
 * otherwise keeps out of it.
 */
public class LOTRBurningPanicGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;
    private double x;
    private double y;
    private double z;
    private float waterMalus;

    public LOTRBurningPanicGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    private @Nullable Vec3 findWaterLocation() {
        BlockPos base = BlockPos.containing(this.mob.getX(), this.mob.getBoundingBox().minY, this.mob.getZ());
        for (int l = 0; l < 32; ++l) {
            BlockPos pos = base.offset(Mth.nextInt(this.mob.getRandom(), -8, 8), Mth.nextInt(this.mob.getRandom(), -8, 8),
                    Mth.nextInt(this.mob.getRandom(), -8, 8));
            var level = this.mob.level();
            if (level.getBlockState(pos.above()).isRedstoneConductor(level, pos.above())
                    || level.getBlockState(pos).isRedstoneConductor(level, pos)
                    || !level.getFluidState(pos.below()).is(Fluids.WATER) && !level.getFluidState(pos.below()).is(Fluids.FLOWING_WATER)) {
                continue;
            }
            return Vec3.atCenterOf(pos);
        }
        return null;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isOnFire() && this.mob.getTarget() == null) {
            Vec3 target = findWaterLocation();
            if (target == null) {
                target = DefaultRandomPos.getPos(this.mob, 5, 4);
            }
            if (target != null) {
                this.x = target.x;
                this.y = target.y;
                this.z = target.z;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isOnFire() && this.mob.getTarget() == null && !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.waterMalus = this.mob.getPathfindingMalus(PathType.WATER);
        this.mob.setPathfindingMalus(PathType.WATER, Math.max(this.waterMalus, 0.0f));
        this.mob.getNavigation().moveTo(this.x, this.y, this.z, this.speed);
    }

    @Override
    public void stop() {
        this.mob.setPathfindingMalus(PathType.WATER, this.waterMalus);
    }
}
