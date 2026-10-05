package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.minecraft.util.RandomSource;

/**
 * OddmentCollectorNameMischief: how the oddment collector garbles a name --
 * up to two letters dropped (from a name longer than three), up to two
 * swapped for another vowel or consonant (keeping the case), and up to one
 * doubled. Formatting codes are left alone.
 */
public final class LOTRNameMischief {

    private static final String VOWELS = "aeiou";
    private static final String CONSONANTS = "bcdfghjklmnopqrstvwxyz";

    private LOTRNameMischief() {
    }

    public static String garbleName(String name, RandomSource rand) {
        int deletes = rand.nextInt(3);
        for (int l = 0; l < deletes; ++l) {
            if (name.length() <= 3) {
                continue;
            }
            int x = rand.nextInt(name.length());
            if (isFormattingCharacter(name, x)) {
                continue;
            }
            name = name.substring(0, x) + name.substring(x + 1);
        }
        int replaces = rand.nextInt(3);
        for (int l = 0; l < replaces; ++l) {
            int x = rand.nextInt(name.length());
            if (isFormattingCharacter(name, x)) {
                continue;
            }
            char c = name.charAt(x);
            if (VOWELS.indexOf(Character.toLowerCase(c)) >= 0) {
                c = sameCase(c, VOWELS.charAt(rand.nextInt(VOWELS.length())));
            } else if (CONSONANTS.indexOf(Character.toLowerCase(c)) >= 0) {
                c = sameCase(c, CONSONANTS.charAt(rand.nextInt(CONSONANTS.length())));
            }
            name = name.substring(0, x) + c + name.substring(x + 1);
        }
        int dupes = rand.nextInt(2);
        for (int l = 0; l < dupes; ++l) {
            int x = rand.nextInt(name.length());
            char c;
            if (isFormattingCharacter(name, x) || !Character.isAlphabetic(c = name.charAt(x))) {
                continue;
            }
            name = name.substring(0, x) + c + c + name.substring(x + 1);
        }
        return name;
    }

    private static char sameCase(char original, char replacement) {
        return Character.isUpperCase(original) ? Character.toUpperCase(replacement) : replacement;
    }

    private static boolean isFormattingCharacter(CharSequence s, int index) {
        return s.charAt(index) == '§' || index >= 1 && s.charAt(index - 1) == '§';
    }
}
