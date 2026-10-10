package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;



/**
 * LOTRBiomeGenFarHaradJungleLake.
 */
public class LOTRFarHaradJungleLakeBiome extends LOTRFarHaradJungleBiome {

    public LOTRFarHaradJungleLakeBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        decorator.sandPerChunk = 0;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }
}
