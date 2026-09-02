package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A LOTR torch on the side of a block. Same particles as the standing form; the
 * offset away from the wall is LOTRBlockTorch.randomDisplayTick's 0.27, applied
 * opposite the direction the torch faces.
 */
public class LOTRWallTorchBlock extends WallTorchBlock {
    // WallTorchBlock declares codec() as MapCodec<WallTorchBlock>, so the override
    // cannot narrow it.
    public static final MapCodec<WallTorchBlock> CODEC =
            simpleCodec(props -> new LOTRWallTorchBlock(LOTRGlowStyle.FLAME, props));

    private final LOTRGlowStyle glow;

    public LOTRWallTorchBlock(LOTRGlowStyle glow, Properties properties) {
        super(ParticleTypes.FLAME, properties);
        this.glow = glow;
    }

    @Override
    public MapCodec<WallTorchBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction behind = state.getValue(FACING).getOpposite();
        glow.spawn(level, random,
                pos.getX() + 0.5 + 0.27 * behind.getStepX(),
                pos.getY() + 0.7 + 0.22,
                pos.getZ() + 0.5 + 0.27 * behind.getStepZ());
    }
}
