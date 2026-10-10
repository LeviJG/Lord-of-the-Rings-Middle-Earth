package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;

/**
 * LOTRBiomeGenFangornClearing.
 */
public class LOTRFangornClearingBiome extends LOTRFangornBiome {

    public LOTRFangornClearingBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ENTS, 10);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        clearBiomeVariants();
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 4;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 8;
    }
}
