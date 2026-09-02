package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

/**
 * What a LOTR light source gives off.
 *
 * <p>Shared by the torches and the chandeliers because the 1.7.10 classes shared
 * it too: LOTRBlockChandelier.spawnChandelierParticles reaches into
 * LOTRBlockTorch.createTorchParticle for the four mallorn colours rather than
 * describing them again.
 *
 * <p>The mallorn glows are the fiddly ones. "elvenGlow_&lt;hex&gt;" is a single
 * particle in an arbitrary colour, so they map onto vanilla dust -- the only
 * particle that takes one -- rather than onto four unrelated vanilla effects.
 * They also fire only one tick in three (createTorchParticle returns null the
 * other two) and sit 0.1 lower than the point they are spawned at.
 */
public enum LOTRGlowStyle {
    /** The default: smoke and flame together, no drift. */
    FLAME(ParticleTypes.SMOKE, ParticleTypes.FLAME, 0.0, false, 0.0),

    /** LOTRBlockElvenTorch: plain "elvenGlow", one tick in three, nudged down. */
    ELVEN_GLOW(ParticleTypes.END_ROD, null, 0.0, true, -0.1),

    /**
     * LOTRBlockChandelier case 10: the high-elven CHANDELIER calls the proxy
     * directly rather than going through a torch, so unlike the elven torch it
     * glows every tick and is not nudged.
     */
    ELVEN_GLOW_STEADY(ParticleTypes.END_ROD, null, 0.0, false, 0.0),

    // LOTRMod: new LOTRBlockMallornTorch(<decimal>), the colour of
    // "elvenGlow_" + Integer.toHexString(torchColor).
    MALLORN_SILVER(0xF1F8FA),   // 15857914
    MALLORN_BLUE(0x5ADCFF),     // 5954815
    MALLORN_GOLD(0xEFE85B),     // 15722587
    MALLORN_GREEN(0x54E863),    // 5564515

    /**
     * LOTRBlockWoodElvenTorch: "leafRed_&lt;n&gt;" drifting a hair in every
     * direction. Cherry petals are the closest vanilla has to a falling leaf.
     */
    WOOD_ELVEN_TORCH(ParticleTypes.CHERRY_LEAVES, null, 0.01, false, 0.0),

    /** The chandelier's version of the same, with half the drift. */
    WOOD_ELVEN_CHANDELIER(ParticleTypes.CHERRY_LEAVES, null, 0.005, false, 0.0),

    /** "morgulPortal": sideways a little, upward a lot. */
    MORGUL(ParticleTypes.PORTAL, null, 0.0, false, 0.0);

    private final ParticleOptions primary;
    private final ParticleOptions secondary;
    /** Symmetric random drift applied to x, y and z alike. */
    private final double drift;
    /** Spawn only one tick in three. */
    private final boolean sparse;
    /** Vertical nudge the torch particle carried in its posY. */
    private final double yOffset;

    LOTRGlowStyle(ParticleOptions primary, ParticleOptions secondary,
            double drift, boolean sparse, double yOffset) {
        this.primary = primary;
        this.secondary = secondary;
        this.drift = drift;
        this.sparse = sparse;
        this.yOffset = yOffset;
    }

    LOTRGlowStyle(int colour) {
        this(new DustParticleOptions(colour, 1.0F), null, 0.0, true, -0.1);
    }

    public double yOffset() {
        return yOffset;
    }

    /** Spawns this style's particles at one flame position. */
    public void spawn(Level level, RandomSource random, double x, double y, double z) {
        if (sparse && random.nextInt(3) != 0) {
            return;
        }
        double dx;
        double dy;
        double dz;
        if (this == MORGUL) {
            dx = -0.05 + random.nextDouble() * 0.1;
            dy = 0.1 + random.nextDouble() * 0.1;
            dz = -0.05 + random.nextDouble() * 0.1;
        } else {
            dx = jitter(random, drift);
            dy = jitter(random, drift);
            dz = jitter(random, drift);
        }
        level.addParticle(primary, x, y + yOffset, z, dx, dy, dz);
        if (secondary != null) {
            level.addParticle(secondary, x, y + yOffset, z, 0.0, 0.0, 0.0);
        }
    }

    private static double jitter(RandomSource random, double spread) {
        return spread == 0.0 ? 0.0 : -spread + random.nextDouble() * spread * 2.0;
    }
}
