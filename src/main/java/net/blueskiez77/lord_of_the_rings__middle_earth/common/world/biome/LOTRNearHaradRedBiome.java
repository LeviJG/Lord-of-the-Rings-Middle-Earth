package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

/**
 * LOTRBiomeGenNearHaradRed.
 */
public class LOTRNearHaradRedBiome extends LOTRNearHaradBiome {

    public LOTRNearHaradRedBiome(int i, boolean major) {
        super(i, major);
        setDisableRain();
        topBlock = LOTRLegacyBlocks.vanilla("sand").state(1);
        fillerBlock = LOTRLegacyBlocks.vanilla("sand").state(1);
        decorator.clearRandomStructures();
        decorator.clearVillages();
    }
}
