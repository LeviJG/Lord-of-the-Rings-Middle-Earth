package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise;

import java.util.Random;

/**
 * 1.7.10's NoiseGeneratorImproved: Ken Perlin's improved noise, seeded from a java.util.Random,
 * filling an array a column at a time. Kept exactly, so that LOTRChunkProvider's terrain comes
 * out as it did -- vanilla's ImprovedNoise is the same noise, but its two-dimensional case is not
 * 1.7.10's, which sampled the y cell 0 without the y offset.
 */
public class LOTRNoiseGeneratorImproved {

    private final int[] permutations = new int[512];
    public final double xCoord;
    public final double yCoord;
    public final double zCoord;

    public LOTRNoiseGeneratorImproved(Random random) {
        this.xCoord = random.nextDouble() * 256.0;
        this.yCoord = random.nextDouble() * 256.0;
        this.zCoord = random.nextDouble() * 256.0;
        for (int i = 0; i < 256; ++i) {
            this.permutations[i] = i;
        }
        for (int i = 0; i < 256; ++i) {
            int j = random.nextInt(256 - i) + i;
            int k = this.permutations[i];
            this.permutations[i] = this.permutations[j];
            this.permutations[j] = k;
            this.permutations[i + 256] = this.permutations[i];
        }
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private static double grad2(int hash, double x, double z) {
        int j = hash & 15;
        double d3 = (1 - ((j & 8) >> 3)) * x;
        double d4 = j < 4 ? 0.0 : (j != 12 && j != 14 ? z : x);
        return ((j & 1) == 0 ? d3 : -d3) + ((j & 2) == 0 ? d4 : -d4);
    }

    private static double grad(int hash, double x, double y, double z) {
        int j = hash & 15;
        double d3 = j < 8 ? x : y;
        double d4 = j < 4 ? y : (j != 12 && j != 14 ? z : x);
        return ((j & 1) == 0 ? d3 : -d3) + ((j & 2) == 0 ? d4 : -d4);
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
    }

    /** populateNoiseArray: adds this octave, divided by {@code amplitude}, into {@code noise}. */
    public void populateNoiseArray(double[] noise, double x, double y, double z, int xSize, int ySize, int zSize,
                                   double xScale, double yScale, double zScale, double amplitude) {
        double scale = 1.0 / amplitude;
        int index = 0;
        int[] p = this.permutations;
        if (ySize == 1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                double dx = x + i1 * xScale + this.xCoord;
                int ix = (int) dx;
                if (dx < ix) {
                    --ix;
                }
                int xi = ix & 255;
                dx -= ix;
                double u = fade(dx);
                for (int k1 = 0; k1 < zSize; ++k1) {
                    double dz = z + k1 * zScale + this.zCoord;
                    int iz = (int) dz;
                    if (dz < iz) {
                        --iz;
                    }
                    int zi = iz & 255;
                    dz -= iz;
                    double w = fade(dz);
                    int a = p[xi];
                    int aa = p[a] + zi;
                    int b = p[xi + 1];
                    int ba = p[b] + zi;
                    double d1 = lerp(u, grad2(p[aa], dx, dz), grad(p[ba], dx - 1.0, 0.0, dz));
                    double d2 = lerp(u, grad(p[aa + 1], dx, 0.0, dz - 1.0), grad(p[ba + 1], dx - 1.0, 0.0, dz - 1.0));
                    noise[index++] += lerp(w, d1, d2) * scale;
                }
            }
            return;
        }
        // The x-interpolated gradients are worked out once per y cell, at the first y sampled in it,
        // and reused for the rest of that cell: 1.7.10's own shortcut, which its terrain looks like.
        int lastYCell = -1;
        double x1 = 0.0;
        double x2 = 0.0;
        double x3 = 0.0;
        double x4 = 0.0;
        for (int i1 = 0; i1 < xSize; ++i1) {
            double dx = x + i1 * xScale + this.xCoord;
            int ix = (int) dx;
            if (dx < ix) {
                --ix;
            }
            int xi = ix & 255;
            dx -= ix;
            double u = fade(dx);
            for (int k1 = 0; k1 < zSize; ++k1) {
                double dz = z + k1 * zScale + this.zCoord;
                int iz = (int) dz;
                if (dz < iz) {
                    --iz;
                }
                int zi = iz & 255;
                dz -= iz;
                double w = fade(dz);
                for (int j1 = 0; j1 < ySize; ++j1) {
                    double dy = y + j1 * yScale + this.yCoord;
                    int iy = (int) dy;
                    if (dy < iy) {
                        --iy;
                    }
                    int yi = iy & 255;
                    dy -= iy;
                    double v = fade(dy);
                    if (j1 == 0 || yi != lastYCell) {
                        lastYCell = yi;
                        int a = p[xi] + yi;
                        int aa = p[a] + zi;
                        int ab = p[a + 1] + zi;
                        int b = p[xi + 1] + yi;
                        int ba = p[b] + zi;
                        int bb = p[b + 1] + zi;
                        x1 = lerp(u, grad(p[aa], dx, dy, dz), grad(p[ba], dx - 1.0, dy, dz));
                        x2 = lerp(u, grad(p[ab], dx, dy - 1.0, dz), grad(p[bb], dx - 1.0, dy - 1.0, dz));
                        x3 = lerp(u, grad(p[aa + 1], dx, dy, dz - 1.0), grad(p[ba + 1], dx - 1.0, dy, dz - 1.0));
                        x4 = lerp(u, grad(p[ab + 1], dx, dy - 1.0, dz - 1.0), grad(p[bb + 1], dx - 1.0, dy - 1.0, dz - 1.0));
                    }
                    double y1 = lerp(v, x1, x2);
                    double y2 = lerp(v, x3, x4);
                    noise[index++] += lerp(w, y1, y2) * scale;
                }
            }
        }
    }
}
