package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

import com.mojang.logging.LogUtils;

/**
 * LOTRGenLayerRemoveMapRivers: paints over the map's river pixels with the most common biome
 * around them (a watery one beside, first), the rivers themselves coming back through the river
 * layers instead.
 */
public class LOTRGenLayerRemoveMapRivers extends LOTRGenLayer {

    public static final int MAX_PIXEL_RANGE = 4;
    private final LOTRDimension dimension;

    public LOTRGenLayerRemoveMapRivers(long seed, LOTRGenLayer biomes, LOTRDimension dim) {
        super(seed);
        this.lotrParent = biomes;
        this.dimension = dim;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int maxRange = MAX_PIXEL_RANGE;
        int width = xSize + maxRange * 2;
        int[] biomes = this.lotrParent.getInts(i - maxRange, k - maxRange, width, zSize + maxRange * 2);
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                initChunkSeed(i + i1, k + k1);
                int biomeID = biomes[i1 + maxRange + (k1 + maxRange) * width];
                if (biomeID != LOTRBiomes.RIVER.biomeID) {
                    ints[i1 + k1 * xSize] = biomeID;
                    continue;
                }
                int replaceID = -1;
                for (int range = 1; range <= maxRange; ++range) {
                    Map<Integer, Integer> viableBiomes = new HashMap<>();
                    Map<Integer, Integer> viableBiomesWateryAdjacent = new HashMap<>();
                    for (int k2 = k1 - range; k2 <= k1 + range; ++k2) {
                        for (int i2 = i1 - range; i2 <= i1 + range; ++i2) {
                            if (Math.abs(i2 - i1) != range && Math.abs(k2 - k1) != range) {
                                continue;
                            }
                            int subBiomeID = biomes[i2 + maxRange + (k2 + maxRange) * width];
                            LOTRBiome subBiome = this.dimension.biomeList[subBiomeID];
                            if (subBiome == LOTRBiomes.RIVER) {
                                continue;
                            }
                            boolean wateryAdjacent = subBiome.isWateryBiome() && range == 1;
                            Map<Integer, Integer> srcMap = wateryAdjacent ? viableBiomesWateryAdjacent : viableBiomes;
                            srcMap.merge(subBiomeID, 1, Integer::sum);
                        }
                    }
                    Map<Integer, Integer> priorityMap = viableBiomesWateryAdjacent.isEmpty() ? viableBiomes : viableBiomesWateryAdjacent;
                    if (priorityMap.isEmpty()) {
                        continue;
                    }
                    int maxCount = 0;
                    for (int count : priorityMap.values()) {
                        maxCount = Math.max(maxCount, count);
                    }
                    List<Integer> maxCountBiomes = new ArrayList<>();
                    for (Map.Entry<Integer, Integer> e : priorityMap.entrySet()) {
                        if (e.getValue() == maxCount) {
                            maxCountBiomes.add(e.getKey());
                        }
                    }
                    replaceID = maxCountBiomes.get(nextInt(maxCountBiomes.size()));
                    break;
                }
                if (replaceID == -1) {
                    LogUtils.getLogger().warn("LOTR map generation failed to replace map river at {}, {}", i, k);
                    replaceID = 0;
                }
                ints[i1 + k1 * xSize] = replaceID;
            }
        }
        return ints;
    }
}
