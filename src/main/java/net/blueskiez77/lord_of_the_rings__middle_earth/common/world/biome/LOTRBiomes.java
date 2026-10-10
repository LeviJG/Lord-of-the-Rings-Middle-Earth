package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import java.util.HashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBiome.initBiomes: Middle-earth's biomes, each with its id (LOTRConfigBiomeID's defaults),
 * temperature and rain, height and hilliness, colour on the map and name. Utumno's waits on D15.
 */
public final class LOTRBiomes {

    public static LOTRBiome RIVER;
    public static LOTRBiome ROHAN;
    public static LOTRBiome MISTY_MOUNTAINS;
    public static LOTRBiome SHIRE;
    public static LOTRBiome SHIRE_WOODLANDS;
    public static LOTRBiome MORDOR;
    public static LOTRBiome MORDOR_MOUNTAINS;
    public static LOTRBiome GONDOR;
    public static LOTRBiome WHITE_MOUNTAINS;
    public static LOTRBiome LOTHLORIEN;
    public static LOTRBiome CELEBRANT;
    public static LOTRBiome IRON_HILLS;
    public static LOTRBiome DEAD_MARSHES;
    public static LOTRBiome TROLLSHAWS;
    public static LOTRBiome WOODLAND_REALM;
    public static LOTRBiome MIRKWOOD_CORRUPTED;
    public static LOTRBiome ROHAN_URUK_HIGHLANDS;
    public static LOTRBiome EMYN_MUIL;
    public static LOTRBiome ITHILIEN;
    public static LOTRBiome PELARGIR;
    public static LOTRBiome LONE_LANDS;
    public static LOTRBiome LONE_LANDS_HILLS;
    public static LOTRBiome DUNLAND;
    public static LOTRBiome FANGORN;
    public static LOTRBiome ANGLE;
    public static LOTRBiome ETTENMOORS;
    public static LOTRBiome OLD_FOREST;
    public static LOTRBiome HARONDOR;
    public static LOTRBiome ERIADOR;
    public static LOTRBiome ERIADOR_DOWNS;
    public static LOTRBiome ERYN_VORN;
    public static LOTRBiome GREY_MOUNTAINS;
    public static LOTRBiome MIDGEWATER;
    public static LOTRBiome BROWN_LANDS;
    public static LOTRBiome OCEAN;
    public static LOTRBiome ANDUIN_HILLS;
    public static LOTRBiome MENELTARMA;
    public static LOTRBiome GLADDEN_FIELDS;
    public static LOTRBiome LOTHLORIEN_EDGE;
    public static LOTRBiome FORODWAITH;
    public static LOTRBiome ENEDWAITH;
    public static LOTRBiome ANGMAR;
    public static LOTRBiome EREGION;
    public static LOTRBiome LINDON;
    public static LOTRBiome LINDON_WOODLANDS;
    public static LOTRBiome EAST_BIGHT;
    public static LOTRBiome BLUE_MOUNTAINS;
    public static LOTRBiome MIRKWOOD_MOUNTAINS;
    public static LOTRBiome WILDERLAND;
    public static LOTRBiome DAGORLAD;
    public static LOTRBiome NURN;
    public static LOTRBiome NURNEN;
    public static LOTRBiome NURN_MARSHES;
    public static LOTRBiome ADORNLAND;
    public static LOTRBiome ANGMAR_MOUNTAINS;
    public static LOTRBiome ANDUIN_MOUTH;
    public static LOTRBiome ENTWASH_MOUTH;
    public static LOTRBiome DOR_EN_ERNIL;
    public static LOTRBiome DOR_EN_ERNIL_HILLS;
    public static LOTRBiome FANGORN_WASTELAND;
    public static LOTRBiome ROHAN_WOODLANDS;
    public static LOTRBiome GONDOR_WOODLANDS;
    public static LOTRBiome LAKE;
    public static LOTRBiome LINDON_COAST;
    public static LOTRBiome BARROW_DOWNS;
    public static LOTRBiome LONG_MARSHES;
    public static LOTRBiome FANGORN_CLEARING;
    public static LOTRBiome ITHILIEN_HILLS;
    public static LOTRBiome ITHILIEN_WASTELAND;
    public static LOTRBiome NINDALF;
    public static LOTRBiome COLDFELLS;
    public static LOTRBiome NAN_CURUNIR;
    public static LOTRBiome WHITE_DOWNS;
    public static LOTRBiome SWANFLEET;
    public static LOTRBiome PELENNOR;
    public static LOTRBiome MINHIRIATH;
    public static LOTRBiome EREBOR;
    public static LOTRBiome MIRKWOOD_NORTH;
    public static LOTRBiome WOODLAND_REALM_HILLS;
    public static LOTRBiome NAN_UNGOL;
    public static LOTRBiome PINNATH_GELIN;
    public static LOTRBiome ISLAND;
    public static LOTRBiome FORODWAITH_MOUNTAINS;
    public static LOTRBiome MISTY_MOUNTAINS_FOOTHILLS;
    public static LOTRBiome GREY_MOUNTAINS_FOOTHILLS;
    public static LOTRBiome BLUE_MOUNTAINS_FOOTHILLS;
    public static LOTRBiome TUNDRA;
    public static LOTRBiome TAIGA;
    public static LOTRBiome BREELAND;
    public static LOTRBiome CHETWOOD;
    public static LOTRBiome FORODWAITH_GLACIER;
    public static LOTRBiome WHITE_MOUNTAINS_FOOTHILLS;
    public static LOTRBiome BEACH;
    public static LOTRBiome BEACH_GRAVEL;
    public static LOTRBiome NEAR_HARAD;
    public static LOTRBiome FAR_HARAD;
    public static LOTRBiome HARAD_MOUNTAINS;
    public static LOTRBiome UMBAR;
    public static LOTRBiome FAR_HARAD_JUNGLE;
    public static LOTRBiome UMBAR_HILLS;
    public static LOTRBiome NEAR_HARAD_HILLS;
    public static LOTRBiome FAR_HARAD_JUNGLE_LAKE;
    public static LOTRBiome LOSTLADEN;
    public static LOTRBiome FAR_HARAD_FOREST;
    public static LOTRBiome NEAR_HARAD_FERTILE;
    public static LOTRBiome PERTOROGWAITH;
    public static LOTRBiome UMBAR_FOREST;
    public static LOTRBiome FAR_HARAD_JUNGLE_EDGE;
    public static LOTRBiome TAUREDAIN_CLEARING;
    public static LOTRBiome GULF_HARAD;
    public static LOTRBiome DORWINION_HILLS;
    public static LOTRBiome TOLFALAS;
    public static LOTRBiome LEBENNIN;
    public static LOTRBiome RHUN;
    public static LOTRBiome RHUN_FOREST;
    public static LOTRBiome RED_MOUNTAINS;
    public static LOTRBiome RED_MOUNTAINS_FOOTHILLS;
    public static LOTRBiome DOL_GULDUR;
    public static LOTRBiome NEAR_HARAD_SEMI_DESERT;
    public static LOTRBiome FAR_HARAD_ARID;
    public static LOTRBiome FAR_HARAD_ARID_HILLS;
    public static LOTRBiome FAR_HARAD_SWAMP;
    public static LOTRBiome FAR_HARAD_CLOUD_FOREST;
    public static LOTRBiome FAR_HARAD_BUSHLAND;
    public static LOTRBiome FAR_HARAD_BUSHLAND_HILLS;
    public static LOTRBiome FAR_HARAD_MANGROVE;
    public static LOTRBiome NEAR_HARAD_FERTILE_FOREST;
    public static LOTRBiome ANDUIN_VALE;
    public static LOTRBiome WOLD;
    public static LOTRBiome SHIRE_MOORS;
    public static LOTRBiome SHIRE_MARSHES;
    public static LOTRBiome NEAR_HARAD_RED_DESERT;
    public static LOTRBiome FAR_HARAD_VOLCANO;
    public static LOTRBiome UDUN;
    public static LOTRBiome GORGOROTH;
    public static LOTRBiome MORGUL_VALE;
    public static LOTRBiome EASTERN_DESOLATION;
    public static LOTRBiome DALE;
    public static LOTRBiome DORWINION;
    public static LOTRBiome TOWER_HILLS;
    public static LOTRBiome GULF_HARAD_FOREST;
    public static LOTRBiome WILDERLAND_NORTH;
    public static LOTRBiome FORODWAITH_COAST;
    public static LOTRBiome FAR_HARAD_COAST;
    public static LOTRBiome NEAR_HARAD_RIVERBANK;
    public static LOTRBiome LOSSARNACH;
    public static LOTRBiome IMLOTH_MELUI;
    public static LOTRBiome NEAR_HARAD_OASIS;
    public static LOTRBiome BEACH_WHITE;
    public static LOTRBiome HARNEDOR;
    public static LOTRBiome LAMEDON;
    public static LOTRBiome LAMEDON_HILLS;
    public static LOTRBiome BLACKROOT_VALE;
    public static LOTRBiome ANDRAST;
    public static LOTRBiome PUKEL;
    public static LOTRBiome RHUN_LAND;
    public static LOTRBiome RHUN_LAND_STEPPE;
    public static LOTRBiome RHUN_LAND_HILLS;
    public static LOTRBiome RHUN_RED_FOREST;
    public static LOTRBiome RHUN_ISLAND;
    public static LOTRBiome RHUN_ISLAND_FOREST;
    public static LOTRBiome LAST_DESERT;
    public static LOTRBiome WIND_MOUNTAINS;
    public static LOTRBiome WIND_MOUNTAINS_FOOTHILLS;
    public static LOTRBiome RIVENDELL;
    public static LOTRBiome RIVENDELL_HILLS;
    public static LOTRBiome FAR_HARAD_JUNGLE_MOUNTAINS;
    public static LOTRBiome HALF_TROLL_FOREST;
    public static LOTRBiome FAR_HARAD_KANUKA;

