package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenDoubleFlower;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenRhun.
 */
public class LOTRRhunBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 4);

    public LOTRRhunBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 20, 2, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 5, 1, 4));
        npcSpawnList.clear();
        variantChance = 0.3f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK_SPRUCE);
        addBiomeVariant(LOTRBiomeVariant.DENSEFOREST_SPRUCE, 3.0f);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.HILLS_SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_ASPEN, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_MAPLE, 0.2f);
        decorator.resetTreeCluster();
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 12;
        decorator.doubleGrassPerChunk = 8;
        decorator.addTree(LOTRTreeType.OAK, 100);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 50);
        decorator.addTree(LOTRTreeType.SPRUCE, 200);
        decorator.addTree(LOTRTreeType.PINE, 500);
        decorator.addTree(LOTRTreeType.PINE_SHRUB, 4000);
        decorator.addTree(LOTRTreeType.CHESTNUT, 500);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 20);
        decorator.addTree(LOTRTreeType.ASPEN, 100);
        decorator.addTree(LOTRTreeType.ASPEN_LARGE, 20);
        decorator.addTree(LOTRTreeType.MAPLE, 50);
        decorator.addTree(LOTRTreeType.MAPLE_LARGE, 20);
        registerRhunPlainsFlowers();
        biomeColors.setGrass(12504664);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 1000);
        registerTravellingTrader(LOTREntities.NEAR_HARAD_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.addInvasion(LOTRInvasions.RHUN, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(200) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_RHUN;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.RHUN.getSubregion("rhun");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.RHUN;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public LOTRWorldGenerator getRandomWorldGenForDoubleFlower(RandomSource random) {
        if (random.nextInt(4) == 0) {
            LOTRWorldGenDoubleFlower doubleFlowerGen = new LOTRWorldGenDoubleFlower();
            doubleFlowerGen.setFlowerType(0);
            return doubleFlowerGen;
        }
        return super.getRandomWorldGenForDoubleFlower(random);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.03f;
    }
}
