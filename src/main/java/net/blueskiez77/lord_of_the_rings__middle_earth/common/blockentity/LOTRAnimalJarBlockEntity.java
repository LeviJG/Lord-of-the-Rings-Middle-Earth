package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRAnimalJarBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRAnimalJarUpdater;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRButterflyEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityAnimalJar: the creature a bird cage or butterfly jar holds.
 *
 * <p>What is kept is the creature's own NBT ("JarEntity"); from it each side
 * builds a live copy that is never added to the level, as the original did.
 * Each tick the copy ages with the jar and is told it is in a jar (a bird
 * never perches there). On the server, a caught mob makes its ambient sounds,
 * and one tick in two hundred turns to a new heading, which the clients ease
 * towards a tenth of the way a tick. A Lórien butterfly lights its jar to 7
 * (the jar's {@link LOTRAnimalJarBlock#LIT}).
 */
public class LOTRAnimalJarBlockEntity extends BlockEntity {

    /** The tag the original used, kept so the shape of the data is familiar. */
    public static final String ENTITY_TAG = "JarEntity";
    private static final String TARGET_YAW_TAG = "TargetYaw";

    private @Nullable CompoundTag entityData;
    private @Nullable Entity jarEntity;
    private int ticksExisted = -1;
    private float targetYaw;
    private boolean hasTargetYaw;
    private boolean sendingTargetYaw;

    public LOTRAnimalJarBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.ANIMAL_JAR, pos, state);
    }

    public @Nullable CompoundTag getEntityData() {
        return entityData == null ? null : entityData.copy();
    }

    public boolean isEmpty() {
        return entityData == null || entityData.isEmpty();
    }

    public void setEntityData(@Nullable CompoundTag data) {
        this.entityData = data == null || data.isEmpty() ? null : data.copy();
        this.jarEntity = null;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public int getTicksExisted() {
        return Math.max(this.ticksExisted, 0);
    }

    /** getEntityBobbing. */
    public float getEntityBobbing(float partialTick) {
        return Mth.sin((getTicksExisted() + partialTick) * 0.2f) * 0.05f;
    }

    /** getJarEntityHeight: the middle of a bird cage, a quarter of the way up a butterfly jar. */
    public float getEntityHeight() {
        return LOTRBlocks.ALL_BIRD_CAGES.contains(getBlockState().getBlock()) ? 0.5f : 0.25f;
    }

    /** isEntityWatching: a butterfly turns to whoever looks at it. */
    public boolean isEntityWatching() {
        return getOrCreateJarEntity() instanceof LOTRButterflyEntity;
    }

    private boolean givesLight() {
        return getOrCreateJarEntity() instanceof LOTRButterflyEntity butterfly
                && butterfly.getButterflyType() == LOTRButterflyEntity.ButterflyType.LORIEN;
    }

    /** The caged creature, built from its NBT when first wanted and set in the middle of the jar. */
    public @Nullable Entity getOrCreateJarEntity() {
        if (entityData == null || level == null) {
            return null;
        }
        if (jarEntity == null) {
            jarEntity = EntityType.loadEntityRecursive(entityData.copy(), level,
                    new EntitySpawnRequest(EntitySpawnReason.LOAD, true), e -> e);
            if (jarEntity != null) {
                // Never added to the level, so nothing hands it a network id, and
                // Entity.getId throws without one; renderers reach for it.
                int id = getBlockPos().hashCode();
                jarEntity.setId(id == 0 ? 1 : id);
                jarEntity.tickCount = getTicksExisted();
                BlockPos pos = getBlockPos();
                jarEntity.snapTo(pos.getX() + 0.5, pos.getY() + getEntityHeight() - jarEntity.getBbHeight() / 2.0f,
                        pos.getZ() + 0.5, jarEntity.getYRot(), jarEntity.getXRot());
            }
        }
        return jarEntity;
    }

    /** updateEntity, on both sides. */
    public static void tick(Level level, BlockPos pos, BlockState state, LOTRAnimalJarBlockEntity jar) {
        RandomSource rand = level.getRandom();
        if (jar.ticksExisted < 0) {
            jar.ticksExisted = rand.nextInt(100);
        }
        ++jar.ticksExisted;
        if (!level.isClientSide() && state.hasProperty(LOTRAnimalJarBlock.LIT)
                && state.getValue(LOTRAnimalJarBlock.LIT) != jar.givesLight()) {
            level.setBlock(pos, state.setValue(LOTRAnimalJarBlock.LIT, jar.givesLight()), Block.UPDATE_ALL);
        }
        Entity entity = jar.getOrCreateJarEntity();
        if (entity == null) {
            return;
        }
        entity.tickCount = jar.ticksExisted;
        entity.setOldPosAndRot(entity.position(), entity.getYRot(), entity.getXRot());
        if (entity instanceof LOTRAnimalJarUpdater updater) {
            updater.updateInAnimalJar();
        }
        if (!level.isClientSide()) {
            if (entity instanceof Mob mob) {
                ++mob.ambientSoundTime;
                if (rand.nextInt(1000) < mob.ambientSoundTime) {
                    mob.ambientSoundTime = -mob.getAmbientSoundInterval();
                    mob.playAmbientSound();
                }
                if (rand.nextInt(200) == 0) {
                    jar.targetYaw = rand.nextFloat() * 360.0f;
                    jar.sendingTargetYaw = true;
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                    jar.sendingTargetYaw = false;
                    mob.setYRot(jar.targetYaw);
                }
            }
        } else if (jar.hasTargetYaw) {
            float delta = Mth.wrapDegrees(jar.targetYaw - entity.getYRot());
            entity.setYRot(entity.getYRot() + delta * 0.1f);
            if (Math.abs(entity.getYRot() - jar.targetYaw) <= 0.01f) {
                jar.hasTargetYaw = false;
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        CompoundTag data = input.read(ENTITY_TAG, CompoundTag.CODEC).orElse(null);
        if (data == null ? entityData != null : !data.equals(entityData)) {
            entityData = data;
            jarEntity = null;
        }
        input.read(TARGET_YAW_TAG, Codec.FLOAT).ifPresent(yaw -> {
            this.targetYaw = yaw;
            this.hasTargetYaw = true;
        });
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (entityData != null) {
            output.store(ENTITY_TAG, CompoundTag.CODEC, entityData);
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /** getJarPacket: the contents, and with a new heading the heading (the original's packet type 1). */
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = saveWithoutMetadata(registries);
        if (sendingTargetYaw) {
            tag.putFloat(TARGET_YAW_TAG, targetYaw);
        }
        return tag;
    }
}
