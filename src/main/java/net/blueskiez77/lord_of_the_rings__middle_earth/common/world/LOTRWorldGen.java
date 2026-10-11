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
 * Middle-earth's and Utumno's dimensions ({@code data/lotr/dimension/middle_earth.json}, {@code utumno.json}):
 * their chunk generator and biome source types, their biomes, and the world seed given to the
 * generators as the levels load.
 */
public final class LOTRWorldGen {

    public static final ResourceKey<Level> MIDDLE_EARTH = ResourceKey.create(Registries.DIMENSION,
            Identifier.fromNamespaceAndPath("lotr", "middle_earth"));
    public static final ResourceKey<Level> UTUMNO = ResourceKey.create(Registries.DIMENSION,
            Identifier.fromNamespaceAndPath("lotr", "utumno"));

    /**
     * {@code getTerrainType() == LOTRMod.worldTypeMiddleEarthClassic}: whether this world's Middle-earth
     * is "M.E. Classic" -- set on the server as Middle-earth loads, and on a client as it joins.
     */
    public static volatile boolean classic;

    /** The classic flag, for a client as it joins. */
    public record ClassicPayload(boolean classic) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
        public static final Type<ClassicPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("lotr", "classic_world"));
        public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ClassicPayload> STREAM_CODEC =
                net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.BOOL, ClassicPayload::classic,
                        ClassicPayload::new);

        @Override
        public Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
            return TYPE;
        }
    }

    private LOTRWorldGen() {
    }

    /** After LOTRLegacyBlocks.init: the biomes name the mod's blocks by their old fields. */
    public static void init() {
        LOTRBiomes.init();
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariantStorage.init();
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.legacy.LOTRLegacyStructure.init();
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawning.init();
        Registry.register(BuiltInRegistries.CHUNK_GENERATOR, Identifier.fromNamespaceAndPath("lotr", "middle_earth"), LOTRChunkGenerator.CODEC);
        Registry.register(BuiltInRegistries.BIOME_SOURCE, Identifier.fromNamespaceAndPath("lotr", "middle_earth"), LOTRBiomeSource.CODEC);
        Registry.register(BuiltInRegistries.CHUNK_GENERATOR, Identifier.fromNamespaceAndPath("lotr", "utumno"), LOTRUtumnoChunkGenerator.CODEC);
        LOTRWorldTypeMiddleEarth.init();
        net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDate.init();
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.clientboundPlay().register(ClassicPayload.TYPE, ClassicPayload.STREAM_CODEC);
        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(handler.player, new ClassicPayload(classic)));
        ServerLevelEvents.LOAD.register((server, level) -> {
            if (level.getChunkSource().getGenerator() instanceof LOTRChunkGenerator generator) {
                generator.init(level.getSeed());
            } else if (level.getChunkSource().getGenerator() instanceof LOTRUtumnoChunkGenerator generator) {
                generator.init(level.getSeed());
            }
        });
    }
}
