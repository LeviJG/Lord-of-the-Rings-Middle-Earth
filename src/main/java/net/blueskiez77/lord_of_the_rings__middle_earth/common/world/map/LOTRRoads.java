package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Iterators;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.network.chat.Component;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRoads: Middle-earth's roads, drawn through the waypoints and the points
 * between them as Bezier splines, a point a block, in world coordinates. The
 * display-only ones are drawn on the map but not laid in the world.
 *
 * <p>The original built them, every point of them, and its lookup of road
 * points by area, at start-up, at a cost it noted itself (some 200MB). Here
 * a road keeps its curve and works out a point when asked; all of a road's
 * points, and the lookup, are built the first time a place is asked whether
 * a road runs there (world generation, D10).
 */
public final class LOTRRoads {

    private static List<LOTRRoads> allRoads;
    private static List<LOTRRoads> displayOnlyRoads;
    private static RoadPointDatabase roadPointDatabase;

    public final List<RoadPoint> endpoints = new ArrayList<>();
    public final String roadName;
    /** The segment's curve -- from p1 to p2 by the control points -- and how many points (a block apart) it has. */
    private final RoadPoint p1;
    private final RoadPoint cp1;
    private final RoadPoint cp2;
    private final RoadPoint p2;
    private final boolean straight;
    private final int pointCount;
    private RoadPoint @Nullable [] roadPoints;

    private LOTRRoads(String name, RoadPoint p1, RoadPoint cp1, RoadPoint cp2, RoadPoint p2, boolean straight) {
        this.roadName = name;
        Collections.addAll(this.endpoints, p1, p2);
        this.p1 = p1;
        this.cp1 = cp1;
        this.cp2 = cp2;
        this.p2 = p2;
        this.straight = straight;
        double dx = p2.x() - p1.x();
        double dz = p2.z() - p1.z();
        this.pointCount = (int) Math.round(Math.sqrt(dx * dx + dz * dz)) * BezierCurves.ROAD_LENGTH_FACTOR;
    }

    public int pointCount() {
        return this.pointCount;
    }

    /** The road's l-th point, worked out when asked for: the map draws only every few hundredth. */
    public RoadPoint pointAt(int l) {
        double t = (double) l / this.pointCount;
        if (this.straight) {
            return new RoadPoint(this.p1.x() + (this.p2.x() - this.p1.x()) * t,
                    this.p1.z() + (this.p2.z() - this.p1.z()) * t, false);
        }
        return BezierCurves.bezier(this.p1, this.cp1, this.cp2, this.p2, t);
    }

    /** Every point, a block apart, built the first time something needs them all (the road lookup). */
    public synchronized RoadPoint[] roadPoints() {
        if (this.roadPoints == null) {
            RoadPoint[] points = new RoadPoint[this.pointCount];
            for (int l = 0; l < this.pointCount; ++l) {
                points[l] = pointAt(l);
            }
            this.roadPoints = points;
        }
        return this.roadPoints;
    }

