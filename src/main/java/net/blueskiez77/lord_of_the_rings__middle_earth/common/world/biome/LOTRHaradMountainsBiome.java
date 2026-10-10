package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenHaradMountains.
 */
public class LOTRHaradMountainsBiome extends LOTRFarHaradBiome {

    public LOTRHaradMountainsBiome(int i, boolean major) {
        super(i, major);
        spawnableMonsterList.clear();
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        decorator.biomeGemFactor = 1.0f;
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int stoneHeight = 100 - rockDepth;
        for (int j = ySize - 1; j >= stoneHeight; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState block = blocks[index];
            if (!block.is(topBlock.getBlock()) && !block.is(fillerBlock.getBlock())) {
                continue;
            }
            blocks[index] = LOTRLegacyBlocks.vanilla("stone").state(0);
        }
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FAR_HARAD.getSubregion("mountains");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.HARAD_MOUNTAINS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.0f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }
}
