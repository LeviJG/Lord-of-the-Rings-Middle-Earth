package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

/**
 * 1.7.10's NoiseGeneratorPerlin: octaves of two-dimensional simplex noise. Vanilla's SimplexNoise
 * is 1.7.10's NoiseGeneratorSimplex, seeded the same way, and a legacy random source is
 * java.util.Random's sequence, so the noise is the original's.
 */
public class LOTRNoiseGeneratorPerlin {

    private final SimplexNoise[] generators;

    public LOTRNoiseGeneratorPerlin(long seed, int octaves) {
        this(new LegacyRandomSource(seed), octaves);
    }

    public LOTRNoiseGeneratorPerlin(RandomSource random, int octaves) {
        this.generators = new SimplexNoise[octaves];
        for (int i = 0; i < octaves; ++i) {
            this.generators[i] = new SimplexNoise(random);
        }
    }

    /** func_151601_a. */
    public double getValue(double x, double z) {
        double total = 0.0;
        double scale = 1.0;
        for (SimplexNoise generator : this.generators) {
            total += generator.getValue(x * scale, z * scale) / scale;
            scale /= 2.0;
        }
        return total;
    }
}
