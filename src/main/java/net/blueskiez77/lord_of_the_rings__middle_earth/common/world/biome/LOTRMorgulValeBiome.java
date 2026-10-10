package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenMorgulVale.
 */
public class LOTRMorgulValeBiome extends LOTRMordorBiome {

    public LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(1860286702860L, 1);

    public LOTRNoiseGeneratorPerlin noiseGravel = new LOTRNoiseGeneratorPerlin(8903486028509023054L, 1);

    public LOTRNoiseGeneratorPerlin noiseRock = new LOTRNoiseGeneratorPerlin(769385178389572607L, 1);

    public LOTRMorgulValeBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[5];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 15).setSpawnChance(30);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 2).setSpawnChance(30);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(100.0f);
        arrspawnListContainer[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 2).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_ITHILIEN, 3);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        npcSpawnList.conquestGainRate = 0.5f;
        topBlock = LOTRLegacyBlocks.vanilla("grass").state(0);
        fillerBlock = LOTRLegacyBlocks.vanilla("dirt").state(0);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreGulduril"), 1, 8, LOTRLegacyBlocks.mod("rock")), 10.0f, 0, 60);
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 3;
        decorator.dryReedChance = 1.0f;
        decorator.addTree(LOTRTreeType.OAK, 200);
        decorator.addTree(LOTRTreeType.OAK_DESERT, 500);
        decorator.addTree(LOTRTreeType.OAK_DEAD, 500);
        decorator.addTree(LOTRTreeType.CHARRED, 500);
        flowers.clear();
        addFlower(LOTRLegacyBlocks.mod("morgulFlower"), 0, 20);
        biomeColors.setGrass(6054733);
        biomeColors.setFoliage(4475954);
        biomeColors.setSky(7835270);
        biomeColors.setClouds(5860197);
        biomeColors.setFog(6318950);
        biomeColors.setWater(3563598);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        for (int l = 0; l < 4; ++l) {
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
            boolean foundWater = false;
            for (int a = 0; a < 20; ++a) {
                int range = 8;
                int i2 = i1 + LOTRWorldGenUtil.getRandomIntegerInRange(random, -range, range);
                BlockState block = world.getBlockState(new BlockPos(i2, j1 + LOTRWorldGenUtil.getRandomIntegerInRange(random, -range, range), k1 + LOTRWorldGenUtil.getRandomIntegerInRange(random, -range, range)));
                if (!LOTRFeature.isWater(block)) {
                    continue;
                }
                foundWater = true;
                break;
            }
            if (!foundWater) {
                continue;
            }
            LOTRWorldGenerator flowerGen = new LOTRVanillaWorldGens.Flowers(LOTRLegacyBlocks.mod("morgulFlower"));
            flowerGen.generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseDirt.getValue(i * 0.06, k * 0.06);
        double d2 = noiseDirt.getValue(i * 0.3, k * 0.3);
        double d3 = noiseGravel.getValue(i * 0.06, k * 0.06);
        double d4 = noiseGravel.getValue(i * 0.3, k * 0.3);
        double d5 = noiseRock.getValue(i * 0.06, k * 0.06);
        if (d5 + noiseRock.getValue(i * 0.3, k * 0.3) > 1.1) {
            topBlock = LOTRLegacyBlocks.mod("rock").state(0);
            fillerBlock = topBlock;
        } else if (d3 + d4 > 0.7) {
            topBlock = LOTRLegacyBlocks.mod("mordorGravel").state(0);
            fillerBlock = topBlock;
        } else if (d1 + d2 > 0.7) {
            topBlock = LOTRLegacyBlocks.mod("mordorDirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_MORGUL_VALE;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.MORDOR.getSubregion("morgulVale");
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.2f;
    }

    @Override
    public boolean hasMordorSoils() {
        return false;
    }

    @Override
    public boolean isGorgoroth() {
        return false;
    }
}
