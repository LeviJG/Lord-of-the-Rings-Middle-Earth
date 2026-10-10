package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

/**
 * LOTRGenLayerZoom: doubles the resolution of the layer below, each new cell taking a neighbour's
 * value at random, or the most common of four.
 *
 * <p>The source this port follows clamped every value it zoomed to 0-255, which suits biome ids
 * but not the variant codes (up to 10000) and river codes (up to 300000) that are also zoomed: it
 * left every place the first of its biome's variants and no random rivers at all. Values pass
 * through as the mod's own layers had them.
 */
public class LOTRGenLayerZoom extends LOTRGenLayer {

    public LOTRGenLayerZoom(long seed, LOTRGenLayer layer) {
        super(seed);
        this.lotrParent = layer;
    }

    public static LOTRGenLayer magnify(long seed, LOTRGenLayer source, int zooms) {
        LOTRGenLayer layer = source;
        for (int i = 0; i < zooms; ++i) {
            layer = new LOTRGenLayerZoom(seed + i, layer);
        }
        return layer;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int i1 = i >> 1;
        int k1 = k >> 1;
        int xSizeZoom = (xSize >> 1) + 2;
        int zSizeZoom = (zSize >> 1) + 2;
        int[] variants = this.lotrParent.getInts(i1, k1, xSizeZoom, zSizeZoom);
        int i2 = (xSizeZoom - 1) << 1;
        int k2 = (zSizeZoom - 1) << 1;
        int[] ints = new int[i2 * k2];
        for (int k3 = 0; k3 < zSizeZoom - 1; ++k3) {
            for (int i3 = 0; i3 < xSizeZoom - 1; ++i3) {
                initChunkSeed((long) i3 + i1 << 1, (long) k3 + k1 << 1);
                int int00 = variants[i3 + k3 * xSizeZoom];
                int int01 = variants[i3 + (k3 + 1) * xSizeZoom];
                int int10 = variants[i3 + 1 + k3 * xSizeZoom];
                int int11 = variants[i3 + 1 + (k3 + 1) * xSizeZoom];
                int index = (i3 << 1) + (k3 << 1) * i2;
                int randomInt00Int10 = selectRandom(int00, int10);
                int randomInt00Int01 = selectRandom(int00, int01);
                int randomValues = selectModeOrRandom(int00, int10, int01, int11);
                ints[index] = int00;
                ints[index + 1] = randomInt00Int10;
                ints[index + i2] = randomInt00Int01;
                ints[index + i2 + 1] = randomValues;
            }
        }
        int[] zoomedInts = new int[xSize * zSize];
        for (int k3 = 0; k3 < zSize; ++k3) {
            System.arraycopy(ints, (k3 + (k & 1)) * i2 + (i & 1), zoomedInts, k3 * xSize, xSize);
        }
        return zoomedInts;
    }
}
