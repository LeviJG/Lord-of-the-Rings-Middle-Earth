package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * 1.7.10's WorldGenLakes: a lake of a few overlapping blobs in a 16 x 8 x 16 space, the fluid in
 * the lower half and air above; not where it would spill or break into other liquid. A lava lake
 * is walled with stone; a water lake's shore grasses over where it sees the sky, and it freezes
 * where the biome is cold enough.
 */
public class LOTRWorldGenLakes implements LOTRWorldGenerator {

    private final BlockState fluid;

    public LOTRWorldGenLakes(BlockState fluid) {
        this.fluid = fluid;
    }

    private static boolean isSolid(BlockState state) {
        return !state.isAir() && state.getFluidState().isEmpty() && state.canOcclude();
    }

    private static boolean isBorder(boolean[] lake, int i1, int j2, int j1) {
        return !lake[(i1 * 16 + j2) * 8 + j1] && (i1 < 15 && lake[((i1 + 1) * 16 + j2) * 8 + j1]
                || i1 > 0 && lake[((i1 - 1) * 16 + j2) * 8 + j1]
                || j2 < 15 && lake[(i1 * 16 + j2 + 1) * 8 + j1]
                || j2 > 0 && lake[(i1 * 16 + (j2 - 1)) * 8 + j1]
                || j1 < 7 && lake[(i1 * 16 + j2) * 8 + j1 + 1]
                || j1 > 0 && lake[(i1 * 16 + j2) * 8 + (j1 - 1)]);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
        x -= 8;
        z -= 8;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        while (y > 5 && world.isEmptyBlock(pos.set(x, y, z))) {
            --y;
        }
        if (y <= 4) {
            return false;
        }
        y -= 4;
        boolean[] lake = new boolean[2048];
        int blobs = rand.nextInt(4) + 4;
        for (int i1 = 0; i1 < blobs; ++i1) {
            double d0 = rand.nextDouble() * 6.0 + 3.0;
            double d1 = rand.nextDouble() * 4.0 + 2.0;
            double d2 = rand.nextDouble() * 6.0 + 3.0;
            double d3 = rand.nextDouble() * (16.0 - d0 - 2.0) + 1.0 + d0 / 2.0;
            double d4 = rand.nextDouble() * (8.0 - d1 - 4.0) + 2.0 + d1 / 2.0;
            double d5 = rand.nextDouble() * (16.0 - d2 - 2.0) + 1.0 + d2 / 2.0;
            for (int k1 = 1; k1 < 15; ++k1) {
                for (int l1 = 1; l1 < 15; ++l1) {
                    for (int i2 = 1; i2 < 7; ++i2) {
                        double d6 = (k1 - d3) / (d0 / 2.0);
                        double d7 = (i2 - d4) / (d1 / 2.0);
                        double d8 = (l1 - d5) / (d2 / 2.0);
                        if (d6 * d6 + d7 * d7 + d8 * d8 < 1.0) {
                            lake[(k1 * 16 + l1) * 8 + i2] = true;
                        }
                    }
                }
            }
        }
        for (int i1 = 0; i1 < 16; ++i1) {
            for (int j2 = 0; j2 < 16; ++j2) {
                for (int j1 = 0; j1 < 8; ++j1) {
                    if (isBorder(lake, i1, j2, j1)) {
                        BlockState state = world.getBlockState(pos.set(x + i1, y + j1, z + j2));
                        if (j1 >= 4 && !state.getFluidState().isEmpty()) {
                            return false;
                        }
                        if (j1 < 4 && !isSolid(state) && state.getBlock() != this.fluid.getBlock()) {
                            return false;
                        }
                    }
                }
            }
        }
        for (int i1 = 0; i1 < 16; ++i1) {
            for (int j2 = 0; j2 < 16; ++j2) {
                for (int j1 = 0; j1 < 8; ++j1) {
                    if (lake[(i1 * 16 + j2) * 8 + j1]) {
                        world.setBlock(pos.set(x + i1, y + j1, z + j2), j1 >= 4 ? Blocks.AIR.defaultBlockState() : this.fluid, Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
        for (int i1 = 0; i1 < 16; ++i1) {
            for (int j2 = 0; j2 < 16; ++j2) {
                for (int j1 = 4; j1 < 8; ++j1) {
                    if (lake[(i1 * 16 + j2) * 8 + j1] && world.getBlockState(pos.set(x + i1, y + j1 - 1, z + j2)).is(Blocks.DIRT)
                            && y + j1 >= world.getHeight(Heightmap.Types.WORLD_SURFACE, x + i1, z + j2)) {
                        world.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
        if (this.fluid.getFluidState().is(FluidTags.LAVA)) {
            for (int i1 = 0; i1 < 16; ++i1) {
                for (int j2 = 0; j2 < 16; ++j2) {
                    for (int j1 = 0; j1 < 8; ++j1) {
                        if (isBorder(lake, i1, j2, j1) && (j1 < 4 || rand.nextInt(2) != 0)
                                && isSolid(world.getBlockState(pos.set(x + i1, y + j1, z + j2)))) {
                            world.setBlock(pos, Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
                        }
                    }
                }
            }
        }
        if (this.fluid.getFluidState().is(FluidTags.WATER)) {
            for (int i1 = 0; i1 < 16; ++i1) {
                for (int j2 = 0; j2 < 16; ++j2) {
                    pos.set(x + i1, y + 4, z + j2);
                    if (LOTRWorldGenUtil.isBlockFreezable(world, pos)) {
                        world.setBlock(pos, Blocks.ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
        return true;
    }
}
