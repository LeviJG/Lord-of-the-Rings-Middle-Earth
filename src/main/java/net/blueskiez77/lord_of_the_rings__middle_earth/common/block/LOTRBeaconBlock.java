package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBeaconBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMatchItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jspecify.annotations.Nullable;

// Beacon of Gondor: a pile of firewood on a hilltop that lights its neighbours.
//
// LOTRBlockBeacon behaviour, ported:
//   * lit by flint and steel, a match, or any torch item held in hand
//   * lit by any burning entity that walks into it
//   * quenched by a water bucket, or by water flowing in above it
//   * emits light 15 only once FULLY lit, a hundred ticks after ignition
//   * needs a solid-topped block beneath, and pops off if that goes away
//   * 13/16 of a block tall
//
// LIT lives on the blockstate rather than only in the block entity, because
// light emission is read off the state. FULLY_LIT is the second property: the
// original's getLightValue consulted isFullyLit(), and a blockstate-driven
// light level cannot ask a block entity, so the hundred-tick warmup flips this
// second flag and that is what carries the light.
//
// NO MENU. LOTRGuiBeacon was fellowship assignment plus naming, and the port
// has no fellowship system, so there is nothing for GUI 50 to show yet.
public class LOTRBeaconBlock extends BaseEntityBlock {

    public static final MapCodec<LOTRBeaconBlock> CODEC = simpleCodec(LOTRBeaconBlock::new);

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    // isFullyLit(): lit AND a hundred ticks of warmup done. The original's
    // getLightValue asked the tile entity for this; a blockstate-driven light
    // level cannot, so the warmup flips a second property and the light level
    // registered in LOTRBlocks reads THIS one, not LIT.
    public static final BooleanProperty FULLY_LIT = BooleanProperty.create("fully_lit");

    // 4px cobblestone base + three courses of logs at 3px each = 13, which is
    // exactly the original's setBlockBounds(0, 0, 0, 1, 0.8125, 1).
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0);

    public LOTRBeaconBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
                .setValue(LIT, false)
                .setValue(FULLY_LIT, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, FULLY_LIT);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRBeaconBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // ------------------------------------------------------------- placement

    // canBlockStay: the block below must present a solid top face.
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
                                   @Nullable Orientation orientation, boolean movedByPiston) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }
        // Water above quenches it, as in onNeighborBlockChange.
        if (state.getValue(LIT) && isWaterAbove(level, pos)) {
            quench(level, pos, state);
        }
    }

    private boolean isWaterAbove(Level level, BlockPos pos) {
        return level.getFluidState(pos.above()).is(Fluids.WATER)
                || level.getFluidState(pos.above()).is(Fluids.FLOWING_WATER);
    }

    // -------------------------------------------------------------- lighting

    /** Public so the client-side GUI hook can defer to it. */
    public boolean canItemLightBeacon(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.is(Items.FLINT_AND_STEEL) || stack.getItem() instanceof LOTRMatchItem) {
            return true;
        }
        return stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof TorchBlock;
    }

    private void ignite(Level level, BlockPos pos, BlockState state) {
        // playSoundEffect ran on both sides in 1.7.10; playSound(null, ...)
        // is its equivalent and reaches nearby players.
        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS,
                1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
        if (!level.isClientSide()
                && level.getBlockEntity(pos) instanceof LOTRBeaconBlockEntity beacon) {
            beacon.setLit(level, true);
        }
    }

    private void quench(Level level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F,
                2.6F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8F);
        if (!level.isClientSide()
                && level.getBlockEntity(pos) instanceof LOTRBeaconBlockEntity beacon) {
            beacon.setLit(level, false);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean lit = state.getValue(LIT);

        if (canItemLightBeacon(stack) && !lit && !isWaterAbove(level, pos)) {
            ignite(level, pos, state);
            if (!player.isCreative()) {
                if (stack.isDamageableItem()) {
                    stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
                } else if (stack.getMaxStackSize() > 1) {
                    // The original only decremented stackable items: a torch is
                    // consumed, a one-off tool is not.
                    stack.shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (stack.is(Items.WATER_BUCKET) && lit) {
            quench(level, pos, state);
            if (!player.isCreative()) {
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    /**
     * Everything else opens the naming dialog (openGui 50). The screen itself
     * is opened client-side; the server side is LOTRCommonProxy's half,
     * addEditingPlayer, without which LOTRPacketBeaconEdit is refused.
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof LOTRBeaconBlockEntity beacon) {
            beacon.addEditingPlayer(player);
        }
        return InteractionResult.SUCCESS;
    }

    // onEntityCollidedWithBlock: anything on fire lights it.
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
                                InsideBlockEffectApplier applier, boolean flag) {
        // onEntityCollidedWithBlock played fire.ignite as well -- ignite()
        // carries the sound, and the original played it on BOTH sides, so the
        // client-side call is what makes it audible without a packet.
        if (entity.isOnFire() && !state.getValue(LIT) && !isWaterAbove(level, pos)) {
            ignite(level, pos, state);
        }
    }

    // --------------------------------------------------------------- effects

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        // LOTRBlockBeacon.randomDisplayTick: fire.fire on 1 tick in 24, then
        // three largesmoke puffs from the top half of the block.
        if (random.nextInt(24) == 0) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS,
                    1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
        }
        for (int i = 0; i < 3; ++i) {
            double x = pos.getX() + random.nextFloat();
            double y = pos.getY() + random.nextFloat() * 0.5 + 0.5;
            double z = pos.getZ() + random.nextFloat();
            level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.0, 0.0, 0.0);
        }

        // The smoke column itself lives in LOTRBeaconBlockEntity.clientTick,
        // not here -- animateTick only fires for randomly chosen nearby blocks,
        // which is why it can never produce a steady stream.
        if (!state.getValue(FULLY_LIT)) {
            return;
        }

        // Sparks spitting off the logs, CampfireBlock.animateTick.
        if (random.nextInt(5) == 0) {
            for (int i = 0; i < random.nextInt(1) + 1; ++i) {
                level.addParticle(ParticleTypes.LAVA,
                        pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5,
                        random.nextFloat() / 2.0F, 5.0E-5, random.nextFloat() / 2.0F);
            }
        }

    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                            BlockEntityType<T> type) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(type, LOTRBlockEntities.BEACON,
                    (innerLevel, pos, blockState, entity) ->
                            LOTRBeaconBlockEntity.serverTick(serverLevel, pos, blockState, entity));
        }
        // Client ticker too, for the smoke column -- see clientTick.
        return createTickerHelper(type, LOTRBlockEntities.BEACON, LOTRBeaconBlockEntity::clientTick);
    }
}