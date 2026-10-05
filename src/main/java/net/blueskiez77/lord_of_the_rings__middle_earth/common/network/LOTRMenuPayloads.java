package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRViewingFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import java.util.Map;
import java.util.HashMap;
import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** The LOTR menu's packets. */
public final class LOTRMenuPayloads {

    private LOTRMenuPayloads() {
    }

    /** LOTRPacketSetOption: flip one of the player's options (the Options screen's button ids). */
    public record SetOption(int option) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SetOption> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "set_option"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SetOption> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, SetOption::option, SetOption::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /**
     * LOTRPacketClientInfo: the faction the player now views, and the faction
     * last viewed in each region left.
     *
     * <p>NOT ported yet: the map's waypoint toggles it also carried (D13).
     */
    public record ClientInfo(LOTRFaction viewingFaction, Map<LOTRDimension.DimensionRegion, LOTRFaction> changedRegionMap)
            implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ClientInfo> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "client_info"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ClientInfo> STREAM_CODEC = StreamCodec.composite(
                LOTRViewingFaction.FACTION_STREAM_CODEC, ClientInfo::viewingFaction,
                ByteBufCodecs.map(HashMap::new, LOTRViewingFaction.REGION_STREAM_CODEC, LOTRViewingFaction.FACTION_STREAM_CODEC),
                c -> new HashMap<>(c.changedRegionMap()), ClientInfo::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketAlignmentSee: another player's alignments, for the factions screen. */
    public record AlignmentSee(String username, Map<LOTRFaction, Float> alignments) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<AlignmentSee> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "alignment_see"));
        public static final StreamCodec<RegistryFriendlyByteBuf, AlignmentSee> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, AlignmentSee::username,
                ByteBufCodecs.map(HashMap::new, LOTRViewingFaction.FACTION_STREAM_CODEC, ByteBufCodecs.FLOAT),
                a -> new HashMap<>(a.alignments()), AlignmentSee::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketMessage: show a one-time message (a LOTRGuiMessageTypes ordinal). */
    public record Message(int message) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Message> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "gui_message"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Message> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Message::message, Message::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
