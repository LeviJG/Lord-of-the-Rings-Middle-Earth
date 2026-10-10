package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRNearHaradBiome;

/** LOTRGenLayerNearHaradRiverbanks: desert within two cells of a map river becomes fertile riverbank. */
public class LOTRGenLayerNearHaradRiverbanks extends LOTRGenLayer {

    private final LOTRGenLayer biomeLayer;
    private final LOTRGenLayer mapRiverLayer;
    private final LOTRDimension dimension;

    public LOTRGenLayerNearHaradRiverbanks(long seed, LOTRGenLayer biomes, LOTRGenLayer rivers, LOTRDimension dim) {
        super(seed);
        this.biomeLayer = biomes;
        this.mapRiverLayer = rivers;
        this.dimension = dim;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int width = xSize + 3;
        int[] biomes = this.biomeLayer.getInts(i - 2, k - 2, width, zSize + 3);
        int[] mapRivers = this.mapRiverLayer.getInts(i - 2, k - 2, width, zSize + 3);
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                int biomeID = biomes[i1 + 2 + (k1 + 2) * width];
                int newBiomeID = biomeID;
                if (this.dimension.biomeList[biomeID] instanceof LOTRNearHaradBiome) {
                    boolean adjRiver = false;
                    for (int i2 = -2; i2 <= 1; ++i2) {
                        for (int k2 = -2; k2 <= 1; ++k2) {
                            if (mapRivers[i1 + 2 + i2 + (k1 + 2 + k2) * width] == LOTRGenLayerRiver.MAP_RIVER) {
                                adjRiver = true;
                            }
                        }
                    }
                    if (adjRiver) {
                        newBiomeID = LOTRBiomes.NEAR_HARAD_RIVERBANK.biomeID;
                    }
                }
                ints[i1 + k1 * xSize] = newBiomeID;
            }
        }
        return ints;
    }

    @Override
    public void initWorldGenSeed(long seed) {
        super.initWorldGenSeed(seed);
        this.biomeLayer.initWorldGenSeed(seed);
        this.mapRiverLayer.initWorldGenSeed(seed);
    }
}
