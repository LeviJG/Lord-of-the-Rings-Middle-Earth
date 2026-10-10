package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenFarHaradMangrove.
 */
public class LOTRFarHaradMangroveBiome extends LOTRFarHaradBiome {

    public LOTRFarHaradMangroveBiome(int i, boolean major) {
        super(i, major);
        topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("dirt"), 1, 60, LOTRLegacyBlocks.vanilla("sand")), 12.0f, 60, 90);
        decorator.quagmirePerChunk = 1;
        decorator.treesPerChunk = 5;
        decorator.vinesPerChunk = 20;
        decorator.grassPerChunk = 8;
        decorator.enableFern = true;
        decorator.waterlilyPerChunk = 3;
        decorator.addTree(LOTRTreeType.MANGROVE, 1000);
        decorator.addTree(LOTRTreeType.ACACIA, 10);
        decorator.addTree(LOTRTreeType.OAK_DESERT, 5);
        registerSwampFlowers();
        biomeColors.setGrass(10466679);
        biomeColors.setFoliage(6715206);
        biomeColors.setWater(5985085);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int i1;
        int l;
        super.decorate(world, random, i, k);
        for (l = 0; l < 2; ++l) {
            int k1;
            int j1;
            i1 = i + random.nextInt(16);
            if (!world.getBlockState(new BlockPos(i1, (j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1, k1 = k + random.nextInt(16))) - 1, k1)).isSolidRender() || !LOTRFeature.isWater(world.getBlockState(new BlockPos(i1, j1, k1)))) {
                continue;
            }
            decorator.genTree(world, random, i1, j1, k1);
        }
        for (l = 0; l < 20; ++l) {
            int j1;
            int k1;
            i1 = i + random.nextInt(16);
            if (!world.getBlockState(new BlockPos(i1, j1 = 50 + random.nextInt(15), k1 = k + random.nextInt(16))).isSolidRender() || !LOTRFeature.isWater(world.getBlockState(new BlockPos(i1, j1 + 1, k1)))) {
                continue;
            }
            int height = 2 + random.nextInt(3);
            for (int j2 = j1; j2 <= j1 + height; ++j2) {
                world.setBlock(new BlockPos(i1, j2, k1), LOTRLegacyBlocks.mod("wood3").state(3), 2);
            }
        }
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FAR_HARAD.getSubregion("mangrove");
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.4f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.15f;
    }
}
