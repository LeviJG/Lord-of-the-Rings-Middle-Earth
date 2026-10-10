package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBlastedLand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenSkullPile;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRIsengardWallsStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRUrukCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRUrukWargPitStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRBurntHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenNanCurunir.
 */
public class LOTRNanCurunirBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRWorldGenerator deadMoundGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("wasteBlock"), 0, 1, 3);

    public LOTRNanCurunirBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(EntityTypes.RABBIT, 4, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.CREBAIN, 12, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ISENGARD_SNAGA, 25);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 30);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_WARGS, 15);
        npcSpawnList.newFactionList(65).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RUFFIANS, 10);
        npcSpawnList.newFactionList(5).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDINGS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDING_WARRIORS, 10);
        npcSpawnList.newFactionList(30).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ENTS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HUORNS, 20);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        npcSpawnList.conquestGainRate = 0.2f;
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.logsPerChunk = 1;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 4;
        decorator.doubleGrassPerChunk = 1;
        decorator.addTree(LOTRTreeType.OAK, 100);
        decorator.addTree(LOTRTreeType.OAK_TALL, 100);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 100);
        decorator.addTree(LOTRTreeType.OAK_DEAD, 200);
        decorator.addTree(LOTRTreeType.SPRUCE, 100);
        decorator.addTree(LOTRTreeType.SPRUCE_DEAD, 100);
        decorator.addTree(LOTRTreeType.CHARRED, 300);
        registerPlainsFlowers();
        biomeColors.setSky(8683640);
        decorator.addRandomStructure(new LOTRRuinedHouseStructure(false), 800);
        decorator.addRandomStructure(new LOTRBurntHouseStructure(false), 800);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 5), 400);
        decorator.addRandomStructure(new LOTRUrukCampStructure(false), 120);
        decorator.addRandomStructure(new LOTRUrukWargPitStructure(false), 100);
        decorator.addRandomStructure(new LOTRWorldGenBlastedLand(true), 100);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.ROHAN, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public boolean canSpawnHostilesInDay() {
        return true;
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        LOTRIsengardWallsStructure.INSTANCE.generateWithSetRotation(world, random, i, 0, k, 0);
        if (random.nextInt(40) == 0) {
            int boulders = 1 + random.nextInt(3);
            for (int l = 0; l < boulders; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(32) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                deadMoundGen.generate(world, random, i1, j1, k1);
                new LOTRWorldGenSkullPile().generate(world, random, i1, j1, k1);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = BIOME_TERRAIN_NOISE.getValue(i * 0.09, k * 0.09);
        if (d1 + BIOME_TERRAIN_NOISE.getValue(i * 0.4, k * 0.4) > 0.3) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_NAN_CURUNIR;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.ISENGARD.getSubregion("isengard");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.NAN_CURUNIR;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.0f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.05f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 2;
    }
}
