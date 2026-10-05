package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.util.RandomSource;

/**
 * LOTRNames: the name banks ({@code assets/lotr/names/*.txt}, one name a line)
 * and the name generators the NPCs use. Each faction's generators arrive with
 * its NPCs; the sign, tavern and village names come with the structures
 * (D11).
 */
public final class LOTRNames {

    private static final Map<String, String[]> ALL_NAME_BANKS = new HashMap<>();

    private LOTRNames() {
    }

    /** loadAllNameBanks. Called once from mod init. */
    public static void loadAllNameBanks() {
        for (Map.Entry<String, List<String>> entry : LOTRModTextFiles.readAll("assets/lotr/names").entrySet()) {
            List<String> names = entry.getValue();
            if (names.isEmpty()) {
                LOTRMod.LOGGER.error("LOTR name bank {} is empty!", entry.getKey());
                continue;
            }
            ALL_NAME_BANKS.put(entry.getKey(), names.toArray(new String[0]));
        }
        LOTRMod.LOGGER.info("LOTR: loaded {} name banks", ALL_NAME_BANKS.size());
    }

    public static String[] getNameBank(String name) {
        return ALL_NAME_BANKS.get(name);
    }

    public static boolean nameBankExists(String name) {
        return getNameBank(name) != null;
    }

    public static String getRandomName(String nameBankName, RandomSource rand) {
        String[] bank = ALL_NAME_BANKS.get(nameBankName);
        if (bank != null) {
            return bank[rand.nextInt(bank.length)];
        }
        return "Unnamed";
    }

    // --- Hobbits ----------------------------------------------------------------

    public static String getHobbitForename(RandomSource rand, boolean male) {
        return getRandomName(male ? "hobbit_male" : "hobbit_female", rand);
    }

    public static String getHobbitSurname(RandomSource rand) {
        return getRandomName("hobbit_surname", rand);
    }

    public static String getHobbitName(RandomSource rand, boolean male) {
        return getHobbitForename(rand, male) + " " + getHobbitSurname(rand);
    }

    /** The parent's surname: the part after the first space. */
    private static String surnameOf(String name) {
        return name.substring(name.indexOf(' ') + 1);
    }

    public static String getHobbitChildNameForParent(RandomSource rand, boolean male, LOTRNPCEntity parent) {
        return getHobbitForename(rand, male) + " " + surnameOf(parent.getNPCName());
    }

    /** changeHobbitSurnameForMarriage: the wife takes the husband's surname. */
    public static void changeHobbitSurnameForMarriage(LOTRNPCEntity maleHobbit, LOTRNPCEntity femaleHobbit) {
        String surname = surnameOf(maleHobbit.getNPCName());
        String femaleName = femaleHobbit.getNPCName();
        String femaleFirstName = femaleName.substring(0, femaleName.indexOf(' '));
        femaleHobbit.familyInfo.setName(femaleFirstName + " " + surname);
    }

    public static String[] getHobbitCoupleAndHomeNames(RandomSource rand) {
        String surname = getHobbitSurname(rand);
        return new String[] {getHobbitForename(rand, true) + " " + surname,
                getHobbitForename(rand, false) + " " + surname, surname, getRandomName("hobbit_home", rand)};
    }

    // --- Bree-land -----------------------------------------------------------------

    /** getHobbitSign: a hobbit hole's sign, its two middle lines split at '#' (and once in a thousand, a vote). */
    public static String[] getHobbitSign(RandomSource rand) {
        String[] sign = {"", "", "", ""};
        String[] split = getRandomName("hobbit_sign", rand).split("#");
        sign[1] = split[0];
        sign[2] = split.length < 2 ? "" : split[1];
        if (rand.nextInt(1000) == 0) {
            sign[1] = "Vote";
            sign[2] = "UKIP";
        }
        return sign;
    }

    public static String[] getHobbitTavernName(RandomSource rand) {
        return new String[]{getRandomName("hobbitTavern_prefix", rand), getRandomName("hobbitTavern_suffix", rand)};
    }

