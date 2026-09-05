package lotr.common.world.biome.variant;

public class LOTRBiomeVariantForest extends LOTRBiomeVariant {
	public LOTRBiomeVariantForest(int i, String s) {
		super(i, s, LOTRBiomeVariant.VariantScale.ALL);
		setTemperatureRainfall(0.0f, 0.3f);
		setTrees(8.0f);
		setGrass(2.0f);
	}
}
