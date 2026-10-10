package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenMordorMoss;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRBlackUrukFortStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRMordorCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRMordorTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRMordorWargPitStructure;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenMordor.
 */
public class LOTRMordorBiome extends LOTRBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(389502092662L, 1);

    public static LOTRNoiseGeneratorPerlin noiseGravel = new LOTRNoiseGeneratorPerlin(1379468206L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("rock"), 0, 2, 8);

    public boolean enableMordorBoulders = true;

    public LOTRMordorBiome(int i, boolean major) {
        super(i, major);
        topBlock = LOTRLegacyBlocks.mod("rock").state(0);
        fillerBlock = LOTRLegacyBlocks.mod("rock").state(0);
        if (isGorgoroth()) {
            setDisableRain();
        }
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[5];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 30);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_BOMBARDIERS, 5);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 30);
        arrspawnListContainer[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 10);
        arrspawnListContainer[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 7);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.WICKED_DWARVES, 10);
        npcSpawnList.newFactionList(1).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_ITHILIEN, 3);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        npcSpawnList.conquestGainRate = 0.5f;
        decorator.clearOres();
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("mordorDirt"), 0, 60, LOTRLegacyBlocks.mod("rock")), 10.0f, 0, 60);
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("mordorGravel"), 0, 32, LOTRLegacyBlocks.mod("rock")), 10.0f, 0, 60);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreNaurite"), 12, LOTRLegacyBlocks.mod("rock")), 20.0f, 0, 64);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreMorgulIron"), 1, 8, LOTRLegacyBlocks.mod("rock")), 20.0f, 0, 64);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreGulduril"), 1, 8, LOTRLegacyBlocks.mod("rock")), 6.0f, 0, 32);
        decorator.flowersPerChunk = 0;
        decorator.grassPerChunk = 1;
        decorator.generateWater = false;
        if (isGorgoroth()) {
            decorator.sandPerChunk = 0;
            decorator.clayPerChunk = 0;
            decorator.dryReedChance = 1.0f;
            enableRocky = false;
        }
        decorator.addTree(LOTRTreeType.CHARRED, 1000);
        decorator.addRandomStructure(new LOTRMordorCampStructure(false), 100);
        decorator.addRandomStructure(new LOTRMordorWargPitStructure(false), 300);
        decorator.addRandomStructure(new LOTRMordorTowerStructure(false), 500);
        decorator.addRandomStructure(new LOTRBlackUrukFortStructure(false), 2000);
        biomeColors.setGrass(5980459);
        biomeColors.setFoliage(6508333);
        biomeColors.setSky(6700595);
        biomeColors.setClouds(4924185);
        biomeColors.setFog(3154711);
        biomeColors.setWater(2498845);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    /** Mordor rock, Mordor dirt or Mordor gravel: Mordor's own ground. */
    public static boolean isSurfaceMordorBlock(WorldGenLevel world, int i, int j, int k) {
        BlockState block = world.getBlockState(new BlockPos(i, j, k));
        return block == LOTRLegacyBlocks.mod("rock").state(0) || LOTRLegacyBlocks.mod("mordorDirt").matches(block) || LOTRLegacyBlocks.mod("mordorGravel").matches(block);
    }

    @Override
    public boolean canSpawnHostilesInDay() {
        return true;
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (isGorgoroth()) {
            if (enableMordorBoulders && random.nextInt(24) == 0) {
                for (int l = 0; l < 6; ++l) {
                    int i12 = i + random.nextInt(16) + 8;
                    int k12 = k + random.nextInt(16) + 8;
                    boulderGen.generate(world, random, i12, world.getHeight(), k12);
                }
            }

            if (random.nextInt(60) == 0) {
                for (int l = 0; l < 8; ++l) {
                    int i12 = i + random.nextInt(16) + 8;
                    int k12 = k + random.nextInt(16) + 8;
                    int j12 = world.getHeight();
                    decorator.genTree(world, random, i12, j12, k12);
                }
            }

            if (decorator.grassPerChunk > 0) {
                for (int l = 0; l < 6; ++l) {
                    int i12 = i + random.nextInt(6) + 8;
                    int k12 = k + random.nextInt(6) + 8;
                    int j12 = world.getHeight();
                    if (world.isEmptyBlock(new BlockPos(i12, j12, k12)) && LOTRFeature.canBlockStay(LOTRLegacyBlocks.mod("mordorThorn"), world, i12, j12, k12)) {
                        world.setBlock(new BlockPos(i12, j12, k12), LOTRLegacyBlocks.mod("mordorThorn").state(0), 2);
                    }
                }

                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                int j1 = world.getHeight();
                if (random.nextInt(20) == 0 && world.isEmptyBlock(new BlockPos(i1, j1, k1)) && LOTRFeature.canBlockStay(LOTRLegacyBlocks.mod("mordorMoss"), world, i1, j1, k1)) {
                    new LOTRWorldGenMordorMoss().generate(world, random, i1, j1, k1);
                }
            }
        }

        if (LOTRFixedStructures.MORDOR_CHERRY_TREE.isAt(i, k)) {
            int i1 = i + 8;
            int k1 = k + 8;
            int j1 = world.getHeight();
            LOTRTreeType.CHERRY_MORDOR.create(false, random).generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        if (isGorgoroth() && hasMordorSoils()) {
            double d1 = noiseDirt.getValue(i * 0.08, k * 0.08);
            double d2 = noiseDirt.getValue(i * 0.4, k * 0.4);
            double d3 = noiseGravel.getValue(i * 0.08, k * 0.08);
            if (d3 + noiseGravel.getValue(i * 0.4, k * 0.4) > 0.8) {
                topBlock = LOTRLegacyBlocks.mod("mordorGravel").state(0);
                fillerBlock = topBlock;
            } else if (d1 + d2 > 0.5) {
                topBlock = LOTRLegacyBlocks.mod("mordorDirt").state(0);
                fillerBlock = topBlock;
            }
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        for (int j = ySize - 1; j >= 0; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState block = blocks[index];
            if (!LOTRLegacyBlocks.vanilla("stone").matches(block)) {
                continue;
            }
            blocks[index] = LOTRLegacyBlocks.mod("rock").state(0);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_MORDOR;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.MORDOR.getSubregion("mordor");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.MORDOR;
    }

    @Override
    public LOTRRoadType.BridgeType getBridgeBlock() {
        return LOTRRoadType.BridgeType.CHARRED;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.0f;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        if (isGorgoroth()) {
            return new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.mod("mordorGrass"), 0);
        }
        return super.getRandomGrass(random);
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.MORDOR;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.0f;
    }

    public boolean hasMordorSoils() {
        return true;
    }

    @Override
    public boolean hasSky() {
        return !isGorgoroth();
    }

    public boolean isGorgoroth() {
        return true;
    }
}
