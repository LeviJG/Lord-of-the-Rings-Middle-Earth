package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/**
 * Middle-earth's dimension ({@code data/lotr/dimension/middle_earth.json}): its chunk generator and
 * biome source types, its biomes, and the world seed given to the generator as the level loads.
 */
public final class LOTRWorldGen {

    public static final ResourceKey<Level> MIDDLE_EARTH = ResourceKey.create(Registries.DIMENSION,
            Identifier.fromNamespaceAndPath("lotr", "middle_earth"));

    private LOTRWorldGen() {
    }

    /** After LOTRLegacyBlocks.init: the biomes name the mod's blocks by their old fields. */
    public static void init() {
        LOTRBiomes.init();
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariantStorage.init();
        LOTRChunkPopulator.init();
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawning.init();
        Registry.register(BuiltInRegistries.CHUNK_GENERATOR, Identifier.fromNamespaceAndPath("lotr", "middle_earth"), LOTRChunkGenerator.CODEC);
        Registry.register(BuiltInRegistries.BIOME_SOURCE, Identifier.fromNamespaceAndPath("lotr", "middle_earth"), LOTRBiomeSource.CODEC);
        ServerLevelEvents.LOAD.register((server, level) -> {
            if (level.getChunkSource().getGenerator() instanceof LOTRChunkGenerator generator) {
                generator.init(level.getSeed());
            }
        });
    }
}
