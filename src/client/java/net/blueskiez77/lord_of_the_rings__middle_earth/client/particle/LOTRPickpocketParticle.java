package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;

/**
 * LOTREntityPickpocketFX: a coin, spinning through eight frames (a frame each two ticks), that falls
 * at full gravity and bounces back as fast as it came down; it lives 30 to 69 ticks.
 * LOTREntityPickpocketFailFX: one of six scraps, fixed, at 0.6 gravity, bouncing back half as fast.
 */
public class LOTRPickpocketParticle extends SingleQuadParticle {

    private final SpriteSet sprites;
    private final boolean spins;
    private final float bounciness;
    private double motionBeforeGround;

    private LOTRPickpocketParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd,
                                   SpriteSet sprites, boolean spins, float gravity, float bounciness) {
        super(level, x, y, z, sprites.first());
        this.sprites = sprites;
        this.spins = spins;
        this.bounciness = bounciness;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.gravity = gravity;
        this.lifetime = 30 + this.random.nextInt(40);
        setSprite(spins ? sprites.get(0, 7) : sprites.get(this.random));
    }

    public static LOTRPickpocketParticle coin(ClientLevel level, double x, double y, double z,
                                              double xd, double yd, double zd, SpriteSet sprites) {
        return new LOTRPickpocketParticle(level, x, y, z, xd, yd, zd, sprites, true, 1.0f, 1.0f);
    }

    public static LOTRPickpocketParticle fail(ClientLevel level, double x, double y, double z,
                                              double xd, double yd, double zd, SpriteSet sprites) {
        return new LOTRPickpocketParticle(level, x, y, z, xd, yd, zd, sprites, false, 0.6f, 0.5f);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.spins) {
            setSprite(this.sprites.get(this.age / 2 % 8, 7));
        }
        if (this.onGround) {
            this.yd = this.motionBeforeGround * -this.bounciness;
        } else {
            this.motionBeforeGround = this.yd;
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }
}