    /** getHobbitTavernQuote: a saying for a tavern's sign, one line per '#'. */
    public static String[] getHobbitTavernQuote(RandomSource rand) {
        String[] sign = {"", "", "", ""};
        String[] split = getRandomName("hobbitTavern_quote", rand).split("#");
        for (int l = 0; l < sign.length && l < split.length; ++l) {
            sign[l] = split[l];
        }
        return sign;
    }

    public static String[] getBreeInnName(RandomSource rand) {
        return new String[]{getRandomName("breeInn_prefix", rand), getRandomName("breeInn_suffix", rand)};
    }

    /** getBreeRuffianSign: a ruffians' sign, its two middle lines split at '#'. */
    public static String[] getBreeRuffianSign(RandomSource rand) {
        String[] sign = {"", "", "", ""};
        String[] split = getRandomName("bree_ruffian_sign", rand).split("#");
        sign[1] = split[0];
        sign[2] = split.length < 2 ? "" : split[1];
        return sign;
    }

    /** getDaleBakeryName: the baker's name and a bakery title, for its sign. */
    public static String[] getDaleBakeryName(RandomSource rand, String name) {
        return new String[]{name + "'s", getRandomName("dale_bakery", rand)};
    }

    /** LOTRDate.THIRD_AGE_CURRENT and SECOND_AGE_LENGTH as the original's class loaded them: 1401 S.R. is 3001 T.A. */
    private static final int THIRD_AGE_CURRENT = 3001;
    private static final int SECOND_AGE_LENGTH = 3441;

    /**
     * getRandomVillageDate: a founding date some way back from now, "T.A." or,
     * before the Third Age, "S.A." -- with the age's length written after the
     * year rather than added to it, as the original's string concatenation did.
     */
    public static String getRandomVillageDate(RandomSource rand, int min, int max, int std) {
        double d = Math.abs(rand.nextGaussian());
        int ago = min + (int) Math.round(d * std);
        int date = THIRD_AGE_CURRENT - Math.min(ago, max);
        if (date >= 1) {
            return "T.A. " + date;
        }
        return "S.A. " + date + SECOND_AGE_LENGTH;
    }

    public static String[] getRohanMeadHallName(RandomSource rand) {
        return new String[]{getRandomName("rohanMeadHall_prefix", rand), getRandomName("rohanMeadHall_suffix", rand)};
    }

    /** getRohanVillageName: a village sign -- a welcome, the name, and when it was founded. */
    public static String[] getRohanVillageName(RandomSource rand) {
        String prefix = getRandomName("rohanVillage_prefix", rand);
        String suffix = getRandomName("rohanVillage_suffix", rand);
        if (prefix.endsWith(suffix.substring(0, 1))) {
            suffix = suffix.substring(1);
        }
        String date = getRandomVillageDate(rand, 50, 500, 100);
        return new String[]{"Welcome to", prefix + suffix, "", "est. " + date};
    }

    public static String[] getDunlendingTavernName(RandomSource rand) {
        return new String[]{getRandomName("dunlendingTavern_prefix", rand), getRandomName("dunlendingTavern_suffix", rand)};
    }

    public static String[] getGondorTavernName(RandomSource rand) {
        return new String[]{getRandomName("gondorTavern_prefix", rand), getRandomName("gondorTavern_suffix", rand)};
    }

    /** getGondorVillageName: a village sign -- a welcome, the name, and when it was founded. */
    public static String[] getGondorVillageName(RandomSource rand) {
        String prefix = getRandomName("gondorVillage_prefix", rand);
        String suffix = getRandomName("gondorVillage_suffix", rand);
        if (prefix.endsWith(suffix.substring(0, 1))) {
            suffix = suffix.substring(1);
        }
        String date = getRandomVillageDate(rand, 50, 5000, 1500);
        return new String[]{"Welcome to", prefix + suffix, "", "est. " + date};
    }

    public static String[] getHaradTavernName(RandomSource rand) {
        return new String[]{getRandomName("haradTavern_prefix", rand), getRandomName("haradTavern_suffix", rand)};
    }

