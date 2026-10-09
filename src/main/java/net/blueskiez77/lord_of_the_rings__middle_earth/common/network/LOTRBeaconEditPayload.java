package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * LOTRPacketBeaconEdit: the beacon's fellowship (one of the sender's, by its
 * id, or none) and its name, sent as the dialog closes (releasePlayer).
 *
 * NOTE the type id is built by hand rather than with
 * CustomPacketPayload.createType(String) -- that helper calls
 * Identifier.withDefaultNamespace, which would register this as
 * "minecraft:beacon_edit".
 */
public record LOTRBeaconEditPayload(BlockPos pos, java.util.Optional<java.util.UUID> fellowshipID, String beaconName)
        implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRBeaconEditPayload> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "beacon_edit"));

    /** LOTRGuiBeacon capped both text fields at 40 characters. */
    public static final int MAX_NAME_LENGTH = 40;

    public LOTRBeaconEditPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readOptional(b -> b.readUUID()), buf.readUtf(MAX_NAME_LENGTH));
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeOptional(fellowshipID, (b, id) -> b.writeUUID(id));
        buf.writeUtf(beaconName, MAX_NAME_LENGTH);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRBeaconEditPayload> STREAM_CODEC =
            CustomPacketPayload.codec(LOTRBeaconEditPayload::write, LOTRBeaconEditPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}