package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;

/**
 * LOTRBiomeGenIthilienWasteland.
 */
public class LOTRIthilienWastelandBiome extends LOTRIthilienBiome {

    public LOTRIthilienWastelandBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        clearBiomeVariants();
        variantChance = 0.7f;
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        decorator.logsPerChunk = 2;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 2;
        decorator.addTree(LOTRTreeType.OAK_DEAD, 1000);
        decorator.addTree(LOTRTreeType.LEBETHRON_DEAD, 200);
        decorator.addTree(LOTRTreeType.BIRCH_DEAD, 50);
    }
}