    private static synchronized void createRoads() {
        if (allRoads != null) {
            return;
        }
        LOTRMod.LOGGER.info("LOTRRoads: Creating roads (reticulating splines)");
        long time = System.nanoTime();
        allRoads = new ArrayList<>();
        displayOnlyRoads = new ArrayList<>();
        registerRoad("EredLuin", LOTRWaypoint.NOGROD, LOTRWaypoint.BELEGOST);
        registerRoad("NogrodForlond", LOTRWaypoint.NOGROD, LOTRWaypoint.FORLOND);
        registerRoad("NogrodMithlond", LOTRWaypoint.NOGROD, new int[]{654, 650}, LOTRWaypoint.MITHLOND_NORTH);
        registerRoad("Mithlond", LOTRWaypoint.HARLOND, new int[]{658, 755}, LOTRWaypoint.MITHLOND_SOUTH, new int[]{690, 711}, new int[]{681, 705}, LOTRWaypoint.MITHLOND_NORTH, new int[]{644, 733}, new int[]{603, 733}, new int[]{554, 715}, LOTRWaypoint.FORLOND);
        registerRoad("WestEast", LOTRWaypoint.MITHLOND_SOUTH, LOTRWaypoint.TOWER_HILLS, LOTRWaypoint.GREENHOLM, LOTRWaypoint.MICHEL_DELVING, LOTRWaypoint.WAYMEET, LOTRWaypoint.BYWATER, LOTRWaypoint.FROGMORTON, LOTRWaypoint.WHITFURROWS, LOTRWaypoint.BRANDYWINE_BRIDGE, new int[]{870, 718}, new int[]{902, 729}, LOTRWaypoint.BREE);
        registerRoad("WestEast", LOTRWaypoint.BREE, new double[]{LOTRWaypoint.BREE.getX() + 0.5, LOTRWaypoint.BREE.getY()});
        registerRoad("WestEast", new double[]{LOTRWaypoint.BREE.getX() + 2.0, LOTRWaypoint.BREE.getY() + 1.5}, new double[]{LOTRWaypoint.STADDLE.getX(), LOTRWaypoint.STADDLE.getY() + 5.0}, LOTRWaypoint.FORSAKEN_INN, new double[]{LOTRWaypoint.WEATHERTOP.getX(), LOTRWaypoint.WEATHERTOP.getY() + 2.0}, LOTRWaypoint.LAST_BRIDGE, new int[]{1132, 723}, new int[]{1178, 704}, LOTRWaypoint.HIGH_PASS, LOTRWaypoint.OLD_FORD, LOTRWaypoint.RIVER_GATE, LOTRWaypoint.DALE_CROSSROADS, LOTRWaypoint.REDWATER_FORD, new int[]{1785, 775}, LOTRWaypoint.RHUN_NORTH_FORD, LOTRWaypoint.RHUN_NORTHEAST, LOTRWaypoint.RHUN_ROAD_WAY, LOTRWaypoint.BARAZ_DUM);
        registerRoad("WestEast", new double[]{LOTRWaypoint.BREE.getX() - 0.375, LOTRWaypoint.BREE.getY() - 2.476}, new double[]{LOTRWaypoint.BREE.getX() + 2.0, LOTRWaypoint.BREE.getY() - 1.5});
        registerDisplayOnlyRoad("WestEast", new double[]{LOTRWaypoint.BREE.getX() + 0.5, LOTRWaypoint.BREE.getY()}, new double[]{LOTRWaypoint.BREE.getX() + 2.0, LOTRWaypoint.BREE.getY()});
        registerDisplayOnlyRoad("WestEast", new double[]{LOTRWaypoint.BREE.getX() + 2.0, LOTRWaypoint.BREE.getY() - 1.5}, new double[]{LOTRWaypoint.BREE.getX() + 2.0, LOTRWaypoint.BREE.getY() + 1.5});
        registerRoad("BywaterRoad", LOTRWaypoint.BYWATER, LOTRWaypoint.HOBBITON);
        registerRoad("Overhill", LOTRWaypoint.HOBBITON, LOTRWaypoint.OVERHILL);
        registerRoad("BucklandRoad", LOTRWaypoint.HAY_GATE, LOTRWaypoint.BUCKLEBURY, LOTRWaypoint.HAYSEND);
        registerRoad("Chetroad", new double[]{LOTRWaypoint.STADDLE.getX(), LOTRWaypoint.STADDLE.getY() + 5.0}, LOTRWaypoint.STADDLE, LOTRWaypoint.COMBE, LOTRWaypoint.ARCHET);
        registerRoad("Chetroad", LOTRWaypoint.STADDLE, new double[]{LOTRWaypoint.STADDLE.getX() - 0.5, LOTRWaypoint.STADDLE.getY()});
        registerRoad("Chetroad", LOTRWaypoint.COMBE, new double[]{LOTRWaypoint.COMBE.getX() + 0.5, LOTRWaypoint.COMBE.getY()});
        registerRoad("Chetroad", LOTRWaypoint.ARCHET, new double[]{LOTRWaypoint.ARCHET.getX(), LOTRWaypoint.ARCHET.getY() - 0.5});
        registerRoad("ElfPath", LOTRWaypoint.FOREST_GATE, LOTRWaypoint.ENCHANTED_RIVER, LOTRWaypoint.THRANDUIL_HALLS);
        registerRoad("EreborRoad", LOTRWaypoint.LONG_LAKE, LOTRWaypoint.DALE_CITY, LOTRWaypoint.EREBOR);
        registerRoad("DalePortRoad", LOTRWaypoint.DALE_CITY, LOTRWaypoint.DALE_CROSSROADS, LOTRWaypoint.DALE_PORT);
        registerRoad("DaleSouthRoad", LOTRWaypoint.EAST_RHOVANION_ROAD, LOTRWaypoint.OLD_RHOVANION, LOTRWaypoint.RUNNING_FORD, LOTRWaypoint.DALE_CROSSROADS, LOTRWaypoint.WEST_PEAK);
        registerRoad("IronHills", LOTRWaypoint.WEST_PEAK, new int[]{1652, 621}, LOTRWaypoint.EAST_PEAK);
        registerRoad("DorwinionSouthRoad", LOTRWaypoint.DALE_PORT, LOTRWaypoint.DORWINION_CROSSROADS, LOTRWaypoint.DORWINION_COURT, LOTRWaypoint.DORWINION_FORD);
        registerRoad("DorwinionEastRoad", LOTRWaypoint.OLD_RHOVANION, LOTRWaypoint.DORWINION_CROSSROADS, LOTRWaypoint.DORWINION_PORT);
        registerRoad("RhunRoad", LOTRWaypoint.DORWINION_FORD, LOTRWaypoint.BORDER_TOWN, LOTRWaypoint.RHUN_SEA_CITY, LOTRWaypoint.RHUN_CAPITAL, new int[]{1888, 958}, LOTRWaypoint.RHUN_NORTH_CITY, LOTRWaypoint.BAZYLAN, LOTRWaypoint.RHUN_NORTHEAST);
        registerRoad("RhunEastRoad", LOTRWaypoint.RHUN_NORTH_CITY, LOTRWaypoint.RHUN_EAST_TOWN, LOTRWaypoint.RHUN_EAST_CITY);
        registerRoad("Nobottle", LOTRWaypoint.TIGHFIELD, LOTRWaypoint.LITTLE_DELVING, LOTRWaypoint.NOBOTTLE, LOTRWaypoint.NEEDLEHOLE);
        registerRoad("Oatbarton", LOTRWaypoint.OATBARTON, LOTRWaypoint.FROGMORTON);
        registerRoad("Stock", LOTRWaypoint.TUCKBOROUGH, LOTRWaypoint.STOCK);
        registerRoad("Deephallow", LOTRWaypoint.SCARY, LOTRWaypoint.WHITFURROWS, LOTRWaypoint.STOCK, LOTRWaypoint.DEEPHALLOW);
        registerRoad("Willowbottom", LOTRWaypoint.WILLOWBOTTOM, LOTRWaypoint.DEEPHALLOW);
        registerRoad("ArnorRoad", LOTRWaypoint.ANNUMINAS, LOTRWaypoint.FORNOST);
        registerRoad("Greenway", LOTRWaypoint.FORNOST, LOTRWaypoint.BREE, LOTRWaypoint.GREENWAY_CROSSROADS);
        registerRoad("ElvenWay", LOTRWaypoint.WEST_GATE, new int[]{1133, 867}, new int[]{1124, 868}, LOTRWaypoint.OST_IN_EDHIL, new int[]{1073, 864}, LOTRWaypoint.OLD_ELF_WAY, new int[]{1002, 849}, new int[]{992, 860}, LOTRWaypoint.THARBAD, new int[]{959, 889}, new int[]{926, 913}, new int[]{902, 942}, LOTRWaypoint.LOND_DAER);
        registerRoad("BruinenPath", LOTRWaypoint.FORD_BRUINEN, LOTRWaypoint.RIVENDELL);
        registerRoad("NimrodelRoad", LOTRWaypoint.DIMRILL_DALE, LOTRWaypoint.NIMRODEL);
        registerRoad("AnduinRoad", LOTRWaypoint.MORANNON, new int[]{1428, 1066}, LOTRWaypoint.EAST_RHOVANION_ROAD, LOTRWaypoint.ANDUIN_CROSSROADS, new int[]{1325, 820}, new int[]{1318, 735}, LOTRWaypoint.FOREST_GATE);
        registerRoad("DolGuldurRoad", LOTRWaypoint.ANDUIN_CROSSROADS, LOTRWaypoint.DOL_GULDUR);
        registerRoad("Framsburg", LOTRWaypoint.FOREST_GATE, new int[]{1278, 605}, LOTRWaypoint.FRAMSBURG, new int[]{1260, 565}, LOTRWaypoint.DAINS_HALLS);
        registerRoad("NorthSouth", LOTRWaypoint.LITTLE_DELVING, LOTRWaypoint.WAYMEET, LOTRWaypoint.LONGBOTTOM, LOTRWaypoint.SARN_FORD, LOTRWaypoint.GREENWAY_CROSSROADS, LOTRWaypoint.THARBAD, LOTRWaypoint.ENEDWAITH_ROAD, LOTRWaypoint.FORDS_OF_ISEN, LOTRWaypoint.HELMS_CROSSROADS, LOTRWaypoint.GRIMSLADE, LOTRWaypoint.EDORAS, LOTRWaypoint.ALDBURG, LOTRWaypoint.MERING_STREAM, LOTRWaypoint.AMON_DIN);
        registerRoad("TirithRoad", LOTRWaypoint.AMON_DIN, LOTRWaypoint.MINAS_TIRITH);
        registerRoad("OsgiliathRoad", LOTRWaypoint.MINAS_TIRITH, LOTRWaypoint.OSGILIATH_WEST);
        registerRoad("OsgiliathCrossing", LOTRWaypoint.OSGILIATH_WEST, LOTRWaypoint.OSGILIATH_EAST);
        registerRoad("OsgiliathMorgulRoad", LOTRWaypoint.OSGILIATH_EAST, LOTRWaypoint.CROSSROADS_ITHILIEN, LOTRWaypoint.MINAS_MORGUL);
        registerRoad("GondorSouthRoad", LOTRWaypoint.MINAS_TIRITH, LOTRWaypoint.CROSSINGS_ERUI, new int[]{1408, 1291}, LOTRWaypoint.PELARGIR, LOTRWaypoint.LINHIR, new int[]{1266, 1301}, LOTRWaypoint.ETHRING, LOTRWaypoint.CALEMBEL, LOTRWaypoint.TARLANG, LOTRWaypoint.ERECH);
        registerRoad("IsengardRoad", LOTRWaypoint.FORDS_OF_ISEN, LOTRWaypoint.ISENGARD);
        registerRoad("IsengardRoad", LOTRWaypoint.ISENGARD, new double[]{LOTRWaypoint.ISENGARD.getX(), LOTRWaypoint.ISENGARD.getY() - 3.5});
        registerRoad("HelmRoad", LOTRWaypoint.HELMS_CROSSROADS, LOTRWaypoint.HELMS_DEEP);
        registerRoad("WoldRoad", LOTRWaypoint.EDORAS, LOTRWaypoint.ENTWADE, new int[]{1260, 1060}, LOTRWaypoint.WOLD);
        registerRoad("DolAmroth", new int[]{1266, 1301}, LOTRWaypoint.TARNOST, LOTRWaypoint.EDHELLOND, new int[]{1185, 1325}, LOTRWaypoint.DOL_AMROTH);
        registerRoad("Pelargir", LOTRWaypoint.PELARGIR, new int[]{1394, 1352});
        registerRoad("Poros", new int[]{1397, 1355}, LOTRWaypoint.CROSSINGS_OF_POROS);
        registerRoad("CairAndros", LOTRWaypoint.AMON_DIN, LOTRWaypoint.CAIR_ANDROS, LOTRWaypoint.NORTH_ITHILIEN);
        registerRoad("SauronRoad", LOTRWaypoint.MINAS_MORGUL, LOTRWaypoint.MOUNT_DOOM, LOTRWaypoint.BARAD_DUR, LOTRWaypoint.SEREGOST, new int[]{1742, 1209}, new int[]{1809, 1172}, LOTRWaypoint.EASTERN_GUARD, LOTRWaypoint.MORDOR_FORD, LOTRWaypoint.RHUN_SOUTH_PASS, new int[]{1875, 1003}, new int[]{1867, 996}, LOTRWaypoint.RHUN_CAPITAL);
        registerRoad("MorannonRoad", LOTRWaypoint.MORANNON, LOTRWaypoint.UDUN);
        registerRoad("MorannonRhunRoad", LOTRWaypoint.MORANNON, new int[]{1520, 1130}, new int[]{1658, 1140}, new int[]{1780, 1115}, LOTRWaypoint.MORDOR_FORD, LOTRWaypoint.RHUN_SOUTHEAST, LOTRWaypoint.KHAND_NORTH_ROAD, LOTRWaypoint.KHAND_FORD, LOTRWaypoint.HARNEN_BLACK_TOWN, LOTRWaypoint.CROSSINGS_OF_LITHNEN, LOTRWaypoint.HARNEN_ROAD_TOWN, LOTRWaypoint.HARNEN_RIVER_TOWN, LOTRWaypoint.HARNEN_SEA_TOWN, LOTRWaypoint.COAST_FORTRESS, LOTRWaypoint.GATE_FUINUR, LOTRWaypoint.UMBAR_CITY, LOTRWaypoint.GATE_HERUMOR);
        registerRoad("GorgorothRoad", LOTRWaypoint.UDUN, LOTRWaypoint.CARACH_ANGREN, LOTRWaypoint.BARAD_DUR, LOTRWaypoint.THAURBAND);
        registerRoad("HaradRoad", LOTRWaypoint.MORANNON, LOTRWaypoint.NORTH_ITHILIEN, LOTRWaypoint.CROSSROADS_ITHILIEN, LOTRWaypoint.CROSSINGS_OF_POROS, new int[]{1429, 1394}, new int[]{1408, 1432}, new int[]{1428, 1470}, new int[]{1435, 1526}, LOTRWaypoint.CROSSINGS_OF_HARAD, LOTRWaypoint.HARNEN_ROAD_TOWN, LOTRWaypoint.DESERT_TOWN);
        registerRoad("UmbarRoad", LOTRWaypoint.UMBAR_CITY, LOTRWaypoint.UMBAR_GATE, LOTRWaypoint.AIN_AL_HARAD, LOTRWaypoint.GARDENS_BERUTHIEL, LOTRWaypoint.FERTILE_VALLEY, LOTRWaypoint.SOUTH_DESERT_TOWN);
        registerRoad("GulfRoad", LOTRWaypoint.TOWN_BONES, new int[]{1794, 2110}, LOTRWaypoint.GULF_FORD, LOTRWaypoint.GULF_TRADE_TOWN, LOTRWaypoint.GULF_CITY, LOTRWaypoint.GULF_NORTH_TOWN, new int[]{1702, 1940}, LOTRWaypoint.GULF_OF_HARAD, new int[]{1775, 2002}, LOTRWaypoint.GULF_EAST_PORT);
        registerRoad("JungleNorthRoad", LOTRWaypoint.JUNGLE_CITY_TRADE, LOTRWaypoint.JUNGLE_CITY_OLD, LOTRWaypoint.JUNGLE_CITY_NORTH);
        registerRoad("JungleMangroveRoad", LOTRWaypoint.JUNGLE_CITY_NORTH, LOTRWaypoint.JUNGLE_CITY_EAST, LOTRWaypoint.HARADUIN_MOUTH);
        registerRoad("JungleDeepRoad", LOTRWaypoint.JUNGLE_CITY_NORTH, LOTRWaypoint.JUNGLE_CITY_CAPITAL, LOTRWaypoint.JUNGLE_CITY_CAVES, LOTRWaypoint.JUNGLE_CITY_DEEP);
        registerRoad("JungleWestEastRoad", LOTRWaypoint.JUNGLE_CITY_OLD, LOTRWaypoint.JUNGLE_CITY_STONE, LOTRWaypoint.JUNGLE_CITY_CAPITAL, LOTRWaypoint.JUNGLE_LAKES, LOTRWaypoint.JUNGLE_CITY_WATCH);
        registerRoad("JungleLakeRoad", LOTRWaypoint.JUNGLE_LAKES, LOTRWaypoint.JUNGLE_CITY_EAST, LOTRWaypoint.HARADUIN_BRIDGE, LOTRWaypoint.OLD_JUNGLE_RUIN);
        int points = 0;
        for (LOTRRoads road : allRoads) {
            points += road.pointCount;
        }
        LOTRMod.LOGGER.info("LOTRRoads: Created {} roads, {} points, in {}s", allRoads.size(), points,
                (System.nanoTime() - time) / 1.0E9);
    }

