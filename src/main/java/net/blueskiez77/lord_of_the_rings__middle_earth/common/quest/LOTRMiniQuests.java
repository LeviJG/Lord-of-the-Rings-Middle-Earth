package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCQuestInfo;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionData;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import org.jspecify.annotations.Nullable;

/**
 * LOTRPlayerData's mini-quests: each player's quests in hand and those done, how many they have done
 * (and of those, bounties), the quest they follow, and the factions that have newly set a bounty on
 * them. Kept for the whole world, as quests are failed and bounties set while their players are away
 * (an NPC's death, a bounty taken out); each player is sent theirs as they change.
 */
public final class LOTRMiniQuests extends SavedData {

    private static final SavedDataType<LOTRMiniQuests> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "miniquests"), LOTRMiniQuests::new,
            CompoundTag.CODEC.xmap(LOTRMiniQuests::load, LOTRMiniQuests::save), null);

    private static @Nullable LOTRMiniQuests instance;
    private static @Nullable MinecraftServer server;

    private final Map<UUID, PlayerQuests> players = new HashMap<>();

    // ---------------------------------------------------------------- packets

    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> payloadType(String name) {
        return new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
    }

    /** LOTRPacketMiniquest: a quest of the player's as it now is. */
    public record QuestPayload(CompoundTag quest, boolean completed) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<QuestPayload> TYPE = payloadType("miniquest");
        public static final StreamCodec<RegistryFriendlyByteBuf, QuestPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.COMPOUND_TAG, QuestPayload::quest, ByteBufCodecs.BOOL, QuestPayload::completed, QuestPayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketMiniquestRemove: a quest gone from the list -- or moved to those done. */
    public record RemovePayload(UUID questID, boolean wasCompleted, boolean addToCompleted) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<RemovePayload> TYPE = payloadType("miniquest_remove");
        public static final StreamCodec<RegistryFriendlyByteBuf, RemovePayload> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, RemovePayload::questID, ByteBufCodecs.BOOL, RemovePayload::wasCompleted,
                ByteBufCodecs.BOOL, RemovePayload::addToCompleted, RemovePayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketMiniquestTrack: the quest the player now follows. */
    public record TrackPayload(Optional<UUID> questID) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<TrackPayload> TYPE = payloadType("miniquest_track");
        public static final StreamCodec<RegistryFriendlyByteBuf, TrackPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), TrackPayload::questID, TrackPayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketMiniquestTrackClient: the quest followed, told back to the player. */
    public record TrackClientPayload(Optional<UUID> questID) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<TrackClientPayload> TYPE = payloadType("miniquest_track_client");
        public static final StreamCodec<RegistryFriendlyByteBuf, TrackClientPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), TrackClientPayload::questID, TrackClientPayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** The login packet's MQCompleteCount: how many quests the player has done, for the red book. */
    public record CompletedCountPayload(int count) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<CompletedCountPayload> TYPE = payloadType("miniquest_completed_count");
        public static final StreamCodec<RegistryFriendlyByteBuf, CompletedCountPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, CompletedCountPayload::count, CompletedCountPayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketDeleteMiniquest: the red book's "remove this quest". */
    public record DeletePayload(UUID questID, boolean completed) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<DeletePayload> TYPE = payloadType("miniquest_delete");
        public static final StreamCodec<RegistryFriendlyByteBuf, DeletePayload> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, DeletePayload::questID, ByteBufCodecs.BOOL, DeletePayload::completed, DeletePayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketClientMQEvent: the map or the factions screen opened. */
    public record ClientEventPayload(LOTRMiniQuestEvent event) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ClientEventPayload> TYPE = payloadType("miniquest_client_event");
        public static final StreamCodec<RegistryFriendlyByteBuf, ClientEventPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.idMapper(i -> LOTRMiniQuestEvent.values()[i], LOTRMiniQuestEvent::ordinal),
                ClientEventPayload::event, ClientEventPayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    // ---------------------------------------------------------------- world data

    private static LOTRMiniQuests load(CompoundTag tag) {
        LOTRMiniQuests data = new LOTRMiniQuests();
        if (server == null) {
            return data;
        }
        HolderLookup.Provider registries = server.registryAccess();
        for (Tag t : tag.getListOrEmpty("Players")) {
            if (t instanceof CompoundTag playerTag) {
                try {
                    UUID id = UUID.fromString(playerTag.getStringOr("UUID", ""));
                    PlayerQuests pq = new PlayerQuests(id);
                    pq.load(playerTag, registries);
                    data.players.put(id, pq);
                } catch (IllegalArgumentException e) {
                    LOTRMod.LOGGER.error("LOTR: could not load a player's mini-quests", e);
                }
            }
        }
        return data;
    }

    private CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        if (server != null) {
            for (PlayerQuests pq : this.players.values()) {
                CompoundTag playerTag = new CompoundTag();
                playerTag.putString("UUID", pq.playerID.toString());
                pq.save(playerTag, server.registryAccess());
                list.add(playerTag);
            }
        }
        tag.put("Players", list);
        return tag;
    }

    /** Any player's quests, there or not. */
    public static PlayerQuests forPlayer(UUID player) {
        if (instance == null) {
            return new PlayerQuests(player);
        }
        return instance.players.computeIfAbsent(player, PlayerQuests::new);
    }

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(QuestPayload.TYPE, QuestPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(RemovePayload.TYPE, RemovePayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(TrackClientPayload.TYPE, TrackClientPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(CompletedCountPayload.TYPE, CompletedCountPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(TrackPayload.TYPE, TrackPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(DeletePayload.TYPE, DeletePayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ClientEventPayload.TYPE, ClientEventPayload.STREAM_CODEC);
        LOTRMiniQuestFactory.createMiniQuests();
        LOTRNPCQuestInfo.initNetworking();

        ServerLifecycleEvents.SERVER_STARTED.register(s -> {
            server = s;
            LOTRMiniQuestKillEntity.resolveKillClasses();
            instance = s.getDataStorage().computeIfAbsent(TYPE);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(s -> {
            server = null;
            instance = null;
        });
        // sendLoginPacket's quests.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, s) -> forPlayer(handler.player.getUUID()).sendAll(handler.player));
        // onUpdate: handleTrackingMiniQuest and handleBounties.
        ServerTickEvents.END_SERVER_TICK.register(s -> {
            for (ServerPlayer player : s.getPlayerList().getPlayers()) {
                forPlayer(player.getUUID()).onUpdate(player);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(TrackPayload.TYPE, (payload, context) ->
                forPlayer(context.player().getUUID()).setTrackingMiniQuestID(payload.questID().orElse(null)));
        ServerPlayNetworking.registerGlobalReceiver(DeletePayload.TYPE, (payload, context) -> {
            PlayerQuests pq = forPlayer(context.player().getUUID());
            LOTRMiniQuest quest = pq.getMiniQuestForID(payload.questID(), payload.completed());
            if (quest != null) {
                pq.removeMiniQuest(quest, payload.completed());
            } else {
                LOTRMod.LOGGER.warn("Tried to remove a LOTR miniquest that doesn't exist");
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(ClientEventPayload.TYPE, (payload, context) -> {
            if (payload.event() == LOTRMiniQuestEvent.VIEW_MAP || payload.event() == LOTRMiniQuestEvent.VIEW_FACTIONS) {
                forPlayer(context.player().getUUID()).distributeMQEvent(payload.event());
            }
        });
    }

    /** onLivingDeath's mini-quest kills: from LOTRNPCKillEvents, for a player's own kill. */
    public static void onKill(Player player, net.minecraft.world.entity.LivingEntity entity) {
        for (LOTRMiniQuest quest : new ArrayList<>(forPlayer(player.getUUID()).getMiniQuests())) {
            quest.onKill(player, entity);
        }
    }

    private static @Nullable ServerPlayer onlinePlayer(UUID id) {
        return server == null ? null : server.getPlayerList().getPlayer(id);
    }

    private static void markDirty() {
        if (instance != null) {
            instance.setDirty();
        }
    }

    // ---------------------------------------------------------------- one player's

    public static final class PlayerQuests {
        public final UUID playerID;
        private final List<LOTRMiniQuest> miniQuests = new ArrayList<>();
        private final List<LOTRMiniQuest> miniQuestsCompleted = new ArrayList<>();
        private int completedMiniquestCount;
        private int completedBountyQuests;
        private @Nullable UUID trackingMiniQuestID;
        private final List<LOTRFaction> bountiesPlaced = new ArrayList<>();
        /** LOTRPlayerQuestData's "Pouches": the Grey Wanderer's three pouches given. */
        private boolean givenFirstPouches;

        PlayerQuests(UUID playerID) {
            this.playerID = playerID;
        }

        void load(CompoundTag tag, HolderLookup.Provider registries) {
            this.miniQuests.clear();
            for (Tag t : tag.getListOrEmpty("MiniQuests")) {
                if (t instanceof CompoundTag q) {
                    LOTRMiniQuest quest = LOTRMiniQuest.loadQuest(q, registries, this);
                    if (quest != null) {
                        this.miniQuests.add(quest);
                    }
                }
            }
            this.miniQuestsCompleted.clear();
            for (Tag t : tag.getListOrEmpty("MiniQuestsCompleted")) {
                if (t instanceof CompoundTag q) {
                    LOTRMiniQuest quest = LOTRMiniQuest.loadQuest(q, registries, this);
                    if (quest != null) {
                        this.miniQuestsCompleted.add(quest);
                    }
                }
            }
            this.completedMiniquestCount = tag.getIntOr("MQCompleteCount", 0);
            this.completedBountyQuests = tag.getIntOr("MQCompletedBounties", 0);
            this.trackingMiniQuestID = tag.getString("MiniQuestTrack").map(UUID::fromString).orElse(null);
            this.givenFirstPouches = tag.getBooleanOr("Pouches", false);
            this.bountiesPlaced.clear();
            for (Tag t : tag.getListOrEmpty("BountiesPlaced")) {
                t.asString().map(LOTRFaction::forName).ifPresent(this.bountiesPlaced::add);
            }
        }

        void save(CompoundTag tag, HolderLookup.Provider registries) {
            ListTag active = new ListTag();
            this.miniQuests.forEach(q -> active.add(q.save(registries)));
            tag.put("MiniQuests", active);
            ListTag done = new ListTag();
            this.miniQuestsCompleted.forEach(q -> done.add(q.save(registries)));
            tag.put("MiniQuestsCompleted", done);
            tag.putInt("MQCompleteCount", this.completedMiniquestCount);
            tag.putInt("MQCompletedBounties", this.completedBountyQuests);
            if (this.trackingMiniQuestID != null) {
                tag.putString("MiniQuestTrack", this.trackingMiniQuestID.toString());
            }
            ListTag bounties = new ListTag();
            this.bountiesPlaced.forEach(f -> bounties.add(StringTag.valueOf(f.codeName())));
            tag.put("BountiesPlaced", bounties);
            tag.putBoolean("Pouches", this.givenFirstPouches);
        }

        /** sendLoginPacket: every quest, and the one followed. */
        void sendAll(ServerPlayer player) {
            for (LOTRMiniQuest quest : this.miniQuests) {
                sendMiniQuestPacket(player, quest, false);
            }
            for (LOTRMiniQuest quest : this.miniQuestsCompleted) {
                sendMiniQuestPacket(player, quest, true);
            }
            ServerPlayNetworking.send(player, new TrackClientPayload(Optional.ofNullable(this.trackingMiniQuestID)));
            ServerPlayNetworking.send(player, new CompletedCountPayload(this.completedMiniquestCount));
        }

        private void sendMiniQuestPacket(ServerPlayer player, LOTRMiniQuest quest, boolean completed) {
            ServerPlayNetworking.send(player, new QuestPayload(quest.save(player.level().registryAccess()), completed));
        }

        void onUpdate(ServerPlayer player) {
            if (this.trackingMiniQuestID != null && getTrackingMiniQuest() == null) {
                setTrackingMiniQuest(null);
            }
            for (LOTRMiniQuest quest : getActiveMiniQuests()) {
                quest.onPlayerTick(player);
            }
            if (!this.bountiesPlaced.isEmpty()) {
                for (LOTRFaction fac : this.bountiesPlaced) {
                    player.sendSystemMessage(Component.translatable("chat.lotr.bountyPlaced", fac.factionName())
                            .withStyle(ChatFormatting.YELLOW));
                }
                this.bountiesPlaced.clear();
                markDirty();
            }
        }

        public void addMiniQuest(LOTRMiniQuest quest) {
            quest.playerData = this;
            this.miniQuests.add(quest);
            updateMiniQuest(quest);
        }

        /** completeMiniQuest: among those done, counted, and for its faction too. */
        public void completeMiniQuest(LOTRMiniQuest quest) {
            if (this.miniQuests.remove(quest)) {
                this.miniQuestsCompleted.add(quest);
                ++this.completedMiniquestCount;
                ServerPlayer player = onlinePlayer(this.playerID);
                if (player != null) {
                    LOTRFactionData.completeMiniQuest(player, quest.entityFaction);
                    ServerPlayNetworking.send(player, new RemovePayload(quest.questUUID, false, true));
                    ServerPlayNetworking.send(player, new CompletedCountPayload(this.completedMiniquestCount));
                }
                markDirty();
            } else {
                LOTRMod.LOGGER.warn("Attempted to remove a miniquest which does not belong to the player data");
            }
        }

        public void removeMiniQuest(LOTRMiniQuest quest, boolean completed) {
            List<LOTRMiniQuest> removeList = completed ? this.miniQuestsCompleted : this.miniQuests;
            if (removeList.remove(quest)) {
                markDirty();
                ServerPlayer player = onlinePlayer(this.playerID);
                if (player != null) {
                    ServerPlayNetworking.send(player, new RemovePayload(quest.questUUID, quest.isCompleted(), false));
                }
            } else {
                LOTRMod.LOGGER.warn("Attempted to remove a miniquest which does not belong to the player data");
            }
        }

        public void updateMiniQuest(LOTRMiniQuest quest) {
            markDirty();
            ServerPlayer player = onlinePlayer(this.playerID);
            if (player != null) {
                sendMiniQuestPacket(player, quest, false);
            }
        }

        public void distributeMQEvent(LOTRMiniQuestEvent event) {
            for (LOTRMiniQuest quest : new ArrayList<>(this.miniQuests)) {
                if (quest.isActive()) {
                    quest.handleEvent(event);
                }
            }
        }

        public List<LOTRMiniQuest> getMiniQuests() {
            return this.miniQuests;
        }

        public List<LOTRMiniQuest> getMiniQuestsCompleted() {
            return this.miniQuestsCompleted;
        }

        public List<LOTRMiniQuest> selectMiniQuests(MiniQuestSelector selector) {
            List<LOTRMiniQuest> ret = new ArrayList<>();
            for (LOTRMiniQuest quest : new ArrayList<>(this.miniQuests)) {
                if (selector.include(quest)) {
                    ret.add(quest);
                }
            }
            return ret;
        }

        public List<LOTRMiniQuest> getActiveMiniQuests() {
            return selectMiniQuests(new MiniQuestSelector.OptionalActive().setActiveOnly());
        }

        public List<LOTRMiniQuest> getMiniQuestsForEntity(LOTRNPCEntity npc, boolean activeOnly) {
            return getMiniQuestsForEntityID(npc.getUUID(), activeOnly);
        }

        public List<LOTRMiniQuest> getMiniQuestsForEntityID(UUID npcID, boolean activeOnly) {
            MiniQuestSelector.EntityId sel = new MiniQuestSelector.EntityId(npcID);
            if (activeOnly) {
                sel.setActiveOnly();
            }
            return selectMiniQuests(sel);
        }

        public List<LOTRMiniQuest> getMiniQuestsForFaction(LOTRFaction faction, boolean activeOnly) {
            MiniQuestSelector.Faction sel = new MiniQuestSelector.Faction(() -> faction);
            if (activeOnly) {
                sel.setActiveOnly();
            }
            return selectMiniQuests(sel);
        }

        public @Nullable LOTRMiniQuest getMiniQuestForID(UUID id, boolean completed) {
            for (LOTRMiniQuest quest : new ArrayList<>(completed ? this.miniQuestsCompleted : this.miniQuests)) {
                if (quest.questUUID.equals(id)) {
                    return quest;
                }
            }
            return null;
        }

        public @Nullable LOTRMiniQuest getTrackingMiniQuest() {
            return this.trackingMiniQuestID == null ? null : getMiniQuestForID(this.trackingMiniQuestID, false);
        }

        public void setTrackingMiniQuest(@Nullable LOTRMiniQuest quest) {
            setTrackingMiniQuestID(quest == null ? null : quest.questUUID);
        }

        public void setTrackingMiniQuestID(@Nullable UUID id) {
            this.trackingMiniQuestID = id;
            markDirty();
            ServerPlayer player = onlinePlayer(this.playerID);
            if (player != null) {
                ServerPlayNetworking.send(player, new TrackClientPayload(Optional.ofNullable(id)));
            }
        }

        public void addCompletedBountyQuest() {
            ++this.completedBountyQuests;
            markDirty();
        }

        public int getCompletedBountyQuests() {
            return this.completedBountyQuests;
        }

        public int getCompletedMiniQuestsTotal() {
            return this.completedMiniquestCount;
        }

        public boolean getGivenFirstPouches() {
            return this.givenFirstPouches;
        }

        public void setGivenFirstPouches(boolean flag) {
            this.givenFirstPouches = flag;
            markDirty();
        }

        public void placeBountyFor(LOTRFaction faction) {
            this.bountiesPlaced.add(faction);
            markDirty();
        }

        /** hasActiveOrCompleteMQType(LOTRMiniQuestWelcome): a welcome quest in hand, or done (whether failed or not). */
        public boolean hasAnyGWQuest() {
            for (LOTRMiniQuest q : this.miniQuests) {
                if (q.isActive() && q instanceof LOTRMiniQuestWelcome) {
                    return true;
                }
            }
            for (LOTRMiniQuest q : this.miniQuestsCompleted) {
                if (q instanceof LOTRMiniQuestWelcome) {
                    return true;
                }
            }
            return false;
        }
    }
}
