package lotr.common.world.biome;

import lotr.common.LOTRAchievement;
import lotr.common.world.map.LOTRWaypoint;

public class LOTRBiomeGenRhunIslandForest extends LOTRBiomeGenRhunRedForest {
	public LOTRBiomeGenRhunIslandForest(int i, boolean major) {
		super(i, major);
		npcSpawnList.clear();
		decorator.treesPerChunk = 10;
		biomeColors.setFog(6132078);
		decorator.clearRandomStructures();
		decorator.clearVillages();
		clearTravellingTraders();
		invasionSpawns.clearInvasions();
	}

	@Override
	public LOTRAchievement getBiomeAchievement() {
		return LOTRAchievement.enterRhunIsland;
	}

	@Override
	public LOTRWaypoint.Region getBiomeWaypoints() {
		return LOTRWaypoint.Region.TOL_RHUNAER;
	}
}
