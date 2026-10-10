package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

import java.nio.ByteBuffer;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkGenerator;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * LOTRBiomeVariantStorage: each Middle-earth chunk's biome variants, a byte a column at
 * {@code x + z * 16} -- kept with the chunk ("LOTRBiomeVariants" in the original's chunk data) and
 * sent to the players who can see it, whose grass and leaf colours the variants tint
 * (LOTRPacketBiomeVariantsWatch).
 */
public final class LOTRBiomeVariantStorage {

    public static final AttachmentType<byte[]> VARIANTS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "biome_variants"),
            builder -> builder.persistent(Codec.BYTE_BUFFER.xmap(LOTRBiomeVariantStorage::toArray, ByteBuffer::wrap))
                    .syncWith(ByteBufCodecs.BYTE_ARRAY, AttachmentSyncPredicate.all()));

    private LOTRBiomeVariantStorage() {
    }

    public static void init() {
    }

    private static byte[] toArray(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.duplicate().get(bytes);
        return bytes;
    }

    public static void setChunkBiomeVariants(ChunkAccess chunk, LOTRBiomeVariant[] variants) {
        byte[] bytes = new byte[256];
        for (int l = 0; l < bytes.length; ++l) {
            bytes[l] = (byte) variants[l].variantID;
        }
        chunk.setAttached(VARIANTS, bytes);
    }

    /**
     * LOTRWorldChunkManager.getBiomeVariantAt: the variant at a block -- from its chunk when that
     * is loaded, else, on the server, worked out from the layers; the standard one otherwise.
     */
    public static LOTRBiomeVariant getBiomeVariantAt(Level level, int x, int z) {
        if (level.hasChunk(x >> 4, z >> 4)) {
            LevelChunk chunk = level.getChunk(x >> 4, z >> 4);
            byte[] variants = chunk.getAttached(VARIANTS);
            if (variants != null && variants.length == 256) {
                return LOTRBiomeVariant.getVariantForID(variants[(x & 15) + (z & 15) * 16]);
            }
        }
        if (level instanceof ServerLevel server && server.getChunkSource().getGenerator() instanceof LOTRChunkGenerator generator) {
            return generator.world().chunkManager.getBiomeVariants(x, z, 1, 1)[0];
        }
        return LOTRBiomeVariant.STANDARD;
    }
}
