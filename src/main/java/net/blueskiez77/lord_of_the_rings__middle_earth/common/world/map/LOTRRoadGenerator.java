package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRRoadGenerator: lays a column's road as its surface is built -- the biome's road blocks four
 * deep where the road runs, worn away where it is out of repair, slabs where the ground slopes
 * down; a bridge where it crosses water, rising half a block for each block of depth, with edges,
 * fences and pillars along its sides; and, beside a road that has them, flowers and hedges.
 */
public final class LOTRRoadGenerator {

    public static final int ROAD_DEPTH = 4;
    private static final BlockState HEDGE = Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);

    private LOTRRoadGenerator() {
    }

    private static boolean isLiquid(BlockState state) {
        return !state.getFluidState().isEmpty();
    }

    public static boolean generateRoad(RandomSource rand, int i, int k, LOTRBiome biome, LOTRChunkTerrain terrain, double[] heightNoise) {
        BlockState[] blocks = terrain.blocks;
        int xzIndex = (i & 0xF) * 16 + (k & 0xF);
        int ySize = LOTRChunkTerrain.HEIGHT;
        LOTRRoadType roadType = biome.getRoadBlock();
        LOTRRoadType.BridgeType bridgeType = biome.getBridgeBlock();
        if (LOTRRoads.isRoadAt(i, k)) {
            int roadTop = 0;
            int bridgeBase = 0;
            boolean bridge = false;
            boolean bridgeSlab = false;
            for (int j = ySize - 1; j > 0; --j) {
                BlockState block = blocks[xzIndex * ySize + j];
                if (LOTRChunkTerrain.isOpaque(block)) {
                    roadTop = j;
                    break;
                }
                if (!isLiquid(block)) {
                    continue;
                }
                bridgeBase = roadTop = j + 1;
                int maxBridgeTop = j + 6;
                float bridgeHeight = 0.0f;
                for (int j1 = j - 1; j1 > 0 && isLiquid(blocks[xzIndex * ySize + j1]); --j1) {
                    bridgeHeight += 0.5f;
                }
                int bridgeHeightInt = (int) Math.floor(bridgeHeight);
                roadTop = Math.min(roadTop + bridgeHeightInt, maxBridgeTop);
                if (roadTop >= maxBridgeTop || bridgeHeight - bridgeHeightInt < 0.5f) {
                    bridgeSlab = true;
                }
                bridge = true;
                break;
            }
            if (bridge) {
                BlockState bridgeBlock = bridgeType.getBlock(rand, false).state();
                BlockState bridgeBlockSlab = bridgeType.getBlock(rand, true).state();
                BlockState bridgeEdge = bridgeType.getEdge(rand).state();
                BlockState bridgeFence = bridgeType.getFence(rand).state();
                int index2 = xzIndex * ySize + roadTop;
                if (isFenceAt(i, k)) {
                    if (isPillarAt(i, k)) {
                        int pillarIndex;
                        for (int j2 = roadTop + 4; j2 > 0 && !LOTRChunkTerrain.isOpaque(blocks[pillarIndex = xzIndex * ySize + j2]); --j2) {
                            if (j2 >= roadTop + 4) {
                                blocks[pillarIndex] = bridgeFence;
                            } else if (j2 >= roadTop + 3) {
                                blocks[pillarIndex] = bridgeBlock;
                            } else {
                                blocks[pillarIndex] = bridgeEdge;
                            }
                        }
                    } else {
                        blocks[index2] = bridgeEdge;
                        blocks[index2 + 1] = bridgeFence;
                        if (roadTop > bridgeBase) {
                            blocks[index2 - 1] = bridgeEdge;
                        }
                        int support = bridgeBase + 2;
                        if (roadTop - 1 > support) {
                            blocks[xzIndex * ySize + support] = bridgeFence;
                        }
                    }
                } else {
                    blocks[index2] = bridgeSlab ? bridgeBlockSlab : bridgeBlock;
                    if (roadTop > bridgeBase) {
                        blocks[index2 - 1] = bridgeBlock;
                    }
                }
            } else {
                for (int j = roadTop; j > roadTop - ROAD_DEPTH && j > 0; --j) {
                    int index = xzIndex * ySize + j;
                    if (rand.nextFloat() >= roadType.getRepair()) {
                        continue;
                    }
                    boolean isTop = j == roadTop;
                    boolean isSlab = false;
                    if (isTop && j >= 63) {
                        isSlab = (heightNoise[index] + heightNoise[index + 1]) / 2.0 < 0.0;
                    }
                    blocks[index] = roadType.getBlock(rand, isTop, isSlab).state();
                }
            }
            return true;
        }
        if (roadType.hasFlowers()) {
            int roadTop = 0;
            for (int j = ySize - 1; j > 0; --j) {
                if (LOTRChunkTerrain.isOpaque(blocks[xzIndex * ySize + j])) {
                    roadTop = j;
                    break;
                }
            }
            int index = xzIndex * ySize + roadTop + 1;
            if (anyRoadWithin(i, k, 2, 0)) {
                LOTRBiome.FlowerEntry flower = biome.getRandomFlower(terrain.variants == null ? LOTRBiomeVariant.STANDARD : terrain.variants[(i & 0xF) + (k & 0xF) * 16], rand);
                if (flower != null) {
                    blocks[index] = flower.state();
                }
            } else if (anyRoadWithin(i, k, 3, 2)) {
                blocks[index] = HEDGE;
            }
            return true;
        }
        return false;
    }

    /** A road within {@code range} of (i, k), outside the square of {@code inner} about it. */
    private static boolean anyRoadWithin(int i, int k, int range, int inner) {
        for (int i1 = -range; i1 <= range; ++i1) {
            for (int k1 = -range; k1 <= range; ++k1) {
                if (Math.abs(i1) <= inner && Math.abs(k1) <= inner) {
                    continue;
                }
                if (LOTRRoads.isRoadAt(i + i1, k + k1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBridgeEdgePillar(int i, int k) {
        return LOTRRoads.isRoadAt(i, k) && isFenceAt(i, k) && isPillarAt(i, k);
    }

    public static boolean isFenceAt(int i, int k) {
        for (int i1 = -1; i1 <= 1; ++i1) {
            for (int k1 = -1; k1 <= 1; ++k1) {
                if ((i1 != 0 || k1 != 0) && !LOTRRoads.isRoadAt(i + i1, k + k1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPillarAt(int i, int k) {
        int pRange = 8;
        if (Mth.positiveModulo(Mth.positiveModulo(i, pRange) + Mth.positiveModulo(k, pRange), pRange) == 0) {
            return !isBridgeEdgePillar(i + 1, k - 1) && !isBridgeEdgePillar(i + 1, k + 1);
        }
        return false;
    }
}