    private static final Map<ResourceKey<Biome>, LOTRBiome> BY_KEY = new HashMap<>();

    private LOTRBiomes() {
    }

    public static void init() {
        RIVER = new LOTRRiverBiome(0, false).setMinMaxHeight(-0.5f, 0.0f).setColor(3570869).setBiomeName("river");
        ROHAN = new LOTRRohanBiome(1, true).setTemperatureRainfall(0.8f, 0.8f).setMinMaxHeight(0.2f, 0.15f).setColor(7384389).setBiomeName("rohan");
        MISTY_MOUNTAINS = new LOTRMistyMountainsBiome(2, true).setTemperatureRainfall(0.2f, 0.5f).setMinMaxHeight(2.0f, 2.0f).setColor(15263713).setBiomeName("mistyMountains");
        SHIRE = new LOTRShireBiome(3, true).setTemperatureRainfall(0.8f, 0.9f).setMinMaxHeight(0.15f, 0.3f).setColor(6794549).setBiomeName("shire");
        SHIRE_WOODLANDS = new LOTRShireWoodlandsBiome(4, true).setTemperatureRainfall(0.8f, 0.9f).setMinMaxHeight(0.3f, 0.5f).setColor(4486966).setBiomeName("shireWoodlands");
        MORDOR = new LOTRMordorBiome(5, true).setTemperatureRainfall(2.0f, 0.0f).setMinMaxHeight(0.3f, 0.5f).setColor(1118222).setBiomeName("mordor");
        MORDOR_MOUNTAINS = new LOTRMordorMountainsBiome(6, true).setTemperatureRainfall(2.0f, 0.0f).setMinMaxHeight(2.0f, 3.0f).setColor(5328200).setBiomeName("mordorMountains");
        GONDOR = new LOTRGondorBiome(7, true).setTemperatureRainfall(0.8f, 0.8f).setMinMaxHeight(0.1f, 0.15f).setColor(8959045).setBiomeName("gondor");
        WHITE_MOUNTAINS = new LOTRWhiteMountainsBiome(8, true).setTemperatureRainfall(0.6f, 0.8f).setMinMaxHeight(1.5f, 2.0f).setColor(15066600).setBiomeName("whiteMountains");
        LOTHLORIEN = new LOTRLothlorienBiome(9, true).setTemperatureRainfall(0.9f, 1.0f).setMinMaxHeight(0.1f, 0.3f).setColor(16504895).setBiomeName("lothlorien");
        CELEBRANT = new LOTRCelebrantBiome(10, true).setTemperatureRainfall(1.1f, 1.1f).setMinMaxHeight(0.1f, 0.05f).setColor(7647046).setBiomeName("celebrant");
        IRON_HILLS = new LOTRIronHillsBiome(11, true).setTemperatureRainfall(0.27f, 0.4f).setMinMaxHeight(0.3f, 1.4f).setColor(9142093).setBiomeName("ironHills");
        DEAD_MARSHES = new LOTRDeadMarshesBiome(12, true).setTemperatureRainfall(0.4f, 1.0f).setMinMaxHeight(0.0f, 0.1f).setColor(7303999).setBiomeName("deadMarshes");
        TROLLSHAWS = new LOTRTrollshawsBiome(13, true).setTemperatureRainfall(0.6f, 0.8f).setMinMaxHeight(0.15f, 1.0f).setColor(5798959).setBiomeName("trollshaws");
        WOODLAND_REALM = new LOTRWoodlandRealmBiome(14, true).setTemperatureRainfall(0.8f, 0.9f).setMinMaxHeight(0.2f, 0.3f).setColor(4089126).setBiomeName("woodlandRealm");
        MIRKWOOD_CORRUPTED = new LOTRMirkwoodCorruptedBiome(15, true).setTemperatureRainfall(0.6f, 0.8f).setMinMaxHeight(0.2f, 0.4f).setColor(3032091).setBiomeName("mirkwoodCorrupted");
        ROHAN_URUK_HIGHLANDS = new LOTRRohanUrukBiome(16, true).setTemperatureRainfall(0.7f, 0.4f).setMinMaxHeight(0.8f, 0.3f).setColor(8295258).setBiomeName("rohanUrukHighlands");
        EMYN_MUIL = new LOTREmynMuilBiome(17, true).setTemperatureRainfall(0.5f, 0.9f).setMinMaxHeight(0.2f, 0.8f).setColor(9866354).setBiomeName("emynMuil");
        ITHILIEN = new LOTRIthilienBiome(18, true).setTemperatureRainfall(0.9f, 0.9f).setMinMaxHeight(0.15f, 0.5f).setColor(7710516).setBiomeName("ithilien");
        PELARGIR = new LOTRPelargirBiome(19, true).setTemperatureRainfall(1.0f, 1.0f).setMinMaxHeight(0.08f, 0.2f).setColor(11256145).setBiomeName("pelargir");
        LONE_LANDS = new LOTRLoneLandsBiome(20, true).setTemperatureRainfall(0.6f, 0.5f).setMinMaxHeight(0.15f, 0.4f).setColor(8562762).setBiomeName("loneLands");
        LONE_LANDS_HILLS = new LOTRLoneLandsHillsBiome(21, false).setTemperatureRainfall(0.6f, 0.5f).setMinMaxHeight(0.6f, 0.8f).setColor(8687182).setBiomeName("loneLandsHills");
        DUNLAND = new LOTRDunlandBiome(22, true).setTemperatureRainfall(0.4f, 0.7f).setMinMaxHeight(0.3f, 0.5f).setColor(6920524).setBiomeName("dunland");
        FANGORN = new LOTRFangornBiome(23, true).setTemperatureRainfall(0.7f, 0.8f).setMinMaxHeight(0.2f, 0.4f).setColor(4355353).setBiomeName("fangorn");
        ANGLE = new LOTRAngleBiome(24, true).setTemperatureRainfall(0.6f, 0.8f).setMinMaxHeight(0.15f, 0.3f).setColor(9416527).setBiomeName("angle");
        ETTENMOORS = new LOTREttenmoorsBiome(25, true).setTemperatureRainfall(0.2f, 0.6f).setMinMaxHeight(0.5f, 0.6f).setColor(8161626).setBiomeName("ettenmoors");
        OLD_FOREST = new LOTROldForestBiome(26, true).setTemperatureRainfall(0.5f, 1.0f).setMinMaxHeight(0.2f, 0.3f).setColor(4551995).setBiomeName("oldForest");
        HARONDOR = new LOTRHarondorBiome(27, true).setTemperatureRainfall(1.0f, 0.6f).setMinMaxHeight(0.2f, 0.3f).setColor(10663238).setBiomeName("harondor");
        ERIADOR = new LOTREriadorBiome(28, true).setTemperatureRainfall(0.9f, 0.8f).setMinMaxHeight(0.1f, 0.4f).setColor(7054916).setBiomeName("eriador");
        ERIADOR_DOWNS = new LOTREriadorDownsBiome(29, true).setTemperatureRainfall(0.6f, 0.7f).setMinMaxHeight(0.5f, 0.5f).setColor(7638087).setBiomeName("eriadorDowns");
        ERYN_VORN = new LOTRErynVornBiome(30, false).setTemperatureRainfall(0.8f, 0.9f).setMinMaxHeight(0.1f, 0.4f).setColor(4357965).setBiomeName("erynVorn");
        GREY_MOUNTAINS = new LOTRGreyMountainsBiome(31, true).setTemperatureRainfall(0.28f, 0.2f).setMinMaxHeight(1.8f, 2.0f).setColor(13290689).setBiomeName("greyMountains");
        MIDGEWATER = new LOTRMidgewaterBiome(32, true).setTemperatureRainfall(0.6f, 1.0f).setMinMaxHeight(0.0f, 0.1f).setColor(6001495).setBiomeName("midgewater");
        BROWN_LANDS = new LOTRBrownLandsBiome(33, true).setTemperatureRainfall(1.0f, 0.2f).setMinMaxHeight(0.2f, 0.2f).setColor(8552016).setBiomeName("brownLands");
        OCEAN = new LOTROceanBiome(34, false).setTemperatureRainfall(0.8f, 0.8f).setMinMaxHeight(-1.0f, 0.3f).setColor(153997).setBiomeName("ocean");
        ANDUIN_HILLS = new LOTRAnduinBiome(35, true).setTemperatureRainfall(0.7f, 0.7f).setMinMaxHeight(0.6f, 0.4f).setColor(7058012).setBiomeName("anduinHills");
        MENELTARMA = new LOTRMeneltarmaBiome(36, false).setTemperatureRainfall(0.9f, 0.8f).setMinMaxHeight(0.1f, 0.2f).setColor(9549658).setBiomeName("meneltarma");
        GLADDEN_FIELDS = new LOTRGladdenFieldsBiome(37, true).setTemperatureRainfall(0.6f, 1.2f).setMinMaxHeight(0.0f, 0.1f).setColor(5020505).setBiomeName("gladdenFields");
        LOTHLORIEN_EDGE = new LOTRLothlorienEdgeBiome(38, true).setTemperatureRainfall(0.9f, 1.0f).setMinMaxHeight(0.1f, 0.2f).setColor(13944387).setBiomeName("lothlorienEdge");
        FORODWAITH = new LOTRForodwaithBiome(39, true).setTemperatureRainfall(0.0f, 0.2f).setMinMaxHeight(0.1f, 0.1f).setColor(14211282).setBiomeName("forodwaith");
        ENEDWAITH = new LOTREnedwaithBiome(40, true).setTemperatureRainfall(0.6f, 0.8f).setMinMaxHeight(0.2f, 0.3f).setColor(8038479).setBiomeName("enedwaith");
        ANGMAR = new LOTRAngmarBiome(41, true).setTemperatureRainfall(0.2f, 0.2f).setMinMaxHeight(0.2f, 0.6f).setColor(5523247).setBiomeName("angmar");
        EREGION = new LOTREregionBiome(42, true).setTemperatureRainfall(0.6f, 0.7f).setMinMaxHeight(0.2f, 0.3f).setColor(6656072).setBiomeName("eregion");
        LINDON = new LOTRLindonBiome(43, true).setTemperatureRainfall(0.9f, 0.9f).setMinMaxHeight(0.15f, 0.2f).setColor(7646533).setBiomeName("lindon");
        LINDON_WOODLANDS = new LOTRLindonWoodlandsBiome(44, false).setTemperatureRainfall(0.9f, 1.0f).setMinMaxHeight(0.2f, 0.5f).setColor(1996591).setBiomeName("lindonWoodlands");
        EAST_BIGHT = new LOTREastBightBiome(45, true).setTemperatureRainfall(0.8f, 0.3f).setMinMaxHeight(0.15f, 0.05f).setColor(9082205).setBiomeName("eastBight");
        BLUE_MOUNTAINS = new LOTRBlueMountainsBiome(46, true).setTemperatureRainfall(0.22f, 0.8f).setMinMaxHeight(1.0f, 2.5f).setColor(13228770).setBiomeName("blueMountains");
        MIRKWOOD_MOUNTAINS = new LOTRMirkwoodMountainsBiome(47, true).setTemperatureRainfall(0.28f, 0.9f).setMinMaxHeight(1.2f, 1.5f).setColor(2632989).setBiomeName("mirkwoodMountains");
        WILDERLAND = new LOTRWilderlandBiome(48, true).setTemperatureRainfall(0.9f, 0.4f).setMinMaxHeight(0.2f, 0.4f).setColor(9612368).setBiomeName("wilderland");
        DAGORLAD = new LOTRDagorladBiome(49, true).setTemperatureRainfall(1.0f, 0.2f).setMinMaxHeight(0.1f, 0.05f).setColor(7036741).setBiomeName("dagorlad");
        NURN = new LOTRNurnBiome(50, true).setTemperatureRainfall(0.9f, 0.4f).setMinMaxHeight(0.1f, 0.2f).setColor(2630683).setBiomeName("nurn");
        NURNEN = new LOTRNurnenBiome(51, false).setTemperatureRainfall(0.9f, 0.4f).setMinMaxHeight(-1.0f, 0.3f).setColor(931414).setBiomeName("nurnen");
        NURN_MARSHES = new LOTRNurnMarshesBiome(52, true).setTemperatureRainfall(0.9f, 0.4f).setMinMaxHeight(0.0f, 0.1f).setColor(4012843).setBiomeName("nurnMarshes");
        ADORNLAND = new LOTRAdornlandBiome(53, true).setTemperatureRainfall(0.7f, 0.6f).setMinMaxHeight(0.2f, 0.2f).setColor(7838543).setBiomeName("adornland");
        ANGMAR_MOUNTAINS = new LOTRAngmarMountainsBiome(54, true).setTemperatureRainfall(0.25f, 0.1f).setMinMaxHeight(1.6f, 1.5f).setColor(13619147).setBiomeName("angmarMountains");
        ANDUIN_MOUTH = new LOTRAnduinMouthBiome(55, true).setTemperatureRainfall(0.9f, 1.0f).setMinMaxHeight(0.0f, 0.1f).setColor(5089363).setBiomeName("anduinMouth");
        ENTWASH_MOUTH = new LOTREntwashMouthBiome(56, true).setTemperatureRainfall(0.5f, 1.0f).setMinMaxHeight(0.0f, 0.1f).setColor(5612358).setBiomeName("entwashMouth");
        DOR_EN_ERNIL = new LOTRDorEnErnilBiome(57, true).setTemperatureRainfall(0.9f, 0.9f).setMinMaxHeight(0.07f, 0.2f).setColor(9355077).setBiomeName("dorEnErnil");
        DOR_EN_ERNIL_HILLS = new LOTRDorEnErnilHillsBiome(58, false).setTemperatureRainfall(0.8f, 0.7f).setMinMaxHeight(0.5f, 0.5f).setColor(8560707).setBiomeName("dorEnErnilHills");
        FANGORN_WASTELAND = new LOTRFangornWastelandBiome(59, true).setTemperatureRainfall(0.7f, 0.4f).setMinMaxHeight(0.2f, 0.4f).setColor(6782028).setBiomeName("fangornWasteland");
        ROHAN_WOODLANDS = new LOTRRohanWoodlandsBiome(60, false).setTemperatureRainfall(0.9f, 0.9f).setMinMaxHeight(0.2f, 0.4f).setColor(5736246).setBiomeName("rohanWoodlands");
        GONDOR_WOODLANDS = new LOTRGondorWoodlandsBiome(61, false).setTemperatureRainfall(0.8f, 0.9f).setMinMaxHeight(0.2f, 0.2f).setColor(5867307).setBiomeName("gondorWoodlands");
        LAKE = new LOTRLakeBiome(62, false).setColor(3433630).setBiomeName("lake");
        LINDON_COAST = new LOTRLindonCoastBiome(63, false).setTemperatureRainfall(0.9f, 0.9f).setMinMaxHeight(0.0f, 0.5f).setColor(9278870).setBiomeName("lindonCoast");
        BARROW_DOWNS = new LOTRBarrowDownsBiome(64, true).setTemperatureRainfall(0.6f, 0.7f).setMinMaxHeight(0.3f, 0.4f).setColor(8097362).setBiomeName("barrowDowns");
        LONG_MARSHES = new LOTRLongMarshesBiome(65, true).setTemperatureRainfall(0.6f, 0.9f).setMinMaxHeight(0.0f, 0.1f).setColor(7178054).setBiomeName("longMarshes");
        FANGORN_CLEARING = new LOTRFangornClearingBiome(66, false).setTemperatureRainfall(0.7f, 0.8f).setMinMaxHeight(0.2f, 0.1f).setColor(5877050).setBiomeName("fangornClearing");
        ITHILIEN_HILLS = new LOTRIthilienHillsBiome(67, false).setTemperatureRainfall(0.7f, 0.7f).setMinMaxHeight(0.6f, 0.6f).setColor(6985792).setBiomeName("ithilienHills");
        ITHILIEN_WASTELAND = new LOTRIthilienWastelandBiome(68, true).setTemperatureRainfall(0.6f, 0.6f).setMinMaxHeight(0.15f, 0.2f).setColor(8030031).setBiomeName("ithilienWasteland");
        NINDALF = new LOTRNindalfBiome(69, true).setTemperatureRainfall(0.4f, 1.0f).setMinMaxHeight(0.0f, 0.1f).setColor(7111750).setBiomeName("nindalf");
        COLDFELLS = new LOTRColdfellsBiome(70, true).setTemperatureRainfall(0.25f, 0.8f).setMinMaxHeight(0.4f, 0.8f).setColor(8296018).setBiomeName("coldfells");
        NAN_CURUNIR = new LOTRNanCurunirBiome(71, true).setTemperatureRainfall(0.6f, 0.4f).setMinMaxHeight(0.2f, 0.1f).setColor(7109714).setBiomeName("nanCurunir");
        WHITE_DOWNS = new LOTRWhiteDownsBiome(72, true).setTemperatureRainfall(0.6f, 0.7f).setMinMaxHeight(0.6f, 0.6f).setColor(10210937).setBiomeName("whiteDowns");
        SWANFLEET = new LOTRSwanfleetBiome(73, true).setTemperatureRainfall(0.8f, 1.0f).setMinMaxHeight(0.0f, 0.1f).setColor(6265945).setBiomeName("swanfleet");
        PELENNOR = new LOTRPelennorBiome(74, true).setTemperatureRainfall(0.9f, 0.9f).setMinMaxHeight(0.1f, 0.02f).setColor(11258955).setBiomeName("pelennor");
        MINHIRIATH = new LOTRMinhiriathBiome(75, true).setTemperatureRainfall(0.7f, 0.4f).setMinMaxHeight(0.1f, 0.2f).setColor(7380550).setBiomeName("minhiriath");
        EREBOR = new LOTREreborBiome(76, true).setTemperatureRainfall(0.6f, 0.7f).setMinMaxHeight(0.4f, 0.6f).setColor(7499093).setBiomeName("erebor");
        MIRKWOOD_NORTH = new LOTRMirkwoodNorthBiome(77, true).setTemperatureRainfall(0.7f, 0.7f).setMinMaxHeight(0.2f, 0.4f).setColor(3822115).setBiomeName("mirkwoodNorth");
        WOODLAND_REALM_HILLS = new LOTRWoodlandRealmHillsBiome(78, false).setTemperatureRainfall(0.8f, 0.6f).setMinMaxHeight(0.9f, 0.7f).setColor(3624991).setBiomeName("woodlandRealmHills");
        NAN_UNGOL = new LOTRNanUngolBiome(79, true).setTemperatureRainfall(2.0f, 0.0f).setMinMaxHeight(0.1f, 0.4f).setColor(656641).setBiomeName("nanUngol");
        PINNATH_GELIN = new LOTRPinnathGelinBiome(80, true).setTemperatureRainfall(0.8f, 0.8f).setMinMaxHeight(0.5f, 0.5f).setColor(9946693).setBiomeName("pinnathGelin");
        ISLAND = new LOTROceanBiome(81, false).setTemperatureRainfall(0.9f, 0.8f).setMinMaxHeight(0.0f, 0.3f).setColor(10138963).setBiomeName("island");
        FORODWAITH_MOUNTAINS = new LOTRForodwaithMountainsBiome(82, true).setTemperatureRainfall(0.0f, 0.2f).setMinMaxHeight(2.0f, 2.0f).setColor(15592942).setBiomeName("forodwaithMountains");
        MISTY_MOUNTAINS_FOOTHILLS = new LOTRMistyMountainsFoothillsBiome(83, true).setTemperatureRainfall(0.25f, 0.6f).setMinMaxHeight(0.7f, 0.9f).setColor(12501430).setBiomeName("mistyMountainsFoothills");
        GREY_MOUNTAINS_FOOTHILLS = new LOTRGreyMountainsFoothillsBiome(84, true).setTemperatureRainfall(0.5f, 0.7f).setMinMaxHeight(0.5f, 0.9f).setColor(9148000).setBiomeName("greyMountainsFoothills");
        BLUE_MOUNTAINS_FOOTHILLS = new LOTRBlueMountainsFoothillsBiome(85, true).setTemperatureRainfall(0.5f, 0.8f).setMinMaxHeight(0.5f, 0.9f).setColor(11253170).setBiomeName("blueMountainsFoothills");
        TUNDRA = new LOTRTundraBiome(86, true).setTemperatureRainfall(0.1f, 0.3f).setMinMaxHeight(0.1f, 0.2f).setColor(12366486).setBiomeName("tundra");
        TAIGA = new LOTRTaigaBiome(87, true).setTemperatureRainfall(0.1f, 0.7f).setMinMaxHeight(0.1f, 0.5f).setColor(6526543).setBiomeName("taiga");
        BREELAND = new LOTRBreelandBiome(88, true).setTemperatureRainfall(0.8f, 0.7f).setMinMaxHeight(0.1f, 0.2f).setColor(6861625).setBiomeName("breeland");
        CHETWOOD = new LOTRChetwoodBiome(89, true).setTemperatureRainfall(0.8f, 0.9f).setMinMaxHeight(0.2f, 0.4f).setColor(4424477).setBiomeName("chetwood");
        FORODWAITH_GLACIER = new LOTRForodwaithGlacierBiome(90, true).setTemperatureRainfall(0.0f, 0.1f).setMinMaxHeight(1.0f, 0.1f).setColor(9424096).setBiomeName("forodwaithGlacier");
        WHITE_MOUNTAINS_FOOTHILLS = new LOTRWhiteMountainsFoothillsBiome(91, true).setTemperatureRainfall(0.6f, 0.7f).setMinMaxHeight(0.5f, 0.9f).setColor(12635575).setBiomeName("whiteMountainsFoothills");
        BEACH = new LOTRBeachBiome(92, false).setBeachBlock(Blocks.SAND.defaultBlockState()).setColor(14404247).setBiomeName("beach");
        BEACH_GRAVEL = new LOTRBeachBiome(93, false).setBeachBlock(Blocks.GRAVEL.defaultBlockState()).setColor(9868704).setBiomeName("beachGravel");
        NEAR_HARAD = new LOTRNearHaradBiome(94, true).setTemperatureRainfall(1.5f, 0.1f).setMinMaxHeight(0.2f, 0.1f).setColor(14205815).setBiomeName("nearHarad");
        FAR_HARAD = new LOTRFarHaradSavannahBiome(95, true).setTemperatureRainfall(1.2f, 0.2f).setMinMaxHeight(0.1f, 0.1f).setColor(9740353).setBiomeName("farHarad");
        HARAD_MOUNTAINS = new LOTRHaradMountainsBiome(96, true).setTemperatureRainfall(0.9f, 0.5f).setMinMaxHeight(1.8f, 2.0f).setColor(9867381).setBiomeName("haradMountains");
        UMBAR = new LOTRUmbarBiome(97, true).setTemperatureRainfall(0.9f, 0.6f).setMinMaxHeight(0.1f, 0.2f).setColor(9542740).setBiomeName("umbar");
        FAR_HARAD_JUNGLE = new LOTRFarHaradJungleBiome(98, true).setTemperatureRainfall(1.2f, 0.9f).setMinMaxHeight(0.2f, 0.4f).setColor(4944931).setBiomeName("farHaradJungle");
        UMBAR_HILLS = new LOTRUmbarBiome(99, false).setTemperatureRainfall(0.8f, 0.5f).setMinMaxHeight(1.2f, 0.8f).setColor(8226378).setBiomeName("umbarHills");
        NEAR_HARAD_HILLS = new LOTRNearHaradHillsBiome(100, false).setTemperatureRainfall(1.2f, 0.3f).setMinMaxHeight(0.5f, 0.8f).setColor(12167010).setBiomeName("nearHaradHills");
        FAR_HARAD_JUNGLE_LAKE = new LOTRFarHaradJungleLakeBiome(101, false).setTemperatureRainfall(1.2f, 0.9f).setMinMaxHeight(-0.5f, 0.2f).setColor(2271948).setBiomeName("farHaradJungleLake");
        LOSTLADEN = new LOTRLostladenBiome(102, true).setTemperatureRainfall(1.2f, 0.2f).setMinMaxHeight(0.2f, 0.1f).setColor(10658661).setBiomeName("lostladen");
        FAR_HARAD_FOREST = new LOTRFarHaradForestBiome(103, true).setTemperatureRainfall(1.0f, 1.0f).setMinMaxHeight(0.3f, 0.4f).setColor(3703325).setBiomeName("farHaradForest");
        NEAR_HARAD_FERTILE = new LOTRNearHaradFertileBiome(104, true).setTemperatureRainfall(1.2f, 0.7f).setMinMaxHeight(0.2f, 0.1f).setColor(10398286).setBiomeName("nearHaradFertile");
        PERTOROGWAITH = new LOTRPertorogwaithBiome(105, true).setTemperatureRainfall(0.7f, 0.1f).setMinMaxHeight(0.2f, 0.5f).setColor(8879706).setBiomeName("pertorogwaith");
        UMBAR_FOREST = new LOTRUmbarForestBiome(106, false).setTemperatureRainfall(0.8f, 0.8f).setMinMaxHeight(0.2f, 0.3f).setColor(7178042).setBiomeName("umbarForest");
        FAR_HARAD_JUNGLE_EDGE = new LOTRFarHaradJungleEdgeBiome(107, true).setTemperatureRainfall(1.2f, 0.8f).setMinMaxHeight(0.2f, 0.2f).setColor(7440430).setBiomeName("farHaradJungleEdge");
        TAUREDAIN_CLEARING = new LOTRTauredainClearingBiome(108, true).setTemperatureRainfall(1.2f, 0.8f).setMinMaxHeight(0.2f, 0.2f).setColor(10796101).setBiomeName("tauredainClearing");
        GULF_HARAD = new LOTRGulfHaradBiome(109, true).setTemperatureRainfall(1.0f, 0.5f).setMinMaxHeight(0.15f, 0.1f).setColor(9152592).setBiomeName("gulfHarad");
        DORWINION_HILLS = new LOTRDorwinionHillsBiome(110, true).setTemperatureRainfall(0.9f, 0.8f).setMinMaxHeight(0.8f, 0.8f).setColor(13357993).setBiomeName("dorwinionHills");
        TOLFALAS = new LOTRTolfalasBiome(111, true).setTemperatureRainfall(0.8f, 0.4f).setMinMaxHeight(0.3f, 1.0f).setColor(10199149).setBiomeName("tolfalas");
        LEBENNIN = new LOTRLebenninBiome(112, true).setTemperatureRainfall(1.0f, 0.9f).setMinMaxHeight(0.1f, 0.3f).setColor(7845418).setBiomeName("lebennin");
        RHUN = new LOTRRhunBiome(113, true).setTemperatureRainfall(0.9f, 0.3f).setMinMaxHeight(0.3f, 0.0f).setColor(10465880).setBiomeName("rhun");
        RHUN_FOREST = new LOTRRhunForestBiome(114, true).setTemperatureRainfall(0.8f, 0.9f).setMinMaxHeight(0.3f, 0.5f).setColor(7505723).setBiomeName("rhunForest");
        RED_MOUNTAINS = new LOTRRedMountainsBiome(115, true).setTemperatureRainfall(0.3f, 0.4f).setMinMaxHeight(1.5f, 2.0f).setColor(9662796).setBiomeName("redMountains");
        RED_MOUNTAINS_FOOTHILLS = new LOTRRedMountainsFoothillsBiome(116, true).setTemperatureRainfall(0.7f, 0.4f).setMinMaxHeight(0.5f, 0.9f).setColor(10064978).setBiomeName("redMountainsFoothills");
        DOL_GULDUR = new LOTRDolGuldurBiome(117, true).setTemperatureRainfall(0.6f, 0.8f).setMinMaxHeight(0.2f, 0.5f).setColor(2371343).setBiomeName("dolGuldur");
        NEAR_HARAD_SEMI_DESERT = new LOTRNearHaradSemiDesertBiome(118, true).setTemperatureRainfall(1.5f, 0.2f).setMinMaxHeight(0.2f, 0.1f).setColor(12434282).setBiomeName("nearHaradSemiDesert");
        FAR_HARAD_ARID = new LOTRFarHaradAridBiome(119, true).setTemperatureRainfall(1.5f, 0.3f).setMinMaxHeight(0.2f, 0.15f).setColor(11185749).setBiomeName("farHaradArid");
        FAR_HARAD_ARID_HILLS = new LOTRFarHaradAridBiome(120, false).setTemperatureRainfall(1.5f, 0.3f).setMinMaxHeight(1.0f, 0.6f).setColor(10063195).setBiomeName("farHaradAridHills");
        FAR_HARAD_SWAMP = new LOTRFarHaradSwampBiome(121, true).setTemperatureRainfall(0.8f, 1.0f).setMinMaxHeight(0.0f, 0.1f).setColor(5608267).setBiomeName("farHaradSwamp");
        FAR_HARAD_CLOUD_FOREST = new LOTRFarHaradCloudForestBiome(122, true).setTemperatureRainfall(1.2f, 1.2f).setMinMaxHeight(0.7f, 0.4f).setColor(3046208).setBiomeName("farHaradCloudForest");
        FAR_HARAD_BUSHLAND = new LOTRFarHaradBushlandBiome(123, true).setTemperatureRainfall(1.0f, 0.4f).setMinMaxHeight(0.2f, 0.1f).setColor(10064190).setBiomeName("farHaradBushland");
        FAR_HARAD_BUSHLAND_HILLS = new LOTRFarHaradBushlandBiome(124, false).setTemperatureRainfall(0.8f, 0.4f).setMinMaxHeight(0.8f, 0.8f).setColor(8354100).setBiomeName("farHaradBushlandHills");
        FAR_HARAD_MANGROVE = new LOTRFarHaradMangroveBiome(125, true).setTemperatureRainfall(1.0f, 0.9f).setMinMaxHeight(-0.05f, 0.05f).setColor(8883789).setBiomeName("farHaradMangrove");
        NEAR_HARAD_FERTILE_FOREST = new LOTRNearHaradFertileForestBiome(126, false).setTemperatureRainfall(1.2f, 1.0f).setMinMaxHeight(0.2f, 0.4f).setColor(6915122).setBiomeName("nearHaradFertileForest");
        ANDUIN_VALE = new LOTRAnduinValeBiome(127, true).setTemperatureRainfall(0.9f, 1.1f).setMinMaxHeight(0.05f, 0.05f).setColor(7447880).setBiomeName("anduinVale");
        WOLD = new LOTRWoldBiome(128, true).setTemperatureRainfall(0.9f, 0.1f).setMinMaxHeight(0.4f, 0.3f).setColor(9483599).setBiomeName("wold");
        SHIRE_MOORS = new LOTRShireMoorsBiome(129, true).setTemperatureRainfall(0.6f, 1.6f).setMinMaxHeight(0.4f, 0.6f).setColor(6921036).setBiomeName("shireMoors");
        SHIRE_MARSHES = new LOTRShireMarshesBiome(130, true).setTemperatureRainfall(0.8f, 1.2f).setMinMaxHeight(0.0f, 0.1f).setColor(4038751).setBiomeName("shireMarshes");
        NEAR_HARAD_RED_DESERT = new LOTRNearHaradRedBiome(131, true).setTemperatureRainfall(1.5f, 0.1f).setMinMaxHeight(0.2f, 0.0f).setColor(13210447).setBiomeName("nearHaradRedDesert");
        FAR_HARAD_VOLCANO = new LOTRFarHaradVolcanoBiome(132, true).setTemperatureRainfall(1.5f, 0.0f).setMinMaxHeight(0.6f, 1.2f).setColor(6838068).setBiomeName("farHaradVolcano");
        UDUN = new LOTRUdunBiome(133, true).setTemperatureRainfall(1.5f, 0.0f).setMinMaxHeight(0.2f, 0.7f).setColor(65536).setBiomeName("udun");
        GORGOROTH = new LOTRGorgorothBiome(134, true).setTemperatureRainfall(2.0f, 0.0f).setMinMaxHeight(0.6f, 0.2f).setColor(2170141).setBiomeName("gorgoroth");
        MORGUL_VALE = new LOTRMorgulValeBiome(135, true).setTemperatureRainfall(1.0f, 0.0f).setMinMaxHeight(0.2f, 0.1f).setColor(1387801).setBiomeName("morgulVale");
        EASTERN_DESOLATION = new LOTREasternDesolationBiome(136, true).setTemperatureRainfall(1.0f, 0.3f).setMinMaxHeight(0.2f, 0.2f).setColor(6052935).setBiomeName("easternDesolation");
        DALE = new LOTRDaleBiome(137, true).setTemperatureRainfall(0.8f, 0.7f).setMinMaxHeight(0.1f, 0.2f).setColor(8233807).setBiomeName("dale");
        DORWINION = new LOTRDorwinionBiome(138, true).setTemperatureRainfall(0.9f, 0.9f).setMinMaxHeight(0.1f, 0.3f).setColor(7120197).setBiomeName("dorwinion");
        TOWER_HILLS = new LOTRTowerHillsBiome(139, true).setTemperatureRainfall(0.8f, 0.8f).setMinMaxHeight(0.5f, 0.5f).setColor(6854209).setBiomeName("towerHills");
        GULF_HARAD_FOREST = new LOTRGulfHaradForestBiome(140, false).setTemperatureRainfall(1.0f, 1.0f).setMinMaxHeight(0.2f, 0.4f).setColor(5868590).setBiomeName("gulfHaradForest");
        WILDERLAND_NORTH = new LOTRWilderlandNorthBiome(141, true).setTemperatureRainfall(0.6f, 0.6f).setMinMaxHeight(0.2f, 0.5f).setColor(9676396).setBiomeName("wilderlandNorth");
        FORODWAITH_COAST = new LOTRForodwaithCoastBiome(142, false).setTemperatureRainfall(0.0f, 0.4f).setMinMaxHeight(0.0f, 0.5f).setColor(9214637).setBiomeName("forodwaithCoast");
        FAR_HARAD_COAST = new LOTRFarHaradCoastBiome(143, false).setTemperatureRainfall(1.2f, 0.8f).setMinMaxHeight(0.0f, 0.5f).setColor(8356472).setBiomeName("farHaradCoast");
        NEAR_HARAD_RIVERBANK = new LOTRNearHaradRiverbankBiome(144, false).setTemperatureRainfall(1.2f, 0.8f).setMinMaxHeight(0.1f, 0.1f).setColor(7183952).setBiomeName("nearHaradRiverbank");
        LOSSARNACH = new LOTRLossarnachBiome(145, true).setTemperatureRainfall(1.0f, 1.0f).setMinMaxHeight(0.1f, 0.2f).setColor(8439086).setBiomeName("lossarnach");
        IMLOTH_MELUI = new LOTRImlothMeluiBiome(146, true).setTemperatureRainfall(1.0f, 1.2f).setMinMaxHeight(0.1f, 0.2f).setColor(14517608).setBiomeName("imlothMelui");
        NEAR_HARAD_OASIS = new LOTRNearHaradOasisBiome(147, false).setTemperatureRainfall(1.2f, 0.8f).setMinMaxHeight(0.1f, 0.1f).setColor(832768).setBiomeName("nearHaradOasis");
        BEACH_WHITE = new LOTRBeachBiome(148, false).setBeachBlock(LOTRLegacyBlocks.mod("whiteSand").state()).setColor(15592941).setBiomeName("beachWhite");
        HARNEDOR = new LOTRHarnedorBiome(149, true).setTemperatureRainfall(1.0f, 0.3f).setMinMaxHeight(0.1f, 0.3f).setColor(11449173).setBiomeName("harnedor");
        LAMEDON = new LOTRLamedonBiome(150, true).setTemperatureRainfall(0.9f, 0.5f).setMinMaxHeight(0.2f, 0.2f).setColor(10927460).setBiomeName("lamedon");
        LAMEDON_HILLS = new LOTRLamedonHillsBiome(151, true).setTemperatureRainfall(0.6f, 0.4f).setMinMaxHeight(0.6f, 0.9f).setColor(13555369).setBiomeName("lamedonHills");
        BLACKROOT_VALE = new LOTRBlackrootValeBiome(152, true).setTemperatureRainfall(0.8f, 0.9f).setMinMaxHeight(0.2f, 0.12f).setColor(7183921).setBiomeName("blackrootVale");
        ANDRAST = new LOTRAndrastBiome(153, true).setTemperatureRainfall(0.8f, 0.8f).setMinMaxHeight(0.2f, 0.2f).setColor(8885856).setBiomeName("andrast");
        PUKEL = new LOTRPukelBiome(154, true).setTemperatureRainfall(0.7f, 0.7f).setMinMaxHeight(0.2f, 0.4f).setColor(5667394).setBiomeName("pukel");
        RHUN_LAND = new LOTRRhunLandBiome(155, true).setTemperatureRainfall(1.0f, 0.8f).setMinMaxHeight(0.1f, 0.3f).setColor(11381583).setBiomeName("rhunLand");
        RHUN_LAND_STEPPE = new LOTRRhunLandSteppeBiome(156, true).setTemperatureRainfall(1.0f, 0.3f).setMinMaxHeight(0.2f, 0.05f).setColor(11712354).setBiomeName("rhunLandSteppe");
        RHUN_LAND_HILLS = new LOTRRhunLandHillsBiome(157, true).setTemperatureRainfall(1.0f, 0.5f).setMinMaxHeight(0.6f, 0.8f).setColor(9342286).setBiomeName("rhunLandHills");
        RHUN_RED_FOREST = new LOTRRhunRedForestBiome(158, true).setTemperatureRainfall(0.9f, 1.0f).setMinMaxHeight(0.1f, 0.3f).setColor(9530430).setBiomeName("rhunRedForest");
        RHUN_ISLAND = new LOTRRhunIslandBiome(159, false).setTemperatureRainfall(1.0f, 0.8f).setMinMaxHeight(0.1f, 0.4f).setColor(10858839).setBiomeName("rhunIsland");
        RHUN_ISLAND_FOREST = new LOTRRhunIslandForestBiome(160, false).setTemperatureRainfall(0.9f, 1.0f).setMinMaxHeight(0.1f, 0.4f).setColor(9533758).setBiomeName("rhunIslandForest");
        LAST_DESERT = new LOTRLastDesertBiome(161, true).setTemperatureRainfall(0.7f, 0.0f).setMinMaxHeight(0.2f, 0.05f).setColor(13878151).setBiomeName("lastDesert");
        WIND_MOUNTAINS = new LOTRWindMountainsBiome(162, true).setTemperatureRainfall(0.28f, 0.2f).setMinMaxHeight(2.0f, 2.0f).setColor(13882323).setBiomeName("windMountains");
        WIND_MOUNTAINS_FOOTHILLS = new LOTRWindMountainsFoothillsBiome(163, true).setTemperatureRainfall(0.4f, 0.6f).setMinMaxHeight(0.5f, 0.6f).setColor(10133354).setBiomeName("windMountainsFoothills");
        RIVENDELL = new LOTRRivendellBiome(164, true).setTemperatureRainfall(0.9f, 1.0f).setMinMaxHeight(0.15f, 0.3f).setColor(8828714).setBiomeName("rivendell");
        RIVENDELL_HILLS = new LOTRRivendellHillsBiome(165, true).setTemperatureRainfall(0.7f, 0.8f).setMinMaxHeight(2.0f, 0.5f).setColor(14210481).setBiomeName("rivendellHills");
        FAR_HARAD_JUNGLE_MOUNTAINS = new LOTRFarHaradJungleMountainsBiome(166, true).setTemperatureRainfall(1.0f, 1.0f).setMinMaxHeight(1.8f, 1.5f).setColor(6511174).setBiomeName("farHaradJungleMountains");
        HALF_TROLL_FOREST = new LOTRHalfTrollForestBiome(167, true).setTemperatureRainfall(0.8f, 0.2f).setMinMaxHeight(0.3f, 0.4f).setColor(5992500).setBiomeName("halfTrollForest");
        FAR_HARAD_KANUKA = new LOTRKanukaBiome(168, true).setTemperatureRainfall(1.0f, 1.0f).setMinMaxHeight(0.3f, 0.5f).setColor(5142552).setBiomeName("farHaradKanuka");
        for (LOTRBiome biome : LOTRDimension.MIDDLE_EARTH.biomeList) {
            if (biome != null) {
                BY_KEY.put(biome.key, biome);
            }
        }
    }

    /** The Middle-earth biome a vanilla biome stands for, or null for any other. */
    public static @Nullable LOTRBiome byKey(ResourceKey<Biome> key) {
        return BY_KEY.get(key);
    }

    public static @Nullable LOTRBiome of(Holder<Biome> biome) {
        return biome.unwrapKey().map(BY_KEY::get).orElse(null);
    }

    /**
     * {@code biome instanceof LOTRBiomeGen<name>}: whether the biome at a position is of the class
     * of the biome by that original name (so of a subclass too, as the original's checks were).
     */
    public static boolean isBiomeOfClass(Holder<Biome> holder, String name) {
        LOTRBiome actual = of(holder);
        if (actual == null) {
            return false;
        }
        for (LOTRBiome b : LOTRDimension.MIDDLE_EARTH.biomeList) {
            if (b != null && b.biomeName.equals(name)) {
                return b.getClass().isInstance(actual);
            }
        }
        return false;
    }
}
