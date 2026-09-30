package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMarshWraithBall: a marsh wraith's cold light, floating straight
 * at the one it was cast at (and passing through anything else) for ten
 * seconds, a ghostly sixteen-frame flicker. It strikes only its mark: 5
 * damage and five seconds' slowness. Striking a block, it goes out.
 */
public class LOTRMarshWraithBallEntity extends ThrowableProjectile {

    private static final EntityDataAccessor<Integer> DATA_AGE =
            SynchedEntityData.defineId(LOTRMarshWraithBallEntity.class, EntityDataSerializers.INT);

    /** Client side: the sprite frame, 0 to 15. */
    public int animationTick;
    private @Nullable Entity attackTarget;

    public LOTRMarshWraithBallEntity(EntityType<? extends LOTRMarshWraithBallEntity> type, Level level) {
        super(type, level);
    }

    /** Cast from just under the caster's eyes, at the middle of the target, at half a block a tick. */
    public LOTRMarshWraithBallEntity(Level level, LivingEntity caster, LivingEntity target) {
        this(LOTREntities.MARSH_WRAITH_BALL, level);
        setOwner(caster);
        this.attackTarget = target;
        double y = caster.getBoundingBox().minY + caster.getEyeHeight() - 0.1;
        setPos(caster.getX(), y, caster.getZ());
        double dx = target.getX() - getX();
        double dy = target.getBoundingBox().minY + target.getBbHeight() / 2.0f - y;
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        if (horizontal >= 1.0E-7) {
            snapTo(getX(), y, getZ(), (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0f,
                    (float) -(Mth.atan2(dy, horizontal) * Mth.RAD_TO_DEG));
            shoot(dx, dy, dz, 0.5f, 1.0f);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_AGE, 0);
    }

    public int getBallAge() {
        return this.entityData.get(DATA_AGE);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && entity == this.attackTarget;
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        if (!level().isClientSide()) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        Entity entity = hit.getEntity();
        if (level() instanceof ServerLevel level && entity == this.attackTarget) {
            if (entity.hurtServer(level, damageSources().thrown(this, getOwner()), 5.0f)
                    && entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 5 * 20, 0), this);
            }
            discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 2 == 0) {
            ++this.animationTick;
            if (this.animationTick >= 16) {
                this.animationTick = 0;
            }
        }
        if (!level().isClientSide()) {
            this.entityData.set(DATA_AGE, getBallAge() + 1);
            if (getBallAge() >= 200) {
                discard();
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("BallAge", getBallAge());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(DATA_AGE, input.getIntOr("BallAge", 0));
    }
}
