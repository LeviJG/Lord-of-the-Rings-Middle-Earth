package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import io.netty.buffer.ByteBuf;
import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** LOTRPacketOpenSignEditor: the server tells the carver to open the lettering screen. */
public record LOTROpenSignEditorPayload(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LOTROpenSignEditorPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "open_sign_editor"));

    public static final StreamCodec<ByteBuf, LOTROpenSignEditorPayload> STREAM_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, LOTROpenSignEditorPayload::pos, LOTROpenSignEditorPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
