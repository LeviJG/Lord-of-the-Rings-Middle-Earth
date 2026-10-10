package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenLake.
 */
public class LOTRLakeBiome extends LOTRBiome {

    public LOTRLakeBiome(int i, boolean major) {
        super(i, major);
        setMinMaxHeight(-0.5f, 0.2f);
        spawnableCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        npcSpawnList.clear();
        decorator.sandPerChunk = 0;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.SEA.getSubregion("lake");
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    public LOTRLakeBiome setLakeBlock(BlockState block) {
        topBlock = block;
        fillerBlock = block;
        return this;
    }
}
