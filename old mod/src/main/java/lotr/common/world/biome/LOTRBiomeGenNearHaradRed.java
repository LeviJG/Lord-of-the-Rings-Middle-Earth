package lotr.common.world.biome;

import net.minecraft.init.Blocks;

public class LOTRBiomeGenNearHaradRed extends LOTRBiomeGenNearHarad {
	public LOTRBiomeGenNearHaradRed(int i, boolean major) {
		super(i, major);
		setDisableRain();
		topBlock = Blocks.sand;
		topBlockMeta = 1;
		fillerBlock = Blocks.sand;
		fillerBlockMeta = 1;
		decorator.clearRandomStructures();
		decorator.clearVillages();
	}
}
