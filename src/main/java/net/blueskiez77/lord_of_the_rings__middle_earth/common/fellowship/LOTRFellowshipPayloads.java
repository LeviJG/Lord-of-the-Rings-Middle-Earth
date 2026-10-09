package net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRPlayerTitles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRTitle;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/**
 * The fellowship packets. A fellowship is sent whole to each of its members as it changes (in place
 * of the original's LOTRPacketFellowship and its partial updates), and to those invited to it.
 */
public final class LOTRFellowshipPayloads {

    private LOTRFellowshipPayloads() {
    }

    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> payloadType(String name) {
        return new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
    }

    /**
     * LOTRFellowshipClient: a fellowship as one player sees it -- whether they own it or are one of its
     * admins, and its players' names and titles.
     */
    public record View(UUID fellowshipID, String name, ItemStack icon, boolean owned, boolean adminned, UUID owner,
                       List<UUID> members, Map<UUID, String> usernames, Map<UUID, LOTRTitle.PlayerTitle> titles,
                       Set<UUID> admins, boolean preventPVP, boolean preventHiredFF, boolean showMapLocations) {

        public static final StreamCodec<RegistryFriendlyByteBuf, View> STREAM_CODEC = StreamCodec.of((buf, v) -> {
            UUIDUtil.STREAM_CODEC.encode(buf, v.fellowshipID);
            ByteBufCodecs.STRING_UTF8.encode(buf, v.name);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, v.icon);
            buf.writeBoolean(v.owned);
            buf.writeBoolean(v.adminned);
            UUIDUtil.STREAM_CODEC.encode(buf, v.owner);
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, v.members);
            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.STRING_UTF8)
                    .encode(buf, new HashMap<>(v.usernames));
            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, LOTRPlayerTitles.STREAM_CODEC)
                    .encode(buf, new HashMap<>(v.titles));
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, List.copyOf(v.admins));
            buf.writeBoolean(v.preventPVP);
            buf.writeBoolean(v.preventHiredFF);
            buf.writeBoolean(v.showMapLocations);
        }, buf -> new View(UUIDUtil.STREAM_CODEC.decode(buf), ByteBufCodecs.STRING_UTF8.decode(buf),
                ItemStack.OPTIONAL_STREAM_CODEC.decode(buf), buf.readBoolean(), buf.readBoolean(),
                UUIDUtil.STREAM_CODEC.decode(buf), UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf),
                ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.STRING_UTF8).decode(buf),
                ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, LOTRPlayerTitles.STREAM_CODEC).decode(buf),
                new HashSet<>(UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf)),
                buf.readBoolean(), buf.readBoolean(), buf.readBoolean()));

        public boolean containsPlayer(UUID player) {
            return this.owner.equals(player) || this.members.contains(player);
        }

        public boolean containsPlayerUsername(String username) {
            return this.usernames.containsValue(username);
        }

        public int getPlayerCount() {
            return this.members.size() + 1;
        }

        public List<UUID> getAllPlayerUuids() {
            List<UUID> all = new java.util.ArrayList<>();
            all.add(this.owner);
            all.addAll(this.members);
            return all;
        }

        public String getUsernameFor(UUID player) {
            return this.usernames.getOrDefault(player, "?");
        }

        public LOTRTitle.@org.jspecify.annotations.Nullable PlayerTitle getTitleFor(UUID player) {
            return this.titles.get(player);
        }

        public boolean isAdmin(UUID player) {
            return this.admins.contains(player);
        }
    }

    /** LOTRPacketFellowship: a fellowship (or an invitation to one) as the player now sees it. */
    public record Sync(View view, boolean isInvite) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Sync> TYPE = payloadType("fellowship");
        public static final StreamCodec<RegistryFriendlyByteBuf, Sync> STREAM_CODEC = StreamCodec.composite(
                View.STREAM_CODEC, Sync::view, ByteBufCodecs.BOOL, Sync::isInvite, Sync::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFellowshipRemove: the player is no longer in (or invited to) a fellowship. */
    public record Remove(UUID fellowshipID, boolean isInvite) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Remove> TYPE = payloadType("fellowship_remove");
        public static final StreamCodec<RegistryFriendlyByteBuf, Remove> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, Remove::fellowshipID, ByteBufCodecs.BOOL, Remove::isInvite, Remove::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public enum AcceptInviteResult {
        JOINED, DISBANDED, TOO_LARGE, NONEXISTENT
    }

    /** LOTRPacketFellowshipAcceptInviteResult. */
    public record AcceptResult(UUID fellowshipID, String name, AcceptInviteResult result) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<AcceptResult> TYPE = payloadType("fellowship_accept_result");
        public static final StreamCodec<RegistryFriendlyByteBuf, AcceptResult> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, AcceptResult::fellowshipID, ByteBufCodecs.STRING_UTF8, AcceptResult::name,
                ByteBufCodecs.idMapper(i -> AcceptInviteResult.values()[i], AcceptInviteResult::ordinal),
                AcceptResult::result, AcceptResult::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFellowshipNotification: the message to pop up in the corner as well as in chat. */
    public record Notification(Component message) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Notification> TYPE = payloadType("fellowship_notification");
        public static final StreamCodec<RegistryFriendlyByteBuf, Notification> STREAM_CODEC = StreamCodec.composite(
                ComponentSerialization.STREAM_CODEC, Notification::message, Notification::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFellowshipCreate. */
    public record Create(String name) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Create> TYPE = payloadType("fellowship_create");
        public static final StreamCodec<RegistryFriendlyByteBuf, Create> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.stringUtf8(40), Create::name, Create::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** What LOTRPacketFellowshipDisband, Leave, SetIcon and Toggle did. */
    public enum Action {
        DISBAND, LEAVE, SET_ICON, TOGGLE_PVP, TOGGLE_HIRED_FF, TOGGLE_MAP_SHOW
    }

    public record Do(UUID fellowshipID, Action action) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Do> TYPE = payloadType("fellowship_do");
        public static final StreamCodec<RegistryFriendlyByteBuf, Do> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, Do::fellowshipID,
                ByteBufCodecs.idMapper(i -> Action.values()[i], Action::ordinal), Do::action, Do::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFellowshipDoPlayer's functions. */
    public enum PlayerFunction {
        REMOVE, TRANSFER, OP, DEOP
    }

    public record DoPlayer(UUID fellowshipID, UUID subject, PlayerFunction function) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<DoPlayer> TYPE = payloadType("fellowship_do_player");
        public static final StreamCodec<RegistryFriendlyByteBuf, DoPlayer> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, DoPlayer::fellowshipID, UUIDUtil.STREAM_CODEC, DoPlayer::subject,
                ByteBufCodecs.idMapper(i -> PlayerFunction.values()[i], PlayerFunction::ordinal), DoPlayer::function,
                DoPlayer::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFellowshipInvitePlayer. */
    public record InvitePlayer(UUID fellowshipID, String username) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<InvitePlayer> TYPE = payloadType("fellowship_invite");
        public static final StreamCodec<RegistryFriendlyByteBuf, InvitePlayer> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, InvitePlayer::fellowshipID, ByteBufCodecs.stringUtf8(16),
                InvitePlayer::username, InvitePlayer::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFellowshipRename. */
    public record Rename(UUID fellowshipID, String name) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Rename> TYPE = payloadType("fellowship_rename");
        public static final StreamCodec<RegistryFriendlyByteBuf, Rename> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, Rename::fellowshipID, ByteBufCodecs.stringUtf8(40), Rename::name, Rename::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFellowshipRespondInvite. */
    public record RespondInvite(UUID fellowshipID, boolean accept) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<RespondInvite> TYPE = payloadType("fellowship_respond_invite");
        public static final StreamCodec<RegistryFriendlyByteBuf, RespondInvite> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, RespondInvite::fellowshipID, ByteBufCodecs.BOOL, RespondInvite::accept,
                RespondInvite::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
