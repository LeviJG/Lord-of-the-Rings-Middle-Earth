package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

/** LOTRPacketBrokenPledge: the player's broken-pledge cooldown, for the faction screen. */
public record LOTRBrokenPledgePayload(int cooldown, int cooldownStart, @Nullable LOTRFaction brokenFaction)
        implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRBrokenPledgePayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "broken_pledge"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRBrokenPledgePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                buf.writeVarInt(p.cooldown);
                buf.writeVarInt(p.cooldownStart);
                buf.writeUtf(p.brokenFaction == null ? "" : p.brokenFaction.codeName());
            }, buf -> new LOTRBrokenPledgePayload(buf.readVarInt(), buf.readVarInt(),
                    LOTRFaction.forName(buf.readUtf())));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