    public static Iterator<LOTRRoads> getAllRoadsForDisplay() {
        createRoads();
        return Iterators.concat(allRoads.iterator(), displayOnlyRoads.iterator());
    }

    public static List<LOTRRoads> getAllRoadsInWorld() {
        createRoads();
        return allRoads;
    }

    public static boolean isRoadAt(int x, int z) {
        return isRoadNear(x, z, 4) >= 0.0f;
    }

    /** How close to a road this is, as the squared fraction of the width, or -1 if no road is within it. */
    public static float isRoadNear(int x, int z, int width) {
        double widthSq = width * width;
        float leastSqRatio = -1.0f;
        for (RoadPoint point : database().getPointsForCoords(x, z)) {
            double dx = point.x - x;
            double dz = point.z - z;
            double distSq = dx * dx + dz * dz;
            if (distSq >= widthSq) {
                continue;
            }
            float f = (float) (distSq / widthSq);
            if (leastSqRatio == -1.0f || f < leastSqRatio) {
                leastSqRatio = f;
            }
        }
        return leastSqRatio;
    }

    private static synchronized RoadPointDatabase database() {
        if (roadPointDatabase == null) {
            RoadPointDatabase database = new RoadPointDatabase();
            for (LOTRRoads road : getAllRoadsInWorld()) {
                for (RoadPoint point : road.roadPoints()) {
                    database.add(point);
                }
            }
            roadPointDatabase = database;
        }
        return roadPointDatabase;
    }

