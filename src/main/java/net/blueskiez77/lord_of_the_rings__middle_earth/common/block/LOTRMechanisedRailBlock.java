package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRBlockMechanisedRail: a rail with a mechanism in it that drives carts
 * along by itself, the way a powered rail does with redstone.
 *
 * <p>There are two of them, as in the original. The "on" rail
 * ({@code mechanised_rail}, mechanisedRailOn) runs until a redstone signal
 * stops it; the "off" rail ({@code mechanised_rail_off}, mechanisedRailOff)
 * waits for one, like a vanilla powered rail. POWERED is the original's
 * metadata bit 8 -- whether redstone reaches the rail, spread along a line of
 * the same rail exactly as vanilla's powered rail spreads it -- and
 * {@link #isPowerOn} turns that into whether the mechanism is running.
 * Clicking the rail swaps one for the other; sneak-clicking takes the
 * mechanism back out, leaving a plain rail.
 *
 * <p>Carts see a running mechanised rail as a powered powered rail and a
 * stopped one as an unpowered one -- LOTRReplacedMethods.Minecart, which the
 * original patched into EntityMinecart; here it is LOTRMinecartMixin.
 */
public class LOTRMechanisedRailBlock extends PoweredRailBlock {
    private final boolean defaultPower;

    public LOTRMechanisedRailBlock(boolean defaultPower, Properties properties) {
        super(properties);
        this.defaultPower = defaultPower;
    }

    /** isPowerOn: the on rail runs unless powered, the off rail only when powered. */
    public boolean isPowerOn(BlockState state) {
        return state.getValue(POWERED) != this.defaultPower;
    }

    /**
     * How a minecart sees this rail: a powered rail of the same shape, powered
     * exactly when the mechanism is running. Anything else passes through.
     */
    public static BlockState asPoweredRail(BlockState state) {
        if (!(state.getBlock() instanceof LOTRMechanisedRailBlock rail)) {
            return state;
        }
        return Blocks.POWERED_RAIL.defaultBlockState()
                .setValue(SHAPE, state.getValue(SHAPE))
                .setValue(POWERED, rail.isPowerOn(state))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    }

    /** onBlockActivated: holding a minecart places it; anything else flips the rail. */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof MinecartItem) {
            return InteractionResult.PASS;
        }
        return toggle(state, level, pos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        return toggle(state, level, pos);
    }

    private InteractionResult toggle(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            LOTRMechanisedRailBlock other = (LOTRMechanisedRailBlock) (this == LOTRBlocks.MECHANISED_RAIL_OFF
                    ? LOTRBlocks.MECHANISED_RAIL : LOTRBlocks.MECHANISED_RAIL_OFF);
            BlockState swapped = other.defaultBlockState()
                    .setValue(SHAPE, state.getValue(SHAPE))
                    .setValue(POWERED, state.getValue(POWERED))
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
            level.setBlock(pos, swapped, Block.UPDATE_ALL);
            // random.click, pitched by the power the swapped-in rail starts with.
            level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS,
                    0.3f, other.isPowerOn(swapped) ? 0.6f : 0.5f);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * LOTREventHandler's sneak-click, which came before onBlockActivated and so
     * worked with anything in hand but a minecart: the plain rail goes back in
     * its place and the mechanism pops out.
     */
    public static void init() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (hand != InteractionHand.MAIN_HAND || player.isSpectator() || !player.isShiftKeyDown()
                    || !(state.getBlock() instanceof LOTRMechanisedRailBlock)
                    || player.getMainHandItem().getItem() instanceof MinecartItem) {
                return InteractionResult.PASS;
            }
            if (!level.isClientSide()) {
                BlockState rail = Blocks.RAIL.defaultBlockState()
                        .setValue(RailBlock.SHAPE, state.getValue(SHAPE))
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
                level.setBlock(pos, rail, Block.UPDATE_ALL);
                SoundType sound = rail.getSoundType();
                level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS,
                        (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
                Block.popResource(level, pos, new ItemStack(LOTRItems.MECHANISM));
            }
            return InteractionResult.SUCCESS;
        });
    }

    /** getPickBlock: the mechanism. */
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(LOTRItems.MECHANISM);
    }

    /** randomDisplayTick: a running rail puffs smoke from somewhere along each side. */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!isPowerOn(state)) {
            return;
        }
        Vec3[] corners = corners(state.getValue(SHAPE));
        Vec3 base = new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        Vec3 edge1 = corners[0].lerp(corners[1], random.nextFloat()).add(base);
        Vec3 edge2 = corners[2].lerp(corners[3], random.nextFloat()).add(base);
        level.addParticle(ParticleTypes.SMOKE, edge1.x, edge1.y, edge1.z, 0.0, 0.1, 0.0);
        level.addParticle(ParticleTypes.SMOKE, edge2.x, edge2.y, edge2.z, 0.0, 0.1, 0.0);
    }

    /** The two edges of the rail, each as a pair of ends, rising with a slope. */
    private static Vec3[] corners(RailShape shape) {
        return switch (shape) {
            case NORTH_SOUTH -> new Vec3[] {
                    new Vec3(-0.4, 0.0, -0.5), new Vec3(-0.4, 0.0, 0.5),
                    new Vec3(0.4, 0.0, -0.5), new Vec3(0.4, 0.0, 0.5)};
            case EAST_WEST -> new Vec3[] {
                    new Vec3(-0.5, 0.0, -0.4), new Vec3(0.5, 0.0, -0.4),
                    new Vec3(-0.5, 0.0, 0.4), new Vec3(0.5, 0.0, 0.4)};
            case ASCENDING_EAST -> new Vec3[] {
                    new Vec3(-0.5, 0.0, -0.4), new Vec3(0.5, 1.0, -0.4),
                    new Vec3(-0.5, 0.0, 0.4), new Vec3(0.5, 1.0, 0.4)};
            case ASCENDING_WEST -> new Vec3[] {
                    new Vec3(-0.5, 1.0, -0.4), new Vec3(0.5, 0.0, -0.4),
                    new Vec3(-0.5, 1.0, 0.4), new Vec3(0.5, 0.0, 0.4)};
            case ASCENDING_NORTH -> new Vec3[] {
                    new Vec3(-0.4, 1.0, -0.5), new Vec3(-0.4, 0.0, 0.5),
                    new Vec3(0.4, 1.0, -0.5), new Vec3(0.4, 0.0, 0.5)};
            case ASCENDING_SOUTH -> new Vec3[] {
                    new Vec3(-0.4, 0.0, -0.5), new Vec3(-0.4, 1.0, 0.5),
                    new Vec3(0.4, 0.0, -0.5), new Vec3(0.4, 1.0, 0.5)};
            default -> new Vec3[] {Vec3.ZERO, Vec3.ZERO, Vec3.ZERO, Vec3.ZERO};
        };
    }
}
