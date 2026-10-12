package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMudGrassBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTREventHandler.onUseBonemeal: bone meal on grass in one of the mod's biomes grows that biome's
 * own grasses (getRandomGrass) and, one time in eight, its flowers (plantFlower), in place of
 * vanilla's.
 */
public final class LOTRBiomeBoneMeal {

    private LOTRBiomeBoneMeal() {
    }

    /** Whether bone meal on this block, here, grows the biome's plants. */
    public static boolean applies(ServerLevel level, BlockPos pos, BlockState state) {
        return (state.is(Blocks.GRASS_BLOCK) || state.getBlock() instanceof LOTRMudGrassBlock)
                && LOTRBiomes.of(level.getBiome(pos)) != null;
    }

    public static void grow(ServerLevel level, RandomSource rand, BlockPos pos) {
        LOTRBiome biome = LOTRBiomes.of(level.getBiome(pos));
        if (biome == null) {
            return;
        }
        attempts:
        for (int attempts = 0; attempts < 128; ++attempts) {
            BlockPos pos1 = pos.above();
            for (int subAttempts = 0; subAttempts < attempts / 16; ++subAttempts) {
                pos1 = pos1.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                BlockState below = level.getBlockState(pos1.below());
                if (!(below.getBlock() instanceof BonemealableBlock)
                        || !Blocks.SHORT_GRASS.defaultBlockState().canSurvive(level, pos1)
                        || level.getBlockState(pos1).isRedstoneConductor(level, pos1)) {
                    continue attempts;
                }
            }
            if (!level.isEmptyBlock(pos1)) {
                continue;
            }
            if (rand.nextInt(8) > 0) {
                LOTRBiome.GrassBlockAndMeta grass = biome.getRandomGrass(rand);
                BlockState state = grass.block().state(grass.meta());
                if (!state.canSurvive(level, pos1)) {
                    continue;
                }
                level.setBlock(pos1, state, 3);
                continue;
            }
            LOTRBiome.FlowerEntry flower = biome.getRandomFlower(level, rand, pos1.getX(), pos1.getY(), pos1.getZ());
            if (flower == null || !flower.state().canSurvive(level, pos1)) {
                continue;
            }
            level.setBlock(pos1, flower.state(), 3);
        }
    }
}
