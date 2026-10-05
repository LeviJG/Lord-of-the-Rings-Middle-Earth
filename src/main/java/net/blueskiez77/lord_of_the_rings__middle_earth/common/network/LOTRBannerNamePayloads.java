package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * LOTRPacketBannerRequestInvalidName and LOTRPacketBannerValidate: the banner
 * screen asks whether a name typed into a whitelist slot is a player the
 * server knows (or a fellowship of the owner's), and hears back.
 */
public final class LOTRBannerNamePayloads {

    private LOTRBannerNamePayloads() {
    }

    public record Request(BlockPos pos, int slot, String username) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Request> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "banner_name_request"));

        public static final StreamCodec<RegistryFriendlyByteBuf, Request> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, Request::pos, ByteBufCodecs.VAR_INT, Request::slot,
                ByteBufCodecs.stringUtf8(64), Request::username, Request::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record Validate(BlockPos pos, int slot, String prevText, boolean valid) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Validate> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "banner_name_validate"));

        public static final StreamCodec<RegistryFriendlyByteBuf, Validate> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, Validate::pos, ByteBufCodecs.VAR_INT, Validate::slot,
                ByteBufCodecs.stringUtf8(64), Validate::prevText, ByteBufCodecs.BOOL, Validate::valid, Validate::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
