package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitTavernStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitWindmillStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenShireMoors.
 */
public class LOTRShireMoorsBiome extends LOTRShireBiome {

    public LOTRWorldGenerator boulderSmall = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 2);

    public LOTRWorldGenerator boulderLarge = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 3, 5);

    public LOTRShireMoorsBiome(int i, boolean major) {
        super(i, major);
        clearBiomeVariants();
        variantChance = 0.2f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 16;
        decorator.doubleFlowersPerChunk = 0;
        decorator.grassPerChunk = 16;
        decorator.doubleGrassPerChunk = 1;
        decorator.addTree(LOTRTreeType.OAK_LARGE, 8000);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 2000);
        addFlower(LOTRLegacyBlocks.mod("shireHeather"), 0, 100);
        biomeColors.resetGrass();
        decorator.addRandomStructure(new LOTRHobbitWindmillStructure(false), 500);
        decorator.addRandomStructure(new LOTRHobbitFarmStructure(false), 1000);
        decorator.addRandomStructure(new LOTRHobbitTavernStructure(false), 200);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        int i1;
        int l;
        super.decorate(world, random, i, k);
        if (random.nextInt(8) == 0) {
            for (l = 0; l < 4; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderSmall.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(30) == 0) {
            for (l = 0; l < 4; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderLarge.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.SHIRE.getSubregion("moors");
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.1f;
    }

    @Override
    public int spawnCountMultiplier() {
        return super.spawnCountMultiplier() * 2;
    }
}
