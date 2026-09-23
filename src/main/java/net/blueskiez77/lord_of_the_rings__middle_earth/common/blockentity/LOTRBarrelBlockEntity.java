package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import java.util.Arrays;
import java.util.Comparator;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRBarrelMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRBrewingRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityBarrel: ten slots, three modes, one very slow timer.
 *
 * <pre>
 *   0-5  ingredients        6-8  water buckets        9  the brew
 * </pre>
 *
 * <p>EMPTY: whatever recipe the slots make is previewed in slot 9. Pressing
 * Start Brewing takes one of everything (buckets come back empty) and goes to
 * BREWING. Every {@value #BREW_TIME} ticks -- ten minutes -- the brew grows a
 * strength, up to potent, then the barrel is FULL. Stop Brewing ends it early
 * at the strength BEFORE the current one. A FULL barrel is poured from its tap
 * a drink at a time and empties itself when the last is gone.
 *
 * <p>Slot 9 holds up to sixteen drinks in one stack even though a drink stacks
 * to one; the count is the barrel's fill level, as the original's stackSize was.
 *
 * <p>NOT ported: poisoning the barrel -- the bottle of poison is not in the port.
 */
public class LOTRBarrelBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {
    public static final int SLOT_COUNT = 10;
    public static final int BARREL_SLOT = 9;
    public static final int EMPTY = 0;
    public static final int BREWING = 1;
    public static final int FULL = 2;
    /** brewTime. */
    public static final int BREW_TIME = 12000;
    /** brewAnimTime. */
    public static final int BREW_ANIM_TIME = 32;
    public static final int DATA_MODE = 0;
    public static final int DATA_BREWING_TIME = 1;
    public static final int DATA_COUNT = 2;

    private static final int[] BUCKET_SLOTS = {6, 7, 8};

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int barrelMode;
    private int brewingTime;
    private @Nullable Component customName;
    /** onBlockHarvested's meta bit 8: broken in creative, so no barrel drops. */
    private boolean creativeBroken;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_MODE -> barrelMode;
                case DATA_BREWING_TIME -> brewingTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_MODE -> barrelMode = value;
                case DATA_BREWING_TIME -> brewingTime = value;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public LOTRBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.BARREL, pos, state);
    }

    private static int strength(ItemStack stack) {
        return stack.getOrDefault(LOTRDataComponents.DRINK_STRENGTH, 0);
    }

    public int getBarrelMode() {
        return barrelMode;
    }

    public void setBarrelMode(int mode) {
        barrelMode = mode;
        setChanged();
    }

    public void markCreativeBroken() {
        creativeBroken = true;
    }

    // ---------------------------------------------------------------- brewing

    /** getBrewedDrink: a copy of the brew, only once it is ready. */
    public ItemStack getBrewedDrink() {
        ItemStack brew = items.get(BARREL_SLOT);
        return barrelMode == FULL && !brew.isEmpty() ? brew.copy() : ItemStack.EMPTY;
    }

    /** consumeMugRefill: one drink poured off; an empty barrel is EMPTY again. */
    public void consumeMugRefill() {
        ItemStack brew = items.get(BARREL_SLOT);
        if (barrelMode == FULL && !brew.isEmpty()) {
            brew.shrink(1);
            if (brew.isEmpty()) {
                items.set(BARREL_SLOT, ItemStack.EMPTY);
                barrelMode = EMPTY;
            }
            setChanged();
        }
    }

    /** updateBrewingRecipe: while EMPTY, slot 9 previews what the slots would make. */
    private void updateBrewingRecipe() {
        if (barrelMode == EMPTY) {
            items.set(BARREL_SLOT, LOTRBrewingRecipes.findMatchingRecipe(items));
        }
    }

    /** handleBrewingButtonPress. */
    public void handleBrewingButtonPress() {
        ItemStack brew = items.get(BARREL_SLOT);
        if (barrelMode == EMPTY && !brew.isEmpty()) {
            barrelMode = BREWING;
            for (int i = 0; i < BARREL_SLOT; ++i) {
                ItemStack stack = items.get(i);
                if (stack.isEmpty()) {
                    continue;
                }
                ItemStackTemplate remainder = stack.getItem().getCraftingRemainder();
                stack.shrink(1);
                if (stack.isEmpty()) {
                    items.set(i, remainder != null ? remainder.create() : ItemStack.EMPTY);
                }
            }
            setChanged();
        } else if (barrelMode == BREWING && !brew.isEmpty() && strength(brew) > 0) {
            barrelMode = FULL;
            brewingTime = 0;
            ItemStack stopped = brew.copy();
            stopped.set(LOTRDataComponents.DRINK_STRENGTH, strength(brew) - 1);
            items.set(BARREL_SLOT, stopped);
            setChanged();
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LOTRBarrelBlockEntity barrel) {
        boolean changed = false;
        ItemStack brew = barrel.items.get(BARREL_SLOT);
        if (barrel.barrelMode == BREWING) {
            if (!brew.isEmpty()) {
                ++barrel.brewingTime;
                if (barrel.brewingTime >= BREW_TIME) {
                    barrel.brewingTime = 0;
                    if (strength(brew) < 4) {
                        brew.set(LOTRDataComponents.DRINK_STRENGTH, strength(brew) + 1);
                        changed = true;
                    } else {
                        barrel.barrelMode = FULL;
                    }
                }
            } else {
                barrel.barrelMode = EMPTY;
            }
        } else {
            barrel.brewingTime = 0;
        }
        if (barrel.barrelMode == FULL && brew.isEmpty()) {
            barrel.barrelMode = EMPTY;
        }
        if (changed) {
            barrel.setChanged();
        }
    }

    /** getInvSubtitle, for the screen and the barrel item's tooltip. */
    public static Component subtitle(int mode, ItemStack brew) {
        if (mode == EMPTY) {
            return Component.translatable("container.lotr.barrel.empty");
        }
        if (brew.isEmpty()) {
            return Component.empty();
        }
        Component strength = LOTRDrinkItem.strengthName(brew);
        if (mode == BREWING) {
            return Component.translatable("container.lotr.barrel.brewing", brew.getHoverName(), strength);
        }
        return Component.translatable("container.lotr.barrel.full", brew.getHoverName(), strength, brew.getCount());
    }

    public Component subtitle() {
        return subtitle(barrelMode, items.get(BARREL_SLOT));
    }

    // ------------------------------------------------------------- the item

    /** getBarrelDrop: a barrel that is doing anything keeps its contents. */
    public ItemStack getBarrelDrop() {
        ItemStack stack = new ItemStack(getBlockState().getBlock());
        if (barrelMode != EMPTY && level != null) {
            stack.set(LOTRDataComponents.BARREL_DATA, CustomData.of(toTag(level.registryAccess())));
            // getItemStackLimit: one, while it holds something.
            stack.set(DataComponents.MAX_STACK_SIZE, 1);
        }
        return stack;
    }

    public CompoundTag toTag(HolderLookup.Provider registries) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        writeBarrel(output);
        return output.buildResult();
    }

    public void loadFromTag(CompoundTag tag, HolderLookup.Provider registries) {
        readBarrel(TagValueInput.create(ProblemReporter.DISCARDING, registries, tag));
    }

    /**
     * breakBlock: the ingredients and buckets always spill, and the barrel
     * itself drops -- with the brew inside it -- unless a creative player broke it.
     */
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (level == null) {
            return;
        }
        ItemStack brew = items.get(BARREL_SLOT);
        items.set(BARREL_SLOT, ItemStack.EMPTY);
        for (int i = 0; i < BARREL_SLOT; ++i) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), items.get(i));
            items.set(i, ItemStack.EMPTY);
        }
        items.set(BARREL_SLOT, brew);
        if (!creativeBroken) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), getBarrelDrop());
        }
    }

    // ------------------------------------------------------------ persistence

    public void writeBarrel(ValueOutput output) {
        ContainerHelper.saveAllItems(output, items);
        output.putByte("BarrelMode", (byte) barrelMode);
        output.putInt("BrewingTime", brewingTime);
        if (customName != null) {
            output.store("CustomName", ComponentSerialization.CODEC, customName);
        }
    }

    public void readBarrel(ValueInput input) {
        items.clear();
        ContainerHelper.loadAllItems(input, items);
        barrelMode = input.getByteOr("BarrelMode", (byte) 0);
        brewingTime = input.getIntOr("BrewingTime", 0);
        customName = parseCustomNameSafe(input, "CustomName");
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        readBarrel(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        writeBarrel(output);
    }

    // -------------------------------------------------------------- Container

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = items.get(slot);
        if (stack.isEmpty() || amount <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack split = stack.split(amount);
        if (slot != BARREL_SLOT) {
            updateBrewingRecipe();
        }
        setChanged();
        return split;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = items.get(slot);
        items.set(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        if (slot != BARREL_SLOT) {
            updateBrewingRecipe();
        }
        setChanged();
    }

    /** isItemValidForSlot. */
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < 6) {
            return true;
        }
        if (slot >= 6 && slot < 9) {
            return LOTRBrewingRecipes.isWaterSource(stack);
        }
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    /**
     * getAccessibleSlotsFromSide: buckets from below and the sides, ingredients
     * from above -- emptiest slot first, so a hopper spreads its load.
     */
    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            Integer[] sorted = {0, 1, 2, 3, 4, 5};
            Arrays.sort(sorted, Comparator.comparingInt((Integer slot) -> items.get(slot).getCount()));
            int[] result = new int[sorted.length];
            for (int i = 0; i < sorted.length; ++i) {
                result[i] = sorted[i];
            }
            return result;
        }
        return BUCKET_SLOTS.clone();
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return canPlaceItem(slot, stack);
    }

    /** canExtractItem: only the emptied buckets. */
    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return slot >= 6 && slot < 9 && !canPlaceItem(slot, stack);
    }

    // ----------------------------------------------------------- MenuProvider

    public void setCustomName(Component name) {
        customName = name;
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return customName != null ? customName : Component.translatable("container.lotr.barrel");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new LOTRBarrelMenu(containerId, inventory, this, dataAccess);
    }
}
