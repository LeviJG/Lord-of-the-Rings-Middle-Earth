package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTREntJarBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockEntJar. A clay jar that stands on the forest floor of Fangorn and
 * holds up to six draughts of liquid.
 *
 * <p>In 1.7.10 the jar was the only way to brew an Ent-draught: fill it with
 * water, throw in a Fangorn herb while standing in Fangorn, and the whole jar
 * turns to that draught. See the class comment on
 * {@link LOTREntJarBlockEntity} for the parts of that cycle the port cannot
 * reach yet -- the Ent-draught item, the Fangorn herbs and the Fangorn biome
 * are all unported, so what remains here is the water half: rain fills it, and
 * buckets fill and empty it.
 */
public class LOTREntJarBlock extends Block implements EntityBlock {

    public static final MapCodec<LOTREntJarBlock> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    propertiesCodec()
            ).apply(instance, LOTREntJarBlock::new));

    /** setBlockBounds(0.25, 0.0, 0.25, 0.75, 0.875, 0.75) in sixteenths. */
    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 14.0, 12.0);

    public LOTREntJarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTREntJarBlockEntity(pos, state);
    }

    /**
     * The rain check is server-side work, so unlike the ithildin door this one
     * ticks on the server only.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide() || type != LOTRBlockEntities.ENT_JAR) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<LOTREntJarBlockEntity>)
                LOTREntJarBlockEntity::serverTick;
    }

    // ------------------------------------------------------------------ shape

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // ------------------------------------------------------------- attachment

    /** LOTRBlockEntJar.canBlockStay: it needs something solid to stand on. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    /**
     * onNeighborBlockChange dropped the jar as an item and cleared the block
     * when its support went. Returning AIR here is the modern spelling: vanilla
     * routes that through Block.updateOrDestroy, which drops the block's loot.
     */
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, RandomSource random) {
        if (direction == Direction.DOWN && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    // ------------------------------------------------------------ interaction

    /**
     * LOTRBlockEntJar.onBlockActivated, restricted to the branches whose items
     * exist in the port.
     *
     * <p>What is here: a water bucket fills the jar to the brim, and an empty
     * bucket drains a full jar back into a bucket. The original also accepted
     * every {@code LOTRItemMug.Vessel} -- wooden mug, skin, goblet, ceramic
     * mug -- one draught at a time, and those are the same two branches with a
     * different capacity, so they slot straight in when mugs are ported.
     *
     * <p>What is NOT here, and why: filling the jar from a bowl of Ent-draught,
     * pouring a bowl back out of it, and brewing a draught by throwing a
     * Fangorn herb into a water-filled jar. All three need
     * {@code LOTRItemEntDraught}, which does not exist in the port; the brewing
     * branch additionally tested {@code biome instanceof LOTRBiomeGenFangorn}
     * and there are no biomes yet either.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof LOTREntJarBlockEntity jar)) {
            return InteractionResult.PASS;
        }

        // Draining. Only water can be taken back out: a jar holding a draught
        // needs a bowl, and bowls are part of the unported half.
        //
        // DELIBERATE BUGFIX, not a porting slip. The original gated this on
        // drinkAmount > 0 and then called consume() six times, clamping at
        // zero -- so a jar holding a single unit still handed back a FULL
        // bucket, duplicating water out of nothing. Requiring the jar to
        // actually be full is the fix; do not "restore" the > 0 test.
        if (stack.is(Items.BUCKET) && jar.holdsWater()
                && jar.getDrinkAmount() >= LOTREntJarBlockEntity.MAX_CAPACITY) {
            if (!level.isClientSide()) {
                for (int i = 0; i < LOTREntJarBlockEntity.MAX_CAPACITY; i++) {
                    jar.consume();
                }
                giveOrDrop(player, stack, new ItemStack(Items.WATER_BUCKET));
            }
            playFill(level, pos, player);
            return InteractionResult.SUCCESS;
        }

        // Filling.
        if (stack.is(Items.WATER_BUCKET) && jar.holdsWater()
                && jar.getDrinkAmount() < LOTREntJarBlockEntity.MAX_CAPACITY) {
            if (!level.isClientSide()) {
                for (int i = 0; i < LOTREntJarBlockEntity.MAX_CAPACITY; i++) {
                    jar.fillWithWater();
                }
                giveOrDrop(player, stack, new ItemStack(Items.BUCKET));
            }
            playFill(level, pos, player);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    /**
     * Consumes one of the held stack and gives back the swapped container,
     * which is what the original's inventory juggling amounted to. Creative
     * players keep their bucket and get nothing back.
     */
    private static void giveOrDrop(Player player, ItemStack held, ItemStack result) {
        if (player.getAbilities().instabuild) {
            return;
        }
        held.shrink(1);
        if (held.isEmpty()) {
            player.setItemInHand(player.getUsedItemHand(), result);
        } else if (!player.getInventory().add(result)) {
            player.drop(result, false);
        }
    }

    private static void playFill(Level level, BlockPos pos, Player player) {
        // "lotr:item.mug_fill" in the original. The port has no mug sounds yet,
        // so the vanilla bottle fill stands in -- it is the same gesture.
        level.playSound(player, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS,
                0.5f, 0.8f + level.getRandom().nextFloat() * 0.4f);
    }

    // -------------------------------------------------------------- particles

    /**
     * LOTRBlockEntJar.randomDisplayTick: a jar holding a DRAUGHT (not plain
     * water) puffs happy-villager particles from its mouth. Nothing can set a
     * draught in the port yet, so this stays quiet until Ent-draughts land.
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(4) != 0) {
            return;
        }
        if (level.getBlockEntity(pos) instanceof LOTREntJarBlockEntity jar && !jar.holdsWater()) {
            level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.25 + random.nextFloat() * 0.5,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.25 + random.nextFloat() * 0.5,
                    0.0, 0.2, 0.0);
        }
    }
}
