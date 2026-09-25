package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRKhamulsFireJarBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * LOTREntityFirePot: a thrown pot that bursts into fire.
 *
 * <p>onImpact, transcribed: everything living within three blocks takes one
 * point of thrown damage and burns for two to four seconds; the thing it struck
 * directly takes three and burns two to four seconds longer. A pot that lands
 * on one of Khamul's fire jars sets the jar off. Then it smashes -- shards of
 * the jar's own texture and a puff of flame and smoke.
 *
 * <p>NOT ported: the achievement for downing a bird in flight, and the
 * plate-breaking sound, which the port has no sound event for; a decorated
 * pot's shatter stands in for it.
 */
public class LOTRFirePotEntity extends ThrowableItemProjectile {

    /** getGravityVelocity, against a snowball's 0.03. */
    private static final double GRAVITY = 0.04;

    private static final double RANGE = 3.0;
    private static final float DAMAGE_SPLASH = 1.0f;
    private static final float DAMAGE_DIRECT = 3.0f;

    /** The entity event that plays the smash on clients, as a snowball's does. */
    private static final byte SMASH = 3;

    public LOTRFirePotEntity(EntityType<? extends LOTRFirePotEntity> type, Level level) {
        super(type, level);
    }

    public LOTRFirePotEntity(EntityType<? extends LOTRFirePotEntity> type, LivingEntity thrower,
            Level level, ItemStack stack) {
        super(type, thrower, level, stack);
    }

    public LOTRFirePotEntity(EntityType<? extends LOTRFirePotEntity> type, Level level,
            double x, double y, double z, ItemStack stack) {
        super(type, x, y, z, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return LOTRItems.RHUNIC_FIRE_POT;
    }

    @Override
    protected double getDefaultGravity() {
        return GRAVITY;
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!(level() instanceof ServerLevel server)) {
            return;
        }
        Entity struck = hit instanceof EntityHitResult entityHit ? entityHit.getEntity() : null;
        List<LivingEntity> targets = new ArrayList<>(
                server.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(RANGE)));
        if (struck instanceof LivingEntity living && !targets.contains(living)) {
            targets.add(living);
        }
        for (LivingEntity target : targets) {
            boolean direct = target == struck;
            if (!target.hurtServer(server, damageSources().thrown(this, getOwner()),
                    direct ? DAMAGE_DIRECT : DAMAGE_SPLASH)) {
                continue;
            }
            int fire = 2 + this.random.nextInt(3);
            if (direct) {
                fire += 2 + this.random.nextInt(3);
            }
            target.igniteForSeconds(fire);
        }
        if (hit instanceof BlockHitResult blockHit
                && server.getBlockState(blockHit.getBlockPos()).getBlock() instanceof LOTRKhamulsFireJarBlock jar) {
            jar.explode(server, blockHit.getBlockPos());
        }
        // LOTRBlockPlate.soundTypePlate's break sound, as the original played.
        playSound(LOTRSounds.BLOCK_PLATE_BREAK, 1.0f,
                (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        server.broadcastEntityEvent(this, SMASH);
        discard();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id != SMASH) {
            super.handleEntityEvent(id);
            return;
        }
        BlockParticleOption shards = new BlockParticleOption(ParticleTypes.BLOCK,
                LOTRBlocks.KHAMULS_FIRE_JAR.defaultBlockState());
        for (int i = 0; i < 8; i++) {
            level().addParticle(shards,
                    getX() + Mth.randomBetween(this.random, -0.25f, 0.25f),
                    getY() + Mth.randomBetween(this.random, -0.25f, 0.25f),
                    getZ() + Mth.randomBetween(this.random, -0.25f, 0.25f), 0.0, 0.0, 0.0);
        }
        for (int i = 0; i < 16; i++) {
            level().addParticle(this.random.nextBoolean() ? ParticleTypes.FLAME : ParticleTypes.SMOKE,
                    getX(), getY(), getZ(),
                    Mth.randomBetween(this.random, -0.1f, 0.1f),
                    Mth.randomBetween(this.random, 0.2f, 0.3f),
                    Mth.randomBetween(this.random, -0.1f, 0.1f));
        }
    }
}
