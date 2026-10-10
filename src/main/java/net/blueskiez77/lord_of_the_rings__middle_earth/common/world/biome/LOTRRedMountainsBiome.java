package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenRedMountains.
 */
public class LOTRRedMountainsBiome extends LOTRBiome {

    public LOTRRedMountainsBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_MAPLE, 0.2f);
        decorator.biomeOreFactor = 2.0f;
        decorator.biomeGemFactor = 1.5f;
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("rock"), 4, 60, LOTRLegacyBlocks.vanilla("stone")), 12.0f, 0, 96);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreGlowstone"), 4), 8.0f, 0, 48);
        decorator.treesPerChunk = 1;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 1;
        decorator.addTree(LOTRTreeType.OAK, 300);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 50);
        decorator.addTree(LOTRTreeType.SPRUCE, 500);
        decorator.addTree(LOTRTreeType.LARCH, 300);
        decorator.addTree(LOTRTreeType.MAPLE, 300);
        decorator.addTree(LOTRTreeType.MAPLE_LARGE, 50);
        decorator.addTree(LOTRTreeType.FIR, 500);
        decorator.addTree(LOTRTreeType.PINE, 500);
        registerMountainsFlowers();
        addFlower(LOTRLegacyBlocks.mod("dwarfHerb"), 0, 1);
        biomeColors.setSky(13541522);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int stoneHeight = 110 - rockDepth;
        int sandHeight = stoneHeight - 6;
        for (int j = ySize - 1; j >= sandHeight; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState block = blocks[index];
            if (!block.is(topBlock.getBlock()) && !block.is(fillerBlock.getBlock())) {
                continue;
            }
            if (j >= stoneHeight) {
                blocks[index] = LOTRLegacyBlocks.mod("rock").state(4);
                continue;
            }
            blocks[index] = LOTRLegacyBlocks.vanilla("sand").state(1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_RED_MOUNTAINS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.DWARVEN.getSubregion("redMountains");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.RED_MOUNTAINS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.2f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.DWARVEN;
    }
}
