package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitBurrowStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitHoleStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitPicnicBenchStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitTavernStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitWindmillStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenWhiteDowns.
 */
public class LOTRWhiteDownsBiome extends LOTRShireBiome {

    public LOTRWorldGenerator chalkBoulder = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("rock"), 5, 1, 3);

    public LOTRWhiteDownsBiome(int i, boolean major) {
        super(i, major);
        fillerBlock = LOTRLegacyBlocks.mod("rock").state(5);
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HOBBITS, 10).setSpawnChance(100);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HOBBITS, 1).setConquestOnly();
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 3).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 3).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ISENGARD_SNAGA, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 5);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        npcSpawnList.conquestGainRate = 0.2f;
        clearBiomeVariants();
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        biomeColors.resetGrass();
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRHobbitHoleStructure(false), 300);
        decorator.addRandomStructure(new LOTRHobbitBurrowStructure(false), 150);
        decorator.addRandomStructure(new LOTRHobbitHouseStructure(false), 150);
        decorator.addRandomStructure(new LOTRHobbitTavernStructure(false), 300);
        decorator.addRandomStructure(new LOTRHobbitWindmillStructure(false), 600);
        decorator.addRandomStructure(new LOTRHobbitFarmStructure(false), 2000);
        decorator.addRandomStructure(new LOTRHobbitPicnicBenchStructure(false), 200);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 4), 1500);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.ARNOR(1, 4), 1500);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(80) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                chalkBoulder.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_WHITE_DOWNS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.SHIRE.getSubregion("whiteDowns");
    }

    @Override
    public int spawnCountMultiplier() {
        return 5;
    }
}
