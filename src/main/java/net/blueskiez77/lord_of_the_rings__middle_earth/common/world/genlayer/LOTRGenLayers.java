package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

/**
 * LOTRGenLayerWorld.createWorld and LOTRWorldChunkManager.setupGenLayers: the layer stacks of a
 * dimension, for one world seed -- the biomes, the large and small variants, the lakes and the
 * rivers, each at four-block resolution (chunkGen) and, zoomed once more, at block resolution
 * (world).
 *
 * <p>A layer keeps its random state as it works, so the stacks are not to be shared between
 * threads: each world-generation thread asks for its own (LOTRChunkGenerator).
 */
public final class LOTRGenLayers {

    public static final int LAYER_BIOME = 0;
    public static final int LAYER_VARIANTS_LARGE = 1;
    public static final int LAYER_VARIANTS_SMALL = 2;
    public static final int LAYER_VARIANTS_LAKES = 3;
    public static final int LAYER_VARIANTS_RIVERS = 4;

    public final LOTRGenLayer[] chunkGenLayers;
    public final LOTRGenLayer[] worldLayers;

    public LOTRGenLayers(LOTRDimension dim, long worldSeed) {
        this.chunkGenLayers = createWorld(dim);
        this.worldLayers = new LOTRGenLayer[this.chunkGenLayers.length];
        for (int i = 0; i < this.worldLayers.length; ++i) {
            this.worldLayers[i] = new LOTRGenLayerZoomVoronoi(10L, this.chunkGenLayers[i]);
        }
        long seed = worldSeed + 1954L;
        for (int i = 0; i < this.worldLayers.length; ++i) {
            this.chunkGenLayers[i].initWorldGenSeed(seed);
            this.worldLayers[i].initWorldGenSeed(seed);
        }
    }

    private static LOTRGenLayer[] createWorld(LOTRDimension dim) {
        if (dim == LOTRDimension.UTUMNO) {
            throw new UnsupportedOperationException("Utumno waits on D15");
        }
        LOTRGenLayer rivers = new LOTRGenLayerRiverInit(100L);
        rivers = LOTRGenLayerZoom.magnify(1000L, rivers, 10);
        rivers = new LOTRGenLayerRiver(1L, rivers);
        rivers = new LOTRGenLayerSmooth(1000L, rivers);
        rivers = LOTRGenLayerZoom.magnify(1000L, rivers, 1);
        LOTRGenLayer biomeSubtypes = new LOTRGenLayerBiomeSubtypesInit(3000L);
        biomeSubtypes = LOTRGenLayerZoom.magnify(3000L, biomeSubtypes, 2);
        LOTRGenLayer biomes = new LOTRGenLayerWorld();
        LOTRGenLayer mapRivers = new LOTRGenLayerExtractMapRivers(5000L, biomes);
        biomes = new LOTRGenLayerRemoveMapRivers(1000L, biomes, dim);
        biomes = new LOTRGenLayerBiomeSubtypes(1000L, biomes, biomeSubtypes);
        biomes = new LOTRGenLayerNearHaradRiverbanks(200L, biomes, mapRivers, dim);
        biomes = new LOTRGenLayerNearHaradOasis(500L, biomes, dim);
        biomes = LOTRGenLayerZoom.magnify(1000L, biomes, 1);
        biomes = new LOTRGenLayerBeach(1000L, biomes, dim, LOTRBiomes.OCEAN);
        biomes = LOTRGenLayerZoom.magnify(1000L, biomes, 2);
        biomes = new LOTRGenLayerOasisLake(600L, biomes, dim);
        biomes = LOTRGenLayerZoom.magnify(1000L, biomes, 2);
        biomes = new LOTRGenLayerSmooth(1000L, biomes);
        LOTRGenLayer variants = new LOTRGenLayerBiomeVariants(200L);
        variants = LOTRGenLayerZoom.magnify(200L, variants, 8);
        LOTRGenLayer variantsSmall = new LOTRGenLayerBiomeVariants(300L);
        variantsSmall = LOTRGenLayerZoom.magnify(300L, variantsSmall, 6);
        LOTRGenLayer lakes = new LOTRGenLayerBiomeVariantsLake(100L, null, 0).setLakeFlags(LOTRGenLayerBiomeVariantsLake.FLAG_LAKE);
        for (int i = 1; i <= 5; ++i) {
            lakes = new LOTRGenLayerZoom(200L + i, lakes);
            if (i <= 2) {
                lakes = new LOTRGenLayerBiomeVariantsLake(300L + i, lakes, i).setLakeFlags(LOTRGenLayerBiomeVariantsLake.FLAG_LAKE);
            }
            if (i == 3) {
                lakes = new LOTRGenLayerBiomeVariantsLake(500L, lakes, i)
                        .setLakeFlags(LOTRGenLayerBiomeVariantsLake.FLAG_JUNGLE, LOTRGenLayerBiomeVariantsLake.FLAG_MANGROVE);
            }
        }
        for (int i = 0; i < 4; ++i) {
            mapRivers = new LOTRGenLayerMapRiverZoom(4000L + i, mapRivers);
        }
        mapRivers = new LOTRGenLayerNarrowRivers(3000L, mapRivers, 6);
        mapRivers = LOTRGenLayerZoom.magnify(4000L, mapRivers, 1);
        rivers = new LOTRGenLayerIncludeMapRivers(5000L, rivers, mapRivers);
        return new LOTRGenLayer[]{biomes, variants, variantsSmall, lakes, rivers};
    }
}
