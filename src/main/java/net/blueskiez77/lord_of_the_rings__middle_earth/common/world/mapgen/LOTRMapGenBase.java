package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen;

import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;

/**
 * 1.7.10's MapGenBase: something carved through the terrain from chunks up to {@code range} away
 * -- each of those chunks seeded from the world seed and its position, so a tunnel begun in one
 * chunk carries on through its neighbours. Made fresh for each chunk carved, its random state
 * being its own.
 */
public abstract class LOTRMapGenBase {

    protected final int range = 8;
    protected final Random rand = new Random();
    protected LOTRBiome[] biomes;
    protected LOTRChunkGenerator.ChunkFlags chunkFlags;

    /** func_151539_a: carve chunk (chunkX, chunkZ), its block-resolution biomes given. */
    public void generate(long worldSeed, int chunkX, int chunkZ, LOTRChunkTerrain terrain, LOTRBiome[] chunkBiomes,
                         LOTRChunkGenerator.ChunkFlags flags) {
        this.biomes = chunkBiomes;
        this.chunkFlags = flags;
        this.rand.setSeed(worldSeed);
        long l = this.rand.nextLong();
        long l1 = this.rand.nextLong();
        for (int i = chunkX - this.range; i <= chunkX + this.range; ++i) {
            for (int k = chunkZ - this.range; k <= chunkZ + this.range; ++k) {
                this.rand.setSeed(i * l ^ k * l1 ^ worldSeed);
                recursiveGenerate(i, k, chunkX, chunkZ, terrain);
            }
        }
    }

    /** func_151538_a: what chunk (i, k) starts that reaches chunk (chunkX, chunkZ). */
    protected abstract void recursiveGenerate(int i, int k, int chunkX, int chunkZ, LOTRChunkTerrain terrain);

    /** The biome at a column of the chunk being carved. */
    protected LOTRBiome biomeAt(int x, int z) {
        return this.biomes[(x & 15) + (z & 15) * 16];
    }
}
