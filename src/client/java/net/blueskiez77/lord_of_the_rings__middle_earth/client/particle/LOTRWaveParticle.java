package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;

/**
 * LOTREntityWaveFX: a grey puff of spray, as 1.7.10's explosion smoke, that fades in and swells as it
 * rides on at the speed it was given while in the sea, and falls once out of it.
 */
public class LOTRWaveParticle extends SingleQuadParticle {

    private final SpriteSet sprites;
    private final float initScale;
    private final float finalScale;
    private final double origMotionX;
    private final double origMotionZ;

    LOTRWaveParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites.first());
        this.sprites = sprites;
        // EntityExplodeFX's grey.
        float grey = this.random.nextFloat() * 0.3F + 0.7F;
        setColor(grey, grey, grey);
        this.origMotionX = this.xd = xd;
        this.yd = yd;
        this.origMotionZ = this.zd = zd;
        this.initScale = 1.0F + this.random.nextFloat() * 4.0F;
        this.finalScale = this.initScale * (1.2F + this.random.nextFloat() * 0.8F);
        this.quadSize = 0.1F * this.initScale;
        this.lifetime = Mth.randomBetweenInclusive(this.random, 60, 80);
        this.alpha = 0.0F;
        setSprite(sprites.get(0, 7));
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
        if (this.alpha < 1.0F) {
            this.alpha = Math.min(this.alpha + 0.02F, 1.0F);
        }
        this.quadSize = 0.1F * (this.initScale + (float) this.age / this.lifetime * (this.finalScale - this.initScale));
        setSprite(this.sprites.get((this.lifetime - this.age) % 8, 7));
        if (this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
            this.yd += 0.04F + this.random.nextFloat() * 0.06F;
            this.xd = this.origMotionX;
            this.zd = this.origMotionZ;
        } else {
            this.yd -= 0.02;
            this.xd *= 0.98;
            this.zd *= 0.98;
        }
        move(this.xd, this.yd, this.zd);
        this.yd *= 0.995;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }
}
