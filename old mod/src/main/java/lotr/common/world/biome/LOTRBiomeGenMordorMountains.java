package lotr.common.world.biome;

import lotr.common.LOTRAchievement;
import lotr.common.world.map.LOTRWaypoint;
import lotr.common.world.structure.LOTRWorldGenMordorTower;

public class LOTRBiomeGenMordorMountains extends LOTRBiomeGenMordor {
	public LOTRBiomeGenMordorMountains(int i, boolean major) {
		super(i, major);
		decorator.clearRandomStructures();
		decorator.addRandomStructure(new LOTRWorldGenMordorTower(false), 400);
	}

	@Override
	public LOTRAchievement getBiomeAchievement() {
		return null;
	}

	@Override
	public LOTRMusicRegion.Sub getBiomeMusic() {
		return LOTRMusicRegion.MORDOR.getSubregion("mountains");
	}

	@Override
	public LOTRWaypoint.Region getBiomeWaypoints() {
		return null;
	}

	@Override
	public boolean getEnableRiver() {
		return false;
	}
}
