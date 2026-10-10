package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

/**
 * LOTRBiomeGenEasternDesolation.
 */
public class LOTREasternDesolationBiome extends LOTRMordorBiome {

    public LOTREasternDesolationBiome(int i, boolean major) {
        super(i, major);
        topBlock = LOTRLegacyBlocks.mod("mordorDirt").state(0);
        fillerBlock = LOTRLegacyBlocks.mod("mordorDirt").state(0);
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("mordorGravel"), 0, 60, LOTRLegacyBlocks.mod("mordorDirt")), 6.0f, 60, 100);
        decorator.grassPerChunk = 3;
        biomeColors.setSky(9538431);
        biomeColors.resetClouds();
        biomeColors.resetFog();
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.MORDOR.getSubregion("east");
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.DIRT;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.5f;
    }

    @Override
    public int spawnCountMultiplier() {
        return super.spawnCountMultiplier() * 2;
    }
}
