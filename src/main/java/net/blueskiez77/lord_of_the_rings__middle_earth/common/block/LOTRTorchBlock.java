package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A standing LOTR torch.
 *
 * <p>TorchBlock hardcodes its own particle in animateTick, so every LOTR torch
 * came out breathing plain flame -- the mallorn torches, the elven ones, the
 * morgul one and the wood-elven one all look nothing like that in 1.7.10. The
 * flame position is LOTRBlockTorch.randomDisplayTick's: dead centre, 0.7 up.
 */
public class LOTRTorchBlock extends TorchBlock {
    public static final MapCodec<LOTRTorchBlock> CODEC =
            simpleCodec(props -> new LOTRTorchBlock(LOTRGlowStyle.FLAME, props));

    private final LOTRGlowStyle glow;

    public LOTRTorchBlock(LOTRGlowStyle glow, Properties properties) {
        super(ParticleTypes.FLAME, properties);
        this.glow = glow;
    }

    @Override
    public MapCodec<? extends TorchBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        glow.spawn(level, random, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5);
    }
}
