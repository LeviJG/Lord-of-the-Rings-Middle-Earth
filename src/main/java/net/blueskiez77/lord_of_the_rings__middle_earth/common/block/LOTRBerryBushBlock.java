package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.tags.BlockTags;

/**
 * A berry bush.
 *
 * <p>LOTRBlockBerryBush extends plain Block and never overrides getRenderType,
 * so 1.7.10 drew it as an ordinary full cube -- the same way it drew leaves. It
 * is not a cross and not a bush shape, which is why it keeps Block here rather
 * than joining LOTRPlantBlock.
 *
 * <p>{@link #HAS_BERRIES} is the original's metadata bit 8. A bush ripens on a
 * random tick at a rate from getGrowthFactor, and bone meal ripens it outright
 * one time in three, matching func_149853_b.
 *
 * <p>Right-clicking a ripe bush strips it and drops one to four of its berry,
 * as onBlockActivated did; breaking a ripe bush drops them through its loot
 * table instead.
 */
public class LOTRBerryBushBlock extends Block implements BonemealableBlock {
    public static final MapCodec<LOTRBerryBushBlock> CODEC = simpleCodec(LOTRBerryBushBlock::new);

    public static final BooleanProperty HAS_BERRIES = BooleanProperty.create("has_berries");

    public LOTRBerryBushBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(HAS_BERRIES, false));
    }

    @Override
    public MapCodec<LOTRBerryBushBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_BERRIES);
    }

    /** getBerryDrops' switch on the berry type. */
    private Item berry() {
        if (this == LOTRBlocks.BERRY_BUSH_BLUEBERRY) {
            return LOTRItems.BLUEBERRIES;
        }
        if (this == LOTRBlocks.BERRY_BUSH_BLACKBERRY) {
            return LOTRItems.BLACKBERRIES;
        }
        if (this == LOTRBlocks.BERRY_BUSH_RASPBERRY) {
            return LOTRItems.RASPBERRIES;
        }
        if (this == LOTRBlocks.BERRY_BUSH_CRANBERRY) {
            return LOTRItems.CRANBERRIES;
        }
        if (this == LOTRBlocks.BERRY_BUSH_ELDERBERRY) {
            return LOTRItems.ELDERBERRIES;
        }
        return LOTRItems.WILDBERRIES;
    }

    /** onBlockActivated: whatever is in hand, a ripe bush gives up its berries. */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!state.getValue(HAS_BERRIES)) {
            return InteractionResult.PASS;
        }
        level.setBlock(pos, state.setValue(HAS_BERRIES, false), Block.UPDATE_ALL);
        if (!level.isClientSide()) {
            int berries = 1 + level.getRandom().nextInt(4);
            for (int i = 0; i < berries; i++) {
                popResource(level, pos, new ItemStack(berry()));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(HAS_BERRIES) && random.nextFloat() < growthChance(level, pos)) {
            level.setBlock(pos, state.setValue(HAS_BERRIES, true), Block.UPDATE_ALL);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !state.getValue(HAS_BERRIES);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        // func_149853_b: one try in three actually takes.
        if (random.nextInt(3) == 0) {
            level.setBlock(pos, state.setValue(HAS_BERRIES, true), Block.UPDATE_ALL);
        }
    }

    /**
     * LOTRBlockBerryBush.getGrowthFactor, kept in shape if not in every detail:
     * a bush on soil in light 9 or better counts the soil in the 3x3 under and
     * around it, scoring 1 for soil and 3 for wet soil (neighbours at a quarter
     * weight), halves the result if another bush is adjacent, triples it while
     * it rains, and divides by 150. In poorer light it falls back to the light
     * level over 2000.
     */
    private float growthChance(Level level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        if (!below.is(BlockTags.SUPPORTS_VEGETATION)) {
            return 0.0F;
        }
        int light = level.getMaxLocalRawBrightness(pos.above());
        if (light < 9) {
            // The fallback for soil that would take a sapling: the light alone,
            // over 2000, tripled while it rains.
            float growth = light / 2000.0F;
            return level.isRaining() ? growth * 3.0F : growth;
        }

        float growth = 1.0F;
        boolean bushAdjacent = false;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos side = pos.offset(dx, 0, dz);
                if ((dx != 0 || dz != 0) && level.getBlockState(side).getBlock() instanceof LOTRBerryBushBlock) {
                    bushAdjacent = true;
                }
                BlockState soil = level.getBlockState(side.below());
                float bonus = 0.0F;
                if (soil.is(BlockTags.SUPPORTS_VEGETATION)) {
                    bonus = soil.getBlock() instanceof FarmlandBlock
                            && soil.getValue(FarmlandBlock.MOISTURE) > 0 ? 3.0F : 1.0F;
                }
                if (dx != 0 || dz != 0) {
                    bonus /= 4.0F;
                }
                growth += bonus;
            }
        }
        if (bushAdjacent) {
            growth /= 2.0F;
        }
        if (level.isRaining()) {
            growth *= 3.0F;
        }
        return growth / 150.0F;
    }
}
