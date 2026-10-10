package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

/** LOTRBiomeVariantDenseForest. */
public class LOTRDenseForestBiomeVariant extends LOTRBiomeVariant {

    public LOTRDenseForestBiomeVariant(int i, String s) {
        super(i, s, VariantScale.LARGE);
        setTemperatureRainfall(0.1f, 0.3f);
        setHeight(0.5f, 2.0f);
        setTrees(8.0f);
        setGrass(2.0f);
    }
}
