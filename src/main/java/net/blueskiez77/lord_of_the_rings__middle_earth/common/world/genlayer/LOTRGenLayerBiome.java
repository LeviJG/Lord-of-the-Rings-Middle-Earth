package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import java.util.Arrays;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;

/** LOTRGenLayerBiome: one biome everywhere (Utumno's). */
public class LOTRGenLayerBiome extends LOTRGenLayer {

    private final LOTRBiome theBiome;

    public LOTRGenLayerBiome(LOTRBiome biome) {
        super(0L);
        this.theBiome = biome;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int[] ints = new int[xSize * zSize];
        Arrays.fill(ints, this.theBiome.biomeID);
        return ints;
    }
}
