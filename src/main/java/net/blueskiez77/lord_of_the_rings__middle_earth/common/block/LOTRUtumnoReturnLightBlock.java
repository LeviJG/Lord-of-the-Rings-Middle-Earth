package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBlockUtumnoReturnLight: the column of light the return portal's tile
 * entity stacks from the portal up to PORTAL_TOP. It has no icons and render
 * type -1, so it draws nothing itself; it exists to light the shaft.
 *
 * <p>Material.circuits with no hardness set, so it breaks instantly; no
 * collision box; replaceable; full light; drops nothing. The collision,
 * replaceability, instant break and drops live in its Properties (see
 * LOTRBlocks); this class only makes it invisible.
 */
public class LOTRUtumnoReturnLightBlock extends Block {
    public static final MapCodec<LOTRUtumnoReturnLightBlock> CODEC =
            simpleCodec(LOTRUtumnoReturnLightBlock::new);

    public LOTRUtumnoReturnLightBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
}
