package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

/**
 * The mod's own particles: the types LOTRClientProxy.spawnParticle took by name.
 * Only the ones ported blocks use are here so far; the rest of lotr.client.fx
 * belongs to Track D16. Their client providers are in LOTRParticleProviders.
 */
public final class LOTRParticles {

    /** LOTREntityMorgulPortalFX: green, full-bright spell particles. */
    public static final SimpleParticleType MORGUL_PORTAL = register("morgul_portal");
    /** LOTREntityRiverWaterFX in the Morgul Vale's water colour ("morgulWater"). */
    public static final SimpleParticleType MORGUL_WATER = register("morgul_water");
    /** LOTREntityWhiteSmokeFX. */
    public static final SimpleParticleType WHITE_SMOKE = register("white_smoke");
    /** LOTREntityQuenditeSmokeFX: blue-green smoke off quendite grass. */
    public static final SimpleParticleType QUENDITE_SMOKE = register("quendite_smoke");
    /** LOTREntityChillFX: pale blue smoke that hangs and sinks, off a Chilling blow. */
    public static final SimpleParticleType CHILL = register("chill");

    private LOTRParticles() {
    }

    private static SimpleParticleType register(String name) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name), FabricParticleTypes.simple());
    }

    public static void init() {
    }
}
