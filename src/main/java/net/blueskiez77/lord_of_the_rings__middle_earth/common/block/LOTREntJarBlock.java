package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTREntJarBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTREntDraughtItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LevelEvent;
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
 * holds up to six draughts of liquid: rain, buckets and vessels fill it with
 * water, a Fangorn herb thrown in while in Fangorn brews the whole jar into
 * that Ent-draught, and bowls draw the draught out one at a time.
 */
public class LOTREntJarBlock extends Block implements EntityBlock {
    /** Where a herb brews: LOTRBiomeGenFangorn. Empty until the biome is ported. */
    public static final TagKey<Biome> IS_FANGORN = TagKey.create(Registries.BIOME,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "is_fangorn"));

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
     * LOTREntJarRecipes: the six Fangorn herbs and the riverweed, each brewing
     * the Ent-draught of the same index. LOTRBlockFangornPlant's metadata ran
     * green, brown, gold, yellow, red, silver -- the draughts' own order.
     */
    private static int draughtFor(ItemStack stack) {
        if (stack.is(LOTRDecorationBlocks.FANGORN_PLANT_GREEN.asItem())) {
            return 0;
        }
        if (stack.is(LOTRDecorationBlocks.FANGORN_PLANT_BROWN.asItem())) {
            return 1;
        }
        if (stack.is(LOTRDecorationBlocks.FANGORN_PLANT_GOLD.asItem())) {
            return 2;
        }
        if (stack.is(LOTRDecorationBlocks.FANGORN_PLANT_YELLOW.asItem())) {
            return 3;
        }
        if (stack.is(LOTRDecorationBlocks.FANGORN_PLANT_RED.asItem())) {
            return 4;
        }
        if (stack.is(LOTRDecorationBlocks.FANGORN_PLANT_SILVER.asItem())) {
            return 5;
        }
        if (stack.is(LOTRDecorationBlocks.FANGORN_RIVERWEED.asItem())) {
            return 6;
        }
        return -1;
    }

    /**
     * LOTRBlockEntJar.onBlockActivated, branch for branch.
     *
     * <p>The Fangorn test was {@code biome instanceof LOTRBiomeGenFangorn}. The
     * port has no Fangorn biome yet, so it asks the {@code lotr:is_fangorn}
     * biome tag instead, which ships empty: brewing switches on when the biome
     * is added to it.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof LOTREntJarBlockEntity jar)) {
            return InteractionResult.PASS;
        }

        // A bowl of draught poured in.
        if (stack.getItem() instanceof LOTREntDraughtItem
                && jar.fillFromBowl(LOTREntDraughtItem.index(stack))) {
            if (!player.getAbilities().instabuild) {
                player.setItemInHand(hand, new ItemStack(Items.BOWL));
            }
            playFill(level, pos, player);
            return InteractionResult.SUCCESS;
        }

        // A jar of draught only gives it up to a bowl.
        if (!jar.holdsWater()) {
            if (stack.is(Items.BOWL)) {
                ItemStack draught = LOTREntDraughtItem.stack(LOTRFoodItems.ENT_DRAUGHT, jar.getDrinkMeta());
                if (!level.isClientSide()) {
                    jar.consume();
                }
                giveOrDrop(player, hand, stack, draught);
                playFill(level, pos, player);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        if (stack.isEmpty()) {
            return InteractionResult.PASS;
        }

        // Brewing: a herb thrown into water, in Fangorn, turns the whole jar.
        int draught = draughtFor(stack);
        if (draught >= 0 && jar.getDrinkAmount() > 0 && level.getBiome(pos).is(IS_FANGORN)) {
            if (!level.isClientSide()) {
                jar.brew(draught);
                level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 0);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if (jar.getDrinkAmount() > 0) {
            // DELIBERATE BUGFIX, not a porting slip. The original gated this on
            // drinkAmount > 0 and then called consume() six times, clamping at
            // zero -- so a jar holding a single unit still handed back a FULL
            // bucket, duplicating water out of nothing. Requiring the jar to
            // actually be full is the fix; do not "restore" the > 0 test.
            if (stack.is(Items.BUCKET) && jar.getDrinkAmount() >= LOTREntJarBlockEntity.MAX_CAPACITY) {
                if (!level.isClientSide()) {
                    for (int i = 0; i < LOTREntJarBlockEntity.MAX_CAPACITY; i++) {
                        jar.consume();
                    }
                }
                giveOrDrop(player, hand, stack, new ItemStack(Items.WATER_BUCKET));
                playFill(level, pos, player);
                return InteractionResult.SUCCESS;
            }
            // Every empty vessel draws one unit.
            LOTRVessel vessel = LOTRVessel.of(stack);
            if (vessel != null && LOTRVessel.isEmptyDrink(stack)) {
                if (!level.isClientSide()) {
                    jar.consume();
                }
                giveOrDrop(player, hand, stack, vessel.fill(new ItemStack(LOTRFoodItems.WATER)));
                playFill(level, pos, player);
                return InteractionResult.SUCCESS;
            }
        }

        if (jar.getDrinkAmount() < LOTREntJarBlockEntity.MAX_CAPACITY) {
            if (stack.is(Items.WATER_BUCKET)) {
                if (!level.isClientSide()) {
                    for (int i = 0; i < LOTREntJarBlockEntity.MAX_CAPACITY; i++) {
                        jar.fillWithWater();
                    }
                }
                replaceHeld(player, hand, new ItemStack(Items.BUCKET));
                playFill(level, pos, player);
                return InteractionResult.SUCCESS;
            }
            // Every vessel of water pours one unit in.
            LOTRVessel vessel = LOTRVessel.of(stack);
            if (vessel != null && LOTRVessel.isFullDrink(stack)
                    && LOTRVessel.equivalentDrink(stack).is(LOTRFoodItems.WATER)) {
                if (!level.isClientSide()) {
                    jar.fillWithWater();
                }
                replaceHeld(player, hand, vessel.emptyStack());
                playFill(level, pos, player);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    /**
     * tryTakeWaterFromJar: one of the held stack is used up and the filled
     * container goes to the hand if that emptied it, otherwise to the
     * inventory, otherwise onto the ground. Creative players keep what they
     * hold and get nothing.
     */
    private static void giveOrDrop(Player player, InteractionHand hand, ItemStack held, ItemStack result) {
        if (player.getAbilities().instabuild) {
            return;
        }
        held.shrink(1);
        if (held.isEmpty()) {
            player.setItemInHand(hand, result);
        } else if (!player.getInventory().add(result)) {
            player.drop(result, false);
        }
    }

    /** tryAddWaterToJar: the whole held slot becomes the empty container. */
    private static void replaceHeld(Player player, InteractionHand hand, ItemStack container) {
        if (!player.getAbilities().instabuild) {
            player.setItemInHand(hand, container);
        }
    }

    private static void playFill(Level level, BlockPos pos, Player player) {
        level.playSound(player, pos, LOTRSounds.ITEM_MUG_FILL, SoundSource.BLOCKS,
                0.5f, 0.8f + level.getRandom().nextFloat() * 0.4f);
    }

    // -------------------------------------------------------------- particles

    /**
     * LOTRBlockEntJar.randomDisplayTick: a jar holding a DRAUGHT (not plain
     * water) puffs happy-villager particles from its mouth.
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
