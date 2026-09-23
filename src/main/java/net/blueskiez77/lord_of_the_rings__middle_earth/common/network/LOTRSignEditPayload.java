package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRCarvedSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** LOTRPacketEditSign: the finished lines, sent when the lettering screen closes. */
public record LOTRSignEditPayload(BlockPos pos, List<String> lines) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LOTRSignEditPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "edit_sign"));

    public static final StreamCodec<ByteBuf, LOTRSignEditPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, LOTRSignEditPayload::pos,
            ByteBufCodecs.stringUtf8(256).apply(ByteBufCodecs.list(LOTRCarvedSignBlockEntity.NUM_LINES)),
            LOTRSignEditPayload::lines,
            LOTRSignEditPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
