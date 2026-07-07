package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapCoords;
import com.mojang.serialization.Codec;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Faithful port of 1.7.10 lotr.common.fac.LOTRFaction.
 * Enum constants, fields, and all five constructors are verbatim from source.
 * TODO(port) markers flag deferred subsystems (banners, achievements, player
 * data, april-fools, custom dimension). Control-zone waypoint references are
 * inlined as map coordinates (commented wp:NAME) until waypoints are ported.
 */
public enum LOTRFaction {

    HOBBIT(5885518, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(830, 745, 100), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_MAN)), BREE(11373426, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(925, 735, 50), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_MAN)), RANGER_NORTH(3823170, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1070, 760, 150), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_MAN)), BLUE_MOUNTAINS(6132172, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(650, 600, 125), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_DWARF)), HIGH_ELF(13035007, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(570, 770, 200), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_ELF)), GUNDABAD(9858132, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1160, 670, 150), EnumSet.of(FactionType.TYPE_ORC)), ANGMAR(7836023, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1080, 600, 125), EnumSet.of(FactionType.TYPE_ORC, FactionType.TYPE_TROLL)), WOOD_ELF(3774030, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1400, 640, 75), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_ELF)), DOL_GULDUR(3488580, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1380, 870, 100), EnumSet.of(FactionType.TYPE_ORC)), DALE(13535071, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1530, 670, 100), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_MAN)), DURINS_FOLK(4940162, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1650, 650, 125), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_DWARF)), LOTHLORIEN(15716696, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1230, 900, 75), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_ELF)), DUNLAND(11048079, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1090, 1030, 125), EnumSet.of(FactionType.TYPE_MAN)), ISENGARD(3356723, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1110, 1070, 50), EnumSet.of(FactionType.TYPE_ORC)), FANGORN(4831058, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1200, 1000, 75), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_TREE)), ROHAN(3508007, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1230, 1090, 150), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_MAN)), GONDOR(16382457, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1170, 1300, 300), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_MAN)), MORDOR(3481375, LOTRDimension.DimensionRegion.WEST, new LOTRMapRegion(1620, 1290, 225), EnumSet.of(FactionType.TYPE_ORC)), DORWINION(7155816, LOTRDimension.DimensionRegion.EAST, new LOTRMapRegion(1750, 900, 100), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_MAN, FactionType.TYPE_ELF)), RHUDEL(12882471, LOTRDimension.DimensionRegion.EAST, new LOTRMapRegion(1890, 980, 200), EnumSet.of(FactionType.TYPE_MAN)), NEAR_HARAD(11868955, LOTRDimension.DimensionRegion.SOUTH, new LOTRMapRegion(1400, 1730, 375), EnumSet.of(FactionType.TYPE_MAN)), MORWAITH(14266458, LOTRDimension.DimensionRegion.SOUTH, new LOTRMapRegion(1400, 2360, 450), EnumSet.of(FactionType.TYPE_MAN)), TAURETHRIM(3040066, LOTRDimension.DimensionRegion.SOUTH, new LOTRMapRegion(1250, 2870, 400), EnumSet.of(FactionType.TYPE_FREE, FactionType.TYPE_MAN)), HALF_TROLL(10388339, LOTRDimension.DimensionRegion.SOUTH, new LOTRMapRegion(1900, 2500, 200), EnumSet.of(FactionType.TYPE_MAN, FactionType.TYPE_TROLL)), DARK_HUORN(0, null, null, true, true, -1, null, null), RUFFIAN(0, null, null, true, true, 0, null, null), UTUMNO(3343616, LOTRDimension.UTUMNO, -66666, EnumSet.of(FactionType.TYPE_ORC)), HOSTILE(true, -1), UNALIGNED(false, 0);

    public static final Random factionRand = new Random();
    public static final int CONTROL_ZONE_EXTRA_RANGE = 50;

    /**
     * Codec keyed by faction code name (e.g. "GONDOR"), used by the alignment
     * attachment for persistence. Faithful to the old NBT scheme, which also
     * stored factions by codeName() rather than ordinal.
     */
    public static final Codec<LOTRFaction> CODEC =
            Codec.STRING.xmap(LOTRFaction::forName, LOTRFaction::codeName);

    public LOTRDimension factionDimension;
    public LOTRDimension.DimensionRegion factionRegion;
    public Color factionColor;
    public final Map<Float, float[]> facRGBCache = new HashMap<>();
    public final Collection<FactionType> factionTypes = EnumSet.noneOf(FactionType.class);
    public final Collection<String> factionBanners = new ArrayList<>(); // TODO(port): LOTRItemBanner.BannerType
    public boolean allowPlayer;
    public boolean allowEntityRegistry;
    public boolean hasFixedAlignment;
    public int fixedAlignment;
    public final List<LOTRFactionRank> ranksSortedDescending = new ArrayList<>();
    public LOTRFactionRank pledgeRank;
    // TODO(port): achieveCategory was LOTRAchievement.Category (achievements unported)
    public LOTRMapRegion factionMapInfo;
    public final List<LOTRControlZone> controlZones = new ArrayList<>();
    public boolean isolationist;
    public boolean approvesWarCrimes = true;
    public final List<String> legacyAliases = new ArrayList<>();

    LOTRFaction(boolean registry, int alignment) {
        this(0, null, null, false, registry, alignment, null, null);
    }

    LOTRFaction(int color, LOTRDimension dim, int alignment, Collection<FactionType> types) {
        this(color, dim, dim.dimensionRegions.getFirst(), true, true, alignment, null, types);
    }

    LOTRFaction(int color, LOTRDimension dim, LOTRDimension.DimensionRegion region, boolean player, boolean registry, int alignment, LOTRMapRegion mapInfo, Collection<FactionType> types) {
        allowPlayer = player;
        allowEntityRegistry = registry;
        factionColor = new Color(color);
        factionDimension = dim;
        if (factionDimension != null) {
            factionDimension.factionList.add(this);
        }
        factionRegion = region;
        if (factionRegion != null) {
            factionRegion.factionList.add(this);
            if (factionRegion.getDimension() != factionDimension) {
                throw new IllegalArgumentException("Faction dimension region must agree with faction dimension!");
            }
        }
        if (alignment != Integer.MIN_VALUE) {
            setFixedAlignment(alignment);
        }
        if (mapInfo != null) {
            factionMapInfo = mapInfo;
        }
        if (types != null) {
            factionTypes.addAll(types);
        }
    }

    LOTRFaction(int color, LOTRDimension dim, LOTRDimension.DimensionRegion region, LOTRMapRegion mapInfo, Collection<FactionType> types) {
        this(color, dim, region, true, true, Integer.MIN_VALUE, mapInfo, types);
    }

    LOTRFaction(int color, LOTRDimension.DimensionRegion region, LOTRMapRegion mapInfo, Collection<FactionType> types) {
        this(color, LOTRDimension.MIDDLE_EARTH, region, mapInfo, types);
    }

    public static boolean controlZonesEnabled(Level level) {
        // TODO(port): was LOTRLevelData.enableAlignmentZones() && worldType != MiddleEarthClassic
        return true;
    }

    public static LOTRFaction forID(int ID) {
        for (LOTRFaction f : values()) {
            if (f.ordinal() != ID) {
                continue;
            }
            return f;
        }
        return null;
    }

    public static LOTRFaction forName(String name) {
        for (LOTRFaction f : values()) {
            if (!f.matchesNameOrAlias(name)) {
                continue;
            }
            return f;
        }
        return null;
    }

    public static List<LOTRFaction> getAllHarad() {
        return getAllRegional(LOTRDimension.DimensionRegion.SOUTH);
    }

    public static List<LOTRFaction> getAllOfType(boolean includeUnplayable, FactionType... types) {
        List<LOTRFaction> factions = new ArrayList<>();
        for (LOTRFaction f : values()) {
            if (!includeUnplayable && (!f.allowPlayer || f.hasFixedAlignment)) {
                continue;
            }
            boolean match = false;
            for (FactionType t : types) {
                if (!f.isOfType(t)) {
                    continue;
                }
                match = true;
                break;
            }
            if (!match) {
                continue;
            }
            factions.add(f);
        }
        return factions;
    }

    public static List<LOTRFaction> getAllOfType(FactionType... types) {
        return getAllOfType(false, types);
    }

    public static List<LOTRFaction> getAllRegional(LOTRDimension.DimensionRegion region) {
        List<LOTRFaction> factions = new ArrayList<>();
        for (LOTRFaction f : values()) {
            if (f.factionRegion != region) {
                continue;
            }
            factions.add(f);
        }
        return factions;
    }

    public static List<LOTRFaction> getAllRhun() {
        return getAllRegional(LOTRDimension.DimensionRegion.EAST);
    }

    public static List<String> getPlayableAlignmentFactionNames() {
        List<LOTRFaction> factions = getPlayableAlignmentFactions();
        List<String> names = new ArrayList<>();
        for (LOTRFaction f : factions) {
            names.add(f.codeName());
        }
        return names;
    }

    public static List<LOTRFaction> getPlayableAlignmentFactions() {
        List<LOTRFaction> factions = new ArrayList<>();
        for (LOTRFaction f : values()) {
            if (!f.isPlayableAlignmentFaction()) {
                continue;
            }
            factions.add(f);
        }
        return factions;
    }

    private static boolean isAprilFools() {
        // TODO(port): LOTRMod.isAprilFools() date check
        return false;
    }

    public static void initAllProperties() {
        LOTRFactionRelations.setDefaultRelations(HOBBIT, BREE, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, RANGER_NORTH, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, BLUE_MOUNTAINS, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, HIGH_ELF, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, WOOD_ELF, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, DALE, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, DURINS_FOLK, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, LOTHLORIEN, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, ROHAN, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, GONDOR, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(BREE, RANGER_NORTH, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(BREE, BLUE_MOUNTAINS, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(BREE, HIGH_ELF, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(BREE, WOOD_ELF, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(BREE, DALE, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(BREE, DURINS_FOLK, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(BREE, LOTHLORIEN, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, HIGH_ELF, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, WOOD_ELF, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, LOTHLORIEN, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, ROHAN, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, GONDOR, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(BLUE_MOUNTAINS, DURINS_FOLK, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, WOOD_ELF, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, LOTHLORIEN, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, FANGORN, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, GONDOR, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, ANGMAR, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, DOL_GULDUR, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, MORDOR, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(ANGMAR, DOL_GULDUR, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(ANGMAR, MORDOR, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(WOOD_ELF, LOTHLORIEN, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(WOOD_ELF, FANGORN, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(WOOD_ELF, DORWINION, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(DOL_GULDUR, MORDOR, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(DALE, DURINS_FOLK, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(DALE, ROHAN, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(DALE, GONDOR, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(DURINS_FOLK, DUNLAND, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(LOTHLORIEN, FANGORN, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(DUNLAND, ISENGARD, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(ISENGARD, HALF_TROLL, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(FANGORN, TAURETHRIM, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(ROHAN, GONDOR, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(MORDOR, RHUDEL, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(MORDOR, NEAR_HARAD, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(MORDOR, MORWAITH, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(MORDOR, HALF_TROLL, LOTRFactionRelations.Relation.ALLY);
        LOTRFactionRelations.setDefaultRelations(NEAR_HARAD, MORWAITH, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(NEAR_HARAD, HALF_TROLL, LOTRFactionRelations.Relation.FRIEND);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, GUNDABAD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, ANGMAR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, DOL_GULDUR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, ISENGARD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HOBBIT, DARK_HUORN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BREE, GUNDABAD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BREE, ANGMAR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BREE, DOL_GULDUR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BREE, ISENGARD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BREE, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BREE, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BREE, DARK_HUORN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, GUNDABAD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, ANGMAR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, DOL_GULDUR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, DUNLAND, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, ISENGARD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, RHUDEL, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, NEAR_HARAD, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, MORWAITH, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(RANGER_NORTH, DARK_HUORN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BLUE_MOUNTAINS, GUNDABAD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BLUE_MOUNTAINS, ANGMAR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BLUE_MOUNTAINS, DOL_GULDUR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BLUE_MOUNTAINS, ISENGARD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BLUE_MOUNTAINS, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(BLUE_MOUNTAINS, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, GUNDABAD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, ANGMAR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, DOL_GULDUR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, ISENGARD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, RHUDEL, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, NEAR_HARAD, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(HIGH_ELF, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, WOOD_ELF, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, DALE, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, DURINS_FOLK, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, LOTHLORIEN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, FANGORN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, ROHAN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, GONDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GUNDABAD, DORWINION, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ANGMAR, WOOD_ELF, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ANGMAR, DALE, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ANGMAR, DURINS_FOLK, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ANGMAR, LOTHLORIEN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ANGMAR, FANGORN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ANGMAR, ROHAN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ANGMAR, GONDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ANGMAR, DORWINION, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(WOOD_ELF, DOL_GULDUR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(WOOD_ELF, ISENGARD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(WOOD_ELF, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(WOOD_ELF, RHUDEL, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(WOOD_ELF, NEAR_HARAD, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(WOOD_ELF, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DOL_GULDUR, DALE, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DOL_GULDUR, DURINS_FOLK, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DOL_GULDUR, LOTHLORIEN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DOL_GULDUR, FANGORN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DOL_GULDUR, ROHAN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DOL_GULDUR, GONDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DOL_GULDUR, DORWINION, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DALE, ISENGARD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DALE, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DALE, RHUDEL, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(DALE, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DURINS_FOLK, ISENGARD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DURINS_FOLK, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DURINS_FOLK, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(LOTHLORIEN, ISENGARD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(LOTHLORIEN, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(LOTHLORIEN, RHUDEL, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(LOTHLORIEN, NEAR_HARAD, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(LOTHLORIEN, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DUNLAND, ROHAN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DUNLAND, GONDOR, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(ISENGARD, FANGORN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ISENGARD, ROHAN, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ISENGARD, GONDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ISENGARD, DORWINION, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(FANGORN, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(FANGORN, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ROHAN, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ROHAN, RHUDEL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(ROHAN, NEAR_HARAD, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(ROHAN, MORWAITH, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(ROHAN, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GONDOR, MORDOR, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GONDOR, RHUDEL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GONDOR, NEAR_HARAD, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(GONDOR, MORWAITH, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(GONDOR, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(MORDOR, DORWINION, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(MORDOR, TAURETHRIM, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(DORWINION, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(NEAR_HARAD, TAURETHRIM, LOTRFactionRelations.Relation.ENEMY);
        LOTRFactionRelations.setDefaultRelations(MORWAITH, TAURETHRIM, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        LOTRFactionRelations.setDefaultRelations(TAURETHRIM, HALF_TROLL, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        for (LOTRFaction f : values()) {
            if (!f.allowPlayer || f == UTUMNO) {
                continue;
            }
            LOTRFactionRelations.setDefaultRelations(f, UTUMNO, LOTRFactionRelations.Relation.MORTAL_ENEMY);
        }
        HOBBIT.approvesWarCrimes = false;
        HOBBIT.isolationist = true;
        HOBBIT.addControlZone(new LOTRControlZone(820.0, 730.0, 40 /* wp:BYWATER */));
        HOBBIT.addControlZone(new LOTRControlZone(857.0, 734.0, 15 /* wp:BUCKLEBURY */));
        HOBBIT.addControlZone(new LOTRControlZone(858.0, 747.0, 10 /* wp:HAYSEND */));
        HOBBIT.addControlZone(new LOTRControlZone(796.0, 739.0, 35 /* wp:MICHEL_DELVING */));
        HOBBIT.addControlZone(new LOTRControlZone(762.0, 745.0, 10 /* wp:GREENHOLM */));
        HOBBIT.addControlZone(new LOTRControlZone(820.0, 765.0, 30 /* wp:LONGBOTTOM */));
        HOBBIT.addControlZone(new LOTRControlZone(915.0, 734.0, 15 /* wp:BREE */));
        BREE.approvesWarCrimes = false;
        BREE.addControlZone(new LOTRControlZone(915.0, 734.0, 25 /* wp:BREE */));
        BREE.addControlZone(new LOTRControlZone(928.0, 728.0, 20 /* wp:ARCHET */));
        BREE.addControlZone(new LOTRControlZone(950.0, 743.0, 15 /* wp:FORSAKEN_INN */));
        RANGER_NORTH.approvesWarCrimes = false;
        RANGER_NORTH.addControlZone(new LOTRControlZone(820.0, 730.0, 110 /* wp:BYWATER */));
        RANGER_NORTH.addControlZone(new LOTRControlZone(883.0, 802.0, 60 /* wp:SARN_FORD */));
        RANGER_NORTH.addControlZone(new LOTRControlZone(1088.0, 714.0, 110 /* wp:LAST_BRIDGE */));
        RANGER_NORTH.addControlZone(new LOTRControlZone(915.0, 734.0, 100 /* wp:BREE */));
        RANGER_NORTH.addControlZone(new LOTRControlZone(814.0, 661.0, 50 /* wp:ANNUMINAS */));
        RANGER_NORTH.addControlZone(new LOTRControlZone(897.0, 652.0, 50 /* wp:FORNOST */));
        RANGER_NORTH.addControlZone(new LOTRControlZone(1106.0, 589.0, 100 /* wp:MOUNT_GRAM */));
        RANGER_NORTH.addControlZone(new LOTRControlZone(1010.0, 503.0, 60 /* wp:CARN_DUM */));
        RANGER_NORTH.addControlZone(new LOTRControlZone(920.0, 810.0, 60 /* wp:GREENWAY_CROSSROADS */));
        RANGER_NORTH.addControlZone(new LOTRControlZone(979.0, 878.0, 50 /* wp:THARBAD */));
        BLUE_MOUNTAINS.approvesWarCrimes = false;
        BLUE_MOUNTAINS.addControlZone(new LOTRControlZone(622.0, 600.0, 40 /* wp:BELEGOST */));
        BLUE_MOUNTAINS.addControlZone(new LOTRControlZone(626.0, 636.0, 40 /* wp:NOGROD */));
        BLUE_MOUNTAINS.addControlZone(new LOTRControlZone(641.0, 671.0, 50 /* wp:THORIN_HALLS */));
        BLUE_MOUNTAINS.addControlZone(new LOTRControlZone(695.0, 820.0, 80));
        HIGH_ELF.approvesWarCrimes = false;
        HIGH_ELF.addControlZone(new LOTRControlZone(679.0, 729.0, 60 /* wp:MITHLOND_SOUTH */));
        HIGH_ELF.addControlZone(new LOTRControlZone(526.0, 718.0, 80 /* wp:FORLOND */));
        HIGH_ELF.addControlZone(new LOTRControlZone(605.0, 783.0, 80 /* wp:HARLOND */));
        HIGH_ELF.addControlZone(new LOTRControlZone(1163.0, 723.0, 50 /* wp:FORD_BRUINEN */));
        GUNDABAD.approvesWarCrimes = true;
        GUNDABAD.addControlZone(new LOTRControlZone(1195.0, 592.0, 200 /* wp:MOUNT_GUNDABAD */));
        GUNDABAD.addControlZone(new LOTRControlZone(1106.0, 589.0, 200 /* wp:MOUNT_GRAM */));
        GUNDABAD.addControlZone(new LOTRControlZone(1220.0, 696.0, 150 /* wp:GOBLIN_TOWN */));
        GUNDABAD.addControlZone(new LOTRControlZone(1166.0, 845.0, 100 /* wp:MOUNT_CARADHRAS */));
        ANGMAR.approvesWarCrimes = true;
        ANGMAR.addControlZone(new LOTRControlZone(1010.0, 503.0, 75 /* wp:CARN_DUM */));
        ANGMAR.addControlZone(new LOTRControlZone(1106.0, 589.0, 125 /* wp:MOUNT_GRAM */));
        ANGMAR.addControlZone(new LOTRControlZone(1130.0, 703.0, 50 /* wp:THE_TROLLSHAWS */));
        WOOD_ELF.approvesWarCrimes = false;
        WOOD_ELF.addControlZone(new LOTRControlZone(1396.0, 650.0, 75 /* wp:ENCHANTED_RIVER */));
        WOOD_ELF.addControlZone(new LOTRControlZone(1303.0, 655.0, 20 /* wp:FOREST_GATE */));
        WOOD_ELF.addControlZone(new LOTRControlZone(1339.0, 894.0, 30 /* wp:DOL_GULDUR */));
        DOL_GULDUR.approvesWarCrimes = true;
        DOL_GULDUR.addControlZone(new LOTRControlZone(1339.0, 894.0, 125 /* wp:DOL_GULDUR */));
        DOL_GULDUR.addControlZone(new LOTRControlZone(1396.0, 650.0, 75 /* wp:ENCHANTED_RIVER */));
        DALE.approvesWarCrimes = false;
        DALE.addControlZone(new LOTRControlZone(1567.0, 680.0, 175 /* wp:DALE_CROSSROADS */));
        DURINS_FOLK.approvesWarCrimes = false;
        DURINS_FOLK.addControlZone(new LOTRControlZone(1463.0, 609.0, 75 /* wp:EREBOR */));
        DURINS_FOLK.addControlZone(new LOTRControlZone(1588.0, 608.0, 100 /* wp:WEST_PEAK */));
        DURINS_FOLK.addControlZone(new LOTRControlZone(1729.0, 610.0, 75 /* wp:EAST_PEAK */));
        DURINS_FOLK.addControlZone(new LOTRControlZone(1651.0, 690.0, 75 /* wp:REDWATER_FORD */));
        DURINS_FOLK.addControlZone(new LOTRControlZone(1166.0, 845.0, 100 /* wp:MOUNT_CARADHRAS */));
        DURINS_FOLK.addControlZone(new LOTRControlZone(1195.0, 592.0, 100 /* wp:MOUNT_GUNDABAD */));
        DURINS_FOLK.addControlZone(new LOTRControlZone(1262.0, 554.0, 50 /* wp:DAINS_HALLS */));
        LOTHLORIEN.approvesWarCrimes = false;
        LOTHLORIEN.addControlZone(new LOTRControlZone(1242.0, 902.0, 100 /* wp:CARAS_GALADHON */));
        DUNLAND.approvesWarCrimes = true;
        DUNLAND.addControlZone(new LOTRControlZone(1070.0, 1027.0, 125 /* wp:SOUTH_DUNLAND */));
        ISENGARD.approvesWarCrimes = true;
        ISENGARD.addControlZone(new LOTRControlZone(1102.0, 1061.5, 100 /* wp:ISENGARD */));
        ISENGARD.addControlZone(new LOTRControlZone(1190.0, 1148.0, 50 /* wp:EDORAS */));
        FANGORN.approvesWarCrimes = false;
        FANGORN.isolationist = true;
        FANGORN.addControlZone(new LOTRControlZone(1180.0, 1005.0, 70));
        ROHAN.approvesWarCrimes = false;
        ROHAN.addControlZone(new LOTRControlZone(1239.0, 1104.0, 150 /* wp:ENTWADE */));
        ROHAN.addControlZone(new LOTRControlZone(1102.0, 1061.5, 100 /* wp:ISENGARD */));
        GONDOR.approvesWarCrimes = false;
        GONDOR.addControlZone(new LOTRControlZone(1419.0, 1247.0, 200 /* wp:MINAS_TIRITH */));
        GONDOR.addControlZone(new LOTRControlZone(1189.0, 1293.0, 125 /* wp:EDHELLOND */));
        GONDOR.addControlZone(new LOTRControlZone(1045.0, 1273.0, 100 /* wp:GREEN_HILLS */));
        GONDOR.addControlZone(new LOTRControlZone(1442.0, 1370.0, 150 /* wp:CROSSINGS_OF_POROS */));
        GONDOR.addControlZone(new LOTRControlZone(1503.0, 1544.0, 75 /* wp:CROSSINGS_OF_HARAD */));
        GONDOR.addControlZone(new LOTRControlZone(1214.0, 1689.0, 150 /* wp:UMBAR_CITY */));
        MORDOR.approvesWarCrimes = true;
        MORDOR.addControlZone(new LOTRControlZone(1573.0, 1196.0, 500 /* wp:BARAD_DUR */));
        DORWINION.approvesWarCrimes = false;
        DORWINION.addControlZone(new LOTRControlZone(1758.0, 939.0, 175 /* wp:DORWINION_COURT */));
        DORWINION.addControlZone(new LOTRControlZone(1657.0, 768.0, 30 /* wp:DALE_PORT */));
        RHUDEL.approvesWarCrimes = false;
        RHUDEL.addControlZone(new LOTRControlZone(1867.0, 984.0, 175 /* wp:RHUN_CAPITAL */));
        RHUDEL.addControlZone(new LOTRControlZone(1419.0, 1247.0, 100 /* wp:MINAS_TIRITH */));
        RHUDEL.addControlZone(new LOTRControlZone(1464.0, 615.0, 50 /* wp:DALE_CITY */));
        NEAR_HARAD.approvesWarCrimes = false;
        NEAR_HARAD.addControlZone(new LOTRControlZone(1214.0, 1689.0, 200 /* wp:UMBAR_CITY */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1169.0, 1821.0, 150 /* wp:FERTILE_VALLEY */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1343.0, 1561.0, 60 /* wp:HARNEN_SEA_TOWN */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1447.0, 1558.0, 60 /* wp:HARNEN_RIVER_TOWN */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1563.0, 1611.0, 50 /* wp:DESERT_TOWN */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1141.0, 1976.0, 50 /* wp:SOUTH_DESERT_TOWN */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1640.0, 1922.0, 150 /* wp:GULF_CITY */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1503.0, 1544.0, 75 /* wp:CROSSINGS_OF_HARAD */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1442.0, 1370.0, 50 /* wp:CROSSINGS_OF_POROS */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1419.0, 1247.0, 50 /* wp:MINAS_TIRITH */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1210.0, 1340.0, 75));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1390.0, 1348.0, 75 /* wp:PELARGIR */));
        NEAR_HARAD.addControlZone(new LOTRControlZone(1292.0, 1342.0, 75 /* wp:LINHIR */));
        MORWAITH.approvesWarCrimes = true;
        MORWAITH.addControlZone(new LOTRControlZone(1462.0, 2452.0, 350 /* wp:GREAT_PLAINS_SOUTH */));
        MORWAITH.addControlZone(new LOTRControlZone(1048.0, 2215.0, 170 /* wp:GREAT_PLAINS_WEST */));
        MORWAITH.addControlZone(new LOTRControlZone(1637.0, 2176.0, 200 /* wp:GREAT_PLAINS_EAST */));
        MORWAITH.addControlZone(new LOTRControlZone(1308.0, 2067.0, 75 /* wp:GREAT_PLAINS_NORTH */));
        TAURETHRIM.approvesWarCrimes = true;
        TAURETHRIM.addControlZone(new LOTRControlZone(1380.0, 2861.0, 400 /* wp:JUNGLE_CITY_CAPITAL */));
        TAURETHRIM.addControlZone(new LOTRControlZone(1834.0, 2523.0, 75 /* wp:OLD_JUNGLE_RUIN */));
        HALF_TROLL.approvesWarCrimes = true;
        HALF_TROLL.addControlZone(new LOTRControlZone(1966.0, 2342.0, 100 /* wp:TROLL_ISLAND */));
        HALF_TROLL.addControlZone(new LOTRControlZone(1897.0, 2605.0, 200 /* wp:BLOOD_RIVER */));
        HALF_TROLL.addControlZone(new LOTRControlZone(1952.0, 2863.0, 100 /* wp:SHADOW_POINT */));
        HALF_TROLL.addControlZone(new LOTRControlZone(1442.0, 1370.0, 40 /* wp:CROSSINGS_OF_POROS */));
        HALF_TROLL.addControlZone(new LOTRControlZone(1621.0, 2673.0, 100 /* wp:HARADUIN_BRIDGE */));
        UTUMNO.approvesWarCrimes = true;
        // TODO(port) achieveCategory: HOBBIT -> SHIRE
        HOBBIT.addRank(10.0f, "guest").makeAchievement().makeTitle();
        HOBBIT.addRank(100.0f, "friend").makeAchievement().makeTitle().setPledgeRank();
        HOBBIT.addRank(250.0f, "hayward").makeAchievement().makeTitle();
        HOBBIT.addRank(500.0f, "bounder").makeAchievement().makeTitle();
        HOBBIT.addRank(1000.0f, "shirriff").makeAchievement().makeTitle();
        HOBBIT.addRank(2000.0f, "chief").makeAchievement().makeTitle();
        HOBBIT.addRank(3000.0f, "thain").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: BREE -> BREE_LAND
        BREE.addRank(10.0f, "guest").makeAchievement().makeTitle();
        BREE.addRank(50.0f, "friend").makeAchievement().makeTitle();
        BREE.addRank(100.0f, "townsman").makeAchievement().makeTitle().setPledgeRank();
        BREE.addRank(200.0f, "trustee").makeAchievement().makeTitle();
        BREE.addRank(500.0f, "champion").makeAchievement().makeTitle();
        BREE.addRank(1000.0f, "captain").makeAchievement().makeTitle();
        BREE.addRank(2000.0f, "master").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: RANGER_NORTH -> ERIADOR
        RANGER_NORTH.addRank(10.0f, "friend").makeAchievement().makeTitle();
        RANGER_NORTH.addRank(50.0f, "warden").makeAchievement().makeTitle();
        RANGER_NORTH.addRank(100.0f, "ranger").makeAchievement().makeTitle().setPledgeRank();
        RANGER_NORTH.addRank(200.0f, "ohtar").makeAchievement().makeTitle();
        RANGER_NORTH.addRank(500.0f, "roquen").makeAchievement().makeTitle();
        RANGER_NORTH.addRank(1000.0f, "champion").makeAchievement().makeTitle();
        RANGER_NORTH.addRank(2000.0f, "captain").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: BLUE_MOUNTAINS -> BLUE_MOUNTAINS
        BLUE_MOUNTAINS.addRank(10.0f, "guest").makeAchievement().makeTitle();
        BLUE_MOUNTAINS.addRank(50.0f, "friend").makeAchievement().makeTitle();
        BLUE_MOUNTAINS.addRank(100.0f, "warden").makeAchievement().makeTitle().setPledgeRank();
        BLUE_MOUNTAINS.addRank(200.0f, "axebearer").makeAchievement().makeTitle();
        BLUE_MOUNTAINS.addRank(500.0f, "champion").makeAchievement().makeTitle();
        BLUE_MOUNTAINS.addRank(1000.0f, "captain").makeAchievement().makeTitle();
        BLUE_MOUNTAINS.addRank(1500.0f, "noble").makeAchievement().makeTitle();
        BLUE_MOUNTAINS.addRank(3000.0f, "lord", true).makeAchievement().makeTitle();
        // TODO(port) achieveCategory: HIGH_ELF -> LINDON
        HIGH_ELF.addRank(10.0f, "guest").makeAchievement().makeTitle();
        HIGH_ELF.addRank(50.0f, "friend").makeAchievement().makeTitle();
        HIGH_ELF.addRank(100.0f, "warrior").makeAchievement().makeTitle().setPledgeRank();
        HIGH_ELF.addRank(200.0f, "herald").makeAchievement().makeTitle();
        HIGH_ELF.addRank(500.0f, "captain").makeAchievement().makeTitle();
        HIGH_ELF.addRank(1000.0f, "noble").makeAchievement().makeTitle();
        HIGH_ELF.addRank(2000.0f, "commander").makeAchievement().makeTitle();
        HIGH_ELF.addRank(3000.0f, "lord", true).makeAchievement().makeTitle();
        // TODO(port) achieveCategory: GUNDABAD -> ERIADOR
        GUNDABAD.addRank(10.0f, "thrall").makeAchievement().makeTitle();
        GUNDABAD.addRank(50.0f, "snaga").makeAchievement().makeTitle();
        GUNDABAD.addRank(100.0f, "raider").makeAchievement().makeTitle().setPledgeRank();
        GUNDABAD.addRank(200.0f, "ravager").makeAchievement().makeTitle();
        GUNDABAD.addRank(500.0f, "scourge").makeAchievement().makeTitle();
        GUNDABAD.addRank(1000.0f, "warlord").makeAchievement().makeTitle();
        GUNDABAD.addRank(2000.0f, "chieftain").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: ANGMAR -> ANGMAR
        ANGMAR.addRank(10.0f, "thrall").makeAchievement().makeTitle();
        ANGMAR.addRank(50.0f, "servant").makeAchievement().makeTitle();
        ANGMAR.addRank(100.0f, "kinsman").makeAchievement().makeTitle().setPledgeRank();
        ANGMAR.addRank(200.0f, "warrior").makeAchievement().makeTitle();
        ANGMAR.addRank(500.0f, "champion").makeAchievement().makeTitle();
        ANGMAR.addRank(1000.0f, "warlord").makeAchievement().makeTitle();
        ANGMAR.addRank(2000.0f, "chieftain").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: WOOD_ELF -> MIRKWOOD
        WOOD_ELF.addRank(50.0f, "guest").makeAchievement().makeTitle();
        WOOD_ELF.addRank(100.0f, "friend").makeAchievement().makeTitle().setPledgeRank();
        WOOD_ELF.addRank(200.0f, "guard").makeAchievement().makeTitle();
        WOOD_ELF.addRank(500.0f, "herald").makeAchievement().makeTitle();
        WOOD_ELF.addRank(1000.0f, "captain").makeAchievement().makeTitle();
        WOOD_ELF.addRank(2000.0f, "noble").makeAchievement().makeTitle();
        WOOD_ELF.addRank(3000.0f, "lord", true).makeAchievement().makeTitle();
        // TODO(port) achieveCategory: DOL_GULDUR -> MIRKWOOD
        DOL_GULDUR.addRank(10.0f, "thrall").makeAchievement().makeTitle();
        DOL_GULDUR.addRank(50.0f, "servant").makeAchievement().makeTitle();
        DOL_GULDUR.addRank(100.0f, "brigand").makeAchievement().makeTitle().setPledgeRank();
        DOL_GULDUR.addRank(200.0f, "torchbearer").makeAchievement().makeTitle();
        DOL_GULDUR.addRank(500.0f, "despoiler").makeAchievement().makeTitle();
        DOL_GULDUR.addRank(1000.0f, "captain").makeAchievement().makeTitle();
        DOL_GULDUR.addRank(2000.0f, "lieutenant").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: DALE -> DALE
        DALE.addRank(10.0f, "guest").makeAchievement().makeTitle();
        DALE.addRank(50.0f, "friend").makeAchievement().makeTitle();
        DALE.addRank(100.0f, "soldier").makeAchievement().makeTitle().setPledgeRank();
        DALE.addRank(200.0f, "herald").makeAchievement().makeTitle();
        DALE.addRank(500.0f, "captain").makeAchievement().makeTitle();
        DALE.addRank(1000.0f, "marshal").makeAchievement().makeTitle();
        DALE.addRank(2000.0f, "lord", true).makeAchievement().makeTitle();
        // TODO(port) achieveCategory: DURINS_FOLK -> IRON_HILLS
        DURINS_FOLK.addRank(10.0f, "guest").makeAchievement().makeTitle();
        DURINS_FOLK.addRank(50.0f, "friend").makeAchievement().makeTitle();
        DURINS_FOLK.addRank(100.0f, "oathfriend").makeAchievement().makeTitle().setPledgeRank();
        DURINS_FOLK.addRank(200.0f, "axebearer").makeAchievement().makeTitle();
        DURINS_FOLK.addRank(500.0f, "champion").makeAchievement().makeTitle();
        DURINS_FOLK.addRank(1000.0f, "commander").makeAchievement().makeTitle();
        DURINS_FOLK.addRank(1500.0f, "lord", true).makeAchievement().makeTitle();
        DURINS_FOLK.addRank(3000.0f, "uzbad", true).makeAchievement().makeTitle();
        // TODO(port) achieveCategory: LOTHLORIEN -> LOTHLORIEN
        LOTHLORIEN.addRank(10.0f, "guest").makeAchievement().makeTitle();
        LOTHLORIEN.addRank(50.0f, "friend").makeAchievement().makeTitle();
        LOTHLORIEN.addRank(100.0f, "warden").makeAchievement().makeTitle().setPledgeRank();
        LOTHLORIEN.addRank(200.0f, "warrior").makeAchievement().makeTitle();
        LOTHLORIEN.addRank(500.0f, "herald", true).makeAchievement().makeTitle();
        LOTHLORIEN.addRank(1000.0f, "captain").makeAchievement().makeTitle();
        LOTHLORIEN.addRank(2000.0f, "noble").makeAchievement().makeTitle();
        LOTHLORIEN.addRank(3000.0f, "lord", true).makeAchievement().makeTitle();
        // TODO(port) achieveCategory: DUNLAND -> DUNLAND
        DUNLAND.addRank(10.0f, "guest").makeAchievement().makeTitle();
        DUNLAND.addRank(50.0f, "kinsman").makeAchievement().makeTitle();
        DUNLAND.addRank(100.0f, "warrior").makeAchievement().makeTitle().setPledgeRank();
        DUNLAND.addRank(200.0f, "bearer").makeAchievement().makeTitle();
        DUNLAND.addRank(500.0f, "avenger").makeAchievement().makeTitle();
        DUNLAND.addRank(1000.0f, "warlord").makeAchievement().makeTitle();
        DUNLAND.addRank(2000.0f, "chieftain").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: ISENGARD -> ROHAN
        ISENGARD.addRank(10.0f, "thrall").makeAchievement().makeTitle();
        ISENGARD.addRank(50.0f, "snaga").makeAchievement().makeTitle();
        ISENGARD.addRank(100.0f, "soldier").makeAchievement().makeTitle().setPledgeRank();
        ISENGARD.addRank(200.0f, "treefeller").makeAchievement().makeTitle();
        ISENGARD.addRank(500.0f, "berserker").makeAchievement().makeTitle();
        ISENGARD.addRank(1000.0f, "corporal").makeAchievement().makeTitle();
        ISENGARD.addRank(1500.0f, "hand").makeAchievement().makeTitle();
        ISENGARD.addRank(3000.0f, "captain").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: FANGORN -> FANGORN
        FANGORN.addRank(10.0f, "newcomer").makeAchievement().makeTitle();
        FANGORN.addRank(50.0f, "friend").makeAchievement().makeTitle();
        FANGORN.addRank(100.0f, "treeherd").makeAchievement().makeTitle().setPledgeRank();
        FANGORN.addRank(250.0f, "master").makeAchievement().makeTitle();
        FANGORN.addRank(500.0f, "elder").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: ROHAN -> ROHAN
        ROHAN.addRank(10.0f, "guest").makeAchievement().makeTitle();
        ROHAN.addRank(50.0f, "footman").makeAchievement().makeTitle();
        ROHAN.addRank(100.0f, "atarms").makeAchievement().makeTitle().setPledgeRank();
        ROHAN.addRank(250.0f, "rider").makeAchievement().makeTitle();
        ROHAN.addRank(500.0f, "esquire").makeAchievement().makeTitle();
        ROHAN.addRank(1000.0f, "captain").makeAchievement().makeTitle();
        ROHAN.addRank(2000.0f, "marshal").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: GONDOR -> GONDOR
        GONDOR.addRank(10.0f, "guest").makeAchievement().makeTitle();
        GONDOR.addRank(50.0f, "friend").makeAchievement().makeTitle();
        GONDOR.addRank(100.0f, "atarms").makeAchievement().makeTitle().setPledgeRank();
        GONDOR.addRank(200.0f, "soldier").makeAchievement().makeTitle();
        GONDOR.addRank(500.0f, "knight").makeAchievement().makeTitle();
        GONDOR.addRank(1000.0f, "champion").makeAchievement().makeTitle();
        GONDOR.addRank(1500.0f, "captain").makeAchievement().makeTitle();
        GONDOR.addRank(3000.0f, "lord", true).makeAchievement().makeTitle();
        // TODO(port) achieveCategory: MORDOR -> MORDOR
        MORDOR.addRank(10.0f, "thrall").makeAchievement().makeTitle();
        MORDOR.addRank(50.0f, "snaga").makeAchievement().makeTitle();
        MORDOR.addRank(100.0f, "brigand").makeAchievement().makeTitle().setPledgeRank();
        MORDOR.addRank(200.0f, "slavedriver").makeAchievement().makeTitle();
        MORDOR.addRank(500.0f, "despoiler").makeAchievement().makeTitle();
        MORDOR.addRank(1000.0f, "captain").makeAchievement().makeTitle();
        MORDOR.addRank(1500.0f, "lieutenant").makeAchievement().makeTitle();
        MORDOR.addRank(3000.0f, "commander").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: DORWINION -> DORWINION
        DORWINION.addRank(10.0f, "guest").makeAchievement().makeTitle();
        DORWINION.addRank(50.0f, "vinehand").makeAchievement().makeTitle();
        DORWINION.addRank(100.0f, "merchant").makeAchievement().makeTitle().setPledgeRank();
        DORWINION.addRank(200.0f, "guard").makeAchievement().makeTitle();
        DORWINION.addRank(500.0f, "captain").makeAchievement().makeTitle();
        DORWINION.addRank(1000.0f, "master").makeAchievement().makeTitle();
        DORWINION.addRank(1500.0f, "chief").makeAchievement().makeTitle();
        DORWINION.addRank(3000.0f, "lord", true).makeAchievement().makeTitle();
        // TODO(port) achieveCategory: RHUDEL -> RHUN
        RHUDEL.addRank(10.0f, "bondsman").makeAchievement().makeTitle();
        RHUDEL.addRank(50.0f, "levyman").makeAchievement().makeTitle();
        RHUDEL.addRank(100.0f, "clansman").makeAchievement().makeTitle().setPledgeRank();
        RHUDEL.addRank(200.0f, "warrior").makeAchievement().makeTitle();
        RHUDEL.addRank(500.0f, "champion").makeAchievement().makeTitle();
        RHUDEL.addRank(1000.0f, "golden").makeAchievement().makeTitle();
        RHUDEL.addRank(1500.0f, "warlord").makeAchievement().makeTitle();
        RHUDEL.addRank(3000.0f, "chieftain").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: NEAR_HARAD -> NEAR_HARAD
        NEAR_HARAD.addRank(10.0f, "guest").makeAchievement().makeTitle();
        NEAR_HARAD.addRank(50.0f, "friend").makeAchievement().makeTitle();
        NEAR_HARAD.addRank(100.0f, "kinsman").makeAchievement().makeTitle().setPledgeRank();
        NEAR_HARAD.addRank(200.0f, "warrior").makeAchievement().makeTitle();
        NEAR_HARAD.addRank(500.0f, "champion").makeAchievement().makeTitle();
        NEAR_HARAD.addRank(1000.0f, "serpentguard").makeAchievement().makeTitle();
        NEAR_HARAD.addRank(1500.0f, "warlord").makeAchievement().makeTitle();
        NEAR_HARAD.addRank(3000.0f, "prince", true).makeAchievement().makeTitle();
        // TODO(port) achieveCategory: MORWAITH -> FAR_HARAD_SAVANNAH
        MORWAITH.addRank(10.0f, "guest").makeAchievement().makeTitle();
        MORWAITH.addRank(50.0f, "friend").makeAchievement().makeTitle();
        MORWAITH.addRank(100.0f, "kinsman").makeAchievement().makeTitle().setPledgeRank();
        MORWAITH.addRank(250.0f, "hunter").makeAchievement().makeTitle();
        MORWAITH.addRank(500.0f, "warrior").makeAchievement().makeTitle();
        MORWAITH.addRank(1000.0f, "chief").makeAchievement().makeTitle();
        MORWAITH.addRank(3000.0f, "greatchief").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: TAURETHRIM -> FAR_HARAD_JUNGLE
        TAURETHRIM.addRank(10.0f, "guest").makeAchievement().makeTitle();
        TAURETHRIM.addRank(50.0f, "friend").makeAchievement().makeTitle();
        TAURETHRIM.addRank(100.0f, "forestman").makeAchievement().makeTitle().setPledgeRank();
        TAURETHRIM.addRank(200.0f, "warrior").makeAchievement().makeTitle();
        TAURETHRIM.addRank(500.0f, "champion").makeAchievement().makeTitle();
        TAURETHRIM.addRank(1000.0f, "warlord").makeAchievement().makeTitle();
        TAURETHRIM.addRank(3000.0f, "splendour").makeAchievement().makeTitle();
        // TODO(port) achieveCategory: HALF_TROLL -> PERDOROGWAITH
        HALF_TROLL.addRank(10.0f, "guest").makeAchievement().makeTitle();
        HALF_TROLL.addRank(50.0f, "scavenger").makeAchievement().makeTitle();
        HALF_TROLL.addRank(100.0f, "kin").makeAchievement().makeTitle().setPledgeRank();
        HALF_TROLL.addRank(200.0f, "warrior").makeAchievement().makeTitle();
        HALF_TROLL.addRank(500.0f, "raider").makeAchievement().makeTitle();
        HALF_TROLL.addRank(1000.0f, "warlord").makeAchievement().makeTitle();
        HALF_TROLL.addRank(2000.0f, "chieftain").makeAchievement().makeTitle();
        DURINS_FOLK.addLegacyAlias("DWARF");
        LOTHLORIEN.addLegacyAlias("GALADHRIM");
        ISENGARD.addLegacyAlias("URUK_HAI");
        RHUDEL.addLegacyAlias("RHUN");
        MORWAITH.addLegacyAlias("MOREDAIN");
        TAURETHRIM.addLegacyAlias("TAUREDAIN");
    }

    public void addControlZone(LOTRControlZone zone) {
        controlZones.add(zone);
    }

    public void addLegacyAlias(String s) {
        legacyAliases.add(s);
    }

    public LOTRFactionRank addRank(float alignment, String name) {
        return addRank(alignment, name, false);
    }

    public LOTRFactionRank addRank(float alignment, String name, boolean gendered) {
        LOTRFactionRank rank = new LOTRFactionRank(this, alignment, name, gendered);
        ranksSortedDescending.add(rank);
        Collections.sort(ranksSortedDescending);
        return rank;
    }

    public int[] calculateFullControlZoneWorldBorders() {
        int xMin = 0;
        int xMax = 0;
        int zMin = 0;
        int zMax = 0;
        boolean first = true;
        for (LOTRControlZone zone : controlZones) {
            int cxMin = zone.xCoord - zone.radiusCoord;
            int cxMax = zone.xCoord + zone.radiusCoord;
            int czMin = zone.zCoord - zone.radiusCoord;
            int czMax = zone.zCoord + zone.radiusCoord;
            if (first) {
                xMin = cxMin;
                xMax = cxMax;
                zMin = czMin;
                zMax = czMax;
                first = false;
                continue;
            }
            xMin = Math.min(xMin, cxMin);
            xMax = Math.max(xMax, cxMax);
            zMin = Math.min(zMin, czMin);
            zMax = Math.max(zMax, czMax);
        }
        return new int[]{xMin, xMax, zMin, zMax};
    }

    public String codeName() {
        return name();
    }

    public double distanceToNearestControlZoneInRange(Level level, double d, double d1, double d2, int mapRange) {
        double closestDist = -1.0;
        if (isFactionDimension(level)) {
            int coordRange = LOTRMapCoords.mapToWorldR(mapRange);
            for (LOTRControlZone zone : controlZones) {
                double dx = d - zone.xCoord;
                double dz = d2 - zone.zCoord;
                double dSq = dx * dx + dz * dz;
                double dToEdge = Math.sqrt(dSq) - zone.radiusCoord;
                if (dToEdge > coordRange || closestDist >= 0.0 && dToEdge >= closestDist) {
                    continue;
                }
                closestDist = dToEdge;
            }
        }
        return closestDist;
    }

    public Component factionEntityName() {
        return Component.translatable("lotr.faction." + codeName() + ".entity");
    }

    public Component factionName() {
        if (isAprilFools()) {
            String[] names = {"Britain Stronger in Europe", "Vote Leave"};
            int i = ordinal();
            i = (int) (i + (i ^ 0xF385L) + 28703L * (i * i ^ 0x30C087L));
            factionRand.setSeed(i);
            List<String> list = Arrays.asList(names);
            Collections.shuffle(list, factionRand);
            return Component.literal(list.getFirst());
        }
        return Component.translatable(untranslatedFactionName());
    }

    public Component factionSubtitle() {
        return Component.translatable("lotr.faction." + codeName() + ".subtitle");
    }

    public List<LOTRFaction> getBonusesForKilling() {
        List<LOTRFaction> list = new ArrayList<>();
        for (LOTRFaction f : values()) {
            if (f == this || !isBadRelation(f)) {
                continue;
            }
            list.add(f);
        }
        return list;
    }

    public List<LOTRFaction> getConquestBoostRelations() {
        List<LOTRFaction> list = new ArrayList<>();
        for (LOTRFaction f : values()) {
            if (f == this || !f.isPlayableAlignmentFaction() || LOTRFactionRelations.getRelations(this, f) != LOTRFactionRelations.Relation.ALLY) {
                continue;
            }
            list.add(f);
        }
        return list;
    }

    public float getControlZoneAlignmentMultiplier(Player player) {
        int reducedRange;
        double dist;
        if (inControlZone(player)) {
            return 1.0f;
        }
        if (isFactionDimension(player.level()) && (dist = distanceToNearestControlZoneInRange(player.level(), player.getX(), player.getBoundingBox().minY, player.getZ(), reducedRange = getControlZoneReducedRange())) >= 0.0) {
            double mapDist = LOTRMapCoords.worldToMapR(dist);
            float frac = (float) mapDist / reducedRange;
            float mplier = 1.0f - frac;
            return Mth.clamp(mplier, 0.0f, 1.0f);
        }
        return 0.0f;
    }

    public int getControlZoneReducedRange() {
        return isolationist ? 0 : 50;
    }

    public List<LOTRControlZone> getControlZones() {
        return controlZones;
    }

    public int getFactionColor() {
        return factionColor.getRGB();
    }

    public float[] getFactionRGB() {
        return getFactionRGB_MinBrightness(0.0f);
    }

    public float[] getFactionRGB_MinBrightness(float minBrightness) {
        float[] rgb = facRGBCache.get(minBrightness);
        if (rgb == null) {
            float[] hsb = Color.RGBtoHSB(factionColor.getRed(), factionColor.getGreen(), factionColor.getBlue(), null);
            hsb[2] = Math.max(hsb[2], minBrightness);
            int alteredColor = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            rgb = new Color(alteredColor).getColorComponents(null);
            facRGBCache.put(minBrightness, rgb);
        }
        return rgb;
    }

    public LOTRFactionRank getFirstRank() {
        if (ranksSortedDescending.isEmpty()) {
            return LOTRFactionRank.RANK_NEUTRAL;
        }
        return ranksSortedDescending.getLast();
    }

    public List<LOTRFaction> getOthersOfRelation(LOTRFactionRelations.Relation rel) {
        List<LOTRFaction> list = new ArrayList<>();
        for (LOTRFaction f : values()) {
            if (f == this || !f.isPlayableAlignmentFaction() || LOTRFactionRelations.getRelations(this, f) != rel) {
                continue;
            }
            list.add(f);
        }
        return list;
    }

    public List<LOTRFaction> getPenaltiesForKilling() {
        List<LOTRFaction> list = new ArrayList<>();
        list.add(this);
        for (LOTRFaction f : values()) {
            if (f == this || !isGoodRelation(f)) {
                continue;
            }
            list.add(f);
        }
        return list;
    }

    public float getPledgeAlignment() {
        if (pledgeRank != null) {
            return pledgeRank.alignment;
        }
        return 0.0f;
    }

    public LOTRFactionRank getPledgeRank() {
        return pledgeRank;
    }

    public void setPledgeRank(LOTRFactionRank rank) {
        if (rank.fac != this) {
            throw new IllegalArgumentException("Incompatible faction!");
        }
        if (pledgeRank != null) {
            throw new IllegalArgumentException("Faction already has a pledge rank!");
        }
        pledgeRank = rank;
    }

    public LOTRFactionRank getRank(float alignment) {
        for (LOTRFactionRank rank : ranksSortedDescending) {
            if (rank.isDummyRank() || alignment < rank.alignment) {
                continue;
            }
            return rank;
        }
        if (alignment >= 0.0f) {
            return LOTRFactionRank.RANK_NEUTRAL;
        }
        return LOTRFactionRank.RANK_ENEMY;
    }

    public LOTRFactionRank getRankAbove(LOTRFactionRank curRank) {
        return getRankNAbove(curRank, 1);
    }

    public LOTRFactionRank getRankBelow(LOTRFactionRank curRank) {
        return getRankNAbove(curRank, -1);
    }

    public LOTRFactionRank getRankNAbove(LOTRFactionRank curRank, int n) {
        if (ranksSortedDescending.isEmpty() || curRank == null) {
            return LOTRFactionRank.RANK_NEUTRAL;
        }
        int index = -1;
        if (curRank.isDummyRank()) {
            index = ranksSortedDescending.size();
        } else if (ranksSortedDescending.contains(curRank)) {
            index = ranksSortedDescending.indexOf(curRank);
        }
        if (index >= 0) {
            index -= n;
            if (index < 0) {
                return ranksSortedDescending.getFirst();
            }
            if (index > ranksSortedDescending.size() - 1) {
                return LOTRFactionRank.RANK_NEUTRAL;
            }
            return ranksSortedDescending.get(index);
        }
        return LOTRFactionRank.RANK_NEUTRAL;
    }

    public boolean inControlZone(Player player) {
        return inControlZone(player.level(), player.getX(), player.getBoundingBox().minY, player.getZ());
    }

    public boolean inControlZone(Level level, double d, double d1, double d2) {
        // TODO(port): original also counted nearby faction NPCs (LOTRNPCSelectForInfluence)
        // as extending the control zone. Restore when NPC entities are ported.
        return inDefinedControlZone(level, d, d1, d2);
    }

    public boolean inDefinedControlZone(Player player) {
        return inDefinedControlZone(player, 0);
    }

    public boolean inDefinedControlZone(Player player, int extraMapRange) {
        return inDefinedControlZone(player.level(), player.getX(), player.getBoundingBox().minY, player.getZ(), extraMapRange);
    }

    public boolean inDefinedControlZone(Level level, double d, double d1, double d2) {
        return inDefinedControlZone(level, d, d1, d2, 0);
    }

    public boolean inDefinedControlZone(Level level, double d, double d1, double d2, int extraMapRange) {
        if (isFactionDimension(level)) {
            if (!controlZonesEnabled(level)) {
                return true;
            }
            for (LOTRControlZone zone : controlZones) {
                if (!zone.inZone(d, d1, d2, extraMapRange)) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public boolean isAlly(LOTRFaction other) {
        LOTRFactionRelations.Relation rel = LOTRFactionRelations.getRelations(this, other);
        return rel == LOTRFactionRelations.Relation.ALLY;
    }

    public boolean isBadRelation(LOTRFaction other) {
        LOTRFactionRelations.Relation rel = LOTRFactionRelations.getRelations(this, other);
        return rel == LOTRFactionRelations.Relation.ENEMY || rel == LOTRFactionRelations.Relation.MORTAL_ENEMY;
    }

    public boolean isFactionDimension(Level level) {
        // TODO(port): was a check that the level is this faction's LOTR dimension.
        // No custom dimension exists yet, so zones currently apply in any level.
        // Revisit if/when the Middle-earth dimension is ported.
        return true;
    }

    public boolean isGoodRelation(LOTRFaction other) {
        LOTRFactionRelations.Relation rel = LOTRFactionRelations.getRelations(this, other);
        return rel == LOTRFactionRelations.Relation.ALLY || rel == LOTRFactionRelations.Relation.FRIEND;
    }

    public boolean isMortalEnemy(LOTRFaction other) {
        LOTRFactionRelations.Relation rel = LOTRFactionRelations.getRelations(this, other);
        return rel == LOTRFactionRelations.Relation.MORTAL_ENEMY;
    }

    public boolean isNeutral(LOTRFaction other) {
        return LOTRFactionRelations.getRelations(this, other) == LOTRFactionRelations.Relation.NEUTRAL;
    }

    public boolean isOfType(FactionType type) {
        return factionTypes.contains(type);
    }

    public boolean isPlayableAlignmentFaction() {
        return allowPlayer && !hasFixedAlignment;
    }

    public List<String> listAliases() {
        return new ArrayList<>(legacyAliases);
    }

    public boolean matchesNameOrAlias(String name) {
        if (codeName().equals(name)) {
            return true;
        }
        for (String alias : legacyAliases) {
            if (!alias.equals(name)) {
                continue;
            }
            return true;
        }
        return false;
    }

    public void setFixedAlignment(int alignment) {
        hasFixedAlignment = true;
        fixedAlignment = alignment;
    }

    public boolean sharesControlZoneWith(LOTRFaction other) {
        return sharesControlZoneWith(other, 0);
    }

    public boolean sharesControlZoneWith(LOTRFaction other, int extraMapRadius) {
        if (other.factionDimension == factionDimension) {
            for (LOTRControlZone zone : controlZones) {
                for (LOTRControlZone otherZone : other.controlZones) {
                    if (!zone.intersectsWith(otherZone, extraMapRadius)) {
                        continue;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public String untranslatedFactionName() {
        return "lotr.faction." + codeName() + ".name";
    }

    public enum FactionType {
        TYPE_FREE, TYPE_ELF, TYPE_MAN, TYPE_DWARF, TYPE_ORC, TYPE_TROLL, TYPE_TREE
    }
}

