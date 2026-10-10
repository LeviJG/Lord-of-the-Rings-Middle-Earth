package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenRhunRedForest.
 */
public class LOTRRhunRedForestBiome extends LOTRRhunLandBiome {

    public LOTRRhunRedForestBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 16, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 20, 4, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 10, 1, 4));
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 10).setSpawnChance(1000);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 8).setConquestOnly();
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 1).setConquestOnly();
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DORWINION_ELVES, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DORWINION_ELF_WARRIORS, 3);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        npcSpawnList.conquestGainRate = 0.5f;
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 6;
        decorator.logsPerChunk = 1;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 2;
        decorator.enableFern = true;
        decorator.addTree(LOTRTreeType.REDWOOD, 10000);
        decorator.addTree(LOTRTreeType.REDWOOD_2, 10000);
        decorator.addTree(LOTRTreeType.REDWOOD_3, 5000);
        decorator.addTree(LOTRTreeType.REDWOOD_4, 5000);
        decorator.addTree(LOTRTreeType.REDWOOD_5, 2000);
        registerForestFlowers();
        decorator.clearRandomStructures();
        decorator.clearVillages();
        biomeColors.resetGrass();
        biomeColors.setGrass(8951356);
        biomeColors.setFog(11259063);
        biomeColors.setFoggy(true);
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState[] blocks = terrain.blocks;
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
        int chunkX = i & 0xF;
        int chunkZ = k & 0xF;
        int xzIndex = chunkX * 16 + chunkZ;
        int ySize = LOTRChunkTerrain.HEIGHT;
        if (variant.treeFactor >= 1.0f && BIOME_TERRAIN_NOISE.getValue(i * 0.05, k * 0.05) + BIOME_TERRAIN_NOISE.getValue(i * 0.4, k * 0.4) > -0.8) {
            int index = LOTRChunkTerrain.index(xzIndex, height);
            if (random.nextFloat() < 0.75f) {
                blocks[index] = LOTRLegacyBlocks.vanilla("dirt").state(2);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_RHUN_REDWOOD;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.5f;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        if (random.nextFloat() < 0.7f) {
            return new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.vanilla("tallgrass"), 2);
        }
        return super.getRandomGrass(random);
    }
}
