package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** LOTRPacketAnvilRename: what the player has typed into the LOTR anvil's name box. */
public record LOTRAnvilRenamePayload(String name) implements CustomPacketPayload {
    /** The name box's setMaxStringLength(40). */
    public static final int MAX_LENGTH = 40;

    public static final CustomPacketPayload.Type<LOTRAnvilRenamePayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "anvil_rename"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRAnvilRenamePayload> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.stringUtf8(MAX_LENGTH), LOTRAnvilRenamePayload::name,
                    LOTRAnvilRenamePayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
