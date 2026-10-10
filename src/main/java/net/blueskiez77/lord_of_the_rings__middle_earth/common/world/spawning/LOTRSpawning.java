package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkGenerator;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.gamerules.GameRules;

/**
 * Middle-earth's spawning each tick: its creatures (LOTRSpawnerAnimals, in place of vanilla's
 * spawner, animals every 400 ticks), its NPCs (LOTRSpawnerNPCs) and its rarer comings
 * (LOTREventSpawner), while mobs may spawn.
 */
public final class LOTRSpawning {

    private LOTRSpawning() {
    }

    public static void init() {
        ServerTickEvents.END_LEVEL_TICK.register(LOTRSpawning::tick);
    }

    private static void tick(ServerLevel level) {
        if (!(level.getChunkSource().getGenerator() instanceof LOTRChunkGenerator) || !level.getGameRules().get(GameRules.SPAWN_MOBS)) {
            return;
        }
        boolean hostiles = level.getDifficulty() != Difficulty.PEACEFUL;
        LOTRSpawnerAnimals.performSpawning(level, hostiles, true, level.getGameTime() % 400L == 0L);
        LOTRSpawnerNPCs.performSpawning(level);
        LOTREventSpawner.performSpawning(level);
    }
}
