package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

/**
 * LOTRGenLayerBiomeSubtypes: patches of one biome within another, where the subtype layer falls
 * low -- the Shire's woodlands, Forodwaith's glaciers, Far Harad's forests, the Tauredain's
 * clearings, Pertorogwaith's volcanoes and the ocean's islands.
 */
public class LOTRGenLayerBiomeSubtypes extends LOTRGenLayer {

    private final LOTRGenLayer biomeLayer;
    private final LOTRGenLayer variantsLayer;

    public LOTRGenLayerBiomeSubtypes(long seed, LOTRGenLayer biomes, LOTRGenLayer subtypes) {
        super(seed);
        this.biomeLayer = biomes;
        this.variantsLayer = subtypes;
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int[] biomes = this.biomeLayer.getInts(i, k, xSize, zSize);
        int[] variants = this.variantsLayer.getInts(i, k, xSize, zSize);
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                initChunkSeed(i + i1, k + k1);
                int biome = biomes[i1 + k1 * xSize];
                int variant = variants[i1 + k1 * xSize];
                int newBiome = biome;
                if (biome == LOTRBiomes.SHIRE.biomeID && variant < 15 && variant != 0) {
                    newBiome = LOTRBiomes.SHIRE_WOODLANDS.biomeID;
                } else if (biome == LOTRBiomes.FORODWAITH_MOUNTAINS.biomeID && variant < 5) {
                    newBiome = LOTRBiomes.FORODWAITH_GLACIER.biomeID;
                } else if (biome == LOTRBiomes.FAR_HARAD.biomeID && variant < 20) {
                    newBiome = LOTRBiomes.FAR_HARAD_FOREST.biomeID;
                } else if (biome == LOTRBiomes.FAR_HARAD_JUNGLE.biomeID && variant < 15) {
                    newBiome = LOTRBiomes.TAUREDAIN_CLEARING.biomeID;
                } else if (biome == LOTRBiomes.PERTOROGWAITH.biomeID && variant < 15) {
                    newBiome = LOTRBiomes.FAR_HARAD_VOLCANO.biomeID;
                } else if (biome == LOTRBiomes.OCEAN.biomeID && variant < 2) {
                    newBiome = LOTRBiomes.ISLAND.biomeID;
                }
                ints[i1 + k1 * xSize] = newBiome;
            }
        }
        return ints;
    }

    @Override
    public void initWorldGenSeed(long seed) {
        this.biomeLayer.initWorldGenSeed(seed);
        this.variantsLayer.initWorldGenSeed(seed);
        super.initWorldGenSeed(seed);
    }
}
