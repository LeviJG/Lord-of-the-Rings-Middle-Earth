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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar.LOTRAngmarHillmanHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar.LOTRAngmarHillmanVillageStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar.LOTRRhudaurCastleStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRGundabadCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRBurntHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRangerCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRangerWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRottenHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedDunedainTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenLoneLands.
 */
public class LOTRLoneLandsBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 5);

    public LOTRLoneLandsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 6, 2, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 4, 1, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_NORTH, 10).setSpawnChance(500);
        npcSpawnList.newFactionList(8, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_NORTH, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 1).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_HILLMEN, 10).setSpawnChance(10000);
        npcSpawnList.newFactionList(50).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 10);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 2);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_HILLMEN, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RIVENDELL_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND, 3.0f);
        addBiomeVariant(LOTRBiomeVariant.HILLS_SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.WASTELAND);
        addBiomeVariant(LOTRBiomeVariant.MOUNTAIN);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_ASPEN, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_MAPLE, 0.2f);
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 3;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 6;
        decorator.generateAthelas = true;
        decorator.addTree(LOTRTreeType.OAK, 1000);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 300);
        decorator.addTree(LOTRTreeType.SPRUCE, 300);
        decorator.addTree(LOTRTreeType.BEECH, 100);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 50);
        decorator.addTree(LOTRTreeType.BIRCH, 10);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 5);
        decorator.addTree(LOTRTreeType.ASPEN, 50);
        decorator.addTree(LOTRTreeType.ASPEN_LARGE, 10);
        decorator.addTree(LOTRTreeType.APPLE, 1);
        decorator.addTree(LOTRTreeType.PEAR, 1);
        registerPlainsFlowers();
        biomeColors.setGrass(12892772);
        decorator.generateOrcDungeon = true;
        decorator.addRandomStructure(new LOTRGundabadCampStructure(false), 1500);
        decorator.addRandomStructure(new LOTRRuinedDunedainTowerStructure(false), 800);
        decorator.addRandomStructure(new LOTRRuinedHouseStructure(false), 1500);
        decorator.addRandomStructure(new LOTRBurntHouseStructure(false), 1500);
        decorator.addRandomStructure(new LOTRRottenHouseStructure(false), 2000);
        decorator.addRandomStructure(new LOTRRangerCampStructure(false), 2000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 4), 400);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.ARNOR(1, 4), 400);
        decorator.addRandomStructure(new LOTRRangerWatchtowerStructure(false), 2000);
        decorator.addRandomStructure(new LOTRAngmarHillmanVillageStructure(false), 4000);
        decorator.addRandomStructure(new LOTRAngmarHillmanHouseStructure(false), 1000);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 200);
        decorator.addRandomStructure(new LOTRRhudaurCastleStructure(false), 1000);
        registerTravellingTrader(LOTREntities.GALADHRIM_TRADER);
        registerTravellingTrader(LOTREntities.BLUE_DWARF_MERCHANT);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.RIVENDELL_TRADER);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.HIGH_ELF_RIVENDELL, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.RANGER_NORTH, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR_HILLMEN, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR_WARG, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(32) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_LONE_LANDS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.ERIADOR.getSubregion("loneLands");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.LONE_LANDS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.1f;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.ARNOR.setRepair(0.4f);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }
}
