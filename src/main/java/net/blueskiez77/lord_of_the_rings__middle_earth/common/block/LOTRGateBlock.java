package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRThrowingAxeItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ProjectileWeaponItem;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jspecify.annotations.Nullable;

// Faction gates. Right-click one panel and the whole gate opens: the block
// floods outwards to every gate panel facing the same way and in the same
// state, up to sixteen steps, and flips them all together.
//
// LOTRBlockGate stored two things in metadata -- the low three bits were a
// direction 0-5 (down, up, north, south, west, east) and bit 8 was open. Those
// are FACING and OPEN here. FACING is the direction the gate's FLAT SIDE
// faces, not the direction it swings: a gate panel filling a north-south
// doorway has FACING north or south.
//
// hasConnectedTextures is the boolean LOTRMod passed to createWooden /
// createStone / createMetal. It only matters to rendering -- LOTRGateModel
// reads it back off the block -- but it lives on the block, exactly as it did
// in 1.7.10, so there is one place that says which gates are which.
//
// LOTRBlockGate's two subclasses are LOTRDwarvenDoorBlock and
// LOTRIthildinDwarvenDoorBlock, which differ mainly in filling their whole cube
// -- see isFullBlock() below for what that changes.
public class LOTRGateBlock extends Block {

    public static final MapCodec<LOTRGateBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("connected_textures", true).forGetter(LOTRGateBlock::hasConnectedTextures),
            propertiesCodec()
    ).apply(instance, LOTRGateBlock::new));

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    /** LOTRBlockGate.MAX_GATE_RANGE: the flood fill runs sixteen deep. */
    public static final int MAX_GATE_RANGE = 16;

    /** setBlockBoundsForDirection: a quarter of a block thick. */
    public static final double THICKNESS = 0.25;

    private final boolean hasConnectedTextures;

    public LOTRGateBlock(Properties properties) {
        this(true, properties);
    }

    public LOTRGateBlock(boolean hasConnectedTextures, Properties properties) {
        super(properties);
        this.hasConnectedTextures = hasConnectedTextures;
        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false));
    }

    public boolean hasConnectedTextures() {
        return hasConnectedTextures;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    // ------------------------------------------------------------- placement

    /**
     * LOTRBlockGate.setFullBlock. The dwarven doors fill their whole cube
     * instead of standing as a quarter-block panel, which changes the shape,
     * the placement rule and the face culling below. It was a mutable field set
     * by a subclass constructor in 1.7.10; here the subclass just says so.
     */
    public boolean isFullBlock() {
        return false;
    }

    /**
     * LOTRItemGate.placeBlockAt. Clicking a floor or ceiling gives a gate
     * facing the way the player does; looking sharply up or down gives a
     * horizontal panel; otherwise the gate turns to sit across the face that
     * was clicked -- rotated LEFT for a thin panel, reversed for a full block.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clicked = context.getClickedFace();
        Player player = context.getPlayer();
        float pitch = player == null ? 0.0F : player.getXRot();

        Direction facing;
        if (clicked.getAxis() == Direction.Axis.Y) {
            facing = context.getHorizontalDirection();
        } else if (pitch < -40.0F || pitch > 40.0F) {
            // A full-block gate went off the look direction. A thin panel went
            // off which HALF of the face was clicked (LOTRItemGate's f1), so
            // the panel lands on the edge you actually pointed at; pitch alone
            // gets that wrong whenever you place low on a block while looking
            // down at it.
            if (isFullBlock()) {
                facing = pitch > 0.0F ? Direction.DOWN : Direction.UP;
            } else {
                double hitY = context.getClickLocation().y - context.getClickedPos().getY();
                facing = hitY > 0.5 ? Direction.DOWN : Direction.UP;
            }
        } else {
            // Direction.rotateLeft in 1.7.10 is counter-clockwise seen from
            // above. getClockWise() is the opposite turn, which left every gate
            // placed against a wall at ninety degrees to where it belonged.
            facing = isFullBlock() ? clicked.getOpposite() : clicked.getCounterClockWise();
        }
        return defaultBlockState().setValue(FACING, facing).setValue(OPEN, false);
    }

    // ------------------------------------------------------------ open/close

    /**
     * LOTRBlockGate.directionsMatch. Up and down only match themselves; the
     * four horizontals match anything on the same axis, so a gate can be built
     * from panels facing either way along its line.
     */
    public boolean directionsMatch(Direction a, Direction b) {
        if (a.getAxis() == Direction.Axis.Y || b.getAxis() == Direction.Axis.Y) {
            return a == b;
        }
        return a.getAxis() == b.getAxis();
    }

    /**
     * LOTRBlockGate.getConnectedGates: a breadth-first flood outwards, sixteen
     * deep, taking every gate panel that faces compatibly and is in the same
     * open state. The search deliberately does not spread along the gate's own
     * axis -- a gate is a plane, so it grows in the two directions its face
     * does not point.
     */
    public List<BlockPos> getConnectedGates(BlockGetter level, BlockPos origin) {
        BlockState originState = level.getBlockState(origin);
        if (!(originState.getBlock() instanceof LOTRGateBlock)) {
            return List.of();
        }
        Direction facing = originState.getValue(FACING);
        boolean open = originState.getValue(OPEN);

        Set<BlockPos> found = new HashSet<>();
        Deque<BlockPos> frontier = new ArrayDeque<>();
        found.add(origin);
        frontier.add(origin);

        for (int depth = 0; depth < MAX_GATE_RANGE && !frontier.isEmpty(); ++depth) {
            Deque<BlockPos> next = new ArrayDeque<>();
            for (BlockPos pos : frontier) {
                for (Direction step : Direction.values()) {
                    // gatherAdjacentGates: skip the axis the gate faces along.
                    if (step.getAxis() == facing.getAxis()) {
                        continue;
                    }
                    BlockPos neighbour = pos.relative(step);
                    if (found.contains(neighbour)) {
                        continue;
                    }
                    BlockState state = level.getBlockState(neighbour);
                    if (!(state.getBlock() instanceof LOTRGateBlock other)) {
                        continue;
                    }
                    if (state.getValue(OPEN) != open) {
                        continue;
                    }
                    Direction otherFacing = state.getValue(FACING);
                    if (!directionsMatch(facing, otherFacing)
                            || !other.directionsMatch(facing, otherFacing)) {
                        continue;
                    }
                    found.add(neighbour);
                    next.add(neighbour);
                }
            }
            frontier = next;
        }
        return new ArrayList<>(found);
    }

    /**
     * LOTRBlockGate.areBlocksConnected. This is the RENDER-side question, not
     * the flood fill: given this panel and one of the eight neighbours around
     * a face, do the two read as one continuous sheet of gate?
     *
     * A closed gate only joins its own block, so a rohan gate beside a gondor
     * gate keeps its frame. An open one joins any gate at all, and also counts
     * whatever sits directly beneath it as joined, so the open frame does not
     * draw a lip along the ground.
     */
    public boolean areBlocksConnected(BlockGetter level, BlockPos pos, BlockState state, BlockPos otherPos) {
        Direction facing = state.getValue(FACING);
        boolean open = state.getValue(OPEN);

        // The neighbour has to lie in the gate's own plane.
        switch (facing.getAxis()) {
            case Y -> {
                if (otherPos.getY() != pos.getY()) {
                    return false;
                }
            }
            case Z -> {
                if (otherPos.getZ() != pos.getZ()) {
                    return false;
                }
            }
            case X -> {
                if (otherPos.getX() != pos.getX()) {
                    return false;
                }
            }
        }

        BlockState otherState = level.getBlockState(otherPos);
        boolean otherIsGate = otherState.getBlock() instanceof LOTRGateBlock;

        if (open && otherPos.getY() == pos.getY() - 1 && !otherIsGate) {
            return true;
        }
        if (!otherIsGate) {
            return false;
        }
        if (!open && !otherState.is(this)) {
            return false;
        }
        LOTRGateBlock other = (LOTRGateBlock) otherState.getBlock();
        Direction otherFacing = otherState.getValue(FACING);
        return directionsMatch(facing, otherFacing)
                && other.directionsMatch(facing, otherFacing)
                && open == otherState.getValue(OPEN);
    }

    /** LOTRBlockGate.activateGate. */
    public void activateGate(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof LOTRGateBlock)) {
            return;
        }
        boolean open = !state.getValue(OPEN);

        for (BlockPos gatePos : getConnectedGates(level, pos)) {
            BlockState gateState = level.getBlockState(gatePos);
            if (gateState.getBlock() instanceof LOTRGateBlock) {
                level.setBlock(gatePos, gateState.setValue(OPEN, open), Block.UPDATE_ALL);
            }
        }

        level.playSound(null, pos, gateSound(state, open), SoundSource.BLOCKS,
                1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
    }

    /**
     * activateGate picked the stone pair off Material.rock and the wooden pair
     * off everything else. SoundType stands in for Material here; the four
     * sound events themselves are the originals, under their original ids.
     */
    protected SoundEvent gateSound(BlockState state, boolean open) {
        if (state.getSoundType() == SoundType.STONE) {
            return open ? LOTRSounds.GATE_STONE_OPEN : LOTRSounds.GATE_STONE_CLOSE;
        }
        return open ? LOTRSounds.GATE_OPEN : LOTRSounds.GATE_CLOSE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            activateGate(level, pos);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Holding another gate block does NOT open the gate -- that is how you
     * build one panel against another -- and nor does a ranged weapon, so it
     * can be drawn while standing at one. LOTRWeaponStats.isRangedWeapon was
     * ItemBow (the crossbows and blowgun included), LOTRItemSpear and
     * LOTRItemThrowingAxe; the port's spears are the ones carrying vanilla's
     * kinetic-weapon component.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand,
                                          BlockHitResult hitResult) {
        if (stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof LOTRGateBlock) {
            return InteractionResult.PASS;
        }
        if (stack.getItem() instanceof ProjectileWeaponItem
                || stack.getItem() instanceof LOTRThrowingAxeItem
                || stack.has(DataComponents.KINETIC_WEAPON)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            activateGate(level, pos);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * LOTRBlockGate.onNeighborBlockChange. The second half of the guard is the
     * important one: a gate reacts when it is powered, or when the block that
     * just changed could itself have been powering it. Testing only
     * powered != open makes any unrelated neighbour edit -- a torch placed, a
     * crop broken, a bucket emptied -- slam a hand-opened gate shut.
     */
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
                                   @Nullable Orientation orientation, boolean movedByPiston) {
        if (level.isClientSide() || block instanceof LOTRGateBlock) {
            return;
        }
        boolean open = state.getValue(OPEN);
        boolean powered = false;
        for (BlockPos gatePos : getConnectedGates(level, pos)) {
            if (level.hasNeighborSignal(gatePos)) {
                powered = true;
                break;
            }
        }
        if ((powered || block.defaultBlockState().isSignalSource()) && powered != open) {
            activateGate(level, pos);
        }
    }

    // ----------------------------------------------------------------- shape

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeFor(state.getValue(FACING));
    }

    /** LOTRBlockGate.setBlockBoundsForDirection. */
    public VoxelShape shapeFor(Direction facing) {
        if (isFullBlock()) {
            return Shapes.block();
        }
        double half = THICKNESS / 2.0;
        return switch (facing) {
            // A down-facing panel hugs the ceiling, an up-facing one the floor.
            case DOWN -> box01(0, 1.0 - THICKNESS, 0, 1, 1, 1);
            case UP -> box01(0, 0, 0, 1, THICKNESS, 1);
            case NORTH, SOUTH -> box01(0, 0, 0.5 - half, 1, 1, 0.5 + half);
            case WEST, EAST -> box01(0.5 - half, 0, 0, 0.5 + half, 1, 1);
        };
    }

    private static VoxelShape box01(double x0, double y0, double z0, double x1, double y1, double z1) {
        return Block.box(x0 * 16, y0 * 16, z0 * 16, x1 * 16, y1 * 16, z1 * 16);
    }

    /** An open gate is walked through: no collision at all. */
    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(OPEN) ? Shapes.empty() : getShape(state, level, pos, context);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        // getBlocksMovement: an open gate is passable to pathfinding.
        return state.getValue(OPEN);
    }

    /**
     * isOpaqueCube in the original was unconditionally false -- LOTRBlockGate
     * never blocked light or occluded a neighbour's face, open or closed,
     * because its geometry never fills a full cube.
     *
     * useShapeForLightOcclusion=false is what actually reproduces that:
     * BlockStateBase only consults getOcclusionShape() when this is true, so
     * with it false the shape below is unused for light and the block casts
     * no shadow of its own. It is left as the real per-facing shape rather
     * than Shapes.empty() only because getOcclusionShape has other callers
     * (e.g. Block.shouldRenderFace) that expect a state's actual footprint,
     * not a lie about it; the light behaviour comes from the flag, not from
     * hollowing the shape out.
     *
     * <p>The full-block doors are the exception, and were in 1.7.10 too:
     * setFullBlock() also set {@code lightOpacity = 255}, unconditionally and
     * regardless of whether the door was open. Consulting the occlusion shape
     * for those reproduces it, since their shape is a full cube in both states.
     */
    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return isFullBlock();
    }

    /**
     * An OPEN gate occludes nothing. This matters only to the full-block
     * doors -- the thin panels are declared .noOcclusion() and never occlude
     * either way -- but for those it matters a great deal: this shape is
     * cached per state and drives BOTH the neighbour face culling in
     * Block.shouldRenderFace AND getLightBlock. Returning the full cube for an
     * open door told the chunk mesher that the walls and floor around it were
     * still hidden, so an opened door became a hole you could see through the
     * world by.
     */
    @Override
    protected VoxelShape getOcclusionShape(BlockState state) {
        if (state.getValue(OPEN)) {
            return Shapes.empty();
        }
        return shapeFor(state.getValue(FACING));
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return !isFullBlock() || state.getValue(OPEN);
    }

    /**
     * LOTRBlockGate.shouldSideBeRendered. Two panels of the same open (or
     * matching closed) gate hide the seam between them, exactly like
     * areBlocksConnected -- this is the mesh-culling twin of that method,
     * asked instead of the neighbour rather than about it.
     */
    @Override
    protected boolean skipRendering(BlockState state, BlockState neighbour, Direction side) {
        // "if (!fullBlockGate || openThis)": a CLOSED full-block door is an
        // ordinary solid cube and culls by the normal rules; only once it opens
        // does the gate-seam logic apply to it.
        if (isFullBlock() && !state.getValue(OPEN)) {
            return false;
        }
        if (neighbour.getBlock() instanceof LOTRGateBlock other) {
            Direction facing = state.getValue(FACING);
            Direction otherFacing = neighbour.getValue(FACING);
            // The seam that can be hidden is an IN-PLANE one: two panels side by
            // side. shouldSideBeRendered's guard was !directionsMatch(dir, side),
            // i.e. the side is NOT on the axis the gate faces along. Testing for
            // equality instead culled the panel's own broad faces and left every
            // seam drawn.
            return side.getAxis() != facing.getAxis()
                    && state.getValue(OPEN) == neighbour.getValue(OPEN)
                    && directionsMatch(facing, otherFacing)
                    && other.directionsMatch(facing, otherFacing);
        }
        return false;
    }

    @Override
    protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

}