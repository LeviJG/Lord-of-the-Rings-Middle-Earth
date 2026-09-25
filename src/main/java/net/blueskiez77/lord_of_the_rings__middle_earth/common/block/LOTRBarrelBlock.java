package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPoisonedDrinks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBarrelBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRBrewingRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockBarrel. The tap faces whoever placed it (metadata 2-5, {@link #FACING}
 * here), and clicking the tap face is how drinks go in and out:
 *
 * <ul>
 *   <li>an empty vessel draws one drink from a full barrel;</li>
 *   <li>a brewable drink is poured in -- into an empty barrel, or onto a
 *       barrel of the same drink at the same strength with room left;</li>
 *   <li>anything else, or any other face, opens the barrel.</li>
 * </ul>
 *
 * <p>A bottle of poison used on any face poisons the drink inside
 * (canPoisonBarrel / poisonBarrel) rather than opening it.
 *
 * <p>Its item can also be set afloat on water and ridden (LOTRBarrelBoatEntity).
 */
public class LOTRBarrelBlock extends BaseEntityBlock {
    public static final MapCodec<LOTRBarrelBlock> CODEC = simpleCodec(LOTRBarrelBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    /** setBlockBounds(0.125, 0, 0.125, 0.875, 0.8125, 0.875). */
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

    public LOTRBarrelBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /** onBlockPlacedBy: the tap towards the player. */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRBarrelBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> type) {
        return level.isClientSide() ? null
                : createTickerHelper(type, LOTRBlockEntities.BARREL, LOTRBarrelBlockEntity::serverTick);
    }

    /** onBlockPlacedBy: a renamed barrel keeps its name. */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
            ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (stack.has(DataComponents.CUSTOM_NAME) && level.getBlockEntity(pos) instanceof LOTRBarrelBlockEntity barrel) {
            barrel.setCustomName(stack.getHoverName());
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof LOTRBarrelBlockEntity barrel)) {
            return InteractionResult.PASS;
        }
        if (hitResult.getDirection() == state.getValue(FACING)) {
            ItemStack barrelDrink = barrel.getBrewedDrink();
            if (!barrelDrink.isEmpty() && LOTRVessel.isEmptyDrink(stack)) {
                if (!level.isClientSide()) {
                    ItemStack playerDrink = LOTRVessel.of(stack).fill(barrelDrink.copyWithCount(1));
                    stack.shrink(1);
                    give(player, hand, stack, playerDrink);
                    barrel.consumeMugRefill();
                    playFill(level, pos);
                }
                return InteractionResult.SUCCESS;
            }
            if (stack.getItem() instanceof LOTRDrinkItem drink && drink.isBrewable()) {
                boolean match = false;
                if (barrel.getBarrelMode() == LOTRBarrelBlockEntity.EMPTY) {
                    match = true;
                } else if (!barrelDrink.isEmpty() && barrelDrink.getCount() < LOTRBrewingRecipes.BARREL_CAPACITY) {
                    match = barrelDrink.is(stack.getItem())
                            && barrelDrink.getOrDefault(LOTRDataComponents.DRINK_STRENGTH, 0)
                            .equals(stack.getOrDefault(LOTRDataComponents.DRINK_STRENGTH, 0));
                }
                if (match) {
                    if (!level.isClientSide()) {
                        if (barrelDrink.isEmpty()) {
                            ItemStack fill = stack.copyWithCount(1);
                            fill.set(LOTRDataComponents.VESSEL, LOTRVessel.MUG);
                            barrel.setItem(LOTRBarrelBlockEntity.BARREL_SLOT, fill);
                        } else {
                            barrelDrink.grow(1);
                            barrel.setItem(LOTRBarrelBlockEntity.BARREL_SLOT, barrelDrink);
                        }
                        barrel.setBarrelMode(LOTRBarrelBlockEntity.FULL);
                        if (!player.hasInfiniteMaterials()) {
                            ItemStack emptyVessel = LOTRDrinkItem.vessel(stack).emptyStack();
                            stack.shrink(1);
                            give(player, hand, stack, emptyVessel);
                        }
                        playFill(level, pos);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (stack.is(LOTRMiscItems.BOTTLE_OF_POISON) && barrel.canPoisonBarrel()) {
            if (!level.isClientSide()) {
                barrel.poisonBarrel(player);
                if (!player.hasInfiniteMaterials()) {
                    player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                }
            }
            return InteractionResult.SUCCESS;
        }
        if (!level.isClientSide()) {
            player.openMenu(barrel);
        }
        return InteractionResult.SUCCESS;
    }

    /** The stack in hand runs out: it becomes the new item, otherwise it goes in the inventory. */
    private static void give(Player player, InteractionHand hand, ItemStack held, ItemStack given) {
        if (held.isEmpty()) {
            player.setItemInHand(hand, given);
        } else if (!player.getInventory().add(given)) {
            player.drop(given, false);
        }
    }

    private static void playFill(Level level, BlockPos pos) {
        level.playSound(null, pos, LOTRSounds.ITEM_MUG_FILL, SoundSource.BLOCKS, 0.5f,
                0.8f + level.getRandom().nextFloat() * 0.4f);
    }

    /** onBlockHarvested: creative players do not get the barrel back. */
    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (player.hasInfiniteMaterials() && level.getBlockEntity(pos) instanceof LOTRBarrelBlockEntity barrel) {
            barrel.markCreativeBroken();
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    /** getPickBlock: the barrel with whatever is in it. */
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        if (level.getBlockEntity(pos) instanceof LOTRBarrelBlockEntity barrel) {
            return barrel.getBarrelDrop();
        }
        return super.getCloneItemStack(level, pos, state, includeData);
    }
}
