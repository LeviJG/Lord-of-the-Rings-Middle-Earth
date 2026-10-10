package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

/** LOTRGenLayerSmooth: a cell between two equal neighbours (across x, or across z) takes their value. */
public class LOTRGenLayerSmooth extends LOTRGenLayer {

    public LOTRGenLayerSmooth(long seed, LOTRGenLayer layer) {
        super(seed);
        this.lotrParent = layer;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int xSizeP = xSize + 2;
        int zSizeP = zSize + 2;
        int[] biomes = this.lotrParent.getInts(i - 1, k - 1, xSizeP, zSizeP);
        int[] ints = new int[xSize * zSize];
        for (int k2 = 0; k2 < zSize; ++k2) {
            for (int i2 = 0; i2 < xSize; ++i2) {
                int centre = biomes[i2 + 1 + (k2 + 1) * xSizeP];
                int xn = biomes[i2 + (k2 + 1) * xSizeP];
                int xp = biomes[i2 + 2 + (k2 + 1) * xSizeP];
                int zn = biomes[i2 + 1 + k2 * xSizeP];
                int zp = biomes[i2 + 1 + (k2 + 2) * xSizeP];
                if (xn == xp && zn == zp) {
                    initChunkSeed(i2 + i, k2 + k);
                    centre = nextInt(2) == 0 ? xn : zn;
                } else {
                    if (xn == xp) {
                        centre = xn;
                    }
                    if (zn == zp) {
                        centre = zn;
                    }
                }
                ints[i2 + k2 * xSize] = centre;
            }
        }
        return ints;
    }
}
