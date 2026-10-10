package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenObsidianGravel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenFarHaradJungle.
 */
public class LOTRFarHaradJungleBiome extends LOTRFarHaradBiome {

    public LOTRWorldGenerator obsidianGen = new LOTRWorldGenObsidianGravel();

    public int obsidianGravelRarity = 20;

    public LOTRFarHaradJungleBiome(int i, boolean major) {
        super(i, major);
        if (isMuddy()) {
            topBlock = LOTRLegacyBlocks.mod("mudGrass").state(0);
            fillerBlock = LOTRLegacyBlocks.mod("mud").state(0);
        }
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.FLAMINGO, 10, 4, 4));
        spawnableLOTRAmbientList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BIRD, 10, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 15, 4, 4));
        if (isMuddy()) {
            spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.MIDGES, 10, 4, 4));
        }
        spawnableMonsterList.add(new LOTRSpawnEntry(LOTREntities.JUNGLE_SCORPION, 30, 4, 4));
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM, 10).setSpawnChance(5000);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM_WARRIORS, 30).setSpawnChance(5000);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM_WARRIORS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM, 4);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GULF_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH_WARRIORS, 10);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH, 5);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.MOUNTAIN);
        addBiomeVariant(LOTRBiomeVariant.JUNGLE_DENSE);
        if (isMuddy()) {
            decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("mud"), 32), 80.0f, 0, 256);
            decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("mud"), 32), 80.0f, 0, 64);
        }
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("gold_ore"), 4), 3.0f, 0, 48);
        decorator.addGem(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreGem"), 4, 8, LOTRLegacyBlocks.vanilla("stone")), 3.0f, 0, 48);
        decorator.treesPerChunk = 40;
        decorator.vinesPerChunk = 50;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 4;
        decorator.grassPerChunk = 15;
        decorator.doubleGrassPerChunk = 10;
        decorator.enableFern = true;
        decorator.canePerChunk = 5;
        decorator.cornPerChunk = 10;
        decorator.melonPerChunk = 0.2f;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.JUNGLE, 1000);
        decorator.addTree(LOTRTreeType.JUNGLE_LARGE, 500);
        decorator.addTree(LOTRTreeType.MAHOGANY, 500);
        decorator.addTree(LOTRTreeType.JUNGLE_SHRUB, 1000);
        decorator.addTree(LOTRTreeType.MANGO, 20);
        decorator.addTree(LOTRTreeType.BANANA, 50);
        registerJungleFlowers();
        biomeColors.setGrass(10607421);
        biomeColors.setFoliage(8376636);
        biomeColors.setSky(11977908);
        biomeColors.setFog(11254938);
        biomeColors.setWater(4104311);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.TAUREDAIN(1, 4), 100);
        invasionSpawns.addInvasion(LOTRInvasions.MOREDAIN, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.TAUREDAIN, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int j1;
        super.decorate(world, random, i, k);
        LOTRWorldGenerator vines = new LOTRVanillaWorldGens.Vines();
        for (int l = 0; l < 10; ++l) {
            int i1 = i + random.nextInt(16) + 8;
            j1 = 24;
            int k1 = k + random.nextInt(16) + 8;
            vines.generate(world, random, i1, j1, k1);
        }
        if (obsidianGravelRarity > 0 && random.nextInt(obsidianGravelRarity) == 0) {
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
            j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1, k1);
            obsidianGen.generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_FAR_HARAD_JUNGLE;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FAR_HARAD_JUNGLE.getSubregion("jungle");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.FAR_HARAD_JUNGLE;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        if (random.nextInt(4) == 0) {
            return new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.mod("tallGrass"), 5);
        }
        return super.getRandomGrass(random);
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.TAUREDAIN.setRepair(0.8f);
    }

    public boolean hasJungleLakes() {
        return true;
    }

    public boolean isMuddy() {
        return true;
    }

    @Override
    public double modifyStoneNoiseForFiller(double stoneNoise) {
        if (isMuddy()) {
            stoneNoise += 40.0;
        }
        return stoneNoise;
    }
}
