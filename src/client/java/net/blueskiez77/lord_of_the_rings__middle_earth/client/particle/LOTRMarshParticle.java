package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;

/**
 * The Dead Marshes' lights, both EntityFlameFX underneath -- here vanilla
 * FlameParticle's behaviour, repeated since its constructor is private: it
 * shrinks as it ages and brightens its own light.
 *
 * <p>LOTREntityMarshFlameFX is a flame that lives 40-59 ticks.
 * LOTREntityMarshLightFX is the same on particle-sheet cell 49, vanilla's lava
 * sprite, tinted a pale grey-white and moving exactly as it was spawned.
 */
public class LOTRMarshParticle extends RisingParticle {

    private LOTRMarshParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd,
            SpriteSet sprites, RandomSource random) {
        super(level, x, y, z, xd, yd, zd, sprites.get(random));
        this.lifetime = 40 + random.nextInt(20);
    }

    public static LOTRMarshParticle flame(ClientLevel level, double x, double y, double z,
            double xd, double yd, double zd, SpriteSet sprites, RandomSource random) {
        return new LOTRMarshParticle(level, x, y, z, xd, yd, zd, sprites, random);
    }

    public static LOTRMarshParticle light(ClientLevel level, double x, double y, double z,
            double xd, double yd, double zd, SpriteSet sprites, RandomSource random) {
        LOTRMarshParticle particle = new LOTRMarshParticle(level, x, y, z, xd, yd, zd, sprites, random);
        particle.xd = xd;
        particle.yd = yd;
        particle.zd = zd;
        float shade = 0.75f + random.nextFloat() * 0.25f;
        particle.setColor(shade, shade, shade);
        return particle;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    @Override
    public void move(double x, double y, double z) {
        setBoundingBox(getBoundingBox().move(x, y, z));
        setLocationFromBoundingbox();
    }

    @Override
    public float getQuadSize(float partialTick) {
        float progress = (this.age + partialTick) / this.lifetime;
        return this.quadSize * (1.0f - progress * progress * 0.5f);
    }

    @Override
    public int getLightCoords(float partialTick) {
        return LightCoordsUtil.addSmoothBlockEmission(super.getLightCoords(partialTick),
                (this.age + partialTick) / this.lifetime);
    }
}
