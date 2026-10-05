package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredTask;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredInfoPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRNPCSpeechPayload;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import org.jspecify.annotations.Nullable;

/**
 * LOTRSpeechClient and LOTRPacketNPCSpeech.Handler (with the hired-info
 * packet's handler, LOTRPacketHiredInfo): what each NPC is saying,
 * shown over its head for ten seconds ("Immersive Speech"), or else -- and
 * also with "Immersive Speech Chat Logs", and always when the server insists
 * -- in chat as {@code <name> line}.
 */
public final class LOTRSpeechClient {

    public static final int DISPLAY_TIME = 200;

    private static final Map<UUID, TimedSpeech> NPC_SPEECHES = new HashMap<>();

    private LOTRSpeechClient() {
    }

    public static final class TimedSpeech {
        private final String speech;
        private int time;

        private TimedSpeech(String speech, int time) {
            this.speech = speech;
            this.time = time;
        }

        public String getSpeech() {
            return this.speech;
        }

        public float getAge() {
            return (float) this.time / DISPLAY_TIME;
        }
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRNPCSpeechPayload.TYPE, (payload, context) -> {
            if (!(context.client().level.getEntity(payload.entityId()) instanceof LOTRNPCEntity npc)) {
                return;
            }
            if (LOTRConfig.immersiveSpeech) {
                NPC_SPEECHES.put(npc.getUUID(), new TimedSpeech(payload.speech(), DISPLAY_TIME));
            }
            if (!LOTRConfig.immersiveSpeech || LOTRConfig.immersiveSpeechChatLog || payload.forceChatMsg()) {
                context.player().sendSystemMessage(Component.empty()
                        .append(Component.literal("<").append(npc.getName()).append("> ").withStyle(ChatFormatting.YELLOW))
                        .append(Component.literal(payload.speech()).withStyle(ChatFormatting.WHITE)));
            }
        });
        // LOTRPacketHiredInfo.Handler: the unit's basic hired data, for its name tag and coin.
        ClientPlayNetworking.registerGlobalReceiver(LOTRHiredInfoPayload.TYPE, (payload, context) -> {
            if (context.client().level.getEntity(payload.entityId()) instanceof LOTRNPCEntity npc) {
                npc.hiredNPCInfo.receiveBasicData(payload.hiringPlayer().orElse(null),
                        LOTRHiredTask.forID(payload.task()), payload.squadron().isEmpty() ? null : payload.squadron(),
                        payload.xpLevel());
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.isPaused()) {
                return;
            }
            Iterator<TimedSpeech> it = NPC_SPEECHES.values().iterator();
            while (it.hasNext()) {
                if (--it.next().time <= 0) {
                    it.remove();
                }
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> NPC_SPEECHES.clear());
    }

    public static @Nullable TimedSpeech getSpeechFor(LOTRNPCEntity npc) {
        return NPC_SPEECHES.get(npc.getUUID());
    }

    public static void removeSpeech(LOTRNPCEntity npc) {
        NPC_SPEECHES.remove(npc.getUUID());
    }
}
