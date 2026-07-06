package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import net.minecraft.network.chat.Component;

/**
 * Faithful port of 1.7.10 lotr.common.fac.LOTRFactionRank.
 * A named alignment threshold within a faction. Ranks sort descending by alignment.
 * Changes: StatCollector -> Component.translatable; gendered-name methods take a
 * boolean useFeminineRanks until LOTRPlayerData ports; achievement/title hooks stubbed.
 */
public class LOTRFactionRank implements Comparable<LOTRFactionRank> {
    public static final LOTRFactionRank RANK_NEUTRAL = new Dummy("lotr.faction.rank.neutral");
    public static final LOTRFactionRank RANK_ENEMY = new Dummy("lotr.faction.rank.enemy");

    public LOTRFaction fac;
    public float alignment;
    public String name;
    public boolean isGendered;
    public boolean hasRankAchievement;
    public boolean hasRankTitle;

    public LOTRFactionRank(LOTRFaction f, float al, String s, boolean gend) {
        fac = f;
        alignment = al;
        name = s;
        isGendered = gend;
    }

    @Override
    public int compareTo(LOTRFactionRank other) {
        if (fac != other.fac) {
            throw new IllegalArgumentException("Cannot compare two ranks from different factions!");
        }
        float al1 = alignment;
        float al2 = other.alignment;
        if (al1 == al2) {
            throw new IllegalArgumentException("Two ranks cannot have the same alignment value!");
        }
        return -Float.compare(al1, al2);
    }

    public String getCodeName() {
        return "lotr.faction." + fac.codeName() + ".rank." + name;
    }

    public String getCodeNameFem() {
        return getCodeName() + "_fm";
    }

    public String getCodeFullName() {
        return getCodeName() + ".f";
    }

    public String getCodeFullNameFem() {
        return getCodeNameFem() + ".f";
    }

    public String getCodeFullNameWithGender(boolean useFeminineRanks) {
        if (isGendered && useFeminineRanks) {
            return getCodeFullNameFem();
        }
        return getCodeFullName();
    }

    public Component getDisplayName() {
        return Component.translatable(getCodeName());
    }

    public Component getDisplayNameFem() {
        return Component.translatable(getCodeNameFem());
    }

    public Component getDisplayFullName() {
        return Component.translatable(getCodeFullName());
    }

    public Component getDisplayFullNameFem() {
        return Component.translatable(getCodeFullNameFem());
    }

    public Component getFullNameWithGender(boolean useFeminineRanks) {
        if (isGendered && useFeminineRanks) {
            return getDisplayFullNameFem();
        }
        return getDisplayFullName();
    }

    public Component getShortNameWithGender(boolean useFeminineRanks) {
        if (isGendered && useFeminineRanks) {
            return getDisplayNameFem();
        }
        return getDisplayName();
    }

    public boolean isAbovePledgeRank() {
        return alignment > fac.getPledgeAlignment();
    }

    public boolean isDummyRank() {
        return false;
    }

    public boolean isGendered() {
        return isGendered;
    }

    public boolean isPledgeRank() {
        return this == fac.getPledgeRank();
    }

    public LOTRFactionRank makeAchievement() {
        hasRankAchievement = true;
        return this;
    }

    public LOTRFactionRank makeTitle() {
        hasRankTitle = true;
        return this;
    }

    public LOTRFactionRank setPledgeRank() {
        fac.setPledgeRank(this);
        return this;
    }

    public static class Dummy extends LOTRFactionRank {
        public Dummy(String s) {
            super(null, 0.0f, s, false);
        }

        @Override
        public String getCodeName() {
            return name;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable(name);
        }

        @Override
        public Component getDisplayFullName() {
            return getDisplayName();
        }

        @Override
        public boolean isDummyRank() {
            return true;
        }
    }
}