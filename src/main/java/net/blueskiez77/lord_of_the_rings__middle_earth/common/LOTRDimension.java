package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.minecraft.network.chat.Component;

/**
 * Minimal faithful port of 1.7.10 lotr.common.LOTRDimension.
 * DELIBERATELY REDUCED: worldgen dropped, so WorldProvider classes, dimension
 * registration/IDs, biome lists, and achievement categories are omitted. What
 * remains is what factions/alignment/map need: identities, regions, faction lists.
 */
public enum LOTRDimension {
    MIDDLE_EARTH("MiddleEarth", 100, EnumSet.of(DimensionRegion.WEST, DimensionRegion.EAST, DimensionRegion.SOUTH)),
    UTUMNO("Utumno", 500, EnumSet.of(DimensionRegion.REG_UTUMNO));

    public final String dimensionName;
    public final int spawnCap;
    public final Collection<LOTRFaction> factionList = new ArrayList<>();
    public final List<DimensionRegion> dimensionRegions = new ArrayList<>();

    LOTRDimension(String s, int spawns, Collection<DimensionRegion> regions) {
        dimensionName = s;
        spawnCap = spawns;
        dimensionRegions.addAll(regions);
        for (DimensionRegion r : dimensionRegions) {
            r.setDimension(this);
        }
    }

    public static LOTRDimension forName(String s) {
        for (LOTRDimension dim : values()) {
            if (!dim.dimensionName.equals(s)) {
                continue;
            }
            return dim;
        }
        return null;
    }

    public Component getDimensionName() {
        return Component.translatable(getUntranslatedDimensionName());
    }

    public String getUntranslatedDimensionName() {
        return "lotr.dimension." + dimensionName;
    }

    public enum DimensionRegion {
        WEST("west"), EAST("east"), SOUTH("south"), REG_UTUMNO("utumno");

        public final String regionName;
        public LOTRDimension dimension;
        public final List<LOTRFaction> factionList = new ArrayList<>();

        DimensionRegion(String s) {
            regionName = s;
        }

        public static DimensionRegion forID(int ID) {
            for (DimensionRegion r : values()) {
                if (r.ordinal() != ID) {
                    continue;
                }
                return r;
            }
            return null;
        }

        public static DimensionRegion forName(String regionName) {
            for (DimensionRegion r : values()) {
                if (!r.codeName().equals(regionName)) {
                    continue;
                }
                return r;
            }
            return null;
        }

        public String codeName() {
            return regionName;
        }

        public LOTRDimension getDimension() {
            return dimension;
        }

        public void setDimension(LOTRDimension dim) {
            dimension = dim;
        }

        public Component getRegionName() {
            return Component.translatable("lotr.dimension." + dimension.dimensionName + "." + codeName());
        }
    }
}