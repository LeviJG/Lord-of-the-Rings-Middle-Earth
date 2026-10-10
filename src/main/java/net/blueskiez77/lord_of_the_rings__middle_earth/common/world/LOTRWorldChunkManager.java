package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFarHaradJungleBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFarHaradMangroveBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariantList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer.LOTRGenLayer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer.LOTRGenLayerBiomeVariants;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer.LOTRGenLayerBiomeVariantsLake;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer.LOTRGenLayerRiver;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer.LOTRGenLayers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillagePositionCache;

import net.minecraft.world.level.WorldGenLevel;

import org.jspecify.annotations.Nullable;

/**
 * LOTRWorldChunkManager: a dimension's biomes and biome variants for one world seed, from its
 * genlayers -- at four-block resolution for shaping the terrain (chunkGen), at block resolution
 * for its surface and for the world (world).
 *
 * <p>The layers keep random state as they work, so each thread has its own.
 */
public final class LOTRWorldChunkManager {

    public final LOTRDimension dimension;
    public final long seed;
    private final ThreadLocal<LOTRGenLayers> layers;

    public LOTRWorldChunkManager(LOTRDimension dimension, long seed) {
        this.dimension = dimension;
        this.seed = seed;
        this.layers = ThreadLocal.withInitial(() -> new LOTRGenLayers(dimension, seed));
    }

    private LOTRGenLayer[] chunkGenLayers() {
        return this.layers.get().chunkGenLayers;
    }

    private LOTRGenLayer[] worldLayers() {
        return this.layers.get().worldLayers;
    }

    /** getBiomesForGeneration: the biomes at four-block resolution, i and k in units of four blocks. */
    public LOTRBiome[] getBiomesForGeneration(int i, int k, int xSize, int zSize) {
        return toBiomes(chunkGenLayers()[LOTRGenLayers.LAYER_BIOME].getInts(i, k, xSize, zSize));
    }

    /** getBiomeGenAt / loadBlockGeneratorData: the biomes at block resolution. */
    public LOTRBiome[] getBiomeGenAt(int i, int k, int xSize, int zSize) {
        return toBiomes(worldLayers()[LOTRGenLayers.LAYER_BIOME].getInts(i, k, xSize, zSize));
    }

    public LOTRBiome getBiomeGenAt(int i, int k) {
        return getBiomeGenAt(i, k, 1, 1)[0];
    }

    /** areBiomesViable: whether every biome within range (at four-block resolution) is one of these. */
    public boolean areBiomesViable(int i, int k, int range, List<LOTRBiome> list) {
        int i1 = i - range >> 2;
        int k1 = k - range >> 2;
        int i2 = i + range >> 2;
        int k2 = k + range >> 2;
        int i3 = i2 - i1 + 1;
        int k3 = k2 - k1 + 1;
        for (LOTRBiome biome : getBiomesForGeneration(i1, k1, i3, k3)) {
            if (!list.contains(biome)) {
                return false;
            }
        }
        return true;
    }

    /** areVariantsSuitableVillage: no steep, wooded, village-free or sunken variant within range. */
    public boolean areVariantsSuitableVillage(int i, int k, int range, boolean requireFlat) {
        int i1 = i - range >> 2;
        int k1 = k - range >> 2;
        int i2 = i + range >> 2;
        int k2 = k + range >> 2;
        int i3 = i2 - i1 + 1;
        int k3 = k2 - k1 + 1;
        LOTRBiome[] biomes = getBiomesForGeneration(i1, k1, i3, k3);
        for (LOTRBiomeVariant v : getVariantsChunkGen(i1, k1, i3, k3, biomes)) {
            if (v.hillFactor > 1.6f || requireFlat && v.hillFactor > 1.0f || v.treeFactor > 1.0f) {
                return false;
            }
            if (v.disableVillages) {
                return false;
            }
            if (v.absoluteHeight && v.absoluteHeightLevel < 0.0f) {
                return false;
            }
        }
        return true;
    }

    private final Map<LOTRVillageGen, LOTRVillagePositionCache> villageCacheMap = new ConcurrentHashMap<>();

    public LOTRVillagePositionCache getVillageCache(LOTRVillageGen village) {
        return this.villageCacheMap.computeIfAbsent(village, v -> new LOTRVillagePositionCache());
    }

    private final Map<Object, LOTRVillagePositionCache> structureCacheMap = new ConcurrentHashMap<>();

    /** getStructureCache: where a structure of a map-gen kind does and does not start. */
    public LOTRVillagePositionCache getStructureCache(Object mapGen) {
        return this.structureCacheMap.computeIfAbsent(mapGen, v -> new LOTRVillagePositionCache());
    }

    /** getBiomeVariantAt: the variant at one block, as the chunk there keeps it. */
    public LOTRBiomeVariant getBiomeVariantAt(int i, int k) {
        return getBiomeVariants(i, k, 1, 1)[0];
    }

    /** {@code world.getWorldChunkManager()}: the chunk manager of a Middle-earth level, or null for any other. */
    public static @Nullable LOTRWorldChunkManager of(WorldGenLevel level) {
        return level.getLevel().getChunkSource().getGenerator() instanceof LOTRChunkGenerator generator
                ? generator.world().chunkManager : null;
    }

    private LOTRBiome[] toBiomes(int[] ints) {
        LOTRBiome[] biomes = new LOTRBiome[ints.length];
        for (int l = 0; l < ints.length; ++l) {
            biomes[l] = this.dimension.biomeList[ints[l]];
        }
        return biomes;
    }

