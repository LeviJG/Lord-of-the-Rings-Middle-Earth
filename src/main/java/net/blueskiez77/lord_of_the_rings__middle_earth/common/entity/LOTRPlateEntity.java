package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityPlate: a thrown plate.
 *
 * <p>onImpact, transcribed. A mob takes one point of thrown damage. Glass it
 * hits shatters, and so does most of the glass within two blocks (three in
 * four), and the plate flies on. Anything else, it smashes -- shards and its
 * break sound. It spins a full turn every twelve ticks, and skims off water
 * when it comes in low and fast.
 *
 * <p>The plate is whichever of the three the item was, read back off the item
 * the entity carries rather than a block ID in its spawn data.
 *
 * <p>NOT ported: banner protection (no banners that protect in the port yet).
 */
public class LOTRPlateEntity extends ThrowableItemProjectile {
    /** func_70182_d. */
    public static final float SPEED = 1.5f;
    /** getGravityVelocity. */
    private static final double GRAVITY = 0.02;
    private static final byte SMASH = 3;

    private int plateSpin;

    public LOTRPlateEntity(EntityType<? extends LOTRPlateEntity> type, Level level) {
        super(type, level);
    }

    public LOTRPlateEntity(EntityType<? extends LOTRPlateEntity> type, LivingEntity thrower, Level level,
            net.minecraft.world.item.ItemStack stack) {
        super(type, thrower, level, stack);
    }

    public LOTRPlateEntity(EntityType<? extends LOTRPlateEntity> type, Level level, double x, double y, double z,
            net.minecraft.world.item.ItemStack stack) {
        super(type, x, y, z, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return LOTRFoodBlocks.FINE_PLATE.asItem();
    }

    @Override
    protected double getDefaultGravity() {
        return GRAVITY;
    }

    public Block getPlateBlock() {
        return getItem().getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : LOTRFoodBlocks.FINE_PLATE;
    }

    @Override
    public void tick() {
        super.tick();
        if (isRemoved()) {
            return;
        }
        ++plateSpin;
        setYRot(plateSpin % 12 / 12.0f * 360.0f);
        Vec3 motion = getDeltaMovement();
        double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        if (speed > 0.1 && motion.y < 0.0 && isInWater()) {
            float factor = Mth.randomBetween(this.random, 0.4f, 0.8f);
            setDeltaMovement(motion.x * factor, motion.y + factor, motion.z * factor);
        }
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!(level() instanceof ServerLevel server)) {
            return;
        }
        if (hit instanceof EntityHitResult entityHit) {
            if (entityHit.getEntity() == getOwner()) {
                return;
            }
            entityHit.getEntity().hurtServer(server, damageSources().thrown(this, getOwner()), 1.0f);
        } else if (hit instanceof BlockHitResult blockHit && breakGlass(server, blockHit.getBlockPos())) {
            BlockPos centre = blockHit.getBlockPos();
            int range = 2;
            for (int dx = -range; dx <= range; ++dx) {
                for (int dy = -range; dy <= range; ++dy) {
                    for (int dz = -range; dz <= range; ++dz) {
                        if (this.random.nextInt(4) == 0) {
                            continue;
                        }
                        breakGlass(server, centre.offset(dx, dy, dz));
                    }
                }
            }
            return;
        }
        server.broadcastEntityEvent(this, SMASH);
        SoundType sound = getPlateBlock().defaultBlockState().getSoundType();
        playSound(sound.getBreakSound(), 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        discard();
    }

    /** breakGlass: Material.glass, which is what sounds like glass now. */
    private static boolean breakGlass(ServerLevel level, BlockPos pos) {
        if (level.getBlockState(pos).getSoundType() == SoundType.GLASS) {
            level.destroyBlock(pos, false);
            return true;
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id != SMASH) {
            super.handleEntityEvent(id);
            return;
        }
        BlockParticleOption shards = new BlockParticleOption(ParticleTypes.BLOCK, getPlateBlock().defaultBlockState());
        for (int i = 0; i < 8; ++i) {
            level().addParticle(shards,
                    getX() + Mth.randomBetween(this.random, -0.25f, 0.25f),
                    getY() + Mth.randomBetween(this.random, -0.25f, 0.25f),
                    getZ() + Mth.randomBetween(this.random, -0.25f, 0.25f), 0.0, 0.0, 0.0);
        }
    }
}
