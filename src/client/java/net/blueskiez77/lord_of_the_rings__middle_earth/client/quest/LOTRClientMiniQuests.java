package net.blueskiez77.lord_of_the_rings__middle_earth.client.quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMiniQuestOfferScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCQuestInfo;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestEvent;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuests;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;

import org.jspecify.annotations.Nullable;

/**
 * LOTRPlayerData's mini-quests on the client: the player's quests in hand and those done, and the one
 * they follow, as the server last sent them; the offers and offer markers NPCs send; and the events
 * (the map or the factions opened, the alignment cycled) the client tells the server of.
 */
public final class LOTRClientMiniQuests {

    private static final List<LOTRMiniQuest> MINI_QUESTS = new ArrayList<>();
    private static final List<LOTRMiniQuest> MINI_QUESTS_COMPLETED = new ArrayList<>();
    private static @Nullable UUID trackingMiniQuestID;
    private static int completedMiniQuestCount;

    private LOTRClientMiniQuests() {
    }

    public static List<LOTRMiniQuest> getMiniQuests() {
        return MINI_QUESTS;
    }

    public static List<LOTRMiniQuest> getMiniQuestsCompleted() {
        return MINI_QUESTS_COMPLETED;
    }

    public static List<LOTRMiniQuest> getActiveMiniQuests() {
        List<LOTRMiniQuest> ret = new ArrayList<>();
        for (LOTRMiniQuest quest : MINI_QUESTS) {
            if (quest.isActive()) {
                ret.add(quest);
            }
        }
        return ret;
    }

    public static boolean anyActiveQuestsFor(LOTRNPCEntity npc) {
        for (LOTRMiniQuest quest : MINI_QUESTS) {
            if (quest.isActive() && quest.entityUUID.equals(npc.getUUID())) {
                return true;
            }
        }
        return false;
    }

    public static @Nullable LOTRMiniQuest getMiniQuestForID(UUID id, boolean completed) {
        for (LOTRMiniQuest quest : completed ? MINI_QUESTS_COMPLETED : MINI_QUESTS) {
            if (quest.questUUID.equals(id)) {
                return quest;
            }
        }
        return null;
    }

    public static @Nullable LOTRMiniQuest getTrackingMiniQuest() {
        return trackingMiniQuestID == null ? null : getMiniQuestForID(trackingMiniQuestID, false);
    }

    /** setTrackingMiniQuest, told to the server. */
    public static void setTrackingMiniQuest(@Nullable LOTRMiniQuest quest) {
        trackingMiniQuestID = quest == null ? null : quest.questUUID;
        ClientPlayNetworking.send(new LOTRMiniQuests.TrackPayload(Optional.ofNullable(trackingMiniQuestID)));
    }

    public static int getCompletedMiniQuestsTotal() {
        return completedMiniQuestCount;
    }

    /** LOTRPacketDeleteMiniquest. */
    public static void deleteMiniQuest(LOTRMiniQuest quest) {
        ClientPlayNetworking.send(new LOTRMiniQuests.DeletePayload(quest.questUUID, quest.isCompleted()));
    }

    /** LOTRPacketClientMQEvent: the map or the factions screen opened. */
    public static void sendEvent(LOTRMiniQuestEvent event) {
        if (ClientPlayNetworking.canSend(LOTRMiniQuests.ClientEventPayload.TYPE)) {
            ClientPlayNetworking.send(new LOTRMiniQuests.ClientEventPayload(event));
        }
    }

    private static HolderLookup.Provider registries() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level != null ? mc.level.registryAccess() : mc.getConnection().registryAccess();
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRMiniQuests.QuestPayload.TYPE, (payload, context) -> {
            LOTRMiniQuest quest = LOTRMiniQuest.loadQuest(payload.quest(), registries(), null);
            if (quest == null) {
                return;
            }
            LOTRMiniQuest existing = getMiniQuestForID(quest.questUUID, payload.completed());
            if (existing == null) {
                (payload.completed() ? MINI_QUESTS_COMPLETED : MINI_QUESTS).add(quest);
            } else {
                existing.readFromNBT(payload.quest(), registries());
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(LOTRMiniQuests.RemovePayload.TYPE, (payload, context) -> {
            LOTRMiniQuest quest = getMiniQuestForID(payload.questID(), payload.wasCompleted());
            if (quest == null) {
                LOTRMod.LOGGER.warn("Tried to remove a LOTR miniquest that doesn't exist");
                return;
            }
            (payload.wasCompleted() ? MINI_QUESTS_COMPLETED : MINI_QUESTS).remove(quest);
            if (payload.addToCompleted()) {
                MINI_QUESTS_COMPLETED.add(quest);
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(LOTRMiniQuests.TrackClientPayload.TYPE, (payload, context) ->
                trackingMiniQuestID = payload.questID().orElse(null));
        ClientPlayNetworking.registerGlobalReceiver(LOTRMiniQuests.CompletedCountPayload.TYPE, (payload, context) ->
                completedMiniQuestCount = payload.count());
        ClientPlayNetworking.registerGlobalReceiver(LOTRNPCQuestInfo.OfferPayload.TYPE, (payload, context) -> {
            if (context.client().level != null
                    && context.client().level.getEntity(payload.npcId()) instanceof LOTRNPCEntity npc) {
                LOTRMiniQuest quest = LOTRMiniQuest.loadQuest(payload.quest(), registries(), null);
                if (quest != null) {
                    context.client().setScreenAndShow(new LOTRMiniQuestOfferScreen(quest, npc));
                } else {
                    ClientPlayNetworking.send(new LOTRNPCQuestInfo.OfferResponsePayload(npc.getId(), false));
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(LOTRNPCQuestInfo.OfferingPayload.TYPE, (payload, context) -> {
            if (context.client().level != null
                    && context.client().level.getEntity(payload.npcId()) instanceof LOTRNPCEntity npc) {
                npc.questInfo.clientIsOffering = payload.offering();
                npc.questInfo.clientOfferColor = payload.color();
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            MINI_QUESTS.clear();
            MINI_QUESTS_COMPLETED.clear();
            trackingMiniQuestID = null;
            completedMiniQuestCount = 0;
        });
    }
}
