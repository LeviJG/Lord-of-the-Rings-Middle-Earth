package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

/** LOTRGenLayerIncludeMapRivers: the random rivers, with the map's rivers laid over them. */
public class LOTRGenLayerIncludeMapRivers extends LOTRGenLayer {

    private final LOTRGenLayer riverLayer;
    private final LOTRGenLayer mapRiverLayer;

    public LOTRGenLayerIncludeMapRivers(long seed, LOTRGenLayer rivers, LOTRGenLayer mapRivers) {
        super(seed);
        this.riverLayer = rivers;
        this.mapRiverLayer = mapRivers;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int[] rivers = this.riverLayer.getInts(i, k, xSize, zSize);
        int[] mapRivers = this.mapRiverLayer.getInts(i, k, xSize, zSize);
        int[] ints = new int[xSize * zSize];
        for (int index = 0; index < ints.length; ++index) {
            ints[index] = mapRivers[index] == LOTRGenLayerRiver.MAP_RIVER ? LOTRGenLayerRiver.MAP_RIVER : rivers[index];
        }
        return ints;
    }

    @Override
    public void initWorldGenSeed(long seed) {
        super.initWorldGenSeed(seed);
        this.riverLayer.initWorldGenSeed(seed);
        this.mapRiverLayer.initWorldGenSeed(seed);
    }
}
