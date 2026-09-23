package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRBlockGrapevine with hasGrapes: a vine growing up a post, ripening over
 * eight stages. Right-click a ripe one and you pick the bunch, leaving the bare
 * post behind -- and it takes bone meal, two to five stages at a time.
 *
 * <p>It stands where the bare post does: on soil, on anything solid-topped, or
 * on another vine, so it climbs.
 */
public class LOTRGrapevineBlock extends Block implements BonemealableBlock {
    public static final MapCodec<LOTRGrapevineBlock> CODEC = simpleCodec(LOTRGrapevineBlock::new);

    /** The original's metadata, 0 to 7. */
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;

    /** setBlockBoundsForItemRender's f = 0.125: a four-pixel post. */
    private static final VoxelShape SHAPE = box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public LOTRGrapevineBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected MapCodec<? extends LOTRGrapevineBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /** canPlaceBlockAt: soil, a solid top, or another vine to climb. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState state2 = level.getBlockState(below);
        return state2.is(this) || state2.is(LOTRBlocks.GRAPEVINE)
                || state2.is(net.minecraft.tags.BlockTags.SUPPORTS_VEGETATION)
                || state2.isFaceSturdy(level, below, net.minecraft.core.Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, net.minecraft.world.level.ScheduledTickAccess ticks,
            BlockPos pos, net.minecraft.core.Direction direction, BlockPos neighbourPos, BlockState neighbourState,
            RandomSource random) {
        // checkCanStay: a vine left without support falls back to the bare post.
        return canSurvive(state, level, pos) ? state : LOTRBlocks.GRAPEVINE.defaultBlockState();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE && level.getRawBrightness(pos, 0) >= 9 && random.nextInt(12) == 0) {
            level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_CLIENTS);
        }
    }

    /** The harvest: the bunch comes off and the post stays. */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hit) {
        if (state.getValue(AGE) < MAX_AGE) {
            return InteractionResult.PASS;
        }
        if (level instanceof ServerLevel server) {
            int grapes = 1 + server.getRandom().nextInt(2) + (server.getRandom().nextInt(3) == 0 ? 1 : 0);
            popResource(server, pos, new ItemStack(grapeItem(), grapes));
            int seeds = server.getRandom().nextInt(3);
            if (seeds > 0) {
                popResource(server, pos, new ItemStack(asItem(), seeds));
            }
            server.setBlock(pos, LOTRBlocks.GRAPEVINE.defaultBlockState(), Block.UPDATE_ALL);
        }
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0f,
                0.8f + level.getRandom().nextFloat() * 0.4f);
        return InteractionResult.SUCCESS;
    }

    private net.minecraft.world.item.Item grapeItem() {
        return this == LOTRBlocks.RED_GRAPEVINE
                ? net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.RED_GRAPES
                : net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.GREEN_GRAPES;
    }

    // --- bone meal: func_149853_b added two to five stages at once ----------

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = Mth.clamp(state.getValue(AGE) + Mth.nextInt(random, 2, 5), 0, MAX_AGE);
        level.setBlock(pos, state.setValue(AGE, age), Block.UPDATE_CLIENTS);
    }
}
