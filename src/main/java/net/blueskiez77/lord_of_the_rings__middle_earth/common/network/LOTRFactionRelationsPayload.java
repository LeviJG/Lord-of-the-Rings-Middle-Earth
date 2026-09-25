package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import java.util.HashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRelations;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * LOTRPacketFactionRelations: the overridden relations. The original sent one
 * entry per change or a reset; the port always sends the whole (small) map.
 */
public record LOTRFactionRelationsPayload(
        Map<LOTRFactionRelations.FactionPair, LOTRFactionRelations.Relation> overrides) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRFactionRelationsPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "faction_relations"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRFactionRelationsPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                buf.writeVarInt(p.overrides.size());
                p.overrides.forEach((pair, rel) -> {
                    buf.writeUtf(pair.getLeft().codeName());
                    buf.writeUtf(pair.getRight().codeName());
                    buf.writeUtf(rel.codeName());
                });
            }, buf -> {
                Map<LOTRFactionRelations.FactionPair, LOTRFactionRelations.Relation> map = new HashMap<>();
                int n = buf.readVarInt();
                for (int i = 0; i < n; i++) {
                    LOTRFaction f1 = LOTRFaction.forName(buf.readUtf());
                    LOTRFaction f2 = LOTRFaction.forName(buf.readUtf());
                    LOTRFactionRelations.Relation rel = LOTRFactionRelations.Relation.forName(buf.readUtf());
                    if (f1 != null && f2 != null && rel != null) {
                        map.put(new LOTRFactionRelations.FactionPair(f1, f2), rel);
                    }
                }
                return new LOTRFactionRelationsPayload(map);
            });

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
