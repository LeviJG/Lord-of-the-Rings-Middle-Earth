package lotr.common.world.biome;

import lotr.common.world.spawning.LOTREventSpawner;

public class LOTRBiomeGenRiver extends LOTRBiome {
	public LOTRBiomeGenRiver(int i, boolean major) {
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
