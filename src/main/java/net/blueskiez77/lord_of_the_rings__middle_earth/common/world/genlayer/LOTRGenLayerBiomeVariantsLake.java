package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGenLayerBiomeVariantsLake: lake flags, rarer the coarser the zoom -- any biome's lakes
 * (FLAG_LAKE), the jungle's (FLAG_JUNGLE) and the mangrove's (FLAG_MANGROVE).
 */
public class LOTRGenLayerBiomeVariantsLake extends LOTRGenLayer {

    public static final int FLAG_LAKE = 1;
    public static final int FLAG_JUNGLE = 2;
    public static final int FLAG_MANGROVE = 4;
    private final int zoomScale;
    private int lakeFlags;

    public LOTRGenLayerBiomeVariantsLake(long seed, @Nullable LOTRGenLayer layer, int i) {
        super(seed);
        this.lotrParent = layer;
        this.zoomScale = 1 << i;
    }

    public static boolean getFlag(int param, int flag) {
        return (param & flag) == flag;
    }

    public static int setFlag(int param, int flag) {
        return param | flag;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int[] baseInts = this.lotrParent == null ? null : this.lotrParent.getInts(i, k, xSize, zSize);
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                initChunkSeed(i + i1, k + k1);
                int baseInt = baseInts == null ? 0 : baseInts[i1 + k1 * xSize];
                if (getFlag(this.lakeFlags, FLAG_LAKE) && nextInt(30 * this.zoomScale * this.zoomScale * this.zoomScale) == 2) {
                    baseInt = setFlag(baseInt, FLAG_LAKE);
                }
                if (getFlag(this.lakeFlags, FLAG_JUNGLE) && nextInt(12) == 3) {
                    baseInt = setFlag(baseInt, FLAG_JUNGLE);
                }
                if (getFlag(this.lakeFlags, FLAG_MANGROVE) && nextInt(10) == 1) {
                    baseInt = setFlag(baseInt, FLAG_MANGROVE);
                }
                ints[i1 + k1 * xSize] = baseInt;
            }
        }
        return ints;
    }

    public LOTRGenLayerBiomeVariantsLake setLakeFlags(int... flags) {
        for (int f : flags) {
            this.lakeFlags = setFlag(this.lakeFlags, f);
        }
        return this;
    }
}
