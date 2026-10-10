package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRMordorCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRMordorWargPitStructure;

/**
 * LOTRBiomeGenUdun.
 */
public class LOTRUdunBiome extends LOTRMordorBiome {

    public LOTRUdunBiome(int i, boolean major) {
        super(i, major);
        biomeColors.setSky(6837327);
        biomeColors.setClouds(4797229);
        biomeColors.setFog(4996410);
        decorator.addRandomStructure(new LOTRMordorCampStructure(false), 20);
        decorator.addRandomStructure(new LOTRMordorWargPitStructure(false), 100);
    }
}
