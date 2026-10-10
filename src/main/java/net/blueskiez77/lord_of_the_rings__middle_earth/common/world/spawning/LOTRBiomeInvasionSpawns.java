package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

/** LOTRBiomeInvasionSpawns: the invasions that can come in a biome, each at its chance. */
public class LOTRBiomeInvasionSpawns {

    public final String biomeName;
    public final Map<LOTREventSpawner.EventChance, List<LOTRInvasions>> invasionsByChance = new EnumMap<>(LOTREventSpawner.EventChance.class);
    public final Collection<LOTRInvasions> registeredInvasions = new ArrayList<>();

    public LOTRBiomeInvasionSpawns(String biomeName) {
        this.biomeName = biomeName;
    }

    public void addInvasion(LOTRInvasions invasion, LOTREventSpawner.EventChance chance) {
        List<LOTRInvasions> chanceList = getInvasionsForChance(chance);
        if (chanceList.contains(invasion) || this.registeredInvasions.contains(invasion)) {
            LOTRMod.LOGGER.warn("LOTR biome {} already has invasion {} registered", this.biomeName, invasion.codeName());
        } else {
            chanceList.add(invasion);
            this.registeredInvasions.add(invasion);
        }
    }

    public void clearInvasions() {
        this.invasionsByChance.clear();
        this.registeredInvasions.clear();
    }

    public List<LOTRInvasions> getInvasionsForChance(LOTREventSpawner.EventChance chance) {
        return this.invasionsByChance.computeIfAbsent(chance, c -> new ArrayList<>());
    }
}
