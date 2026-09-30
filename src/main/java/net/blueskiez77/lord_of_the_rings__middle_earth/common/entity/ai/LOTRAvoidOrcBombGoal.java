package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTROrcBombEntity;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * EntityAIAvoidEntity(LOTREntityOrcBomb): run from a lit orc bomb, faster once
 * within seven blocks. Vanilla's AvoidEntityGoal only flees living things,
 * and a lit bomb is not one.
 */
public class LOTRAvoidOrcBombGoal extends Goal {

    private final PathfinderMob mob;
    private final float distance;
    private final double farSpeed;
    private final double nearSpeed;
    private @Nullable LOTROrcBombEntity bomb;
    private @Nullable Path path;

    public LOTRAvoidOrcBombGoal(PathfinderMob mob, float distance, double farSpeed, double nearSpeed) {
        this.mob = mob;
        this.distance = distance;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        List<LOTROrcBombEntity> bombs = this.mob.level().getEntitiesOfClass(LOTROrcBombEntity.class,
                this.mob.getBoundingBox().inflate(this.distance, 3.0, this.distance));
        if (bombs.isEmpty()) {
            return false;
        }
        this.bomb = bombs.get(0);
        Vec3 flee = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.bomb.position());
        if (flee == null || this.bomb.distanceToSqr(flee) < this.bomb.distanceToSqr(this.mob)) {
            return false;
        }
        this.path = this.mob.getNavigation().createPath(flee.x, flee.y, flee.z, 0);
        return this.path != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.farSpeed);
    }

    @Override
    public void stop() {
        this.bomb = null;
    }

    @Override
    public void tick() {
        if (this.bomb != null) {
            this.mob.getNavigation().setSpeedModifier(
                    this.mob.distanceToSqr(this.bomb) < 49.0 ? this.nearSpeed : this.farSpeed);
        }
    }
}
