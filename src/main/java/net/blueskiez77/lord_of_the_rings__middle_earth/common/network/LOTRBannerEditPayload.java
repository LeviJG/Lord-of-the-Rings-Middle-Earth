package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import java.util.List;
import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * LOTRPacketEditBanner: the banner screen's settings back to the server --
 * the mode, self-protection, the alignment asked, the whitelist's length and,
 * when the screen closes, its names and permissions (the owner's line never).
 */
public record LOTRBannerEditPayload(BlockPos pos, boolean playerSpecificProtection, boolean selfProtection,
                                    float alignmentProtection, int whitelistLength,
                                    Optional<List<LOTRBannerDataPayload.Slot>> whitelist, int defaultPerms)
        implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRBannerEditPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "banner_edit"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRBannerEditPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                buf.writeBlockPos(p.pos);
                buf.writeBoolean(p.playerSpecificProtection);
                buf.writeBoolean(p.selfProtection);
                buf.writeFloat(p.alignmentProtection);
                buf.writeVarInt(p.whitelistLength);
                ByteBufCodecs.optional(LOTRBannerDataPayload.Slot.STREAM_CODEC.apply(ByteBufCodecs.list(4000)))
                        .encode(buf, p.whitelist);
                buf.writeShort(p.defaultPerms);
            }, buf -> new LOTRBannerEditPayload(buf.readBlockPos(), buf.readBoolean(), buf.readBoolean(),
                    buf.readFloat(), buf.readVarInt(),
                    ByteBufCodecs.optional(LOTRBannerDataPayload.Slot.STREAM_CODEC.apply(ByteBufCodecs.list(4000)))
                            .decode(buf), buf.readShort()));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
