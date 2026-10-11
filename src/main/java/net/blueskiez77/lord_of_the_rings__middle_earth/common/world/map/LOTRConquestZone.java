package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

/**
 * LOTRConquestZone: one square of the conquest grid, eight map pixels a side, and each playable
 * faction's conquest strength in it. A strength holds for an hour of world time after the zone last
 * changed, then wears away by one an hour (calcTimeStrReduction).
 * Saved under the original's keys: "X", "Z", "Time" and "<faction>_str".
 */
public class LOTRConquestZone {

    private static List<LOTRFaction> allPlayableFacs;

    public final int gridX;
    public final int gridZ;
    public boolean isDummyZone;
    private float[] conquestStrengths;
    private long lastChangeTime;
    private long isEmptyKey;
    private boolean isLoaded = true;
    private boolean clientSide;

    public LOTRConquestZone(int i, int k) {
        this.gridX = i;
        this.gridZ = k;
        if (allPlayableFacs == null && (allPlayableFacs = LOTRFaction.getPlayableAlignmentFactions()).size() >= 62) {
            throw new RuntimeException("Too many factions! Need to upgrade LOTRConquestZone data format.");
        }
        this.conquestStrengths = new float[allPlayableFacs.size()];
    }

    public static LOTRConquestZone readFromNBT(CompoundTag nbt) {
        short x = nbt.getShortOr("X", (short) 0);
        short z = nbt.getShortOr("Z", (short) 0);
        long time = nbt.getLongOr("Time", 0L);
        LOTRConquestZone zone = new LOTRConquestZone(x, z);
        zone.isLoaded = false;
        zone.lastChangeTime = time;
        facs:
        for (LOTRFaction fac : allPlayableFacs) {
            Collection<String> nameAndAliases = new ArrayList<>();
            nameAndAliases.add(fac.codeName());
            nameAndAliases.addAll(fac.listAliases());
            for (String alias : nameAndAliases) {
                String facKey = alias + "_str";
                if (!nbt.contains(facKey)) {
                    continue;
                }
                zone.setConquestStrengthRaw(fac, nbt.getFloatOr(facKey, 0.0f));
                continue facs;
            }
        }
        zone.isLoaded = true;
        return zone;
    }

    public void writeToNBT(CompoundTag nbt) {
        nbt.putShort("X", (short) this.gridX);
        nbt.putShort("Z", (short) this.gridZ);
        nbt.putLong("Time", this.lastChangeTime);
        for (int i = 0; i < this.conquestStrengths.length; ++i) {
            float str = this.conquestStrengths[i];
            if (str == 0.0f) {
                continue;
            }
            nbt.putFloat(allPlayableFacs.get(i).codeName() + "_str", str);
        }
    }

    public void addConquestStrength(LOTRFaction fac, float add, Level level) {
        float str = getConquestStrength(fac, level);
        setConquestStrength(fac, str + add, level);
    }

    /** How much every strength has worn away: nothing for an hour after the last change, then one per hour. */
    public float calcTimeStrReduction(long worldTime) {
        int dl = (int) (worldTime - this.lastChangeTime);
        float s = dl / 20.0f;
        float graceCap = 3600.0f;
        if (s > graceCap) {
            float decayRate = 3600.0f;
            return (s - graceCap) / decayRate;
        }
        return 0.0f;
    }

    /** checkForEmptiness: a zone whose strengths have all worn away is cleared. */
    public void checkForEmptiness(Level level) {
        for (LOTRFaction fac : allPlayableFacs) {
            if (getConquestStrength(fac, level) != 0.0f) {
                return;
            }
        }
        this.conquestStrengths = new float[allPlayableFacs.size()];
        this.isEmptyKey = 0L;
        markDirty();
    }

    public void clearAllFactions(Level level) {
        for (LOTRFaction fac : allPlayableFacs) {
            setConquestStrengthRaw(fac, 0.0f);
        }
        this.lastChangeTime = level.getGameTime();
        markDirty();
    }

    public float getConquestStrength(LOTRFaction fac, long worldTime) {
        float str = getConquestStrengthRaw(fac);
        str -= calcTimeStrReduction(worldTime);
        return Math.max(str, 0.0f);
    }

    public float getConquestStrength(LOTRFaction fac, Level level) {
        return getConquestStrength(fac, level.getGameTime());
    }

    public float getConquestStrengthRaw(LOTRFaction fac) {
        if (!fac.isPlayableAlignmentFaction()) {
            return 0.0f;
        }
        return this.conquestStrengths[allPlayableFacs.indexOf(fac)];
    }

    public long getLastChangeTime() {
        return this.lastChangeTime;
    }

    public void setLastChangeTime(long l) {
        this.lastChangeTime = l;
        markDirty();
    }

    public boolean isEmpty() {
        return this.isEmptyKey == 0L;
    }

    public void markDirty() {
        if (this.isLoaded && !this.clientSide) {
            LOTRConquestGrid.markZoneDirty(this);
        }
    }

    public LOTRConquestZone setClientSide() {
        this.clientSide = true;
        return this;
    }

    public void setConquestStrength(LOTRFaction fac, float str, Level level) {
        setConquestStrengthRaw(fac, str);
        updateAllOtherFactions(fac, level);
        this.lastChangeTime = level.getGameTime();
        markDirty();
    }

    public void setConquestStrengthRaw(LOTRFaction fac, float str) {
        if (!fac.isPlayableAlignmentFaction()) {
            return;
        }
        if (str < 0.0f) {
            str = 0.0f;
        }
        int index = allPlayableFacs.indexOf(fac);
        this.conquestStrengths[index] = str;
        this.isEmptyKey = str == 0.0f ? this.isEmptyKey & ~(1L << index) : this.isEmptyKey | 1L << index;
        markDirty();
    }

    public LOTRConquestZone setDummyZone() {
        this.isDummyZone = true;
        return this;
    }

    /** updateAllOtherFactions: the others' worn-away strengths made raw, as the change resets the clock. */
    public void updateAllOtherFactions(LOTRFaction fac, Level level) {
        for (int i = 0; i < this.conquestStrengths.length; ++i) {
            LOTRFaction otherFac = allPlayableFacs.get(i);
            if (otherFac == fac || this.conquestStrengths[i] <= 0.0f) {
                continue;
            }
            setConquestStrengthRaw(otherFac, getConquestStrength(otherFac, level));
        }
    }

    @Override
    public String toString() {
        return "LOTRConquestZone: " + this.gridX + ", " + this.gridZ;
    }
}
