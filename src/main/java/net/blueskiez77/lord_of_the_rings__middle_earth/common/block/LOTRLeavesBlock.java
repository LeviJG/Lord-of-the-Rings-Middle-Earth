package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLeafParticleOptions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBlockLeaves.randomDisplayTick and LOTRBlockLeaves7's: mallorn, Mirk oak and red Mirk oak
 * leaves, and green oak's, now and then let one of the mod's leaves fall -- from just under the
 * block, drifting a little sideways and slowly down.
 */
public class LOTRLeavesBlock extends UntintedParticleLeavesBlock {

    private final LOTRLeafParticleOptions fallingLeaf;
    private final int oneIn;

    public LOTRLeavesBlock(float leafParticleChance, ParticleOptions leafParticle, LOTRLeafParticleOptions fallingLeaf,
                           int oneIn, Properties properties) {
        super(leafParticleChance, leafParticle, properties);
        this.fallingLeaf = fallingLeaf;
        this.oneIn = oneIn;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextInt(this.oneIn) == 0) {
            double d = pos.getX() + random.nextFloat();
            double d1 = pos.getY() - 0.05;
            double d2 = pos.getZ() + random.nextFloat();
            double d3 = -0.1 + random.nextFloat() * 0.2f;
            double d4 = -0.03 - random.nextFloat() * 0.02f;
            double d5 = -0.1 + random.nextFloat() * 0.2f;
            level.addParticle(this.fallingLeaf, d, d1, d2, d3, d4, d5);
        }
    }
}