    /** getHaradVillageName: a village sign -- a welcome, the name, and when it was founded. */
    public static String[] getHaradVillageName(RandomSource rand) {
        String prefix = getRandomName("haradVillage_prefix", rand);
        String suffix = getRandomName("haradVillage_suffix", rand);
        if (prefix.endsWith(suffix.substring(0, 1))) {
            suffix = suffix.substring(1);
        }
        String date = getRandomVillageDate(rand, 50, 4000, 1000);
        return new String[]{"Welcome to", prefix + suffix, "", "est. " + date};
    }

    public static String[] getRhunTavernName(RandomSource rand) {
        return new String[]{getRandomName("rhunTavern_prefix", rand), getRandomName("rhunTavern_suffix", rand)};
    }

    /** getRhunVillageName: a village sign -- a welcome, the name, and when it was founded. */
    public static String[] getRhunVillageName(RandomSource rand) {
        String prefix = getRandomName("rhunVillage_prefix", rand);
        String suffix = getRandomName("rhunVillage_suffix", rand);
        if (prefix.endsWith(suffix.substring(0, 1))) {
            suffix = suffix.substring(1);
        }
        String date = getRandomVillageDate(rand, 50, 2000, 300);
        return new String[]{"Welcome to", prefix + suffix, "", "est. " + date};
    }

    public static String getDalishName(RandomSource rand, boolean male) {
        return getRandomName(male ? "dale_male" : "dale_female", rand);
    }

    public static String getBreeName(RandomSource rand, boolean male) {
        return getRandomName(male ? "bree_male" : "bree_female", rand) + " " + getRandomName("bree_surname", rand);
    }

    public static String[] getBreeCoupleAndHomeNames(RandomSource rand) {
        String surname = getRandomName("bree_surname", rand);
        return new String[] {getRandomName("bree_male", rand) + " " + surname,
                getRandomName("bree_female", rand) + " " + surname, surname, "House"};
    }

    /** A Bree-hobbit's forename: one time in three a Shire name, otherwise a Bree one. */
    public static String getBreeHobbitForename(RandomSource rand, boolean male) {
        boolean shirelike = rand.nextInt(3) == 0;
        return getRandomName(shirelike ? male ? "hobbit_male" : "hobbit_female" : male ? "bree_male" : "bree_female", rand);
    }

    /** A Bree-hobbit's surname: one time in three a Shire surname, otherwise a Bree one. */
    public static String getBreeHobbitSurname(RandomSource rand) {
        boolean shirelike = rand.nextInt(3) == 0;
        return getRandomName(shirelike ? "hobbit_surname" : "bree_surname", rand);
    }

    public static String getBreeHobbitName(RandomSource rand, boolean male) {
        return getBreeHobbitForename(rand, male) + " " + getBreeHobbitSurname(rand);
    }

    public static String getBreeHobbitChildNameForParent(RandomSource rand, boolean male, LOTRNPCEntity parent) {
        return getBreeHobbitForename(rand, male) + " " + surnameOf(parent.getNPCName());
    }

    public static String[] getBreeHobbitCoupleAndHomeNames(RandomSource rand) {
        String surname = getBreeHobbitSurname(rand);
        return new String[] {getBreeHobbitForename(rand, true) + " " + surname,
                getBreeHobbitForename(rand, false) + " " + surname, surname, getRandomName("hobbit_home", rand)};
    }

    // --- Dunland -------------------------------------------------------------------

    public static String getDunlendingName(RandomSource rand, boolean male) {
        return getRandomName(male ? "dunlending_male" : "dunlending_female", rand);
    }

    // --- Rohan ---------------------------------------------------------------------

    public static String getRohirricName(RandomSource rand, boolean male) {
        return getRandomName(male ? "rohan_male" : "rohan_female", rand);
    }

    // --- Gondor --------------------------------------------------------------------

    public static String getGondorName(RandomSource rand, boolean male) {
        return getRandomName(male ? "gondor_male" : "gondor_female", rand);
    }

    // --- Elves ---------------------------------------------------------------------

    public static String getSindarinName(RandomSource rand, boolean male) {
        return getRandomName(male ? "sindarin_male" : "sindarin_female", rand);
    }

