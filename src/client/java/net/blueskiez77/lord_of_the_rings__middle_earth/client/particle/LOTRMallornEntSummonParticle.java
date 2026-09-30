package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityMallornEntSummonFX: a large leaf fragment on the arc from the
 * Mallorn Ent to the tree it has called up -- a quadratic curve through a
 * point twenty blocks above their midpoint, seven tenths of the way up each.
 * Each of the sixty is fixed at its own point {@code t} along the arc, and
 * lasts {@code 40t} ticks, so the arc fades back from the tree to the Ent.
 * It follows the two as they move, and goes if either does.
 */
public class LOTRMallornEntSummonParticle extends TerrainParticle {

    private final Entity summoner;
    private final Entity summoned;
    private final float arcParam;

    public LOTRMallornEntSummonParticle(ClientLevel level, Entity summoner, Entity summoned, float t,
                                        BlockState leaves) {
        super(level, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, leaves, summoned.blockPosition());
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.summoner = summoner;
        this.summoned = summoned;
        this.arcParam = t;
        this.lifetime = (int) (40.0f * t);
        this.quadSize *= 2.0f;
        this.gravity = 0.0f;
        this.hasPhysics = false;
        updateArcPos();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    @Override
    public void tick() {
        super.tick();
        updateArcPos();
    }

    private void updateArcPos() {
        if (!this.summoner.isAlive() || !this.summoned.isAlive()) {
            remove();
            return;
        }
        Vec3 a = new Vec3(this.summoner.getX(), this.summoner.getY() + this.summoner.getBbHeight() * 0.7,
                this.summoner.getZ());
        Vec3 c = new Vec3(this.summoned.getX(), this.summoned.getY() + this.summoned.getBbHeight() * 0.7,
                this.summoned.getZ());
        Vec3 b = a.add(c).scale(0.5).add(0.0, 20.0, 0.0);
        Vec3 ab = a.lerp(b, this.arcParam);
        Vec3 bc = b.lerp(c, this.arcParam);
        Vec3 abbc = ab.lerp(bc, this.arcParam);
        setPos(abbc.x, abbc.y, abbc.z);
    }
}
