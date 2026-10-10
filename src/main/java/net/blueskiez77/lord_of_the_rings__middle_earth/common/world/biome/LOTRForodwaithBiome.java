package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenStalactites;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature.LOTRMirkOakStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenForodwaith.
 */
public class LOTRForodwaithBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 2);

    public LOTRWorldGenStalactites stalactiteIceGen = new LOTRWorldGenStalactites(LOTRLegacyBlocks.mod("stalactiteIce"));

    public LOTRForodwaithBiome(int i, boolean major) {
        super(i, major);
        setEnableSnow();
        topBlock = LOTRLegacyBlocks.vanilla("snow").state(0);
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCaveCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SNOW_TROLLS, 10).setSpawnChance(100000);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("packed_ice"), 16), 40.0f, 32, 256);
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 0;
        decorator.grassPerChunk = 0;
        decorator.generateWater = false;
        biomeColors.setSky(10069160);
        decorator.addRandomStructure(new LOTRRuinedHouseStructure(false), 4000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 5), 4000);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int i1;
        int k1;
        super.decorate(world, random, i, k);
        // D15: the Utumno entrance, at LOTRFixedStructures.UTUMNO_ENTRANCE.
        if (random.nextInt(32) == 0) {
            int boulders = 1 + random.nextInt(5);
            for (int l = 0; l < boulders; ++l) {
                int i12 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i12, LOTRWorldGenUtil.getHeightValue(world, i12, k1), k1);
            }
        }
        for (int l = 0; l < 2; ++l) {
            i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(60);
            k1 = k + random.nextInt(16) + 8;
            stalactiteIceGen.generate(world, random, i1, j1, k1);
        }
        if (random.nextInt(20000) == 0) {
            LOTRMirkOakStructure tree = ((LOTRMirkOakStructure) LOTRTreeType.RED_OAK_WEIRWOOD.create(false, random)).disableRestrictions();
            i1 = i + random.nextInt(16) + 8;
            int k12 = k + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k12);
            tree.generate(world, random, i1, j1, k12);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_FORODWAITH;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FORODWAITH.getSubregion("forodwaith");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.FORODWAITH;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.0f;
    }
}
