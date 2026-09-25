package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

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
    }
}
