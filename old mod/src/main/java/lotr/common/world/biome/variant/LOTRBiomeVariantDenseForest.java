package lotr.common.world.biome.variant;

public class LOTRBiomeVariantDenseForest extends LOTRBiomeVariant {
	public LOTRBiomeVariantDenseForest(int i, String s) {
		super(i, s, LOTRBiomeVariant.VariantScale.LARGE);
		setTemperatureRainfall(0.1f, 0.3f);
		setHeight(0.5f, 2.0f);
		setTrees(8.0f);
		setGrass(2.0f);
	}
}
