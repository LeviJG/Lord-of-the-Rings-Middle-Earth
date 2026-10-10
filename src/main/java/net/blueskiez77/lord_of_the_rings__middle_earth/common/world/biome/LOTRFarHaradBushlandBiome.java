package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenFarHaradBushland.
 */
public class LOTRFarHaradBushlandBiome extends LOTRFarHaradBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRFarHaradBushlandBiome(int i, boolean major) {
        super(i, major);
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
        decorator.treesPerChunk = 0;
        decorator.setTreeCluster(3, 3);
        decorator.logsPerChunk = 1;
        decorator.grassPerChunk = 16;
        decorator.doubleGrassPerChunk = 10;
        decorator.cornPerChunk = 4;
        decorator.addTree(LOTRTreeType.BIRCH, 100);
        decorator.addTree(LOTRTreeType.BIRCH_TALL, 100);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 25);
        biomeColors.setGrass(13414999);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int l;
        int k1;
        int i1;
        super.decorate(world, random, i, k);
        if (random.nextInt(32) == 0) {
            int boulders = 1 + random.nextInt(4);
            for (l = 0; l < boulders; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(16) == 0) {
            int termites = 1 + random.nextInt(4);
            for (l = 0; l < termites; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("termiteMound"), 0, 1, 4).generate(world, random, i1, j1, k1);
                for (int x = 0; x < 5; ++x) {
                    int k2;
                    int j2;
                    int i2 = i1 - random.nextInt(5) + random.nextInt(5);
                    BlockState surfaceBlock = world.getBlockState(new BlockPos(i2, (j2 = LOTRWorldGenUtil.getHeightValue(world, i2, k2 = k1 - random.nextInt(5) + random.nextInt(5))) - 1, k1));
                    if (!surfaceBlock.isSolidRender() || !LOTRLegacyBlocks.mod("termiteMound").matches(surfaceBlock) && !LOTRStructureBase2.isSurfaceStatic(world, i2, j2 - 1, k1)) {
                        continue;
                    }
                    int j3 = j2 + random.nextInt(4);
                    for (int j4 = j2; j4 <= j3; ++j4) {
                        world.setBlock(new BlockPos(i2, j4, k2), LOTRLegacyBlocks.mod("termiteMound").state(0), 3);
                        LOTRFeature.onPlantGrow(world, i2, j4 - 1, k2);
                    }
                }
            }
        }
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FAR_HARAD.getSubregion("bushland");
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.75f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.05f;
    }
}
