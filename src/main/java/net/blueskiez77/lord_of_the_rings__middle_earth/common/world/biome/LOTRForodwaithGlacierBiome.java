package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

/**
 * LOTRBiomeGenForodwaithGlacier.
 */
public class LOTRForodwaithGlacierBiome extends LOTRForodwaithMountainsBiome {

    public LOTRForodwaithGlacierBiome(int i, boolean major) {
        super(i, major);
        topBlock = LOTRLegacyBlocks.vanilla("ice").state(0);
        fillerBlock = LOTRLegacyBlocks.vanilla("ice").state(0);
    }
}
