package lotr.common.world.biome;

import lotr.common.world.feature.LOTRTreeType;

public class LOTRBiomeGenWoodlandRealmHills extends LOTRBiomeGenWoodlandRealm {
	public LOTRBiomeGenWoodlandRealmHills(int i, boolean major) {
		super(i, major);
		clearBiomeVariants();
		decorator.treesPerChunk = 4;
		decorator.grassPerChunk = 10;
		decorator.addTree(LOTRTreeType.GREEN_OAK_EXTREME, 500);
	}
}
