package lotr.common.world.biome;

import lotr.common.LOTRMod;
import lotr.common.world.map.LOTRRoadType;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class LOTRBiomeGenEasternDesolation extends LOTRBiomeGenMordor {
	public LOTRBiomeGenEasternDesolation(int i, boolean major) {
		super(i, major);
		topBlock = LOTRMod.mordorDirt;
		fillerBlock = LOTRMod.mordorDirt;
		decorator.addSoil(new WorldGenMinable(LOTRMod.mordorGravel, 0, 60, LOTRMod.mordorDirt), 6.0f, 60, 100);
		decorator.grassPerChunk = 3;
		biomeColors.setSky(9538431);
		biomeColors.resetClouds();
		biomeColors.resetFog();
	}

	@Override
	public LOTRMusicRegion.Sub getBiomeMusic() {
		return LOTRMusicRegion.MORDOR.getSubregion("east");
	}

	@Override
	public LOTRRoadType getRoadBlock() {
		return LOTRRoadType.DIRT;
	}

	@Override
	public float getTreeIncreaseChance() {
		return 0.5f;
	}

	@Override
	public int spawnCountMultiplier() {
		return super.spawnCountMultiplier() * 2;
	}
}
