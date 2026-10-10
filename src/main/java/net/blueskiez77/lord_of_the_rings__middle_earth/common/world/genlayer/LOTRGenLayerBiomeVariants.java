package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

/** LOTRGenLayerBiomeVariants: a random 0-9999 for each cell; below a biome's variant chance, it picks a variant. */
public class LOTRGenLayerBiomeVariants extends LOTRGenLayer {

    public static final int RANDOM_MAX = 10000;

    public LOTRGenLayerBiomeVariants(long seed) {
        super(seed);
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                initChunkSeed(i + i1, k + k1);
                ints[i1 + k1 * xSize] = nextInt(RANDOM_MAX);
            }
        }
        return ints;
    }
}
