package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;

/**
 * LOTRBiomeGenFarHaradCloudForest.
 */
public class LOTRFarHaradCloudForestBiome extends LOTRFarHaradBiome {

    public LOTRFarHaradCloudForestBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.FLAMINGO, 10, 4, 4));
        spawnableLOTRAmbientList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BIRD, 10, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 10, 4, 4));
        spawnableMonsterList.add(new LOTRSpawnEntry(LOTREntities.JUNGLE_SCORPION, 30, 4, 4));
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 10;
        decorator.vinesPerChunk = 50;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 4;
        decorator.grassPerChunk = 15;
        decorator.doubleGrassPerChunk = 10;
        decorator.enableFern = true;
        decorator.melonPerChunk = 0.1f;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.JUNGLE_CLOUD, 4000);
        decorator.addTree(LOTRTreeType.JUNGLE, 500);
        decorator.addTree(LOTRTreeType.JUNGLE_SHRUB, 1000);
        decorator.addTree(LOTRTreeType.MANGO, 20);
        registerJungleFlowers();
        biomeColors.setGrass(2007124);
        biomeColors.setFoliage(428338);
        biomeColors.setSky(11452859);
        biomeColors.setFoggy(true);
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FAR_HARAD_JUNGLE.getSubregion("cloudForest");
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return super.getChanceToSpawnAnimals() * 0.5f;
    }
}
