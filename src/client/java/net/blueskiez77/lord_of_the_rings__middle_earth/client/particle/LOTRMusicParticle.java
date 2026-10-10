package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

/**
 * LOTREntityMusicFX: a note particle of a random pitch's colour, growing in as a note does, that
 * drifts the way it was sent, slowing by a fiftieth each tick, for 8 to 27 ticks.
 */
public class LOTRMusicParticle extends SingleQuadParticle {

    private double noteMoveX;
    private double noteMoveY;
    private double noteMoveZ;

    public LOTRMusicParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd,
                             SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        this.noteMoveX = xd;
        this.noteMoveY = yd;
        this.noteMoveZ = zd;
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        float pitch = this.random.nextFloat();
        this.rCol = Math.max(0.0f, Mth.sin((pitch + 0.0f) * Mth.TWO_PI) * 0.65f + 0.35f);
        this.gCol = Math.max(0.0f, Mth.sin((pitch + 1.0f / 3.0f) * Mth.TWO_PI) * 0.65f + 0.35f);
        this.bCol = Math.max(0.0f, Mth.sin((pitch + 2.0f / 3.0f) * Mth.TWO_PI) * 0.65f + 0.35f);
        this.quadSize *= 1.5f;
        this.lifetime = 8 + this.random.nextInt(20);
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
        move(this.noteMoveX, this.noteMoveY, this.noteMoveZ);
        this.noteMoveX *= 0.98;
        this.noteMoveY *= 0.98;
        this.noteMoveZ *= 0.98;
    }

    /** EntityNoteFX: full size within the first thirty-second of its life. */
    @Override
    public float getQuadSize(float partialTick) {
        return this.quadSize * Mth.clamp((this.age + partialTick) / this.lifetime * 32.0f, 0.0f, 1.0f);
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }
}
