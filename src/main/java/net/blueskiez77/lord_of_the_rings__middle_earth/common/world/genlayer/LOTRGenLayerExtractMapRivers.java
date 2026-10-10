package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

/** LOTRGenLayerExtractMapRivers: the map's own rivers, as MAP_RIVER where the map paints the river biome. */
public class LOTRGenLayerExtractMapRivers extends LOTRGenLayer {

    public LOTRGenLayerExtractMapRivers(long seed, LOTRGenLayer layer) {
        super(seed);
        this.lotrParent = layer;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int[] biomes = this.lotrParent.getInts(i, k, xSize, zSize);
        int[] ints = new int[xSize * zSize];
        for (int l = 0; l < ints.length; ++l) {
            ints[l] = biomes[l] == LOTRBiomes.RIVER.biomeID ? LOTRGenLayerRiver.MAP_RIVER : 0;
        }
        return ints;
    }
}
