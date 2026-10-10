package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

/** LOTRBiomeVariantForest. */
public class LOTRForestBiomeVariant extends LOTRBiomeVariant {

    public LOTRForestBiomeVariant(int i, String s) {
        super(i, s, VariantScale.ALL);
        setTemperatureRainfall(0.0f, 0.3f);
        setTrees(8.0f);
        setGrass(2.0f);
    }
}
