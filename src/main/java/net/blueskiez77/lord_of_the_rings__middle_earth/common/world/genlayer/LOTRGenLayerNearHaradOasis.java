package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRNearHaradBiome;

/** LOTRGenLayerNearHaradOasis: one in 200 cells of desert wholly surrounded by desert becomes an oasis. */
public class LOTRGenLayerNearHaradOasis extends LOTRGenLayer {

    private final LOTRDimension dimension;

    public LOTRGenLayerNearHaradOasis(long seed, LOTRGenLayer layer, LOTRDimension dim) {
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
                LOTRBiome biome = this.dimension.biomeList[biomeID];
                int newBiomeID = biomeID;
                if (biome instanceof LOTRNearHaradBiome && nextInt(200) == 0) {
                    boolean surrounded = true;
                    for (int i2 = -1; i2 <= 1; ++i2) {
                        for (int k2 = -1; k2 <= 1; ++k2) {
                            if (!(this.dimension.biomeList[biomes[i1 + 1 + i2 + (k1 + 1 + k2) * width]] instanceof LOTRNearHaradBiome)) {
                                surrounded = false;
                            }
                        }
                    }
                    if (surrounded) {
                        newBiomeID = LOTRBiomes.NEAR_HARAD_OASIS.biomeID;
                    }
                }
                ints[i1 + k1 * xSize] = newBiomeID;
            }
        }
        return ints;
    }
}
