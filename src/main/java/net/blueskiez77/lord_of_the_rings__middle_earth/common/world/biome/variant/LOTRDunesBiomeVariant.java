package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;

import net.minecraft.util.Mth;

/** LOTRBiomeVariantDunes: sand waves up to twelve blocks high, running north to south. */
public class LOTRDunesBiomeVariant extends LOTRBiomeVariant {

    private static final LOTRNoiseGeneratorPerlin DUNE_WAVE_NOISE = new LOTRNoiseGeneratorPerlin(305620668206968L, 1);

    public LOTRDunesBiomeVariant(int i, String s) {
        super(i, s, VariantScale.ALL);
        setTemperatureRainfall(0.1f, -0.1f);
    }

    public int getDuneHeightAt(int i, int k) {
        double d1 = DUNE_WAVE_NOISE.getValue(i * 0.02, k * 0.02);
        double d2 = DUNE_WAVE_NOISE.getValue(i * 0.7, k * 0.7);
        double d3 = Mth.clamp(d1 * 0.9 + d2 * 0.1, -1.0, 1.0);
        int maxDuneHeight = 12;
        return Math.round((Mth.sin((i + (float) (d3 * 15.0)) * 0.09f) + 1.0f) / 2.0f * maxDuneHeight);
    }

    @Override
    public float getHeightBoostAt(int i, int k) {
        return getDuneHeightAt(i, k) / 22.0f;
    }
}
