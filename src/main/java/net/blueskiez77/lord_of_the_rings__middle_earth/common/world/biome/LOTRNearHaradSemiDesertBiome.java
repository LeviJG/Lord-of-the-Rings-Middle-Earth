package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHaradObeliskStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHaradPyramidStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHaradRuinedFortStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRMoredainMercCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRMumakSkeletonStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenHaradNomad;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenNearHaradSemiDesert.
 */
public class LOTRNearHaradSemiDesertBiome extends LOTRNearHaradBiome {

    public LOTRNearHaradSemiDesertBiome(int i, boolean major) {
        super(i, major);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.NOMADS, 20).setSpawnChance(500);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.NOMAD_WARRIORS, 15).setSpawnChance(500);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.NOMAD_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        clearBiomeVariants();
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.OAK_DEAD, 500);
        decorator.addTree(LOTRTreeType.OAK_DESERT, 500);
        decorator.grassPerChunk = 5;
        decorator.doubleGrassPerChunk = 0;
        decorator.cactiPerChunk = 1;
        decorator.deadBushPerChunk = 1;
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRHaradObeliskStructure(false), 2000);
        decorator.addRandomStructure(new LOTRHaradPyramidStructure(false), 4000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.NEAR_HARAD(1, 4), 1000);
        decorator.addRandomStructure(new LOTRMoredainMercCampStructure(false), 2000);
        decorator.addRandomStructure(new LOTRMumakSkeletonStructure(false), 2000);
        decorator.addRandomStructure(new LOTRHaradRuinedFortStructure(false), 3000);
        decorator.clearVillages();
        decorator.addVillage(new LOTRVillageGenHaradNomad((String) null, 0.5f));
        registerTravellingTrader(LOTREntities.NOMAD_MERCHANT);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int j1;
        int i1;
        int k1;
        super.decorate(world, random, i, k);
        if (random.nextInt(20) == 0 && world.getBlockState(new BlockPos(i1 = i + random.nextInt(16) + 8, j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1 = k + random.nextInt(16) + 8) - 1, k1)) == LOTRLegacyBlocks.vanilla("sand")) {
            world.setBlock(new BlockPos(i1, j1, k1), LOTRLegacyBlocks.vanilla("dirt").state(0), 3);
            LOTRTreeType treeType = LOTRTreeType.OAK_DESERT;
            LOTRWorldGenerator tree = treeType.create(false, random);
            if (!tree.generate(world, random, i1, j1 + 1, k1)) {
                world.setBlock(new BlockPos(i1, j1, k1), LOTRLegacyBlocks.vanilla("sand").state(0), 3);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = BIOME_TERRAIN_NOISE.getValue(i * 0.08, k * 0.08);
        if (d1 + BIOME_TERRAIN_NOISE.getValue(i * 0.3, k * 0.3) > 0.3) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.05f;
    }
}