    /** A Quenya name, one time in five with a title after it. */
    public static String getQuenyaName(RandomSource rand, boolean male) {
        StringBuilder name = new StringBuilder(getRandomName(male ? "quenya_male" : "quenya_female", rand));
        if (rand.nextInt(5) == 0) {
            name.append(" ").append(getRandomName("quenya_title", rand));
        }
        return name.toString();
    }

    /** Sindarin or Quenya, each as likely as its share of the two banks' names. */
    public static String getSindarinOrQuenyaName(RandomSource rand, boolean male) {
        String[] sNames = getNameBank(male ? "sindarin_male" : "sindarin_female");
        int i = sNames.length + getNameBank(male ? "quenya_male" : "quenya_female").length;
        if (rand.nextInt(i) < sNames.length) {
            return getSindarinName(rand, male);
        }
        return getQuenyaName(rand, male);
    }

    // --- Dorwinion -----------------------------------------------------------------

    public static String getDorwinionName(RandomSource rand, boolean male) {
        return getRandomName(male ? "dorwinion_male" : "dorwinion_female", rand);
    }

    // --- Dwarves -------------------------------------------------------------------

    /** "Name son of Father" or "Name daughter of Father", the father chosen at random. */
    public static String getDwarfName(RandomSource rand, boolean male) {
        String name = getRandomName(male ? "dwarf_male" : "dwarf_female", rand);
        String parentName = getRandomName("dwarf_male", rand);
        return name + (male ? " son of " : " daughter of ") + parentName;
    }

    /** A dwarf child is named for the father's own first name. */
    public static String getDwarfChildNameForParent(RandomSource rand, boolean male, LOTRNPCEntity parent) {
        String name = getRandomName(male ? "dwarf_male" : "dwarf_female", rand);
        String parentName = parent.getNPCName();
        parentName = parentName.substring(0, parentName.indexOf(' '));
        return name + (male ? " son of " : " daughter of ") + parentName;
    }

    // --- Orcs ----------------------------------------------------------------------

    public static String getOrcName(RandomSource rand) {
        return getRandomName("orc_prefix", rand) + getRandomName("orc_suffix", rand);
    }

    // --- Ents ---------------------------------------------------------------------

    public static String getEntName(RandomSource rand) {
        return getRandomName("ent_prefix", rand) + getRandomName("ent_suffix", rand);
    }

    // --- Trolls -------------------------------------------------------------------

    public static String getTrollName(RandomSource rand) {
        return getRandomName("troll", rand);
    }

    // --- Rhudaur -------------------------------------------------------------------

    public static String getRhudaurName(RandomSource rand, boolean male) {
        return getRandomName(male ? "rhudaur_male" : "rhudaur_female", rand);
    }

    // --- Near Harad ----------------------------------------------------------------

    public static String getHarnennorName(RandomSource rand, boolean male) {
        return getRandomName(male ? "nearHaradrim_male" : "nearHaradrim_female", rand);
    }

    public static String getUmbarName(RandomSource rand, boolean male) {
        return getRandomName(male ? "umbar_male" : "umbar_female", rand);
    }

    public static String getNomadName(RandomSource rand, boolean male) {
        return getRandomName(male ? "nomad_male" : "nomad_female", rand);
    }

    public static String getGulfHaradName(RandomSource rand, boolean male) {
        return getRandomName(male ? "gulf_male" : "gulf_female", rand);
    }

    /** A coast Southron: one time in three an Umbar name, else a Harnennor one. */
    public static String getSouthronCoastName(RandomSource rand, boolean male) {
        if (rand.nextInt(3) == 0) {
            return getUmbarName(rand, male);
        }
        return getHarnennorName(rand, male);
    }

    public static String getRhunicName(RandomSource rand, boolean male) {
        return getRandomName(male ? "rhun_male" : "rhun_female", rand);
    }

    // --- Far Harad -----------------------------------------------------------------

    public static String getMoredainName(RandomSource rand, boolean male) {
        return getRandomName(male ? "moredain_male" : "moredain_female", rand);
    }

    public static String getTauredainName(RandomSource rand, boolean male) {
        return getRandomName(male ? "tauredain_male" : "tauredain_female", rand);
    }
}
