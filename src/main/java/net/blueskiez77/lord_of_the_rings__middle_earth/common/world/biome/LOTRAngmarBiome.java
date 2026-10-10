package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBlastedLand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar.LOTRAngmarCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar.LOTRAngmarHillmanVillageStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar.LOTRAngmarShrineStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar.LOTRAngmarTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar.LOTRAngmarWargPitStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenAngmar.
 */
public class LOTRAngmarBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 2);

    public LOTRAngmarBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 10, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 2, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 4, 1, 4));
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[7];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 30);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_BOMBARDIERS, 5);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 30);
        arrspawnListContainer[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TROLLS, 30);
        arrspawnListContainer[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HILL_TROLLS, 20);
        arrspawnListContainer[5] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SNOW_TROLLS, 5);
        arrspawnListContainer[6] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_HILLMEN, 20);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.WICKED_DWARVES, 10);
        npcSpawnList.newFactionList(2, 0.0f).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_NORTH, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RIVENDELL_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        npcSpawnList.conquestGainRate = 0.5f;
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 1.0f);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreMorgulIron"), 8), 20.0f, 0, 64);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreGulduril"), 8), 8.0f, 0, 32);
        decorator.flowersPerChunk = 0;
        decorator.grassPerChunk = 4;
        decorator.doubleGrassPerChunk = 1;
        decorator.addTree(LOTRTreeType.SPRUCE_THIN, 100);
        decorator.addTree(LOTRTreeType.SPRUCE, 200);
        decorator.addTree(LOTRTreeType.SPRUCE_DEAD, 150);
        decorator.addTree(LOTRTreeType.CHARRED, 150);
        decorator.addTree(LOTRTreeType.FIR, 100);
        decorator.addTree(LOTRTreeType.PINE, 200);
        biomeColors.setGrass(7896151);
        biomeColors.setSky(5324595);
        biomeColors.setClouds(1644825);
        biomeColors.setFog(1644825);
        decorator.generateOrcDungeon = true;
        decorator.generateTrollHoard = true;
        decorator.addRandomStructure(new LOTRAngmarTowerStructure(false), 300);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.ANGMAR(1, 4), 40);
        decorator.addRandomStructure(new LOTRWorldGenBlastedLand(false), 40);
        decorator.addRandomStructure(new LOTRAngmarShrineStructure(false), 200);
        decorator.addRandomStructure(new LOTRAngmarWargPitStructure(false), 200);
        decorator.addRandomStructure(new LOTRAngmarCampStructure(false), 50);
        decorator.addRandomStructure(new LOTRAngmarHillmanVillageStructure(false), 400);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 300);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
        invasionSpawns.addInvasion(LOTRInvasions.RANGER_NORTH, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.HIGH_ELF_LINDON, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.HIGH_ELF_RIVENDELL, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public boolean canSpawnHostilesInDay() {
        return true;
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        for (int l = 0; l < 4; ++l) {
            int k1;
            int i1 = i + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1 = k + random.nextInt(16) + 8);
            if (j1 <= 80) {
                continue;
            }
            decorator.genTree(world, random, i1, j1, k1);
        }
        if (random.nextInt(6) == 0) {
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
            boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = BIOME_TERRAIN_NOISE.getValue(i * 0.07, k * 0.07);
        if (d1 + BIOME_TERRAIN_NOISE.getValue(i * 0.4, k * 0.4) > 0.5) {
            topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_ANGMAR;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.ANGMAR.getSubregion("angmar");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.ANGMAR;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.1f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
