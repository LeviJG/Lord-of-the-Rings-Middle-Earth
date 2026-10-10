package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorObeliskStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorRuinsStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRRuinedGondorTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRCorsairCampStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenTolfalas.
 */
public class LOTRTolfalasBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 6);

    public LOTRTolfalasBiome(int i, boolean major) {
        super(i, major);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.CORSAIRS, 10).setSpawnChance(200);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.CORSAIRS, 10).setConquestOnly();
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10).setSpawnChance(100);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.PELARGIR_SOLDIERS, 2).setSpawnChance(100).setConquestThreshold(50.0f);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_AMROTH_SOLDIERS, 2).setSpawnChance(100).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        decorator.treesPerChunk = 1;
        decorator.flowersPerChunk = 0;
        decorator.doubleFlowersPerChunk = 0;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 6;
        decorator.generateAthelas = true;
        decorator.addTree(LOTRTreeType.OAK_DEAD, 2000);
        decorator.addRandomStructure(new LOTRGondorRuinsStructure(), 1000);
        decorator.addRandomStructure(new LOTRRuinedGondorTowerStructure(false), 1500);
        decorator.addRandomStructure(new LOTRGondorObeliskStructure(false), 1500);
        decorator.addRandomStructure(new LOTRGondorRuinStructure(false), 1500);
        decorator.addRandomStructure(new LOTRCorsairCampStructure(false), 100);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_COMMON);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(4) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int stoneHeight = 90 - rockDepth;
        for (int j = ySize - 1; j >= stoneHeight; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState block = blocks[index];
            if (!block.is(topBlock.getBlock()) && !block.is(fillerBlock.getBlock())) {
                continue;
            }
            blocks[index] = LOTRLegacyBlocks.vanilla("stone").state(0);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_TOLFALAS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.GONDOR.getSubregion("tolfalas");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.TOLFALAS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }
}
