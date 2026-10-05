package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentBonusMap;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** The alignment HUD's packets. */
public final class LOTRAlignmentHudPayloads {

    private LOTRAlignmentHudPayloads() {
    }

    /** LOTRPacketAlignDrain: how many factions just drained, for the icon beside the bar. */
    public record AlignDrain(int numFactions) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<AlignDrain> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "align_drain"));
        public static final StreamCodec<RegistryFriendlyByteBuf, AlignDrain> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, AlignDrain::numFactions, AlignDrain::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /**
     * LOTRPacketEnvironmentOverlay: frost or burning at the screen's edges
     * (LOTRDamage.doFrostDamage / doBurnDamage). 0 frost, 1 burn.
     */
    public record EnvironmentOverlay(int overlay) implements CustomPacketPayload {
        public static final int FROST = 0;
        public static final int BURN = 1;
        public static final CustomPacketPayload.Type<EnvironmentOverlay> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "environment_overlay"));
        public static final StreamCodec<RegistryFriendlyByteBuf, EnvironmentOverlay> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, EnvironmentOverlay::overlay, EnvironmentOverlay::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketAlignmentBonus: an alignment change, to float up where it was earned. */
    public record AlignmentBonus(LOTRFaction mainFaction, float prevMainAlignment, Map<LOTRFaction, Float> factionBonusMap,
                                 float conquestBonus, double posX, double posY, double posZ, String name,
                                 boolean needsTranslation, boolean isKill, boolean isHiredKill) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<AlignmentBonus> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "alignment_bonus"));
        public static final StreamCodec<RegistryFriendlyByteBuf, AlignmentBonus> STREAM_CODEC = StreamCodec.of(
                (buf, p) -> {
                    buf.writeUtf(p.mainFaction.codeName());
                    buf.writeFloat(p.prevMainAlignment);
                    buf.writeVarInt(p.factionBonusMap.size());
                    p.factionBonusMap.forEach((fac, bonus) -> {
                        buf.writeUtf(fac.codeName());
                        buf.writeFloat(bonus);
                    });
                    buf.writeFloat(p.conquestBonus);
                    buf.writeDouble(p.posX);
                    buf.writeDouble(p.posY);
                    buf.writeDouble(p.posZ);
                    buf.writeUtf(p.name);
                    buf.writeBoolean(p.needsTranslation);
                    buf.writeBoolean(p.isKill);
                    buf.writeBoolean(p.isHiredKill);
                },
                buf -> {
                    LOTRFaction main = LOTRFaction.forName(buf.readUtf());
                    float prev = buf.readFloat();
                    int n = buf.readVarInt();
                    LOTRAlignmentBonusMap map = new LOTRAlignmentBonusMap();
                    for (int i = 0; i < n; ++i) {
                        LOTRFaction fac = LOTRFaction.forName(buf.readUtf());
                        float bonus = buf.readFloat();
                        if (fac != null) {
                            map.put(fac, bonus);
                        }
                    }
                    return new AlignmentBonus(main, prev, map, buf.readFloat(), buf.readDouble(), buf.readDouble(),
                            buf.readDouble(), buf.readUtf(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
                });

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
