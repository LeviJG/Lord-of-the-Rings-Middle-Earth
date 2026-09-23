package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A rope, and the elven rope that shares the class.
 *
 * <p>LOTRBlockRope extends LOTRBlockLadder, so a rope IS a ladder -- climbable,
 * hung on the side of a block, broken when that block goes. What it adds:
 *
 * <ul>
 *   <li>It may also hang from another rope, so a column can dangle in mid-air
 *       below the one block that is actually attached to a wall. Hence the
 *       {@link #canSurvive} and {@link #updateShape} pair: a rope with a rope
 *       above it never checks the wall.</li>
 *   <li>Right-clicking with the same rope in hand extends the column, up if you
 *       are looking up and down if you are looking down.</li>
 *   <li>Right-clicking bare-handed retracts the whole column into your
 *       inventory -- but only if the rope can retract at all.</li>
 * </ul>
 *
 * <p>{@code canRetract} is the ONLY thing separating the two blocks in 1.7.10:
 * plain rope is {@code new LOTRBlockRope(false)}, and hithlainLadder is
 * LOTRBlockHithlainRope, which passes {@code true}, sets a light level of 6, and
 * hurts anything hostile to Lothlorien that climbs it.
 */
public class LOTRRopeBlock extends LadderBlock {
    // LadderBlock declares codec() as MapCodec<LadderBlock>, so the override
    // cannot narrow it.
    public static final MapCodec<LadderBlock> CODEC =
            simpleCodec(props -> new LOTRRopeBlock(false, props));

    // The outline. renderRope draws a 2x2 cord with a 4x4x4 knot on the top
    // length, but a 2-pixel cord is miserable to click, so the hitbox is the
    // knot's 4x4 footprint carried the whole way down instead. Same wall
    // convention as vanilla's ladder: facing=north hangs on the wall at HIGH z.
    private static final VoxelShape NORTH = Block.box(6, 0, 12, 10, 16, 16);
    private static final VoxelShape SOUTH = Block.box(6, 0, 0, 10, 16, 4);
    private static final VoxelShape WEST = Block.box(12, 0, 6, 16, 16, 10);
    private static final VoxelShape EAST = Block.box(0, 0, 6, 4, 16, 10);

    /** No rope above: this length gets the knot renderRope drew on its top. */
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    private final boolean canRetract;
    private final boolean elven;

    public LOTRRopeBlock(boolean canRetract, Properties properties) {
        this(canRetract, false, properties);
    }

    public LOTRRopeBlock(boolean canRetract, boolean elven, Properties properties) {
        super(properties);
        this.canRetract = canRetract;
        this.elven = elven;
        registerDefaultState(defaultBlockState().setValue(TOP, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TOP);
    }

    private BlockState withKnot(BlockState state, LevelReader level, BlockPos pos) {
        return state.setValue(TOP, !level.getBlockState(pos.above()).is(this));
    }

    @Override
    public MapCodec<LadderBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> Shapes.block();
        };
    }

    // --- hanging ---------------------------------------------------------

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(this) || super.canSurvive(state, level, pos);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
            BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState,
            RandomSource random) {
        // LOTRBlockRope.onNeighborBlockChange only ran the ladder's own check
        // when there was no rope above -- a hanging length holds itself up.
        if (level.getBlockState(pos.above()).is(this)) {
            return withKnot(state, level, pos);
        }
        BlockState updated = super.updateShape(state, level, ticks, pos, direction,
                neighbourPos, neighbourState, random);
        return updated.is(this) ? withKnot(updated, level, pos) : updated;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState placed = super.getStateForPlacement(context);
        if (placed != null) {
            return withKnot(placed, context.getLevel(), context.getClickedPos());
        }
        // Nothing to hang on, but there may be a rope above to hang FROM, in
        // which case the new rope copies its facing (onBlockPlaced did this).
        BlockState above = context.getLevel().getBlockState(context.getClickedPos().above());
        if (!above.is(this)) {
            return null;
        }
        return withKnot(defaultBlockState().setValue(FACING, above.getValue(FACING)),
                context.getLevel(), context.getClickedPos());
    }

    // --- extending and retracting ---------------------------------------

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, net.minecraft.world.InteractionHand hand, BlockHitResult hit) {
        // Looking level or up extends upward, looking down extends downward --
        // the original's `entityplayer.rotationPitch <= 0` test.
        Direction step = player.getXRot() <= 0.0F ? Direction.UP : Direction.DOWN;

        if (stack.is(asItem())) {
            return extend(state, level, pos, player, stack, step);
        }
        if (stack.isEmpty() && canRetract && !player.onClimbable()) {
            return retract(level, pos, player, step);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult extend(BlockState state, Level level, BlockPos pos, Player player,
            ItemStack stack, Direction step) {
        BlockPos end = pos;
        while (level.getBlockState(end).is(this)) {
            end = end.relative(step);
            if (level.isOutsideBuildHeight(end)) {
                return InteractionResult.PASS;
            }
        }
        if (!level.getBlockState(end).canBeReplaced() || !level.getFluidState(end).isEmpty()) {
            return InteractionResult.PASS;
        }
        BlockState placed = state.setValue(FACING, state.getValue(FACING));
        if (!placed.canSurvive(level, end) && !level.getBlockState(end.above()).is(this)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            level.setBlockAndUpdate(end, placed);
            level.playSound(null, end, getSoundType(placed).getPlaceSound(), SoundSource.BLOCKS,
                    (getSoundType(placed).getVolume() + 1.0F) / 2.0F,
                    getSoundType(placed).getPitch() * 0.8F);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private InteractionResult retract(Level level, BlockPos pos, Player player, Direction step) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        BlockPos at = pos;
        while (level.getBlockState(at).is(this)) {
            if (!player.getAbilities().instabuild) {
                Block.dropResources(level.getBlockState(at), level, at);
            }
            level.playSound(null, at, getSoundType(level.getBlockState(at)).getBreakSound(),
                    SoundSource.BLOCKS, 1.0F, 0.8F);
            level.removeBlock(at, false);
            at = at.relative(step);
            if (level.isOutsideBuildHeight(at)) {
                break;
            }
        }
        return InteractionResult.SUCCESS;
    }

    // --- the elven rope's bite -------------------------------------------

    /**
     * LOTRBlockHithlainRope: a climber hostile to Lothlorien takes a half heart
     * of magic damage every tick -- a player by their Lothlorien alignment
     * being negative, anything else by its NPC faction being a bad relation.
     * The port has no NPCs yet, and every other mob was UNALIGNED to
     * getNPCFaction, so only the player test can bite for now.
     */
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
            InsideBlockEffectApplier effects, boolean flag) {
        if (!elven || !(level instanceof ServerLevel server)
                || !(entity instanceof LivingEntity living) || !living.onClimbable()) {
            return;
        }
        boolean harm = entity instanceof Player player
                ? LOTRPlayerAlignments.getAlignment(player, LOTRFaction.LOTHLORIEN) < 0.0f
                : LOTRFaction.UNALIGNED.isBadRelation(LOTRFaction.LOTHLORIEN);
        if (harm) {
            entity.hurtServer(server, server.damageSources().magic(), 1.0f);
        }
    }
}
