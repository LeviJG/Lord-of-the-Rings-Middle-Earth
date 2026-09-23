package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * LOTRPacketBrewingButton: the barrel screen's Start/Stop Brewing button. It
 * carries nothing -- the server acts on whatever barrel the player has open.
 */
public record LOTRBrewingButtonPayload() implements CustomPacketPayload {
    public static final LOTRBrewingButtonPayload INSTANCE = new LOTRBrewingButtonPayload();

    public static final CustomPacketPayload.Type<LOTRBrewingButtonPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "brewing_button"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRBrewingButtonPayload> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
