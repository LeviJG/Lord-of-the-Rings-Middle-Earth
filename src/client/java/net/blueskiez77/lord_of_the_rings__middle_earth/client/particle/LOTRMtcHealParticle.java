package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;

/**
 * LOTREntityMTCHealFX: a large red spell wisp, full-bright and passing through
 * blocks, that flies straight along the velocity it was given for a second
 * and a half -- from a troll to the chieftain drawing on it -- running down
 * the eight "effect" frames and fading in from half to full.
 */
public class LOTRMtcHealParticle extends SingleQuadParticle {

    private final SpriteSet sprites;

    LOTRMtcHealParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd,
                        SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites.first());
        this.sprites = sprites;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        setColor(1.0f, 0.3f, 0.3f);
        this.quadSize *= 3.0f;
        this.lifetime = 30;
        this.hasPhysics = false;
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            remove();
            return;
        }
        setSpriteFromAge(this.sprites);
        move(this.xd, this.yd, this.zd);
        setAlpha(0.5f + 0.5f * ((float) this.age / this.lifetime));
    }

    @Override
    protected int getLightCoords(float partialTick) {
        return 0xF000F0;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }
}
