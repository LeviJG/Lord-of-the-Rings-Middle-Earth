package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * LOTRPacketBannerData: a banner's protection settings for one player -- the
 * whitelist names and their permissions (all of them for the owner's screen,
 * else only the owner's line), the default permissions, and whether this
 * player may do as they like in its land -- and whether to open the screen.
 */
public record LOTRBannerDataPayload(BlockPos pos, boolean openGui, boolean playerSpecificProtection,
                                    boolean selfProtection, boolean structureProtection, int customRange,
                                    float alignmentProtection, int whitelistLength, List<Slot> slots,
                                    int defaultPerms, boolean thisPlayerHasPermission) implements CustomPacketPayload {

    /** One named line of the whitelist. */
    public record Slot(int index, String name, int perms) {
        static final StreamCodec<RegistryFriendlyByteBuf, Slot> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Slot::index, ByteBufCodecs.stringUtf8(64), Slot::name,
                ByteBufCodecs.VAR_INT, Slot::perms, Slot::new);
    }

    public static final CustomPacketPayload.Type<LOTRBannerDataPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "banner_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRBannerDataPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                buf.writeBlockPos(p.pos);
                buf.writeBoolean(p.openGui);
                buf.writeBoolean(p.playerSpecificProtection);
                buf.writeBoolean(p.selfProtection);
                buf.writeBoolean(p.structureProtection);
                buf.writeShort(p.customRange);
                buf.writeFloat(p.alignmentProtection);
                buf.writeVarInt(p.whitelistLength);
                Slot.STREAM_CODEC.apply(ByteBufCodecs.list(4000)).encode(buf, p.slots);
                buf.writeShort(p.defaultPerms);
                buf.writeBoolean(p.thisPlayerHasPermission);
            }, buf -> new LOTRBannerDataPayload(buf.readBlockPos(), buf.readBoolean(), buf.readBoolean(),
                    buf.readBoolean(), buf.readBoolean(), buf.readShort(), buf.readFloat(), buf.readVarInt(),
                    Slot.STREAM_CODEC.apply(ByteBufCodecs.list(4000)).decode(buf), buf.readShort(),
                    buf.readBoolean()));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
