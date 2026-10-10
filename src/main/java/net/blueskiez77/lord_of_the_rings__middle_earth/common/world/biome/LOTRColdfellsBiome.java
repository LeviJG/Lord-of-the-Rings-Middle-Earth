package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedDunedainTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenColdfells.
 */
public class LOTRColdfellsBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 2, 4);

    public LOTRColdfellsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 10, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.ELK, 4, 4, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 6, 1, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 2);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 5);
        npcSpawnList.newFactionList(50).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[5];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TROLLS, 15);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_HILLMEN, 5).setSpawnChance(2000);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 5).setConquestOnly();
        arrspawnListContainer2[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 5).setConquestOnly();
        arrspawnListContainer2[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HILL_TROLLS, 5).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(50).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_NORTH, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RIVENDELL_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        decorator.biomeGemFactor = 0.75f;
        decorator.treesPerChunk = 2;
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 2;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 6;
        decorator.doubleGrassPerChunk = 2;
        decorator.generateAthelas = true;
        decorator.addTree(LOTRTreeType.FIR, 500);
        decorator.addTree(LOTRTreeType.PINE, 500);
        decorator.addTree(LOTRTreeType.SPRUCE, 400);
        decorator.addTree(LOTRTreeType.SPRUCE_THIN, 200);
        decorator.addTree(LOTRTreeType.OAK, 200);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 30);
        decorator.addTree(LOTRTreeType.LARCH, 300);
        decorator.addTree(LOTRTreeType.MAPLE, 100);
        decorator.addTree(LOTRTreeType.MAPLE_LARGE, 10);
        registerForestFlowers();
        decorator.generateOrcDungeon = true;
        decorator.generateTrollHoard = true;
        decorator.addRandomStructure(new LOTRRuinedDunedainTowerStructure(false), 500);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 4), 100);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.ARNOR(1, 4), 100);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.ANGMAR(1, 4), 100);
        decorator.addRandomStructure(new LOTRAngmarHillmanVillageStructure(false), 2000);
        decorator.addRandomStructure(new LOTRAngmarHillmanHouseStructure(false), 800);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 400);
        decorator.addRandomStructure(new LOTRRhudaurCastleStructure(false), 3000);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.RANGER_NORTH, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR_HILLMEN, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR_WARG, LOTREventSpawner.EventChance.UNCOMMON);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(3) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_COLDFELLS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.ANGMAR.getSubregion("coldfells");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.COLDFELLS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.1f;
    }
}
