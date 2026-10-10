package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

/** LOTRGenLayerRiverInit: a random code for each cell, whose edges, once zoomed, become the random rivers. */
public class LOTRGenLayerRiverInit extends LOTRGenLayer {

    public LOTRGenLayerRiverInit(long seed) {
        super(seed);
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                initChunkSeed(i + i1, k + k1);
                ints[i1 + k1 * xSize] = 2 + nextInt(299999);
            }
        }
        return ints;
    }
}
