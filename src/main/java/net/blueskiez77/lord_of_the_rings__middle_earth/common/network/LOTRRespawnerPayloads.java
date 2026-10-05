package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** The NPC respawner screen's packets. */
public final class LOTRRespawnerPayloads {

    private LOTRRespawnerPayloads() {
    }

    /** LOTRPacketNPCRespawner: the respawner's settings, to open its screen on. */
    public record Open(int entityId, CompoundTag data) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Open> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "npc_respawner"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Open> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Open::entityId, ByteBufCodecs.COMPOUND_TAG, Open::data, Open::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketEditNPCRespawner: the settings as left, and whether to destroy it. */
    public record Edit(int entityId, CompoundTag data, boolean destroy) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Edit> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "edit_npc_respawner"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Edit> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Edit::entityId, ByteBufCodecs.COMPOUND_TAG, Edit::data, ByteBufCodecs.BOOL,
                Edit::destroy, Edit::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
