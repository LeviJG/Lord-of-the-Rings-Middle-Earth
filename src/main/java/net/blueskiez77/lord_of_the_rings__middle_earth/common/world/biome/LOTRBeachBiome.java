package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;

import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenBeach.
 */
public class LOTRBeachBiome extends LOTROceanBiome {

    public LOTRBeachBiome(int i, boolean major) {
        super(i, major);
        setMinMaxHeight(0.1f, 0.0f);
        setTemperatureRainfall(0.8f, 0.4f);
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableLOTRAmbientList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.SEAGULL, 20, 4, 4));
    }

    public LOTRBeachBiome setBeachBlock(BlockState block) {
        topBlock = block;
        fillerBlock = block;
        return this;
    }
}
