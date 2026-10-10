package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorObeliskStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRRuinedGondorTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRMumakSkeletonStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRNearHaradDesertCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenHarnedor;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenHarondor.
 */
public class LOTRHarondorBiome extends LOTRBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(52809698208569676L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSand = new LOTRNoiseGeneratorPerlin(474929905950956L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRHarondorBiome(int i, boolean major) {
        super(i, major);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_ITHILIEN, 10).setSpawnChance(500);
        npcSpawnList.newFactionList(90, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HARNEDOR_WARRIORS, 75).setSpawnChance(500);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 20).setSpawnChance(500);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.NOMAD_WARRIORS, 5).setSpawnChance(500);
        npcSpawnList.newFactionList(90, 0.0f).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[5];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_ITHILIEN, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 5);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 5).setConquestThreshold(50.0f);
        arrspawnListContainer3[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LOSSARNACH_SOLDIERS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer3[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LEBENNIN_SOLDIERS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HARNEDOR_WARRIORS, 75);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 20);
        arrspawnListContainer4[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.NOMAD_WARRIORS, 5);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer6[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 3).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer7 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer7[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 10);
        arrspawnListContainer7[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer7);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer8 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer8[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer8);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer9 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer9[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer9);
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.HILLS_SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.WASTELAND);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("lapis_ore"), 6), 1.0f, 0, 48);
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 1;
        decorator.flowersPerChunk = 4;
        decorator.cactiPerChunk = 1;
        decorator.deadBushPerChunk = 1;
        decorator.addTree(LOTRTreeType.OAK_DESERT, 1000);
        decorator.addTree(LOTRTreeType.CEDAR, 250);
        decorator.addTree(LOTRTreeType.LEMON, 5);
        decorator.addTree(LOTRTreeType.ORANGE, 5);
        decorator.addTree(LOTRTreeType.LIME, 5);
        decorator.addTree(LOTRTreeType.OLIVE, 5);
        decorator.addTree(LOTRTreeType.OLIVE_LARGE, 5);
        decorator.addTree(LOTRTreeType.ALMOND, 5);
        decorator.addTree(LOTRTreeType.PLUM, 5);
        decorator.addRandomStructure(new LOTRNearHaradDesertCampStructure(false), 3000);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 600);
        decorator.addRandomStructure(new LOTRGondorObeliskStructure(false), 1000);
        decorator.addRandomStructure(new LOTRRuinedGondorTowerStructure(false), 1000);
        decorator.addRandomStructure(new LOTRHarnedorTowerStructure(false), 2000);
        decorator.addRandomStructure(new LOTRMumakSkeletonStructure(false), 6000);
        decorator.addVillage(new LOTRVillageGenHarnedor((String) null, 0.1f).setRuined());
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GONDOR, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GONDOR_ITHILIEN, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GONDOR_DOL_AMROTH, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_BLACK_URUK, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_HARNEDOR, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_COAST, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_UMBAR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_CORSAIR, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_GULF, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int i1;
        int k1;
        int l;
        super.decorate(world, random, i, k);
        if (random.nextInt(16) == 0) {
            int boulders = 1 + random.nextInt(4);
            for (l = 0; l < boulders; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(8) == 0) {
            int trees = 1 + random.nextInt(8);
            for (l = 0; l < trees; ++l) {
                i1 = i + random.nextInt(8) + 8;
                k1 = k + random.nextInt(8) + 8;
                int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                decorator.genTree(world, random, i1, j1, k1);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseDirt.getValue(i * 0.09, k * 0.09);
        double d2 = noiseDirt.getValue(i * 0.4, k * 0.4);
        double d3 = noiseSand.getValue(i * 0.002, k * 0.002);
        double d4 = noiseSand.getValue(i * 0.09, k * 0.09);
        double d5 = noiseSand.getValue(i * 0.4, k * 0.4);
        d4 *= 0.5;
        d5 *= 0.5;
        if (d3 + d4 + d5 > 0.7) {
            topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
            fillerBlock = topBlock;
        } else if (d1 + d2 > 0.3) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_HARONDOR;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.NEAR_HARAD.getSubregion("harondor");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.HARONDOR;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.1f;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        return random.nextInt(3) == 0 ? new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.mod("aridGrass"), 0) : super.getRandomGrass(random);
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.GONDOR_MIX.setRepair(0.6f);
    }
}
