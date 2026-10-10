package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

/**
 * LOTRBiomeGenShireWoodlands.
 */
public class LOTRShireWoodlandsBiome extends LOTRShireBiome {

    public LOTRShireWoodlandsBiome(int i, boolean major) {
        super(i, major);
        variantChance = 0.2f;
        clearBiomeVariants();
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        decorator.treesPerChunk = 9;
        decorator.flowersPerChunk = 6;
        decorator.doubleFlowersPerChunk = 2;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 2;
        decorator.enableFern = true;
        decorator.addTree(LOTRTreeType.BIRCH, 250);
        decorator.addTree(LOTRTreeType.SHIRE_PINE, 2500);
        decorator.addTree(LOTRTreeType.ASPEN, 300);
        decorator.addTree(LOTRTreeType.ASPEN_LARGE, 100);
        addFlower(LOTRLegacyBlocks.mod("shireHeather"), 0, 20);
        biomeColors.resetGrass();
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.SHIRE.getSubregion("woodland");
    }

    @Override
    public boolean hasDomesticAnimals() {
        return false;
    }

    @Override
    public int spawnCountMultiplier() {
        return super.spawnCountMultiplier() * 2;
    }
}
