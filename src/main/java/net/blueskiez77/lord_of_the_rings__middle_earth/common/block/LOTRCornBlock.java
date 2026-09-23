package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Corn stalks.
 *
 * <p>LOTRBlockCorn is a column like the reeds: it grows upward, and breaking one
 * block drops every block above it, through the same neighbour-change cascade
 * that {@link #updateShape} reproduces on VegetationBlock's behalf.
 *
 * <p>Two things differ from the 1.7.10 class on purpose, both asked for:
 * MAX_HEIGHT is 2 rather than the original's 3, and the stalk will only stand on
 * tilled soil. The original also accepted the Forge Beach plant type, which let
 * corn grow on riverside sand.
 *
 * <p>{@link #HAS_CORN} is the original's metadata bit 8: a stalk with a stalk
 * below it eventually grows an ear, which changes its texture. Harvesting the
 * ear by hand (onBlockActivated) strips it and drops one cob, two a quarter of
 * the time, as getCornDrops did; breaking a ripe stalk drops the same through
 * its loot table.
 */
public class LOTRCornBlock extends VegetationBlock implements BonemealableBlock {
    public static final MapCodec<LOTRCornBlock> CODEC = simpleCodec(LOTRCornBlock::new);

    /** Deliberately 2, not LOTRBlockCorn.MAX_GROW_HEIGHT's 3. */
    public static final int MAX_HEIGHT = 2;

    /** LOTRBlockCorn.META_GROW_END: the low three metadata bits ran 0..7. */
    public static final int GROW_END = 7;

    public static final BooleanProperty HAS_CORN = BooleanProperty.create("has_corn");
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    public LOTRCornBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(HAS_CORN, false).setValue(AGE, 0));
    }

    @Override
    public MapCodec<LOTRCornBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_CORN, AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState below, BlockGetter level, BlockPos belowPos) {
        return below.is(this) || below.getBlock() instanceof FarmlandBlock;
    }

    // LOTRBlockCorn.onBlockActivated: whatever the player holds, a ripe ear
    // comes off in their hand.
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!state.getValue(HAS_CORN)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            level.setBlock(pos, state.setValue(HAS_CORN, false), Block.UPDATE_ALL);
            int corns = level.getRandom().nextInt(4) == 0 ? 2 : 1;
            for (int i = 0; i < corns; i++) {
                popResource(level, pos, new ItemStack(LOTRItems.CORN));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int height = columnHeight(level, pos);

        if (level.getBlockState(pos.above()).isAir() && height < MAX_HEIGHT) {
            int age = state.getValue(AGE);
            if (age < GROW_END) {
                level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_INVISIBLE);
            } else {
                growUp(level, pos, state);
                return;
            }
        }

        // LOTRBlockCorn.canGrowCorn: only a stalk standing ON another stalk
        // ever grows an ear, so the bottom block never does.
        if (!state.getValue(HAS_CORN) && level.getBlockState(pos.below()).is(this)
                && random.nextFloat() < growthChance(level, pos.below(height - 1))) {
            level.setBlock(pos, state.setValue(HAS_CORN, true), Block.UPDATE_ALL);
        }
    }

    /**
     * LOTRBlockCorn.getGrowthFactor, measured at the foot of the column: 3 on
     * soil the wheat would accept, 9 if that soil is wet, tripled again while it
     * rains, over 250.
     */
    private float growthChance(Level level, BlockPos foot) {
        float growth = 1.0F;
        BlockState soil = level.getBlockState(foot.below());
        if (soil.getBlock() instanceof FarmlandBlock) {
            growth = soil.getValue(FarmlandBlock.MOISTURE) > 0 ? 9.0F : 3.0F;
        }
        if (level.isRaining()) {
            growth *= 3.0F;
        }
        return growth / 250.0F;
    }

    // LOTRBlockCorn implemented IGrowable: bone meal either adds the next
    // segment (on a stalk with nothing above and nothing below) or, half the
    // time, ripens an ear on a stalk that is standing on another stalk.
    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        boolean canGrowUp = level.getBlockState(pos.above()).isAir()
                && columnHeight(level, pos) < MAX_HEIGHT;
        boolean canRipen = !state.getValue(HAS_CORN) && level.getBlockState(pos.below()).is(this);
        return canGrowUp || canRipen;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (level.getBlockState(pos.above()).isAir() && columnHeight(level, pos) < MAX_HEIGHT) {
            growUp(level, pos, state);
            return;
        }
        if (!state.getValue(HAS_CORN) && level.getBlockState(pos.below()).is(this)
                && random.nextInt(2) == 0) {
            level.setBlock(pos, state.setValue(HAS_CORN, true), Block.UPDATE_ALL);
        }
    }

    /**
     * Adds the next segment. The AGE reset is written FIRST: placing the block
     * above sends this one an update, and writing the captured old state
     * afterwards would put back whatever that update changed.
     */
    private void growUp(ServerLevel level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(AGE, 0), Block.UPDATE_CLIENTS);
        level.setBlockAndUpdate(pos.above(), defaultBlockState());
    }

    private int columnHeight(LevelReader level, BlockPos pos) {
        int height = 1;
        while (level.getBlockState(pos.below(height)).is(this)) {
            height++;
        }
        return height;
    }
}
