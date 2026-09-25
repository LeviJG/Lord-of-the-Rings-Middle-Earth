package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockFallenLeaves: a scatter of one kind of leaf on the ground, or
 * floating on still water. One block per leaf type now; the original packed
 * several into each of its four blocks by metadata.
 *
 * <p>Two pixels tall and nothing to bump into. It needs a solid top below it
 * or a water source, and it cannot be put into a liquid. It drops nothing but
 * to shears (getItemDropped null, IShearable), and like any Material.vine block
 * other blocks may be placed over it, lava sets it alight and pistons break it.
 * The look -- a random scatter of the leaf's texture -- is the client's
 * LOTRFallenLeavesModel.
 */
public class LOTRFallenLeavesBlock extends Block {

    public static final MapCodec<LOTRFallenLeavesBlock> CODEC = simpleCodec(p -> new LOTRFallenLeavesBlock(Blocks.OAK_LEAVES, p));

    /** setBlockBounds(0, 0, 0, 1, 0.125, 1). */
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    private final Block leaves;

    public LOTRFallenLeavesBlock(Block leaves, BlockBehaviour.Properties properties) {
        super(properties);
        this.leaves = leaves;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    /** The leaf block these fell from: its texture, tint and name. */
    public Block leaves() {
        return this.leaves;
    }

    /** LOTRItemFallenLeaves.getItemStackDisplayName: "Fallen" and the leaf's own name. */
    public Component displayName() {
        return Component.translatable("block.lotr.fallen_leaves", this.leaves.getName());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /** canPlaceBlockAt: never into a liquid. */
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!context.getLevel().getFluidState(context.getClickedPos()).isEmpty()) {
            return null;
        }
        return super.getStateForPlacement(context);
    }

    /** canBlockStay: still water beneath, or a solid top. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        if (level.getFluidState(below).is(Fluids.WATER) && level.getFluidState(below).isSource()) {
            return true;
        }
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    /** onNeighborBlockChange: gone, with nothing dropped (getItemDropped was null). */
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess,
                                     BlockPos pos, Direction direction, BlockPos neighbourPos,
                                     BlockState neighbourState, RandomSource random) {
        return state.canSurvive(level, pos) ? state : Blocks.AIR.defaultBlockState();
    }
}
