package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLeafParticleOptions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;

/**
 * LOTREntityLeafFX: a leaf drifting at a steady speed -- no gravity, no drag -- from where it was
 * let fall, a quarter as large as the 1.7.10 particle scale it was given (0.15 to 0.65), turning
 * through one of its colour's two runs of six frames, a frame each four ticks, until it lands or
 * its time is up.
 *
 * <p>The sprites are the original's misc/particles2.png cells, in two runs of six (see the
 * colour's particle definition): the first run, or the second, chosen as it appears.
 */
public class LOTRLeafParticle extends SingleQuadParticle {

    private static final int FRAMES = 6;

    private final SpriteSet sprites;
    private final int firstFrame;

    public LOTRLeafParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd,
                            SpriteSet sprites, LOTRLeafParticleOptions options) {
        super(level, x, y, z, sprites.first());
        this.sprites = sprites;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize = 0.25f * (0.15f + this.random.nextFloat() * 0.5f);
        this.lifetime = options.lifetime()
                + (options.lifetimeRandom() > 0 ? this.random.nextInt(options.lifetimeRandom()) : 0);
        this.firstFrame = this.random.nextBoolean() ? 0 : FRAMES;
        setSprite(frame());
    }

    private net.minecraft.client.renderer.texture.TextureAtlasSprite frame() {
        return this.sprites.get(this.firstFrame + this.age / 4 % FRAMES, FRAMES * 2 - 1);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        move(this.xd, this.yd, this.zd);
        ++this.age;
        setSprite(frame());
        if (this.onGround || this.age >= this.lifetime || this.y < this.level.getMinY()) {
            remove();
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }
}
