package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/** 1.7.10's World height lookups and MathHelper's random range, as the original's generators used them. */
public final class LOTRWorldGenUtil {

    private LOTRWorldGenUtil() {
    }

    /** getHeightValue: one above the highest block that stops light (fluids and leaves among them). */
    public static int getHeightValue(WorldGenLevel world, int x, int z) {
        return world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
    }

    /**
     * getTopSolidOrLiquidBlock: despite its name, one above the highest block that stops movement
     * (has a collision shape) and is not leaves -- water and lava are passed through.
     */
    public static int getTopSolidOrLiquidBlock(WorldGenLevel world, int x, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 0, z);
        for (int y = world.getHeight(Heightmap.Types.WORLD_SURFACE, x, z); y > world.getMinY(); --y) {
            BlockState state = world.getBlockState(pos.setY(y));
            if (!state.getCollisionShape(world, pos).isEmpty() && !state.is(BlockTags.LEAVES)) {
                return y + 1;
            }
        }
        return -1;
    }

    /** MathHelper.getRandomIntegerInRange: min to max inclusive, min if max is not above it. */
    public static int getRandomIntegerInRange(RandomSource random, int min, int max) {
        return min >= max ? min : random.nextInt(max - min + 1) + min;
    }

    /** MathHelper.randomFloatClamp. */
    public static float randomFloatClamp(RandomSource random, float min, float max) {
        return min >= max ? min : random.nextFloat() * (max - min) + min;
    }

    public static int floor(double d) {
        return Mth.floor(d);
    }

    /** isBlockFreezable: still water, open to the sky, where the biome is cold enough to freeze it. */
    public static boolean isBlockFreezable(WorldGenLevel world, BlockPos pos) {
        return world.getBiome(pos).value().shouldFreeze(world, pos);
    }

    /** canSnowAt (func_147478_e): where snow may settle, the Middle-earth biome's snow line allowing. */
    public static boolean canSnowAt(WorldGenLevel world, BlockPos pos) {
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome biome =
                net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes.of(world.getBiome(pos));
        if (biome != null && pos.getY() < biome.getSnowHeight()) {
            return false;
        }
        return world.getBiome(pos).value().shouldSnow(world, pos);
    }
}
