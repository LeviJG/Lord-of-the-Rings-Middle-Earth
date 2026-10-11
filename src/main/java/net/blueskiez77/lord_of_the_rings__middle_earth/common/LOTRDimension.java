package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.minecraft.network.chat.Component;

/**
 * LOTRDimension: the mod's dimensions -- their names, NPC spawn caps, biomes, regions and factions.
 * Both are datapack dimensions ({@code lotr:middle_earth} and {@code lotr:utumno}, LOTRWorldGen).
 */
public enum LOTRDimension {
    MIDDLE_EARTH("MiddleEarth", 100, EnumSet.of(DimensionRegion.WEST, DimensionRegion.EAST, DimensionRegion.SOUTH)),
    UTUMNO("Utumno", 500, EnumSet.of(DimensionRegion.REG_UTUMNO));

    public final String dimensionName;
    public final int spawnCap;
    /** Its biomes by id, and by their colour on the map (as ARGB), and those it counts as major. */
    public final LOTRBiome[] biomeList = new LOTRBiome[256];
    public final Map<Integer, Integer> colorsToBiomeIDs = new HashMap<>();
    public final List<LOTRBiome> majorBiomes = new ArrayList<>();
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

    /** getCurrentDimension: the mod's dimension the level is, or none. */
    public static @org.jspecify.annotations.Nullable LOTRDimension getCurrentDimension(net.minecraft.world.level.@org.jspecify.annotations.Nullable Level level) {
        if (level == null) {
            return null;
        }
        if (level.dimension() == net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen.MIDDLE_EARTH) {
            return MIDDLE_EARTH;
        }
        if (level.dimension() == net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen.UTUMNO) {
            return UTUMNO;
        }
        return null;
    }

    /** getCurrentDimensionWithFallback: Middle-earth for any level that is not one of the mod's. */
    public static LOTRDimension getCurrentDimensionWithFallback(net.minecraft.world.level.@org.jspecify.annotations.Nullable Level level) {
        LOTRDimension dim = getCurrentDimension(level);
        return dim == null ? MIDDLE_EARTH : dim;
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