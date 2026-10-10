package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;

/** LOTRAnimalSpawnConditions: an animal's own say over where the world's generation may put it. */
public interface LOTRAnimalSpawnConditions {
    boolean canWorldGenSpawnAt(int i, int j, int k, LOTRBiome biome, LOTRBiomeVariant variant);
}
