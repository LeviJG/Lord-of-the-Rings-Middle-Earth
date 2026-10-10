package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenRohan;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenWold.
 */
public class LOTRWoldBiome extends LOTRRohanBiome {

    public LOTRWorldGenerator woldBoulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("rock"), 2, 2, 4);

    public LOTRWoldBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        decorator.treesPerChunk = 0;
        decorator.setTreeCluster(8, 100);
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 6;
        decorator.doubleGrassPerChunk = 1;
        decorator.addTree(LOTRTreeType.OAK_DEAD, 400);
        decorator.addTree(LOTRTreeType.BEECH_DEAD, 400);
        registerPlainsFlowers();
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRRohanWatchtowerStructure(false), 1000);
        decorator.clearVillages();
        decorator.addVillage(new LOTRVillageGenRohan((String) null, 0.25f));
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(16) == 0) {
            for (int l = 0; l < 4; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                woldBoulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(30) == 0) {
            int rocks = 10 + random.nextInt(20);
            for (int l = 0; l < rocks; ++l) {
                int j1;
                int rockMeta;
                LOTRLegacyBlocks.LegacyBlock rockBlock;
                int k1;
                int i1 = i + random.nextInt(16) + 8;
                BlockState block = world.getBlockState(new BlockPos(i1, (j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1 = k + random.nextInt(16) + 8)) - 1, k1));
                if (!block.is(topBlock.getBlock()) && !block.is(fillerBlock.getBlock())) {
                    continue;
                }
                if (random.nextBoolean()) {
                    rockBlock = LOTRLegacyBlocks.mod("rock");
                    rockMeta = 2;
                } else {
                    if (random.nextInt(5) == 0) {
                        rockBlock = LOTRLegacyBlocks.vanilla("gravel");
                    } else {
                        rockBlock = LOTRLegacyBlocks.vanilla("stone");
                    }
                    rockMeta = 0;
                }
                if (random.nextInt(3) == 0) {
                    world.setBlock(new BlockPos(i1, j1 - 1, k1), rockBlock.state(rockMeta), 2);
                    continue;
                }
                world.setBlock(new BlockPos(i1, j1, k1), rockBlock.state(rockMeta), 2);
                LOTRFeature.onPlantGrow(world, i1, j1 - 1, k1);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = BIOME_TERRAIN_NOISE.getValue(i * 0.005, k * 0.005);
        if (d1 + BIOME_TERRAIN_NOISE.getValue(i * 0.4, k * 0.4) > 1.0) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.ROHAN.getSubregion("wold");
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.1f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.005f;
    }

    @Override
    public int spawnCountMultiplier() {
        return super.spawnCountMultiplier() * 3;
    }
}
