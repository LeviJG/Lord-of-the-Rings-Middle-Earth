package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRDaleFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRDaleVillageStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf.LOTRDaleWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRBurntHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRottenHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenDale.
 */
public class LOTRDaleBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRDaleBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 5, 2, 6));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DALE_MEN, 10).setSpawnChance(100);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DALE_SOLDIERS, 10).setSpawnChance(100);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DALE_MEN, 5);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DALE_SOLDIERS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2).setConquestOnly();
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 1).setConquestThreshold(50.0f);
        arrspawnListContainer3[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 1).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(10).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_GULDUR_ORCS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRKWOOD_SPIDERS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer4[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRK_TROLLS, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 2);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer5[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 10);
        arrspawnListContainer6[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 1);
        arrspawnListContainer6[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer6[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLINGS, 5).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK_SPRUCE);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.4f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.4f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_MAPLE, 0.4f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_APPLE_PEAR, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_PLUM, 0.5f);
        decorator.setTreeCluster(8, 20);
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 2;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 5;
        decorator.addTree(LOTRTreeType.OAK, 500);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 100);
        decorator.addTree(LOTRTreeType.OAK_DEAD, 50);
        decorator.addTree(LOTRTreeType.SPRUCE, 200);
        decorator.addTree(LOTRTreeType.SPRUCE_DEAD, 50);
        decorator.addTree(LOTRTreeType.CHESTNUT, 100);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 20);
        decorator.addTree(LOTRTreeType.MAPLE, 50);
        decorator.addTree(LOTRTreeType.PINE, 200);
        decorator.addTree(LOTRTreeType.FIR, 200);
        decorator.addTree(LOTRTreeType.APPLE, 5);
        decorator.addTree(LOTRTreeType.PEAR, 5);
        decorator.addTree(LOTRTreeType.PLUM, 5);
        registerPlainsFlowers();
        decorator.addRandomStructure(new LOTRRuinedHouseStructure(false), 4000);
        decorator.addRandomStructure(new LOTRBurntHouseStructure(false), 4000);
        decorator.addRandomStructure(new LOTRRottenHouseStructure(false), 4000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 4), 1000);
        decorator.addRandomStructure(new LOTRDaleWatchtowerStructure(false), 500);
        decorator.addRandomStructure(new LOTRDaleFortressStructure(false), 800);
        decorator.addRandomStructure(new LOTRDaleVillageStructure(false), 400);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 500);
        registerTravellingTrader(LOTREntities.BLUE_DWARF_MERCHANT);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_ELF);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_MAN);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.DOL_GULDUR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.RHUN, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(60) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_DALE;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.DALE.getSubregion("dale");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.DALE;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.DALE;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.05f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
