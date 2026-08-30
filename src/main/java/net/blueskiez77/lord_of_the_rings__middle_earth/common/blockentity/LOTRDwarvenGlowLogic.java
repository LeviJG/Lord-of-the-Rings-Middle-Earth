package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

/**
 * LOTRDwarvenGlowLogic. "Ithildin. It mirrors only starlight and moonlight,
 * and sleeps until one who speaks the words touches it."
 *
 * <p>Three independent factors multiply into the brightness of an ithildin
 * engraving, and all three have to be right for it to show:
 *
 * <ul>
 *   <li><b>Proximity.</b> A counter ramps up over {@code maxGlowTick} ticks
 *       while a player stands within range and back down when they walk away,
 *       which is what gives the doors their slow fade rather than a snap.</li>
 *   <li><b>Night.</b> Zero for the first half of the darkening and rising to
 *       one at full dark, so the engraving never shows in daylight.</li>
 *   <li><b>Skylight.</b> The square root of the sky light level at the block,
 *       so a door buried under a roof stays dark however close you stand.</li>
 * </ul>
 */
public class LOTRDwarvenGlowLogic {

    /** sqrt(level / 15) for each of the sixteen light levels. */
    private static final float[] LIGHT_VALUE_SQRTS = new float[16];

    static {
        for (int i = 0; i <= 15; i++) {
            LIGHT_VALUE_SQRTS[i] = Mth.sqrt(i / 15.0f);
        }
    }

    private int glowTick;
    private int prevGlowTick;
    private int maxGlowTick = 120;
    private int playerRange = 8;
    private float fullGlow = 0.7f;

    public LOTRDwarvenGlowLogic setPlayerRange(int range) {
        this.playerRange = range;
        return this;
    }

    public LOTRDwarvenGlowLogic setGlowTime(int ticks) {
        this.maxGlowTick = ticks;
        return this;
    }

    public LOTRDwarvenGlowLogic setFullGlow(float glow) {
        this.fullGlow = glow;
        return this;
    }

    public int getGlowTick() {
        return glowTick;
    }

    public void setGlowTick(int tick) {
        glowTick = prevGlowTick = tick;
    }

    public void resetGlowTick() {
        glowTick = prevGlowTick = 0;
    }

    /**
     * The original read {@code world.getSunBrightness(tick)}, normalised it out
     * of its 0.2-1.0 range, and took the top half of the resulting darkness.
     * getSkyDarken() is the modern spelling of the same quantity, counting 0 at
     * noon up to 11 at midnight, so the normalisation runs the other way.
     */
    public float getGlowBrightness(Level level, BlockPos pos, float partialTick) {
        float glow = (prevGlowTick + (glowTick - prevGlowTick) * partialTick) / maxGlowTick;
        glow *= fullGlow;

        float daylight = 1.0f - level.getSkyDarken() / 11.0f;
        float night = Math.max(0.0f, (1.0f - daylight) - 0.5f) * 2.0f;

        float skylight = LIGHT_VALUE_SQRTS[level.getBrightness(LightLayer.SKY, pos)];
        return glow * night * skylight;
    }

    /**
     * Client-side only, exactly as in 1.7.10: the counter is presentation, not
     * world state, so it is never saved and never sent. Each client ramps its
     * own copy from the position of its own player.
     */
    public void update(Level level, BlockPos pos) {
        prevGlowTick = glowTick;

        if (!level.isClientSide()) {
            return;
        }

        boolean playerNearby = level.getNearestPlayer(
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, playerRange, false) != null;

        if (playerNearby && glowTick < maxGlowTick) {
            ++glowTick;
        } else if (!playerNearby && glowTick > 0) {
            --glowTick;
        }
    }
}
