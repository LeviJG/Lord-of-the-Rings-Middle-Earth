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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenLostladen.
 */
public class LOTRLostladenBiome extends LOTRBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(486938207230702L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSand = new LOTRNoiseGeneratorPerlin(28507830789060732L, 1);

    public static LOTRNoiseGeneratorPerlin noiseStone = new LOTRNoiseGeneratorPerlin(275928960292060726L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRWorldGenerator boulderGenSandstone = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("sandstone"), 0, 1, 3);

    public LOTRLostladenBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        npcSpawnList.clear();
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.HILLS_SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK, 3.0f);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("lapis_ore"), 6), 1.0f, 0, 48);
        decorator.treesPerChunk = 0;
        decorator.grassPerChunk = 3;
        decorator.doubleGrassPerChunk = 1;
        decorator.flowersPerChunk = 1;
        decorator.cactiPerChunk = 1;
        decorator.deadBushPerChunk = 2;
        decorator.addTree(LOTRTreeType.OAK_DESERT, 1000);
        decorator.addTree(LOTRTreeType.OAK_DEAD, 200);
        registerHaradFlowers();
        biomeColors.setSky(15592678);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        setBanditEntityClass(LOTREntities.BANDIT_HARAD);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(20) == 0) {
            int boulders = 1 + random.nextInt(4);
            for (int l = 0; l < boulders; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                if (random.nextBoolean()) {
                    boulderGen.generate(world, random, i1, j1, k1);
                    continue;
                }
                boulderGenSandstone.generate(world, random, i1, j1, k1);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseDirt.getValue(i * 0.09, k * 0.09);
        double d2 = noiseDirt.getValue(i * 0.4, k * 0.4);
        double d3 = noiseSand.getValue(i * 0.09, k * 0.09);
        double d4 = noiseSand.getValue(i * 0.4, k * 0.4);
        double d5 = noiseStone.getValue(i * 0.09, k * 0.09);
        if (d5 + noiseStone.getValue(i * 0.4, k * 0.4) > 0.3) {
            if (random.nextInt(5) == 0) {
                topBlock = LOTRLegacyBlocks.vanilla("gravel").state(0);
            } else {
                topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
            }
            fillerBlock = topBlock;
        } else if (d3 + d4 > 0.1) {
            if (random.nextInt(5) == 0) {
                topBlock = LOTRLegacyBlocks.vanilla("sandstone").state(0);
            } else {
                topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
            }
            fillerBlock = topBlock;
        } else if (d1 + d2 > -0.2) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_LOSTLADEN;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.NEAR_HARAD.getSubregion("lostladen");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.LOSTLADEN;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        if (random.nextBoolean()) {
            return new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.mod("aridGrass"), 0);
        }
        return super.getRandomGrass(random);
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.HARAD.setRepair(0.3f);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.01f;
    }
}
