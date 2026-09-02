package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityBossTrophy: a boss's remains, set on the floor or hung on a wall.
 *
 * <p>One hit from a player takes it down and gives the item back, unless they
 * are in creative. A floor trophy falls under the original's own gravity, the
 * same figures the stone troll uses; a hanging one does not fall at all, but
 * drops itself the moment the wall behind it goes.
 */
public class LOTRBossTrophyEntity extends Entity {

    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRBossTrophyEntity.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Boolean> DATA_HANGING =
            SynchedEntityData.defineId(LOTRBossTrophyEntity.class, EntityDataSerializers.BOOLEAN);

    /** Which way a hanging trophy faces, i.e. away from its wall. */
    private static final EntityDataAccessor<Byte> DATA_FACING =
            SynchedEntityData.defineId(LOTRBossTrophyEntity.class, EntityDataSerializers.BYTE);

    public LOTRBossTrophyEntity(EntityType<? extends LOTRBossTrophyEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TYPE, (byte) 0);
        builder.define(DATA_HANGING, false);
        builder.define(DATA_FACING, (byte) Direction.NORTH.get2DDataValue());
    }

    public LOTRTrophyType getTrophyType() {
        return LOTRTrophyType.byIndex(this.entityData.get(DATA_TYPE));
    }

    public void setTrophyType(LOTRTrophyType type) {
        this.entityData.set(DATA_TYPE, (byte) type.ordinal());
    }

    public boolean isTrophyHanging() {
        return this.entityData.get(DATA_HANGING);
    }

    public void setTrophyHanging(boolean hanging) {
        this.entityData.set(DATA_HANGING, hanging);
    }

    public Direction getTrophyFacing() {
        return Direction.from2DDataValue(this.entityData.get(DATA_FACING));
    }

    public void setTrophyFacing(Direction facing) {
        this.entityData.set(DATA_FACING, (byte) facing.get2DDataValue());
    }

    @Override
    public boolean canBeCollidedWith(Entity entity) {
        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public ItemStack getPickResult() {
        return getTrophyItem();
    }

    public ItemStack getTrophyItem() {
        return new ItemStack(LOTRItems.trophyItem(getTrophyType()));
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.isRemoved() || !(source.getDirectEntity() instanceof Player player)) {
            return false;
        }
        dropAsItem(level, !player.getAbilities().instabuild);
        return true;
    }

    public void dropAsItem(ServerLevel level, boolean dropItem) {
        level.playSound(null, this.getX(), this.getY(), this.getZ(),
                Blocks.STONE.defaultBlockState().getSoundType().getBreakSound(),
                SoundSource.BLOCKS,
                (Blocks.STONE.defaultBlockState().getSoundType().getVolume() + 1.0f) / 2.0f,
                Blocks.STONE.defaultBlockState().getSoundType().getPitch() * 0.8f);
        if (dropItem) {
            this.spawnAtLocation(level, getTrophyItem());
        }
        this.discard();
    }

    /** hangingOnValidSurface: a solid face on the block the trophy backs onto. */
    public boolean hangingOnValidSurface() {
        if (!isTrophyHanging()) {
            return false;
        }
        Direction behind = getTrophyFacing().getOpposite();
        BlockPos wall = BlockPos.containing(this.getX(), this.getBoundingBox().minY, this.getZ())
                .relative(behind);
        return this.level().getBlockState(wall)
                .isFaceSturdy(this.level(), wall, getTrophyFacing());
    }

    @Override
    public void tick() {
        super.tick();

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        if (isTrophyHanging()) {
            if (!this.level().isClientSide() && !this.isRemoved() && !hangingOnValidSurface()) {
                dropAsItem((ServerLevel) this.level(), true);
            }
            return;
        }

        // The same gravity and friction the stone troll uses, transcribed from
        // the original rather than replaced with applyGravity().
        this.setDeltaMovement(this.getDeltaMovement().subtract(0.0, 0.04, 0.0));
        this.move(MoverType.SELF, this.getDeltaMovement());

        float friction = 0.98f;
        if (this.onGround()) {
            BlockPos below = BlockPos.containing(this.getX(),
                    Mth.floor(this.getBoundingBox().minY) - 1, this.getZ());
            BlockState state = this.level().getBlockState(below);
            friction = state.isAir() ? 0.588f : state.getBlock().getFriction() * 0.98f;
        }

        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.x * friction, motion.y * 0.98, motion.z * friction);
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, -0.5, 1.0));
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.entityData.set(DATA_TYPE, input.getByteOr("TrophyType", (byte) 0));
        setTrophyHanging(input.getBooleanOr("TrophyHanging", false));
        setTrophyFacing(Direction.from2DDataValue(input.getByteOr("TrophyFacing", (byte) 0)));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putByte("TrophyType", (byte) getTrophyType().ordinal());
        output.putBoolean("TrophyHanging", isTrophyHanging());
        output.putByte("TrophyFacing", (byte) getTrophyFacing().get2DDataValue());
    }
}
