package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen;

import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

/**
 * LOTRMapGenCaves: 1.7.10's caves, carved only through the land's own blocks and water; filled
 * with lava below y 10; breaking through the surface only one cave in five, and never under a
 * road or a village; leaving the biome's top block on the floor where it opens to the sky.
 */
public class LOTRMapGenCaves extends LOTRMapGenBase {

    private static final BlockState LAVA = Blocks.LAVA.defaultBlockState();

    /** isTerrainBlock: the biome's top and filler, soils, sands, stones -- what a cave may cut. */
    public static boolean isTerrainBlock(BlockState state, LOTRBiome biome) {
        if (state.is(biome.topBlock.getBlock()) || state.is(biome.fillerBlock.getBlock())) {
            return true;
        }
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.PODZOL)
                || state.is(Blocks.SAND) || state.is(Blocks.RED_SAND) || state.is(Blocks.GRAVEL)
                || LOTRLegacyBlocks.mod("whiteSand").matches(state) || LOTRLegacyBlocks.mod("mudGrass").matches(state)
                || LOTRLegacyBlocks.mod("mud").matches(state) || LOTRLegacyBlocks.mod("dirtPath").matches(state)) {
            return true;
        }
        if (state.is(Blocks.STONE) || LOTRLegacyBlocks.mod("rock").matches(state) || state.is(Blocks.SANDSTONE)
                || state.is(Blocks.RED_SANDSTONE) || LOTRLegacyBlocks.mod("whiteSandstone").matches(state)) {
            return true;
        }
        return LOTRLegacyBlocks.mod("mordorDirt").matches(state) || LOTRLegacyBlocks.mod("mordorGravel").matches(state);
    }

    private static boolean isWater(BlockState state) {
        return state.getFluidState().is(Fluids.WATER) || state.getFluidState().is(Fluids.FLOWING_WATER);
    }

    protected int caveRarity() {
        return 10;
    }

    protected int getCaveGenerationHeight() {
        return this.rand.nextInt(this.rand.nextInt(120) + 8);
    }

    private void digBlock(BlockState[] blockArray, int index, int xzIndex, int j, LOTRBiome biome, boolean cutSurface) {
        BlockState block = blockArray[index];
        boolean isTop = false;
        boolean belowVillageOrRoad = false;
        int topCheckDepth = 1;
        if (j >= 59 - topCheckDepth) {
            isTop = true;
            for (int j1 = topCheckDepth + 1; j1 <= topCheckDepth + 5 && j + j1 <= 255; ++j1) {
                if (LOTRChunkTerrain.isOpaque(blockArray[index + j1])) {
                    isTop = false;
                    break;
                }
            }
        }
        int roadDepth = 4;
        if ((this.chunkFlags.isVillage || this.chunkFlags.roadFlags[xzIndex]) && j >= 59 - roadDepth) {
            belowVillageOrRoad = true;
            for (int j1 = roadDepth + 1; j1 <= roadDepth + 5 && j + j1 <= 255; ++j1) {
                if (LOTRChunkTerrain.isOpaque(blockArray[index + j1])) {
                    belowVillageOrRoad = false;
                    break;
                }
            }
        }
        boolean dig = isTerrainBlock(block, biome) || !block.getFluidState().isEmpty();
        if (belowVillageOrRoad || isTop && (!cutSurface || this.chunkFlags.isVillage)) {
            dig = false;
        }
        if (!dig) {
            return;
        }
        if (j < 10) {
            blockArray[index] = LAVA;
            return;
        }
        blockArray[index] = LOTRChunkTerrain.AIR;
        if (isTop) {
            for (int j1 = 1; j1 <= 5 && j - j1 > 0; ++j1) {
                if (blockArray[index - j1] == biome.fillerBlock) {
                    blockArray[index - j1] = biome.topBlock;
                    break;
                }
            }
        }
    }

    @Override
    protected void recursiveGenerate(int i, int k, int chunkX, int chunkZ, LOTRChunkTerrain terrain) {
        int caves = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);
        if (this.rand.nextInt(caveRarity()) != 0) {
            caves = 0;
        }
        for (int l = 0; l < caves; ++l) {
            int i1 = i * 16 + this.rand.nextInt(16);
            int j1 = getCaveGenerationHeight();
            int k1 = k * 16 + this.rand.nextInt(16);
            boolean cutSurface = this.rand.nextInt(5) == 0;
            int nodes = 1;
            if (this.rand.nextInt(4) == 0) {
                generateLargeCaveNode(this.rand.nextLong(), chunkX, chunkZ, terrain.blocks, i1, j1, k1, cutSurface);
                nodes += this.rand.nextInt(4);
            }
            for (int n = 0; n < nodes; ++n) {
                float angle = this.rand.nextFloat() * (float) Math.PI * 2.0f;
                float var18 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                float size = this.rand.nextFloat() * 2.0f + this.rand.nextFloat();
                if (this.rand.nextInt(10) == 0) {
                    size *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0f + 1.0f;
                }
                generateCaveNode(this.rand.nextLong(), chunkX, chunkZ, terrain.blocks, i1, j1, k1, size, angle, var18, 0, 0, 1.0, cutSurface);
            }
        }
    }

    private void generateLargeCaveNode(long seed, int chunkX, int chunkZ, BlockState[] blockArray, double x, double y, double z, boolean cutSurface) {
        generateCaveNode(seed, chunkX, chunkZ, blockArray, x, y, z, 1.0f + this.rand.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5, cutSurface);
    }

    private void generateCaveNode(long seed, int chunkX, int chunkZ, BlockState[] blockArray, double x, double y, double z,
                                  float size, float angle, float pitch, int step, int steps, double heightScale, boolean cutSurface) {
        double centreX = chunkX * 16 + 8;
        double centreZ = chunkZ * 16 + 8;
        float angleChange = 0.0f;
        float pitchChange = 0.0f;
        Random caveRand = new Random(seed);
        if (steps <= 0) {
            int maxSteps = this.range * 16 - 16;
            steps = maxSteps - caveRand.nextInt(maxSteps / 4);
        }
        boolean isRoom = false;
        if (step == -1) {
            step = steps / 2;
            isRoom = true;
        }
        int branchStep = caveRand.nextInt(steps / 2) + steps / 4;
        boolean steep = caveRand.nextInt(6) == 0;
        while (step < steps) {
            double width = 1.5 + Mth.sin(step * (float) Math.PI / steps) * size * 1.0f;
            double height = width * heightScale;
            float cosPitch = Mth.cos(pitch);
            float sinPitch = Mth.sin(pitch);
            x += Mth.cos(angle) * cosPitch;
            y += sinPitch;
            z += Mth.sin(angle) * cosPitch;
            pitch = steep ? pitch * 0.92f : pitch * 0.7f;
            pitch += pitchChange * 0.1f;
            angle += angleChange * 0.1f;
            pitchChange *= 0.9f;
            angleChange *= 0.75f;
            pitchChange += (caveRand.nextFloat() - caveRand.nextFloat()) * caveRand.nextFloat() * 2.0f;
            angleChange += (caveRand.nextFloat() - caveRand.nextFloat()) * caveRand.nextFloat() * 4.0f;
            if (!isRoom && step == branchStep && size > 1.0f && steps > 0) {
                generateCaveNode(caveRand.nextLong(), chunkX, chunkZ, blockArray, x, y, z, caveRand.nextFloat() * 0.5f + 0.5f,
                        angle - (float) Math.PI / 2.0f, pitch / 3.0f, step, steps, 1.0, cutSurface);
                generateCaveNode(caveRand.nextLong(), chunkX, chunkZ, blockArray, x, y, z, caveRand.nextFloat() * 0.5f + 0.5f,
                        angle + (float) Math.PI / 2.0f, pitch / 3.0f, step, steps, 1.0, cutSurface);
                return;
            }
            if (isRoom || caveRand.nextInt(4) != 0) {
                double dx = x - centreX;
                double dz = z - centreZ;
                double stepsLeft = steps - step;
                double reach = size + 2.0f + 16.0f;
                if (dx * dx + dz * dz - stepsLeft * stepsLeft > reach * reach) {
                    return;
                }
                if (x >= centreX - 16.0 - width * 2.0 && z >= centreZ - 16.0 - width * 2.0
                        && x <= centreX + 16.0 + width * 2.0 && z <= centreZ + 16.0 + width * 2.0) {
                    int xMin = Math.max(Mth.floor(x - width) - chunkX * 16 - 1, 0);
                    int xMax = Math.min(Mth.floor(x + width) - chunkX * 16 + 1, 16);
                    int yMin = Math.max(Mth.floor(y - height) - 1, 1);
                    int yMax = Math.min(Mth.floor(y + height) + 1, 248);
                    int zMin = Math.max(Mth.floor(z - width) - chunkZ * 16 - 1, 0);
                    int zMax = Math.min(Mth.floor(z + width) - chunkZ * 16 + 1, 16);
                    boolean anyWater = false;
                    for (int i1 = xMin; !anyWater && i1 < xMax; ++i1) {
                        for (int k1 = zMin; !anyWater && k1 < zMax; ++k1) {
                            for (int j1 = yMax + 1; !anyWater && j1 >= yMin - 1; --j1) {
                                if (j1 >= 256) {
                                    continue;
                                }
                                if (isWater(blockArray[(i1 * 16 + k1) * 256 + j1])) {
                                    anyWater = true;
                                }
                                if (j1 != yMin - 1 && i1 != xMin && i1 != xMax - 1 && k1 != zMin && k1 != zMax - 1) {
                                    j1 = yMin;
                                }
                            }
                        }
                    }
                    if (!anyWater) {
                        for (int i1 = xMin; i1 < xMax; ++i1) {
                            double fx = (i1 + chunkX * 16 + 0.5 - x) / width;
                            for (int k1 = zMin; k1 < zMax; ++k1) {
                                double fz = (k1 + chunkZ * 16 + 0.5 - z) / width;
                                int xzIndex = i1 * 16 + k1;
                                int blockIndex = xzIndex * 256 + yMin + 1;
                                if (fx * fx + fz * fz >= 1.0) {
                                    continue;
                                }
                                for (int j1 = yMin; j1 <= yMax - 1; ++j1) {
                                    double fy = (j1 + 0.5 - y) / height;
                                    if (fy > -0.7 && fx * fx + fy * fy + fz * fz < 1.0) {
                                        digBlock(blockArray, blockIndex, xzIndex, j1, biomeAt(i1, k1), cutSurface);
                                    }
                                    ++blockIndex;
                                }
                            }
                        }
                        if (isRoom) {
                            break;
                        }
                    }
                }
            }
            ++step;
        }
    }
}
