package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Client side of LOTRParticles. The sprite lists are in
 * assets/lotr/particles/, pointing at vanilla's own frames ("effect",
 * "generic", "flame", "lava"), which are the sheet cells the 1.7.10 particles
 * drew from.
 */
public final class LOTRParticleProviders {

    private LOTRParticleProviders() {
    }

    public static void init() {
        ParticleProviderRegistry registry = ParticleProviderRegistry.getInstance();
        registry.register(LOTRParticles.MORGUL_PORTAL, sprites -> (type, level, x, y, z, xd, yd, zd, random) ->
                LOTRSpellParticle.morgulPortal(level, x, y, z, xd, yd, zd, sprites));
        registry.register(LOTRParticles.MORGUL_WATER, sprites -> (type, level, x, y, z, xd, yd, zd, random) ->
                LOTRSpellParticle.morgulWater(level, x, y, z, xd, yd, zd, sprites));
        registry.register(LOTRParticles.WHITE_SMOKE, sprites -> (type, level, x, y, z, xd, yd, zd, random) ->
                LOTRSmokeParticle.white(level, x, y, z, xd, yd, zd, sprites));
        registry.register(LOTRParticles.QUENDITE_SMOKE, sprites -> (type, level, x, y, z, xd, yd, zd, random) ->
                LOTRSmokeParticle.quendite(level, x, y, z, xd, yd, zd, sprites));
        registry.register(LOTRParticles.CHILL, sprites -> (type, level, x, y, z, xd, yd, zd, random) ->
                LOTRSmokeParticle.chill(level, x, y, z, xd, yd, zd, sprites));
        registry.register(LOTRParticles.MARSH_FLAME, sprites -> (type, level, x, y, z, xd, yd, zd, random) ->
                LOTRMarshParticle.flame(level, x, y, z, xd, yd, zd, sprites, random));
        registry.register(LOTRParticles.MARSH_LIGHT, sprites -> (type, level, x, y, z, xd, yd, zd, random) ->
                LOTRMarshParticle.light(level, x, y, z, xd, yd, zd, sprites, random));
        registry.register(LOTRParticles.LARGE_STONE, (type, level, x, y, z, xd, yd, zd, random) ->
                new TerrainParticle(level, x, y, z, xd, yd, zd, Blocks.STONE.defaultBlockState()).scale(4.0f));
        registry.register(LOTRParticles.MTC_ARMOR, (type, level, x, y, z, xd, yd, zd, random) ->
                new TerrainParticle(level, x, y, z, xd, yd, zd, Blocks.IRON_BLOCK.defaultBlockState()).scale(4.0f));
        registry.register(LOTRParticles.MTC_SPAWN, (type, level, x, y, z, xd, yd, zd, random) -> {
            Block block = random.nextBoolean() ? Blocks.STONE : random.nextBoolean() ? Blocks.DIRT
                    : random.nextBoolean() ? Blocks.GRAVEL : Blocks.SAND;
            return new TerrainParticle(level, x, y, z, xd, yd, zd, block.defaultBlockState()).scale(2.0f);
        });
        registry.register(LOTRParticles.MTC_HEAL, sprites -> (type, level, x, y, z, xd, yd, zd, random) ->
                new LOTRMtcHealParticle(level, x, y, z, xd, yd, zd, sprites));
        registry.register(LOTRParticles.MALLORN_ENT_SPAWN, (type, level, x, y, z, xd, yd, zd, random) -> {
            Block block = random.nextBoolean() ? Blocks.DIRT : LOTRBuildingBlocks.MALLORN_LOG;
            return new TerrainParticle(level, x, y, z, xd, yd, zd, block.defaultBlockState()).scale(2.0f);
        });
        registry.register(LOTRParticles.MALLORN_ENT_JUMP_SMASH, (type, level, x, y, z, xd, yd, zd, random) ->
                new TerrainParticle(level, x, y, z, xd, yd, zd, LOTRBuildingBlocks.MALLORN_LOG.defaultBlockState())
                        .scale(4.0f));
        registry.register(LOTRParticles.MALLORN_ENT_HEAL, (type, level, x, y, z, xd, yd, zd, random) ->
                new LOTRMallornEntHealParticle(level, x, y, z, xd, yd, zd, type.getState()));
    }
}