    private static void registerDisplayOnlyRoad(String name, Object... waypoints) {
        registerRoadToList(displayOnlyRoads, name, waypoints);
    }

    private static void registerRoad(String name, Object... waypoints) {
        registerRoadToList(allRoads, name, waypoints);
    }

    private static void registerRoadToList(List<LOTRRoads> targetList, String name, Object... waypoints) {
        List<RoadPoint> points = new ArrayList<>();
        for (Object obj : waypoints) {
            if (obj instanceof LOTRWaypoint wp) {
                points.add(new RoadPoint(wp.xCoord, wp.zCoord, true));
            } else if (obj instanceof int[] coords && coords.length == 2) {
                points.add(new RoadPoint(LOTRMapCoords.mapToWorldX(coords[0]), LOTRMapCoords.mapToWorldZ(coords[1]), false));
            } else if (obj instanceof double[] coords && coords.length == 2) {
                points.add(new RoadPoint(LOTRMapCoords.mapToWorldX(coords[0]), LOTRMapCoords.mapToWorldZ(coords[1]), false));
            } else {
                throw new IllegalArgumentException("Wrong road parameter!");
            }
        }
        Collections.addAll(targetList, BezierCurves.getSplines(name, points.toArray(new RoadPoint[0])));
    }

    public Component getDisplayName() {
        return Component.translatable("lotr.road." + this.roadName);
    }

