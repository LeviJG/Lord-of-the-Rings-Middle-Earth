package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenObsidianGravel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenStreams;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenVolcanoCrater;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenFarHaradVolcano.
 */
public class LOTRFarHaradVolcanoBiome extends LOTRFarHaradBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(5286926989260620260L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRWorldGenerator obsidianGen = new LOTRWorldGenObsidianGravel();

    public LOTRFarHaradVolcanoBiome(int i, boolean major) {
        super(i, major);
        setDisableRain();
        topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
        fillerBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableMonsterList.clear();
        spawnableLOTRAmbientList.clear();
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10).setSpawnChance(200);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        decorator.treesPerChunk = 0;
        decorator.grassPerChunk = 0;
        decorator.doubleGrassPerChunk = 0;
        decorator.flowersPerChunk = 0;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.OAK_DEAD, 100);
        decorator.addTree(LOTRTreeType.ACACIA_DEAD, 200);
        decorator.addTree(LOTRTreeType.CHARRED, 500);
        biomeColors.setSky(5986904);
        biomeColors.setClouds(3355443);
        biomeColors.setFog(6710886);
        biomeColors.setWater(4009759);
    }

    @Override
    public boolean canSpawnHostilesInDay() {
        return true;
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int i1;
        int k1;
        int i12;
        int j1;
        int l;
        super.decorate(world, random, i, k);
        if (random.nextInt(32) == 0) {
            int boulders = 1 + random.nextInt(4);
            for (l = 0; l < boulders; ++l) {
                i12 = i + random.nextInt(16) + 8;
                int k12 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i12, LOTRWorldGenUtil.getHeightValue(world, i12, k12), k12);
            }
        }
        LOTRWorldGenStreams lavaGen = new LOTRWorldGenStreams(LOTRLegacyBlocks.vanilla("flowing_lava"));
        for (l = 0; l < 50; ++l) {
            i12 = i + random.nextInt(16) + 8;
            j1 = 40 + random.nextInt(120);
            int k13 = k + random.nextInt(16) + 8;
            lavaGen.generate(world, random, i12, j1, k13);
        }
        random.nextInt(1);
        i1 = i + random.nextInt(16) + 8;
        k1 = k + random.nextInt(16) + 8;
        j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
        new LOTRWorldGenVolcanoCrater().generate(world, random, i1, j1, k1);
        if (random.nextInt(50) == 0) {
            i1 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1, k1);
            obsidianGen.generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseDirt.getValue(i * 0.09, k * 0.09);
        if (d1 + noiseDirt.getValue(i * 0.4, k * 0.4) > 0.2) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_FAR_HARAD_VOLCANO;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FAR_HARAD.getSubregion("volcano");
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.5f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 2;
    }
}
