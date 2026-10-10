package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

/** LOTRGenLayerNarrowRivers: keeps only river cells with no dry land within {@code maxRange}, thinning the zoomed map rivers. */
public class LOTRGenLayerNarrowRivers extends LOTRGenLayer {

    private final int maxRange;

    public LOTRGenLayerNarrowRivers(long seed, LOTRGenLayer layer, int range) {
        super(seed);
        this.lotrParent = layer;
        this.maxRange = range;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int width = xSize + this.maxRange * 2;
        int[] rivers = this.lotrParent.getInts(i - this.maxRange, k - this.maxRange, width, zSize + this.maxRange * 2);
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                initChunkSeed(i + i1, k + k1);
                int isRiver = rivers[i1 + this.maxRange + (k1 + this.maxRange) * width];
                if (isRiver > 0) {
                    search:
                    for (int range = 1; range <= this.maxRange; ++range) {
                        for (int k2 = k1 - range; k2 <= k1 + range; ++k2) {
                            for (int i2 = i1 - range; i2 <= i1 + range; ++i2) {
                                if (Math.abs(i2 - i1) != range && Math.abs(k2 - k1) != range
                                        || rivers[i2 + this.maxRange + (k2 + this.maxRange) * width] != 0) {
                                    continue;
                                }
                                isRiver = 0;
                                break search;
                            }
                        }
                    }
                }
                ints[i1 + k1 * xSize] = isRiver;
            }
        }
        return ints;
    }
}
