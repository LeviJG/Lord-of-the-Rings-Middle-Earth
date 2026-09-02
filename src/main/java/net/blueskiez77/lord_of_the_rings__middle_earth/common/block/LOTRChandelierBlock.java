package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LOTRChandelierBlock extends Block {
    public static final MapCodec<LOTRChandelierBlock> CODEC = simpleCodec(
            props -> new LOTRChandelierBlock(LOTRGlowStyle.FLAME, props));

    private static final VoxelShape SHAPE = Block.box(1.0, 3.0, 1.0, 15.0, 16.0, 15.0);

    private final LOTRGlowStyle glow;

    public LOTRChandelierBlock(LOTRGlowStyle glow, Properties properties) {
        super(properties);
        this.glow = glow;
    }

    @Override
    protected MapCodec<? extends LOTRChandelierBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.above(), Direction.DOWN);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, RandomSource random) {
        if (!canSurvive(state, level, pos)) {
            tickAccess.scheduleTick(pos, this, 1);
        }
        return state;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // The four candle positions, straight out of randomDisplayTick. The
        // style applies its own vertical nudge on top of these.
        double near = 0.13;
        double far = 1.0 - near;
        double height = 0.6875;

        glow.spawn(level, random, pos.getX() + near, pos.getY() + height, pos.getZ() + near);
        glow.spawn(level, random, pos.getX() + far, pos.getY() + height, pos.getZ() + far);
        glow.spawn(level, random, pos.getX() + near, pos.getY() + height, pos.getZ() + far);
        glow.spawn(level, random, pos.getX() + far, pos.getY() + height, pos.getZ() + near);
    }
}