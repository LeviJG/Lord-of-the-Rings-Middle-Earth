package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.util.Mth;

/** 1.7.10's NoiseGeneratorOctaves: octaves of LOTRNoiseGeneratorImproved, each half the scale and twice the weight of the last. */
public class LOTRNoiseGeneratorOctaves {

    private final LOTRNoiseGeneratorImproved[] generators;

    public LOTRNoiseGeneratorOctaves(Random random, int octaves) {
        this.generators = new LOTRNoiseGeneratorImproved[octaves];
        for (int i = 0; i < octaves; ++i) {
            this.generators[i] = new LOTRNoiseGeneratorImproved(random);
        }
    }

    public double[] generateNoiseOctaves(double[] noise, int x, int y, int z, int xSize, int ySize, int zSize,
                                         double xScale, double yScale, double zScale) {
        if (noise == null || noise.length < xSize * ySize * zSize) {
            noise = new double[xSize * ySize * zSize];
        } else {
            Arrays.fill(noise, 0.0);
        }
        double amplitude = 1.0;
        for (LOTRNoiseGeneratorImproved generator : this.generators) {
            double dx = x * amplitude * xScale;
            double dy = y * amplitude * yScale;
            double dz = z * amplitude * zScale;
            long lx = Mth.lfloor(dx);
            long lz = Mth.lfloor(dz);
            dx -= lx;
            dz -= lz;
            lx %= 16777216L;
            lz %= 16777216L;
            dx += lx;
            dz += lz;
            generator.populateNoiseArray(noise, dx, dy, dz, xSize, ySize, zSize,
                    xScale * amplitude, yScale * amplitude, zScale * amplitude, amplitude);
            amplitude /= 2.0;
        }
        return noise;
    }

    /** The two-dimensional overload: y 10, one high. */
    public double[] generateNoiseOctaves(double[] noise, int x, int z, int xSize, int zSize, double xScale, double zScale, double unused) {
        return generateNoiseOctaves(noise, x, 10, z, xSize, 1, zSize, xScale, 1.0, zScale);
    }
}
