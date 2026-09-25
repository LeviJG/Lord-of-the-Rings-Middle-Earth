package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRBlockStalactite: a spike of stone, packed ice or obsidian, hanging from
 * a ceiling (metadata 0, the stalactite) or standing on a floor (metadata 1, the
 * stalagmite) -- one block each now. It takes its hardness, resistance, sound
 * and texture from that block.
 *
 * <p>It needs a solid face to hang from or stand on, both to be placed
 * (canReplace) and to stay (canBlockStay); losing it breaks the block with its
 * drop. Landing on a stalagmite hurts: {@code fallDistance * 2 + 1}, in place
 * of the ordinary fall damage. A stalactite under rock drips now and then.
 */
public class LOTRStalactiteBlock extends Block {

    public static final MapCodec<LOTRStalactiteBlock> CODEC = simpleCodec(p -> new LOTRStalactiteBlock(p, true));

    /** setBlockBounds(0.25, 0, 0.25, 0.75, 1, 0.75). */
    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    /** True for a stalactite, hanging from the block above. */
    private final boolean hanging;

    public LOTRStalactiteBlock(BlockBehaviour.Properties properties, boolean hanging) {
        super(properties);
        this.hanging = hanging;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public boolean isHanging() {
        return this.hanging;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /** canBlockStay / canReplace: the face it grows from must be solid. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction toSupport = this.hanging ? Direction.UP : Direction.DOWN;
        BlockPos support = pos.relative(toSupport);
        return level.getBlockState(support).isFaceSturdy(level, support, toSupport.getOpposite());
    }

    /**
     * onNeighborBlockChange: dropBlockAsItem and setBlockToAir. A shape update
     * cannot drop anything, so it schedules a tick that breaks the block.
     */
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess,
                                     BlockPos pos, Direction direction, BlockPos neighbourPos,
                                     BlockState neighbourState, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            tickAccess.scheduleTick(pos, this, 1);
        }
        return state;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    /** onFallenUpon: only a stalagmite hurts, and it replaces the fall damage. */
    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        if (!this.hanging && entity instanceof LivingEntity living && level instanceof ServerLevel server) {
            int damage = (int) (fallDistance * 2.0) + 1;
            living.hurtServer(server, level.damageSources().fall(), damage);
        }
    }

    /**
     * randomDisplayTick: one time in fifty, water drips from a stalactite
     * hanging under an opaque block of rock (Material.rock, here the blocks a
     * pickaxe mines).
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!this.hanging || random.nextInt(50) != 0) {
            return;
        }
        BlockState above = level.getBlockState(pos.above());
        if (above.isSolidRender() && above.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            level.addParticle(ParticleTypes.DRIPPING_WATER,
                    pos.getX() + 0.6, pos.getY(), pos.getZ() + 0.6, 0.0, 0.0, 0.0);
        }
    }
}
