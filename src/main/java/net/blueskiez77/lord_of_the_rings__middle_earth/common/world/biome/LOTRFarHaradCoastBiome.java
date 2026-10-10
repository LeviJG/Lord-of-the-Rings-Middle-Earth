package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRCorsairCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRCorsairCoveStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenFarHaradCoast.
 */
public class LOTRFarHaradCoastBiome extends LOTRFarHaradSavannahBiome {

    public static LOTRNoiseGeneratorPerlin noiseGrass = new LOTRNoiseGeneratorPerlin(75796728360672L, 1);

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(63275968906L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSand = new LOTRNoiseGeneratorPerlin(127425276902L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSandstone = new LOTRNoiseGeneratorPerlin(267215026920L, 1);

    public LOTRFarHaradCoastBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.LION, 4, 2, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.LIONESS, 4, 2, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.ZEBRA, 8, 4, 8));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.RHINO, 8, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.GEMSBOK, 8, 4, 8));
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.CORSAIRS, 10).setSpawnChance(5000);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        populatedSpawnList.clear();
        topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
        fillerBlock = topBlock;
        biomeTerrain.setXZScale(30.0);
        clearBiomeVariants();
        decorator.addTree(LOTRTreeType.PALM, 4000);
        decorator.treesPerChunk = 1;
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRCorsairCoveStructure(false), 10);
        decorator.addRandomStructure(new LOTRCorsairCampStructure(false), 100);
        clearTravellingTraders();
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_COMMON);
        setBanditEntityClass(LOTREntities.BANDIT_HARAD);
        invasionSpawns.clearInvasions();
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_CORSAIR, LOTREventSpawner.EventChance.COMMON);
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseGrass.getValue(i * 0.06, k * 0.06);
        double d2 = noiseGrass.getValue(i * 0.47, k * 0.47);
        double d3 = noiseDirt.getValue(i * 0.06, k * 0.06);
        double d4 = noiseDirt.getValue(i * 0.47, k * 0.47);
        double d5 = noiseSand.getValue(i * 0.06, k * 0.06);
        double d6 = noiseSand.getValue(i * 0.47, k * 0.47);
        double d7 = noiseSandstone.getValue(i * 0.06, k * 0.06);
        if (d7 + noiseSandstone.getValue(i * 0.47, k * 0.47) > 0.8) {
            topBlock = LOTRLegacyBlocks.vanilla("sandstone").state(0);
            fillerBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
        } else if (d5 + d6 > 0.6) {
            topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
            fillerBlock = topBlock;
        } else if (d3 + d4 > 0.5) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        } else if (d1 + d2 > 0.4) {
            topBlock = LOTRLegacyBlocks.vanilla("grass").state(0);
            fillerBlock = LOTRLegacyBlocks.vanilla("dirt").state(0);
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_CORSAIR_COASTS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.1f;
    }
}
