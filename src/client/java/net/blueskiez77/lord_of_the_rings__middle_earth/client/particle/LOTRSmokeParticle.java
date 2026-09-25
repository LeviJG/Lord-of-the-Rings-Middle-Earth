package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

/**
 * 1.7.10's EntitySmokeFX, recoloured the way the mod's smoke particles did:
 * {@link #white} is LOTREntityWhiteSmokeFX (a random pale grey) and
 * {@link #quendite} is LOTREntityQuenditeSmokeFX (blue with a random green).
 * Motion, lifetime, growth and the eight "generic" frames are EntitySmokeFX's.
 */
public class LOTRSmokeParticle extends SingleQuadParticle {

    private final SpriteSet sprites;
    private boolean spriteFromAge = true;

    private LOTRSmokeParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd,
            SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites.first());
        this.sprites = sprites;
        this.xd = this.xd * 0.1 + xd;
        this.yd = this.yd * 0.1 + yd;
        this.zd = this.zd * 0.1 + zd;
        this.friction = 0.96F;
        this.gravity = -0.1F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.quadSize *= 0.75F;
        this.lifetime = (int) (8.0 / (random.nextFloat() * 0.8 + 0.2));
        setSpriteFromAge(sprites);
    }

    public static LOTRSmokeParticle white(ClientLevel level, double x, double y, double z,
            double xd, double yd, double zd, SpriteSet sprites) {
        LOTRSmokeParticle p = new LOTRSmokeParticle(level, x, y, z, xd, yd, zd, sprites);
        float grey = 0.6F + p.random.nextFloat() * 0.4F;
        p.setColor(grey, grey, grey);
        return p;
    }

    public static LOTRSmokeParticle quendite(ClientLevel level, double x, double y, double z,
            double xd, double yd, double zd, SpriteSet sprites) {
        LOTRSmokeParticle p = new LOTRSmokeParticle(level, x, y, z, xd, yd, zd, sprites);
        p.setColor(p.random.nextFloat() * 0.3F, 0.5F + p.random.nextFloat() * 0.5F, 1.0F);
        return p;
    }

    /**
     * LOTREntityChillFX: EntitySmokeFX tinted a pale icy blue, living six times
     * as long, on one fixed frame of the smoke sheet, and slowly sinking --
     * motionY -= 0.005 a tick, which is gravity 0.125 against the modern 0.04.
     */
    public static LOTRSmokeParticle chill(ClientLevel level, double x, double y, double z,
            double xd, double yd, double zd, SpriteSet sprites) {
        LOTRSmokeParticle p = new LOTRSmokeParticle(level, x, y, z, xd, yd, zd, sprites);
        float pale = Mth.randomBetween(p.random, 0.8F, 1.0F);
        float blue = p.random.nextFloat() * 0.25F;
        p.setColor(pale * (1.0F - blue), pale * (1.0F - blue), pale);
        p.lifetime *= 6;
        p.gravity = 0.125F;
        p.spriteFromAge = false;
        p.setSprite(sprites.get(p.random));
        return p;
    }

    @Override
    public void tick() {
        super.tick();
        if (spriteFromAge) {
            setSpriteFromAge(sprites);
        }
    }

    /** EntitySmokeFX.renderParticle: grows to full size over the first 1/32 of its life. */
    @Override
    public float getQuadSize(float partialTick) {
        return quadSize * Mth.clamp((age + partialTick) / lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }
}
