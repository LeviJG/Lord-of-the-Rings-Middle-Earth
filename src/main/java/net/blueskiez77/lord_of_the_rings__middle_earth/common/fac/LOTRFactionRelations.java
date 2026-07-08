package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.network.chat.Component;

/**
 * Faithful port of 1.7.10 lotr.common.fac.LOTRFactionRelations.
 * Relation logic (defaults, overrides, symmetric pair keys, UNALIGNED/HOSTILE
 * special cases) is 1:1. Disk persistence (load/save to faction_relations.dat)
 * and packet broadcasts are stubbed as TODO(port) until the level-datagen and
 * networking slices land. Old NBT format is documented in comments.
 */
public class LOTRFactionRelations {
    public static final Map<FactionPair, Relation> defaultMap = new HashMap<>();
    public static final Map<FactionPair, Relation> overrideMap = new HashMap<>();
    public static boolean needsLoad = true;
    public static boolean needsSave;

    public static Relation getFromDefaultMap(FactionPair key) {
        if (defaultMap.containsKey(key)) {
            return defaultMap.get(key);
        }
        return Relation.NEUTRAL;
    }

    public static Relation getRelations(LOTRFaction f1, LOTRFaction f2) {
        if (f1 == LOTRFaction.UNALIGNED || f2 == LOTRFaction.UNALIGNED) {
            return Relation.NEUTRAL;
        }
        if (f1 == LOTRFaction.HOSTILE || f2 == LOTRFaction.HOSTILE) {
            return Relation.MORTAL_ENEMY;
        }
        if (f1 == f2) {
            return Relation.ALLY;
        }
        FactionPair key = new FactionPair(f1, f2);
        if (overrideMap.containsKey(key)) {
            return overrideMap.get(key);
        }
        return getFromDefaultMap(key);
    }

    public static void load() {
        // TODO(port): read overrides from faction_relations.dat once LOTRLevelData ports.
        // Old format: root compound -> "Overrides" list; each entry has
        // "FacPair1","FacPair2" (faction code names) and "Rel" (relation code name).
        overrideMap.clear();
        needsLoad = false;
    }

    public static void save() {
        // TODO(port): write overrides to faction_relations.dat (format above).
        needsSave = false;
    }

    public static void markDirty() {
        needsSave = true;
    }

    public static boolean needsSave() {
        return needsSave;
    }

    public static void overrideRelations(LOTRFaction f1, LOTRFaction f2, Relation relation) {
        setRelations(f1, f2, relation, false);
    }

    public static void resetAllRelations() {
        boolean wasEmpty = overrideMap.isEmpty();
        overrideMap.clear();
        if (!wasEmpty) {
            markDirty();
            // TODO(port): broadcast reset packet to all players (networking slice).
        }
    }

    public static void setDefaultRelations(LOTRFaction f1, LOTRFaction f2, Relation relation) {
        setRelations(f1, f2, relation, true);
    }

    public static void setRelations(LOTRFaction f1, LOTRFaction f2, Relation relation, boolean isDefault) {
        if (f1 == LOTRFaction.UNALIGNED || f2 == LOTRFaction.UNALIGNED) {
            throw new IllegalArgumentException("Cannot alter UNALIGNED!");
        }
        if (f1 == LOTRFaction.HOSTILE || f2 == LOTRFaction.HOSTILE) {
            throw new IllegalArgumentException("Cannot alter HOSTILE!");
        }
        if (f1 == f2) {
            throw new IllegalArgumentException("Cannot alter a faction's relations with itself!");
        }
        FactionPair key = new FactionPair(f1, f2);
        if (isDefault) {
            if (relation == Relation.NEUTRAL) {
                defaultMap.remove(key);
            } else {
                defaultMap.put(key, relation);
            }
        } else {
            Relation defaultRelation = getFromDefaultMap(key);
            if (relation == defaultRelation) {
                overrideMap.remove(key);
            } else {
                overrideMap.put(key, relation);
            }
            markDirty();
            // TODO(port): broadcast single-entry relation packet (networking slice).
        }
    }

    public enum Relation {
        ALLY, FRIEND, NEUTRAL, ENEMY, MORTAL_ENEMY;

        public static Relation forID(int id) {
            for (Relation rel : values()) {
                if (rel.ordinal() != id) {
                    continue;
                }
                return rel;
            }
            return null;
        }

        public static Relation forName(String name) {
            for (Relation rel : values()) {
                if (!rel.codeName().equals(name)) {
                    continue;
                }
                return rel;
            }
            return null;
        }

        public static List<String> listRelationNames() {
            List<String> names = new ArrayList<>();
            for (Relation rel : values()) {
                names.add(rel.codeName());
            }
            return names;
        }

        public String codeName() {
            return name();
        }

        public Component getDisplayName() {
            return Component.translatable("lotr.faction.rel." + codeName());
        }
    }

    public static class FactionPair {
        public final LOTRFaction fac1;
        public final LOTRFaction fac2;

        public FactionPair(LOTRFaction f1, LOTRFaction f2) {
            fac1 = f1;
            fac2 = f2;
        }

        public LOTRFaction getLeft() {
            return fac1;
        }

        public LOTRFaction getRight() {
            return fac2;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof FactionPair pair) {
                return fac1 == pair.fac1 && fac2 == pair.fac2 || fac1 == pair.fac2 && fac2 == pair.fac1;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int f1 = fac1.ordinal();
            int f2 = fac2.ordinal();
            int lower = Math.min(f1, f2);
            int upper = Math.max(f1, f2);
            return upper << 16 | lower;
        }
    }
}