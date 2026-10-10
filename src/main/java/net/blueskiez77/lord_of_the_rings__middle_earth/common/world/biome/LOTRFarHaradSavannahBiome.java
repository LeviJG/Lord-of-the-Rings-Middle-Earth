package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenSand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenYams;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRMoredainCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRMoredainVillageStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenFarHaradSavannah.
 */
public class LOTRFarHaradSavannahBiome extends LOTRFarHaradBiome {

    public static LOTRNoiseGeneratorPerlin populatedNoise = new LOTRNoiseGeneratorPerlin(100L, 1);

    public LOTRBiomeSpawnList populatedSpawnList = new LOTRBiomeSpawnList(getClass().getName());

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRFarHaradSavannahBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH, 10).setSpawnChance(10000);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH_WARRIORS, 5).setSpawnChance(10000);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH, 5);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 1).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 10);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.NOMAD_WARRIORS, 5);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GULF_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM_WARRIORS, 10);
        arrspawnListContainer6[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM, 5);
        arrspawnListContainer6[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM, 5).setConquestThreshold(100.0f);
        arrspawnListContainer6[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM, 5).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer7 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer7[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer7);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer8 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer8[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH, 10);
        arrspawnListContainer8[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH_WARRIORS, 5);
        populatedSpawnList.newFactionList(0).add(arrspawnListContainer8);
        variantChance = 0.3f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
        addBiomeVariant(LOTRBiomeVariant.SAVANNAH_BAOBAB, 3.0f);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND, 2.0f);
        addBiomeVariant(LOTRBiomeVariant.HILLS_SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.WASTELAND);
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("redClay"), 32, LOTRLegacyBlocks.vanilla("dirt")), 40.0f, 0, 80);
        decorator.setTreeCluster(3, 60);
        decorator.clayGen = new LOTRWorldGenSand(LOTRLegacyBlocks.mod("redClay"), 5, 1);
        decorator.clayPerChunk = 4;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 12;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.melonPerChunk = 0.01f;
        decorator.addRandomStructure(new LOTRMoredainVillageStructure(false), 200);
        decorator.addRandomStructure(new LOTRMoredainCampStructure(false), 500);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.TAUREDAIN(1, 2), 5000);
        registerTravellingTrader(LOTREntities.NOMAD_MERCHANT);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        setBanditEntityClass(LOTREntities.BANDIT_HARAD);
        invasionSpawns.addInvasion(LOTRInvasions.MOREDAIN, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.TAUREDAIN, LOTREventSpawner.EventChance.RARE);
    }

    public static boolean isBiomePopulated(int i, int j, int k) {
        double scale = 8.0E-4;
        double d = populatedNoise.getValue(i * scale, k * scale);
        return d > 0.5;
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(32) == 0) {
            int boulders = 1 + random.nextInt(4);
            for (int l = 0; l < boulders; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(6) == 0) {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
            new LOTRWorldGenYams().generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_FAR_HARAD_SAVANNAH;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FAR_HARAD.getSubregion("savannah");
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.75f;
    }

    @Override
    public LOTRBiomeSpawnList getNPCSpawnList(WorldGenLevel world, RandomSource random, int i, int j, int k, LOTRBiomeVariant variant) {
        if (isBiomePopulated(i, j, k)) {
            return populatedSpawnList;
        }
        return super.getNPCSpawnList(world, random, i, j, k, variant);
    }

    @Override
    public LOTRWorldGenerator getRandomWorldGenForDoubleFlower(RandomSource random) {
        if (random.nextInt(6) == 0) {
            LOTRVanillaWorldGens.DoublePlant gen = new LOTRVanillaWorldGens.DoublePlant();
            gen.func_150548_a(0);
            return gen;
        }
        return super.getRandomWorldGenForDoubleFlower(random);
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
