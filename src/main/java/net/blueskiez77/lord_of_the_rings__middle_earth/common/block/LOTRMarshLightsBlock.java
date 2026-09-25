package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRBlockMarshLights: an invisible block sitting on the water of the Dead
 * Marshes, giving off the corpse-lights -- two times in three a tick-chance of
 * a particle half a block down, a marsh flame one time in three and a pale
 * light otherwise. Nothing to see, touch or target; it drops nothing, and
 * without water beneath it is gone.
 */
public class LOTRMarshLightsBlock extends Block {

    public static final MapCodec<LOTRMarshLightsBlock> CODEC = simpleCodec(LOTRMarshLightsBlock::new);

    public LOTRMarshLightsBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    /** getRenderType -1: not drawn. */
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    /** isCollidable false: not even an outline. */
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    /** canBlockStay: water beneath. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getFluidState(pos.below()).is(FluidTags.WATER);
    }

    /** onNeighborBlockChange: setBlock(air, 0, 2), without a drop. */
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess,
                                     BlockPos pos, Direction direction, BlockPos neighbourPos,
                                     BlockState neighbourState, RandomSource random) {
        return state.canSurvive(level, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) > 0) {
            double x = pos.getX() + random.nextFloat();
            double y = pos.getY() - 0.5;
            double z = pos.getZ() + random.nextFloat();
            double rise = 0.05f + random.nextFloat() * 0.1f;
            level.addParticle(random.nextInt(3) == 0 ? LOTRParticles.MARSH_FLAME : LOTRParticles.MARSH_LIGHT,
                    x, y, z, 0.0, rise, 0.0);
        }
    }
}
