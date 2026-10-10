package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenLastDesert.
 */
public class LOTRLastDesertBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRLastDesertBiome(int i, boolean major) {
        super(i, major);
        setDisableRain();
        topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
        fillerBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.CAMEL, 10, 2, 6));
        spawnableLOTRAmbientList.clear();
        spawnableMonsterList.add(new LOTRSpawnEntry(LOTREntities.DESERT_SCORPION, 10, 4, 4));
        npcSpawnList.clear();
        variantChance = 0.3f;
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        decorator.grassPerChunk = 0;
        decorator.doubleGrassPerChunk = 0;
        decorator.flowersPerChunk = 0;
        decorator.doubleFlowersPerChunk = 0;
        decorator.cactiPerChunk = 0;
        decorator.deadBushPerChunk = 0;
        decorator.addTree(LOTRTreeType.OAK_DEAD, 1000);
        registerRhunPlainsFlowers();
        biomeColors.setGrass(16767886);
        biomeColors.setSky(14736588);
        biomeColors.setClouds(10853237);
        biomeColors.setFog(14406319);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        int i1;
        int j1;
        int k12;
        int l;
        int i12;
        super.decorate(world, random, i, k);
        if (random.nextInt(8) == 0) {
            i12 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            j1 = LOTRWorldGenUtil.getHeightValue(world, i12, k1);
            getRandomWorldGenForGrass(random).generate(world, random, i12, j1, k1);
        }
        if (random.nextInt(100) == 0) {
            i12 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            j1 = LOTRWorldGenUtil.getHeightValue(world, i12, k1);
            new LOTRVanillaWorldGens.Cactus().generate(world, random, i12, j1, k1);
        }
        if (random.nextInt(20) == 0) {
            i12 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            j1 = LOTRWorldGenUtil.getHeightValue(world, i12, k1);
            new LOTRVanillaWorldGens.DeadBush().generate(world, random, i12, j1, k1);
        }
        if (random.nextInt(32) == 0) {
            int boulders = 1 + random.nextInt(4);
            for (l = 0; l < boulders; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k12 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k12), k12);
            }
        }
        if (random.nextInt(500) == 0) {
            int trees = 1 + random.nextInt(4);
            for (l = 0; l < trees; ++l) {
                i1 = i + random.nextInt(8) + 8;
                k12 = k + random.nextInt(8) + 8;
                int j12 = LOTRWorldGenUtil.getHeightValue(world, i1, k12);
                decorator.genTree(world, random, i1, j12, k12);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = BIOME_TERRAIN_NOISE.getValue(i * 0.07, k * 0.07);
        double d2 = BIOME_TERRAIN_NOISE.getValue(i * 0.4, k * 0.4);
        d2 *= 0.6;
        if (d1 + d2 > 0.7) {
            topBlock = LOTRLegacyBlocks.vanilla("grass").state(0);
            fillerBlock = LOTRLegacyBlocks.vanilla("dirt").state(0);
        } else if (d1 + d2 > 0.2) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_LAST_DESERT;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.RHUN.getSubregion("lastDesert");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.RHUN;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.02f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        return new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.mod("aridGrass"), 0);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.0f;
    }
}
