package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRCorruptMallornBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTREntEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIEntHealSapling: an Ent that is not fighting goes to the nearest
 * corrupt mallorn within 24 blocks, and, standing within three of it, tends it
 * for eight seconds until it is a mallorn sapling again -- giving up if the
 * way takes it more than fifteen seconds.
 *
 * <p>The original searched the world's loaded tile entities; here the loaded
 * chunks within 24 blocks are searched, which finds the same ones.
 */
public class LOTREntHealSaplingGoal extends Goal {

    public static final int HEAL_TIME = 160;
    private static final double SEARCH_RANGE = 24.0;

    private final LOTREntEntity ent;
    private final double moveSpeed;
    private double xPos;
    private double yPos;
    private double zPos;
    private int healingTick;
    private int pathingTick;
    private int rePathDelay;

    public LOTREntHealSaplingGoal(LOTREntEntity ent, double moveSpeed) {
        this.ent = ent;
        this.moveSpeed = moveSpeed;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private @Nullable Vec3 findSapling() {
        Level level = this.ent.level();
        double leastDistSq = SEARCH_RANGE * SEARCH_RANGE;
        BlockPos best = null;
        int minChunkX = SectionPos.blockToSectionCoord(this.ent.getX() - SEARCH_RANGE);
        int maxChunkX = SectionPos.blockToSectionCoord(this.ent.getX() + SEARCH_RANGE);
        int minChunkZ = SectionPos.blockToSectionCoord(this.ent.getZ() - SEARCH_RANGE);
        int maxChunkZ = SectionPos.blockToSectionCoord(this.ent.getZ() + SEARCH_RANGE);
        for (int cx = minChunkX; cx <= maxChunkX; ++cx) {
            for (int cz = minChunkZ; cz <= maxChunkZ; ++cz) {
                if (!(level.getChunk(cx, cz, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, false)
                        instanceof LevelChunk chunk)) {
                    continue;
                }
                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if (!(be instanceof LOTRCorruptMallornBlockEntity)) {
                        continue;
                    }
                    BlockPos pos = be.getBlockPos();
                    double distSq = this.ent.distanceToSqr(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    if (distSq < leastDistSq) {
                        best = pos;
                        leastDistSq = distSq;
                    }
                }
            }
        }
        return best == null ? null : new Vec3(best.getX() + 0.5, best.getY(), best.getZ() + 0.5);
    }

    private BlockPos target() {
        return BlockPos.containing(this.xPos, this.yPos, this.zPos);
    }

    @Override
    public boolean canUse() {
        Vec3 vec;
        if (this.ent.canHealSapling && (vec = findSapling()) != null) {
            this.xPos = vec.x;
            this.yPos = vec.y;
            this.zPos = vec.z;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.ent.canHealSapling) {
            return false;
        }
        if (this.pathingTick < 300 && this.healingTick < HEAL_TIME) {
            return this.ent.level().getBlockState(target()).is(LOTRDecorationBlocks.CORRUPT_MALLORN);
        }
        return false;
    }

    @Override
    public void stop() {
        this.pathingTick = 0;
        this.healingTick = 0;
        this.rePathDelay = 0;
        this.ent.setHealingSapling(false);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.ent.distanceToSqr(this.xPos, this.yPos, this.zPos) > 9.0) {
            this.ent.setHealingSapling(false);
            --this.rePathDelay;
            if (this.rePathDelay <= 0) {
                this.rePathDelay = 10;
                this.ent.getNavigation().moveTo(this.xPos, this.yPos, this.zPos, this.moveSpeed);
            }
            ++this.pathingTick;
        } else {
            this.ent.getNavigation().stop();
            this.ent.getLookControl().setLookAt(this.xPos, this.yPos + 0.5, this.zPos, 10.0f, this.ent.getMaxHeadXRot());
            this.ent.setHealingSapling(true);
            this.ent.saplingHealTarget = target();
            ++this.healingTick;
            if (this.healingTick >= HEAL_TIME) {
                this.ent.level().setBlock(target(), LOTRDecorationBlocks.MALLORN_SAPLING.defaultBlockState(),
                        Block.UPDATE_ALL);
                this.ent.setHealingSapling(false);
            }
        }
    }
}
