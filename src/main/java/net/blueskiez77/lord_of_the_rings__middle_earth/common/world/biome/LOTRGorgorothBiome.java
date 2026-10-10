package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;



/**
 * LOTRBiomeGenGorgoroth.
 */
public class LOTRGorgorothBiome extends LOTRMordorBiome {

    public LOTRGorgorothBiome(int i, boolean major) {
        super(i, major);
        enableMordorBoulders = false;
        decorator.grassPerChunk = 0;
        biomeColors.setSky(5843484);
    }
}
