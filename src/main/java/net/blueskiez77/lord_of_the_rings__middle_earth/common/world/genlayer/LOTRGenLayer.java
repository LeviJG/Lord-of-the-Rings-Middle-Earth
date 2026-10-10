package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGenLayer, on 1.7.10's GenLayer: a layer of the biome map, asked for a rectangle of ints at a
 * time, with GenLayer's seeded pseudo-random numbers -- a world seed mixed with the layer's own,
 * then with each position asked for. A layer keeps its random state between calls, so each thread
 * generating terrain has its own set of layers (LOTRGenLayers).
 */
public abstract class LOTRGenLayer {

    private static final long MULTIPLIER = 6364136223846793005L;
    private static final long INCREMENT = 1442695040888963407L;

    protected @Nullable LOTRGenLayer lotrParent;
    private long baseSeed;
    private long worldGenSeed;
    private long chunkSeed;

    protected LOTRGenLayer(long seed) {
        this.baseSeed = seed;
        this.baseSeed *= this.baseSeed * MULTIPLIER + INCREMENT;
        this.baseSeed += seed;
        this.baseSeed *= this.baseSeed * MULTIPLIER + INCREMENT;
        this.baseSeed += seed;
        this.baseSeed *= this.baseSeed * MULTIPLIER + INCREMENT;
        this.baseSeed += seed;
    }

    public abstract int[] getInts(int i, int k, int xSize, int zSize);

    public void initWorldGenSeed(long seed) {
        this.worldGenSeed = seed;
        this.worldGenSeed *= this.worldGenSeed * MULTIPLIER + INCREMENT;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * MULTIPLIER + INCREMENT;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * MULTIPLIER + INCREMENT;
        this.worldGenSeed += this.baseSeed;
        if (this.lotrParent != null) {
            this.lotrParent.initWorldGenSeed(seed);
        }
    }

    protected void initChunkSeed(long x, long z) {
        this.chunkSeed = this.worldGenSeed;
        this.chunkSeed *= this.chunkSeed * MULTIPLIER + INCREMENT;
        this.chunkSeed += x;
        this.chunkSeed *= this.chunkSeed * MULTIPLIER + INCREMENT;
        this.chunkSeed += z;
        this.chunkSeed *= this.chunkSeed * MULTIPLIER + INCREMENT;
        this.chunkSeed += x;
        this.chunkSeed *= this.chunkSeed * MULTIPLIER + INCREMENT;
        this.chunkSeed += z;
    }

    protected int nextInt(int bound) {
        int i = (int) ((this.chunkSeed >> 24) % bound);
        if (i < 0) {
            i += bound;
        }
        this.chunkSeed *= this.chunkSeed * MULTIPLIER + INCREMENT;
        this.chunkSeed += this.worldGenSeed;
        return i;
    }

    protected int selectRandom(int... ints) {
        return ints[nextInt(ints.length)];
    }

    protected int selectModeOrRandom(int a, int b, int c, int d) {
        if (b == c && c == d) {
            return b;
        }
        if (a == b && a == c) {
            return a;
        }
        if (a == b && a == d) {
            return a;
        }
        if (a == c && a == d) {
            return a;
        }
        if (a == b && c != d) {
            return a;
        }
        if (a == c && b != d) {
            return a;
        }
        if (a == d && b != c) {
            return a;
        }
        if (b == c && a != d) {
            return b;
        }
        if (b == d && a != c) {
            return b;
        }
        if (c == d && a != b) {
            return c;
        }
        return selectRandom(a, b, c, d);
    }
}
