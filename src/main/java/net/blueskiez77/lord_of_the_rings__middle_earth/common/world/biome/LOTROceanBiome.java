package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenSeaBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRUnderwaterElvenRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRNumenorRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenOcean.
 */
public class LOTROceanBiome extends LOTRBiome {

    public LOTRWorldGenerator spongeGen = new LOTRWorldGenSeaBlock(LOTRLegacyBlocks.vanilla("sponge"), 0, 24);

    public LOTRWorldGenerator coralGen = new LOTRWorldGenSeaBlock(LOTRLegacyBlocks.mod("coralReef"), 0, 64);

    public LOTROceanBiome(int i, boolean major) {
        super(i, major);
        spawnableWaterCreatureList.add(new LOTRSpawnEntry(EntityTypes.SQUID, 4, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.SEAGULL, 20, 4, 4));
        npcSpawnList.clear();
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreSalt"), 8), 4.0f, 0, 64);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreSalt"), 8, LOTRLegacyBlocks.vanilla("sand")), 0.5f, 56, 80);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreSalt"), 8, LOTRLegacyBlocks.mod("whiteSand")), 0.5f, 56, 80);
        decorator.treesPerChunk = 1;
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 2;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 1;
        decorator.addTree(LOTRTreeType.OAK, 1000);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 100);
        decorator.addTree(LOTRTreeType.BIRCH, 100);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 10);
        decorator.addTree(LOTRTreeType.BEECH, 50);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 5);
        decorator.addTree(LOTRTreeType.APPLE, 3);
        decorator.addTree(LOTRTreeType.PEAR, 3);
        decorator.addRandomStructure(new LOTRNumenorRuinStructure(false), 500);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 400);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    public static boolean isFrozen(int i, int k) {
        if (k > -30000) {
            return false;
        }
        int l = -1;
        return true;
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int j1;
        int i1;
        int k1;
        super.decorate(world, random, i, k);
        if (i < LOTRWaypoint.MITHLOND_SOUTH.getXCoord() && k > LOTRWaypoint.SOUTH_FOROCHEL.getZCoord() && k < LOTRWaypoint.ERYN_VORN.getZCoord() && random.nextInt(200) == 0) {
            i1 = i + random.nextInt(16) + 8;
            k1 = k + random.nextInt(16) + 8;
            j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1, k1);
            new LOTRUnderwaterElvenRuinStructure(false).generate(world, random, i1, j1, k1);
        }
        if (k > -30000) {
            if (random.nextInt(12) == 0 && ((j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1 = i + random.nextInt(16) + 8, k1 = k + random.nextInt(16) + 8)) < 60 || random.nextBoolean())) {
                spongeGen.generate(world, random, i1, j1, k1);
            }
            if (random.nextInt(4) == 0 && ((j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1 = i + random.nextInt(16) + 8, k1 = k + random.nextInt(16) + 8)) < 60 || random.nextBoolean())) {
                coralGen.generate(world, random, i1, j1, k1);
            }
        }
        if (k >= 64000) {
            float chance;
            chance = k >= 130000 ? 1.0f : (k - 64000) / 66000.0f;
            if (random.nextFloat() < chance && random.nextInt(6) == 0) {
                int palms = 1 + random.nextInt(2);
                if (random.nextInt(3) == 0) {
                    ++palms;
                }
                for (int l = 0; l < palms; ++l) {
                    int j12;
                    int k12;
                    int i12 = i + random.nextInt(16) + 8;
                    if (!world.getBlockState(new BlockPos(i12, j12 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i12, k12 = k + random.nextInt(16) + 8) - 1, k12)).isSolidRender() || !LOTRStructureBase2.isSurfaceStatic(world, i12, j12, k12)) {
                        continue;
                    }
                    BlockState prevBlock = world.getBlockState(new BlockPos(i12, j12, k12));
                    world.setBlock(new BlockPos(i12, j12, k12), LOTRLegacyBlocks.vanilla("dirt").state(0), 2);
                    LOTRWorldGenerator palmGen = LOTRTreeType.PALM.create(false, random);
                    if (palmGen.generate(world, random, i12, j12 + 1, k12)) {
                        continue;
                    }
                    world.setBlock(new BlockPos(i12, j12, k12), prevBlock, 2);
                }
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_OCEAN;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.SEA.getSubregion("sea");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.OCEAN;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }
}
