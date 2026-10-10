package net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRPlayerTitles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.title.LOTRTitle;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

/**
 * LOTRFellowshipData and LOTRPlayerData's fellowship half: every fellowship, each player's
 * invitations and the fellowship their /fmsg is bound to, and what may be done with them -- by
 * the same rules, with the same notices. Kept for the whole world, since a fellowship's members
 * (and those invited) are so often away; what each player is in follows from the fellowships.
 */
public final class LOTRFellowships extends SavedData {

    private record Invite(UUID player, UUID fellowship, Optional<UUID> inviter) {
        static final Codec<Invite> CODEC = RecordCodecBuilder.create(i -> i.group(
                UUIDUtil.STRING_CODEC.fieldOf("Player").forGetter(Invite::player),
                UUIDUtil.STRING_CODEC.fieldOf("Fellowship").forGetter(Invite::fellowship),
                UUIDUtil.STRING_CODEC.optionalFieldOf("Inviter").forGetter(Invite::inviter)).apply(i, Invite::new));
    }

    private record ChatBound(UUID player, UUID fellowship) {
        static final Codec<ChatBound> CODEC = RecordCodecBuilder.create(i -> i.group(
                UUIDUtil.STRING_CODEC.fieldOf("Player").forGetter(ChatBound::player),
                UUIDUtil.STRING_CODEC.fieldOf("Fellowship").forGetter(ChatBound::fellowship)).apply(i, ChatBound::new));
    }

    private static final Codec<LOTRFellowships> CODEC = RecordCodecBuilder.create(i -> i.group(
            LOTRFellowship.CODEC.listOf().optionalFieldOf("Fellowships", List.of())
                    .forGetter(d -> List.copyOf(d.fellowships.values())),
            Invite.CODEC.listOf().optionalFieldOf("Invites", List.of()).forGetter(d -> d.invites),
            ChatBound.CODEC.listOf().optionalFieldOf("ChatBound", List.of()).forGetter(d -> d.chatBound.entrySet()
                    .stream().map(e -> new ChatBound(e.getKey(), e.getValue())).toList())
    ).apply(i, (fellowships, invites, chatBound) -> {
        LOTRFellowships data = new LOTRFellowships();
        fellowships.forEach(fs -> data.fellowships.put(fs.getFellowshipID(), fs));
        data.invites.addAll(invites);
        chatBound.forEach(c -> data.chatBound.put(c.player(), c.fellowship()));
        return data;
    }));

