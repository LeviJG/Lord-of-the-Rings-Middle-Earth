package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen;

import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMordorBiome;

import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

/**
 * LOTRMapGenRavine, on 1.7.10's MapGenRavine: ravines through the land's own blocks, lava at their
 * floors below y 10 -- below 60 in Gorgoroth, which is riven with them (twenty tries a chunk where
 * elsewhere half the chunks get one).
 */
public class LOTRMapGenRavine extends LOTRMapGenBase {

    private static final BlockState FLOWING_LAVA = Blocks.LAVA.defaultBlockState();
    private final float[] ravineNoise = new float[1024];

    private static boolean isGorgoroth(LOTRBiome biome) {
        return biome instanceof LOTRMordorBiome mordor && mordor.isGorgoroth();
    }

    private void digBlock(BlockState[] data, int index, int i, int j, int k, boolean topBlock) {
        LOTRBiome biome = biomeAt(i, k);
        if (!LOTRMapGenCaves.isTerrainBlock(data[index], biome)) {
            return;
        }
        if (j < 10 || isGorgoroth(biome) && j < 60) {
            data[index] = FLOWING_LAVA;
        } else {
            data[index] = LOTRChunkTerrain.AIR;
            if (topBlock && data[index - 1] == biome.fillerBlock) {
                data[index - 1] = biome.topBlock;
            }
        }
    }

    @Override
    protected void recursiveGenerate(int i, int k, int chunkX, int chunkZ, LOTRChunkTerrain terrain) {
        if (this.rand.nextBoolean()) {
            vanillaRavine(i, k, chunkX, chunkZ, terrain);
        } else if (isGorgoroth(biomeAt(0, 0))) {
            for (int l = 0; l < 20; ++l) {
                vanillaRavine(i, k, chunkX, chunkZ, terrain);
            }
        }
    }

    /** MapGenRavine.func_151538_a: one chunk in fifty starts a ravine. */
    private void vanillaRavine(int i, int k, int chunkX, int chunkZ, LOTRChunkTerrain terrain) {
        if (this.rand.nextInt(50) != 0) {
            return;
        }
        double x = i * 16 + this.rand.nextInt(16);
        double y = this.rand.nextInt(this.rand.nextInt(40) + 8) + 20;
        double z = k * 16 + this.rand.nextInt(16);
        float angle = this.rand.nextFloat() * (float) Math.PI * 2.0f;
        float pitch = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
        float size = (this.rand.nextFloat() * 2.0f + this.rand.nextFloat()) * 2.0f;
        carveRavine(this.rand.nextLong(), chunkX, chunkZ, terrain.blocks, x, y, z, size, angle, pitch, 0, 0, 3.0);
    }

    private void carveRavine(long seed, int chunkX, int chunkZ, BlockState[] blocks, double d, double d1, double d2, float f,
                             float ravineAngle, float f2, int step, int steps, double increase) {
        Random random = new Random(seed);
        double chunkCentreX = chunkX * 16 + 8;
        double chunkCentreZ = chunkZ * 16 + 8;
        float f3 = 0.0f;
        float f4 = 0.0f;
        if (steps <= 0) {
            int j1 = this.range * 16 - 16;
            steps = j1 - random.nextInt(j1 / 4);
        }
        boolean flag = false;
        if (step == -1) {
            step = steps / 2;
            flag = true;
        }
        float f5 = 1.0f;
        for (int k1 = 0; k1 < 256; ++k1) {
            if (k1 == 0 || random.nextInt(3) == 0) {
                f5 = 1.0f + random.nextFloat() * random.nextFloat() * 1.0f;
            }
            this.ravineNoise[k1] = f5 * f5;
        }
        while (step < steps) {
            double d6 = 1.5 + Mth.sin(step * (float) Math.PI / steps) * f * 1.0f;
            double d7 = d6 * increase;
            d6 *= random.nextFloat() * 0.25 + 0.75;
            d7 *= random.nextFloat() * 0.25 + 0.75;
            float f6 = Mth.cos(f2);
            float f7 = Mth.sin(f2);
            d += Mth.cos(ravineAngle) * f6;
            d1 += f7;
            d2 += Mth.sin(ravineAngle) * f6;
            f2 *= 0.7f;
            f2 += f4 * 0.05f;
            ravineAngle += f3 * 0.05f;
            f4 *= 0.8f;
            f3 *= 0.5f;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f;
            if (flag || random.nextInt(4) != 0) {
                double d8 = d - chunkCentreX;
                double d9 = d2 - chunkCentreZ;
                double d10 = steps - step;
                double d11 = f + 2.0f + 16.0f;
                if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
                    return;
                }
                if (d >= chunkCentreX - 16.0 - d6 * 2.0 && d2 >= chunkCentreZ - 16.0 - d6 * 2.0
                        && d <= chunkCentreX + 16.0 + d6 * 2.0 && d2 <= chunkCentreZ + 16.0 + d6 * 2.0) {
                    int xMin = Math.max(Mth.floor(d - d6) - chunkX * 16 - 1, 0);
                    int xMax = Math.min(Mth.floor(d + d6) - chunkX * 16 + 1, 16);
                    int yMin = Math.max(Mth.floor(d1 - d7) - 1, 1);
                    int yMax = Math.min(Mth.floor(d1 + d7) + 1, 120);
                    int zMin = Math.max(Mth.floor(d2 - d6) - chunkZ * 16 - 1, 0);
                    int zMax = Math.min(Mth.floor(d2 + d6) - chunkZ * 16 + 1, 16);
                    boolean isWater = false;
                    search:
                    for (int i1 = xMin; i1 < xMax; ++i1) {
                        for (int k1 = zMin; k1 < zMax; ++k1) {
                            for (int j1 = yMax + 1; j1 >= yMin - 1; --j1) {
                                if (j1 >= 256) {
                                    continue;
                                }
                                BlockState state = blocks[(i1 * 16 + k1) * 256 + j1];
                                if (state.getFluidState().is(Fluids.WATER) || state.getFluidState().is(Fluids.FLOWING_WATER)) {
                                    isWater = true;
                                }
                                if (j1 != yMin - 1 && i1 != xMin && i1 != xMax - 1 && k1 != zMin && k1 != zMax - 1) {
                                    j1 = yMin;
                                }
                                if (isWater) {
                                    break search;
                                }
                            }
                        }
                    }
                    if (!isWater) {
                        for (int i1 = xMin; i1 < xMax; ++i1) {
                            double d12 = (i1 + chunkX * 16 + 0.5 - d) / d6;
                            for (int k1 = zMin; k1 < zMax; ++k1) {
                                double d13 = (k1 + chunkZ * 16 + 0.5 - d2) / d6;
                                int blockIndex = (i1 * 16 + k1) * 256 + yMax;
                                boolean topBlock = false;
                                if (d12 * d12 + d13 * d13 >= 1.0) {
                                    continue;
                                }
                                for (int j1 = yMax - 1; j1 >= yMin; --j1) {
                                    double d14 = (j1 + 0.5 - d1) / d7;
                                    if ((d12 * d12 + d13 * d13) * this.ravineNoise[j1] + d14 * d14 / 6.0 < 1.0) {
                                        if (blocks[blockIndex] == biomeAt(i1, k1).topBlock) {
                                            topBlock = true;
                                        }
                                        digBlock(blocks, blockIndex, i1, j1, k1, topBlock);
                                    }
                                    --blockIndex;
                                }
                            }
                        }
                        if (flag) {
                            break;
                        }
                    }
                }
            }
            ++step;
        }
    }
}
