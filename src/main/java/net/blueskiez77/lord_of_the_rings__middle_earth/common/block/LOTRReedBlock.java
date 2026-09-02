package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Reeds, and the dried reeds that share the class.
 *
 * <p>A reed is an ordinary waterlogged block: it is planted on the bed at the
 * BOTTOM of shallow water and the column grows up through the water and out
 * into the air above it. The 1.7.10 block instead sat on top of the water
 * surface and its renderer drew the submerged stem by walking down into the
 * water from there -- a trick a static block model cannot do, and the reason an
 * earlier version of this class carried a "skirt" property. Standing each
 * segment in its own block is both simpler and what was asked for.
 *
 * <p>Growth is the original's counter: a random tick raises AGE, and when AGE
 * passes {@link #GROW_END} a new reed appears above and the counter resets. The
 * column stops once {@link #MAX_HEIGHT_ABOVE_WATER} segments stand clear of the
 * water. Dried reeds use the same class with {@link #canGrow} false, exactly as
 * LOTRBlockReedDry overrode canReedGrow to return false.
 */
public class LOTRReedBlock extends VegetationBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    public static final MapCodec<LOTRReedBlock> CODEC =
            simpleCodec(props -> new LOTRReedBlock(true, props));

    /** LOTRBlockReed.MAX_GROW_HEIGHT, now counted above the surface. */
    public static final int MAX_HEIGHT_ABOVE_WATER = 3;

    /** LOTRBlockReed.META_GROW_END: the counter a reed has to pass to grow. */
    public static final int GROW_END = 15;

    /** Deepest water a reed may be planted in. */
    public static final int MAX_WATER_DEPTH = 2;

    /** No reed above: this segment is the tip, and takes the _upper sprite. */
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;

    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    private final boolean canGrow;

    public LOTRReedBlock(boolean canGrow, Properties properties) {
        super(properties);
        this.canGrow = canGrow;
        registerDefaultState(stateDefinition.any()
                .setValue(TOP, true)
                .setValue(WATERLOGGED, false)
                .setValue(AGE, 0));
    }

    @Override
    public MapCodec<? extends LOTRReedBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TOP, WATERLOGGED, AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    /**
     * A reed is planted in water at most {@link #MAX_WATER_DEPTH} deep, or on
     * top of another reed.
     *
     * <p>What broke this before was not the depth rule but the item: reeds were
     * registered with PlaceOnWaterBlockItem, which places on TOP of the water,
     * so the position being tested was the air above the surface and never had
     * any water in it. With a plain BlockItem the clicked position is the water
     * block itself -- water is replaceable, so clicking the bed through it
     * resolves there -- and the rule can be checked where it was meant to be.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (level.getBlockState(pos.below()).is(this)) {
            return describe(defaultBlockState()
                    .setValue(WATERLOGGED, level.getFluidState(pos).is(Fluids.WATER)), level, pos);
        }
        if (!level.getFluidState(pos).is(Fluids.WATER) || waterDepthAbove(level, pos) > MAX_WATER_DEPTH) {
            return null;
        }
        return describe(defaultBlockState().setValue(WATERLOGGED, true), level, pos);
    }

    /** How deep the water is, counting this block and every one above it. */
    private static int waterDepthAbove(LevelReader level, BlockPos pos) {
        int depth = 0;
        BlockPos at = pos;
        while (level.getFluidState(at).is(Fluids.WATER) && depth <= MAX_WATER_DEPTH) {
            depth++;
            at = at.above();
        }
        return depth;
    }

    @Override
    protected boolean mayPlaceOn(BlockState below, BlockGetter level, BlockPos belowPos) {
        return below.is(this) || below.is(LOTRBlockTags.REEDS_PLANTABLE_ON);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
            BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState,
            RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        // VegetationBlock's own updateShape turns the block to air when it can
        // no longer stand -- that is what makes breaking one reed take every
        // reed above it with it. Anything that survives has to recheck whether
        // it is still the tip.
        BlockState updated = super.updateShape(state, level, ticks, pos, direction,
                neighbourPos, neighbourState, random);
        return updated.is(this) ? describe(updated, level, pos) : updated;
    }

    /** Fills in TOP from what is actually above the block. */
    private BlockState describe(BlockState state, LevelReader level, BlockPos pos) {
        return state.setValue(TOP, !level.getBlockState(pos.above()).is(this));
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canGrow || !canGrowHere(level, pos)) {
            return;
        }
        int age = state.getValue(AGE);
        if (age < GROW_END) {
            level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_INVISIBLE);
            return;
        }
        grow(level, pos, state);
    }

    // --- bone meal --------------------------------------------------------

    // LOTRBlockReed was not IGrowable in 1.7.10 -- bone meal on reeds is new.
    // It does what a random tick eventually would: adds the next segment,
    // provided the column has room and is not the dried kind, which never grows.
    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return canGrow && canGrowHere(level, pos);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        grow(level, pos, state);
    }

    /**
     * Adds the next segment. The AGE reset is written FIRST and the new reed
     * placed second, because placing it sends this block an update that clears
     * its TOP flag -- writing the old state afterwards put the flag straight
     * back, which is why a bone-mealed reed kept the tip sprite on the segment
     * that was no longer the tip.
     */
    private void grow(ServerLevel level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(AGE, 0).setValue(TOP, false), Block.UPDATE_CLIENTS);
        BlockState above = defaultBlockState()
                .setValue(WATERLOGGED, level.getFluidState(pos.above()).is(Fluids.WATER));
        level.setBlockAndUpdate(pos.above(), above);
    }

    /**
     * Whether this column may put out another segment. The water requirement
     * lives here rather than in placement: a reed grows only if its foot is
     * standing in water no more than {@link #MAX_WATER_DEPTH} deep, and stops
     * once {@link #MAX_HEIGHT_ABOVE_WATER} segments are clear of the surface. A
     * reed planted on dry land is a decoration and simply never grows.
     */
    private boolean canGrowHere(LevelReader level, BlockPos pos) {
        if (!canGrowInto(level, pos.above())) {
            return false;
        }
        BlockPos foot = foot(level, pos);
        if (!level.getBlockState(foot).getValue(WATERLOGGED)) {
            return false;
        }
        int rooted = 0;
        BlockPos at = foot;
        while (level.getBlockState(at).is(this) && level.getBlockState(at).getValue(WATERLOGGED)) {
            rooted++;
            at = at.above();
        }
        return rooted <= MAX_WATER_DEPTH && heightAboveWater(level, pos) < MAX_HEIGHT_ABOVE_WATER;
    }

    private BlockPos foot(LevelReader level, BlockPos pos) {
        BlockPos at = pos;
        while (level.getBlockState(at.below()).is(this)) {
            at = at.below();
        }
        return at;
    }

    private boolean canGrowInto(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || (state.getBlock() == Blocks.WATER && state.getFluidState().isSource());
    }

    /** How many segments of this column stand clear of the water. */
    private int heightAboveWater(LevelReader level, BlockPos pos) {
        int dry = 0;
        BlockPos at = pos;
        while (level.getBlockState(at).is(this)) {
            if (!level.getBlockState(at).getValue(WATERLOGGED)) {
                dry++;
            }
            at = at.below();
        }
        return dry;
    }
}
