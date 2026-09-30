package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** LOTRPacketMallornEntSummon: the Mallorn Ent has called up a tree, for the arc of leaves between them. */
public record LOTRMallornEntSummonPayload(int entityId, int summonedId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRMallornEntSummonPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "mallorn_ent_summon"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRMallornEntSummonPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                buf.writeVarInt(p.entityId);
                buf.writeVarInt(p.summonedId);
            }, buf -> new LOTRMallornEntSummonPayload(buf.readVarInt(), buf.readVarInt()));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
