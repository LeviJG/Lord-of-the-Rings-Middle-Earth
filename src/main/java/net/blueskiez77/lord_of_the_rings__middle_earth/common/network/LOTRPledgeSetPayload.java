package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

/** LOTRPacketPledgeSet: the faction screen's pledge button, or its unpledge (null). */
public record LOTRPledgeSetPayload(@Nullable LOTRFaction faction) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRPledgeSetPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "pledge_set"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRPledgeSetPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> buf.writeUtf(p.faction == null ? "" : p.faction.codeName()),
                    buf -> new LOTRPledgeSetPayload(LOTRFaction.forName(buf.readUtf())));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
