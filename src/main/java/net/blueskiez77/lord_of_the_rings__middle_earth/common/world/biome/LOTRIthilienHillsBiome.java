package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;

/**
 * LOTRBiomeGenIthilienHills.
 */
public class LOTRIthilienHillsBiome extends LOTRIthilienBiome {

    public LOTRIthilienHillsBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        variantChance = 0.2f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        decorator.treesPerChunk = 0;
        decorator.logsPerChunk = 0;
        decorator.flowersPerChunk = 2;
        decorator.grassPerChunk = 8;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }
}
