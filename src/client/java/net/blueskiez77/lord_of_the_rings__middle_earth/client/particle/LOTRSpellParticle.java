package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

/**
 * 1.7.10's EntitySpellParticleFX as the mod's spell-type particles extended it:
 * the velocity given is used as is, the sprite runs down the eight "effect"
 * frames over its life, it drifts upward (gravity -0.1 is the old +0.004 a
 * tick), and it fades IN, alpha rising from half to full.
 *
 * <p>{@link #morgulPortal} is LOTREntityMorgulPortalFX: pale green, full-bright
 * and spreading sideways ever faster. {@link #morgulWater} is
 * LOTREntityRiverWaterFX tinted with the Morgul Vale's water colour.
 */
public class LOTRSpellParticle extends SingleQuadParticle {

    /**
     * LOTRBiomeGenMorgulVale's water 0x36604E, after LOTRMod.postload multiplied
     * every biome's water colour into the base water 0x4A68EF.
     */
    private static final int MORGUL_VALE_WATER = 0x0F2749;

    private final SpriteSet sprites;
    private final boolean portal;

    private LOTRSpellParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd,
            SpriteSet sprites, boolean portal) {
        super(level, x, y, z, xd, yd, zd, sprites.first());
        this.sprites = sprites;
        this.portal = portal;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.friction = 0.96F;
        this.gravity = -0.1F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.quadSize = 0.1F * (0.5F + random.nextFloat() * 0.5F);
        this.lifetime = 20 + random.nextInt(20);
        setSpriteFromAge(sprites);
    }

    public static LOTRSpellParticle morgulPortal(ClientLevel level, double x, double y, double z,
            double xd, double yd, double zd, SpriteSet sprites) {
        LOTRSpellParticle p = new LOTRSpellParticle(level, x, y, z, xd, yd, zd, sprites, true);
        p.setColor(0.2F, 0.8F, 0.4F);
        return p;
    }

    public static LOTRSpellParticle morgulWater(ClientLevel level, double x, double y, double z,
            double xd, double yd, double zd, SpriteSet sprites) {
        LOTRSpellParticle p = new LOTRSpellParticle(level, x, y, z, xd, yd, zd, sprites, false);
        float r = ((MORGUL_VALE_WATER >> 16) & 0xFF) / 255.0F;
        float g = ((MORGUL_VALE_WATER >> 8) & 0xFF) / 255.0F;
        float b = (MORGUL_VALE_WATER & 0xFF) / 255.0F;
        p.setColor(p.jitter(r), p.jitter(g), p.jitter(b));
        return p;
    }

    /** MathHelper.randomFloatClamp(rand, c - 0.3, c + 0.3), clamped to 0..1. */
    private float jitter(float c) {
        return Mth.clamp(c - 0.3F + random.nextFloat() * 0.6F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);
        setAlpha(0.5F + 0.5F * ((float) age / lifetime));
        if (portal) {
            xd *= 1.1;
            zd *= 1.1;
        }
    }

    @Override
    protected int getLightCoords(float partialTick) {
        return portal ? 0xF000F0 : super.getLightCoords(partialTick);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }
}
