package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;

/**
 * LOTRBiomeGenFarHaradSwamp.
 */
public class LOTRFarHaradSwampBiome extends LOTRFarHaradBiome {

    public LOTRFarHaradSwampBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        clearBiomeVariants();
        variantChance = 1.0f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_SWAMP);
        decorator.sandPerChunk = 0;
        decorator.quagmirePerChunk = 1;
        decorator.treesPerChunk = 0;
        decorator.vinesPerChunk = 20;
        decorator.logsPerChunk = 3;
        decorator.flowersPerChunk = 0;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 8;
        decorator.enableFern = true;
        decorator.mushroomsPerChunk = 3;
        decorator.waterlilyPerChunk = 3;
        decorator.canePerChunk = 10;
        decorator.reedPerChunk = 3;
        decorator.addTree(LOTRTreeType.OAK_SWAMP, 1000);
        registerSwampFlowers();
        biomeColors.setWater(5607038);
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FAR_HARAD.getSubregion("swamp");
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }
}
