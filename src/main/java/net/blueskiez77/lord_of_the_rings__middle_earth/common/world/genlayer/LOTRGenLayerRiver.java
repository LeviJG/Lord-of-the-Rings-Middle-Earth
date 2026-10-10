package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

/** LOTRGenLayerRiver: 1 where the river codes around a cell differ (RANDOM_RIVER), else 0; a map river is 2 (MAP_RIVER). */
public class LOTRGenLayerRiver extends LOTRGenLayer {

    public static final int RANDOM_RIVER = 1;
    public static final int MAP_RIVER = 2;

    public LOTRGenLayerRiver(long seed, LOTRGenLayer layer) {
        super(seed);
        this.lotrParent = layer;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int i2 = xSize + 2;
        int[] riverInit = this.lotrParent.getInts(i - 1, k - 1, i2, zSize + 2);
        int[] ints = new int[xSize * zSize];
        for (int k3 = 0; k3 < zSize; ++k3) {
            for (int i3 = 0; i3 < xSize; ++i3) {
                int centre = riverInit[i3 + 1 + (k3 + 1) * i2];
                int xn = riverInit[i3 + (k3 + 1) * i2];
                int xp = riverInit[i3 + 2 + (k3 + 1) * i2];
                int zn = riverInit[i3 + 1 + k3 * i2];
                int zp = riverInit[i3 + 1 + (k3 + 2) * i2];
                ints[i3 + k3 * xSize] = centre == xn && centre == zn && centre == xp && centre == zp ? 0 : RANDOM_RIVER;
            }
        }
        return ints;
    }
}
