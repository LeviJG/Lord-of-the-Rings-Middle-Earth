package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;

/**
 * LOTRBiomeGenDorEnErnilHills.
 */
public class LOTRDorEnErnilHillsBiome extends LOTRDorEnErnilBiome {

    public LOTRDorEnErnilHillsBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        variantChance = 0.2f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        decorator.treesPerChunk = 1;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public int spawnCountMultiplier() {
        return super.spawnCountMultiplier() * 3;
    }
}
