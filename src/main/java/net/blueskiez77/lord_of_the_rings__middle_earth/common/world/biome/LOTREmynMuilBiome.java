package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenGrassPatch;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenEmynMuil.
 */
public class LOTREmynMuilBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGenSmall = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 4);

    public LOTRWorldGenerator boulderGenLarge = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 5, 8).setHeightCheck(6);

    public LOTRWorldGenerator clayBoulderGenSmall = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("hardened_clay"), 0, 1, 4);

    public LOTRWorldGenerator clayBoulderGenLarge = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("hardened_clay"), 0, 5, 10).setHeightCheck(6);

    public LOTRWorldGenerator grassPatchGen = new LOTRWorldGenGrassPatch();

    public LOTREmynMuilBiome(int i, boolean major) {
        super(i, major);
        topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
        fillerBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
        spawnableCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 1);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 1);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_ITHILIEN, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 2;
        registerMountainsFlowers();
        biomeColors.setGrass(9539937);
        biomeColors.setSky(10000788);
        decorator.generateOrcDungeon = true;
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_WARG, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        int i1;
        int l;
        super.decorate(world, random, i, k);
        for (l = 0; l < 20; ++l) {
            i1 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            if (random.nextInt(5) == 0) {
                clayBoulderGenSmall.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
                continue;
            }
            boulderGenSmall.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
        }
        for (l = 0; l < 20; ++l) {
            i1 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            if (random.nextInt(5) == 0) {
                clayBoulderGenLarge.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
                continue;
            }
            boulderGenLarge.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
        }
        for (l = 0; l < 10; ++l) {
            BlockState block = LOTRLegacyBlocks.vanilla("stone").state(0);
            if (random.nextInt(5) == 0) {
                block = LOTRLegacyBlocks.vanilla("hardened_clay").state(0);
            }
            for (int l1 = 0; l1 < 10; ++l1) {
                int j1;
                int k12;
                int i12 = i + random.nextInt(16) + 8;
                if (world.getBlockState(new BlockPos(i12, (j1 = LOTRWorldGenUtil.getHeightValue(world, i12, k12 = k + random.nextInt(16) + 8)) - 1, k12)).getBlock() != block.getBlock()) {
                    continue;
                }
                int height = j1 + random.nextInt(4);
                for (int j2 = j1; j2 < height && !LOTRStructureBase.isOpaqueAt(world, i12, j2, k12); ++j2) {
                    world.setBlock(new BlockPos(i12, j2, k12), block, 3);
                }
            }
        }
        for (l = 0; l < 3; ++l) {
            i1 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            grassPatchGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_EMYN_MUIL;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.BROWN_LANDS.getSubregion("emynMuil");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.EMYN_MUIL;
    }
}
