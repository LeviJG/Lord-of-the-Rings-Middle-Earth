package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRLamedonWatchfortStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenGondor;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenLamedon.
 */
public class LOTRLamedonBiome extends LOTRGondorBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRLamedonBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 30, 2, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.SHEEP, 40, 4, 4));
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LAMEDON_HILLMEN, 20).setSpawnChance(50);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LAMEDON_SOLDIERS, 25).setSpawnChance(50);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LAMEDON_HILLMEN, 20);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LAMEDON_SOLDIERS, 25);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[5];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 20);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 4).setConquestThreshold(50.0f);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 1).setConquestThreshold(50.0f);
        arrspawnListContainer3[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(100.0f);
        arrspawnListContainer3[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_WARGS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HARNEDHRIM, 2).setConquestThreshold(100.0f);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HARNEDOR_WARRIORS, 10);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.COAST_SOUTHRONS, 2).setConquestThreshold(100.0f);
        arrspawnListContainer5[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLINGS, 2).setConquestThreshold(100.0f);
        arrspawnListContainer6[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 10);
        arrspawnListContainer6[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer7 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer7[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer7);
        clearBiomeVariants();
        variantChance = 0.3f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_APPLE_PEAR, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_PLUM, 0.5f);
        decorator.setTreeCluster(10, 20);
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 6;
        decorator.doubleGrassPerChunk = 1;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.OAK, 500);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 100);
        decorator.addTree(LOTRTreeType.BIRCH, 50);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 10);
        decorator.addTree(LOTRTreeType.BEECH, 50);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 10);
        decorator.addTree(LOTRTreeType.CHESTNUT, 200);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 50);
        decorator.addTree(LOTRTreeType.LARCH, 300);
        decorator.addTree(LOTRTreeType.ASPEN, 300);
        decorator.addTree(LOTRTreeType.APPLE, 5);
        decorator.addTree(LOTRTreeType.PEAR, 5);
        decorator.addTree(LOTRTreeType.PLUM, 5);
        decorator.addTree(LOTRTreeType.OLIVE, 1);
        decorator.addTree(LOTRTreeType.ALMOND, 1);
        biomeColors.setGrass(11646287);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
    }

    @Override
    public void addFiefdomStructures() {
        decorator.addRandomStructure(new LOTRLamedonWatchfortStructure(false), 700);
        decorator.addVillage(new LOTRVillageGenGondor((String) null, LOTRGondorStructure.GondorFiefdom.LAMEDON, 0.8f));
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(16) == 0) {
            for (int l = 0; l < 4; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = BIOME_TERRAIN_NOISE.getValue(i * 0.07, k * 0.07);
        if (d1 + BIOME_TERRAIN_NOISE.getValue(i * 0.4, k * 0.4) > 0.7) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_LAMEDON;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.GONDOR.getSubregion("lamedon");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.LAMEDON;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.75f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.1f;
    }
}
