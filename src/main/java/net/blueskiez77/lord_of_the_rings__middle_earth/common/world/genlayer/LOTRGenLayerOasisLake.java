package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRNearHaradOasisBiome;

/** LOTRGenLayerOasisLake: the heart of an oasis, wholly surrounded by oasis, becomes a lake. */
public class LOTRGenLayerOasisLake extends LOTRGenLayer {

    private final LOTRDimension dimension;

    public LOTRGenLayerOasisLake(long seed, LOTRGenLayer layer, LOTRDimension dim) {
        super(seed);
        this.lotrParent = layer;
        this.dimension = dim;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int width = xSize + 2;
        int[] biomes = this.lotrParent.getInts(i - 1, k - 1, width, zSize + 2);
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                initChunkSeed(i + i1, k + k1);
                int biomeID = biomes[i1 + 1 + (k1 + 1) * width];
                int newBiomeID = biomeID;
                if (this.dimension.biomeList[biomeID] instanceof LOTRNearHaradOasisBiome) {
                    boolean surrounded = true;
                    for (int i2 = -1; i2 <= 1; ++i2) {
                        for (int k2 = -1; k2 <= 1; ++k2) {
                            if (!(this.dimension.biomeList[biomes[i1 + 1 + i2 + (k1 + 1 + k2) * width]] instanceof LOTRNearHaradOasisBiome)) {
                                surrounded = false;
                            }
                        }
                    }
                    if (surrounded) {
                        newBiomeID = LOTRBiomes.LAKE.biomeID;
                    }
                }
                ints[i1 + k1 * xSize] = newBiomeID;
            }
        }
        return ints;
    }
}
