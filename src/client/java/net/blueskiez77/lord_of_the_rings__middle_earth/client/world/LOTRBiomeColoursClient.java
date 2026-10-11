package net.blueskiez77.lord_of_the_rings__middle_earth.client.world;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariantStorage;
import net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client.LOTRClientLevelAccessor;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;

import org.jspecify.annotations.Nullable;

/**
 * Middle-earth's grass, leaves and water, block by block: the grass and leaves of the colour map at
 * the biome's climate raised by the variant there, or the colour the biome set (LOTRBiome
 * getBiomeGrassColor, getBiomeFoliageColor); and the water of every biome without its own colour
 * shading from the cold blue of the north to the warm blue of the south by how far south the viewer
 * stands (LOTRBiome.updateWaterColor, each tick).
 */
public final class LOTRBiomeColoursClient {

    private static final int WATER_COLOR_NORTH = 602979;
    private static final int WATER_COLOR_SOUTH = 4973293;
    private static final int WATER_LIMIT_NORTH = -40000;
    private static final int WATER_LIMIT_SOUTH = 160000;

    private static int waterColor = latitudeWaterColor(0);

    private LOTRBiomeColoursClient() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(LOTRBiomeColoursClient::tick);
    }

    private static void tick(Minecraft mc) {
        ClientLevel level = mc.level;
        Entity viewer = mc.getCameraEntity();
        if (level == null || viewer == null || !LOTREnvironmentClient.isMiddleEarth(level)) {
            return;
        }
        int color = latitudeWaterColor(Mth.floor(viewer.getZ()));
        if (color != waterColor) {
            waterColor = color;
            // Chunks drawn from now on take the new colour, as the original's did when next rebuilt.
            BlockTintCache cache = ((LOTRClientLevelAccessor) level).lotr$tintCaches().get(BiomeColors.WATER_COLOR_RESOLVER);
            if (cache != null) {
                cache.invalidateAll();
            }
        }
    }

    /** updateWaterColor: the colour at a latitude, as java.awt.Color rounded it. */
    private static int latitudeWaterColor(int k) {
        int max = WATER_LIMIT_SOUTH - WATER_LIMIT_NORTH;
        float latitude = (float) Mth.clamp(k - WATER_LIMIT_NORTH, 0, max) / max;
        int r = channel(WATER_COLOR_NORTH >> 16 & 0xFF, WATER_COLOR_SOUTH >> 16 & 0xFF, latitude);
        int g = channel(WATER_COLOR_NORTH >> 8 & 0xFF, WATER_COLOR_SOUTH >> 8 & 0xFF, latitude);
        int b = channel(WATER_COLOR_NORTH & 0xFF, WATER_COLOR_SOUTH & 0xFF, latitude);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    private static int channel(int north, int south, float latitude) {
        float n = north / 255.0f;
        float s = south / 255.0f;
        return (int) ((n + (s - n) * latitude) * 255.0f + 0.5f);
    }

    /** A block's tint by the resolver, for a Middle-earth biome; null to leave it to vanilla. */
    public static @Nullable Integer resolve(ClientLevel level, ColorResolver resolver, Biome biome, double x, double z) {
        if (!LOTREnvironmentClient.isMiddleEarth(level)) {
            return null;
        }
        ResourceKey<Biome> key = level.registryAccess().lookupOrThrow(Registries.BIOME).getResourceKey(biome).orElse(null);
        LOTRBiome lotrBiome = key == null ? null : LOTRBiomes.byKey(key);
        if (lotrBiome == null) {
            return null;
        }
        int i = Mth.floor(x);
        int k = Mth.floor(z);
        if (resolver == BiomeColors.GRASS_COLOR_RESOLVER) {
            LOTRBiomeVariant variant = LOTRBiomeVariantStorage.getBiomeVariantAt(level, i, k);
            return lotrBiome.getBiomeGrassColor(i, k, variant);
        }
        if (resolver == BiomeColors.FOLIAGE_COLOR_RESOLVER) {
            LOTRBiomeVariant variant = LOTRBiomeVariantStorage.getBiomeVariantAt(level, i, k);
            return lotrBiome.getBiomeFoliageColor(i, k, variant);
        }
        if (resolver == BiomeColors.WATER_COLOR_RESOLVER && !lotrBiome.biomeColors.hasCustomWater) {
            return waterColor;
        }
        return null;
    }
}
