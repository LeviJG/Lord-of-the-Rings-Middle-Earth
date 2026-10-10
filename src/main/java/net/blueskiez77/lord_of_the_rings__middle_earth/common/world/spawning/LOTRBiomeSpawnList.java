package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBiomeSpawnList: a biome's NPCs -- its factions, each weighted, each with its own spawn lists
 * (weighted, some only where the faction has conquered) -- and how fast conquest is won there.
 *
 * <p>NOT ported yet: conquest (LOTRConquestGrid), so each faction's conquest strength is nil and
 * conquest-only lists never come (D14).
 */
public class LOTRBiomeSpawnList {

    public final String biomeIdentifier;
    public final Collection<FactionContainer> factionContainers = new ArrayList<>();
    public final Collection<LOTRFaction> presentFactions = new ArrayList<>();
    public float conquestGainRate = 1.0f;

    public LOTRBiomeSpawnList(String biomeIdentifier) {
        this.biomeIdentifier = biomeIdentifier;
    }

    public static SpawnListContainer entry(LOTRSpawnList list) {
        return entry(list, 1);
    }

    public static SpawnListContainer entry(LOTRSpawnList list, int weight) {
        return new SpawnListContainer(list, weight);
    }

    public void clear() {
        this.factionContainers.clear();
        this.presentFactions.clear();
        this.conquestGainRate = 1.0f;
    }

