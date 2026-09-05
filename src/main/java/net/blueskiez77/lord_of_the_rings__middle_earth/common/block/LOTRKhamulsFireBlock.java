package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;

/**
 * LOTRBlockRhunFire: the fire a Khamûl's Fire jar leaves behind.
 *
 * <p>The original was a BlockFire that ate through stone. It did that by a
 * trick: before running vanilla's own fire update it looked at its six
 * neighbours, and for any that were rock, clay or a gate below blast resistance
 * 100 it TEMPORARILY handed them flammability 30/30 in Blocks.fire's global
 * table, ran the update, then put their real values back. It also burned itself
 * out on a 1-in-12 roll each tick.
 *
 * <p>DIVERGENCE, and the important note on this class. That trick cannot be
 * transcribed. 26.2's FireBlock keeps its flammability table in a private map
 * and its spread in private methods, so there is nothing to borrow and nothing
 * to lend to. This is BaseFireBlock -- the base vanilla builds soul fire on --
 * with the spread written here rather than inherited: it burns out on the same
 * 1-in-12 roll, and otherwise tries to set light to the blocks around it,
 * accepting the ones vanilla fire would AND the stone and clay it is supposed
 * to eat. The feel should match; the exact probabilities do not, because
 * vanilla's own numbers are not reachable.
 *
 * <p>NOT ported: isBannered, which let a faction banner smother it. The banner
 * protection system is not in the port.
 */
public class LOTRKhamulsFireBlock extends BaseFireBlock {
    public static final MapCodec<LOTRKhamulsFireBlock> CODEC =
            simpleCodec(LOTRKhamulsFireBlock::new);

    /** As vanilla fire: how far gone this patch is, 0 fresh through 15 spent. */
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;

    /** setLightLevel(1.0f) -- it burns at full brightness. */
    private static final int LIGHT = 15;

    /**
     * How often it acts. The original ran on BlockFire's tickRate of 30, which
     * made it creep; this is deliberately faster so it takes hold and burns
     * through in seconds rather than minutes.
     */
    private static final int TICK_RATE = 5;

    /**
     * And it is FINITE. Age climbs by one every tick and the patch is gone at
     * MAX_AGE, so the worst case is about MAX_AGE * TICK_RATE ticks -- a few
     * seconds. The original leaned on a 1-in-12 roll alone, which is a coin that
     * can keep coming up the same way; this puts a ceiling on it.
     */
    private static final int MAX_AGE = 15;

    public LOTRKhamulsFireBlock(Properties properties) {
        super(properties, 2.0f);
        registerDefaultState(stateDefinition.any().setValue(AGE, 0));
    }

    /** The properties the original set: no collision, instant break, full light. */
    public static Properties fireProperties() {
        return Properties.of()
                .noCollision()
                .instabreak()
                .lightLevel(state -> LIGHT)
                .noLootTable()
                .pushReaction(PushReaction.DESTROY)
                .replaceable();
    }

    @Override
    protected MapCodec<LOTRKhamulsFireBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    /**
     * canPlaceBlockAt: something solid beneath, or something beside it that will
     * take light. Stone counts here where it would not for vanilla fire, which
     * is the whole point of this block.
     */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)
                || canBurnAround(level, pos);
    }

    @Override
    protected boolean canBurn(BlockState state) {
        return true;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        level.scheduleTick(pos, this, TICK_RATE + level.getRandom().nextInt(TICK_RATE));
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.scheduleTick(pos, this, TICK_RATE + random.nextInt(TICK_RATE));
        // LOTRMod.doFireTick asked the doFireTick game rule, which 26.2 has
        // replaced with a radius: fire only spreads within
        // FIRE_SPREAD_RADIUS_AROUND_PLAYER blocks of a player, and zero turns
        // spreading off entirely. Zero is the closest thing to the old
        // "doFireTick false", so it is what stops this fire too.
        if (level.getGameRules().get(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER) <= 0) {
            return;
        }
        if (!canSurvive(state, level, pos)) {
            level.removeBlock(pos, false);
            return;
        }

        // "if (random.nextInt(12) == 0) setBlockToAir" -- it gutters out on its
        // own rather than burning for ever.
        if (random.nextInt(12) == 0) {
            level.removeBlock(pos, false);
            return;
        }

        // Age always climbs, and at the top the patch dies. This is the guarantee
        // that a fire ends; the roll above only decides whether it ends early.
        int age = state.getValue(AGE);
        if (age >= MAX_AGE) {
            level.removeBlock(pos, false);
            return;
        }
        level.setBlock(pos, state.setValue(AGE, age + 1), 4);

        // All six faces, on the original's own odds: LOTRBlockRhunFire handed
        // each burnable neighbour flammability 30, and vanilla rolls that
        // against 300 to the sides and 250 up and down.
        //
        // With TWO limits on top, because the original had neither and it
        // showed: a patch stops spreading once it is past halfway through its
        // life, and anything it does light starts at its own age plus a step.
        // Without those a fire is immortal as a colony even though each patch
        // dies -- every new patch was starting fresh at age 0 and lighting more,
        // so it walked until it ran out of stone. Now a wave is spent within a
        // few generations.
        if (age >= MAX_AGE / 2) {
            return;
        }
        int childAge = Math.min(MAX_AGE, age + GENERATION_STEP);
        for (Direction direction : Direction.values()) {
            BlockPos neighbour = pos.relative(direction);
            if (!burnsThrough(level.getBlockState(neighbour))) {
                continue;
            }
            int chance = direction.getAxis().isVertical() ? 250 : 300;
            if (random.nextInt(chance) < FLAMMABILITY) {
                level.setBlockAndUpdate(neighbour, defaultBlockState().setValue(AGE, childAge));
            }
        }
    }

    /** How much older each patch is than the one that lit it. */
    private static final int GENERATION_STEP = 3;

    /**
     * The flammability the original lent stone and clay while its fire update
     * ran: Blocks.fire.setFireInfo(block, 30, 30).
     */
    private static final int FLAMMABILITY = 30;

    private static boolean canBurnAround(LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (burnsThrough(level.getBlockState(pos.relative(direction)))) {
                return true;
            }
        }
        return false;
    }

    /**
     * What this fire will take hold in.
     *
     * <p>The original's test, exactly: Material.rock or Material.clay, and
     * nothing at blast resistance 100 or more -- which is what kept it out of
     * obsidian and bedrock. Plus whatever vanilla fire would burn anyway.
     *
     * <p>NOT dirt, grass, sand or gravel. Material.ground and Material.grass
     * were never on the original's list, and admitting them gives a fire that
     * eats the landscape and then spreads from every hole it makes.
     */
    private static boolean burnsThrough(BlockState state) {
        if (state.isAir() || state.getBlock() == Blocks.FIRE) {
            return false;
        }
        if (state.getBlock().getExplosionResistance() >= 100.0f) {
            return false;
        }
        return state.ignitedByLava()
                || state.is(net.minecraft.tags.BlockTags.BASE_STONE_OVERWORLD)
                || state.is(net.minecraft.tags.BlockTags.STONE_BRICKS)
                || state.is(net.minecraft.tags.BlockTags.TERRACOTTA)
                || state.is(Blocks.CLAY)
                || state.is(Blocks.STONE)
                || state.is(Blocks.COBBLESTONE);
    }

    /** BlockFire drops nothing and has no item. */
    @Override
    protected net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state, BlockGetter level,
            BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return net.minecraft.world.phys.shapes.Shapes.empty();
    }
}
