package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.Comparator;
import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRDwarvenDoorBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockGateDwarvenIthildin -- the Doors of Durin.
 *
 * <p>A {@link LOTRDwarvenDoorBlock} that carries a block entity so a group of
 * adjacent doors can agree on a shared ithildin design. Placing one searches
 * outwards for the largest rectangle of matching doors it can complete, from
 * 3x4 down to 1x1, and hands every block in that rectangle the same
 * {@link DoorSize} plus its own cell within it. The glow texture a block draws
 * is then just {@code <size>_<x>_<y>}, so the twenty-five sprites tile into one
 * continuous engraving across the door.
 */
public class LOTRIthildinDwarvenDoorBlock extends LOTRDwarvenDoorBlock implements EntityBlock {

    public static final MapCodec<LOTRIthildinDwarvenDoorBlock> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    propertiesCodec()
            ).apply(instance, LOTRIthildinDwarvenDoorBlock::new));

    public LOTRIthildinDwarvenDoorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    /**
     * The base class lets a gate join any panel on the same axis, so a
     * north-facing and a south-facing door would open together. An ithildin
     * door is a picture: the two halves of a design must face the same way or
     * the engraving would run backwards across the seam.
     */
    @Override
    public boolean directionsMatch(Direction a, Direction b) {
        return a == b;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRDwarvenDoorBlockEntity(pos, state);
    }

    /**
     * The glow counter is client-side presentation only -- it is never saved
     * and never sent -- so the door ticks on the client and nowhere else.
     *
     * <p>BaseEntityBlock.createTickerHelper would do the cast, but it is
     * protected there and this block descends from LOTRGateBlock, not from
     * BaseEntityBlock. The check it performs is the whole of its body.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (!level.isClientSide() || type != LOTRBlockEntities.DWARVEN_DOOR) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRDwarvenDoorBlockEntity>)
                LOTRDwarvenDoorBlockEntity::clientTick;
    }

    /**
     * The in-plane horizontal step. 1.7.10 wrote this as xzFactorX/xzFactorZ
     * per metadata: north +X, south -X, west -Z, east +Z. That is exactly the
     * clockwise turn from the facing, so the design's local X always runs the
     * same way round the door regardless of which wall it sits in.
     */
    public static Direction acrossFrom(Direction facing) {
        return facing.getClockWise();
    }

    /** LOTRBlockGateDwarvenIthildin.areDwarfDoorsMatching. */
    public boolean doorsMatch(Level level, BlockPos pos, BlockPos otherPos) {
        BlockState state = level.getBlockState(pos);
        BlockState otherState = level.getBlockState(otherPos);
        if (!state.is(this) || !otherState.is(this)) {
            return false;
        }
        Direction facing = state.getValue(FACING);
        Direction otherFacing = otherState.getValue(FACING);
        return directionsMatch(facing, otherFacing);
    }

    /**
     * LOTRBlockGateDwarvenIthildin.onBlockPlacedBy. Walks the door sizes
     * largest-first and, for each, every rectangle of that size that contains
     * the block just placed. The first rectangle that is wholly made of
     * matching doors AND contains no door already claimed by an equal-or-larger
     * design wins, and the whole rectangle is rewritten to it.
     *
     * <p>The 1.7.10 decompile lost the jump that ends the search on a hit --
     * it survives only as the comment "Byte code: goto -> 570" -- so without
     * the return below every smaller size would overwrite the one just chosen.
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (level.isClientSide()
                || !(level.getBlockEntity(pos) instanceof LOTRDwarvenDoorBlockEntity door)) {
            return;
        }

        Direction facing = state.getValue(FACING);
        door.setDoorBasePos(pos);
        door.setDoorSizeAndPos(null, 0, 0);

        // A door lying flat cannot carry a design: there is no "up" for the
        // engraving to be oriented against.
        if (facing.getAxis() == Direction.Axis.Y) {
            return;
        }

        Direction across = acrossFrom(facing);

        for (DoorSize size : DoorSize.orderedSizes()) {
            int rangeXZ = size.width() - 1;
            int rangeY = size.height() - 1;

            // The corner of a candidate rectangle, offset back from the block
            // just placed so that every rectangle containing it is tried.
            for (int y = -rangeY; y <= 0; y++) {
                for (int xz = -rangeXZ; xz <= 0; xz++) {
                    BlockPos corner = pos.relative(across, xz).above(y);

                    if (!rectangleIsFree(level, pos, corner, across, size)) {
                        continue;
                    }

                    // Base position BEFORE size: setDoorSizeAndPos is what
                    // pushes the update packet, so setting the base afterwards
                    // would leave every client with a stale one and each block
                    // reading its glow counter from the wrong door.
                    door.setDoorBasePos(pos);
                    door.setDoorSizeAndPos(size, -xz, -y);
                    claimRectangle(level, pos, corner, across, size);
                    return;
                }
            }
        }
    }

    /**
     * Every cell of the rectangle is a matching door, and none of them already
     * belongs to a design at least as large as this one (compareLarger sorts
     * larger first, so "not larger" is a comparison result other than -1).
     */
    private boolean rectangleIsFree(Level level, BlockPos placed, BlockPos corner,
                                    Direction across, DoorSize size) {
        for (int y = 0; y < size.height(); y++) {
            for (int x = 0; x < size.width(); x++) {
                BlockPos cell = corner.relative(across, x).above(y);
                if (cell.equals(placed)) {
                    continue;
                }
                if (!doorsMatch(level, placed, cell)) {
                    return false;
                }
                if (level.getBlockEntity(cell) instanceof LOTRDwarvenDoorBlockEntity other
                        && DoorSize.COMPARE_LARGER.compare(other.getDoorSize(), size) != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Hands every other block in the rectangle its cell and the shared base. */
    private void claimRectangle(Level level, BlockPos placed, BlockPos corner,
                                Direction across, DoorSize size) {
        for (int y = 0; y < size.height(); y++) {
            for (int x = 0; x < size.width(); x++) {
                BlockPos cell = corner.relative(across, x).above(y);
                if (cell.equals(placed)) {
                    continue;
                }
                if (level.getBlockEntity(cell) instanceof LOTRDwarvenDoorBlockEntity other) {
                    other.setDoorBasePos(placed);
                    other.setDoorSizeAndPos(size, x, y);
                }
            }
        }
    }

    /**
     * The five ithildin designs, by the grid of blocks they span.
     * {@code assets/lotr/textures/block/ithildin_dwarven_door_glow_<name>_<x>_<y>.png}
     */
    public enum DoorSize {
        _1x1("1x1", 1, 1),
        _1x2("1x2", 1, 2),
        _2x2("2x2", 2, 2),
        _2x3("2x3", 2, 3),
        _3x4("3x4", 3, 4);

        /** Largest first: by area, then height, then width. */
        public static final Comparator<DoorSize> COMPARE_LARGER = (a, b) -> {
            if (a == b) {
                return 0;
            }
            if (a.area != b.area) {
                return -Integer.compare(a.area, b.area);
            }
            if (a.height != b.height) {
                return -Integer.compare(a.height, b.height);
            }
            return -Integer.compare(a.width, b.width);
        };

        private static final List<DoorSize> ORDERED = List.of(values()).stream()
                .sorted(COMPARE_LARGER)
                .toList();

        private final String doorName;
        private final int width;
        private final int height;
        private final int area;

        DoorSize(String doorName, int width, int height) {
            this.doorName = doorName;
            this.width = width;
            this.height = height;
            this.area = width * height;
        }

        public static List<DoorSize> orderedSizes() {
            return ORDERED;
        }

        public static @Nullable DoorSize forName(String name) {
            for (DoorSize size : values()) {
                if (size.doorName.equals(name)) {
                    return size;
                }
            }
            return null;
        }

        public String doorName() {
            return doorName;
        }

        public int width() {
            return width;
        }

        public int height() {
            return height;
        }
    }
}
