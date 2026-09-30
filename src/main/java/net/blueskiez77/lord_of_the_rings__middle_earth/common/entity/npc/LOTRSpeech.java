package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRNPCSpeechPayload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRSpeech: the speech banks ({@code assets/lotr/speech/**.txt}). A bank is
 * a list of lines the NPC picks from at random -- or, if it holds a line
 * reading {@code !RANDOM}, an indexed list read line by line. In a line,
 * {@code #} is the player's name, {@code @} the NPC's home and {@code $} a
 * quest objective; a drunkard slurs it (LOTRDrunkenSpeech). One line in two
 * thousand -- and every line on the first of April -- comes out "Tbh, ...,
 * tbh."
 */
public final class LOTRSpeech {

    private static final Map<String, SpeechBank> ALL_SPEECH_BANKS = new HashMap<>();
    private static final Random RAND = new Random();

    private LOTRSpeech() {
    }

    /** loadAllSpeechBanks. Called once from mod init. */
    public static void loadAllSpeechBanks() {
        for (Map.Entry<String, List<String>> entry : LOTRModTextFiles.readAll("assets/lotr/speech").entrySet()) {
            String name = entry.getKey();
            List<String> speeches = new ArrayList<>();
            boolean random = true;
            for (String line : entry.getValue()) {
                if ("!RANDOM".equals(line)) {
                    random = false;
                } else {
                    speeches.add(line);
                }
            }
            if (speeches.isEmpty()) {
                LOTRMod.LOGGER.error("LOTR speech bank {} is empty!", name);
                continue;
            }
            ALL_SPEECH_BANKS.put(name, random ? new SpeechBank(name, true, speeches)
                    : new SpeechBank(name, false, entry.getValue()));
        }
        LOTRMod.LOGGER.info("LOTR: loaded {} speech banks", ALL_SPEECH_BANKS.size());
    }

    public static SpeechBank getSpeechBank(String name) {
        SpeechBank bank = ALL_SPEECH_BANKS.get(name);
        if (bank != null) {
            return bank;
        }
        return new SpeechBank("dummy_" + name, true,
                Collections.singletonList("Speech bank " + name + " could not be found!"));
    }

    public static String formatSpeech(String speech, @Nullable Player player, @Nullable CharSequence location,
                                      @Nullable CharSequence objective) {
        if (player != null) {
            speech = speech.replace("#", player.getName().getString());
        }
        if (location != null) {
            speech = speech.replace("@", location);
        }
        if (objective != null) {
            speech = speech.replace("$", objective);
        }
        return speech;
    }

    public static String getRandomSpeechForPlayer(LOTRNPCEntity npc, String bank, @Nullable Player player,
                                                  @Nullable CharSequence location, @Nullable CharSequence objective) {
        String s = formatSpeech(getSpeechBank(bank).getRandomSpeech(), player, location, objective);
        return npc.isDrunkard() ? LOTRDrunkenSpeech.getDrunkenSpeech(s, npc.getDrunkenSpeechFactor()) : s;
    }

    public static String getSpeechLineForPlayer(LOTRNPCEntity npc, String bank, int line, @Nullable Player player,
                                                @Nullable CharSequence location, @Nullable CharSequence objective) {
        String s = formatSpeech(getSpeechBank(bank).getSpeechAtLine(line), player, location, objective);
        return npc.isDrunkard() ? LOTRDrunkenSpeech.getDrunkenSpeech(s, npc.getDrunkenSpeechFactor()) : s;
    }

    public static void sendSpeech(Player player, LOTRNPCEntity npc, String speech) {
        sendSpeech(player, npc, speech, false);
    }

    public static void sendSpeech(Player player, LOTRNPCEntity npc, String speech, boolean forceChatMsg) {
        if (player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new LOTRNPCSpeechPayload(npc.getId(), speech, forceChatMsg));
        }
    }

    /** sendSpeechAndChatMessage: the line said aloud and written to chat. */
    public static void sendSpeechAndChatMessage(Player player, LOTRNPCEntity npc, String bank) {
        String speech = getRandomSpeechForPlayer(npc, bank, player, null, null);
        player.sendSystemMessage(Component.literal("<" + npc.getName().getString() + ">").withStyle(ChatFormatting.YELLOW)
                .append(Component.literal(" " + speech).withStyle(ChatFormatting.WHITE)));
        sendSpeech(player, npc, speech);
    }

    public static void sendSpeechBankWithChatMsg(Player player, LOTRNPCEntity npc, String bank) {
        sendSpeech(player, npc, getRandomSpeechForPlayer(npc, bank, player, null, null), true);
    }

    public static final class SpeechBank {
        public final String name;
        public final boolean isRandom;
        public final List<String> speeches;

        SpeechBank(String name, boolean isRandom, List<String> speeches) {
            this.name = name;
            this.isRandom = isRandom;
            this.speeches = speeches;
        }

        public String getRandomSpeech() {
            if (!this.isRandom) {
                return "ERROR: Tried to retrieve random speech from non-random speech bank " + this.name;
            }
            return internalFormatSpeech(this.speeches.get(RAND.nextInt(this.speeches.size())));
        }

        public String getSpeechAtLine(int line) {
            if (this.isRandom) {
                return "ERROR: Tried to retrieve indexed speech from random speech bank " + this.name;
            }
            int index = line - 1;
            if (index >= 0 && index < this.speeches.size()) {
                return internalFormatSpeech(this.speeches.get(index));
            }
            return "ERROR: Speech line " + line + " is out of range!";
        }

        private String internalFormatSpeech(String s) {
            if (s.length() > 1 && RAND.nextInt(2000) == 0) {
                s = "Tbh, " + s.substring(0, 1).toLowerCase(Locale.ROOT) + s.substring(1, s.length() - 1) + ", tbh.";
            }
            return s;
        }
    }
}
