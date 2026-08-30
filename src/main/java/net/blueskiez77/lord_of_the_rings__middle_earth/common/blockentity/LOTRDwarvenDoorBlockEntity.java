package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGateBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRIthildinDwarvenDoorBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRIthildinDwarvenDoorBlock.DoorSize;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityDwarvenDoor. Remembers which ithildin design this block belongs
 * to, which cell of it this block is, and where the design's base block sits --
 * the base position being the group's identity, so blocks of two designs that
 * happen to touch never mistake each other for kin.
 *
 * <p>It also drives the glow: {@link LOTRDwarvenGlowLogic} ramps a counter up
 * while a player is within twelve blocks and back down when they leave, and the
 * whole design reads that counter off its base block so a 3x4 door brightens as
 * one picture rather than one block at a time.
 */
public class LOTRDwarvenDoorBlockEntity extends BlockEntity {

    /** LOTRTileEntityDwarvenDoor.GLOW_RANGE. */
    public static final int GLOW_RANGE = 12;

    private final LOTRDwarvenGlowLogic glowLogic =
            new LOTRDwarvenGlowLogic().setPlayerRange(GLOW_RANGE);

    private @Nullable DoorSize doorSize;
    private int doorPosX;
    private int doorPosY;
    private @Nullable BlockPos doorBasePos;

    public LOTRDwarvenDoorBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.DWARVEN_DOOR, pos, state);
    }

    // ------------------------------------------------------------- door group

    /** Never null: an unassigned door reads as the 1x1 design. */
    public DoorSize getDoorSize() {
        if (doorSize == null) {
            doorSize = DoorSize._1x1;
        }
        return doorSize;
    }

    /** Clamped, because a saved position can outlive a shrinking design. */
    public int getDoorPosX() {
        if (doorPosX < 0 || doorPosX >= getDoorSize().width()) {
            doorPosX = 0;
        }
        return doorPosX;
    }

    public int getDoorPosY() {
        if (doorPosY < 0 || doorPosY >= getDoorSize().height()) {
            doorPosY = 0;
        }
        return doorPosY;
    }

    public @Nullable BlockPos getDoorBasePos() {
        return doorBasePos;
    }

    public boolean isSameDoor(LOTRDwarvenDoorBlockEntity other) {
        return doorBasePos != null && doorBasePos.equals(other.doorBasePos);
    }

    public void setDoorSizeAndPos(@Nullable DoorSize size, int x, int y) {
        doorSize = size == null ? DoorSize._1x1 : size;
        doorPosX = x;
        doorPosY = y;
        syncToClients();
    }

    public void setDoorBasePos(BlockPos pos) {
        doorBasePos = pos.immutable();
        glowLogic.resetGlowTick();
        setChanged();
    }

    /**
     * markBlockForUpdate + markDirty in the original. The design is block
     * entity data, not blockstate, so without the sendBlockUpdated the client
     * would keep whatever it had when the chunk loaded and the engraving would
     * not appear until a reload.
     */
    private void syncToClients() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    // ------------------------------------------------------------------ glow

    /**
     * The brightness of this block's share of the engraving. Every block of a
     * design defers to the base block's counter, so the picture fades in and
     * out as a whole even though each block ticks its own logic.
     */
    public float getGlowBrightness(float partialTick) {
        if (level == null) {
            return 0.0f;
        }
        LOTRDwarvenGlowLogic logic = glowLogic;

        if (doorBasePos != null && !doorBasePos.equals(worldPosition)
                && level.isLoaded(doorBasePos)
                && level.getBlockEntity(doorBasePos) instanceof LOTRDwarvenDoorBlockEntity base) {
            logic = base.glowLogic;
        }
        return logic.getGlowBrightness(level, lightSamplePos(), partialTick);
    }

    /**
     * Where to read sky light for the glow. NOT this block: a dwarven door is
     * a full, light-blocking cube, so the sky light inside it is always zero
     * and the engraving would never light at all. 1.7.10 papered over exactly
     * this with the {@code useNeighborBrightness = true} that setFullBlock()
     * set alongside {@code lightOpacity = 255}; the modern equivalent is to
     * sample the open block the engraving actually faces into.
     */
    private BlockPos lightSamplePos() {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof LOTRIthildinDwarvenDoorBlock) {
            Direction facing = state.getValue(LOTRGateBlock.FACING);
            if (facing.getAxis() != Direction.Axis.Y) {
                return worldPosition.relative(facing.getOpposite());
            }
        }
        return worldPosition;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state,
                                  LOTRDwarvenDoorBlockEntity door) {
        door.glowLogic.update(level, door.doorBasePos == null ? pos : door.doorBasePos);
    }

    /**
     * LOTRBlockGateDwarvenIthildin.breakBlock. Breaking one block of a design
     * dissolves the whole design: every door sharing this one's base position
     * falls back to 1x1 rather than keeping half an engraving.
     *
     * <p>The original hung this on the block's breakBlock, which still had the
     * tile entity to read. Its modern counterpart, affectNeighborsAfterRemoval,
     * runs AFTER the block entity is gone -- so the logic lives here instead,
     * on the last hook that still has the design in hand. Unlike setRemoved
     * this fires only on real removal, not on chunk unload, which would
     * otherwise dissolve every door in the world as the player walked away.
     */
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        if (level == null || level.isClientSide()
                || !(state.getBlock() instanceof LOTRIthildinDwarvenDoorBlock)) {
            return;
        }
        Direction facing = state.getValue(LOTRIthildinDwarvenDoorBlock.FACING);
        if (facing.getAxis() == Direction.Axis.Y) {
            return;
        }

        DoorSize size = getDoorSize();
        Direction across = LOTRIthildinDwarvenDoorBlock.acrossFrom(facing);
        int rangeXZ = size.width() - 1;
        int rangeY = size.height() - 1;

        for (int y = -rangeY; y <= rangeY; y++) {
            for (int x = -rangeXZ; x <= rangeXZ; x++) {
                BlockPos cell = pos.relative(across, x).above(y);
                if (cell.equals(pos)) {
                    continue;
                }
                if (level.getBlockEntity(cell) instanceof LOTRDwarvenDoorBlockEntity other
                        && other.isSameDoor(this)) {
                    other.setDoorSizeAndPos(null, 0, 0);
                }
            }
        }
    }

    // ------------------------------------------------------------ persistence

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        String name = input.getStringOr("DoorSize", null);
        if (name != null) {
            doorSize = DoorSize.forName(name);
            doorPosX = input.getByteOr("DoorX", (byte) 0);
            doorPosY = input.getByteOr("DoorY", (byte) 0);
            doorBasePos = new BlockPos(
                    input.getIntOr("DoorBaseX", 0),
                    input.getIntOr("DoorBaseY", 0),
                    input.getIntOr("DoorBaseZ", 0));
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (doorSize != null) {
            output.putString("DoorSize", doorSize.doorName());
            output.putByte("DoorX", (byte) doorPosX);
            output.putByte("DoorY", (byte) doorPosY);
            BlockPos base = doorBasePos == null ? worldPosition : doorBasePos;
            output.putInt("DoorBaseX", base.getX());
            output.putInt("DoorBaseY", base.getY());
            output.putInt("DoorBaseZ", base.getZ());
        }
    }
}
