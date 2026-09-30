package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.Random;

/** LOTRDrunkenSpeech: letters dropped and hiccups added, each at the given chance. */
public final class LOTRDrunkenSpeech {

    private static final Random RAND = new Random();

    private LOTRDrunkenSpeech() {
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
