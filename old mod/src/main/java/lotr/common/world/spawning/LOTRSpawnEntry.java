package lotr.common.world.spawning;

import net.minecraft.world.biome.BiomeGenBase;

public class LOTRSpawnEntry extends BiomeGenBase.SpawnListEntry {
	public LOTRSpawnEntry(Class c, int weight, int min, int max) {
		super(c, weight, min, max);
	}

	public static class Instance {
		public LOTRSpawnEntry spawnEntry;
		public int spawnChance;
		public boolean isConquestSpawn;

		public Instance(LOTRSpawnEntry entry, int chance, boolean conquest) {
			spawnEntry = entry;
			spawnChance = chance;
			isConquestSpawn = conquest;
		}
	}

}
