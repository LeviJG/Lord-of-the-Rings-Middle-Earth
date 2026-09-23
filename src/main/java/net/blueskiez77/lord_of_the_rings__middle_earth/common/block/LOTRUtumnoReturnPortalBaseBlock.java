package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRBlockUtumnoReturnPortalBase: the altar at the bottom of Utumno that fills
 * with each Utumno servant a player kills nearby, and becomes the return
 * portal once full.
 *
 * <p>Its metadata was the sacrifice count, 0 to MAX_SACRIFICE. That count
 * drives both its height -- a one-pixel plate at 0 rising a pixel per kill --
 * and its light, from 8 at 0 to full brightness when full. Unbreakable
 * (hardness -1, resistance Float.MAX_VALUE) and drops nothing.
 *
 * <p>NOT ported yet: the kill hook in LOTREventHandler.onLivingDeath that raises
 * SACRIFICE and swaps the block for utumnoReturnPortal at MAX_SACRIFICE. It
 * needs the Utumno faction's NPCs and the return portal, neither of which
 * exists in the port.
 */
public class LOTRUtumnoReturnPortalBaseBlock extends Block {
    public static final MapCodec<LOTRUtumnoReturnPortalBaseBlock> CODEC =
            simpleCodec(LOTRUtumnoReturnPortalBaseBlock::new);

    /** LOTRBlockUtumnoReturnPortalBase.MAX_SACRIFICE. */
    public static final int MAX_SACRIFICE = 15;

    /** LOTRBlockUtumnoReturnPortalBase.RANGE: how close a kill must be to count. */
    public static final int RANGE = 5;

    /** The 1.7.10 metadata. */
    public static final IntegerProperty SACRIFICE = IntegerProperty.create("sacrifice", 0, MAX_SACRIFICE);

    // setBlockBoundsMeta: height 1/16 + 15/16 * meta/15, i.e. exactly 1 + meta pixels.
    private static final VoxelShape[] SHAPES = new VoxelShape[MAX_SACRIFICE + 1];
    static {
        for (int i = 0; i <= MAX_SACRIFICE; ++i) {
            SHAPES[i] = Block.box(0.0, 0.0, 0.0, 16.0, 1 + i, 16.0);
        }
    }

    public LOTRUtumnoReturnPortalBaseBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(SACRIFICE, 0));
    }

    /**
     * getLightValue: (0.5 + 0.5 * meta/15) * 16, truncated. That reaches 16 at a
     * full altar, one past the engine's maximum, so the top step is clamped.
     */
    public static int lightLevel(BlockState state) {
        float f = (float) state.getValue(SACRIFICE) / MAX_SACRIFICE;
        return Math.min(15, (int) ((0.5f + 0.5f * f) * 16.0f));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SACRIFICE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(SACRIFICE)];
    }
}
