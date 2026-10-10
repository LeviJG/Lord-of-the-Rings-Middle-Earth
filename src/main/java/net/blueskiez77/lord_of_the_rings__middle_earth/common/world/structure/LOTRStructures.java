package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dunland.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.halftroll.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rhun.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.*;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.*;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRStructures: every structure a structure spawner can build, by the
 * original's id, with its name (for {@code lotr.structure.<name>}) and the two
 * colours its spawner is drawn in.
 *
 * <p>Villages are registered here too, built whole from a spawner.
 */
public final class LOTRStructures {

    private static final Map<Integer, StructureInfo> STRUCTURES = new LinkedHashMap<>();

    /** A structure or a village a spawner builds, standing on the block above the one used. */
    public interface IStructureProvider {
        boolean generateStructure(ServerLevel level, Player player, int i, int j, int k);

        boolean isVillage();
    }

    /** StructureColorInfo, and the name and builder with it. */
    public record StructureInfo(int id, String name, IStructureProvider provider, int colorBackground,
                                int colorForeground, boolean isVillage, boolean isHidden) {
    }

    private LOTRStructures() {
    }

    public static void init() {
        registerStructure(1, LOTRHobbitHoleStructure::new, "HobbitHole", 2727977, 8997164);
        registerStructure(2, LOTRHobbitTavernStructure::new, "HobbitTavern", 9324081, 15975807);
        registerOldStructure(3, LOTRHobbitPicnicBenchStructure::new, "HobbitPicnicBench", 7032622, 13882323);
        registerStructure(4, LOTRHobbitWindmillStructure::new, "HobbitWindmill", 9324081, 15975807);
        registerStructure(5, LOTRHobbitFarmStructure::new, "HobbitFarm", 9324081, 15975807);
        registerStructure(6, LOTRHayBalesStructure::new, "HayBale", 14863437, 11499334);
        registerStructure(7, LOTRHobbitHouseStructure::new, "HobbitHouse", 9324081, 15975807);
        registerStructure(8, LOTRHobbitBurrowStructure::new, "HobbitBurrow", 9324081, 15975807);
        registerStructure(20, LOTRBreeHouseStructure::new, "BreeHouse", 7366748, 13547379);
        registerStructure(21, LOTRBreeOfficeStructure::new, "BreeOffice", 7366748, 13547379);
        registerStructure(22, LOTRBreeSmithyStructure::new, "BreeSmithy", 7895160, 13547379);
        registerStructure(23, LOTRBreeInnStructure::new, "BreeInn", 7366748, 13547379);
        registerStructure(24, LOTRBreeWellStructure::new, "BreeWell", 7366748, 13547379);
        registerStructure(25, LOTRBreeLampPostStructure::new, "BreeLampPost", 7366748, 13547379);
        registerStructure(26, LOTRBreeBarnStructure::new, "BreeBarn", 7366748, 13547379);
        registerStructure(27, LOTRBreeRuffianHouseStructure::new, "BreeRuffianHouse", 7366748, 13547379);
        registerStructure(28, LOTRBreeStableStructure::new, "BreeStables", 7366748, 13547379);
        registerStructure(29, LOTRBreeGardenStructure::new, "BreeGarden", 3056942, 9012349);
        registerStructure(30, LOTRBreeHobbitBurrowStructure::new, "BreeHobbitBurrow", 7366748, 13547379);
        registerStructure(31, LOTRBreeMarketStallStructure.Baker::new, "BreeMarketBaker", 16246393, 13547379);
        registerStructure(32, LOTRBreeMarketStallStructure.Butcher::new, "BreeMarketButcher", 14173509, 13547379);
        registerStructure(33, LOTRBreeMarketStallStructure.Brewer::new, "BreeMarketBrewer", 11368000, 13547379);
        registerStructure(34, LOTRBreeMarketStallStructure.Mason::new, "BreeMarketMason", 8948105, 13547379);
        registerStructure(35, LOTRBreeMarketStallStructure.Lumber::new, "BreeMarketLumber", 7160619, 13547379);
        registerStructure(36, LOTRBreeMarketStallStructure.Smith::new, "BreeMarketSmith", 5658198, 13547379);
        registerStructure(37, LOTRBreeMarketStallStructure.Florist::new, "BreeMarketFlorist", 10966702, 13547379);
        registerStructure(38, LOTRBreeMarketStallStructure.Farmer::new, "BreeMarketFarmer", 5137960, 13547379);
        registerStructure(39, LOTRBreeMarketStructure::new, "BreeMarket", 7366748, 13547379);
        registerVillage(40, new LOTRVillageGenBree("breeland", 1.0f), "BreeHamlet", 7366748, 13547379, (IVillageProperties<LOTRVillageGenBree.Instance>) instance -> instance.villageType = LOTRVillageGenBree.VillageType.HAMLET);
        registerVillage(41, new LOTRVillageGenBree("breeland", 1.0f), "BreeVillage", 7366748, 13547379, (IVillageProperties<LOTRVillageGenBree.Instance>) instance -> instance.villageType = LOTRVillageGenBree.VillageType.VILLAGE);
        registerStructure(42, LOTRBreeGateStructure::new, "BreeGate", 7366748, 13547379);
        registerStructure(43, LOTRBreeGatehouseStructure::new, "BreeGatehouse", 7366748, 13547379);
        registerStructure(50, LOTRBlueMountainsHouseStructure::new, "BlueMountainsHouse", 10397380, 7633815);
        registerOldStructure(51, LOTRBlueMountainsStrongholdStructure::new, "BlueMountainsStronghold", 10397380, 7633815);
        registerStructure(52, LOTRBlueMountainsSmithyStructure::new, "BlueMountainsSmithy", 10397380, 7633815);
        registerOldStructure(60, LOTRHighElvenTurretStructure::new, "HighElvenTurret", 13419962, 11380637);
        registerOldStructure(61, LOTRRuinedHighElvenTurretStructure::new, "RuinedHighElvenTurret", 13419962, 11380637);
        registerOldStructure(62, LOTRHighElvenHallStructure::new, "HighElvenHall", 13419962, 11380637);
        registerOldStructure(63, LOTRUnderwaterElvenRuinStructure::new, "UnderwaterElvenRuin", 13419962, 11380637);
        registerStructure(64, LOTRHighElvenForgeStructure::new, "HighElvenForge", 13419962, 11380637);
        registerStructure(65, LOTRRuinedEregionForgeStructure::new, "RuinedEregionForge", 13419962, 11380637);
        registerStructure(66, LOTRHighElvenTowerStructure::new, "HighElvenTower", 13419962, 11380637);
        registerStructure(67, LOTRTowerHillsTowerStructure::new, "TowerHillsTower", 16250346, 14211019);
        registerStructure(68, LOTRHighElfHouseStructure::new, "HighElfHouse", 13419962, 11380637);
        registerStructure(69, LOTRRivendellHouseStructure::new, "RivendellHouse", 13419962, 11380637);
        registerOldStructure(70, LOTRRivendellHallStructure::new, "RivendellHall", 13419962, 11380637);
        registerStructure(71, LOTRRivendellForgeStructure::new, "RivendellForge", 13419962, 11380637);
        registerOldStructure(80, LOTRRuinedDunedainTowerStructure::new, "RuinedDunedainTower", 8947848, 6052956);
        registerStructure(81, LOTRRuinedHouseStructure::new, "RuinedHouse", 8355197, 6838845);
        registerStructure(82, LOTRRangerTentStructure::new, "RangerTent", 3755037, 4142111);
        registerStructure(83, LOTRNumenorRuinStructure::new, "NumenorRuin", 8947848, 6052956);
        registerStructure(84, LOTRBDBarrowStructure::new, "BDBarrow", 6586202, 6505786);
        registerStructure(85, LOTRRangerWatchtowerStructure::new, "RangerWatchtower", 5982252, 13411436);
        registerStructure(86, LOTRBurntHouseStructure::new, "BurntHouse", 1117449, 3288357);
        registerStructure(87, LOTRRottenHouseStructure::new, "RottenHouse", 3026204, 5854007);
        registerStructure(88, LOTRRangerHouseStructure::new, "RangerHouse", 5982252, 13411436);
        registerStructure(89, LOTRRangerLodgeStructure::new, "RangerLodge", 5982252, 13411436);
        registerStructure(90, LOTRRangerStablesStructure::new, "RangerStables", 5982252, 13411436);
        registerStructure(91, LOTRRangerSmithyStructure::new, "RangerSmithy", 5982252, 13411436);
        registerStructure(92, LOTRRangerWellStructure::new, "RangerWell", 5982252, 13411436);
        registerStructure(93, LOTRRangerVillageLightStructure::new, "RangerVillageLight", 5982252, 13411436);
        registerVillage(94, new LOTRVillageGenDunedain("angle", 1.0f), "DunedainVillage", 5982252, 13411436, (IVillageProperties<LOTRVillageGenDunedain.Instance>) instance -> instance.villageType = LOTRVillageGenDunedain.VillageType.VILLAGE);
        registerStructure(95, LOTRRangerCampStructure::new, "RangerCamp", 3755037, 4142111);
        registerOldStructure(120, LOTROrcDungeonStructure::new, "OrcDungeon", 8947848, 6052956);
        registerStructure(121, LOTRGundabadTentStructure::new, "GundabadTent", 2301210, 131586);
        registerStructure(122, LOTRGundabadForgeTentStructure::new, "GundabadForgeTent", 2301210, 131586);
        registerStructure(123, LOTRGundabadCampStructure::new, "GundabadCamp", 2301210, 131586);
        registerOldStructure(140, LOTRAngmarTowerStructure::new, "AngmarTower", 3815994, 1644825);
        registerOldStructure(141, LOTRAngmarShrineStructure::new, "AngmarShrine", 3815994, 1644825);
        registerStructure(142, LOTRAngmarWargPitStructure::new, "AngmarWargPit", 3815994, 1644825);
        registerStructure(143, LOTRAngmarTentStructure::new, "AngmarTent", 2301210, 131586);
        registerStructure(144, LOTRAngmarForgeTentStructure::new, "AngmarForgeTent", 3815994, 1644825);
        registerStructure(145, LOTRAngmarCampStructure::new, "AngmarCamp", 2301210, 131586);
        registerStructure(160, LOTRAngmarHillmanHouseStructure::new, "AngmarHillmanHouse", 6705465, 3813154);
        registerStructure(161, LOTRAngmarHillmanChieftainHouseStructure::new, "AngmarHillmanChieftainHouse", 6705465, 3813154);
        registerStructure(162, LOTRRhudaurCastleStructure::new, "RhudaurCastle", 3815994, 1644825);
        registerOldStructure(200, LOTRWoodElfPlatformStructure::new, "WoodElfLookoutPlatform", 2498840, 4932405);
        registerStructure(201, LOTRWoodElfHouseStructure::new, "WoodElfHouse", 2498840, 1004574);
        registerOldStructure(202, LOTRWoodElfTowerStructure::new, "WoodElfTower", 12692892, 9733494);
        registerOldStructure(203, LOTRRuinedWoodElfTowerStructure::new, "RuinedWoodElfTower", 12692892, 9733494);
        registerStructure(204, LOTRWoodElvenForgeStructure::new, "WoodElvenForge", 12692892, 9733494);
        registerStructure(220, LOTRDolGuldurAltarStructure::new, "DolGuldurAltar", 4408654, 2040101);
        registerStructure(221, LOTRDolGuldurTowerStructure::new, "DolGuldurTower", 4408654, 2040101);
        registerStructure(222, LOTRDolGuldurSpiderPitStructure::new, "DolGuldurSpiderPit", 4408654, 2040101);
        registerStructure(223, LOTRDolGuldurTentStructure::new, "DolGuldurTent", 2301210, 131586);
        registerStructure(224, LOTRDolGuldurForgeTentStructure::new, "DolGuldurForgeTent", 4408654, 2040101);
        registerStructure(225, LOTRDolGuldurCampStructure::new, "DolGuldurCamp", 2301210, 131586);
        registerStructure(240, LOTRDaleWatchtowerStructure::new, "DaleWatchtower", 13278568, 6836795);
        registerStructure(241, LOTRDaleFortressStructure::new, "DaleFortress", 13278568, 6836795);
        registerStructure(242, LOTRDaleHouseStructure::new, "DaleHouse", 13278568, 6836795);
        registerStructure(243, LOTRDaleSmithyStructure::new, "DaleSmithy", 13278568, 6836795);
        registerStructure(244, LOTRDaleVillageTowerStructure::new, "DaleVillageTower", 13278568, 6836795);
        registerStructure(245, LOTRDaleBakeryStructure::new, "DaleBakery", 13278568, 6836795);
        registerStructure(260, LOTRDwarvenMineEntranceStructure::new, "DwarvenMineEntrance", 4935761, 2961971);
        registerStructure(261, LOTRDwarvenTowerStructure::new, "DwarvenTower", 4935761, 2961971);
        registerStructure(262, LOTRDwarfHouseStructure::new, "DwarfHouse", 4935761, 2961971);
        registerStructure(263, LOTRDwarvenMineEntranceRuinedStructure::new, "DwarvenMineEntranceRuined", 4935761, 2961971);
        registerStructure(264, LOTRDwarfSmithyStructure::new, "DwarfSmithy", 4935761, 2961971);
        registerStructure(265, LOTRRuinedDwarvenTowerStructure::new, "DwarvenTowerRuined", 4935761, 2961971);
        registerStructure(280, LOTRElfHouseStructure::new, "ElfHouse", 15325615, 2315809);
        registerOldStructure(281, LOTRElfLordHouseStructure::new, "ElfLordHouse", 15325615, 2315809);
        registerStructure(282, LOTRGaladhrimForgeStructure::new, "GaladhrimForge", 14407118, 10854552);
        registerStructure(300, LOTRMeadHallStructure::new, "RohanMeadHall", 5982252, 13411436);
        registerStructure(301, LOTRRohanWatchtowerStructure::new, "RohanWatchtower", 5982252, 13411436);
        registerOldStructure(302, LOTRRohanBarrowStructure::new, "RohanBarrow", 9016133, 16775901);
        registerStructure(303, LOTRRohanFortressStructure::new, "RohanFortress", 5982252, 13411436);
        registerStructure(304, LOTRRohanHouseStructure::new, "RohanHouse", 5982252, 13411436);
        registerStructure(305, LOTRRohanSmithyStructure::new, "RohanSmithy", 5982252, 13411436);
        registerStructure(306, LOTRRohanVillageFarmStructure::new, "RohanVillageFarm", 7648578, 8546111);
        registerStructure(307, LOTRRohanStablesStructure::new, "RohanStables", 5982252, 13411436);
        registerStructure(308, LOTRRohanBarnStructure::new, "RohanBarn", 5982252, 13411436);
        registerStructure(309, LOTRRohanWellStructure::new, "RohanWell", 5982252, 13411436);
        registerStructure(310, LOTRRohanVillageGardenStructure::new, "RohanVillageGarden", 7648578, 8546111);
        registerStructure(311, LOTRRohanMarketStallStructure.Blacksmith::new, "RohanMarketBlacksmith", 2960684, 13411436);
        registerStructure(312, LOTRRohanMarketStallStructure.Farmer::new, "RohanMarketFarmer", 15066597, 13411436);
        registerStructure(313, LOTRRohanMarketStallStructure.Lumber::new, "RohanMarketLumber", 5981994, 13411436);
        registerStructure(314, LOTRRohanMarketStallStructure.Builder::new, "RohanMarketBuilder", 7693401, 13411436);
        registerStructure(315, LOTRRohanMarketStallStructure.Brewer::new, "RohanMarketBrewer", 13874218, 13411436);
        registerStructure(316, LOTRRohanMarketStallStructure.Butcher::new, "RohanMarketButcher", 16358066, 13411436);
        registerStructure(317, LOTRRohanMarketStallStructure.Fish::new, "RohanMarketFish", 9882879, 13411436);
        registerStructure(318, LOTRRohanMarketStallStructure.Baker::new, "RohanMarketBaker", 14725995, 13411436);
        registerStructure(319, LOTRRohanMarketStallStructure.Orcharder::new, "RohanMarketOrcharder", 9161006, 13411436);
        registerStructure(320, LOTRRohanVillagePastureStructure::new, "RohanVillagePasture", 7648578, 8546111);
        registerStructure(321, LOTRRohanVillageSignStructure::new, "RohanVillageSign", 5982252, 13411436);
        registerStructure(322, LOTRRohanGatehouseStructure::new, "RohanGatehouse", 5982252, 13411436);
        registerVillage(323, new LOTRVillageGenRohan("rohan", 1.0f), "RohanVillage", 5982252, 13411436, (IVillageProperties<LOTRVillageGenRohan.Instance>) instance -> instance.villageType = LOTRVillageGenRohan.VillageType.VILLAGE);
        registerVillage(324, new LOTRVillageGenRohan("rohan", 1.0f), "RohanFortVillage", 5982252, 13411436, (IVillageProperties<LOTRVillageGenRohan.Instance>) instance -> instance.villageType = LOTRVillageGenRohan.VillageType.FORT);
        registerStructure(350, LOTRUrukTentStructure::new, "UrukTent", 2301210, 131586);
        registerOldStructure(351, LOTRRuinedRohanWatchtowerStructure::new, "RuinedRohanWatchtower", 1117449, 3288357);
        registerStructure(352, LOTRUrukForgeTentStructure::new, "UrukForgeTent", 3682596, 2038547);
        registerStructure(353, LOTRUrukWargPitStructure::new, "UrukWargPit", 3682596, 2038547);
        registerStructure(354, LOTRUrukCampStructure::new, "UrukCamp", 2301210, 131586);
        registerStructure(380, LOTRDunlendingHouseStructure::new, "DunlendingHouse", 6705465, 3813154);
        registerStructure(381, LOTRDunlendingTavernStructure::new, "DunlendingTavern", 6705465, 3813154);
        registerOldStructure(382, LOTRDunlendingCampfireStructure::new, "DunlendingCampfire", 9539472, 6837299);
        registerStructure(383, LOTRDunlandHillFortStructure::new, "DunlandHillFort", 6705465, 3813154);
        registerStructure(400, LOTRBeaconTowerStructure::new, "BeaconTower", 14869218, 11513775);
        registerStructure(401, LOTRGondorWatchfortStructure::new, "GondorWatchfort", 14869218, 2367263);
        registerStructure(402, LOTRGondorSmithyStructure::new, "GondorSmithy", 14869218, 2367263);
        registerStructure(403, LOTRGondorTurretStructure::new, "GondorTurret", 14869218, 11513775);
        registerStructure(404, LOTRIthilienHideoutStructure::new, "IthilienHideout", 8882055, 7365464);
        registerStructure(405, LOTRGondorHouseStructure::new, "GondorHouse", 14869218, 9861961);
        registerStructure(406, LOTRGondorCottageStructure::new, "GondorCottage", 14869218, 9861961);
        registerStructure(407, LOTRGondorStoneHouseStructure::new, "GondorStoneHouse", 14869218, 2367263);
        registerStructure(408, LOTRGondorWatchtowerStructure::new, "GondorWatchtower", 14869218, 11513775);
        registerStructure(409, LOTRGondorStablesStructure::new, "GondorStables", 14869218, 9861961);
        registerStructure(410, LOTRGondorBarnStructure::new, "GondorBarn", 14869218, 9861961);
        registerStructure(411, LOTRGondorFortressStructure::new, "GondorFortress", 14869218, 2367263);
        registerStructure(412, LOTRGondorTavernStructure::new, "GondorTavern", 14869218, 9861961);
        registerStructure(413, LOTRGondorWellStructure::new, "GondorWell", 14869218, 11513775);
        registerStructure(414, LOTRGondorVillageFarmStructure.Crops::new, "GondorFarmCrops", 7047232, 15066597);
        registerStructure(415, LOTRGondorVillageFarmStructure.Animals::new, "GondorFarmAnimals", 7047232, 15066597);
        registerStructure(416, LOTRGondorVillageFarmStructure.Tree::new, "GondorFarmTree", 7047232, 15066597);
        registerStructure(417, LOTRGondorMarketStallStructure.Greengrocer::new, "GondorMarketGreengrocer", 8567851, 9861961);
        registerStructure(418, LOTRGondorMarketStallStructure.Lumber::new, "GondorMarketLumber", 5981994, 9861961);
        registerStructure(419, LOTRGondorMarketStallStructure.Mason::new, "GondorMarketMason", 10526621, 9861961);
        registerStructure(420, LOTRGondorMarketStallStructure.Brewer::new, "GondorMarketBrewer", 13874218, 9861961);
        registerStructure(421, LOTRGondorMarketStallStructure.Flowers::new, "GondorMarketFlowers", 16243515, 9861961);
        registerStructure(422, LOTRGondorMarketStallStructure.Butcher::new, "GondorMarketButcher", 14521508, 9861961);
        registerStructure(423, LOTRGondorMarketStallStructure.Fish::new, "GondorMarketFish", 6862591, 9861961);
        registerStructure(424, LOTRGondorMarketStallStructure.Farmer::new, "GondorMarketFarmer", 14401433, 9861961);
        registerStructure(425, LOTRGondorMarketStallStructure.Blacksmith::new, "GondorMarketBlacksmith", 2960684, 9861961);
        registerStructure(426, LOTRGondorMarketStallStructure.Baker::new, "GondorMarketBaker", 13543009, 9861961);
        registerStructure(427, LOTRGondorVillageSignStructure::new, "GondorVillageSign", 5982252, 13411436);
        registerStructure(428, LOTRGondorBathStructure::new, "GondorBath", 14869218, 2367263);
        registerStructure(429, LOTRGondorGatehouseStructure::new, "GondorGatehouse", 14869218, 2367263);
        registerStructure(430, LOTRGondorLampPostStructure::new, "GondorLampPost", 14869218, 11513775);
        registerStructure(431, LOTRGondorTownGardenStructure::new, "GondorTownGarden", 7047232, 15066597);
        registerStructure(432, LOTRGondorTownTreesStructure::new, "GondorTownTrees", 7047232, 15066597);
        registerStructure(433, LOTRGondorTownBenchStructure::new, "GondorTownBench", 14869218, 11513775);
        registerVillage(434, new LOTRVillageGenGondor("gondor", LOTRGondorStructure.GondorFiefdom.GONDOR, 1.0f), "GondorVillage", 14869218, 2367263, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE);
        registerVillage(435, new LOTRVillageGenGondor("gondor", LOTRGondorStructure.GondorFiefdom.GONDOR, 1.0f), "GondorTown", 14869218, 2367263, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.TOWN);
        registerVillage(436, new LOTRVillageGenGondor("gondor", LOTRGondorStructure.GondorFiefdom.GONDOR, 1.0f), "GondorFortVillage", 14869218, 2367263, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.FORT);
        registerStructure(450, LOTRRuinedBeaconTowerStructure::new, "RuinedBeaconTower", 14869218, 11513775);
        registerOldStructure(451, LOTRRuinedGondorTowerStructure::new, "RuinedGondorTower", 14869218, 11513775);
        registerStructure(452, LOTRGondorObeliskStructure::new, "GondorObelisk", 14869218, 11513775);
        registerOldStructure(453, LOTRGondorRuinStructure::new, "GondorRuin", 14869218, 11513775);
        registerStructure(500, LOTRDolAmrothStablesStructure::new, "DolAmrothStables", 15002613, 2709918);
        registerStructure(501, LOTRDolAmrothWatchtowerStructure::new, "DolAmrothWatchtower", 14869218, 11513775);
        registerStructure(502, LOTRDolAmrothWatchfortStructure::new, "DolAmrothWatchfort", 15002613, 2709918);
        registerVillage(503, new LOTRVillageGenGondor("dorEnErnil", LOTRGondorStructure.GondorFiefdom.DOL_AMROTH, 1.0f), "DolAmrothVillage", 15002613, 2709918, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE);
        registerVillage(504, new LOTRVillageGenGondor("dorEnErnil", LOTRGondorStructure.GondorFiefdom.DOL_AMROTH, 1.0f), "DolAmrothTown", 15002613, 2709918, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.TOWN);
        registerVillage(505, new LOTRVillageGenGondor("dorEnErnil", LOTRGondorStructure.GondorFiefdom.DOL_AMROTH, 1.0f), "DolAmrothFortVillage", 15002613, 2709918, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.FORT);
        registerStructure(510, LOTRLossarnachFortressStructure::new, "LossarnachFortress", 14869218, 15138816);
        registerStructure(511, LOTRLossarnachWatchtowerStructure::new, "LossarnachWatchtower", 14869218, 11513775);
        registerStructure(512, LOTRLossarnachWatchfortStructure::new, "LossarnachWatchfort", 14869218, 15138816);
        registerVillage(513, new LOTRVillageGenGondor("lossarnach", LOTRGondorStructure.GondorFiefdom.LOSSARNACH, 1.0f), "LossarnachVillage", 14869218, 15138816, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE);
        registerVillage(514, new LOTRVillageGenGondor("lossarnach", LOTRGondorStructure.GondorFiefdom.LOSSARNACH, 1.0f), "LossarnachTown", 14869218, 15138816, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.TOWN);
        registerVillage(515, new LOTRVillageGenGondor("lossarnach", LOTRGondorStructure.GondorFiefdom.LOSSARNACH, 1.0f), "LossarnachFortVillage", 14869218, 15138816, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.FORT);
        registerStructure(520, LOTRLebenninFortressStructure::new, "LebenninFortress", 14869218, 621750);
        registerStructure(521, LOTRLebenninWatchtowerStructure::new, "LebenninWatchtower", 14869218, 11513775);
        registerStructure(522, LOTRLebenninWatchfortStructure::new, "LebenninWatchfort", 14869218, 621750);
        registerVillage(523, new LOTRVillageGenGondor("lebennin", LOTRGondorStructure.GondorFiefdom.LEBENNIN, 1.0f), "LebenninVillage", 14869218, 621750, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE);
        registerVillage(524, new LOTRVillageGenGondor("lebennin", LOTRGondorStructure.GondorFiefdom.LEBENNIN, 1.0f), "LebenninTown", 14869218, 621750, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.TOWN);
        registerVillage(525, new LOTRVillageGenGondor("lebennin", LOTRGondorStructure.GondorFiefdom.LEBENNIN, 1.0f), "LebenninFortVillage", 14869218, 621750, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.FORT);
        registerStructure(530, LOTRPelargirFortressStructure::new, "PelargirFortress", 14869218, 2917253);
        registerStructure(531, LOTRPelargirWatchtowerStructure::new, "PelargirWatchtower", 14869218, 11513775);
        registerStructure(532, LOTRPelargirWatchfortStructure::new, "PelargirWatchfort", 14869218, 2917253);
        registerVillage(533, new LOTRVillageGenGondor("pelargir", LOTRGondorStructure.GondorFiefdom.PELARGIR, 1.0f), "PelargirVillage", 14869218, 2917253, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE);
        registerVillage(534, new LOTRVillageGenGondor("pelargir", LOTRGondorStructure.GondorFiefdom.PELARGIR, 1.0f), "PelargirTown", 14869218, 2917253, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.TOWN);
        registerVillage(535, new LOTRVillageGenGondor("pelargir", LOTRGondorStructure.GondorFiefdom.PELARGIR, 1.0f), "PelargirFortVillage", 14869218, 2917253, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.FORT);
        registerStructure(540, LOTRPinnathGelinFortressStructure::new, "PinnathGelinFortress", 14869218, 1401651);
        registerStructure(541, LOTRPinnathGelinWatchtowerStructure::new, "PinnathGelinWatchtower", 14869218, 11513775);
        registerStructure(542, LOTRPinnathGelinWatchfortStructure::new, "PinnathGelinWatchfort", 14869218, 1401651);
        registerVillage(543, new LOTRVillageGenGondor("pinnathGelin", LOTRGondorStructure.GondorFiefdom.PINNATH_GELIN, 1.0f), "PinnathGelinVillage", 14869218, 1401651, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE);
        registerVillage(544, new LOTRVillageGenGondor("pinnathGelin", LOTRGondorStructure.GondorFiefdom.PINNATH_GELIN, 1.0f), "PinnathGelinTown", 14869218, 1401651, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.TOWN);
        registerVillage(545, new LOTRVillageGenGondor("pinnathGelin", LOTRGondorStructure.GondorFiefdom.PINNATH_GELIN, 1.0f), "PinnathGelinFortVillage", 14869218, 1401651, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.FORT);
        registerStructure(550, LOTRBlackrootFortressStructure::new, "BlackrootFortress", 14869218, 2367263);
        registerStructure(551, LOTRBlackrootWatchtowerStructure::new, "BlackrootWatchtower", 14869218, 11513775);
        registerStructure(552, LOTRBlackrootWatchfortStructure::new, "BlackrootWatchfort", 14869218, 2367263);
        registerVillage(553, new LOTRVillageGenGondor("blackrootVale", LOTRGondorStructure.GondorFiefdom.BLACKROOT_VALE, 1.0f), "BlackrootVillage", 14869218, 2367263, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE);
        registerVillage(554, new LOTRVillageGenGondor("blackrootVale", LOTRGondorStructure.GondorFiefdom.BLACKROOT_VALE, 1.0f), "BlackrootTown", 14869218, 2367263, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.TOWN);
        registerVillage(555, new LOTRVillageGenGondor("blackrootVale", LOTRGondorStructure.GondorFiefdom.BLACKROOT_VALE, 1.0f), "BlackrootFortVillage", 14869218, 2367263, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.FORT);
        registerStructure(560, LOTRLamedonFortressStructure::new, "LamedonFortress", 14869218, 1784649);
        registerStructure(561, LOTRLamedonWatchtowerStructure::new, "LamedonWatchtower", 14869218, 11513775);
        registerStructure(562, LOTRLamedonWatchfortStructure::new, "LamedonWatchfort", 14869218, 1784649);
        registerVillage(563, new LOTRVillageGenGondor("lamedon", LOTRGondorStructure.GondorFiefdom.LAMEDON, 1.0f), "LamedonVillage", 14869218, 1784649, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE);
        registerVillage(564, new LOTRVillageGenGondor("lamedon", LOTRGondorStructure.GondorFiefdom.LAMEDON, 1.0f), "LamedonTown", 14869218, 1784649, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.TOWN);
        registerVillage(565, new LOTRVillageGenGondor("lamedon", LOTRGondorStructure.GondorFiefdom.LAMEDON, 1.0f), "LamedonFortVillage", 14869218, 1784649, (IVillageProperties<LOTRVillageGenGondor.Instance>) instance -> instance.villageType = LOTRVillageGenGondor.VillageType.FORT);
        registerOldStructure(600, LOTRMordorTowerStructure::new, "MordorTower", 2631720, 328965);
        registerStructure(601, LOTRMordorTentStructure::new, "MordorTent", 2301210, 131586);
        registerStructure(602, LOTRMordorForgeTentStructure::new, "MordorForgeTent", 2631720, 328965);
        registerStructure(603, LOTRMordorWargPitStructure::new, "MordorWargPit", 2631720, 328965);
        registerStructure(604, LOTRMordorCampStructure::new, "MordorCamp", 2301210, 131586);
        registerStructure(605, LOTRBlackUrukFortStructure::new, "BlackUrukFort", 2631720, 328965);
        registerOldStructure(650, LOTRNurnWheatFarmStructure::new, "NurnWheatFarm", 4469796, 328965);
        registerOldStructure(651, LOTROrcSlaverTowerStructure::new, "OrcSlaverTower", 1117449, 3288357);
        registerStructure(670, LOTRMordorSpiderPitStructure::new, "MordorSpiderPit", 1511181, 12917534);
        registerStructure(700, LOTRDorwinionGardenStructure::new, "DorwinionGarden", 16572875, 13418417);
        registerStructure(701, LOTRDorwinionTentStructure::new, "DorwinionTent", 6706573, 15058766);
        registerStructure(702, LOTRDorwinionCaptainTentStructure::new, "DorwinionCaptainTent", 6706573, 15058766);
        registerStructure(703, LOTRDorwinionHouseStructure::new, "DorwinionHouse", 7167128, 15390149);
        registerStructure(704, LOTRDorwinionBreweryStructure::new, "DorwinionBrewery", 7167128, 15390149);
        registerStructure(705, LOTRDorwinionElfHouseStructure::new, "DorwinionElfHouse", 7167128, 15390149);
        registerStructure(706, LOTRDorwinionBathStructure::new, "DorwinionBath", 7167128, 15390149);
        registerStructure(750, LOTREasterlingHouseStructure::new, "EasterlingHouse", 12693373, 7689786);
        registerStructure(751, LOTREasterlingStablesStructure::new, "EasterlingStables", 12693373, 7689786);
        registerStructure(752, LOTREasterlingTownHouseStructure::new, "EasterlingTownHouse", 6304287, 12693373);
        registerStructure(753, LOTREasterlingLargeTownHouseStructure::new, "EasterlingLargeTownHouse", 6304287, 12693373);
        registerStructure(754, LOTREasterlingFortressStructure::new, "EasterlingFortress", 6304287, 12693373);
        registerStructure(755, LOTREasterlingTowerStructure::new, "EasterlingTower", 6304287, 12693373);
        registerStructure(756, LOTREasterlingSmithyStructure::new, "EasterlingSmithy", 6304287, 12693373);
        registerStructure(757, LOTREasterlingMarketStallStructure.Blacksmith::new, "EasterlingMarketBlacksmith", 2960684, 12693373);
        registerStructure(758, LOTREasterlingMarketStallStructure.Lumber::new, "EasterlingMarketLumber", 5981994, 12693373);
        registerStructure(759, LOTREasterlingMarketStallStructure.Mason::new, "EasterlingMarketMason", 7039594, 12693373);
        registerStructure(760, LOTREasterlingMarketStallStructure.Butcher::new, "EasterlingMarketButcher", 12544103, 12693373);
        registerStructure(761, LOTREasterlingMarketStallStructure.Brewer::new, "EasterlingMarketBrewer", 11891243, 12693373);
        registerStructure(762, LOTREasterlingMarketStallStructure.Fish::new, "EasterlingMarketFish", 4882395, 12693373);
        registerStructure(763, LOTREasterlingMarketStallStructure.Baker::new, "EasterlingMarketBaker", 14725995, 12693373);
        registerStructure(764, LOTREasterlingMarketStallStructure.Hunter::new, "EasterlingMarketHunter", 4471854, 12693373);
        registerStructure(765, LOTREasterlingMarketStallStructure.Farmer::new, "EasterlingMarketFarmer", 8893759, 12693373);
        registerStructure(766, LOTREasterlingMarketStallStructure.Gold::new, "EasterlingMarketGold", 16237060, 12693373);
        registerStructure(767, LOTREasterlingTavernStructure::new, "EasterlingTavern", 12693373, 7689786);
        registerStructure(768, LOTREasterlingTavernTownStructure::new, "EasterlingTavernTown", 6304287, 12693373);
        registerStructure(769, LOTREasterlingStatueStructure::new, "EasterlingStatue", 12693373, 7689786);
        registerStructure(770, LOTREasterlingGardenStructure::new, "EasterlingGarden", 4030994, 12693373);
        registerStructure(771, LOTREasterlingVillageSignStructure::new, "EasterlingVillageSign", 12693373, 7689786);
        registerStructure(772, LOTREasterlingWellStructure::new, "EasterlingWell", 12693373, 7689786);
        registerStructure(773, LOTREasterlingVillageFarmStructure.Crops::new, "EasterlingFarmCrops", 4030994, 12693373);
        registerStructure(774, LOTREasterlingVillageFarmStructure.Animals::new, "EasterlingFarmAnimals", 4030994, 12693373);
        registerStructure(775, LOTREasterlingVillageFarmStructure.Tree::new, "EasterlingFarmTree", 4030994, 12693373);
        registerStructure(776, LOTREasterlingGatehouseStructure::new, "EasterlingGatehouse", 6304287, 12693373);
        registerStructure(777, LOTREasterlingLampStructure::new, "EasterlingLamp", 6304287, 12693373);
        registerVillage(778, new LOTRVillageGenRhun("rhunLand", 1.0f, true), "EasterlingVillage", 6304287, 12693373, (IVillageProperties<LOTRVillageGenRhun.Instance>) instance -> instance.villageType = LOTRVillageGenRhun.VillageType.VILLAGE);
        registerVillage(779, new LOTRVillageGenRhun("rhunLand", 1.0f, true), "EasterlingTown", 6304287, 12693373, (IVillageProperties<LOTRVillageGenRhun.Instance>) instance -> instance.villageType = LOTRVillageGenRhun.VillageType.TOWN);
        registerVillage(780, new LOTRVillageGenRhun("rhunLand", 1.0f, true), "EasterlingFortVillage", 6304287, 12693373, (IVillageProperties<LOTRVillageGenRhun.Instance>) instance -> instance.villageType = LOTRVillageGenRhun.VillageType.FORT);
        registerOldStructure(1000, LOTRHaradObeliskStructure::new, "HaradObelisk", 10854007, 15590575);
        registerStructure(1001, LOTRHaradPyramidStructure::new, "HaradPyramid", 10854007, 15590575);
        registerStructure(1002, LOTRMumakSkeletonStructure::new, "MumakSkeleton", 14737111, 16250349);
        registerStructure(1003, LOTRHaradRuinedFortStructure::new, "HaradRuinedFort", 10854007, 15590575);
        registerStructure(1050, LOTRHarnedorHouseStructure::new, "HarnedorHouse", 4994339, 12814421);
        registerStructure(1051, LOTRHarnedorSmithyStructure::new, "HarnedorSmithy", 4994339, 12814421);
        registerStructure(1052, LOTRHarnedorTavernStructure::new, "HarnedorTavern", 4994339, 12814421);
        registerStructure(1053, LOTRHarnedorMarketStructure::new, "HarnedorMarket", 4994339, 12814421);
        registerStructure(1054, LOTRHarnedorTowerStructure::new, "HarnedorTower", 4994339, 12814421);
        registerStructure(1055, LOTRHarnedorFortStructure::new, "HarnedorFort", 4994339, 12814421);
        registerStructure(1056, LOTRNearHaradTentStructure::new, "NearHaradTent", 13519170, 1775897);
        registerStructure(1057, LOTRHarnedorFarmStructure::new, "HarnedorFarm", 10073953, 12814421);
        registerStructure(1058, LOTRHarnedorPastureStructure::new, "HarnedorPasture", 10073953, 12814421);
        registerVillage(1059, new LOTRVillageGenHarnedor("harnedor", 1.0f), "HarnedorVillage", 4994339, 12814421, (IVillageProperties<LOTRVillageGenHarnedor.Instance>) instance -> instance.villageType = LOTRVillageGenHarnedor.VillageType.VILLAGE);
        registerStructure(1060, LOTRHarnedorStablesStructure::new, "HarnedorStables", 4994339, 12814421);
        registerStructure(1061, LOTRHarnedorVillageSignStructure::new, "HarnedorVillageSign", 4994339, 12814421);
        registerVillage(1062, new LOTRVillageGenHarnedor("harnedor", 1.0f), "HarnedorFortVillage", 4994339, 12814421, (IVillageProperties<LOTRVillageGenHarnedor.Instance>) instance -> instance.villageType = LOTRVillageGenHarnedor.VillageType.FORTRESS);
        registerStructure(1080, LOTRHarnedorHouseRuinedStructure::new, "HarnedorHouseRuined", 5519919, 10059372);
        registerStructure(1081, LOTRHarnedorTavernRuinedStructure::new, "HarnedorTavernRuined", 5519919, 10059372);
        registerVillage(1082, new LOTRVillageGenHarnedor("harondor", 1.0f).setRuined(), "HarnedorVillageRuined", 5519919, 10059372, (IVillageProperties<LOTRVillageGenHarnedor.Instance>) instance -> instance.villageType = LOTRVillageGenHarnedor.VillageType.VILLAGE);
        registerStructure(1100, LOTRSouthronHouseStructure::new, "SouthronHouse", 15063989, 10052655);
        registerStructure(1101, LOTRSouthronTavernStructure::new, "SouthronTavern", 15063989, 10052655);
        registerStructure(1102, LOTRSouthronSmithyStructure::new, "SouthronSmithy", 15063989, 10052655);
        registerStructure(1103, LOTRSouthronTowerStructure::new, "SouthronTower", 15063989, 10052655);
        registerStructure(1104, LOTRSouthronMansionStructure::new, "SouthronMansion", 15063989, 10052655);
        registerStructure(1105, LOTRSouthronStablesStructure::new, "SouthronStables", 15063989, 10052655);
        registerStructure(1106, LOTRSouthronFarmStructure::new, "SouthronFarm", 9547581, 10052655);
        registerStructure(1107, LOTRSouthronFortressStructure::new, "SouthronFortress", 15063989, 10052655);
        registerStructure(1108, LOTRSouthronWellStructure::new, "SouthronWell", 15063989, 10052655);
        registerStructure(1109, LOTRSouthronBazaarStructure::new, "SouthronBazaar", 15063989, 10052655);
        registerStructure(1110, LOTRSouthronPastureStructure::new, "SouthronPasture", 9547581, 10052655);
        registerStructure(1111, LOTRSouthronVillageSignStructure::new, "SouthronVillageSign", 15063989, 10052655);
        registerVillage(1112, new LOTRVillageGenSouthron("nearHaradFertile", 1.0f), "SouthronVillage", 15063989, 10052655, (IVillageProperties<LOTRVillageGenSouthron.Instance>) instance -> instance.villageType = LOTRVillageGenSouthron.VillageType.VILLAGE);
        registerStructure(1113, LOTRSouthronStatueStructure::new, "SouthronStatue", 15063989, 10052655);
        registerStructure(1114, LOTRSouthronBarracksStructure::new, "SouthronBarracks", 15063989, 10052655);
        registerStructure(1115, LOTRSouthronTrainingStructure::new, "SouthronTraining", 15063989, 10052655);
        registerStructure(1116, LOTRSouthronFortGateStructure::new, "SouthronFortGate", 15063989, 10052655);
        registerVillage(1117, new LOTRVillageGenSouthron("nearHaradFertile", 1.0f), "SouthronFortVillage", 15063989, 10052655, (IVillageProperties<LOTRVillageGenSouthron.Instance>) instance -> instance.villageType = LOTRVillageGenSouthron.VillageType.FORT);
        registerStructure(1118, LOTRSouthronLampStructure::new, "SouthronLamp", 15063989, 10052655);
        registerStructure(1119, LOTRSouthronTownTreeStructure::new, "SouthronTownTree", 9547581, 10052655);
        registerStructure(1120, LOTRSouthronTownFlowersStructure::new, "SouthronTownFlowers", 9547581, 10052655);
        registerVillage(1121, new LOTRVillageGenSouthron("nearHaradFertile", 1.0f), "SouthronTown", 15063989, 10052655, (IVillageProperties<LOTRVillageGenSouthron.Instance>) instance -> instance.villageType = LOTRVillageGenSouthron.VillageType.TOWN);
        registerStructure(1122, LOTRSouthronTownGateStructure::new, "SouthronTownGate", 15063989, 10052655);
        registerStructure(1123, LOTRSouthronTownCornerStructure::new, "SouthronTownCorner", 15063989, 10052655);
        registerStructure(1140, LOTRMoredainMercTentStructure::new, "MoredainMercTent", 12845056, 2949120);
        registerStructure(1141, LOTRMoredainMercCampStructure::new, "MoredainMercCamp", 12845056, 2949120);
        registerStructure(1150, LOTRUmbarHouseStructure::new, "UmbarHouse", 14407104, 3354926);
        registerStructure(1151, LOTRUmbarTavernStructure::new, "UmbarTavern", 14407104, 3354926);
        registerStructure(1152, LOTRUmbarSmithyStructure::new, "UmbarSmithy", 14407104, 3354926);
        registerStructure(1153, LOTRUmbarTowerStructure::new, "UmbarTower", 14407104, 3354926);
        registerStructure(1154, LOTRUmbarMansionStructure::new, "UmbarMansion", 14407104, 3354926);
        registerStructure(1155, LOTRUmbarStablesStructure::new, "UmbarStables", 14407104, 3354926);
        registerStructure(1156, LOTRUmbarFarmStructure::new, "UmbarFarm", 9547581, 3354926);
        registerStructure(1157, LOTRUmbarFortressStructure::new, "UmbarFortress", 14407104, 3354926);
        registerStructure(1158, LOTRUmbarWellStructure::new, "UmbarWell", 14407104, 3354926);
        registerStructure(1159, LOTRUmbarBazaarStructure::new, "UmbarBazaar", 14407104, 3354926);
        registerStructure(1160, LOTRUmbarPastureStructure::new, "UmbarPasture", 9547581, 3354926);
        registerStructure(1161, LOTRUmbarVillageSignStructure::new, "UmbarVillageSign", 14407104, 3354926);
        registerVillage(1162, new LOTRVillageGenUmbar("umbar", 1.0f), "UmbarVillage", 14407104, 3354926, (IVillageProperties<LOTRVillageGenUmbar.InstanceUmbar>) instance -> instance.villageType = LOTRVillageGenSouthron.VillageType.VILLAGE);
        registerStructure(1163, LOTRUmbarStatueStructure::new, "UmbarStatue", 14407104, 3354926);
        registerStructure(1164, LOTRUmbarBarracksStructure::new, "UmbarBarracks", 14407104, 3354926);
        registerStructure(1165, LOTRUmbarTrainingStructure::new, "UmbarTraining", 14407104, 3354926);
        registerStructure(1166, LOTRUmbarFortGateStructure::new, "UmbarFortGate", 14407104, 3354926);
        registerVillage(1167, new LOTRVillageGenUmbar("umbar", 1.0f), "UmbarFortVillage", 14407104, 3354926, (IVillageProperties<LOTRVillageGenSouthron.Instance>) instance -> instance.villageType = LOTRVillageGenSouthron.VillageType.FORT);
        registerStructure(1168, LOTRUmbarLampStructure::new, "UmbarLamp", 14407104, 3354926);
        registerStructure(1169, LOTRUmbarTownTreeStructure::new, "UmbarTownTree", 9547581, 3354926);
        registerStructure(1170, LOTRUmbarTownFlowersStructure::new, "UmbarTownFlowers", 9547581, 3354926);
        registerVillage(1171, new LOTRVillageGenUmbar("umbar", 1.0f), "UmbarTown", 14407104, 3354926, (IVillageProperties<LOTRVillageGenSouthron.Instance>) instance -> instance.villageType = LOTRVillageGenSouthron.VillageType.TOWN);
        registerStructure(1172, LOTRUmbarTownGateStructure::new, "UmbarTownGate", 14407104, 3354926);
        registerStructure(1173, LOTRUmbarTownCornerStructure::new, "UmbarTownCorner", 14407104, 3354926);
        registerStructure(1180, LOTRCorsairCoveStructure::new, "CorsairCove", 8355711, 1644825);
        registerStructure(1181, LOTRCorsairTentStructure::new, "CorsairTent", 5658198, 657930);
        registerStructure(1182, LOTRCorsairCampStructure::new, "CorsairCamp", 5658198, 657930);
        registerStructure(1200, LOTRNomadTentStructure::new, "NomadTent", 16775927, 8345150);
        registerStructure(1201, LOTRNomadTentLargeStructure::new, "NomadTentLarge", 16775927, 8345150);
        registerStructure(1202, LOTRNomadChieftainTentStructure::new, "NomadChieftainTent", 16775927, 8345150);
        registerStructure(1203, LOTRNomadWellStructure::new, "NomadWell", 5478114, 15391151);
        registerVillage(1204, new LOTRVillageGenHaradNomad("nearHaradSemiDesert", 1.0f), "NomadVillageSmall", 16775927, 8345150, (IVillageProperties<LOTRVillageGenHaradNomad.Instance>) instance -> instance.villageType = LOTRVillageGenHaradNomad.VillageType.SMALL);
        registerVillage(1205, new LOTRVillageGenHaradNomad("nearHaradSemiDesert", 1.0f), "NomadVillageBig", 16775927, 8345150, (IVillageProperties<LOTRVillageGenHaradNomad.Instance>) instance -> instance.villageType = LOTRVillageGenHaradNomad.VillageType.BIG);
        registerStructure(1206, LOTRNomadBazaarTentStructure::new, "NomadBazaarTent", 16775927, 8345150);
        registerStructure(1250, LOTRGulfWarCampStructure::new, "GulfWarCamp", 12849937, 4275226);
        registerStructure(1251, LOTRGulfHouseStructure::new, "GulfHouse", 9335899, 5654831);
        registerStructure(1252, LOTRGulfAltarStructure::new, "GulfAltar", 12849937, 4275226);
        registerStructure(1253, LOTRGulfSmithyStructure::new, "GulfSmithy", 9335899, 5654831);
        registerStructure(1254, LOTRGulfBazaarStructure::new, "GulfBazaar", 9335899, 5654831);
        registerStructure(1255, LOTRGulfTotemStructure::new, "GulfTotem", 12849937, 4275226);
        registerStructure(1256, LOTRGulfPyramidStructure::new, "GulfPyramid", 15721151, 12873038);
        registerStructure(1257, LOTRGulfFarmStructure::new, "GulfFarm", 9547581, 12849937);
        registerStructure(1258, LOTRGulfTowerStructure::new, "GulfTower", 12849937, 4275226);
        registerStructure(1259, LOTRGulfTavernStructure::new, "GulfTavern", 9335899, 5654831);
        registerStructure(1260, LOTRGulfVillageSignStructure::new, "GulfVillageSign", 14737111, 16250349);
        registerStructure(1261, LOTRGulfVillageLightStructure::new, "GulfVillageLight", 14737111, 16250349);
        registerVillage(1262, new LOTRVillageGenGulfHarad("gulfHarad", 1.0f), "GulfVillage", 9335899, 5654831, (IVillageProperties<LOTRVillageGenGulfHarad.Instance>) instance -> instance.villageType = LOTRVillageGenGulfHarad.VillageType.VILLAGE);
        registerStructure(1263, LOTRGulfPastureStructure::new, "GulfPasture", 9547581, 12849937);
        registerVillage(1264, new LOTRVillageGenGulfHarad("gulfHarad", 1.0f), "GulfTown", 15721151, 12873038, (IVillageProperties<LOTRVillageGenGulfHarad.Instance>) instance -> instance.villageType = LOTRVillageGenGulfHarad.VillageType.TOWN);
        registerVillage(1265, new LOTRVillageGenGulfHarad("gulfHarad", 1.0f), "GulfFortVillage", 12849937, 4275226, (IVillageProperties<LOTRVillageGenGulfHarad.Instance>) instance -> instance.villageType = LOTRVillageGenGulfHarad.VillageType.FORT);
        registerStructure(1500, LOTRMoredainHutVillageStructure::new, "MoredainHutVillage", 8873812, 12891279);
        registerStructure(1501, LOTRMoredainHutChieftainStructure::new, "MoredainHutChieftain", 8873812, 12891279);
        registerStructure(1502, LOTRMoredainHutTraderStructure::new, "MoredainHutTrader", 8873812, 12891279);
        registerStructure(1503, LOTRMoredainHutHunterStructure::new, "MoredainHutHunter", 8873812, 12891279);
        registerStructure(1550, LOTRTauredainPyramidStructure::new, "TauredainPyramid", 6513746, 4803646);
        registerStructure(1551, LOTRTauredainHouseSimpleStructure::new, "TauredainHouseSimple", 4796447, 8021303);
        registerStructure(1552, LOTRTauredainHouseStiltsStructure::new, "TauredainHouseStilts", 4796447, 8021303);
        registerStructure(1553, LOTRTauredainWatchtowerStructure::new, "TauredainWatchtower", 4796447, 8021303);
        registerStructure(1554, LOTRTauredainHouseLargeStructure::new, "TauredainHouseLarge", 4796447, 14593598);
        registerStructure(1555, LOTRTauredainChieftainPyramidStructure::new, "TauredainChieftainPyramid", 6513746, 4803646);
        registerStructure(1556, LOTRTauredainVillageTreeStructure::new, "TauredainVillageTree", 9285414, 4796447);
        registerStructure(1557, LOTRTauredainVillageFarmStructure::new, "TauredainVillageFarm", 9285414, 4796447);
        registerVillage(1558, new LOTRVillageGenTauredain("tauredainClearing", 1.0f), "TauredainVillage", 6840658, 5979708, (IVillageProperties<LOTRVillageGenTauredain.Instance>) instance -> {
        });
        registerStructure(1559, LOTRTauredainSmithyStructure::new, "TauredainSmithy", 4796447, 8021303);
        registerStructure(1700, LOTRHalfTrollHouseStructure::new, "HalfTrollHouse", 10058344, 5325111);
        registerStructure(1701, LOTRHalfTrollWarlordHouseStructure::new, "HalfTrollWarlordHouse", 10058344, 5325111);
        registerStructure(1994, LOTRTicketBoothStructure::new, "TicketBooth", 15313961, 1118481, true);
    }

