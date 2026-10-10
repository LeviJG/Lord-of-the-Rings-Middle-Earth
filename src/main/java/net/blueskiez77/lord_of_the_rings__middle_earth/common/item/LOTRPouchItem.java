package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRPouchMenu;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

/**
 * LOTRItemPouch: a pouch of one, two or three rows of nine slots, its own little chest -- opened from
 * the hand, or against a chest to move goods between the two. Picked-up goods go first into a pouch
 * already holding the like; dyed, and, at a faction's table, given that faction's colour. Its goods
 * are the vanilla container contents; its colour the vanilla dyed colour (the original's own leather
 * brown when undyed); its name the vanilla custom name.
 */
public class LOTRPouchItem extends Item implements LOTRTooltipItem {

    /** getPouchColor's undyed brown. */
    public static final int DEFAULT_COLOR = 10841676;

    /** 0 small, 1 medium, 2 large: the original's damage value. */
    private final int size;

    public LOTRPouchItem(int size, Properties properties) {
        super(properties);
        this.size = size;
    }

    public int size() {
        return this.size;
    }

    public static boolean isPouch(ItemStack stack) {
        return stack.getItem() instanceof LOTRPouchItem;
    }

    /** The pouch of this size (0 small, 1 medium, 2 large). */
    public static Item ofSize(int size) {
        return switch (size) {
            case 0 -> LOTRMiscItems.SMALL_POUCH;
            case 1 -> LOTRMiscItems.MEDIUM_POUCH;
            default -> LOTRMiscItems.LARGE_POUCH;
        };
    }

    /** getCapacityForMeta. */
    public static int capacityForSize(int size) {
        return (size + 1) * 9;
    }

    public static int getCapacity(ItemStack stack) {
        return stack.getItem() instanceof LOTRPouchItem pouch ? capacityForSize(pouch.size) : 0;
    }

    /** getMaxPouchCapacity. */
    public static int getMaxPouchCapacity() {
        return capacityForSize(2);
    }

    /** getRandomPouchSize: small 6 in 10, medium 3, large 1. */
    public static int getRandomPouchSize(RandomSource random) {
        float f = random.nextFloat();
        if (f < 0.6f) {
            return 0;
        }
        if (f < 0.9f) {
            return 1;
        }
        return 2;
    }

    public static ItemStack randomPouch(RandomSource random) {
        return new ItemStack(ofSize(getRandomPouchSize(random)));
    }

    public static int getPouchColor(ItemStack stack) {
        return DyedItemColor.getOrDefault(stack, DEFAULT_COLOR) & 0xFFFFFF;
    }

    public static boolean isPouchDyed(ItemStack stack) {
        return stack.has(DataComponents.DYED_COLOR);
    }

    public static void setPouchColor(ItemStack stack, int color) {
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color & 0xFFFFFF));
    }

    /** The pouch's goods, slot for slot. */
    public static NonNullList<ItemStack> getContents(ItemStack pouch) {
        NonNullList<ItemStack> items = NonNullList.withSize(getCapacity(pouch), ItemStack.EMPTY);
        pouch.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
        return items;
    }

    public static void setContents(ItemStack pouch, List<ItemStack> items) {
        pouch.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    /**
     * tryAddItemToPouch: as much of the stack as fits, into stacks of the same already in the pouch
     * and, unless only those are wanted, into its first empty slot. The stack is shrunk by what went
     * in; whether it all went is returned.
     */
    public static boolean tryAddItemToPouch(ItemStack pouch, ItemStack stack, boolean requireMatchInPouch) {
        if (stack.isEmpty() || isPouch(stack)) {
            return false;
        }
        NonNullList<ItemStack> items = getContents(pouch);
        boolean changed = false;
        boolean allIn = false;
        for (int i = 0; i < items.size() && !stack.isEmpty(); ++i) {
            ItemStack inSlot = items.get(i);
            if (inSlot.isEmpty()) {
                if (requireMatchInPouch) {
                    continue;
                }
                items.set(i, stack.copyAndClear());
                changed = true;
                allIn = true;
                break;
            }
            if (inSlot.getCount() >= inSlot.getMaxStackSize() || !inSlot.isStackable()
                    || !ItemStack.isSameItemSameComponents(inSlot, stack)) {
                continue;
            }
            int difference = Math.min(inSlot.getMaxStackSize() - inSlot.getCount(), stack.getCount());
            stack.shrink(difference);
            inSlot.grow(difference);
            changed = true;
            if (stack.isEmpty()) {
                allIn = true;
                break;
            }
        }
        if (changed) {
            setContents(pouch, items);
        }
        return allIn;
    }

    /**
     * restockPouches: everything in the main inventory that a pouch there already holds the like of,
     * moved into it. Whether anything moved is returned.
     */
    public static boolean restockPouches(Player player) {
        Inventory inv = player.getInventory();
        List<Integer> pouchSlots = new ArrayList<>();
        List<Integer> itemSlots = new ArrayList<>();
        NonNullList<ItemStack> main = inv.getNonEquipmentItems();
        for (int i = 0; i < main.size(); ++i) {
            ItemStack stack = main.get(i);
            if (!stack.isEmpty()) {
                (isPouch(stack) ? pouchSlots : itemSlots).add(i);
            }
        }
        boolean movedAny = false;
        for (int j : itemSlots) {
            ItemStack stack = main.get(j);
            for (int p : pouchSlots) {
                int before = stack.getCount();
                tryAddItemToPouch(main.get(p), stack, true);
                if (stack.getCount() != before) {
                    movedAny = true;
                }
            }
            if (stack.isEmpty()) {
                inv.setItem(j, ItemStack.EMPTY);
            }
        }
        if (movedAny) {
            inv.setChanged();
        }
        return movedAny;
    }

    /**
     * fillPouchFromListAndRetainUnfilled: each of the items put in the pouch where it fits; those that
     * do not are left in the list.
     */
    public static void fillPouchFromListAndRetainUnfilled(ItemStack pouch, List<ItemStack> items) {
        List<ItemStack> contents = new ArrayList<>(items);
        items.clear();
        for (ItemStack stack : contents) {
            if (!tryAddItemToPouch(pouch, stack, false)) {
                items.add(stack);
            }
        }
    }

    /** onItemRightClick: the pouch opens. */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            int slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().getSelectedSlot() : Inventory.SLOT_OFFHAND;
            openPouch(serverPlayer, slot);
        }
        return InteractionResult.SUCCESS;
    }

    public static void openPouch(ServerPlayer player, int slot) {
        ItemStack pouch = player.getInventory().getItem(slot);
        player.openMenu(new ExtendedMenuProvider<Integer>() {
            @Override
            public Integer getScreenOpeningData(ServerPlayer p) {
                return slot;
            }

            @Override
            public Component getDisplayName() {
                return pouch.getHoverName();
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player p) {
                return new LOTRPouchMenu(containerId, inv, slot);
            }
        });
    }

    /** addInformation: how many of its slots are in use. */
    @Override
    public void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        int slotsFull = 0;
        for (ItemStack item : getContents(stack)) {
            if (!item.isEmpty()) {
                ++slotsFull;
            }
        }
        builder.accept(Component.translatable("item.lotr.pouch.slots", slotsFull, getCapacity(stack)));
    }
}
