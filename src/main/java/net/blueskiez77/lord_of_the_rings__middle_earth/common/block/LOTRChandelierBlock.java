package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
            props -> new LOTRChandelierBlock(ParticleStyle.FLAME, props));

    private static final VoxelShape SHAPE = Block.box(1.0, 3.0, 1.0, 15.0, 16.0, 15.0);

    public enum ParticleStyle {
        FLAME(ParticleTypes.SMOKE, ParticleTypes.FLAME),

        MALLORN_SILVER(ParticleTypes.END_ROD, null),

        MALLORN_BLUE(ParticleTypes.SOUL_FIRE_FLAME, null),

        MALLORN_GOLD(ParticleTypes.ELECTRIC_SPARK, null),

        MALLORN_GREEN(ParticleTypes.HAPPY_VILLAGER, null),

        WOOD_ELVEN(ParticleTypes.CHERRY_LEAVES, null),

        HIGH_ELVEN(ParticleTypes.END_ROD, null),

        MORGUL(ParticleTypes.PORTAL, null);

        private final ParticleOptions primary;
        private final ParticleOptions secondary;

        ParticleStyle(ParticleOptions primary, ParticleOptions secondary) {
            this.primary = primary;
            this.secondary = secondary;
        }
    }

    private final ParticleStyle particleStyle;

    public LOTRChandelierBlock(ParticleStyle particleStyle, Properties properties) {
        super(properties);
        this.particleStyle = particleStyle;
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
        double near = 0.13;
        double far = 1.0 - near;
        double height = 0.6875;

        spawn(level, pos.getX() + near, pos.getY() + height, pos.getZ() + near);
        spawn(level, pos.getX() + far, pos.getY() + height, pos.getZ() + far);
        spawn(level, pos.getX() + near, pos.getY() + height, pos.getZ() + far);
        spawn(level, pos.getX() + far, pos.getY() + height, pos.getZ() + near);
    }

    private void spawn(Level level, double x, double y, double z) {
        level.addParticle(particleStyle.primary, x, y, z, 0.0, 0.0, 0.0);
        if (particleStyle.secondary != null) {
            level.addParticle(particleStyle.secondary, x, y, z, 0.0, 0.0, 0.0);
        }
    }
}