    private static final SavedDataType<LOTRFellowships> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "fellowships"), LOTRFellowships::new, CODEC, null);

    private static @Nullable LOTRFellowships instance;
    private static @Nullable MinecraftServer server;

    private final Map<UUID, LOTRFellowship> fellowships = new LinkedHashMap<>();
    private final List<Invite> invites = new ArrayList<>();
    private final Map<UUID, UUID> chatBound = new HashMap<>();

    public static void init() {
        LOTRFellowshipNetworking.init();
        ServerLifecycleEvents.SERVER_STARTED.register(s -> {
            server = s;
            instance = s.getDataStorage().computeIfAbsent(TYPE);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(s -> {
            server = null;
            instance = null;
        });
        // sendLoginPacket's fellowships: the player's fellowships and invitations.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, s) -> {
            ServerPlayer player = handler.player;
            for (LOTRFellowship fs : getFellowships(player.getUUID())) {
                ServerPlayNetworking.send(player, new LOTRFellowshipPayloads.Sync(view(fs, player.getUUID()), false));
            }
            for (Invite invite : List.copyOf(data().invites)) {
                LOTRFellowship fs = getFellowship(invite.fellowship());
                if (invite.player().equals(player.getUUID()) && fs != null) {
                    ServerPlayNetworking.send(player, new LOTRFellowshipPayloads.Sync(view(fs, player.getUUID()), true));
                }
            }
        });
    }

    private static LOTRFellowships data() {
        if (instance == null) {
            throw new IllegalStateException("LOTR fellowship data used with no server running");
        }
        return instance;
    }

    private static MinecraftServer server() {
        if (server == null) {
            throw new IllegalStateException("LOTR fellowship data used with no server running");
        }
        return server;
    }

    private static void markDirty() {
        data().setDirty();
    }

    // ---------------------------------------------------------------- lookups

    public static @Nullable LOTRFellowship getFellowship(UUID fsID) {
        return instance == null ? null : instance.fellowships.get(fsID);
    }

    /** getActiveFellowship: one not disbanded. */
    public static @Nullable LOTRFellowship getActiveFellowship(UUID fsID) {
        LOTRFellowship fs = getFellowship(fsID);
        return fs != null && !fs.isDisbanded() ? fs : null;
    }

    /** getFellowships: the active fellowships the player is in. */
    public static List<LOTRFellowship> getFellowships(UUID player) {
        List<LOTRFellowship> list = new ArrayList<>();
        if (instance != null) {
            for (LOTRFellowship fs : instance.fellowships.values()) {
                if (!fs.isDisbanded() && fs.containsPlayer(player)) {
                    list.add(fs);
                }
            }
        }
        return list;
    }

    public static @Nullable LOTRFellowship getFellowshipByName(UUID player, String fsName) {
        for (LOTRFellowship fs : getFellowships(player)) {
            if (fs.getName().equalsIgnoreCase(fsName)) {
                return fs;
            }
        }
        return null;
    }

    public static boolean anyMatchingFellowshipNames(UUID player, String name) {
        String stripped = StringUtils.strip(name).toLowerCase(Locale.ROOT);
        for (LOTRFellowship fs : getFellowships(player)) {
            if (stripped.equals(StringUtils.strip(fs.getName()).toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    /** getMaxLeadingFellowships: one, and one more for every twenty achievements. */
    public static int getMaxLeadingFellowships(UUID player) {
        net.minecraft.server.level.ServerPlayer online = server == null ? null : server.getPlayerList().getPlayer(player);
        int achievements = online == null ? 0
                : net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements.countEarned(online);
        return 1 + achievements / 20;
    }

    public static boolean canCreateFellowships(UUID player) {
        int leading = 0;
        for (LOTRFellowship fs : getFellowships(player)) {
            if (fs.isOwner(player)) {
                ++leading;
            }
        }
        return leading < getMaxLeadingFellowships(player);
    }

    public static List<String> listAllFellowshipNames(UUID player, boolean leadingOnly) {
        List<String> names = new ArrayList<>();
        for (LOTRFellowship fs : getFellowships(player)) {
            if (!leadingOnly || fs.isOwner(player)) {
                names.add(fs.getName());
            }
        }
        return names;
    }

    public static @Nullable LOTRFellowship getChatBoundFellowship(UUID player) {
        UUID fsID = data().chatBound.get(player);
        return fsID == null ? null : getActiveFellowship(fsID);
    }

    public static void setChatBoundFellowship(UUID player, @Nullable LOTRFellowship fs) {
        if (fs == null) {
            data().chatBound.remove(player);
        } else {
            data().chatBound.put(player, fs.getFellowshipID());
        }
        markDirty();
    }

    // ---------------------------------------------------------------- what players do

    public static void createFellowship(UUID player, String name, boolean normalCreation) {
        if (normalCreation && (!LOTRConfig.enableFellowshipCreation || !canCreateFellowships(player))) {
            return;
        }
        if (!anyMatchingFellowshipNames(player, name)) {
            LOTRFellowship fs = new LOTRFellowship(player, name);
            data().fellowships.put(fs.getFellowshipID(), fs);
            markDirty();
            updateForAllMembers(fs);
        }
    }

    /** invitePlayerToFellowship: by its owner or an admin. */
    public static void invitePlayerToFellowship(UUID actor, LOTRFellowship fs, UUID invited, String inviterUsername) {
        if (fs.isOwner(actor) || fs.isAdmin(actor)) {
            addFellowshipInvite(invited, fs, actor, inviterUsername);
        }
    }

    private static void addFellowshipInvite(UUID invited, LOTRFellowship fs, UUID inviter, String inviterUsername) {
        if (findInvite(invited, fs) == null) {
            data().invites.add(new Invite(invited, fs.getFellowshipID(), Optional.of(inviter)));
            markDirty();
            ServerPlayer player = onlinePlayer(invited);
            if (player != null) {
                ServerPlayNetworking.send(player, new LOTRFellowshipPayloads.Sync(view(fs, invited), true));
                sendNotification(player, "lotr.gui.fellowships.notifyInvite", inviterUsername);
            }
        }
    }

    private static @Nullable Invite findInvite(UUID player, LOTRFellowship fs) {
        for (Invite invite : data().invites) {
            if (invite.player().equals(player) && invite.fellowship().equals(fs.getFellowshipID())) {
                return invite;
            }
        }
        return null;
    }

    public static void acceptFellowshipInvite(UUID player, LOTRFellowship fs, boolean respectSizeLimit) {
        Invite invite = findInvite(player, fs);
        if (invite == null) {
            return;
        }
        ServerPlayer entityplayer = onlinePlayer(player);
        if (fs.isDisbanded()) {
            rejectFellowshipInvite(player, fs);
            if (entityplayer != null) {
                ServerPlayNetworking.send(entityplayer, new LOTRFellowshipPayloads.AcceptResult(fs.getFellowshipID(),
                        fs.getName(), LOTRFellowshipPayloads.AcceptInviteResult.DISBANDED));
            }
            return;
        }
        int limit = LOTRConfig.fellowshipMaxSize;
        if (respectSizeLimit && limit >= 0 && fs.getPlayerCount() >= limit) {
            rejectFellowshipInvite(player, fs);
            if (entityplayer != null) {
                ServerPlayNetworking.send(entityplayer, new LOTRFellowshipPayloads.AcceptResult(fs.getFellowshipID(),
                        fs.getName(), LOTRFellowshipPayloads.AcceptInviteResult.TOO_LARGE));
            }
            return;
        }
        fs.addMember(player);
        data().invites.remove(invite);
        markDirty();
        updateForAllMembers(fs);
        if (entityplayer != null) {
            ServerPlayNetworking.send(entityplayer, new LOTRFellowshipPayloads.Remove(fs.getFellowshipID(), true));
            ServerPlayNetworking.send(entityplayer, new LOTRFellowshipPayloads.AcceptResult(fs.getFellowshipID(),
                    fs.getName(), LOTRFellowshipPayloads.AcceptInviteResult.JOINED));
            ServerPlayer inviter = onlinePlayer(invite.inviter().orElse(fs.getOwner()));
            if (inviter != null) {
                sendNotification(inviter, "lotr.gui.fellowships.notifyAccept", entityplayer.getName().getString());
            }
        }
    }

    public static void rejectFellowshipInvite(UUID player, LOTRFellowship fs) {
        Invite invite = findInvite(player, fs);
        if (invite != null) {
            data().invites.remove(invite);
            markDirty();
            ServerPlayer entityplayer = onlinePlayer(player);
            if (entityplayer != null) {
                ServerPlayNetworking.send(entityplayer, new LOTRFellowshipPayloads.Remove(fs.getFellowshipID(), true));
            }
        }
    }

    /** acceptFellowshipInvite's answer when the fellowship asked about no longer exists at all. */
    public static void sendNonexistent(ServerPlayer player, UUID fsID) {
        ServerPlayNetworking.send(player, new LOTRFellowshipPayloads.AcceptResult(fsID, "",
                LOTRFellowshipPayloads.AcceptInviteResult.NONEXISTENT));
    }

    public static void leaveFellowship(UUID player, LOTRFellowship fs) {
        if (!fs.isOwner(player)) {
            removeMember(fs, player);
            ServerPlayer entityplayer = onlinePlayer(player);
            ServerPlayer owner = onlinePlayer(fs.getOwner());
            if (entityplayer != null && owner != null) {
                sendNotification(owner, "lotr.gui.fellowships.notifyLeave", entityplayer.getName().getString());
            }
        }
    }

    public static void disbandFellowship(UUID actor, LOTRFellowship fs, String disbanderUsername) {
        if (fs.isOwner(actor)) {
            List<UUID> memberUUIDs = new ArrayList<>(fs.getMemberUUIDs());
            fs.setDisbanded();
            markDirty();
            for (UUID member : memberUUIDs) {
                removeMember(fs, member);
            }
            sendRemove(actor, fs);
            for (UUID member : memberUUIDs) {
                ServerPlayer memberPlayer = onlinePlayer(member);
                if (memberPlayer != null) {
                    sendNotification(memberPlayer, "lotr.gui.fellowships.notifyDisband", disbanderUsername);
                }
            }
        }
    }

    public static void removePlayerFromFellowship(UUID actor, LOTRFellowship fs, UUID subject, String removerUsername) {
        if (fs.isOwner(actor) || fs.isAdmin(actor)) {
            removeMember(fs, subject);
            ServerPlayer removed = onlinePlayer(subject);
            if (removed != null) {
                sendNotification(removed, "lotr.gui.fellowships.notifyRemove", removerUsername);
            }
        }
    }

    private static void removeMember(LOTRFellowship fs, UUID player) {
        if (fs.hasMember(player)) {
            fs.removeMember(player);
            markDirty();
            updateForAllMembers(fs);
            sendRemove(player, fs);
        }
    }

    public static void transferFellowship(UUID actor, LOTRFellowship fs, UUID newOwner, String prevOwnerUsername) {
        if (fs.isOwner(actor)) {
            fs.setOwner(newOwner);
            markDirty();
            updateForAllMembers(fs);
            ServerPlayer player = onlinePlayer(newOwner);
            if (player != null) {
                sendNotification(player, "lotr.gui.fellowships.notifyTransfer", prevOwnerUsername);
            }
        }
    }

    public static void setFellowshipAdmin(UUID actor, LOTRFellowship fs, UUID subject, boolean flag, String granterUsername) {
        if (fs.isOwner(actor) && fs.hasMember(subject) && fs.getAdminUUIDs().contains(subject) != flag) {
            fs.setAdmin(subject, flag);
            markDirty();
            updateForAllMembers(fs);
            ServerPlayer player = onlinePlayer(subject);
            if (player != null) {
                sendNotification(player, flag ? "lotr.gui.fellowships.notifyOp" : "lotr.gui.fellowships.notifyDeop",
                        granterUsername);
            }
        }
    }

    public static void renameFellowship(UUID actor, LOTRFellowship fs, String name) {
        if (fs.isOwner(actor)) {
            fs.setName(name);
            markDirty();
            updateForAllMembers(fs);
        }
    }

    public static void setFellowshipIcon(UUID actor, LOTRFellowship fs, @Nullable ItemStack icon) {
        if (fs.isOwner(actor) || fs.isAdmin(actor)) {
            fs.setIcon(icon);
            markDirty();
            updateForAllMembers(fs);
        }
    }

    public static void setFellowshipPreventPVP(UUID actor, LOTRFellowship fs, boolean prevent) {
        if (fs.isOwner(actor) || fs.isAdmin(actor)) {
            fs.setPreventPVP(prevent);
            markDirty();
            updateForAllMembers(fs);
        }
    }

    public static void setFellowshipPreventHiredFF(UUID actor, LOTRFellowship fs, boolean prevent) {
        if (fs.isOwner(actor) || fs.isAdmin(actor)) {
            fs.setPreventHiredFriendlyFire(prevent);
            markDirty();
            updateForAllMembers(fs);
        }
    }

    public static void setFellowshipShowMapLocations(UUID actor, LOTRFellowship fs, boolean show) {
        if (fs.isOwner(actor)) {
            fs.setShowMapLocations(show);
            markDirty();
            updateForAllMembers(fs);
        }
    }

    /** A member's title changed: their fellows see it. */
    public static void onTitleChanged(ServerPlayer player) {
        if (instance != null) {
            for (LOTRFellowship fs : getFellowships(player.getUUID())) {
                updateForAllMembers(fs);
            }
        }
    }

    // ---------------------------------------------------------------- messages

    /** sendFellowshipMessage: "[Fellowship] <Name> message" to every member online, in yellow. */
    public static void sendFellowshipMessage(ServerPlayer sender, LOTRFellowship fs, String message) {
        message = StringUtils.normalizeSpace(message);
        if (StringUtils.isBlank(message)) {
            return;
        }
        ClickEvent fMsgClickEvent = new ClickEvent.SuggestCommand("/fmsg \"" + fs.getName() + "\" ");
        MutableComponent senderComponent = sender.getDisplayName().copy()
                .withStyle(style -> style.withClickEvent(fMsgClickEvent));
        MutableComponent chatComponent = Component.translatable("chat.type.text", senderComponent,
                Component.literal(message).withStyle(ChatFormatting.YELLOW));
        MutableComponent fsComponent = Component.translatable("commands.lotr.fmsg.fsPrefix", fs.getName())
                .withStyle(style -> style.withColor(ChatFormatting.YELLOW).withClickEvent(fMsgClickEvent));
        Component full = Component.translatable("%s %s", fsComponent, chatComponent);
        MinecraftServer s = server();
        s.sendSystemMessage(full);
        for (ServerPlayer player : s.getPlayerList().getPlayers()) {
            if (fs.containsPlayer(player.getUUID())) {
                player.sendSystemMessage(full);
            }
        }
    }

    /** sendNotification: in chat, in yellow, and in the corner of the screen. */
    public static void sendNotification(ServerPlayer player, String key, Object... args) {
        Component message = Component.translatable(key, args).withStyle(ChatFormatting.YELLOW);
        player.sendSystemMessage(message);
        ServerPlayNetworking.send(player, new LOTRFellowshipPayloads.Notification(message));
    }

    // ---------------------------------------------------------------- sending

    private static @Nullable ServerPlayer onlinePlayer(UUID player) {
        return server == null ? null : server.getPlayerList().getPlayer(player);
    }

    private static void sendRemove(UUID player, LOTRFellowship fs) {
        ServerPlayer entityplayer = onlinePlayer(player);
        if (entityplayer != null) {
            ServerPlayNetworking.send(entityplayer, new LOTRFellowshipPayloads.Remove(fs.getFellowshipID(), false));
        }
    }

    /** updateForAllMembers: each member online sees the fellowship as it now is. */
    private static void updateForAllMembers(LOTRFellowship fs) {
        for (UUID member : fs.getAllPlayerUUIDs()) {
            ServerPlayer player = onlinePlayer(member);
            if (player != null) {
                ServerPlayNetworking.send(player, new LOTRFellowshipPayloads.Sync(view(fs, member), false));
            }
        }
    }

    private static LOTRFellowshipPayloads.View view(LOTRFellowship fs, UUID viewer) {
        Map<UUID, String> usernames = new HashMap<>();
        Map<UUID, LOTRTitle.PlayerTitle> titles = new HashMap<>();
        for (UUID player : fs.getAllPlayerUUIDs()) {
            usernames.put(player, usernameOf(player));
            LOTRTitle.PlayerTitle title = titleOf(player);
            if (title != null) {
                titles.put(player, title);
            }
        }
        return new LOTRFellowshipPayloads.View(fs.getFellowshipID(), fs.getName(),
                fs.getIcon() == null ? ItemStack.EMPTY : fs.getIcon(), fs.isOwner(viewer), fs.isAdmin(viewer),
                fs.getOwner(), List.copyOf(fs.getMemberUUIDs()), usernames, titles, java.util.Set.copyOf(fs.getAdminUUIDs()),
                fs.getPreventPVP(), fs.getPreventHiredFriendlyFire(), fs.getShowMapLocations());
    }

    public static String usernameOf(UUID player) {
        ServerPlayer online = onlinePlayer(player);
        if (online != null) {
            return online.getName().getString();
        }
        return server == null ? "?" : server.services().nameToIdCache().get(player).map(NameAndId::name).orElse("?");
    }

    /** getPlayerTitleWithOfflineCache: an absent player's title, from their saved player file. */
    private static LOTRTitle.@Nullable PlayerTitle titleOf(UUID player) {
        ServerPlayer online = onlinePlayer(player);
        if (online != null) {
            return LOTRPlayerTitles.getPlayerTitle(online);
        }
        if (server == null) {
            return null;
        }
        Optional<NameAndId> profile = server.services().nameToIdCache().get(player);
        return profile.flatMap(p -> server.playerDataStorage.load(p))
                .flatMap(tag -> tag.getCompound(AttachmentTarget.NBT_ATTACHMENT_KEY))
                .flatMap(attachments -> Optional.ofNullable(
                        attachments.get(LOTRPlayerTitles.PLAYER_TITLE.identifier().toString())))
                .flatMap(tag -> LOTRPlayerTitles.CODEC.parse(
                        RegistryOps.create(NbtOps.INSTANCE, server.registryAccess()), tag).result())
                .orElse(null);
    }
}
