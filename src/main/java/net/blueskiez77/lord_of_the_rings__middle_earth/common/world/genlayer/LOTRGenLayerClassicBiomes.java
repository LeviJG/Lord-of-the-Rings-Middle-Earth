package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

/** LOTRGenLayerClassicBiomes ("M.E. Classic"): the ocean, or any of the dimension's major biomes at random. */
public class LOTRGenLayerClassicBiomes extends LOTRGenLayer {

    private final LOTRDimension dimension;

    public LOTRGenLayerClassicBiomes(long seed, LOTRGenLayer layer, LOTRDimension dim) {
        super(seed);
        this.lotrParent = layer;
        this.dimension = dim;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int[] oceans = this.lotrParent.getInts(i, k, xSize, zSize);
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                initChunkSeed(i + i1, k + k1);
                int biomeID;
                if (oceans[i1 + k1 * xSize] == 1) {
                    biomeID = LOTRBiomes.OCEAN.biomeID;
                } else {
                    List<LOTRBiome> biomeList = this.dimension.majorBiomes;
                    biomeID = biomeList.get(nextInt(biomeList.size())).biomeID;
                }
                ints[i1 + k1 * xSize] = biomeID;
            }
        }
        return ints;
    }
}
