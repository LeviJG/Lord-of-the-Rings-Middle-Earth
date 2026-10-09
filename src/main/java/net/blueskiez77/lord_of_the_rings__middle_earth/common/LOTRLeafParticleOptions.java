package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * The original's "leafGold", "leafRed", "leafMirk" and "leafGreen" particles, each optionally
 * "_&lt;age&gt;": the colour is the particle type; the age is {@code lifetime} ticks, plus up to
 * {@code lifetimeRandom} more where the spawner rolled one (as "leafGold_" + (30 + rand.nextInt(30))
 * did), the roll made where the leaf appears. No age is the leaf's own 600.
 */
public record LOTRLeafParticleOptions(ParticleType<LOTRLeafParticleOptions> type, int lifetime, int lifetimeRandom)
        implements ParticleOptions {

    public static final int DEFAULT_LIFETIME = 600;

    public static MapCodec<LOTRLeafParticleOptions> codec(ParticleType<LOTRLeafParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
                Codec.INT.optionalFieldOf("lifetime", DEFAULT_LIFETIME).forGetter(LOTRLeafParticleOptions::lifetime),
                Codec.INT.optionalFieldOf("lifetime_random", 0).forGetter(LOTRLeafParticleOptions::lifetimeRandom)
        ).apply(i, (lifetime, lifetimeRandom) -> new LOTRLeafParticleOptions(type, lifetime, lifetimeRandom)));
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, LOTRLeafParticleOptions> streamCodec(
            ParticleType<LOTRLeafParticleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.VAR_INT, LOTRLeafParticleOptions::lifetime,
                ByteBufCodecs.VAR_INT, LOTRLeafParticleOptions::lifetimeRandom,
                (lifetime, lifetimeRandom) -> new LOTRLeafParticleOptions(type, lifetime, lifetimeRandom));
    }

    /** A leaf of the original's own lifetime, 600 ticks. */
    public static LOTRLeafParticleOptions of(ParticleType<LOTRLeafParticleOptions> type) {
        return new LOTRLeafParticleOptions(type, DEFAULT_LIFETIME, 0);
    }

    /** "leafX_" + (lifetime + rand.nextInt(lifetimeRandom)), or a fixed lifetime with no random part. */
    public static LOTRLeafParticleOptions of(ParticleType<LOTRLeafParticleOptions> type, int lifetime, int lifetimeRandom) {
        return new LOTRLeafParticleOptions(type, lifetime, lifetimeRandom);
    }

    @Override
    public ParticleType<LOTRLeafParticleOptions> getType() {
        return this.type;
    }
}