    public static int getRotationFromPlayer(Player player) {
        return Mth.floor(player.getYRot() * 4.0f / 360.0f + 0.5) & 3;
    }

    public static @Nullable StructureInfo get(int id) {
        return STRUCTURES.get(id);
    }

    public static Iterable<StructureInfo> all() {
        return STRUCTURES.values();
    }

    public static void registerStructure(int id, java.util.function.Function<Boolean, ? extends LOTRStructureBase2> factory, String name,
                                         int colorBG, int colorFG) {
        registerStructure(id, factory, name, colorBG, colorFG, false);
    }

    /**
     * A structure built fresh each time, with notifications on, free of the
     * restrictions natural generation checks, facing the way the player does.
     */
    public static void registerStructure(int id, java.util.function.Function<Boolean, ? extends LOTRStructureBase2> factory, String name,
                                         int colorBG, int colorFG, boolean hide) {
        registerStructure(id, new IStructureProvider() {
            @Override
            public boolean generateStructure(ServerLevel level, Player player, int i, int j, int k) {
                LOTRStructureBase2 str = factory.apply(true);
                str.restrictions = false;
                str.usingPlayer = player;
                return str.generateAndFinish(level, level.getRandom(), i, j, k, str.usingPlayerRotation());
            }

            @Override
            public boolean isVillage() {
                return false;
            }
        }, name, colorBG, colorFG, hide);
    }

