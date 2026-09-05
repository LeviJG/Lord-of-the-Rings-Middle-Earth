package lotr.common.world.biome.variant;

public class LOTRBiomeVariantDeadForest extends LOTRBiomeVariant {
	public LOTRBiomeVariantDeadForest(int i, String s) {
		super(i, s, LOTRBiomeVariant.VariantScale.ALL);
		setTemperatureRainfall(0.0f, -0.3f);
		setTrees(3.0f);
		setGrass(0.5f);
	}
}
