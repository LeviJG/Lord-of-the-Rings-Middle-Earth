package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

/**
 * LOTRBiomeGenForodwaithCoast.
 */
public class LOTRForodwaithCoastBiome extends LOTRForodwaithBiome {

    public LOTRForodwaithCoastBiome(int i, boolean major) {
        super(i, major);
        topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
        fillerBlock = topBlock;
        biomeTerrain.setXZScale(30.0);
        clearBiomeVariants();
        decorator.clearRandomStructures();
    }
}
