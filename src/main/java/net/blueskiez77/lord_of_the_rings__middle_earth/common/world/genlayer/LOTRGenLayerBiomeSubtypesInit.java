package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

/** LOTRGenLayerBiomeSubtypesInit: a random 0-99 for each cell, for LOTRGenLayerBiomeSubtypes. */
public class LOTRGenLayerBiomeSubtypesInit extends LOTRGenLayer {

    public LOTRGenLayerBiomeSubtypesInit(long seed) {
        super(seed);
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                initChunkSeed(i + i1, k + k1);
                ints[i1 + k1 * xSize] = nextInt(100);
            }
        }
        return ints;
    }
}
