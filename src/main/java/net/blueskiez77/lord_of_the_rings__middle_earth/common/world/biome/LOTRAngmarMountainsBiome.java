package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenAngmarMountains.
 */
public class LOTRAngmarMountainsBiome extends LOTRAngmarBiome {

    public LOTRAngmarMountainsBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SNOW_TROLLS, 10);
        npcSpawnList.newFactionList(50).add(arrspawnListContainer);
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        decorator.biomeGemFactor = 0.75f;
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int snowHeight = 130 - rockDepth;
        int stoneHeight = snowHeight - 20;
        for (int j = ySize - 1; j >= stoneHeight; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState block = blocks[index];
            if (j >= snowHeight && block.is(topBlock.getBlock())) {
                blocks[index] = LOTRLegacyBlocks.vanilla("snow").state(0);
                continue;
            }
            if (!block.is(topBlock.getBlock()) && !block.is(fillerBlock.getBlock())) {
                continue;
            }
            blocks[index] = LOTRLegacyBlocks.vanilla("stone").state(0);
        }
    }
}
