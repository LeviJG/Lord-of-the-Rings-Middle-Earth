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
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;
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

    /**
     * LOTRBlockGrapevine.canPlantGrapesAt: farmland no more than three blocks
     * below, reached through nothing but grapevine posts and vines.
     */
    public static boolean canPlantGrapesAt(LevelReader level, BlockPos pos) {
        for (int l = 1; l <= 3; l++) {
            BlockState below = level.getBlockState(pos.below(l));
            if (below.getBlock() instanceof FarmlandBlock) {
                return true;
            }
            if (!below.is(LOTRBlocks.GRAPEVINE) && !(below.getBlock() instanceof LOTRGrapevineBlock)) {
                return false;
            }
        }
        return false;
    }

    /**
     * LOTRBlockGrapevine.getGrowthFactor: 1, plus the 3x3 of farmland at the
     * vine's farmland level -- 1 for dry and 3 for wet farmland, the eight
     * around the centre counting a quarter.
     *
     * <p>The original also multiplied by 1.6 in the Dorwinion biome. That
     * biome does not exist in the port yet (Track D); add it with the biomes.
     */
    private static float growthFactor(LevelReader level, BlockPos pos) {
        if (!canPlantGrapesAt(level, pos)) {
            return 0.0F;
        }
        int farmlandY = pos.getY();
        for (int l = 1; l <= 3; l++) {
            if (level.getBlockState(pos.below(l)).getBlock() instanceof FarmlandBlock) {
                farmlandY = pos.getY() - l;
                break;
            }
        }
        float growth = 1.0F;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockState soil = level.getBlockState(new BlockPos(pos.getX() + dx, farmlandY, pos.getZ() + dz));
                float f = 0.0F;
                if (soil.getBlock() instanceof FarmlandBlock) {
                    f = soil.getValue(FarmlandBlock.MOISTURE) > 0 ? 3.0F : 1.0F;
                }
                if (dx != 0 || dz != 0) {
                    f /= 4.0F;
                }
                growth += f;
            }
        }
        return growth;
    }

    // LOTRBlockGrapevine.updateTick: light 9, and a 1 in (80 / growth + 1)
    // chance a tick. The light test is the one vanilla crops use today
    // (CropBlock: getRawBrightness(pos, 0)); the original copied 1.7.10's crop
    // test, which looked at the block above.
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE && level.getRawBrightness(pos, 0) >= 9) {
            float growth = growthFactor(level, pos);
            if (growth > 0.0F && random.nextInt((int) (80.0F / growth) + 1) == 0) {
                level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_CLIENTS);
            }
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
            // LOTRBlockGrapevine.getVineDrops(meta 7, fortune 0): three tries at
            // a seed, each landing if nextInt(15) <= 7; one bunch, two a third
            // of the time.
            RandomSource random = server.getRandom();
            int seeds = 0;
            for (int l = 0; l < 3; l++) {
                if (random.nextInt(15) <= MAX_AGE) {
                    seeds++;
                }
            }
            int grapes = random.nextInt(3) == 0 ? 2 : 1;
            popResource(server, pos, new ItemStack(grapeItem(), grapes));
            if (seeds > 0) {
                popResource(server, pos, new ItemStack(asItem(), seeds));
            }
            server.setBlock(pos, LOTRBlocks.GRAPEVINE.defaultBlockState(), Block.UPDATE_ALL);
        }
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0f,
                0.8f + level.getRandom().nextFloat() * 0.4f);
        return InteractionResult.SUCCESS;
    }

    // LOTRBlockGrapevine.removedByPlayer: a player breaking the vine takes the
    // grapes and seeds (the loot table) but leaves the post standing.
    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
            @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (level.getBlockState(pos).isAir()) {
            level.setBlock(pos, LOTRBlocks.GRAPEVINE.defaultBlockState(), Block.UPDATE_ALL);
        }
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
