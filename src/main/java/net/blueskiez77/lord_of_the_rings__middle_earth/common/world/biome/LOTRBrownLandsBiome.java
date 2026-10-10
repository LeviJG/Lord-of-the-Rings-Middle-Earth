package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenBrownLands.
 */
public class LOTRBrownLandsBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 2);

    public LOTRBrownLandsBiome(int i, boolean major) {
        super(i, major);
        setDisableRain();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        npcSpawnList.clear();
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 0;
        decorator.grassPerChunk = 2;
        decorator.addTree(LOTRTreeType.OAK_DEAD, 1000);
        biomeColors.setGrass(11373417);
        biomeColors.setSky(8878434);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 3), 2000);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 1000);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_BLACK_URUK, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_WARG, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(8) == 0) {
            int boulders = 1 + random.nextInt(6);
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
        double d1 = BIOME_TERRAIN_NOISE.getValue(i * 0.08, k * 0.08);
        if (d1 + BIOME_TERRAIN_NOISE.getValue(i * 0.7, k * 0.7) > 0.1) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_BROWN_LANDS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.BROWN_LANDS.getSubregion("brownLands");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.BROWN_LANDS;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        if (random.nextInt(3) == 0) {
            return new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.vanilla("tallgrass"), 1);
        }
        return new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.mod("tallGrass"), 0);
    }
}
