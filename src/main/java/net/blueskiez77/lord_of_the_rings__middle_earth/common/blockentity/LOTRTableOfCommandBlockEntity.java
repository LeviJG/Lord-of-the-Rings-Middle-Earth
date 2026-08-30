package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityCommandTable. All it remembers is how far the map on the
 * tabletop is zoomed in.
 *
 * <p>The map itself is not ported -- see docs/TODO-table-of-command.md -- but
 * the zoom is stored and synced anyway, so a table set up now will already be
 * at the zoom its owner chose once there is a map to draw.
 */
public class LOTRTableOfCommandBlockEntity extends BlockEntity {

    /** setZoomExp clamped to [-2, 2]: five steps, stored as a power of two. */
    public static final int MIN_ZOOM = -2;
    public static final int MAX_ZOOM = 2;

    private int zoomExp;

    public LOTRTableOfCommandBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.TABLE_OF_COMMAND, pos, state);
    }

    public int getZoomExp() {
        return zoomExp;
    }

    public void setZoomExp(int exp) {
        zoomExp = Mth.clamp(exp, MIN_ZOOM, MAX_ZOOM);
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    /**
     * LOTRTileEntityCommandTable.toggleZoomExp. Steps DOWN through the range
     * and wraps round at the bottom, so repeated sneak-clicks zoom out one
     * notch at a time and then snap back to fully zoomed in.
     */
    public void toggleZoom() {
        setZoomExp(zoomExp <= MIN_ZOOM ? MAX_ZOOM : zoomExp - 1);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        zoomExp = input.getByteOr("Zoom", (byte) 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putByte("Zoom", (byte) zoomExp);
    }
}
