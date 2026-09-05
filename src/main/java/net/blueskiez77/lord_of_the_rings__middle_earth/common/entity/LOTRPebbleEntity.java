package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityPebble: a stone, thrown by hand or slung.
 *
 * <p>It hurts for one thrown and two out of a sling, and either way the stone
 * survives -- onImpact dropped it back on the ground as an item, so a pebble is
 * something you throw, walk over to, and pick up again. Gravity is 0.04 rather
 * than a snowball's 0.03, and it leaves the hand at 1.0 where a snowball
 * manages 1.5: a stone is heavier and does not go as far.
 *
 * <p>And it SKIPS. The original checked, every tick, whether the stone was
 * moving horizontally, falling, and in water, and if so bled off the horizontal
 * speed and pushed it back up -- a stone skimmed across a pond bouncing off the
 * surface. That is transcribed below, factor for factor.
 */
public class LOTRPebbleEntity extends ThrowableItemProjectile {

    /** func_70182_d: the speed a thrown pebble leaves at. */
    public static final float THROW_VELOCITY = 1.0f;

    /** getGravityVelocity, against a snowball's 0.03. */
    private static final double GRAVITY = 0.04;

    private static final float DAMAGE_THROWN = 1.0f;
    private static final float DAMAGE_SLUNG = 2.0f;

    /** LOTRItemSling.setSling: a slung stone hits twice as hard as a thrown one. */
    private boolean slung;

    public LOTRPebbleEntity(EntityType<? extends LOTRPebbleEntity> type, Level level) {
        super(type, level);
    }

    public LOTRPebbleEntity(EntityType<? extends LOTRPebbleEntity> type, LivingEntity thrower,
            Level level, ItemStack stack) {
        super(type, thrower, level, stack);
    }

    public LOTRPebbleEntity(EntityType<? extends LOTRPebbleEntity> type, Level level,
            double x, double y, double z, ItemStack stack) {
        super(type, x, y, z, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return LOTRItems.PEBBLE;
    }

    @Override
    protected double getDefaultGravity() {
        return GRAVITY;
    }

    public boolean isSlung() {
        return this.slung;
    }

    public LOTRPebbleEntity setSlung() {
        this.slung = true;
        return this;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Sling", this.slung);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.slung = input.getBooleanOr("Sling", false);
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (this.level() instanceof ServerLevel server) {
            hit.getEntity().hurtServer(server, damageSources().thrown(this, getOwner()),
                    this.slung ? DAMAGE_SLUNG : DAMAGE_THROWN);
        }
    }

    /**
     * onImpact: whatever it hit, the stone drops where it landed and is gone as
     * a projectile. Anything can pick it up again, which is the whole economy
     * of the thing -- a pebble is never really spent.
     */
    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (this.level() instanceof ServerLevel server) {
            spawnAtLocation(server, new ItemStack(LOTRItems.PEBBLE));
            discard();
        }
    }

    /**
     * onUpdate's skip. A stone travelling horizontally, on its way down, that
     * finds itself in water loses some of its run and gains that much height
     * back -- a fresh roll between 0.4 and 0.8 each time, so no two skips are
     * alike and a flat throw can bounce several times before it sinks.
     */
    @Override
    public void tick() {
        super.tick();
        Vec3 motion = getDeltaMovement();
        double horizontal = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        if (horizontal > 0.1 && motion.y < 0.0 && isInWater()) {
            float factor = Mth.randomBetween(this.random, 0.4f, 0.8f);
            setDeltaMovement(motion.x * factor, motion.y + factor, motion.z * factor);
        }
    }
}