    /** getVariantsChunkGen: the variants at four-block resolution, for the biomes given. */
    public LOTRBiomeVariant[] getVariantsChunkGen(int i, int k, int xSize, int zSize, LOTRBiome[] biomes) {
        return getBiomeVariantsFromLayers(i, k, xSize, zSize, biomes, true);
    }

    /** getBiomeVariants: the variants at block resolution. */
    public LOTRBiomeVariant[] getBiomeVariants(int i, int k, int xSize, int zSize) {
        return getBiomeVariantsFromLayers(i, k, xSize, zSize, getBiomeGenAt(i, k, xSize, zSize), false);
    }

    /**
     * getBiomeVariantsFromLayers: a variant of the biome's own where the large or small variant
     * layer falls under its chance, a lake where the lake layer has one (jungle and mangrove lakes
     * for those biomes), a river along a river -- none of them by a mountain or a fixed village,
     * and no random lake or river by a structure.
     */
    public LOTRBiomeVariant[] getBiomeVariantsFromLayers(int i, int k, int xSize, int zSize, LOTRBiome[] biomes, boolean isChunkGeneration) {
        LOTRGenLayer[] sourceLayers = isChunkGeneration ? chunkGenLayers() : worldLayers();
        int[] variantsLargeInts = sourceLayers[LOTRGenLayers.LAYER_VARIANTS_LARGE].getInts(i, k, xSize, zSize);
        int[] variantsSmallInts = sourceLayers[LOTRGenLayers.LAYER_VARIANTS_SMALL].getInts(i, k, xSize, zSize);
        int[] variantsLakesInts = sourceLayers[LOTRGenLayers.LAYER_VARIANTS_LAKES].getInts(i, k, xSize, zSize);
        int[] variantsRiversInts = sourceLayers[LOTRGenLayers.LAYER_VARIANTS_RIVERS].getInts(i, k, xSize, zSize);
        LOTRBiomeVariant[] variants = new LOTRBiomeVariant[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                int index = i1 + k1 * xSize;
                LOTRBiome biome = biomes[index];
                LOTRBiomeVariant variant = LOTRBiomeVariant.STANDARD;
                int xPos = i + i1;
                int zPos = k + k1;
                if (isChunkGeneration) {
                    xPos <<= 2;
                    zPos <<= 2;
                }
                boolean mountainNear = isMountainNear(xPos, zPos);
                boolean structureNear = isStructureNear(xPos, zPos);
                boolean fixedVillageNear = anyFixedVillagesAt(biome, xPos, zPos);
                if (!mountainNear && !fixedVillageNear) {
                    float variantChance = biome.variantChance;
                    if (variantChance > 0.0f) {
                        for (int pass = 0; pass <= 1; ++pass) {
                            LOTRBiomeVariantList variantList = pass == 0 ? biome.getBiomeVariantsLarge() : biome.getBiomeVariantsSmall();
                            if (variantList.isEmpty()) {
                                continue;
                            }
                            int variantCode = (pass == 0 ? variantsLargeInts : variantsSmallInts)[index];
                            float variantF = (float) variantCode / LOTRGenLayerBiomeVariants.RANDOM_MAX;
                            if (variantF < variantChance) {
                                variant = variantList.get(variantF / variantChance);
                                break;
                            }
                            variant = LOTRBiomeVariant.STANDARD;
                        }
                    }
                    if (!structureNear && biome.getEnableRiver()) {
                        int lakeCode = variantsLakesInts[index];
                        if (LOTRGenLayerBiomeVariantsLake.getFlag(lakeCode, LOTRGenLayerBiomeVariantsLake.FLAG_LAKE)) {
                            variant = LOTRBiomeVariant.LAKE;
                        }
                        if (LOTRGenLayerBiomeVariantsLake.getFlag(lakeCode, LOTRGenLayerBiomeVariantsLake.FLAG_JUNGLE)
                                && biome instanceof LOTRFarHaradJungleBiome jungle && jungle.hasJungleLakes()) {
                            variant = LOTRBiomeVariant.LAKE;
                        }
                        if (LOTRGenLayerBiomeVariantsLake.getFlag(lakeCode, LOTRGenLayerBiomeVariantsLake.FLAG_MANGROVE)
                                && biome instanceof LOTRFarHaradMangroveBiome) {
                            variant = LOTRBiomeVariant.LAKE;
                        }
                    }
                }
                int riverCode = variantsRiversInts[index];
                if (riverCode == LOTRGenLayerRiver.MAP_RIVER) {
                    variant = LOTRBiomeVariant.RIVER;
                } else if (riverCode == LOTRGenLayerRiver.RANDOM_RIVER && biome.getEnableRiver() && !structureNear && !mountainNear) {
                    variant = LOTRBiomeVariant.RIVER;
                }
                variants[index] = variant == null ? LOTRBiomeVariant.STANDARD : variant;
            }
        }
        return variants;
    }

    private static boolean isMountainNear(int x, int z) {
        return LOTRFixedStructures.mountainNear(x, z);
    }

    private static boolean isStructureNear(int x, int z) {
        return LOTRFixedStructures.structureNear(x, z);
    }

    private static boolean anyFixedVillagesAt(LOTRBiome biome, int x, int z) {
        return biome.decorator.anyFixedVillagesAt(x, z);
    }
}
