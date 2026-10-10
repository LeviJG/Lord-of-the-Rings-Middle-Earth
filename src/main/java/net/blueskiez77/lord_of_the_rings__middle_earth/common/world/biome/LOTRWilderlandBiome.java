package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenDoubleFlower;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRDolGuldurCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRMordorCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRBurntHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRottenHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenWilderland.
 */
public class LOTRWilderlandBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRWilderlandBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 6, 2, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.AUROCHS, 20, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.KINE_OF_ARAW, 3, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 6, 1, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2).setConquestOnly();
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(33).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_GULDUR_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRKWOOD_SPIDERS, 2).setConquestOnly();
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRK_TROLLS, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(33).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[5];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 2).setConquestOnly();
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 1).setConquestThreshold(50.0f);
        arrspawnListContainer3[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 1).setConquestThreshold(100.0f);
        arrspawnListContainer3[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(33).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DALE_SOLDIERS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DALE_MEN, 2).setConquestThreshold(100.0f);
        arrspawnListContainer4[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DALE_MEN, 2).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_MEN, 2).setConquestThreshold(100.0f);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_MEN, 2).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DORWINION_GUARDS, 10);
        arrspawnListContainer6[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DORWINION_MEN, 2).setConquestThreshold(100.0f);
        arrspawnListContainer6[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DORWINION_MEN, 2).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer7 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer7[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 10);
        arrspawnListContainer7[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer7[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLINGS, 2).setConquestThreshold(100.0f);
        arrspawnListContainer7[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLINGS, 2).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer7);
        variantChance = 0.7f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK_SPRUCE);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND, 5.0f);
        addBiomeVariant(LOTRBiomeVariant.WASTELAND, 3.0f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_MAPLE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_ASPEN, 0.2f);
        decorator.setTreeCluster(8, 20);
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 14;
        decorator.doubleGrassPerChunk = 8;
        decorator.addTree(LOTRTreeType.OAK, 1000);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 100);
        decorator.addTree(LOTRTreeType.OAK_DEAD, 500);
        decorator.addTree(LOTRTreeType.SPRUCE, 200);
        decorator.addTree(LOTRTreeType.SPRUCE_DEAD, 100);
        decorator.addTree(LOTRTreeType.CHESTNUT, 100);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 10);
        registerRhunPlainsFlowers();
        decorator.generateOrcDungeon = true;
        decorator.addRandomStructure(new LOTRDolGuldurCampStructure(false), 2000);
        decorator.addRandomStructure(new LOTRMordorCampStructure(false), 3000);
        decorator.addRandomStructure(new LOTRRuinedHouseStructure(false), 2000);
        decorator.addRandomStructure(new LOTRBurntHouseStructure(false), 3000);
        decorator.addRandomStructure(new LOTRRottenHouseStructure(false), 3000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 4), 500);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 400);
        registerTravellingTrader(LOTREntities.NEAR_HARAD_MERCHANT);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_ELF);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_MAN);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.DOL_GULDUR, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_WARG, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.DALE, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.DORWINION, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.RHUN, LOTREventSpawner.EventChance.UNCOMMON);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(24) == 0) {
            for (int l = 0; l < 4; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_WILDERLAND;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.RHOVANION.getSubregion("wilderland");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.WILDERLAND;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.1f;
    }

    @Override
    public LOTRWorldGenerator getRandomWorldGenForDoubleFlower(RandomSource random) {
        if (random.nextInt(6) == 0) {
            LOTRWorldGenDoubleFlower doubleFlowerGen = new LOTRWorldGenDoubleFlower();
            doubleFlowerGen.setFlowerType(0);
            return doubleFlowerGen;
        }
        return super.getRandomWorldGenForDoubleFlower(random);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.05f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 2;
    }
}
