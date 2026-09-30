package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** LOTRPacketMallornEntHeal: one of the Mallorn Ent's leaf healings, started or ended. */
public record LOTRMallornEntHealPayload(int entityId, int slot, boolean active, BlockPos leaf, int healTime)
        implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRMallornEntHealPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "mallorn_ent_heal"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRMallornEntHealPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                buf.writeVarInt(p.entityId);
                buf.writeByte(p.slot);
                buf.writeBoolean(p.active);
                buf.writeBlockPos(p.leaf);
                buf.writeShort(p.healTime);
            }, buf -> new LOTRMallornEntHealPayload(buf.readVarInt(), buf.readByte(), buf.readBoolean(),
                    buf.readBlockPos(), buf.readShort()));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
