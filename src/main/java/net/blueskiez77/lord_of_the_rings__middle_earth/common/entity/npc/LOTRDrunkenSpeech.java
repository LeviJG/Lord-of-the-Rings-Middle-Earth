package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/** LOTRDrunkenSpeech: letters dropped and hiccups added, each at the given chance. */
public final class LOTRDrunkenSpeech {

    private static final Random RAND = new Random();

    private LOTRDrunkenSpeech() {
    }

    /**
     * LOTREventHandler.onServerChat, "Enable Drunken Messages": a nauseous player's chat garbled, more
     * the longer their nausea has to run (up to 0.4 at four minutes). Chat being signed, the garbled
     * text goes out as the server's decoration of it.
     */
    public static void init() {
        ServerMessageDecoratorEvent.EVENT.register(ServerMessageDecoratorEvent.CONTENT_PHASE, (player, message) -> {
            MobEffectInstance nausea;
            if (!LOTRConfig.drunkMessages || player == null || (nausea = player.getEffect(MobEffects.NAUSEA)) == null) {
                return message;
            }
            float chance = Math.min(nausea.getDuration() / 4800.0f, 1.0f) * 0.4f;
            return Component.literal(getDrunkenSpeech(message.getString(), chance));
        });
    }

    public static String getDrunkenSpeech(String speech, float chance) {
        StringBuilder newSpeech = new StringBuilder();
        for (int i = 0; i < speech.length(); ++i) {
            String s = speech.substring(i, i + 1);
            if (RAND.nextFloat() < chance) {
                s = "";
            } else if (RAND.nextFloat() < chance * 0.4f) {
                s = " *hic* ";
            }
            newSpeech.append(s);
        }
        return newSpeech.toString();
    }
}
