package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * LOTRPacketBeaconEdit, minus the fellowship.
 *
 * The original carried x/y/z, a fellowship UUID, the beacon name and a
 * releasePlayer flag. There is no fellowship system in the port yet, so the
 * fellowship is carried as the typed NAME rather than a resolved UUID -- the
 * text is kept verbatim so that when fellowships land it can be looked up and
 * turned into a real assignment without anyone having to retype it. The
 * editing-player list is dropped; it existed only to gate fellowship edits.
 *
 * NOTE the type id is built by hand rather than with
 * CustomPacketPayload.createType(String) -- that helper calls
 * Identifier.withDefaultNamespace, which would register this as
 * "minecraft:beacon_edit".
 */
public record LOTRBeaconEditPayload(BlockPos pos, String fellowshipName, String beaconName)
        implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRBeaconEditPayload> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "beacon_edit"));

    /** LOTRGuiBeacon capped both text fields at 40 characters. */
    public static final int MAX_NAME_LENGTH = 40;

    public LOTRBeaconEditPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readUtf(MAX_NAME_LENGTH), buf.readUtf(MAX_NAME_LENGTH));
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(fellowshipName, MAX_NAME_LENGTH);
        buf.writeUtf(beaconName, MAX_NAME_LENGTH);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRBeaconEditPayload> STREAM_CODEC =
            CustomPacketPayload.codec(LOTRBeaconEditPayload::write, LOTRBeaconEditPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}