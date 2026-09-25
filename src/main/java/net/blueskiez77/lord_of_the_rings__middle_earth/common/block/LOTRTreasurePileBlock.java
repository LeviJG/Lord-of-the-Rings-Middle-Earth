package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRBlockTreasurePile: a heap of coin that piles up a layer at a time.
 *
 * <p>The original packed the height into the block's metadata, 0 through 7, and
 * {@code setBlockBoundsMeta} made that {@code (meta + 1) / 8} of a block tall --
 * so the smallest pile is a carpet-thin scatter and the largest is a full cube.
 * That is {@link #LAYERS} here, 1 through 8, the same eight heights under
 * vanilla's own snow-layer property.
 *
 * <p>It falls, like the original's {@link FallingBlock} check, and it lands on
 * top of a pile that is not yet full rather than beside it. Walking on one
 * kicks coins up.
 *
 * <p>DIVERGENCE, worth knowing about: in 1.7.10 the ITEM carried a metadata too,
 * so one item could be a whole eight-layer block and breaking a full pile gave
 * back exactly one item. Item metadata is gone, so this follows vanilla's snow
 * instead -- one item is one layer, a full pile drops eight, and right-clicking
 * a pile with another adds a layer. The original's stacking interaction, which
 * poured a multi-layer item into a pile and spilled the remainder into the block
 * above, has no meaning once every item is a single layer.
 */
public class LOTRTreasurePileBlock extends FallingBlock {
    public static final MapCodec<LOTRTreasurePileBlock> CODEC =
            simpleCodec(LOTRTreasurePileBlock::new);

    /** setBlockBoundsMeta: (meta + 1) / 8 of a block, for meta 0..7. */
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    public static final int MAX_LAYERS = 8;

    private static final VoxelShape[] SHAPES = new VoxelShape[MAX_LAYERS + 1];

    static {
        for (int layers = 0; layers <= MAX_LAYERS; layers++) {
            SHAPES[layers] = Block.box(0.0, 0.0, 0.0, 16.0, layers * 2.0, 16.0);
        }
    }

    public LOTRTreasurePileBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LAYERS, 1));
    }

    @Override
    protected MapCodec<LOTRTreasurePileBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(LAYERS)];
    }

    /**
     * getCollisionBoundingBoxFromPool snapped the collision height to one of
     * three steps -- a full block, a half block, or a sixteenth -- so that a
     * shallow heap is something you walk over rather than climb.
     */
    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        float height = state.getValue(LAYERS) / (float) MAX_LAYERS;
        int sixteenths = height >= 1.0f ? 16 : height >= 0.5f ? 8 : 1;
        return Block.box(0.0, 0.0, 0.0, 16.0, sixteenths, 16.0);
    }

    /** isSideSolid: only a full pile is something you can build off the top of. */
    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LAYERS) == MAX_LAYERS
                ? Shapes.block()
                : Shapes.empty();
    }

    /**
     * canBlockStay: a solid face beneath. Only the tick asks it -- placement
     * does not (canPlaceBlockAt was the default), so a pile can be put down
     * over air and then falls.
     */
    private static boolean hasFloor(LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
            BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState,
            RandomSource random) {
        // onNeighborBlockChange scheduled a tick at tickRate 2 and let the tick
        // decide whether to fall or to break.
        ticks.scheduleTick(pos, this, 2);
        return state;
    }

    /**
     * Right-clicking a pile with more of the same deepens it, as
     * onBlockActivated did -- and vanilla's snow uses the identical rule, so
     * canBeReplaced is all it takes.
     */
    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        int layers = state.getValue(LAYERS);
        if (context.getItemInHand().is(this.asItem()) && layers < MAX_LAYERS) {
            return context.replacingClickedOnBlock() ? context.getClickedFace() == Direction.UP : true;
        }
        return layers == 1;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
        if (existing.is(this)) {
            int layers = existing.getValue(LAYERS);
            return existing.setValue(LAYERS, Math.min(MAX_LAYERS, layers + 1));
        }
        return defaultBlockState();
    }

    /**
     * onEntityWalking and onFallenUpon: eight coins kicked up off the surface
     * of the heap, from whatever height the heap currently is.
     */
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        spray(level, pos, state);
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        spray(level, pos, state);
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    private void spray(Level level, BlockPos pos, BlockState state) {
        double top = state.getValue(LAYERS) / (double) MAX_LAYERS;
        for (int i = 0; i < 8; i++) {
            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state),
                    pos.getX() + level.getRandom().nextFloat(),
                    pos.getY() + top,
                    pos.getZ() + level.getRandom().nextFloat(),
                    Mth.randomBetween(level.getRandom(), -0.15f, 0.15f),
                    Mth.randomBetween(level.getRandom(), 0.1f, 0.4f),
                    Mth.randomBetween(level.getRandom(), -0.15f, 0.15f));
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // updateTick: tryFall first; only a pile that can neither fall nor
        // stand breaks. FallingBlock.tick does the falling.
        if (!isFree(level.getBlockState(pos.below())) && !hasFloor(level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }
        super.tick(state, level, pos, random);
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getMapColor(level, pos).col;
    }
}
