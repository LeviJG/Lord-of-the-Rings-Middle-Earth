package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.LilyPadBlock;

/**
 * Fangorn riverweed.
 *
 * <p>LOTRBlockFangornRiverweed extended BlockLilyPad, so it floats flat on
 * water, breaks boats that hit it, and can only be placed on a water source.
 * LilyPadBlock is the modern class and its constructor is protected, which is
 * the only reason this subclass exists.
 *
 * <p>The one thing the original changed about a lily pad was the colour:
 * colorMultiplier, getBlockColor and getRenderColor all returned 0xFFFFFF, so
 * riverweed is NOT tinted the way vanilla's pad is. That is handled in the
 * model -- it carries no tintindex -- rather than here.
 */
public class LOTRRiverweedBlock extends LilyPadBlock {
    // LilyPadBlock.codec() is declared as MapCodec<LilyPadBlock>, so the
    // override cannot narrow the type.
    public static final MapCodec<LilyPadBlock> CODEC = simpleCodec(LOTRRiverweedBlock::new);

    public LOTRRiverweedBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<LilyPadBlock> codec() {
        return CODEC;
    }
}
