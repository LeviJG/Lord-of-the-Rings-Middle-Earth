package lotr.common.world.biome;

import net.minecraft.init.Blocks;

public class LOTRBiomeGenLindonCoast extends LOTRBiomeGenLindon {
	public LOTRBiomeGenLindonCoast(int i, boolean major) {
		super(i, major);
		topBlock = Blocks.stone;
		topBlockMeta = 0;
		fillerBlock = topBlock;
		fillerBlockMeta = topBlockMeta;
		biomeTerrain.setXZScale(30.0);
		clearBiomeVariants();
		decorator.clearRandomStructures();
	}
}
