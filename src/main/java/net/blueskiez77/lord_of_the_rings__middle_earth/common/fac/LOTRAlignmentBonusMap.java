package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

/**
 * Faithful port of 1.7.10 lotr.common.fac.LOTRAlignmentBonusMap.
 * Maps factions to the alignment bonus a single action grants each.
 */
public class LOTRAlignmentBonusMap extends HashMap<LOTRFaction, Float> {
    public Set<LOTRFaction> getChangedFactions() {
        Set<LOTRFaction> changed = EnumSet.noneOf(LOTRFaction.class);
        for (LOTRFaction fac : keySet()) {
            float bonus = get(fac);
            if (bonus == 0.0f) {
                continue;
            }
            changed.add(fac);
        }
        return changed;
    }
}