package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;

import net.minecraft.world.entity.EntityTypes;

/**
 * LOTRBiomeGenLoneLandsHills.
 */
public class LOTRLoneLandsHillsBiome extends LOTRLoneLandsBiome {

    public LOTRLoneLandsHillsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 10, 4, 8));
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        decorator.treesPerChunk = 1;
    }
}
