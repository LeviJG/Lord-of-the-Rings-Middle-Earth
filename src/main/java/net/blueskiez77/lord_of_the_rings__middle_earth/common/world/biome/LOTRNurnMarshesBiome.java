package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;

/**
 * LOTRBiomeGenNurnMarshes.
 */
public class LOTRNurnMarshesBiome extends LOTRNurnBiome {

    public LOTRNurnMarshesBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        variantChance = 1.0f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_SWAMP);
        decorator.sandPerChunk = 0;
        decorator.quagmirePerChunk = 1;
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.logsPerChunk = 2;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 10;
        decorator.enableFern = true;
        decorator.canePerChunk = 10;
        decorator.reedPerChunk = 4;
        decorator.clearRandomStructures();
    }
}
