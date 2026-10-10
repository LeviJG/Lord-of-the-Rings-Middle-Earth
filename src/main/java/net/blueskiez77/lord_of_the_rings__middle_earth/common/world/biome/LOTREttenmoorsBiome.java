package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
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
 * LOTRBiomeGenEttenmoors.
 */
public class LOTREttenmoorsBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGenLarge = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 2, 5);

    public LOTRWorldGenerator boulderGenSmall = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 2);

    public LOTREttenmoorsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 10, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.ELK, 6, 4, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 6, 1, 4));
        spawnableLOTRAmbientList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BIRD, 10, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 10, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 30);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 7);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 10);
        npcSpawnList.newFactionList(35).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[5];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TROLLS, 40);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HILL_TROLLS, 20);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_HILLMEN, 20).setSpawnChance(500);
        arrspawnListContainer2[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 15);
        arrspawnListContainer2[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 5);
        npcSpawnList.newFactionList(70).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_NORTH, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RIVENDELL_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        npcSpawnList.conquestGainRate = 0.75f;
        biomeTerrain.setXZScale(100.0);
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 1.0f);
        decorator.biomeGemFactor = 0.75f;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 4;
        decorator.doubleGrassPerChunk = 2;
        decorator.generateAthelas = true;
        decorator.addTree(LOTRTreeType.FIR, 400);
        decorator.addTree(LOTRTreeType.PINE, 800);
        decorator.addTree(LOTRTreeType.SPRUCE, 500);
        decorator.addTree(LOTRTreeType.SPRUCE_THIN, 500);
        decorator.addTree(LOTRTreeType.SPRUCE_DEAD, 200);
        decorator.addTree(LOTRTreeType.SPRUCE_MEGA, 100);
        registerTaigaFlowers();
        decorator.generateOrcDungeon = true;
        decorator.generateTrollHoard = true;
        decorator.addRandomStructure(new LOTRRuinedDunedainTowerStructure(false), 500);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 4), 100);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.ARNOR(1, 4), 100);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.ANGMAR(1, 4), 100);
        decorator.addRandomStructure(new LOTRAngmarHillmanVillageStructure(false), 1000);
        decorator.addRandomStructure(new LOTRAngmarHillmanHouseStructure(false), 500);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 400);
        decorator.addRandomStructure(new LOTRRhudaurCastleStructure(false), 3000);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.RANGER_NORTH, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR_HILLMEN, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR_WARG, LOTREventSpawner.EventChance.COMMON);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        int i1;
        int l;
        super.decorate(world, random, i, k);
        for (l = 0; l < 3; ++l) {
            i1 = i + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1 = k + random.nextInt(16) + 8);
            if (j1 <= 84) {
                continue;
            }
            decorator.genTree(world, random, i1, j1, k1);
        }
        if (random.nextInt(4) == 0) {
            for (l = 0; l < 3; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderGenLarge.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        for (l = 0; l < 2; ++l) {
            i1 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            boulderGenSmall.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_ETTENMOORS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.ANGMAR.getSubregion("ettenmoors");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.ETTENMOORS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.1f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }
}
