package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityAnimalJar: the creature a bird cage (or, when they are ported,
 * a butterfly jar) is holding.
 *
 * <p>All it stores is the captured entity's own NBT under "JarEntity", exactly
 * as the original did. Nothing is instantiated while the creature is caged --
 * it is data, not a live entity -- so it does not tick, wander or despawn, and
 * it comes back with everything it had when it goes free.
 */
public class LOTRAnimalJarBlockEntity extends BlockEntity {

    /** The tag the original used, kept so the shape of the data is familiar. */
    public static final String ENTITY_TAG = "JarEntity";

    private @Nullable CompoundTag entityData;

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
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    /**
     * Lets the caged creature out, and empties the cage. Returns false when
     * there was nothing in it or the entity could not be rebuilt.
     */
    public boolean release(Level level, BlockPos pos) {
        if (isEmpty()) {
            return false;
        }
        Entity entity = EntityType.loadEntityRecursive(entityData.copy(), level,
                new EntitySpawnRequest(EntitySpawnReason.BUCKET, true), e -> {
                    e.snapTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            e.getYRot(), e.getXRot());
                    return e;
                });
        if (entity == null) {
            return false;
        }
        level.addFreshEntity(entity);
        setEntityData(null);
        return true;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        entityData = input.read(ENTITY_TAG, CompoundTag.CODEC).orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (entityData != null) {
            output.store(ENTITY_TAG, CompoundTag.CODEC, entityData);
        }
    }

    // The client needs to know whether the cage is occupied so a renderer can
    // draw the occupant later; the totem syncs the same way.
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
