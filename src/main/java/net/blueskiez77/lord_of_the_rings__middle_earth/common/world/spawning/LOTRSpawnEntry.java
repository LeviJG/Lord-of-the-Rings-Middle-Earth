package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWeightedRandom;

import net.minecraft.world.entity.EntityType;

/**
 * LOTRSpawnEntry (and 1.7.10's BiomeGenBase.SpawnListEntry, which it extended): a kind of creature,
 * its weight among the others in its list, and how many come together.
 */
public record LOTRSpawnEntry(EntityType<?> type, int itemWeight, int minGroupCount, int maxGroupCount)
        implements LOTRWeightedRandom.Item {

    /** One draw from a biome's NPC lists: the entry, the list's spawn chance, and whether it came by conquest. */
    public record Instance(LOTRSpawnEntry spawnEntry, int spawnChance, boolean isConquestSpawn) {
    }
}
