package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRTrollEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAITrollFleeSun: a troll caught under the open sky by day starts
 * to burn -- 300 ticks to turn to stone -- and runs for shelter: a spot
 * within 24 blocks (12 up or down) out of the sky and darker than half light,
 * or failing that anywhere within 12.
 *
 * <p>NOT ported yet: the biomes where hostiles walk by day, which the sun
 * does not trouble (LOTRBiome.canSpawnHostilesInDay, with the biomes, D10).
 */
public class LOTRTrollFleeSunGoal extends Goal {

    private final LOTRTrollEntity troll;
    private final double moveSpeed;
    private double x;
    private double y;
    private double z;

    public LOTRTrollFleeSunGoal(LOTRTrollEntity troll, double moveSpeed) {
        this.troll = troll;
        this.moveSpeed = moveSpeed;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        Level level = this.troll.level();
        BlockPos pos = BlockPos.containing(this.troll.getX(), this.troll.getBoundingBox().minY, this.troll.getZ());
        if (!level.isBrightOutside() || !level.canSeeSky(pos) || this.troll.trollImmuneToSun) {
            return false;
        }
        if (this.troll.getTrollBurnTime() == -1) {
            this.troll.setTrollBurnTime(300);
        }
        Vec3 target = findPossibleShelter();
        if (target == null && (target = DefaultRandomPos.getPos(this.troll, 12, 6)) == null) {
            return false;
        }
        this.x = target.x;
        this.y = target.y;
        this.z = target.z;
        return true;
    }

    private @Nullable Vec3 findPossibleShelter() {
        RandomSource random = this.troll.getRandom();
        Level level = this.troll.level();
        for (int l = 0; l < 32; ++l) {
            int i = Mth.floor(this.troll.getX()) - 24 + random.nextInt(49);
            int j = Mth.floor(this.troll.getBoundingBox().minY) - 12 + random.nextInt(25);
            int k = Mth.floor(this.troll.getZ()) - 24 + random.nextInt(49);
            BlockPos pos = new BlockPos(i, j, k);
            if (level.canSeeSky(pos) || this.troll.getWalkTargetValue(pos, level) >= 0.0f) {
                continue;
            }
            return new Vec3(i, j, k);
        }
        return null;
    }

    @Override
    public void start() {
        this.troll.getNavigation().moveTo(this.x, this.y, this.z, this.moveSpeed);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.troll.getNavigation().isDone();
    }
}
