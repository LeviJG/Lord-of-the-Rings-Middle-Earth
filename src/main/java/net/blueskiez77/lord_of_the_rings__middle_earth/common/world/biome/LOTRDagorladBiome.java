package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenDagorlad.
 */
public class LOTRDagorladBiome extends LOTRBiome {

    public LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(42956029606L, 1);

    public LOTRNoiseGeneratorPerlin noiseGravel = new LOTRNoiseGeneratorPerlin(7185609602367L, 1);

    public LOTRNoiseGeneratorPerlin noiseMordorGravel = new LOTRNoiseGeneratorPerlin(12480634985056L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 2);

    public LOTRDagorladBiome(int i, boolean major) {
        super(i, major);
        setDisableRain();
        topBlock = LOTRLegacyBlocks.mod("mordorDirt").state(0);
        fillerBlock = LOTRLegacyBlocks.mod("mordorDirt").state(0);
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_BOMBARDIERS, 1);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 2).setConquestOnly();
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 5);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_ITHILIEN, 5);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 0;
        decorator.grassPerChunk = 0;
        decorator.addTree(LOTRTreeType.CHARRED, 1000);
        biomeColors.setSky(5523773);
        biomeColors.setClouds(3355443);
        biomeColors.setFog(6710886);
        biomeColors.setWater(2498845);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_BLACK_URUK, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_WARG, LOTREventSpawner.EventChance.UNCOMMON);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(24) == 0) {
            int boulders = 1 + random.nextInt(4);
            for (int l = 0; l < boulders; ++l) {
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
        double d1 = noiseDirt.getValue(i * 0.09, k * 0.09);
        double d2 = noiseDirt.getValue(i * 0.6, k * 0.6);
        double d3 = noiseGravel.getValue(i * 0.09, k * 0.09);
        double d4 = noiseGravel.getValue(i * 0.6, k * 0.6);
        double d5 = noiseMordorGravel.getValue(i * 0.09, k * 0.09);
        if (d5 + noiseMordorGravel.getValue(i * 0.6, k * 0.6) > 0.5) {
            topBlock = LOTRLegacyBlocks.mod("mordorGravel").state(0);
        } else if (d3 + d4 > 0.6) {
            topBlock = LOTRLegacyBlocks.vanilla("gravel").state(0);
        } else if (d1 + d2 > 0.6) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_DAGORLAD;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.MORDOR.getSubregion("dagorlad");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.DAGORLAD;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.MORDOR;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.05f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