    private static final class BezierCurves {
        static final int ROAD_LENGTH_FACTOR = 1;

        static RoadPoint bezier(RoadPoint a, RoadPoint b, RoadPoint c, RoadPoint d, double t) {
            RoadPoint ab = lerp(a, b, t);
            RoadPoint bc = lerp(b, c, t);
            RoadPoint cd = lerp(c, d, t);
            return lerp(lerp(ab, bc, t), lerp(bc, cd, t), t);
        }

        private static double[][] getControlPoints(double[] src) {
            int length = src.length - 1;
            double[] p1 = new double[length];
            double[] p2 = new double[length];
            double[] a = new double[length];
            double[] b = new double[length];
            double[] c = new double[length];
            double[] r = new double[length];
            a[0] = 0.0;
            b[0] = 2.0;
            c[0] = 1.0;
            r[0] = src[0] + 2.0 * src[1];
            for (int i = 1; i < length - 1; ++i) {
                a[i] = 1.0;
                b[i] = 4.0;
                c[i] = 1.0;
                r[i] = 4.0 * src[i] + 2.0 * src[i + 1];
            }
            a[length - 1] = 2.0;
            b[length - 1] = 7.0;
            c[length - 1] = 0.0;
            r[length - 1] = 8.0 * src[length - 1] + src[length];
            for (int i = 1; i < length; ++i) {
                double p = a[i] / b[i - 1];
                b[i] -= p * c[i - 1];
                r[i] -= p * r[i - 1];
            }
            p1[length - 1] = r[length - 1] / b[length - 1];
            for (int i = length - 2; i >= 0; --i) {
                p1[i] = (r[i] - c[i] * p1[i + 1]) / b[i];
            }
            for (int i = 0; i < length - 1; ++i) {
                p2[i] = 2.0 * src[i + 1] - p1[i + 1];
            }
            p2[length - 1] = 0.5 * (src[length] + p1[length - 1]);
            return new double[][]{p1, p2};
        }

