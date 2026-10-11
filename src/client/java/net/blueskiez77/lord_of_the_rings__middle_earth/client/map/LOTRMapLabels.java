package net.blueskiez77.lord_of_the_rings__middle_earth.client.map;

import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

import net.minecraft.network.chat.Component;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMapLabels: the names written over the map -- a biome's, or one of the map's own (lotr.map.*) --
 * each at a place on the map image, a scale and an angle, shown between two zooms.
 */
public enum LOTRMapLabels {
    BELEGAER("belegaer", 250, 1100, 16.0f, 0, -6.0f, -1.0f),
    BELEGAER_SUB("belegaer_sub", 250, 1220, 6.0f, 0, -6.0f, -1.0f),
    WESTERN_ISLES("westernIsles", 290, 620, 4.0f, -20, -4.5f, 1.5f),
    FORODWAITH((Supplier<LOTRBiome>) () -> LOTRBiomes.FORODWAITH, 1680, 380, 15.0f, 2, -5.5f, -0.5f),
    FOROCHEL("forochel", 780, 420, 3.0f, 10, -2.5f, 1.5f),
    SHIRE((Supplier<LOTRBiome>) () -> LOTRBiomes.SHIRE, 820, 750, 2.0f, -10, -2.5f, 0.5f),
    WHITE_DOWNS((Supplier<LOTRBiome>) () -> LOTRBiomes.WHITE_DOWNS, 783, 730, 0.5f, -5, -0.5f, 2.5f),
    NORTH_MOORS("northMoors", 805, 690, 1.0f, -5, -2.5f, 2.5f),
    OLD_FOREST((Supplier<LOTRBiome>) () -> LOTRBiomes.OLD_FOREST, 875, 745, 0.5f, -5, -0.5f, 2.5f),
    BREE_LAND((Supplier<LOTRBiome>) () -> LOTRBiomes.BREELAND, 920, 740, 1.0f, 15, -1.5f, 1.5f),
    BARROW_DOWNS((Supplier<LOTRBiome>) () -> LOTRBiomes.BARROW_DOWNS, 900, 760, 0.5f, -10, -0.5f, 2.5f),
    ERIADOR((Supplier<LOTRBiome>) () -> LOTRBiomes.ERIADOR, 900, 750, 10.0f, -15, -4.5f, -1.5f),
    MINHIRIATH((Supplier<LOTRBiome>) () -> LOTRBiomes.MINHIRIATH, 850, 900, 3.0f, -40, -2.5f, 1.5f),
    HILLS_EVENDIM("hillsEvendim", 800, 630, 1.5f, -30, -2.5f, 2.5f),
    NORTH_DOWNS("northDowns", 930, 620, 1.0f, -25, -2.5f, 2.5f),
    SOUTH_DOWNS("southDowns", 965, 775, 1.0f, 0, -2.5f, 2.5f),
    LONE_LANDS((Supplier<LOTRBiome>) () -> LOTRBiomes.LONE_LANDS, 1080, 740, 3.0f, -5, -2.5f, 0.5f),
    THE_ANGLE((Supplier<LOTRBiome>) () -> LOTRBiomes.ANGLE, 1110, 755, 1.0f, -50, -0.5f, 2.5f),
    WEATHER_HILLS("weatherHills", 985, 695, 1.0f, 30, -2.5f, 2.5f),
    TROLLSHAWS((Supplier<LOTRBiome>) () -> LOTRBiomes.TROLLSHAWS, 1130, 695, 1.0f, -5, -1.5f, 2.5f),
    ETTENMOORS((Supplier<LOTRBiome>) () -> LOTRBiomes.ETTENMOORS, 1085, 605, 2.0f, 25, -1.5f, 1.5f),
    COLDFELLS((Supplier<LOTRBiome>) () -> LOTRBiomes.COLDFELLS, 1180, 640, 1.0f, -5, -1.5f, 2.5f),
    ANGMAR((Supplier<LOTRBiome>) () -> LOTRBiomes.ANGMAR, 1080, 550, 4.0f, 17, -2.5f, 0.5f),
    MISTY_MOUNTAINS((Supplier<LOTRBiome>) () -> LOTRBiomes.MISTY_MOUNTAINS, 1175, 830, 4.0f, -75, -3.5f, 0.5f),
    LINDON((Supplier<LOTRBiome>) () -> LOTRBiomes.LINDON, 570, 750, 8.0f, 70, -5.5f, -0.5f),
    FORLINDON("forlindon", 535, 665, 2.0f, -5, -1.5f, 2.5f),
    HARLINDON("harlindon", 580, 825, 2.0f, 15, -1.5f, 2.5f),
    GULF_LUNE("gulfLune", 570, 760, 1.5f, -10, -0.5f, 2.5f),
    TOWER_HILLS((Supplier<LOTRBiome>) () -> LOTRBiomes.TOWER_HILLS, 710, 750, 0.5f, 10, -0.5f, 2.5f),
    FAR_DOWNS("farDowns", 755, 738, 0.5f, 0, -0.5f, 2.5f),
    BLUE_MOUNTAINS((Supplier<LOTRBiome>) () -> LOTRBiomes.BLUE_MOUNTAINS, 625, 620, 3.0f, 80, -3.5f, 0.5f),
    EREGION((Supplier<LOTRBiome>) () -> LOTRBiomes.EREGION, 1125, 845, 1.0f, -50, -2.5f, 2.5f),
    ENEDWAITH((Supplier<LOTRBiome>) () -> LOTRBiomes.ENEDWAITH, 950, 1020, 4.0f, -50, -4.5f, 0.5f),
    DUNLAND((Supplier<LOTRBiome>) () -> LOTRBiomes.DUNLAND, 1055, 990, 1.5f, -70, -1.5f, 1.5f),
    SWANFLEET((Supplier<LOTRBiome>) () -> LOTRBiomes.SWANFLEET, 1020, 875, 1.0f, 5, -1.5f, 2.5f),
    PUKEL((Supplier<LOTRBiome>) () -> LOTRBiomes.PUKEL, 920, 1180, 1.5f, -30, -2.5f, 1.5f),
    RHOVANION("rhovanion", 1490, 810, 10.0f, 40, -4.5f, -1.5f),
    VALES_ANDUIN((Supplier<LOTRBiome>) () -> LOTRBiomes.ANDUIN_VALE, 1260, 775, 2.3f, -80, -2.5f, 0.5f),
    GLADDEN_FIELDS((Supplier<LOTRBiome>) () -> LOTRBiomes.GLADDEN_FIELDS, 1290, 775, 1.0f, -3, -0.5f, 2.5f),
    LOTHLORIEN((Supplier<LOTRBiome>) () -> LOTRBiomes.LOTHLORIEN, 1225, 930, 1.0f, 15, -2.5f, 1.5f),
    MIRKWOOD((Supplier<LOTRBiome>) () -> LOTRBiomes.MIRKWOOD_CORRUPTED, 1400, 730, 3.0f, -2, -2.5f, 1.5f),
    WOODLAND_REALM((Supplier<LOTRBiome>) () -> LOTRBiomes.WOODLAND_REALM, 1385, 620, 1.0f, -5, -1.5f, 2.5f),
    DALE((Supplier<LOTRBiome>) () -> LOTRBiomes.DALE, 1560, 700, 4.0f, 10, -3.5f, 0.5f),
    GREY_MOUNTAINS((Supplier<LOTRBiome>) () -> LOTRBiomes.GREY_MOUNTAINS, 1360, 565, 2.0f, 5, -3.5f, 0.5f),
    IRON_HILLS((Supplier<LOTRBiome>) () -> LOTRBiomes.IRON_HILLS, 1660, 600, 2.0f, -2, -3.5f, 0.5f),
    ROHAN((Supplier<LOTRBiome>) () -> LOTRBiomes.ROHAN, 1240, 1080, 5.0f, -10, -4.5f, -0.5f),
    WESTEMNET("westemnet", 1180, 1080, 1.5f, 10, -1.5f, 1.5f),
    EASTEMNET("eastemnet", 1290, 1100, 1.5f, -15, -1.5f, 1.5f),
    WOLD((Supplier<LOTRBiome>) () -> LOTRBiomes.WOLD, 1270, 1000, 1.5f, 10, -1.5f, 1.5f),
    WESTFOLD("westfold", 1165, 1110, 1.0f, 40, -1.5f, 2.5f),
    EASTFOLD("eastfold", 1250, 1175, 1.0f, 20, -1.5f, 2.5f),
    WESTMARCH("westmarch", 1060, 1115, 1.0f, 2, -1.5f, 2.5f),
    FANGORN((Supplier<LOTRBiome>) () -> LOTRBiomes.FANGORN, 1190, 1000, 1.5f, 0, -2.5f, 2.5f),
    EMYN_MUIL((Supplier<LOTRBiome>) () -> LOTRBiomes.EMYN_MUIL, 1360, 1100, 1.5f, -12, -2.5f, 1.5f),
    NINDALF((Supplier<LOTRBiome>) () -> LOTRBiomes.NINDALF, 1395, 1165, 1.0f, 0, -1.5f, 1.5f),
    DEAD_MARSHES((Supplier<LOTRBiome>) () -> LOTRBiomes.DEAD_MARSHES, 1420, 1125, 1.0f, 0, -1.5f, 1.5f),
    BROWN_LANDS((Supplier<LOTRBiome>) () -> LOTRBiomes.BROWN_LANDS, 1480, 1050, 3.0f, 15, -2.5f, 0.5f),
    DAGORLAD((Supplier<LOTRBiome>) () -> LOTRBiomes.DAGORLAD, 1470, 1110, 1.5f, 5, -1.5f, 1.5f),
    GONDOR((Supplier<LOTRBiome>) () -> LOTRBiomes.GONDOR, 1150, 1250, 8.0f, 10, -4.5f, -0.5f),
    ANORIEN("anorien", 1370, 1205, 1.5f, 15, -2.5f, 2.5f),
    ITHILIEN((Supplier<LOTRBiome>) () -> LOTRBiomes.ITHILIEN, 1440, 1320, 2.0f, -80, -2.5f, 2.5f),
    LEBENNIN((Supplier<LOTRBiome>) () -> LOTRBiomes.LEBENNIN, 1360, 1320, 2.0f, -20, -2.5f, 2.5f),
    LOSSARNACH((Supplier<LOTRBiome>) () -> LOTRBiomes.LOSSARNACH, 1405, 1265, 0.75f, -10, -1.5f, 2.5f),
    LAMEDON((Supplier<LOTRBiome>) () -> LOTRBiomes.LAMEDON, 1260, 1240, 1.5f, 5, -1.5f, 2.5f),
    BLACKROOT_VALE((Supplier<LOTRBiome>) () -> LOTRBiomes.BLACKROOT_VALE, 1150, 1225, 1.0f, -25, -1.5f, 2.5f),
    PINNATH_GELIN((Supplier<LOTRBiome>) () -> LOTRBiomes.PINNATH_GELIN, 1035, 1255, 1.5f, -10, -1.5f, 2.5f),
    DOR_EN_ERNIL((Supplier<LOTRBiome>) () -> LOTRBiomes.DOR_EN_ERNIL, 1220, 1330, 1.5f, -30, -2.5f, 2.5f),
    ANFALAS("anfalas", 1045, 1320, 2.0f, -15, -2.5f, 1.5f),
    ANDRAST((Supplier<LOTRBiome>) () -> LOTRBiomes.ANDRAST, 860, 1365, 1.5f, -25, -2.5f, 1.5f),
    HARONDOR((Supplier<LOTRBiome>) () -> LOTRBiomes.HARONDOR, 1405, 1470, 4.0f, -5, -2.5f, 1.5f),
    BAY_BELFALAS("bayBelfalas", 1050, 1450, 4.0f, 0, -4.5f, 1.5f),
    WHITE_MOUNTAINS((Supplier<LOTRBiome>) () -> LOTRBiomes.WHITE_MOUNTAINS, 1125, 1180, 3.0f, 5, -3.5f, 1.5f),
    MORDOR((Supplier<LOTRBiome>) () -> LOTRBiomes.MORDOR, 1650, 1280, 8.0f, -20, -4.5f, -0.5f),
    ASH_MOUNTAINS("ashMountains", 1640, 1160, 2.0f, -5, -2.5f, 0.5f),
    SHADOW_MOUNTAINS("shadowMountains", 1480, 1285, 2.0f, 85, -2.5f, 0.5f),
    GORGOROTH((Supplier<LOTRBiome>) () -> LOTRBiomes.GORGOROTH, 1580, 1220, 2.0f, -15, -1.5f, 1.5f),
    NURN((Supplier<LOTRBiome>) () -> LOTRBiomes.NURN, 1660, 1310, 5.0f, -25, -0.5f, 1.5f),
    NURNEN((Supplier<LOTRBiome>) () -> LOTRBiomes.NURNEN, 1710, 1345, 1.0f, -15, -1.5f, 2.5f),
    DORWINION((Supplier<LOTRBiome>) () -> LOTRBiomes.DORWINION, 1730, 890, 2.0f, -10, -2.5f, 2.5f),
    RHUN("rhun", 2400, 1150, 20.0f, -5, -3.5f, -2.5f),
    SEA_RHUN("rhunSea", 1830, 930, 1.5f, -5, -3.5f, 1.5f),
    PALISOR("palisor", 2120, 1020, 12.0f, 2, -3.5f, -1.0f),
    RHUN_LAND((Supplier<LOTRBiome>) () -> LOTRBiomes.RHUN_LAND, 1870, 1010, 3.0f, -10, -2.0f, 0.5f),
    RHUN_RED_FOREST((Supplier<LOTRBiome>) () -> LOTRBiomes.RHUN_RED_FOREST, 1880, 835, 1.25f, 10, -1.5f, 1.5f),
    WILD_WOOD("wildWood", 2450, 1120, 6.0f, 60, -2.5f, 0.5f),
    HARUNNOR("harunnor", 2610, 1760, 10.0f, -5, -3.5f, -0.5f),
    MOUNTAINS_WIND((Supplier<LOTRBiome>) () -> LOTRBiomes.WIND_MOUNTAINS, 2490, 1610, 4.0f, -10, -3.5f, 0.5f),
    LAST_DESERT((Supplier<LOTRBiome>) () -> LOTRBiomes.LAST_DESERT, 2540, 1460, 4.0f, -20, -2.5f, 0.5f),
    UTTER_EAST("utterEast", 2740, 1000, 10.0f, 60, -3.5f, -1.0f),
    OROCARNI((Supplier<LOTRBiome>) () -> LOTRBiomes.RED_MOUNTAINS, 2420, 870, 6.0f, 60, -3.5f, -0.5f),
    NEAR_HARAD("nearHarad", 1590, 1685, 8.0f, -2, -4.5f, -0.5f),
    HARNENNOR((Supplier<LOTRBiome>) () -> LOTRBiomes.HARNEDOR, 1440, 1580, 3.0f, -3, -2.5f, 1.5f),
    LITHNEN("lithnen", 1615, 1615, 1.5f, -15, -1.5f, 1.5f),
    UMBAR((Supplier<LOTRBiome>) () -> LOTRBiomes.UMBAR, 1160, 1685, 4.0f, -15, -3.5f, 1.5f),
    LOSTLADEN("lostladen", 1685, 1470, 2.0f, -5, -2.5f, 1.5f),
    SOUTHRON_COASTS((Supplier<LOTRBiome>) () -> LOTRBiomes.NEAR_HARAD_FERTILE, 1125, 1790, 3.0f, -20, -2.5f, 0.5f),
    GULF_HARAD((Supplier<LOTRBiome>) () -> LOTRBiomes.GULF_HARAD, 1930, 2165, 4.0f, 40, -4.5f, 1.5f),
    KHAND("khand", 1940, 1420, 4.0f, 5, -3.5f, 0.5f),
    FAR_HARAD("farHarad", 1350, 2500, 15.0f, 5, -5.5f, -1.5f),
    FAR_HARAD_HILLS("farHaradHills", 1200, 2050, 3.0f, 25, -2.5f, 0.5f),
    GREAT_PLAINS("greatPlains", 1370, 2280, 7.5f, 2, -2.5f, -0.5f),
    FORESTS_SOUTH("forestsSouth", 1300, 3000, 6.0f, 2, -2.5f, -0.5f),
    HARAD_MOUNTAINS((Supplier<LOTRBiome>) () -> LOTRBiomes.HARAD_MOUNTAINS, 850, 2280, 5.0f, 80, -2.5f, 0.5f),
    PERDOROGWAITH((Supplier<LOTRBiome>) () -> LOTRBiomes.PERTOROGWAITH, 1920, 2560, 5.0f, -10, -2.5f, 0.5f),
    FOREST_TROLLS((Supplier<LOTRBiome>) () -> LOTRBiomes.HALF_TROLL_FOREST, 1740, 2530, 2.0f, 5, -2.0f, 1.5f);

    public final int posX;
    public final int posY;
    public final float scale;
    public final int angle;
    public final float minZoom;
    public final float maxZoom;
    private final @Nullable Supplier<LOTRBiome> biome;
    private final @Nullable String labelName;

    LOTRMapLabels(Supplier<LOTRBiome> biome, int x, int y, float f, int r, float z1, float z2) {
        this(biome, null, x, y, f, r, z1, z2);
    }

    LOTRMapLabels(String labelName, int x, int y, float f, int r, float z1, float z2) {
        this(null, labelName, x, y, f, r, z1, z2);
    }

    LOTRMapLabels(@Nullable Supplier<LOTRBiome> biome, @Nullable String labelName, int x, int y, float f, int r, float z1, float z2) {
        this.posX = x;
        this.posY = y;
        this.scale = f;
        this.angle = r;
        this.minZoom = z1;
        this.maxZoom = z2;
        this.biome = biome;
        this.labelName = labelName;
    }

    public Component getDisplayName() {
        if (this.labelName != null) {
            return Component.translatable("lotr.map." + this.labelName);
        }
        return this.biome.get().getBiomeDisplayName();
    }
}