    /** containsEntityClassByDefault: whether any of the non-conquest lists holds an NPC of the class. */
    public boolean containsEntityClassByDefault(Class<? extends Entity> desiredClass, Level level) {
        determineFactions(level);
        for (FactionContainer facCont : this.factionContainers) {
            if (facCont.isEmpty() || facCont.isConquestFaction()) {
                continue;
            }
            for (SpawnListContainer listCont : facCont.spawnLists) {
                for (LOTRSpawnEntry e : listCont.spawnList.spawnList) {
                    Entity entity = e.type().create(level, EntitySpawnReason.NATURAL);
                    boolean matches = desiredClass.isInstance(entity);
                    if (entity != null) {
                        entity.discard();
                    }
                    if (matches) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void determineFactions(Level level) {
        if (this.presentFactions.isEmpty() && !this.factionContainers.isEmpty()) {
            for (FactionContainer facContainer : this.factionContainers) {
                facContainer.determineFaction(level);
                LOTRFaction fac = facContainer.theFaction;
                if (!this.presentFactions.contains(fac)) {
                    this.presentFactions.add(fac);
                }
            }
        }
    }

    public List<LOTRSpawnEntry> getAllSpawnEntries(Level level) {
        determineFactions(level);
        List<LOTRSpawnEntry> spawns = new ArrayList<>();
        for (FactionContainer facCont : this.factionContainers) {
            for (SpawnListContainer listCont : facCont.spawnLists) {
                spawns.addAll(listCont.spawnList.getReadOnlyList());
            }
        }
        return spawns;
    }

    /** getRandomSpawnEntry: a faction by weight (conquest adding to it), then one of its lists, then an entry. */
    public LOTRSpawnEntry.@Nullable Instance getRandomSpawnEntry(RandomSource rand, Level level, int i, int j, int k) {
        determineFactions(level);
        int totalWeight = 0;
        Map<FactionContainer, Integer> cachedFacWeights = new HashMap<>();
        Map<FactionContainer, Float> cachedConqStrengths = new HashMap<>();
        for (FactionContainer cont : this.factionContainers) {
            if (cont.isEmpty()) {
                continue;
            }
            float conq = cont.getEffectiveConquestStrength(level, i, k);
            int weight = cont.getFactionWeight(conq);
            if (weight <= 0) {
                continue;
            }
            totalWeight += weight;
            cachedFacWeights.put(cont, weight);
            cachedConqStrengths.put(cont, conq);
        }
        if (totalWeight > 0) {
            FactionContainer chosenFacContainer = null;
            boolean isConquestSpawn = false;
            int w = rand.nextInt(totalWeight);
            for (FactionContainer cont : this.factionContainers) {
                if (cont.isEmpty() || !cachedFacWeights.containsKey(cont)) {
                    continue;
                }
                int facWeight = cachedFacWeights.get(cont);
                if ((w -= facWeight) >= 0) {
                    continue;
                }
                chosenFacContainer = cont;
                if (facWeight > cont.baseWeight) {
                    isConquestSpawn = rand.nextFloat() < (float) (facWeight - cont.baseWeight) / facWeight;
                }
                break;
            }
            if (chosenFacContainer != null) {
                float conq = cachedConqStrengths.get(chosenFacContainer);
                SpawnListContainer spawnList = chosenFacContainer.getRandomSpawnList(rand, conq);
                if (spawnList == null) {
                    LOTRMod.LOGGER.warn("WARNING NPE in {}, {}", this.biomeIdentifier, chosenFacContainer.theFaction);
                    return null;
                }
                return new LOTRSpawnEntry.Instance(spawnList.spawnList.getRandomSpawnEntry(rand), spawnList.spawnChance, isConquestSpawn);
            }
        }
        return null;
    }

    public boolean isFactionPresent(Level level, LOTRFaction fac) {
        determineFactions(level);
        return this.presentFactions.contains(fac);
    }

    public FactionContainer newFactionList(int w) {
        return newFactionList(w, 1.0f);
    }

    public FactionContainer newFactionList(int w, float conq) {
        FactionContainer cont = new FactionContainer(this, w);
        cont.conquestSensitivity = conq;
        this.factionContainers.add(cont);
        return cont;
    }

    public static class FactionContainer {
        public final LOTRBiomeSpawnList parent;
        public @Nullable LOTRFaction theFaction;
        public final Collection<SpawnListContainer> spawnLists = new ArrayList<>();
        public final int baseWeight;
        public float conquestSensitivity = 1.0f;

        public FactionContainer(LOTRBiomeSpawnList parent, int baseWeight) {
            this.parent = parent;
            this.baseWeight = baseWeight;
        }

        public void add(SpawnListContainer... lists) {
            Collections.addAll(this.spawnLists, lists);
        }

        public void determineFaction(Level level) {
            if (this.theFaction == null) {
                for (SpawnListContainer cont : this.spawnLists) {
                    LOTRFaction fac = cont.spawnList.getListCommonFaction(level);
                    if (this.theFaction == null) {
                        this.theFaction = fac;
                    } else if (fac != this.theFaction) {
                        throw new IllegalArgumentException("Faction containers must include spawn lists of only one faction! Mismatched faction "
                                + fac + " in biome " + this.parent.biomeIdentifier);
                    }
                }
            }
        }

        /** getEffectiveConquestStrength: nil until conquest is ported. */
        public float getEffectiveConquestStrength(Level level, int i, int k) {
            return 0.0f;
        }

        public int getFactionWeight(float conq) {
            if (conq > 0.0f) {
                return this.baseWeight + Math.round(conq * 0.2f * this.conquestSensitivity);
            }
            return this.baseWeight;
        }

        public @Nullable SpawnListContainer getRandomSpawnList(RandomSource rand, float conq) {
            int totalWeight = 0;
            for (SpawnListContainer cont : this.spawnLists) {
                if (cont.canSpawnAtConquestLevel(conq)) {
                    totalWeight += cont.weight;
                }
            }
            if (totalWeight > 0) {
                int w = rand.nextInt(totalWeight);
                for (SpawnListContainer cont : this.spawnLists) {
                    if (cont.canSpawnAtConquestLevel(conq) && (w -= cont.weight) < 0) {
                        return cont;
                    }
                }
            }
            return null;
        }

        public boolean isConquestFaction() {
            return this.baseWeight <= 0;
        }

        public boolean isEmpty() {
            return this.spawnLists.isEmpty();
        }
    }

    public static class SpawnListContainer {
        public final LOTRSpawnList spawnList;
        public final int weight;
        public int spawnChance;
        public float conquestThreshold = -1.0f;

        public SpawnListContainer(LOTRSpawnList spawnList, int weight) {
            this.spawnList = spawnList;
            this.weight = weight;
        }

        public boolean canSpawnAtConquestLevel(float conq) {
            return conq > this.conquestThreshold;
        }

        public boolean isConquestOnly() {
            return this.conquestThreshold >= 0.0f;
        }

        public SpawnListContainer setConquestOnly() {
            return setConquestThreshold(0.0f);
        }

        public SpawnListContainer setConquestThreshold(float f) {
            this.conquestThreshold = f;
            return this;
        }

        public SpawnListContainer setSpawnChance(int i) {
            this.spawnChance = i;
            return this;
        }
    }
}
