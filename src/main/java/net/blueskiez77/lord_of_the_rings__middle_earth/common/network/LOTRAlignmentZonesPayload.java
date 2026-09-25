package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** LOTRPacketEnableAlignmentZones: whether control zones are on. */
public record LOTRAlignmentZonesPayload(boolean enabled) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRAlignmentZonesPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "alignment_zones"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRAlignmentZonesPayload> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.BOOL, LOTRAlignmentZonesPayload::enabled, LOTRAlignmentZonesPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
