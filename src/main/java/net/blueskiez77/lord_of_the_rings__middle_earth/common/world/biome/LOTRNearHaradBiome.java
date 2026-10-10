package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHaradObeliskStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHaradPyramidStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHaradRuinedFortStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRMumakSkeletonStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenHaradNomad;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenNearHarad.
 */
public class LOTRNearHaradBiome extends LOTRBiome {

    public static LOTRNoiseGeneratorPerlin noiseAridGrass = new LOTRNoiseGeneratorPerlin(62926025827260L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRWorldGenerator boulderGenSandstone = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("sandstone"), 0, 1, 3);

    public LOTRNearHaradBiome(int i, boolean major) {
        super(i, major);
        setDisableRain();
        topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
        fillerBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.CAMEL, 10, 2, 6));
        spawnableLOTRAmbientList.clear();
        spawnableMonsterList.add(new LOTRSpawnEntry(LOTREntities.DESERT_SCORPION, 10, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.NOMADS, 20).setSpawnChance(10000);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.NOMAD_WARRIORS, 15).setSpawnChance(10000);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        variantChance = 0.8f;
        addBiomeVariant(LOTRBiomeVariant.DUNES, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.BOULDERS_RED);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND_SAND);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("lapis_ore"), 6), 1.0f, 0, 48);
        decorator.grassPerChunk = 0;
        decorator.doubleGrassPerChunk = 0;
        decorator.cactiPerChunk = 0;
        decorator.deadBushPerChunk = 0;
        decorator.addTree(LOTRTreeType.OAK_DEAD, 800);
        decorator.addTree(LOTRTreeType.OAK_DESERT, 200);
        registerHaradFlowers();
        biomeColors.setFog(16180681);
        decorator.addRandomStructure(new LOTRHaradObeliskStructure(false), 3000);
        decorator.addRandomStructure(new LOTRHaradPyramidStructure(false), 3000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.NEAR_HARAD(1, 4), 2000);
        decorator.addRandomStructure(new LOTRMumakSkeletonStructure(false), 1500);
        decorator.addRandomStructure(new LOTRHaradRuinedFortStructure(false), 3000);
        decorator.addVillage(new LOTRVillageGenHaradNomad((String) null, 0.05f));
        clearTravellingTraders();
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        setBanditEntityClass(LOTREntities.BANDIT_HARAD);
    }

    /** The original raised decorator.grassPerChunk by this about its decorate. */
    @Override
    public int getGrassPerChunk(int i, int k) {
        int grasses = decorator.grassPerChunk;
        double d1 = noiseAridGrass.getValue(i * 0.002, k * 0.002);
        if (d1 > 0.5) {
            ++grasses;
        }
        return grasses;
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        int k12;
        int i1;
        int j1;
        int j12;
        int l;
        int i12;
        super.decorate(world, random, i, k);
        if (random.nextInt(50) == 0) {
            i12 = i + random.nextInt(16) + 8;
            k12 = k + random.nextInt(16) + 8;
            j12 = LOTRWorldGenUtil.getHeightValue(world, i12, k12);
            new LOTRVanillaWorldGens.Cactus().generate(world, random, i12, j12, k12);
        }
        if (random.nextInt(16) == 0) {
            i12 = i + random.nextInt(16) + 8;
            k12 = k + random.nextInt(16) + 8;
            j12 = LOTRWorldGenUtil.getHeightValue(world, i12, k12);
            new LOTRVanillaWorldGens.DeadBush().generate(world, random, i12, j12, k12);
        }
        if (random.nextInt(120) == 0) {
            int boulders = 1 + random.nextInt(4);
            for (l = 0; l < boulders; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                if (random.nextBoolean()) {
                    boulderGen.generate(world, random, i1, j1, k1);
                    continue;
                }
                boulderGenSandstone.generate(world, random, i1, j1, k1);
            }
        }
        if (random.nextInt(2000) == 0) {
            int trees = 1 + random.nextInt(4);
            for (l = 0; l < trees; ++l) {
                i1 = i + random.nextInt(8) + 8;
                k1 = k + random.nextInt(8) + 8;
                j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                decorator.genTree(world, random, i1, j1, k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_NEAR_HARAD;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.NEAR_HARAD.getSubregion("desert");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.HARAD_DESERT;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.05f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        return new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.mod("aridGrass"), 0);
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.HARAD.setRepair(0.5f);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 5.0E-4f;
    }

    public interface ImmuneToHeat {
    }
}