        static LOTRRoads[] getSplines(String name, RoadPoint[] waypoints) {
            if (waypoints.length == 2) {
                return new LOTRRoads[]{new LOTRRoads(name, waypoints[0], waypoints[0], waypoints[1], waypoints[1], true)};
            }
            int length = waypoints.length;
            double[] x = new double[length];
            double[] z = new double[length];
            for (int i = 0; i < length; ++i) {
                x[i] = waypoints[i].x;
                z[i] = waypoints[i].z;
            }
            double[][] controlX = getControlPoints(x);
            double[][] controlZ = getControlPoints(z);
            LOTRRoads[] roads = new LOTRRoads[length - 1];
            for (int i = 0; i < roads.length; ++i) {
                roads[i] = new LOTRRoads(name, waypoints[i], new RoadPoint(controlX[0][i], controlZ[0][i], false),
                        new RoadPoint(controlX[1][i], controlZ[1][i], false), waypoints[i + 1], false);
            }
            return roads;
        }

        private static RoadPoint lerp(RoadPoint a, RoadPoint b, double t) {
            return new RoadPoint(a.x + (b.x - a.x) * t, a.z + (b.z - a.z) * t, false);
        }
    }

    public record RoadPoint(double x, double z, boolean isWaypoint) {
    }

    /** The road points by area, a thousand blocks a cell, each point in its cell and the eight around it. */
    private static final class RoadPointDatabase {
        private static final int COORD_LOOKUP_SIZE = 1000;
        private final Map<Long, List<RoadPoint>> pointMap = new HashMap<>();

        void add(RoadPoint point) {
            int x = (int) Math.round(point.x() / COORD_LOOKUP_SIZE);
            int z = (int) Math.round(point.z() / COORD_LOOKUP_SIZE);
            for (int i = -1; i <= 1; ++i) {
                for (int k = -1; k <= 1; ++k) {
                    this.pointMap.computeIfAbsent(key(x + i, z + k), unused -> new ArrayList<>()).add(point);
                }
            }
        }

        List<RoadPoint> getPointsForCoords(int x, int z) {
            return this.pointMap.getOrDefault(key(x / COORD_LOOKUP_SIZE, z / COORD_LOOKUP_SIZE), List.of());
        }

        private static long key(int x, int z) {
            return ((long) x << 32) | (z & 0xFFFFFFFFL);
        }
    }
}