    /** registerStructure for a structure on the older base (LOTRWorldGenStructureBase): generate, as it lies. */
    public static void registerOldStructure(int id, java.util.function.Function<Boolean, ? extends LOTRStructureBase> factory,
                                            String name, int colorBG, int colorFG) {
        registerStructure(id, new IStructureProvider() {
            @Override
            public boolean generateStructure(ServerLevel level, Player player, int i, int j, int k) {
                LOTRStructureBase str = factory.apply(true);
                str.restrictions = false;
                str.usingPlayer = player;
                return str.generateAndFinish(level, level.getRandom(), i, j, k);
            }

            @Override
            public boolean isVillage() {
                return false;
            }
        }, name, colorBG, colorFG, false);
    }

    /**
     * registerVillage: a whole village from a spawner, laid out around the
     * spot used and turned to face the player, with the properties this entry
     * gives it (which kind of village, for a people that has several).
     */
    public static <I extends LOTRVillageGen.AbstractInstance<?>> void registerVillage(int id, LOTRVillageGen village,
            String name, int colorBG, int colorFG, IVillageProperties<I> properties) {
        registerStructure(id, new IStructureProvider() {
            @SuppressWarnings("unchecked")
            @Override
            public boolean generateStructure(ServerLevel level, Player player, int i, int j, int k) {
                LOTRVillageGen.AbstractInstance<?> instance = village.createAndSetupVillageInstance(level.getSeed(), i, k,
                        level.getRandom(), LocationInfo.SPAWNED_BY_PLAYER);
                instance.setRotation((getRotationFromPlayer(player) + 2) % 4);
                properties.apply((I) instance);
                village.generateCompleteVillageInstance(instance, level, i, k);
                return true;
            }

            @Override
            public boolean isVillage() {
                return true;
            }
        }, name, colorBG, colorFG, false);
    }

    /** Sets a newly made village's properties, before it is laid out. */
    public interface IVillageProperties<V> {
        void apply(V instance);
    }

    public static void registerStructure(int id, IStructureProvider provider, String name, int colorBG, int colorFG,
                                         boolean hide) {
        if (STRUCTURES.containsKey(id)) {
            throw new IllegalArgumentException("Structure ID " + id + " is already registered to " + name + "!");
        }
        STRUCTURES.put(id, new StructureInfo(id, name, provider, colorBG, colorFG, provider.isVillage(), hide));
    }
}
