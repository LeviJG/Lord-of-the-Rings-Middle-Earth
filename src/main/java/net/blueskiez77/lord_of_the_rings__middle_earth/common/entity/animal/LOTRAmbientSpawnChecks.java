package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRAmbientSpawnChecks: a small creature spawns on its biome's own top block, in light enough
 * (above sea level, a little; below, bright), and only where enough of what it lives among --
 * plants, or water -- lies about.
 */
public final class LOTRAmbientSpawnChecks {

    /** Material.plants and Material.vine: flowers, grasses, saplings, crops, vines and the like. */
    public static final Predicate<BlockState> PLANTS = state -> state.getBlock() instanceof VegetationBlock || state.is(Blocks.VINE);
    /** Material.water. */
    public static final Predicate<BlockState> WATER = state -> state.getFluidState().is(FluidTags.WATER);

    private LOTRAmbientSpawnChecks() {
    }

    public static boolean canSpawn(Mob entity, LevelAccessor world, int xzRange, int yRange, int attempts, int required,
                                   Predicate<BlockState> materials) {
        RandomSource rand = entity.getRandom();
        int i = Mth.floor(entity.getX());
        int j = Mth.floor(entity.getY());
        int k = Mth.floor(entity.getZ());
        BlockState below = world.getBlockState(new BlockPos(i, j - 1, k));
        LOTRBiome biome = LOTRBiomes.of(world.getBiome(new BlockPos(i, j, k)));
        if (biome != null && below.is(biome.topBlock.getBlock())) {
            int light = world.getMaxLocalRawBrightness(new BlockPos(i, j, k));
            if (j >= 62 && light >= rand.nextInt(8) || light >= 8) {
                int counted = 0;
                for (int l = 0; l < attempts; ++l) {
                    int i1 = i + rand.nextInt(xzRange) - rand.nextInt(xzRange);
                    int k1 = k + rand.nextInt(xzRange) - rand.nextInt(xzRange);
                    int j1 = j + rand.nextInt(yRange) - rand.nextInt(yRange);
                    BlockPos pos = new BlockPos(i1, j1, k1);
                    if (!world.hasChunkAt(pos) || !materials.test(world.getBlockState(pos)) || ++counted <= required) {
                        continue;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
