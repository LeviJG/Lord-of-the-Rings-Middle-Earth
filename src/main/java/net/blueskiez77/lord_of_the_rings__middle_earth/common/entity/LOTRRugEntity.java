package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRugBase: an animal's skin laid on the floor. It falls and slides
 * like a dropped item, breaks at any hit -- giving its rug back outside
 * creative, with wool's break sound -- and every few minutes lets out the
 * growl of the animal it was.
 *
 * <p>NOT ported yet: LOTRBannerProtectable (banner protection, D14).
 */
public abstract class LOTRRugEntity extends Entity {

    private int timeSinceLastGrowl = getTimeUntilGrowl();

    protected LOTRRugEntity(EntityType<? extends LOTRRugEntity> type, Level level) {
        super(type, level);
    }

    /** The rug item this is, for breaking and pick-block. */
    public abstract ItemStack getRugItem();

    /** The animal's growl, or null for a rug that never growls. */
    public abstract @Nullable SoundEvent getRugNoise();

    private int getTimeUntilGrowl() {
        return (60 + this.random.nextInt(150)) * 20;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (!isRemoved()) {
            SoundType wool = SoundType.WOOL;
            playSound(wool.getBreakSound(), (wool.getVolume() + 1.0f) / 2.0f, wool.getPitch() * 0.8f);
            boolean creative = source.getEntity() instanceof Player player && player.getAbilities().instabuild;
            if (!creative) {
                spawnAtLocation(level, getRugItem(), 0.0f);
            }
            discard();
        }
        return true;
    }

    /** canBeCollidedWith: it can be clicked. */
    @Override
    public boolean isPickable() {
        return true;
    }

    /** getBoundingBox returned its box: solid to stand on and walk into, as a boat is. */
    @Override
    public boolean canBeCollidedWith(@Nullable Entity entity) {
        return true;
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        return getRugItem();
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = getDeltaMovement().add(0.0, -0.04, 0.0);
        // func_145771_j only when inside a block (see LOTRStoneTrollEntity.tick).
        if (!level().noCollision(this, getBoundingBox().deflate(1.0E-7))) {
            moveTowardsClosestSpace(getX(), (getBoundingBox().minY + getBoundingBox().maxY) / 2.0, getZ());
        }
        setDeltaMovement(motion);
        move(MoverType.SELF, motion);
        float friction = 0.98f;
        if (onGround()) {
            friction = 0.588f;
            BlockState below = level().getBlockState(BlockPos.containing(getX(), Math.floor(getBoundingBox().minY) - 1, getZ()));
            if (!below.isAir()) {
                friction = below.getBlock().getFriction() * 0.98f;
            }
        }
        motion = getDeltaMovement().multiply(friction, 0.98, friction);
        if (onGround()) {
            motion = motion.multiply(1.0, -0.5, 1.0);
        }
        setDeltaMovement(motion);
        if (level() instanceof ServerLevel) {
            if (this.timeSinceLastGrowl > 0) {
                --this.timeSinceLastGrowl;
            } else if (this.random.nextInt(5000) == 0) {
                SoundEvent noise = getRugNoise();
                if (noise != null) {
                    playSound(noise, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                }
                this.timeSinceLastGrowl = getTimeUntilGrowl();
            }
        }
    }

    /** Places the rug as LOTRItemRugBase did: yaw faces the player. */
    public void placeAt(double x, double y, double z, Player player) {
        snapTo(x, y, z, 180.0f - player.getYRot() % 360.0f, 0.0f);
    }
}
