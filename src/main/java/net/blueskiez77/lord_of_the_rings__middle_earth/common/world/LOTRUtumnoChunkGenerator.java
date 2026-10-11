package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariantStorage;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.LOTRMapGenCavesUtumno;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

/**
 * LOTRChunkProviderUtumno: Utumno's three levels (LOTRUtumnoLevel.generateTerrain) cut through by
 * its caves, all one biome; decorated, as Middle-earth is, through LOTRDecoration.
 */
public class LOTRUtumnoChunkGenerator extends ChunkGenerator implements LOTRDecoration.Decorating {

    public static final MapCodec<LOTRUtumnoChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource)
    ).apply(i, LOTRUtumnoChunkGenerator::new));

    private volatile long seed;

    public LOTRUtumnoChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    /** Called as the level loads, with the world's seed. */
    public void init(long seed) {
        this.seed = seed;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    /** provideChunk: the levels, then the caves. */
    public LOTRChunkTerrain provideTerrain(int i, int k) {
        Random rand = new Random(i * 341873128712L + k * 132897987541L);
        LOTRChunkTerrain terrain = new LOTRChunkTerrain();
        LOTRUtumnoLevel.generateTerrain(this.seed, rand, i, k, terrain);
        LOTRBiome[] biomes = new LOTRBiome[256];
        Arrays.fill(biomes, LOTRBiomes.UTUMNO);
        new LOTRMapGenCavesUtumno().generate(this.seed, i, k, terrain, biomes, new LOTRChunkGenerator.ChunkFlags());
        LOTRBiomeVariant[] variants = new LOTRBiomeVariant[256];
        Arrays.fill(variants, LOTRBiomeVariant.STANDARD);
        terrain.variants = variants;
        return terrain;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager,
                                                        ChunkAccess chunk) {
        LOTRChunkTerrain terrain = provideTerrain(chunk.getPos().x(), chunk.getPos().z());
        Heightmap oceanFloor = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap worldSurface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        int minY = chunk.getMinY();
        for (int i1 = 0; i1 < 16; ++i1) {
            for (int k1 = 0; k1 < 16; ++k1) {
                for (int j1 = 0; j1 < LOTRChunkTerrain.HEIGHT; ++j1) {
                    BlockState state = terrain.blocks[LOTRChunkTerrain.index(i1 * 16 + k1, j1)];
                    if (state.isAir()) {
                        continue;
                    }
                    int y = j1 + minY;
                    LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(y));
                    section.setBlockState(i1, y & 15, k1, state, false);
                    oceanFloor.update(i1, y, k1, state);
                    worldSurface.update(i1, y, k1, state);
                }
            }
        }
        LOTRBiomeVariantStorage.setChunkBiomeVariants(chunk, terrain.variants);
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager,
                             StructureManager structureManager, ChunkAccess chunk) {
    }

    /** FEATURES: the original's populate, through LOTRDecoration. */
    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        LOTRDecoration.decorate(level, chunk.getPos(), this);
    }

    /** populate: the biome's decoration, over the chunk's own square (the original's reached eight blocks on). */
    @Override
    public void decorate(WorldGenLevel level, ServerLevel server, int chunkX, int chunkZ) {
        RandomSource rand = new LegacyRandomSource(this.seed);
        long l1 = rand.nextLong() / 2L * 2L + 1L;
        long l2 = rand.nextLong() / 2L * 2L + 1L;
        rand.setSeed(chunkX * l1 + chunkZ * l2 ^ this.seed);
        LOTRBiomes.UTUMNO.decorate(level, rand, chunkX * 16 - 8, chunkZ * 16 - 8);
    }

    @Override
    public BlockState terrainBlock(LOTRDecoration.TerrainCache cache, int x, int y, int z) {
        if (y < 0 || y >= LOTRChunkTerrain.HEIGHT) {
            return LOTRDecoration.outsideOriginalWorld(y);
        }
        return cache.get(x >> 4, z >> 4, this::provideTerrain).blocks[LOTRChunkTerrain.index((x & 15) * 16 + (z & 15), y)];
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
    }

    @Override
    public int getGenDepth() {
        return LOTRChunkTerrain.HEIGHT;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState randomState) {
        LOTRChunkTerrain terrain = provideTerrain(x >> 4, z >> 4);
        int xzIndex = (x & 15) * 16 + (z & 15);
        for (int y = LOTRChunkTerrain.HEIGHT - 1; y >= 0; --y) {
            if (type.isOpaque().test(terrain.blocks[LOTRChunkTerrain.index(xzIndex, y)])) {
                return y + 1;
            }
        }
        return level.getMinY();
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        LOTRChunkTerrain terrain = provideTerrain(x >> 4, z >> 4);
        int xzIndex = (x & 15) * 16 + (z & 15);
        BlockState[] column = new BlockState[LOTRChunkTerrain.HEIGHT];
        for (int y = 0; y < column.length; ++y) {
            column[y] = terrain.blocks[LOTRChunkTerrain.index(xzIndex, y)];
        }
        return new NoiseColumn(0, column);
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState randomState, BlockPos pos) {
    }
}
