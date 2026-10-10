package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenImlothMelui.
 */
public class LOTRImlothMeluiBiome extends LOTRLossarnachBiome {

    public LOTRImlothMeluiBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 20;
        decorator.doubleFlowersPerChunk = 12;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 3;
        registerPlainsFlowers();
        addFlower(LOTRLegacyBlocks.vanilla("red_flower"), 0, 80);
        decorator.addTree(LOTRTreeType.MAPLE, 500);
        decorator.addTree(LOTRTreeType.MAPLE_LARGE, 100);
        decorator.addTree(LOTRTreeType.BEECH, 500);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 100);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        for (int l = 0; l < 1; ++l) {
            LOTRWorldGenerator shrub = LOTRTreeType.OAK_SHRUB.create(false, random);
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
            shrub.generate(world, random, i1, j1, k1);
        }
        if (random.nextInt(8) == 0) {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
            new LOTRVanillaWorldGens.Flowers(LOTRLegacyBlocks.mod("athelas")).generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_IMLOTH_MELUI;
    }

    @Override
    public LOTRWorldGenerator getRandomWorldGenForDoubleFlower(RandomSource random) {
        if (random.nextInt(5) > 0) {
            LOTRVanillaWorldGens.DoublePlant doubleFlowerGen = new LOTRVanillaWorldGens.DoublePlant();
            doubleFlowerGen.func_150548_a(4);
            return doubleFlowerGen;
        }
        return super.getRandomWorldGenForDoubleFlower(random);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.5f;
    }
}
