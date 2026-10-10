package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

/**
 * LOTRBiomeGenLindonCoast.
 */
public class LOTRLindonCoastBiome extends LOTRLindonBiome {

    public LOTRLindonCoastBiome(int i, boolean major) {
        super(i, major);
        topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
        fillerBlock = topBlock;
        biomeTerrain.setXZScale(30.0);
        clearBiomeVariants();
        decorator.clearRandomStructures();
    }
}
