package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;

/**
 * LOTREntityAngryFX: a thundercloud, full bright and weightless, flickering through four frames (a
 * frame each four ticks) and swelling and shrinking between seven and ten tenths of its size; it
 * lives 40 to 59 ticks, fading over the last fifth.
 */
public class LOTRAngryParticle extends SingleQuadParticle {

    private static final float ANGRY_SCALE = 7.5f;

    private final SpriteSet sprites;

    public LOTRAngryParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd,
                             SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        this.sprites = sprites;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = 40 + this.random.nextInt(20);
        this.gravity = 0.0f;
        setSprite(sprites.get(0, 3));
    }

    @Override
    public void tick() {
        super.tick();
        setSprite(this.sprites.get(this.age / 4 % 4, 3));
        float fade = 0.8f;
        float ageF = (float) this.age / this.lifetime;
        if (ageF >= fade) {
            this.alpha = 1.0f - (ageF - fade) / (1.0f - fade);
            if (this.alpha <= 0.0f) {
                remove();
            }
        }
    }

    /** renderParticle: an eighth of its scale either way, the scale pulsing with a cosine of its age. */
    @Override
    public float getQuadSize(float partialTick) {
        float modScale = Mth.clamp((Mth.cos((this.age + partialTick) / 12.0f) + 1.0f) / 2.0f, 0.0f, 1.0f);
        return 0.125f * ANGRY_SCALE * (0.7f + modScale * 0.3f);
    }

    @Override
    public int getLightCoords(float partialTick) {
        return LightCoordsUtil.FULL_BRIGHT;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }
}
