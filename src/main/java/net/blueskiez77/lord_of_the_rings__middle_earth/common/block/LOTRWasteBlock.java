package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRBlockWaste, the Mordor wasteland's ash-and-slag ground.
 *
 * <p>Its collision box stopped an eighth short of the top and it multiplied
 * the motion of anything inside it by 0.4 -- exactly what 1.7.10 soul sand did.
 * Under the port's modern-vanilla rule it therefore behaves as soul sand does
 * today: a 14-pixel collision box here, and speedFactor 0.4 on its properties
 * (LOTRBlocks). It is not a SoulSandBlock, which would also raise bubble columns.
 *
 * <p>isFireSource on the top face (fire burns on it forever) is the
 * infiniburn block tags; the per-face random textures are the blockstate's
 * weighted model variants (LOTRModelProvider).
 */
public class LOTRWasteBlock extends Block {
    public static final MapCodec<LOTRWasteBlock> CODEC = simpleCodec(LOTRWasteBlock::new);

    private static final VoxelShape COLLISION = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public LOTRWasteBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<LOTRWasteBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return COLLISION;
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }
}
