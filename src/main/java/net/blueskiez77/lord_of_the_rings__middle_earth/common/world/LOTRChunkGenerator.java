package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomeDecorator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnerAnimals;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.dwarvenmine.LOTRMapGenDwarvenMine;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.tpyr.LOTRMapGenTauredainPyramid;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariantStorage;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenLakes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMountains;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.LOTRMapGenCaves;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.LOTRMapGenRavine;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorOctaves;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import org.jspecify.annotations.Nullable;

/**
 * LOTRChunkProvider: Middle-earth's terrain, as the original built it. Each chunk's shape comes
 * from 1.7.10's octave noise, raised and roughened by a weighted sample of the biomes and variants
 * within six cells (initializeHeightNoise); each column then gets its biome's surface
 * (replaceBlocksForBiome). The world is 256 high, the sea at 62.
 *
 * <p>The terrain and its surface are both built in fillFromNoise, together, as provideChunk built
 * them, so the chunk's random numbers run as the original's did; buildSurface has nothing left
 * to do.
 */
public class LOTRChunkGenerator extends ChunkGenerator {

    public static final MapCodec<LOTRChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource)
    ).apply(i, LOTRChunkGenerator::new));

    public static final int SEA_LEVEL = 62;
    /** The world's dwarven mines and Tauredain pyramids (1.7.10's dwarvenMineGenerator and tauredainPyramid). */
    private static final LOTRMapGenDwarvenMine DWARVEN_MINES = new LOTRMapGenDwarvenMine();
    private static final LOTRMapGenTauredainPyramid TAUREDAIN_PYRAMIDS = new LOTRMapGenTauredainPyramid();
    private static final LOTRWorldGenLakes WATER_LAKES = new LOTRWorldGenLakes(Blocks.WATER.defaultBlockState());
    private static final LOTRWorldGenLakes LAVA_LAKES = new LOTRWorldGenLakes(Blocks.LAVA.defaultBlockState());
    private static final BlockState LAVA = Blocks.LAVA.defaultBlockState();
    private static final int BIOME_SAMPLE_RADIUS = 6;
    private static final int BIOME_SAMPLE_WIDTH = 2 * BIOME_SAMPLE_RADIUS + 1;
    private static final float[] BIOME_HEIGHT_NOISE = new float[BIOME_SAMPLE_WIDTH * BIOME_SAMPLE_WIDTH];

    static {
        for (int i = -BIOME_SAMPLE_RADIUS; i <= BIOME_SAMPLE_RADIUS; ++i) {
            for (int k = -BIOME_SAMPLE_RADIUS; k <= BIOME_SAMPLE_RADIUS; ++k) {
                BIOME_HEIGHT_NOISE[i + BIOME_SAMPLE_RADIUS + (k + BIOME_SAMPLE_RADIUS) * BIOME_SAMPLE_WIDTH] =
                        10.0f / Mth.sqrt(i * i + k * k + 0.2f);
            }
        }
    }

    private volatile @Nullable World world;

    public LOTRChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    /** One world's generator state: its seed, its noise (read-only once made) and its chunk manager. */
    public static final class World {
        public final long seed;
        public final LOTRWorldChunkManager chunkManager;
        final LOTRNoiseGeneratorOctaves noiseGen1;
        final LOTRNoiseGeneratorOctaves noiseGen2;
        final LOTRNoiseGeneratorOctaves noiseGen3;
        final LOTRNoiseGeneratorOctaves noiseGen5;
        final LOTRNoiseGeneratorOctaves noiseGen6;
        final LOTRNoiseGeneratorOctaves stoneNoiseGen;

        World(long seed) {
            this.seed = seed;
            this.chunkManager = new LOTRWorldChunkManager(LOTRDimension.MIDDLE_EARTH, seed);
            Random rand = new Random(seed);
            this.noiseGen1 = new LOTRNoiseGeneratorOctaves(rand, 16);
            this.noiseGen2 = new LOTRNoiseGeneratorOctaves(rand, 16);
            this.noiseGen3 = new LOTRNoiseGeneratorOctaves(rand, 8);
            this.stoneNoiseGen = new LOTRNoiseGeneratorOctaves(rand, 4);
            this.noiseGen5 = new LOTRNoiseGeneratorOctaves(rand, 10);
            this.noiseGen6 = new LOTRNoiseGeneratorOctaves(rand, 16);
        }
    }

    /** Called as the level loads, with the world's seed (the generator is made before it is known). */
    public void init(long seed) {
        World current = this.world;
        if (current == null || current.seed != seed) {
            this.world = new World(seed);
            if (this.biomeSource instanceof LOTRBiomeSource source) {
                source.init(this.world.chunkManager);
            }
        }
    }

    public World world() {
        World current = this.world;
        if (current == null) {
            throw new IllegalStateException("Middle-earth's generator was used before its world seed was given");
        }
        return current;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    /** ChunkFlags: whether the chunk is in a village, a flat one, and where roads run (D10b, D10h). */
    public static final class ChunkFlags {
        public boolean isVillage;
        public boolean isFlatVillage;
        public final boolean[] roadFlags = new boolean[256];
    }

    // --- provideChunk ------------------------------------------------------------

    /** provideChunk: the terrain and the surface of chunk (i, k), in the original's arrays. */
    public LOTRChunkTerrain provideTerrain(int i, int k) {
        World w = world();
        RandomSource rand = new LegacyRandomSource(i * 341873128712L + k * 132897987541L);
        LOTRChunkTerrain terrain = new LOTRChunkTerrain();
        ChunkFlags chunkFlags = new ChunkFlags();
        double[] blockHeightNoise = generateTerrain(w, i, k, terrain, chunkFlags);
        LOTRBiome[] biomes = w.chunkManager.getBiomeGenAt(i * 16, k * 16, 16, 16);
        LOTRBiomeVariant[] variants = w.chunkManager.getBiomeVariants(i * 16, k * 16, 16, 16);
        replaceBlocksForBiome(w, rand, i, k, terrain, biomes, variants, chunkFlags, blockHeightNoise);
        new LOTRMapGenCaves().generate(w.seed, i, k, terrain, biomes, chunkFlags);
        new LOTRMapGenRavine().generate(w.seed, i, k, terrain, biomes, chunkFlags);
        terrain.variants = variants;
        return terrain;
    }

    /** generateTerrain: stone below the noise surface, water up to the sea, air above. */
    private double[] generateTerrain(World w, int i, int j, LOTRChunkTerrain terrain, ChunkFlags chunkFlags) {
        int byte0 = 4;
        int byte1 = 32;
        int k = byte0 + 1;
        int byte3 = 33;
        int l = byte0 + 1;
        LOTRBiome[] biomesForGeneration = w.chunkManager.getBiomesForGeneration(i * byte0 - BIOME_SAMPLE_RADIUS,
                j * byte0 - BIOME_SAMPLE_RADIUS, k + BIOME_SAMPLE_WIDTH, l + BIOME_SAMPLE_WIDTH);
        LOTRBiomeVariant[] variantsForGeneration = w.chunkManager.getVariantsChunkGen(i * byte0 - BIOME_SAMPLE_RADIUS,
                j * byte0 - BIOME_SAMPLE_RADIUS, k + BIOME_SAMPLE_WIDTH, l + BIOME_SAMPLE_WIDTH, biomesForGeneration);
        double[] heightNoise = initializeHeightNoise(w, i * byte0, 0, j * byte0, k, byte3, l, biomesForGeneration,
                variantsForGeneration, chunkFlags);
        double[] blockHeightNoise = new double[terrain.blocks.length];
        for (int i1 = 0; i1 < byte0; ++i1) {
            for (int j1 = 0; j1 < byte0; ++j1) {
                for (int k1 = 0; k1 < byte1; ++k1) {
                    double d = 0.125;
                    double d1 = heightNoise[(i1 * l + j1) * byte3 + k1];
                    double d2 = heightNoise[(i1 * l + j1 + 1) * byte3 + k1];
                    double d3 = heightNoise[((i1 + 1) * l + j1) * byte3 + k1];
                    double d4 = heightNoise[((i1 + 1) * l + j1 + 1) * byte3 + k1];
                    double d5 = (heightNoise[(i1 * l + j1) * byte3 + k1 + 1] - d1) * d;
                    double d6 = (heightNoise[(i1 * l + j1 + 1) * byte3 + k1 + 1] - d2) * d;
                    double d7 = (heightNoise[((i1 + 1) * l + j1) * byte3 + k1 + 1] - d3) * d;
                    double d8 = (heightNoise[((i1 + 1) * l + j1 + 1) * byte3 + k1 + 1] - d4) * d;
                    for (int l1 = 0; l1 < 8; ++l1) {
                        double d9 = 0.25;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for (int i2 = 0; i2 < 4; ++i2) {
                            int j2 = i2 + i1 * 4 << 12 | j1 * 4 << 8 | k1 * 8 + l1;
                            double d14 = 0.25;
                            double d15 = (d11 - d10) * d14;
                            for (int k2 = 0; k2 < 4; ++k2) {
                                int blockIndex = j2 + k2 * 256;
                                double noise = d10 + d15 * k2;
                                blockHeightNoise[blockIndex] = noise;
                                terrain.blocks[blockIndex] = noise > 0.0 ? LOTRChunkTerrain.STONE
                                        : k1 * 8 + l1 <= SEA_LEVEL ? LOTRChunkTerrain.WATER : LOTRChunkTerrain.AIR;
                            }
                            d10 += d12;
                            d11 += d13;
                        }
                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
        return blockHeightNoise;
    }

    private double[] initializeHeightNoise(World w, int i, int j, int k, int xSize, int ySize, int zSize,
                                           LOTRBiome[] biomesForGeneration, LOTRBiomeVariant[] variantsForGeneration,
                                           ChunkFlags chunkFlags) {
        double[] noise = new double[xSize * ySize * zSize];
        double xzNoiseScale = 400.0;
        double heightStretch = 6.0;
        int noiseCentralIndex = (xSize - 1) / 2 + BIOME_SAMPLE_RADIUS + ((zSize - 1) / 2 + BIOME_SAMPLE_RADIUS) * (xSize + BIOME_SAMPLE_WIDTH);
        LOTRBiome noiseCentralBiome = biomesForGeneration[noiseCentralIndex];
        if (noiseCentralBiome.biomeTerrain.hasXZScale()) {
            xzNoiseScale = noiseCentralBiome.biomeTerrain.xzScale;
        }
        if (noiseCentralBiome.biomeTerrain.hasHeightStretchFactor()) {
            heightStretch *= noiseCentralBiome.biomeTerrain.heightStretchFactor;
        }
        double[] noise5 = w.noiseGen5.generateNoiseOctaves(null, i, k, xSize, zSize, 1.121, 1.121, 0.5);
        double[] noise6 = w.noiseGen6.generateNoiseOctaves(null, i, k, xSize, zSize, 200.0, 200.0, 0.5);
        double[] noise3 = w.noiseGen3.generateNoiseOctaves(null, i, j, k, xSize, ySize, zSize, 684.412 / xzNoiseScale, 2.0E-4, 684.412 / xzNoiseScale);
        double[] noise1 = w.noiseGen1.generateNoiseOctaves(null, i, j, k, xSize, ySize, zSize, 684.412, 1.0, 684.412);
        double[] noise2 = w.noiseGen2.generateNoiseOctaves(null, i, j, k, xSize, ySize, zSize, 684.412, 1.0, 684.412);
        int noiseIndexXZ = 0;
        int noiseIndex = 0;
        for (int i1 = 0; i1 < xSize; ++i1) {
            for (int k1 = 0; k1 < zSize; ++k1) {
                int xPos = i + i1 << 2;
                int zPos = k + k1 << 2;
                float totalBaseHeight = 0.0f;
                float totalHeightVariation = 0.0f;
                float totalHeightNoise = 0.0f;
                float totalVariantHillFactor = 0.0f;
                float totalFlatBiomeHeight = 0.0f;
                int biomeCount = 0;
                int centreBiomeIndex = i1 + BIOME_SAMPLE_RADIUS + (k1 + BIOME_SAMPLE_RADIUS) * (xSize + BIOME_SAMPLE_WIDTH);
                LOTRBiome centreBiome = biomesForGeneration[centreBiomeIndex];
                LOTRBiomeVariant centreVariant = variantsForGeneration[centreBiomeIndex];
                xPos += 2;
                zPos += 2;
                float centreHeight = centreBiome.rootHeight + centreVariant.getHeightBoostAt(xPos, zPos);
                if (centreVariant.absoluteHeight) {
                    centreHeight = centreVariant.getHeightBoostAt(xPos, zPos);
                }
                for (int i2 = -BIOME_SAMPLE_RADIUS; i2 <= BIOME_SAMPLE_RADIUS; ++i2) {
                    for (int k2 = -BIOME_SAMPLE_RADIUS; k2 <= BIOME_SAMPLE_RADIUS; ++k2) {
                        int biomeIndex = i1 + i2 + BIOME_SAMPLE_RADIUS + (k1 + k2 + BIOME_SAMPLE_RADIUS) * (xSize + BIOME_SAMPLE_WIDTH);
                        LOTRBiome biome = biomesForGeneration[biomeIndex];
                        LOTRBiomeVariant variant = variantsForGeneration[biomeIndex];
                        int xPosHere = xPos + (i2 << 2);
                        int zPosHere = zPos + (k2 << 2);
                        float baseHeight = biome.rootHeight + variant.getHeightBoostAt(xPosHere, zPosHere);
                        float heightVariation = biome.heightVariation * variant.hillFactor;
                        if (variant.absoluteHeight) {
                            baseHeight = variant.getHeightBoostAt(xPosHere, zPosHere);
                            heightVariation = variant.hillFactor;
                        }
                        float hillFactor = variant.hillFactor;
                        float baseHeightPlus = baseHeight + 2.0f;
                        if (baseHeightPlus == 0.0f) {
                            baseHeightPlus = 0.001f;
                        }
                        float heightNoise2 = Math.abs(BIOME_HEIGHT_NOISE[i2 + BIOME_SAMPLE_RADIUS + (k2 + BIOME_SAMPLE_RADIUS) * BIOME_SAMPLE_WIDTH]
                                / baseHeightPlus / 2.0f);
                        if (baseHeight > centreHeight) {
                            heightNoise2 /= 2.0f;
                        }
                        totalBaseHeight += baseHeight * heightNoise2;
                        totalHeightVariation += heightVariation * heightNoise2;
                        totalHeightNoise += heightNoise2;
                        totalVariantHillFactor += hillFactor;
                        float flatBiomeHeight = biome.rootHeight;
                        boolean isWater = biome.isWateryBiome();
                        if (variant.absoluteHeight && variant.absoluteHeightLevel < 0.0f) {
                            isWater = true;
                        }
                        if (isWater) {
                            flatBiomeHeight = baseHeight;
                        }
                        totalFlatBiomeHeight += flatBiomeHeight;
                        ++biomeCount;
                    }
                }
                float avgBaseHeight = totalBaseHeight / totalHeightNoise;
                float avgHeightVariation = totalHeightVariation / totalHeightNoise;
                float avgFlatBiomeHeight = totalFlatBiomeHeight / biomeCount;
                float avgVariantHillFactor = totalVariantHillFactor / biomeCount;
                if (LOTRFixedStructures.hasMapFeatures()) {
                    float roadNear = LOTRRoads.isRoadNear(xPos, zPos, 32);
                    if (roadNear >= 0.0f) {
                        avgBaseHeight = avgFlatBiomeHeight + (avgBaseHeight - avgFlatBiomeHeight) * roadNear;
                        avgHeightVariation *= roadNear;
                    }
                    float mountain = LOTRMountains.getTotalHeightBoost(xPos, zPos);
                    if (mountain > 0.005f) {
                        avgBaseHeight += mountain;
                        float mtnV = 0.2f;
                        float dv = avgHeightVariation - mtnV;
                        avgHeightVariation = mtnV + dv / (1.0f + mountain);
                    }
                }
                LOTRBiomeDecorator.VillageFlags villageFlags = centreBiome.decorator.checkForVillages(w.seed, w.chunkManager, xPos, zPos);
                chunkFlags.isVillage = villageFlags.isVillage();
                chunkFlags.isFlatVillage = villageFlags.isFlatVillage();
                if (chunkFlags.isFlatVillage) {
                    avgBaseHeight = avgFlatBiomeHeight;
                    avgHeightVariation = 0.0f;
                }
                avgBaseHeight = (avgBaseHeight * 4.0f - 1.0f) / 8.0f;
                if (avgHeightVariation == 0.0f) {
                    avgHeightVariation = 0.001f;
                }
                double heightNoise = noise6[noiseIndexXZ] / 8000.0;
                if (heightNoise < 0.0) {
                    heightNoise = -heightNoise * 0.3;
                }
                heightNoise = heightNoise * 3.0 - 2.0;
                if (heightNoise < 0.0) {
                    heightNoise /= 2.0;
                    if (heightNoise < -1.0) {
                        heightNoise = -1.0;
                    }
                    heightNoise /= 1.4;
                    heightNoise /= 2.0;
                } else {
                    if (heightNoise > 1.0) {
                        heightNoise = 1.0;
                    }
                    heightNoise /= 8.0;
                }
                ++noiseIndexXZ;
                for (int j1 = 0; j1 < ySize; ++j1) {
                    double baseHeight = avgBaseHeight;
                    baseHeight += heightNoise * 0.2 * avgVariantHillFactor;
                    baseHeight = baseHeight * ySize / 16.0;
                    double var28 = ySize / 2.0 + baseHeight * 4.0;
                    double var32 = (j1 - var28) * heightStretch * 128.0 / 256.0 / avgHeightVariation;
                    if (var32 < 0.0) {
                        var32 *= 4.0;
                    }
                    double var34 = noise1[noiseIndex] / 512.0;
                    double var36 = noise2[noiseIndex] / 512.0;
                    double var38 = (noise3[noiseIndex] / 10.0 + 1.0) / 2.0 * avgVariantHillFactor;
                    double totalNoise = var38 < 0.0 ? var34 : var38 > 1.0 ? var36 : var34 + (var36 - var34) * var38;
                    totalNoise -= var32;
                    if (j1 > ySize - 4) {
                        double var40 = (j1 - (ySize - 4)) / 3.0f;
                        totalNoise = totalNoise * (1.0 - var40) + -10.0 * var40;
                    }
                    noise[noiseIndex] = totalNoise;
                    ++noiseIndex;
                }
            }
        }
        return noise;
    }

    /** replaceBlocksForBiome: each column's biome lays its surface over the bare stone. */
    private void replaceBlocksForBiome(World w, RandomSource rand, int i, int k, LOTRChunkTerrain terrain, LOTRBiome[] biomeArray,
                                       LOTRBiomeVariant[] variantArray, ChunkFlags chunkFlags, double[] blockHeightNoise) {
        double d = 0.03125;
        double[] stoneNoise = w.stoneNoiseGen.generateNoiseOctaves(null, i * 16, k * 16, 0, 16, 16, 1, d * 2.0, d * 2.0, d * 2.0);
        for (int i1 = 0; i1 < 16; ++i1) {
            for (int k1 = 0; k1 < 16; ++k1) {
                int x = i * 16 + i1;
                int z = k * 16 + k1;
                int xzIndex = i1 * 16 + k1;
                int xzIndexBiome = i1 + k1 * 16;
                LOTRBiome biome = biomeArray[xzIndexBiome];
                LOTRBiomeVariant variant = variantArray[xzIndexBiome];
                int height = 0;
                for (int j = LOTRChunkTerrain.HEIGHT - 1; j >= 0; --j) {
                    if (LOTRChunkTerrain.isOpaque(terrain.blocks[LOTRChunkTerrain.index(xzIndex, j)])) {
                        height = j;
                        break;
                    }
                }
                biome.generateBiomeTerrain(w.seed, rand, terrain, x, z, stoneNoise[xzIndex], height, variant);
                if (!LOTRFixedStructures.hasMapFeatures()) {
                    continue;
                }
                chunkFlags.roadFlags[xzIndex] = LOTRRoadGenerator.generateRoad(rand, x, z, biome, terrain, blockHeightNoise);
                int lavaHeight = LOTRMountains.getLavaHeight(x, z);
                for (int j = lavaHeight; lavaHeight > 0 && j >= 0
                        && !LOTRChunkTerrain.isOpaque(terrain.blocks[LOTRChunkTerrain.index(xzIndex, j)]); --j) {
                    terrain.blocks[LOTRChunkTerrain.index(xzIndex, j)] = LAVA;
                }
            }
        }
    }

    // --- ChunkGenerator ---------------------------------------------------------

    @Override
    public CompletableFuture<ChunkAccess> createBiomes(RandomState randomState, Blender blender, StructureManager structureManager,
                                                       ChunkAccess chunk) {
        if (this.biomeSource instanceof LOTRBiomeSource source) {
            int qx = chunk.getPos().getMinBlockX() >> 2;
            int qz = chunk.getPos().getMinBlockZ() >> 2;
            var holders = source.holders(qx, qz, 4, 4);
            chunk.fillBiomesFromNoise((x, y, z, sampler) -> holders[(x - qx) + (z - qz) * 4], randomState.sampler());
            return CompletableFuture.completedFuture(chunk);
        }
        return super.createBiomes(randomState, blender, structureManager, chunk);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager,
                                                        ChunkAccess chunk) {
        int chunkX = chunk.getPos().x();
        int chunkZ = chunk.getPos().z();
        LOTRChunkTerrain terrain = provideTerrain(chunkX, chunkZ);
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

    /**
     * populate: water and lava lakes, the biome's decorator, animals, then snow and
     * ice -- over the area from eight blocks into this chunk, as the original's populate ran.
     */
    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        // Middle-earth is decorated by LOTRChunkPopulator, as 1.7.10 populated it: see populate.
    }

    /**
     * populate: the chunk's decoration, as 1.7.10's ChunkProviderGenerate.populate did it once the
     * chunk and its neighbours to the east, south and south-east were there -- lakes, the biome's
     * decorator, then ice and snow -- in the level itself, so what it builds may reach into any
     * chunk about it.
     */
    public void populate(ServerLevel level, int i, int j) {
        World w = world();
        int k = i * 16;
        int l = j * 16;
        RandomSource rand = new LegacyRandomSource(w.seed);
        long l1 = rand.nextLong() / 2L * 2L + 1L;
        long l2 = rand.nextLong() / 2L * 2L + 1L;
        rand.setSeed(i * l1 + j * l2 ^ w.seed);
        DWARVEN_MINES.generateStructuresInChunk(level, w.chunkManager, rand, i, j);
        TAUREDAIN_PYRAMIDS.generateStructuresInChunk(level, w.chunkManager, rand, i, j);
        if (rand.nextInt(4) == 0) {
            int i1 = k + rand.nextInt(16) + 8;
            int j1 = rand.nextInt(128);
            int k1 = l + rand.nextInt(16) + 8;
            if (j1 < 60) {
                WATER_LAKES.generate(level, rand, i1, j1, k1);
            }
        }
        if (rand.nextInt(8) == 0) {
            int i1 = k + rand.nextInt(16) + 8;
            int j1 = rand.nextInt(rand.nextInt(120) + 8);
            int k1 = l + rand.nextInt(16) + 8;
            if (j1 < 60) {
                LAVA_LAKES.generate(level, rand, i1, j1, k1);
            }
        }
        LOTRBiome biome = w.chunkManager.getBiomeGenAt(k + 16, l + 16);
        LOTRBiomeVariant variant = w.chunkManager.getBiomeVariantAt(k + 16, l + 16);
        biome.decorate(level, rand, k, l);
        if (biome.getChanceToSpawnAnimals() <= 1.0f) {
            if (rand.nextFloat() < biome.getChanceToSpawnAnimals()) {
                LOTRSpawnerAnimals.worldGenSpawnAnimals(level, biome, variant, k + 8, l + 8, rand);
            }
        } else {
            int spawns = Mth.floor(biome.getChanceToSpawnAnimals());
            for (int i12 = 0; i12 < spawns; ++i12) {
                LOTRSpawnerAnimals.worldGenSpawnAnimals(level, biome, variant, k + 8, l + 8, rand);
            }
        }
        k += 8;
        l += 8;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i1 = 0; i1 < 16; ++i1) {
            for (int k1 = 0; k1 < 16; ++k1) {
                int j1 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k + i1, l + k1);
                if (LOTRWorldGenUtil.isBlockFreezable(level, pos.set(k + i1, j1 - 1, l + k1))) {
                    level.setBlock(pos, Blocks.ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
                }
                if (LOTRWorldGenUtil.canSnowAt(level, pos.set(k + i1, j1, l + k1))) {
                    level.setBlock(pos, Blocks.SNOW.defaultBlockState(), Block.UPDATE_CLIENTS);
                }
            }
        }
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
        return SEA_LEVEL;
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
        World w = this.world;
        if (w == null) {
            return;
        }
        LOTRBiome biome = w.chunkManager.getBiomeGenAt(pos.getX(), pos.getZ());
        LOTRBiomeVariant variant = w.chunkManager.getBiomeVariants(pos.getX(), pos.getZ(), 1, 1)[0];
        info.add("Middle-earth biome: " + biome.biomeName + ", ID: " + biome.biomeID + ", c: #" + String.format("%06x", biome.color & 0xFFFFFF));
        info.add("Variant: " + variant.variantName);
    }
}
