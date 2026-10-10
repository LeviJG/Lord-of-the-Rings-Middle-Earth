package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRLionBaseEntity;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAILionChase: once in a long while (1 in 800 per check) an adult
 * lion that is not in love picks an animal within 12 blocks that has no
 * attack of its own and chases it for 15-35 seconds, while the quarry runs.
 * The chase is for show: the lion never bites.
 */
public class LOTRLionChaseGoal extends Goal {

    private final LOTRLionBaseEntity lion;
    private final double speed;
    private @Nullable Animal target;
    private int chaseTimer;
    private int rePathDelay;

    public LOTRLionChaseGoal(LOTRLionBaseEntity lion, double speed) {
        this.lion = lion;
        this.speed = speed;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.lion.isBaby() || this.lion.isInLove() || this.lion.getRandom().nextInt(800) != 0) {
            return false;
        }
        List<Animal> valid = new ArrayList<>();
        for (Animal animal : this.lion.level().getEntitiesOfClass(Animal.class, this.lion.getBoundingBox().inflate(12.0))) {
            if (!animal.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)) {
                valid.add(animal);
            }
        }
        if (valid.isEmpty()) {
            return false;
        }
        this.target = valid.get(this.lion.getRandom().nextInt(valid.size()));
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && this.chaseTimer > 0
                && this.lion.distanceToSqr(this.target) < 256.0;
    }

    @Override
    public void start() {
        this.chaseTimer = 300 + this.lion.getRandom().nextInt(400);
    }

    @Override
    public void stop() {
        this.chaseTimer = 0;
        this.rePathDelay = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        Animal quarry = this.target;
        if (quarry == null) {
            return;
        }
        --this.chaseTimer;
        this.lion.getLookControl().setLookAt(quarry, 30.0f, 30.0f);
        if (--this.rePathDelay <= 0) {
            this.rePathDelay = 10;
            this.lion.getNavigation().moveTo(quarry, this.speed);
        }
        if (quarry.getNavigation().isDone()) {
            Vec3 away = DefaultRandomPos.getPosAway(quarry, 16, 7, this.lion.position());
            if (away != null) {
                quarry.getNavigation().moveTo(away.x, away.y, away.z, 2.0);
            }
        }
    }
}
