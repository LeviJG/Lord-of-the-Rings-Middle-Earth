package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;

/**
 * LOTRBlockMudGrass: grass on mud. Its tick was the grass block's, which the
 * original's coremod rewrote (LOTRReplacedMethods.Grass): smothered, it turns
 * back to mud; in good light it spreads -- grass onto dirt and mud grass onto
 * mud, whichever is near -- by modern grass's rules for light. Bone meal does
 * what it does on grass, as the original handed it to Blocks.grass.
 */
public class LOTRMudGrassBlock extends Block implements BonemealableBlock {

    public LOTRMudGrassBlock(Properties properties) {
        super(properties);
    }

    /** SpreadingSnowyBlock.canStayAlive. */
    public static boolean canStayAlive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (aboveState.is(Blocks.SNOW) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        }
        if (aboveState.getFluidState().isFull()) {
            return false;
        }
        return LightEngine.getLightDampeningInto(state, aboveState, Direction.UP, aboveState.getLightDampening()) < 15;
    }

    /** SpreadingSnowyBlock.canPropagate. */
    public static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        return canStayAlive(state, level, pos) && !level.getFluidState(pos.above()).is(FluidTags.WATER);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canStayAlive(state, level, pos)) {
            level.setBlockAndUpdate(pos, LOTRBuildingBlocks.MUD.defaultBlockState());
            return;
        }
        if (level.getMaxLocalRawBrightness(pos.above()) < 9) {
            return;
        }
        for (int l = 0; l < 4; ++l) {
            BlockPos testPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
            BlockState test = level.getBlockState(testPos);
            if (test.is(Blocks.DIRT) && canPropagate(Blocks.GRASS_BLOCK.defaultBlockState(), level, testPos)) {
                level.setBlockAndUpdate(testPos, Blocks.GRASS_BLOCK.defaultBlockState()
                        .setValue(SnowyBlock.SNOWY, level.getBlockState(testPos.above()).is(BlockTags.SNOW)));
            } else if (test.is(LOTRBuildingBlocks.MUD) && canPropagate(defaultBlockState(), level, testPos)) {
                level.setBlockAndUpdate(testPos, defaultBlockState());
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return ((BonemealableBlock) Blocks.GRASS_BLOCK).isValidBonemealTarget(level, pos, state);
    }

    @Override
    public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource random, BlockPos pos, BlockState state) {
        return ((BonemealableBlock) Blocks.GRASS_BLOCK).isBonemealSuccess(level, random, pos, state);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        ((BonemealableBlock) Blocks.GRASS_BLOCK).performBonemeal(level, random, pos, state);
    }
}
