package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRFeature;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenTundra.
 */
public class LOTRTundraBiome extends LOTRBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(47684796930956L, 1);

    public static LOTRNoiseGeneratorPerlin noiseStone = new LOTRNoiseGeneratorPerlin(8894086030764L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSnow = new LOTRNoiseGeneratorPerlin(2490309256000602L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRTundraBiome(int i, boolean major) {
        super(i, major);
        setEnableSnow();
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 10, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 10, 4, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.ELK, 10, 4, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 10, 1, 4));
        spawnableLOTRAmbientList.clear();
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10).setSpawnChance(1000);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 5).setSpawnChance(1000);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_NORTH, 10).setSpawnChance(5000);
        npcSpawnList.newFactionList(10).add(arrspawnListContainer2);
        variantChance = 0.2f;
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_SPRUCE);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK_SPRUCE);
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 2;
        decorator.grassPerChunk = 4;
        decorator.doubleGrassPerChunk = 1;
        decorator.generateOrcDungeon = true;
        decorator.addTree(LOTRTreeType.SPRUCE_THIN, 100);
        decorator.addTree(LOTRTreeType.SPRUCE_DEAD, 100);
        decorator.addTree(LOTRTreeType.PINE, 100);
        decorator.addTree(LOTRTreeType.FIR, 100);
        decorator.addTree(LOTRTreeType.MAPLE, 10);
        decorator.addTree(LOTRTreeType.BEECH, 10);
        registerTaigaFlowers();
        decorator.addRandomStructure(new LOTRRuinedHouseStructure(false), 1500);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 500);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
    }

    public static boolean isTundraSnowy(int i, int k) {
        double d1 = noiseSnow.getValue(i * 0.002, k * 0.002);
        double d2 = noiseSnow.getValue(i * 0.05, k * 0.05);
        double d3 = noiseSnow.getValue(i * 0.3, k * 0.3);
        return d1 + d2 * 0.3 + d3 * 0.3 > 0.8;
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(2) == 0) {
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
            int bushes = 4 + random.nextInt(20);
            for (int l = 0; l < bushes; ++l) {
                int i2 = i1 + LOTRWorldGenUtil.getRandomIntegerInRange(random, -4, 4);
                int k2 = k1 + LOTRWorldGenUtil.getRandomIntegerInRange(random, -4, 4);
                int j2 = j1 + LOTRWorldGenUtil.getRandomIntegerInRange(random, -1, 1);
                BlockState below = world.getBlockState(new BlockPos(i2, j2 - 1, k2));
                BlockState block = world.getBlockState(new BlockPos(i2, j2, k2));
                if (!LOTRFeature.canSustainPlant(world, i2, j2 - 1, k2) || block.getFluidState().isEmpty() == false || !block.canBeReplaced()) {
                    continue;
                }
                LOTRLegacyBlocks.LegacyBlock leafBlock = LOTRLegacyBlocks.vanilla("leaves");
                int leafMeta = 1;
                if (random.nextInt(3) == 0) {
                    leafBlock = LOTRLegacyBlocks.mod("leaves3");
                    leafMeta = 0;
                } else if (random.nextInt(3) == 0) {
                    leafBlock = LOTRLegacyBlocks.mod("leaves2");
                    leafMeta = 1;
                }
                world.setBlock(new BlockPos(i2, j2, k2), leafBlock.state(leafMeta | 4), 2);
            }
        }
        if (random.nextInt(40) == 0) {
            int boulders = 1 + random.nextInt(4);
            for (int l = 0; l < boulders; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseDirt.getValue(i * 0.07, k * 0.07);
        double d2 = noiseDirt.getValue(i * 0.3, k * 0.3);
        double d3 = noiseStone.getValue(i * 0.07, k * 0.07);
        if (d3 + noiseStone.getValue(i * 0.3, k * 0.3) > 1.2) {
            topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
            fillerBlock = topBlock;
        } else if (d1 + d2 > 0.8) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FORODWAITH.getSubregion("tundra");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.FORODWAITH;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.02f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.04f;
    }
}
