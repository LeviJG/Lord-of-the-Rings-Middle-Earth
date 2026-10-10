package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenMeneltarma.
 */
public class LOTRMeneltarmaBiome extends LOTROceanBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 4);

    public LOTRMeneltarmaBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        decorator.setTreeCluster(8, 20);
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 5;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 6;
        decorator.doubleGrassPerChunk = 2;
        decorator.generateAthelas = true;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.CEDAR, 1000);
        decorator.addTree(LOTRTreeType.CEDAR_LARGE, 500);
        decorator.addTree(LOTRTreeType.OAK, 200);
        decorator.addTree(LOTRTreeType.OAK_TALL, 200);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 400);
        decorator.addTree(LOTRTreeType.BIRCH, 200);
        decorator.addTree(LOTRTreeType.BIRCH_TALL, 200);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 400);
        decorator.addTree(LOTRTreeType.BEECH, 200);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 400);
        List<FlowerEntry> flowerDupes = new ArrayList<>();
        for (int l = 0; l < 10; ++l) {
            flowers.clear();
            registerPlainsFlowers();
            flowerDupes.addAll(flowers);
        }
        flowers.clear();
        flowers.addAll(flowerDupes);
        addFlower(LOTRLegacyBlocks.mod("athelas"), 0, 10);
        decorator.clearRandomStructures();
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        super.decorate(world, random, i, k);
        if (random.nextInt(2) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(3) == 0) {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            k1 = k + random.nextInt(16) + 8;
            new LOTRVanillaWorldGens.Flowers(LOTRLegacyBlocks.mod("athelas")).generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = BIOME_TERRAIN_NOISE.getValue(i * 0.1, k * 0.1);
        if (d1 > -0.1) {
            topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_MENELTARMA;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.SEA.getSubregion("meneltarma");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.MENELTARMA;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.2f;
    }

    @Override
    public boolean isHiddenBiome() {
        return true;
    }
}
