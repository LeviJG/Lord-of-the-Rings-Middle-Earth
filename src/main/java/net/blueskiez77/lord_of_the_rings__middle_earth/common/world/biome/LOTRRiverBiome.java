package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;

/**
 * LOTRBiomeGenRiver.
 */
public class LOTRRiverBiome extends LOTRBiome {

    public LOTRRiverBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        npcSpawnList.clear();
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
        invasionSpawns.clearInvasions();
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return null;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.5f;
    }

    @Override
    public boolean isRiver() {
        return true;
    }
}
