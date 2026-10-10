package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBeachBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFarHaradCoastBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFarHaradVolcanoBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRForodwaithBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRLindonBiome;

/**
 * LOTRGenLayerBeach: land beside the target (the ocean) becomes a beach -- white or (one in twenty)
 * gravel by the biome, Lindon's coast or white sand by Lindon, Forodwaith's own coast; Far Harad's
 * coast and volcano keep their own shore.
 */
public class LOTRGenLayerBeach extends LOTRGenLayer {

    private final LOTRDimension dimension;
    private final LOTRBiome targetBiome;

    public LOTRGenLayerBeach(long seed, LOTRGenLayer layer, LOTRDimension dim, LOTRBiome target) {
        super(seed);
        this.lotrParent = layer;
        this.dimension = dim;
        this.targetBiome = target;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int width = xSize + 2;
        int[] biomes = this.lotrParent.getInts(i - 1, k - 1, width, zSize + 2);
        int[] ints = new int[xSize * zSize];
        int target = this.targetBiome.biomeID;
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                initChunkSeed(i + i1, k + k1);
                int biomeID = biomes[i1 + 1 + (k1 + 1) * width];
                LOTRBiome biome = this.dimension.biomeList[biomeID];
                int newBiomeID = biomeID;
                if (biomeID != target && !biome.isWateryBiome()) {
                    int biome1 = biomes[i1 + 1 + k1 * width];
                    int biome2 = biomes[i1 + 2 + (k1 + 1) * width];
                    int biome3 = biomes[i1 + (k1 + 1) * width];
                    int biome4 = biomes[i1 + 1 + (k1 + 2) * width];
                    if (biome1 == target || biome2 == target || biome3 == target || biome4 == target) {
                        if (biome instanceof LOTRLindonBiome) {
                            newBiomeID = nextInt(3) == 0 ? LOTRBiomes.LINDON_COAST.biomeID : LOTRBiomes.BEACH_WHITE.biomeID;
                        } else if (biome instanceof LOTRForodwaithBiome) {
                            newBiomeID = LOTRBiomes.FORODWAITH_COAST.biomeID;
                        } else if (!(biome instanceof LOTRFarHaradCoastBiome) && !(biome instanceof LOTRFarHaradVolcanoBiome)
                                && !(biome instanceof LOTRBeachBiome)) {
                            newBiomeID = biome.decorator.whiteSand ? LOTRBiomes.BEACH_WHITE.biomeID
                                    : nextInt(20) == 0 ? LOTRBiomes.BEACH_GRAVEL.biomeID : LOTRBiomes.BEACH.biomeID;
                        }
                    }
                }
                ints[i1 + k1 * xSize] = newBiomeID;
            }
        }
        return ints;
    }
}
