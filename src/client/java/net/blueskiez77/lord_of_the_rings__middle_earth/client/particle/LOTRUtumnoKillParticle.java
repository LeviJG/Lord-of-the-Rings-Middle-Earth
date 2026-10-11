package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;

/**
 * LOTREntityUtumnoKillFX: a flame (EntityFlameFX) on one of the mod's sheet's three dark flame cells,
 * moving exactly as it was sent, and gone the moment it reaches the altar.
 */
public class LOTRUtumnoKillParticle extends RisingParticle {

    private final double paramMotionX;
    private final double paramMotionY;
    private final double paramMotionZ;

    public LOTRUtumnoKillParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites,
                                  RandomSource random) {
        super(level, x, y, z, xd, yd, zd, sprites.get(random));
        this.xd = this.paramMotionX = xd;
        this.yd = this.paramMotionY = yd;
        this.zd = this.paramMotionZ = zd;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    @Override
    public void move(double x, double y, double z) {
        setBoundingBox(getBoundingBox().move(x, y, z));
        setLocationFromBoundingbox();
    }

    @Override
    public void tick() {
        super.tick();
        this.xd = this.paramMotionX;
        this.yd = this.paramMotionY;
        this.zd = this.paramMotionZ;
        if (this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).is(LOTRUtilityBlocks.UTUMNO_RETURN_PORTAL_BASE)) {
            remove();
        }
    }

    @Override
    public float getQuadSize(float partialTick) {
        float progress = (this.age + partialTick) / this.lifetime;
        return this.quadSize * (1.0f - progress * progress * 0.5f);
    }

    @Override
    public int getLightCoords(float partialTick) {
        return LightCoordsUtil.addSmoothBlockEmission(super.getLightCoords(partialTick), (this.age + partialTick) / this.lifetime);
    }
}
