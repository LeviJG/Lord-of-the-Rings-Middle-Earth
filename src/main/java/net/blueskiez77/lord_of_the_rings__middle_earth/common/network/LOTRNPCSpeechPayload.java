package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** LOTRPacketNPCSpeech: a line an NPC says to this player, and whether it must go to chat too. */
public record LOTRNPCSpeechPayload(int entityId, String speech, boolean forceChatMsg) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRNPCSpeechPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "npc_speech"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRNPCSpeechPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                buf.writeVarInt(p.entityId);
                buf.writeUtf(p.speech);
                buf.writeBoolean(p.forceChatMsg);
            }, buf -> new LOTRNPCSpeechPayload(buf.readVarInt(), buf.readUtf(), buf.readBoolean()));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
