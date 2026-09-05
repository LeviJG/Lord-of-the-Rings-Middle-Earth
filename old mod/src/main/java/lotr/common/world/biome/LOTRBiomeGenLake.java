package lotr.common.world.biome;

import net.minecraft.block.Block;

public class LOTRBiomeGenLake extends LOTRBiome {
	public LOTRBiomeGenLake(int i, boolean major) {
		super(i, major);
		setMinMaxHeight(-0.5f, 0.2f);
		spawnableCreatureList.clear();
		spawnableLOTRAmbientList.clear();
		npcSpawnList.clear();
		decorator.sandPerChunk = 0;
	}

	@Override
	public LOTRMusicRegion.Sub getBiomeMusic() {
		return LOTRMusicRegion.SEA.getSubregion("lake");
	}

	@Override
	public boolean getEnableRiver() {
		return false;
	}

	public LOTRBiomeGenLake setLakeBlock(Block block) {
		topBlock = block;
		fillerBlock = block;
		return this;
	}
}
