package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTREntityMallornEntHealFX: a fragment of a leaf block at twice the usual
 * size, tinted as the block is where it starts, weightless and passing
 * through blocks, carried along the velocity it was given for a second and a
 * half.
 */
public class LOTRMallornEntHealParticle extends TerrainParticle {

    public LOTRMallornEntHealParticle(ClientLevel level, double x, double y, double z, double xd, double yd,
                                      double zd, BlockState state) {
        super(level, x, y, z, xd, yd, zd, state, BlockPos.containing(x, y, z));
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize *= 2.0f;
        this.lifetime = 30;
        this.gravity = 0.0f;
        this.hasPhysics = false;
    }
}
