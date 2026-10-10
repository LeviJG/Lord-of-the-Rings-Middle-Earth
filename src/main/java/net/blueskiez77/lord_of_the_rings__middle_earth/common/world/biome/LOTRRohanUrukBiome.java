package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBlastedLand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenSkullPile;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRUrukCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRUrukWargPitStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRuinedRohanWatchtowerStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenRohanUruk.
 */
public class LOTRRohanUrukBiome extends LOTRRohanBiome {

    public LOTRWorldGenerator deadMoundGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("wasteBlock"), 0, 1, 3);

    public LOTRRohanUrukBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_WARGS, 4);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDINGS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDING_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ENTS, 5);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HUORNS, 20);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        clearBiomeVariants();
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        decorator.resetTreeCluster();
        decorator.willowPerChunk = 0;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 6;
        decorator.doubleGrassPerChunk = 1;
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRUrukCampStructure(false), 120);
        decorator.addRandomStructure(new LOTRUrukWargPitStructure(false), 300);
        decorator.addRandomStructure(new LOTRRuinedRohanWatchtowerStructure(false), 300);
        decorator.addRandomStructure(new LOTRWorldGenBlastedLand(true), 40);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 300);
        decorator.clearVillages();
        clearTravellingTraders();
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
        invasionSpawns.clearInvasions();
        invasionSpawns.addInvasion(LOTRInvasions.ROHAN, LOTREventSpawner.EventChance.COMMON);
    }

    @Override
    public boolean canSpawnHostilesInDay() {
        return true;
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(30) == 0) {
            LOTRWorldGenerator treeGen = random.nextInt(3) == 0 ? LOTRTreeType.OAK_DEAD.create(false, random) : LOTRTreeType.CHARRED.create(false, random);
            int trees = 3 + random.nextInt(5);
            for (int l = 0; l < trees; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                treeGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(32) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                deadMoundGen.generate(world, random, i1, j1, k1);
                new LOTRWorldGenSkullPile().generate(world, random, i1, j1, k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_ROHAN_URUK_HIGHLANDS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.ISENGARD.getSubregion("rohan");
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.1f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }
}
