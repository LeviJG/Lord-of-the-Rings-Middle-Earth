package lotr.common.world.mapgen;

import lotr.common.world.LOTRUtumnoLevel;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class LOTRMapGenCavesUtumno extends LOTRMapGenCaves {
	@Override
	public int caveRarity() {
		return 3;
	}

	@Override
	public void digBlock(Block[] blockArray, int index, int xzIndex, int i, int j, int k, int chunkX, int chunkZ, LOTRBiome biome, boolean foundTop) {
		if (j < LOTRUtumnoLevel.forY(0).getLowestCorridorFloor() || j > LOTRUtumnoLevel.forY(255).getHighestCorridorRoof()) {
			return;
		}
		for (int l = 0; l < LOTRUtumnoLevel.values().length - 1; ++l) {
			LOTRUtumnoLevel levelUpper = LOTRUtumnoLevel.values()[l];
			LOTRUtumnoLevel levelLower = LOTRUtumnoLevel.values()[l + 1];
			if (j <= levelLower.getHighestCorridorRoof() || j >= levelUpper.getLowestCorridorFloor()) {
				continue;
			}
			return;
		}
		blockArray[index] = Blocks.air;
	}

	@Override
	public int getCaveGenerationHeight() {
		return rand.nextInt(rand.nextInt(240) + 8);
	}
}
