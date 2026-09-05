package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * What LOTRGuiHornSelect sent: which order the horn in hand should carry, and
 * which squadron it speaks to.
 *
 * <p>The original set the item damage straight from the GUI, which worked
 * because 1.7.10 ran the screen and the inventory in one place. Here the screen
 * is client-side and the stack it is editing lives on the server, so the choice
 * has to travel.
 */
public record LOTRHornModePayload(int mode, String squadron) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRHornModePayload> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "horn_mode"));

    public LOTRHornModePayload(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readUtf(LOTRCommandHornItem.SQUADRON_LENGTH_MAX));
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(mode);
        buf.writeUtf(squadron, LOTRCommandHornItem.SQUADRON_LENGTH_MAX);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRHornModePayload> STREAM_CODEC =
            CustomPacketPayload.codec(LOTRHornModePayload::write, LOTRHornModePayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
