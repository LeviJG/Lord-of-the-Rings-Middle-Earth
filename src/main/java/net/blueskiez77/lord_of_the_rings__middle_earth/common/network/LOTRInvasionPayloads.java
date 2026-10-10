package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** The invasions' packets. */
public final class LOTRInvasionPayloads {

    private LOTRInvasionPayloads() {
    }

    /** LOTRPacketInvasionWatch: show this invasion's bar -- in place of one already shown, if so told. */
    public record Watch(int invasionEntityID, boolean overrideAlreadyWatched) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Watch> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "invasion_watch"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Watch> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Watch::invasionEntityID, ByteBufCodecs.BOOL, Watch::overrideAlreadyWatched, Watch::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
