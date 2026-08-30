package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRBlockOrcChain. A length of chain that hangs from the ceiling, which you
 * can climb, and which chandeliers hang from in turn.
 *
 * <p>Four things it does that a decorative block would not:
 *
 * <ul>
 *   <li><b>It must hang from something.</b> {@code canPlaceBlockAt} accepts
 *       another orc chain, a fence, a wall, a bottom slab, a bottom-half stair,
 *       or any block with a solid underside. Take the support away and the
 *       chain pops off and drops -- which, because a chain counts as support
 *       for the one below it, unravels the whole run from the top down.</li>
 *   <li><b>It is a ladder.</b> {@code isLadder} returns true unconditionally, so
 *       a hanging chain is a climbable shaft. That is the climbable block tag
 *       here.</li>
 *   <li><b>Right-clicking it with another chain extends it downward.</b> The
 *       original walks down from the clicked block for as long as it keeps
 *       finding chain, then places one in the first free space below the run --
 *       so you build a chain down a shaft from the top without having to aim at
 *       its far end.</li>
 *   <li><b>It picks its texture from its neighbours</b>: middle with chain both
 *       above and below, bottom with chain only above, top with chain only
 *       below, single with neither. A {@link LOTRChandelierBlock} underneath
 *       counts as "below", so a chain meeting a chandelier draws its linking
 *       end rather than a loose end.</li>
 * </ul>
 *
 * <p>Its collision box is a 0.02-wide thread, so it never gets in your way even
 * though the block it occupies is a fifth of a block across visually.
 */
public class LOTROrcChainBlock extends Block {

    public static final MapCodec<LOTROrcChainBlock> CODEC = simpleCodec(LOTROrcChainBlock::new);

    /** getIcon's two neighbour tests, hoisted into the blockstate. */
    public static final BooleanProperty CHAIN_ABOVE = BooleanProperty.create("chain_above");
    public static final BooleanProperty CHAIN_BELOW = BooleanProperty.create("chain_below");

    /** setBlockBounds(0.5 +/- 0.2, 0..1, 0.5 +/- 0.2). */
    private static final VoxelShape SHAPE = Block.box(4.8, 0.0, 4.8, 11.2, 16.0, 11.2);

    /** getCollisionBoundingBoxFromPool uses a far thinner box: 0.5 +/- 0.01. */
    private static final VoxelShape COLLISION_SHAPE = Block.box(7.84, 0.0, 7.84, 8.16, 16.0, 8.16);

    public LOTROrcChainBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
                .setValue(CHAIN_ABOVE, false)
                .setValue(CHAIN_BELOW, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHAIN_ABOVE, CHAIN_BELOW);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
                                           CollisionContext context) {
        return COLLISION_SHAPE;
    }

    // ---- hanging ----------------------------------------------------------

    /**
     * canPlaceBlockAt: what will a chain hang from? Everything here is judged
     * by the block ABOVE, never by the sides -- an orc chain has no wall
     * mounting.
     */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        Block block = aboveState.getBlock();

        if (block instanceof LOTROrcChainBlock || block instanceof FenceBlock || block instanceof WallBlock) {
            return true;
        }
        // A slab or stair only holds the chain when its solid half is the
        // bottom one -- metadata bits 8 and 4 in the original.
        if (block instanceof SlabBlock && aboveState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
            return true;
        }
        if (block instanceof StairBlock && aboveState.getValue(StairBlock.HALF) == Half.BOTTOM) {
            return true;
        }
        return aboveState.isFaceSturdy(level, above, Direction.DOWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return connect(defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    /**
     * onNeighborBlockChange plus the icon refresh in one: an unsupported chain
     * becomes air -- vanilla then drops it -- and a supported one re-reads its
     * two neighbours.
     */
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
                                     BlockPos pos, Direction direction, BlockPos neighbourPos,
                                     BlockState neighbourState, RandomSource random) {
        if (!canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return connect(state, level, pos);
    }

    private static BlockState connect(BlockState state, LevelReader level, BlockPos pos) {
        Block above = level.getBlockState(pos.above()).getBlock();
        Block below = level.getBlockState(pos.below()).getBlock();
        return state
                .setValue(CHAIN_ABOVE, above instanceof LOTROrcChainBlock)
                // A chandelier hanging beneath is part of the same run.
                .setValue(CHAIN_BELOW, below instanceof LOTROrcChainBlock
                        || below instanceof LOTRChandelierBlock);
    }

    // ---- extending downward ------------------------------------------------

    /**
     * onBlockActivated. Walk down from the clicked block for as long as there is
     * chain, then drop one more into the first space below the run. Placing at
     * the far end of a long chain would otherwise mean flying down to it.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.is(asItem())) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        BlockPos.MutableBlockPos cursor = pos.mutable();
        while (!level.isOutsideBuildHeight(cursor) && level.getBlockState(cursor).is(this)) {
            cursor.move(Direction.DOWN);
        }
        if (level.isOutsideBuildHeight(cursor)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        BlockState target = level.getBlockState(cursor);
        if (!target.canBeReplaced() || !target.getFluidState().isEmpty()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        BlockPos placeAt = cursor.immutable();
        if (!level.isClientSide()) {
            level.setBlock(placeAt, connect(defaultBlockState(), level, placeAt), Block.UPDATE_ALL);
            SoundType sound = getSoundType(state);
            level.playSound(null, placeAt, sound.getPlaceSound(), SoundSource.BLOCKS,
                    (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
            stack.consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }
}
