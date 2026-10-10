package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

/** LOTRBiomeVariantDeadForest. */
public class LOTRDeadForestBiomeVariant extends LOTRBiomeVariant {

    public LOTRDeadForestBiomeVariant(int i, String s) {
        super(i, s, VariantScale.ALL);
        setTemperatureRainfall(0.0f, -0.3f);
        setTrees(3.0f);
        setGrass(0.5f);
    }
}
