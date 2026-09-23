package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRBlockPlaceableFood: the cakes and pies, and the marchpane block.
 *
 * <p>Six bites, eaten from the west side as a vanilla cake is, and the item
 * only comes back if you break it whole. Each bite feeds whoever can eat:
 * addStats(healAmount, saturationAmount), which is FoodData.eat with the same
 * two numbers, then a burp. The metadata count is {@link #BITES} here.
 *
 * <p>foodHalfWidth and foodHeight are the original's fractions of a block --
 * 0.4375 wide and 0.5 high unless the constructor said otherwise.
 */
public class LOTRPlaceableFoodBlock extends Block {
    public static final MapCodec<LOTRPlaceableFoodBlock> CODEC =
            simpleCodec(props -> new LOTRPlaceableFoodBlock(0.4375f, 0.5f, 2, 0.1f, props));

    /** MAX_EATS. */
    public static final int MAX_EATS = 6;
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, MAX_EATS - 1);

    private final VoxelShape[] shapes = new VoxelShape[MAX_EATS];
    private final int healAmount;
    private final float saturationAmount;

    public LOTRPlaceableFoodBlock(float foodHalfWidth, float foodHeight, int healAmount, float saturationAmount,
            Properties properties) {
        super(properties);
        this.healAmount = healAmount;
        this.saturationAmount = saturationAmount;
        float min = 0.5f - foodHalfWidth;
        float max = 0.5f + foodHalfWidth;
        for (int bites = 0; bites < MAX_EATS; ++bites) {
            // setBlockBoundsBasedOnState: the west edge moves in a sixth per bite.
            float eaten = min + (max - min) * ((float) bites / MAX_EATS);
            shapes[bites] = Block.box(eaten * 16.0, 0.0, min * 16.0, max * 16.0, foodHeight * 16.0, max * 16.0);
        }
        registerDefaultState(getStateDefinition().any().setValue(BITES, 0));
    }

    @Override
    public MapCodec<? extends LOTRPlaceableFoodBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapes[state.getValue(BITES)];
    }

    /** onBlockActivated -> eatCake, whatever is in hand. */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide() && player.canEat(false)) {
            player.getFoodData().eat(healAmount, saturationAmount);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP,
                    SoundSource.PLAYERS, 0.5f, level.getRandom().nextFloat() * 0.1f + 0.9f);
            int bites = state.getValue(BITES) + 1;
            if (bites >= MAX_EATS) {
                level.removeBlock(pos, false);
            } else {
                level.setBlock(pos, state.setValue(BITES, bites), Block.UPDATE_ALL);
            }
        }
        return InteractionResult.SUCCESS;
    }

    /** canBlockStay: a solid top face underneath. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    /**
     * onNeighborBlockChange. Returning air here goes through destroyBlock, so
     * the loot table decides the drop -- the item, and only while uneaten, as
     * getDrops had it.
     */
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
            Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == Direction.DOWN && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
