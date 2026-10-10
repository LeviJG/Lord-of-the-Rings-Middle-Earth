package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;

/**
 * LOTRBiomeGenWoodlandRealmHills.
 */
public class LOTRWoodlandRealmHillsBiome extends LOTRWoodlandRealmBiome {

    public LOTRWoodlandRealmHillsBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        decorator.treesPerChunk = 4;
        decorator.grassPerChunk = 10;
        decorator.addTree(LOTRTreeType.GREEN_OAK_EXTREME, 500);
    }
}
