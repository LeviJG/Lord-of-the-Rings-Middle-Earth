package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGlowStyle;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityButterfly: flutters from point to point like a bat, and now and
 * then settles on top of a solid block, opening and closing its wings, until
 * something comes within three blocks, it is hurt, or it tires of sitting.
 * Four kinds, each with its own skins; the Lórien butterfly glows and trails
 * the light of a mallorn torch -- which of the four colours is fixed by its
 * UUID, as LOTRWorldGenElfHouse.getRandomTorch seeded from it.
 *
 * <p>NOT ported yet: the kind from the biome it hatched in (Mirkwood and the
 * Woodland Realm, Lothlórien, the Far Harad jungle), which needs the LOTR
 * biomes (D10) -- until then every butterfly is a common one, as in any
 * other biome -- and LOTRAmbientSpawnChecks (D12).
 */
public class LOTRButterflyEntity extends Mob {

    /** dataWatcher 16 and 17. */
    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRButterflyEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_STILL =
            SynchedEntityData.defineId(LOTRButterflyEntity.class, EntityDataSerializers.BOOLEAN);

    private @Nullable BlockPos currentFlightTarget;
    /** Client only: ticks left of a sitting butterfly's wing-beat. */
    public int flapTime;
    private @Nullable LOTRGlowStyle elfTorch;

    public LOTRButterflyEntity(EntityType<? extends LOTRButterflyEntity> type, Level level) {
        super(type, level);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.nextDouble(this.random, 0.08, 0.12));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TYPE, (byte) 0);
        builder.define(DATA_STILL, false);
    }

    /** onSpawnWithEgg: a common butterfly outside the LOTR biomes (see the class note). */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        setButterflyType(ButterflyType.COMMON);
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    public ButterflyType getButterflyType() {
        int i = this.entityData.get(DATA_TYPE);
        return i < 0 || i >= ButterflyType.values().length ? ButterflyType.MIRKWOOD : ButterflyType.values()[i];
    }

    public void setButterflyType(ButterflyType type) {
        this.entityData.set(DATA_TYPE, (byte) type.ordinal());
    }

    public boolean isButterflyStill() {
        return this.entityData.get(DATA_STILL);
    }

    public void setButterflyStill(boolean still) {
        this.entityData.set(DATA_STILL, still);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt && isButterflyStill()) {
            setButterflyStill(false);
        }
        return hurt;
    }

    @Override
    public void tick() {
        super.tick();
        if (isButterflyStill()) {
            setDeltaMovement(Vec3.ZERO);
            setPosRaw(getX(), Mth.floor(getY()), getZ());
            if (level().isClientSide()) {
                if (this.random.nextInt(200) == 0) {
                    this.flapTime = 40;
                }
                if (this.flapTime > 0) {
                    --this.flapTime;
                }
            }
        } else {
            setDeltaMovement(getDeltaMovement().multiply(1.0, 0.6, 1.0));
            if (level().isClientSide()) {
                this.flapTime = 0;
                if (getButterflyType() == ButterflyType.LORIEN) {
                    if (this.elfTorch == null) {
                        this.elfTorch = randomMallornTorch(new Random(getUUID().getLeastSignificantBits()));
                    }
                    this.elfTorch.spawn(level(), this.random, getX(), getY(), getZ());
                }
            }
        }
    }

    /** LOTRWorldGenElfHouse.getRandomTorch. */
    private static LOTRGlowStyle randomMallornTorch(Random random) {
        if (random.nextBoolean()) {
            return switch (random.nextInt(3)) {
                case 0 -> LOTRGlowStyle.MALLORN_BLUE;
                case 1 -> LOTRGlowStyle.MALLORN_GOLD;
                default -> LOTRGlowStyle.MALLORN_GREEN;
            };
        }
        return LOTRGlowStyle.MALLORN_SILVER;
    }

    /** updateAITasks. */
    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        if (isButterflyStill()) {
            BlockPos below = BlockPos.containing(getX(), (int) getY() - 1, getZ());
            if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP) || this.random.nextInt(400) == 0
                    || level.getNearestPlayer(this, 3.0) != null) {
                setButterflyStill(false);
            }
            return;
        }
        BlockPos target = this.currentFlightTarget;
        if (target != null && (!level.isEmptyBlock(target) || target.getY() < 1)) {
            target = null;
        }
        if (target == null || this.random.nextInt(30) == 0
                || target.distSqr(BlockPos.containing((int) getX(), (int) getY(), (int) getZ())) < 4.0) {
            target = BlockPos.containing((int) getX() + this.random.nextInt(7) - this.random.nextInt(7),
                    (int) getY() + this.random.nextInt(6) - 2,
                    (int) getZ() + this.random.nextInt(7) - this.random.nextInt(7));
        }
        this.currentFlightTarget = target;
        double speed = getAttributeValue(Attributes.MOVEMENT_SPEED);
        double dx = target.getX() + 0.5 - getX();
        double dy = target.getY() + 0.5 - getY();
        double dz = target.getZ() + 0.5 - getZ();
        Vec3 m = getDeltaMovement();
        Vec3 next = m.add((Math.signum(dx) * 0.5 - m.x) * speed, (Math.signum(dy) * 0.7 - m.y) * speed,
                (Math.signum(dz) * 0.5 - m.z) * speed);
        setDeltaMovement(next);
        float yaw = (float) (Mth.atan2(next.z, next.x) * 180.0 / Math.PI) - 90.0f;
        this.zza = 0.5f;
        setYRot(getYRot() + Mth.wrapDegrees(yaw - getYRot()));
        BlockPos below = BlockPos.containing(getX(), (int) getY() - 1, getZ());
        if (this.random.nextInt(150) == 0 && level.getBlockState(below).isRedstoneConductor(level, below)) {
            setButterflyStill(true);
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    /** canDespawn: true, as an ambient creature. */
    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return true;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("ButterflyType", getButterflyType().ordinal());
        output.putBoolean("ButterflyStill", isButterflyStill());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(DATA_TYPE, (byte) input.getIntOr("ButterflyType", 0));
        setButterflyStill(input.getBooleanOr("ButterflyStill", false));
    }

    public enum ButterflyType {
        MIRKWOOD("mirkwood"), LORIEN("lorien"), COMMON("common"), JUNGLE("jungle");

        public final String textureDir;

        ButterflyType(String dir) {
            this.textureDir = dir;
        }
    }
}
