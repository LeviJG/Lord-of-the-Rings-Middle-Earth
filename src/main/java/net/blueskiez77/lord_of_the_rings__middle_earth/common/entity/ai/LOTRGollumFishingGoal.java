package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGollumEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIGollumFishing: a tamed Gollum, not told to stay, empty-handed
 * and rested, now and then goes to water within 16 blocks and splashes about
 * in it for five seconds, muttering to his owner, and comes up with four to
 * twelve fish -- which he then brings back and drops at their feet. He rests
 * two and a half minutes after a catch, half a minute after giving up (fifteen
 * seconds without getting there).
 */
public class LOTRGollumFishingGoal extends Goal {

    private final LOTRGollumEntity gollum;
    private final double moveSpeed;
    private final Level level;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private int moveTick;
    private int fishTick;
    private boolean finished;
    private float waterMalus;

    public LOTRGollumFishingGoal(LOTRGollumEntity gollum, double speed) {
        this.gollum = gollum;
        this.moveSpeed = speed;
        this.level = gollum.level();
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean atFishingLocation() {
        if (this.gollum.distanceToSqr(this.xPosition, this.yPosition, this.zPosition) < 4.0) {
            BlockPos pos = BlockPos.containing(this.gollum.getX(), this.gollum.getBoundingBox().minY, this.gollum.getZ());
            return this.level.getFluidState(pos).is(FluidTags.WATER)
                    || this.level.getFluidState(pos.below()).is(FluidTags.WATER);
        }
        return false;
    }

    private @Nullable Vec3 findPossibleFishingLocation() {
        RandomSource random = this.gollum.getRandom();
        for (int l = 0; l < 32; ++l) {
            int i = Mth.floor(this.gollum.getX()) - 16 + random.nextInt(33);
            int j = Mth.floor(this.gollum.getBoundingBox().minY) - 8 + random.nextInt(17);
            int k = Mth.floor(this.gollum.getZ()) - 16 + random.nextInt(33);
            BlockPos pos = new BlockPos(i, j, k);
            if (this.level.getBlockState(pos.above()).isRedstoneConductor(this.level, pos.above())
                    || this.level.getBlockState(pos).isRedstoneConductor(this.level, pos)
                    || !this.level.getFluidState(pos.below()).is(FluidTags.WATER)) {
                continue;
            }
            return new Vec3(i + 0.5, j + 0.5, k + 0.5);
        }
        return null;
    }

    @Override
    public boolean canUse() {
        if (this.gollum.getGollumOwner() == null || this.gollum.isGollumSitting() || this.gollum.prevFishTime > 0
                || this.gollum.isFishing) {
            return false;
        }
        if (!this.gollum.getMainHandItem().isEmpty()) {
            return false;
        }
        if (this.gollum.getRandom().nextInt(60) == 0) {
            Vec3 vec = findPossibleFishingLocation();
            if (vec == null) {
                return false;
            }
            this.xPosition = vec.x;
            this.yPosition = vec.y;
            this.zPosition = vec.z;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.gollum.getGollumOwner() != null && !this.gollum.isGollumSitting() && this.moveTick < 300
                && !this.finished;
    }

    @Override
    public void start() {
        this.waterMalus = this.gollum.getPathfindingMalus(PathType.WATER);
        this.gollum.setPathfindingMalus(PathType.WATER, 0.0f);
        this.gollum.isFishing = true;
    }

    @Override
    public void stop() {
        this.gollum.getNavigation().stop();
        this.gollum.setPathfindingMalus(PathType.WATER, this.waterMalus);
        this.moveTick = 0;
        this.fishTick = 0;
        if (this.finished) {
            this.finished = false;
            this.gollum.prevFishTime = 3000;
        } else {
            this.gollum.prevFishTime = 600;
        }
        this.gollum.isFishing = false;
    }

    @Override
    public void tick() {
        Player owner = this.gollum.getGollumOwner();
        if (atFishingLocation()) {
            if (this.gollum.isInWater()) {
                this.level.broadcastEntityEvent(this.gollum, LOTRGollumEntity.EVENT_SPLASH);
                RandomSource random = this.gollum.getRandom();
                if (random.nextInt(4) == 0) {
                    this.gollum.playSound(this.gollum.getSwimSplashSoundPublic(), 1.0f,
                            1.0f + (random.nextFloat() - random.nextFloat()) * 0.4f);
                }
                this.gollum.getJumpControl().jump();
                if (random.nextInt(50) == 0 && owner != null) {
                    LOTRSpeech.sendSpeech(owner, this.gollum, LOTRSpeech.getRandomSpeechForPlayer(this.gollum,
                            "char/gollum/fishing", owner, null, null));
                }
            }
            ++this.fishTick;
            if (this.fishTick > 100) {
                this.gollum.setItemSlot(EquipmentSlot.MAINHAND,
                        new ItemStack(Items.COD, 4 + this.gollum.getRandom().nextInt(9)));
                this.finished = true;
                if (owner != null) {
                    LOTRSpeech.sendSpeech(owner, this.gollum, LOTRSpeech.getRandomSpeechForPlayer(this.gollum,
                            "char/gollum/catchFish", owner, null, null));
                }
            }
        } else {
            this.gollum.getNavigation().moveTo(this.xPosition, this.yPosition, this.zPosition, this.moveSpeed);
            ++this.moveTick;
        }
    }
}
